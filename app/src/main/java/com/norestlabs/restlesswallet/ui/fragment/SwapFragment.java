package com.norestlabs.restlesswallet.ui.fragment;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.norestlabs.restlesswallet.R;
import com.norestlabs.restlesswallet.api.ApiClient;
import com.norestlabs.restlesswallet.models.Coin;
import com.norestlabs.restlesswallet.models.CoinModel;
import com.norestlabs.restlesswallet.models.request.ShiftRequest;
import com.norestlabs.restlesswallet.models.response.CoinResponse;
import com.norestlabs.restlesswallet.models.response.MarketInfoResponse;
import com.norestlabs.restlesswallet.ui.TransactionActivity;
import com.norestlabs.restlesswallet.ui.adapter.SwapAdapter;
import com.norestlabs.restlesswallet.utils.Constants;
import com.norestlabs.restlesswallet.utils.Global;
import com.norestlabs.restlesswallet.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.SeekBarProgressChange;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@EFragment(R.layout.fragment_swap)
public class SwapFragment extends Fragment {

    @ViewById
    Spinner spinnerFrom, spinnerTo;

    @ViewById
    ImageView imgSymbolFrom, imgSymbolTo;

    @ViewById
    TextView txtSymbolFrom, txtSymbolTo, txtStatus, txtBalanceFrom, txtBalanceTo;

    @ViewById
    TextView txtExchangeRate, txtSwapMinMax, txtTransactionFee;

    @ViewById
    EditText edtSymbolFrom, edtSymbolTo;

    @ViewById
    SeekBar seekBar;

    @ViewById
    Button btnSend;

    ArrayAdapter adapterFrom;
    SwapAdapter adapterTo;

    private CoinModel coinModel;
    private Coin baseCoin;
    private List<Coin> pairedCoins;
    private String selectedSymbol = "";
    private MarketInfoResponse marketInfo;

    @AfterViews
    void init() {
        coinModel = ((TransactionActivity)getContext()).coinModel;

        adapterFrom = new ArrayAdapter<>(getContext(), R.layout.spinner_item, new String[] {coinModel.getCoin()});
        spinnerFrom.setAdapter(adapterFrom);
        imgSymbolFrom.setImageResource(Utils.getResourceId(getContext(), coinModel.getSymbol().toLowerCase()));
        txtSymbolFrom.setText(coinModel.getSymbol());
        edtSymbolFrom.setHint(coinModel.getSymbol());

        if (Global.swapCoins == null) {
            getSwapCoins();
        } else {
            updateView();
        }
    }

    @Click(R.id.btnSend)
    void onSend() {
        if (TextUtils.isEmpty(edtSymbolFrom.getText()) || TextUtils.isEmpty(edtSymbolTo.getText())) {
            Toast.makeText(getContext(), R.string.swap_amount_empty_error, Toast.LENGTH_SHORT).show();
            return;
        }
        final double fromValue = Double.valueOf(edtSymbolFrom.getText().toString());
        final double toValue = Double.valueOf(edtSymbolTo.getText().toString());
        if (fromValue < marketInfo.getMinimum()) {
            Toast.makeText(getContext(), getString(R.string.swap_amount_min_error, baseCoin.getSymbol(), marketInfo.getMinimum()), Toast.LENGTH_SHORT).show();
        } else if (toValue > marketInfo.getMaxLimit()) {
            Toast.makeText(getContext(), getString(R.string.swap_amount_max_error, selectedSymbol, marketInfo.getMaxLimit()), Toast.LENGTH_SHORT).show();
        } else {
            shift(fromValue);
        }
    }

    @TextChange(R.id.edtSymbolFrom)
    void onFromTextChanged(CharSequence s) {
        if (marketInfo == null) return;
        double amount;
        try {
            amount = Double.valueOf(s.toString());
        } catch (NumberFormatException e) {
            amount = -1;
        }
        if (edtSymbolFrom.hasFocus()) {
            edtSymbolTo.setText(amount < 0 ? "" : String.format(Locale.US, "%.3f", amount * marketInfo.getRate()));
        }
        updateFeeView();
    }

    @TextChange(R.id.edtSymbolTo)
    void onToTextChanged(CharSequence s) {
        if (marketInfo == null) return;
        double amount;
        try {
            amount = Double.valueOf(s.toString());
        } catch (NumberFormatException e) {
            amount = -1;
        }
        if (edtSymbolTo.hasFocus()) {
            edtSymbolFrom.setText(amount < 0 ? "" : String.format(Locale.US, "%.3f", amount / marketInfo.getRate()));
        }
    }

    @SeekBarProgressChange(R.id.seekBar)
    void onProgressChange(int progress) {

    }

    private void updateView() {
        if (!isVisible()) return;
        pairedCoins = new ArrayList<>();
        for (Coin coin : Global.swapCoins) {
            if (coin.getSymbol().equals(coinModel.getSymbol())) {
                baseCoin = coin;
            } else {
                pairedCoins.add(coin);
            }
        }

        if (txtStatus != null) {
            if (baseCoin == null) {
                txtStatus.setText(R.string.unsupported);
                txtStatus.setVisibility(View.VISIBLE);
            } else if (baseCoin.getStatus().equals("unavailable")) {
                txtStatus.setText(R.string.unavailable);
                txtStatus.setVisibility(View.VISIBLE);
            }
        }

        adapterTo = new SwapAdapter(getContext(), R.layout.spinner_item, pairedCoins);
        spinnerTo.setAdapter(adapterTo);
        spinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSymbol = pairedCoins.get(position).getSymbol();

                imgSymbolTo.setImageResource(Utils.getResourceId(getContext(), selectedSymbol.toLowerCase()));
                txtSymbolTo.setText(selectedSymbol);
                edtSymbolTo.setHint(selectedSymbol);

                getMarketInfo();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void updateMarketView() {
        if (!isVisible()) return;
        double rate = 0, min = 0, max = 0;
        if (marketInfo != null) {
            rate = marketInfo.getRate();
            min = marketInfo.getMinimum();
            max = marketInfo.getMaxLimit();
        }
        txtExchangeRate.setText(getString(R.string.exchange_rate_from_shapeshift, baseCoin.getSymbol(), rate, selectedSymbol));
        txtSwapMinMax.setText(getString(R.string.swap_min_max, min, baseCoin.getSymbol(), max, selectedSymbol));
        updateFeeView();
    }

    private void updateFeeView() {
        final double fee = marketInfo == null ? 0 : marketInfo.getMinerFee();
        double amount;
        try {
            amount = Double.valueOf(edtSymbolFrom.getText().toString());
        } catch (NumberFormatException e) {
            amount = 1;
        }
        txtTransactionFee.setText(getString(R.string.transaction_fee, amount, baseCoin.getSymbol(), fee * amount, "USD"));
    }

    private void setEnabled(boolean enabled) {
        if (!isVisible()) return;
        edtSymbolFrom.setEnabled(enabled);
        edtSymbolTo.setEnabled(enabled);
        seekBar.setEnabled(enabled);
        btnSend.setEnabled(enabled);
        btnSend.setTextColor(Color.parseColor(enabled ? "#ffffff" : "#666666"));
    }

    private void showToastMessage(String message) {
        if (isVisible() && ((TransactionActivity)getContext()).getTabPosition() == 2) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    private void getSwapCoins() {
        Call<CoinResponse> call = ApiClient.getInterface(Constants.SHAPESHIFT_URL).getCoins();
        call.enqueue(new Callback<CoinResponse>() {
            @Override
            public void onResponse(Call<CoinResponse> call, Response<CoinResponse> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    injectSwapCoins(response.body());
                } else {
                    showToastMessage(Utils.getErrorStringFromBody(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<CoinResponse> call, Throwable t) {
                showToastMessage(t.getMessage());
            }
        });
    }

    private void injectSwapCoins(CoinResponse response) {
        final List<Coin> coins = new ArrayList<>();
        coins.add(response.getBitcoin());
        coins.add(response.getEthereum());
        coins.add(response.getLitecoin());
        coins.add(response.getNeo());
        Global.swapCoins = coins;

        updateView();
    }

    private void getMarketInfo() {
        if (baseCoin == null) return;
        marketInfo = null;
        Call<MarketInfoResponse> call = ApiClient.getInterface(Constants.SHAPESHIFT_URL).getMarketInfo(baseCoin.getSymbol().toLowerCase()
                + "_" + selectedSymbol.toLowerCase());
        call.enqueue(new Callback<MarketInfoResponse>() {
            @Override
            public void onResponse(Call<MarketInfoResponse> call, Response<MarketInfoResponse> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    marketInfo = response.body();
                    setEnabled(true);
                } else {
                    showToastMessage(Utils.getErrorStringFromBody(response.errorBody()));
                    setEnabled(false);
                }
                updateMarketView();
            }

            @Override
            public void onFailure(Call<MarketInfoResponse> call, Throwable t) {
                showToastMessage(t.getMessage());
                setEnabled(false);
                updateMarketView();
            }
        });
    }

    private void shift(double fromValue) {
        //TODO: set withdrawal and returnAddress parameter
        Call<MarketInfoResponse> call = ApiClient.getInterface(Constants.SHAPESHIFT_URL).shift(new ShiftRequest("", baseCoin.getSymbol().toLowerCase() + "_" + selectedSymbol.toLowerCase(), ""));
        call.enqueue(new Callback<MarketInfoResponse>() {
            @Override
            public void onResponse(Call<MarketInfoResponse> call, Response<MarketInfoResponse> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    if (response.body().getError() != null) {
                        showToastMessage(response.body().getError());
                    } else {
                        //TODO: what to do next
                        showToastMessage("Success!");
                    }
                } else {
                    showToastMessage(Utils.getErrorStringFromBody(response.errorBody()));
                    setEnabled(false);
                }
            }

            @Override
            public void onFailure(Call<MarketInfoResponse> call, Throwable t) {
                showToastMessage(t.getMessage());
                setEnabled(false);
            }
        });
    }
}

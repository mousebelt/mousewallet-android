package com.norestlabs.restlesswallet.ui.fragment;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.norestlabs.restlesswallet.R;
import com.norestlabs.restlesswallet.RWApplication;
import com.norestlabs.restlesswallet.api.ApiClient;
import com.norestlabs.restlesswallet.models.Coin;
import com.norestlabs.restlesswallet.models.CoinModel;
import com.norestlabs.restlesswallet.models.request.ShiftRequest;
import com.norestlabs.restlesswallet.models.response.BitcoinFeeResponse;
import com.norestlabs.restlesswallet.models.response.CoinResponse;
import com.norestlabs.restlesswallet.models.response.EtherChainResponse;
import com.norestlabs.restlesswallet.models.response.MarketInfoResponse;
import com.norestlabs.restlesswallet.models.response.ShiftResponse;
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
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import module.nrlwallet.com.nrlwalletsdk.Coins.NRLBitcoin;
import module.nrlwallet.com.nrlwalletsdk.Coins.NRLEthereum;
import module.nrlwallet.com.nrlwalletsdk.Coins.NRLLite;
import module.nrlwallet.com.nrlwalletsdk.Coins.NRLNeo;
import module.nrlwallet.com.nrlwalletsdk.Coins.NRLStellar;
import module.nrlwallet.com.nrlwalletsdk.abstracts.NRLCallback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@EFragment(R.layout.fragment_swap)
public class SwapFragment extends Fragment implements NRLCallback {

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

    @ViewById
    ProgressBar progressBar;

    ArrayAdapter adapterFrom;
    SwapAdapter adapterTo;

    private CoinModel coinModel;
    private Coin baseCoin;
    private List<Coin> pairedCoins;
    private String selectedSymbol = "";
    private MarketInfoResponse marketInfo;
    double transactionFee[] = {0, 0, 0};

    private class ShiftTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            final double fromValue = Double.valueOf(edtSymbolFrom.getText().toString());
            shift(fromValue);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
        }
    }

    @AfterViews
    void init() {
        final TransactionActivity activity = ((TransactionActivity)getContext());
        coinModel = activity.coinModel;

        adapterFrom = new ArrayAdapter<>(activity, R.layout.spinner_item, new String[] {coinModel.getCoin()});
        spinnerFrom.setAdapter(adapterFrom);
        imgSymbolFrom.setImageResource(Utils.getResourceId(activity, coinModel.getSymbol().toLowerCase()));
        txtSymbolFrom.setText(coinModel.getSymbol());
        edtSymbolFrom.setHint(coinModel.getSymbol());
        txtBalanceFrom.setText(String.valueOf(activity.selectedBalance));

        getTransactionFee();

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
            new ShiftTask().execute();
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
            edtSymbolTo.setText(amount < 0 ? "" : String.format(Locale.US, "%f", amount * marketInfo.getRate()));
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
            edtSymbolFrom.setText(amount < 0 ? "" : String.format(Locale.US, "%f", amount / marketInfo.getRate()));
        }
    }

    @SeekBarProgressChange(R.id.seekBar)
    void onProgressChange(int progress) {
        updateFeeView();
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
                final String pairedBalance;
                switch (selectedSymbol) {
                    case "BTC":
                        pairedBalance = String.valueOf(Global.btcBalance);
                        break;
                    case "ETH":
                        pairedBalance = String.valueOf(Global.ethBalance);
                        break;
                    case "LTC":
                        pairedBalance = String.valueOf(Global.ltcBalance);
                        break;
                    case "NEO":
                        pairedBalance = String.valueOf(Global.neoBalance);
                        break;
                    case "STL":
                        pairedBalance = String.valueOf(Global.stlBalance);
                        break;
                    default:
                        pairedBalance = "0";
                        break;
                }
                txtBalanceTo.setText(pairedBalance);

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
        if (baseCoin == null || edtSymbolFrom == null) return;
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

    private void getTransactionFee() {
        switch (coinModel.getSymbol()) {
            case "BTC"://satoshis
            case "LTC":
                getBTCFee();
                break;
            case "STL"://stroops
                transactionFee[0] = 100;
                transactionFee[1] = 100;
                transactionFee[2] = 100;
                updateFeeView();
                break;
            case "ETH"://wei
                getETHFee();
                break;
            case "NEO":
            default:
                break;
        }
    }

    private void getETHFee() {
        Call<EtherChainResponse> call = ApiClient.getInterface(Constants.ETHERCHAIN_URL).getETHFee();
        call.enqueue(new Callback<EtherChainResponse>() {
            @Override
            public void onResponse(Call<EtherChainResponse> call, Response<EtherChainResponse> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    final EtherChainResponse fee = response.body();
                    if (fee != null) {
                        transactionFee[0] = fee.getSafeLow() * Math.pow(10, 9);
                        transactionFee[1] = fee.getStandard() * Math.pow(10, 9);
                        transactionFee[2] = fee.getFast() * Math.pow(10, 9);
                        updateFeeView();
                    }
                } else {
                    showToastMessage(Utils.getErrorStringFromBody(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<EtherChainResponse> call, Throwable t) {
                showToastMessage(t.getMessage());
            }
        });
    }

    private void getBTCFee() {
        Call<BitcoinFeeResponse> call = ApiClient.getInterface(Constants.BTCFEE_URL).getBTCFee();
        call.enqueue(new Callback<BitcoinFeeResponse>() {
            @Override
            public void onResponse(Call<BitcoinFeeResponse> call, Response<BitcoinFeeResponse> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    final BitcoinFeeResponse fee = response.body();
                    if (fee != null) {
                        transactionFee[0] = fee.getHourFee();
                        transactionFee[1] = fee.getHalfHourFee();
                        transactionFee[2] = fee.getFastestFee();
                        updateFeeView();
                    }
                } else {
                    showToastMessage(Utils.getErrorStringFromBody(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<BitcoinFeeResponse> call, Throwable t) {
                showToastMessage(t.getMessage());
            }
        });
    }

    private void shift(final double amount) {
        final TransactionActivity activity = ((TransactionActivity)getContext());
        String pairedAddress = "";
        switch (selectedSymbol) {
            case "BTC":
                final NRLBitcoin nrlBitcoin = RWApplication.getApp().getBitcoin();
                if (nrlBitcoin != null) {
                    pairedAddress = nrlBitcoin.getAddress();
                }
                break;
            case "ETH":
                final NRLEthereum nrlEthereum = RWApplication.getApp().getEthereum();
                if (nrlEthereum != null) {
                    pairedAddress = nrlEthereum.getAddress();
                }
                break;
            case "LTC":
                final NRLLite nrlLite = RWApplication.getApp().getLitecoin();
                if (nrlLite != null) {
                    pairedAddress = nrlLite.getAddress();
                }
            case "NEO":
                final NRLNeo nrlNeo = RWApplication.getApp().getNeo();
                if (nrlNeo != null) {
                    pairedAddress = nrlNeo.getAddress();
                }
            case "STL":
                final NRLStellar nrlStellar = RWApplication.getApp().getStellar();
                if (nrlStellar != null) {
                    pairedAddress = nrlStellar.getAddress();
                }
                break;
            default:
                break;
        }

        Call<ShiftResponse> call = ApiClient.getInterface(Constants.SHAPESHIFT_URL)
                .shift(new ShiftRequest(pairedAddress, baseCoin.getSymbol().toLowerCase() + "_" + selectedSymbol.toLowerCase(), activity.selectedAddress));
        call.enqueue(new Callback<ShiftResponse>() {
            @Override
            public void onResponse(Call<ShiftResponse> call, Response<ShiftResponse> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    send(amount, response.body().getDeposit());
                } else {
                    progressBar.setVisibility(View.GONE);
                    showToastMessage(Utils.getErrorStringFromBody(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<ShiftResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                showToastMessage(t.getMessage());
            }
        });
    }

    private void send(double amount, String address) {
        //TODO: should set memo
        final String memo = "";
        final long fee = (long)transactionFee[seekBar.getProgress()];

        switch (coinModel.getSymbol()) {
            case "BTC":
                final NRLBitcoin nrlBitcoin = RWApplication.getApp().getBitcoin();
                if (nrlBitcoin != null) {
                    nrlBitcoin.setTransaction((long)(amount * Math.pow(10, 8)), address, this);
                }
                break;
            case "ETH":
                final NRLEthereum nrlEthereum = RWApplication.getApp().getEthereum();
                if (nrlEthereum != null) {
                    final String value = String.valueOf((long)(amount * Math.pow(10, 18)));
                    nrlEthereum.createTransaction(value, address, memo, fee, this);
                }
                break;
            case "LTC":
                final NRLLite nrlLite = RWApplication.getApp().getLitecoin();
                if (nrlLite != null) {
                    nrlLite.createTransaction(String.valueOf(amount), address, memo, fee, this);
                }
                break;
            case "NEO":
                final NRLNeo nrlNeo = RWApplication.getApp().getNeo();
                if (nrlNeo != null) {
                    nrlNeo.createTransaction(amount, address, memo, fee, this);
                }
                break;
            case "STL":
                final NRLStellar nrlStellar = RWApplication.getApp().getStellar();
                if (nrlStellar != null) {
                    nrlStellar.SendTransaction((long)amount, address, this);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onFailure(Throwable t) {
        if (getActivity() == null) return;
        getActivity().runOnUiThread(() -> {
            progressBar.setVisibility(View.GONE);
            showToastMessage(t.getMessage());
        });
    }

    @Override
    public void onResponse(String response) {
        if (getActivity() == null) return;
        getActivity().runOnUiThread(() -> {
            progressBar.setVisibility(View.GONE);
            showToastMessage(response);
        });
    }

    @Override
    public void onResponseArray(JSONArray jsonArray) {
        if (getActivity() == null) return;
        getActivity().runOnUiThread(() -> {
            progressBar.setVisibility(View.GONE);
            showToastMessage(jsonArray.toString());
        });
    }
}

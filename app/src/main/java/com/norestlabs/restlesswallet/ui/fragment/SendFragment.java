package com.norestlabs.restlesswallet.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.norestlabs.restlesswallet.R;
import com.norestlabs.restlesswallet.RWApplication;
import com.norestlabs.restlesswallet.api.ApiClient;
import com.norestlabs.restlesswallet.models.CoinModel;
import com.norestlabs.restlesswallet.models.response.BitcoinFeeResponse;
import com.norestlabs.restlesswallet.models.response.EtherChainResponse;
import com.norestlabs.restlesswallet.models.wallet.EthereumBalance;
import com.norestlabs.restlesswallet.ui.TransactionActivity;
import com.norestlabs.restlesswallet.ui.adapter.BalanceAdapter;
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

@EFragment(R.layout.fragment_send)
public class SendFragment extends Fragment {

    @ViewById
    ImageView imgSymbol, imgArrow;

    @ViewById
    TextView txtSymbol, txtCoin, txtSymbolFrom, txtSymbolTo, txtBalance, txtTransactionFee;

    @ViewById
    Spinner spinnerView;

    @ViewById
    EditText edtAddress, edtSymbolFrom, edtSymbolTo, edtMemo;

    @ViewById
    SeekBar seekBar;

    @ViewById
    Button btnSend;

    BalanceAdapter adapterBalance;

    CoinModel coinModel;
    double conversionRate, balance;
    double transactionFee[] = {0, 0, 0};

    @AfterViews
    void init() {
        final TransactionActivity activity = ((TransactionActivity)getContext());
        coinModel = activity.coinModel;
        balance = activity.selectedBalance;

        if (coinModel.getSymbol().equals("ETH") && Global.ethBalances != null) {
            updateETHView();
        } else {
            imgSymbol.setImageResource(Utils.getResourceId(activity, coinModel.getSymbol().toLowerCase()));
            txtSymbol.setText(coinModel.getSymbol());
            txtCoin.setText("(" + coinModel.getCoin() + ")");
            txtBalance.setText(getString(R.string.current_balance_value, balance, coinModel.getSymbol()));
        }

        edtSymbolFrom.setHint(coinModel.getSymbol());
        edtSymbolTo.setHint("USD");

        getUSDConversionRate();
        getTransactionFee();
    }

    @Click(R.id.btnSend)
    void onSend() {
        if (TextUtils.isEmpty(edtAddress.getText())) {
            Toast.makeText(getContext(), R.string.address_empty_error, Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(edtSymbolFrom.getText()) || TextUtils.isEmpty(edtSymbolTo.getText())) {
            Toast.makeText(getContext(), R.string.swap_amount_empty_error, Toast.LENGTH_SHORT).show();
            return;
        }
        final double fromValue = Double.valueOf(edtSymbolFrom.getText().toString());
        final double toValue = Double.valueOf(edtSymbolTo.getText().toString());
        send(fromValue);
    }

    @TextChange(R.id.edtSymbolFrom)
    void onFromTextChanged(CharSequence s) {
        double amount;
        try {
            amount = Double.valueOf(s.toString());
        } catch (NumberFormatException e) {
            amount = -1;
        }
        if (edtSymbolFrom.hasFocus()) {
            edtSymbolTo.setText(amount < 0 ? "" : String.format(Locale.US, "%.3f", amount * conversionRate));
        }
        updateFeeView();
    }

    @TextChange(R.id.edtSymbolTo)
    void onToTextChanged(CharSequence s) {
        double amount;
        try {
            amount = Double.valueOf(s.toString());
        } catch (NumberFormatException e) {
            amount = -1;
        }
        if (edtSymbolTo.hasFocus() && conversionRate > 0) {
            edtSymbolFrom.setText(amount < 0 ? "" : String.format(Locale.US, "%.3f", amount / conversionRate));
        }
    }

    @SeekBarProgressChange(R.id.seekBar)
    void onProgressChange(int progress) {
        updateFeeView();
    }

    public void scan() {
        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                edtAddress.setText(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void updateETHView() {
        if (Global.ethBalances == null) return;
        imgArrow.setVisibility(View.GONE);
        spinnerView.setVisibility(View.VISIBLE);
        adapterBalance = new BalanceAdapter(getContext(), R.layout.spinner_item, Global.ethBalances, false);
        spinnerView.setAdapter(adapterBalance);
        spinnerView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final EthereumBalance balance = Global.ethBalances.get(position);
                imgSymbol.setImageResource(Utils.getResourceId(getContext(), balance.getSymbol().toLowerCase()));
                txtSymbol.setText(balance.getSymbol());
                txtCoin.setText("(" + Utils.getCoinNameFromSymbol(balance.getSymbol()) + ")");
                txtBalance.setText(getString(R.string.current_balance_value, balance.getBalance(), balance.getSymbol()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void updateFeeView() {
        double amount;
        try {
            amount = Double.valueOf(edtSymbolFrom.getText().toString());
        } catch (NumberFormatException e) {
            amount = 1;
        }
        txtTransactionFee.setText(getString(R.string.transaction_fee, amount, coinModel.getSymbol(), transactionFee[seekBar.getProgress()] * amount, "USD"));
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
        if (isVisible()) Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void getUSDConversionRate() {
        if (Global.marketInfo != null) {
            switch (coinModel.getSymbol()) {
                case "BTC":
                    conversionRate = Global.marketInfo.get(0).getUSDPrice();
                    //TODO: should be removed
                    edtAddress.setText("134JaVTn6ZcjuSnBZaPHdvw7rkZJ8Cwcg9");
                    break;
                case "ETH":
                    conversionRate = Global.marketInfo.get(1).getUSDPrice();
                    //TODO: should be removed
                    edtAddress.setText("0xe9e9bb953200622e8d3ce5a01dd6f608bcde3b41");
                    break;
                case "LTC":
                    conversionRate = Global.marketInfo.get(2).getUSDPrice();
                    //TODO: should be removed
                    edtAddress.setText("LeR3qsGMvP3bzQux7hj3LLoKfdDSHdtFo8");
                case "NEO":
                    conversionRate = Global.marketInfo.get(3).getUSDPrice();
                    //TODO: should be removed
                    edtAddress.setText("AWFwJFrd15MxibXi3oGd6FxQRsvs6G8kna");
                case "STL":
                    conversionRate = Global.marketInfo.get(4).getUSDPrice();
                    //TODO: should be removed
                    edtAddress.setText("GC2SYM6Q67THT5J6BZ4A7FFZ4YZB6COTLIXNYTYAXRIJFEHSB7HPWVUU");
                    break;
                default:
                    return;
            }
            onFromTextChanged(edtSymbolFrom.getText());
        }
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

    private void send(double amount) {
        final String address = edtAddress.getText().toString();
        final String memo = edtMemo.getText().toString();
        final long fee = (long)transactionFee[seekBar.getProgress()];
        switch (coinModel.getSymbol()) {
            case "BTC":
                final NRLBitcoin nrlBitcoin = RWApplication.getApp().getBitcoin();
                if (nrlBitcoin != null) {
                    nrlBitcoin.createTransaction((long)amount, address, new NRLCallback() {
                        @Override
                        public void onFailure(Throwable t) {

                        }

                        @Override
                        public void onResponse(String response) {

                        }

                        @Override
                        public void onResponseArray(JSONArray jsonArray) {

                        }
                    });
                }
                break;
            case "ETH":
                final NRLEthereum nrlEthereum = RWApplication.getApp().getEthereum();
                if (nrlEthereum != null) {
                    nrlEthereum.createTransaction(String.valueOf(amount), address, memo, fee, new NRLCallback() {
                        @Override
                        public void onFailure(Throwable t) {

                        }

                        @Override
                        public void onResponse(String response) {

                        }

                        @Override
                        public void onResponseArray(JSONArray jsonArray) {

                        }
                    });
                }
                break;
            case "LTC":
                final NRLLite nrlLite = RWApplication.getApp().getLitecoin();
                if (nrlLite != null) {
                    nrlLite.createTransaction(String.valueOf(amount), address, memo, fee, new NRLCallback() {
                        @Override
                        public void onFailure(Throwable t) {

                        }

                        @Override
                        public void onResponse(String response) {

                        }

                        @Override
                        public void onResponseArray(JSONArray jsonArray) {

                        }
                    });
                }
                break;
            case "NEO":
                final NRLNeo nrlNeo = RWApplication.getApp().getNeo();
                if (nrlNeo != null) {
                    nrlNeo.createTransaction(amount, address, memo, fee, new NRLCallback() {
                        @Override
                        public void onFailure(Throwable t) {

                        }

                        @Override
                        public void onResponse(String response) {

                        }

                        @Override
                        public void onResponseArray(JSONArray jsonArray) {

                        }
                    });
                }
                break;
            case "STL":
                final NRLStellar nrlStellar = RWApplication.getApp().getStellar();
                if (nrlStellar != null) {
                    nrlStellar.createTransaction((long)amount, address, new NRLCallback() {
                        @Override
                        public void onFailure(Throwable t) {

                        }

                        @Override
                        public void onResponse(String response) {

                        }

                        @Override
                        public void onResponseArray(JSONArray jsonArray) {

                        }
                    });
                }
                break;
            default:
                break;
        }
    }
}

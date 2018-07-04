package com.norestlabs.restlesswallet.ui.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.norestlabs.restlesswallet.R;
import com.norestlabs.restlesswallet.models.CoinModel;
import com.norestlabs.restlesswallet.models.wallet.Transaction;
import com.norestlabs.restlesswallet.ui.TransactionActivity;
import com.norestlabs.restlesswallet.utils.QRGenerator;
import com.norestlabs.restlesswallet.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EFragment(R.layout.fragment_receive)
public class ReceiveFragment extends Fragment {

    @ViewById
    ImageView imgQRCode;

    @ViewById
    View pbQRCode;

    @ViewById
    ImageView imgSymbol;

    @ViewById
    TextView txtQRCode, txtBalance;

    CoinModel coinModel;
    String address;
    double balance;
    List<Transaction> transactions;
    private QRGenerator qrGenerator;

    @AfterViews
    void init() {
        final TransactionActivity activity = ((TransactionActivity)getContext());
        coinModel = activity.coinModel;
        address = activity.selectedAddress;
        balance = activity.selectedBalance;
        transactions = activity.selectedTransactions;

        imgSymbol.setImageResource(Utils.getResourceId(activity, coinModel.getSymbol().toLowerCase()));
        txtBalance.setText(getString(R.string.current_balance_value, balance, coinModel.getSymbol()));

        if (address != null) {
            showQRCode(address);
            txtQRCode.setText(address);
        }
    }

    @Click(R.id.btnCopy)
    void onCopy() {
        if (address == null) return;
        final ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        final ClipData clip = ClipData.newPlainText("Wallet Address", address);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getContext(), R.string.alert_copied, Toast.LENGTH_SHORT).show();
    }

    private void showQRCode(final String qrCode) {
        if (qrGenerator != null && qrGenerator.getStatus() == AsyncTask.Status.RUNNING) {
            qrGenerator.cancel(true);
        }

        final int width = getResources().getDimensionPixelSize(R.dimen.qrcode_size) / 2;
        final int height = getResources().getDimensionPixelSize(R.dimen.qrcode_size) / 2;

        qrGenerator = new QRGenerator(width, height, new QRGenerator.QRGeneratorListener() {
            @Override
            public void qrGenerationStarted() {
                pbQRCode.setVisibility(View.VISIBLE);
            }

            @Override
            public void qrGenerated(Bitmap bitmap) {
                if (pbQRCode != null) pbQRCode.setVisibility(View.GONE);
                if (imgQRCode != null) imgQRCode.setImageBitmap(bitmap);
            }
        });
        qrGenerator.execute(qrCode);
    }
}

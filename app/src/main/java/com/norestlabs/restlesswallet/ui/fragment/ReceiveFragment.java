package com.norestlabs.restlesswallet.ui.fragment;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;

import com.norestlabs.restlesswallet.R;
import com.norestlabs.restlesswallet.utils.QRGenerator;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_receive)
public class ReceiveFragment extends Fragment {

    @ViewById
    ImageView imgQRCode;

    @ViewById
    View pbQRCode;

    private QRGenerator qrGenerator;

    @AfterViews
    void init() {
        showQRCode("56&VZB09SD97S867S%6879S809A0GE9876A5F68A654A7867ASB568H");
    }

    private void showQRCode(String qrCode) {
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
                pbQRCode.setVisibility(View.GONE);
                imgQRCode.setImageBitmap(bitmap);
            }
        });
        qrGenerator.execute(qrCode);
    }
}

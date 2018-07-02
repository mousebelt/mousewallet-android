package com.norestlabs.restlesswallet.api;

import android.content.Context;

import com.norestlabs.restlesswallet.R;
import com.norestlabs.restlesswallet.utils.Utils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ConnectivityInterceptor implements Interceptor {

    private Context mContext;

    ConnectivityInterceptor(Context context) {
        mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (!Utils.isOnline(mContext)) {
            throw new NoConnectivityException();
        }

        Request.Builder builder = chain.request().newBuilder();
        return chain.proceed(builder.build());
    }

    class NoConnectivityException extends IOException {

        @Override
        public String getMessage() {
            return mContext.getString(R.string.no_internet_connection);
        }

    }
}

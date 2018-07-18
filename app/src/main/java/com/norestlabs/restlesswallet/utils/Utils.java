package com.norestlabs.restlesswallet.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;
import com.norestlabs.restlesswallet.models.response.ErrorResponse;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Random;

import module.nrlwallet.com.nrlwalletsdk.Utils.MnemonicToSeed;
import okhttp3.ResponseBody;

public class Utils {

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnected());
    }

    public static String[] randomArray(String[] arr) {
        final String[] randomArr = arr.clone();
        Random rnd = new Random();
        for (int i = randomArr.length - 1; i > 0; i --) {
            final int index = rnd.nextInt(i + 1);
            String a = randomArr[index];
            randomArr[index] = randomArr[i];
            randomArr[i] = a;
        }
        return randomArr;
    }

    public static String arrayListToString(List<String> array) {
        String result = "";
        for (String item : array) {
            result = result.concat(" " + item);
        }
        return result.substring(1);
    }

    public static byte[] stringToBytes(String string) {
        try {
            if (string == null) return null;
            return string.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String generateSeed(String strMnemonic) {
        final String seed = new MnemonicToSeed().calculateSeed(strMnemonic, "");
        return seed;
    }

    public static byte[] generateBSeed(String strMnemonic) {
        final byte[] bSeed = new MnemonicToSeed().calculateSeedByte(strMnemonic, "");
        return bSeed;
    }

    public static int getResourceId(Context context, String name) {
        return context.getResources().getIdentifier(name, "mipmap", context.getPackageName());
    }

    public static String getErrorStringFromBody(ResponseBody responseBody) {
        if (responseBody == null) return "Unexpected error";
        try {
            final String error = responseBody.string();
            if (error.contains("{")) {
                ErrorResponse response = new Gson().fromJson(error, ErrorResponse.class);
                if (response == null) {
                    return error;
                } else {
                    return response.getError();
                }
            } else {
                return error;
            }
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    public static String getCoinNameFromSymbol(final String symbol) {
        switch (symbol) {
            case "BTC":
                return "Bitcoin";
            case "ETH":
                return "Ethereum";
            case "LTC":
                return "Litecoin";
            case "NEO":
                return "Neo";
            case "OMG":
                return "OmiseGo";
            case "STL":
                return "Stellar";
            default:
                return "";
        }
    }
}

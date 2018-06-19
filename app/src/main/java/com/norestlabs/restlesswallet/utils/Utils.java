package com.norestlabs.restlesswallet.utils;

import java.util.List;
import java.util.Random;

import module.nrlwallet.com.nrlwalletsdk.Utils.MnemonicToSeed;

public class Utils {

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

    public static String generateSeed(String strMnemonic) {
        final byte[] bseed = new MnemonicToSeed().calculateSeedByte(strMnemonic, "");
        final String seed = new MnemonicToSeed().calculateSeed(strMnemonic, "");
        return seed;
    }
}

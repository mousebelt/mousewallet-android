package com.norestlabs.restlesswallet.utils;

import java.util.Random;

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
}

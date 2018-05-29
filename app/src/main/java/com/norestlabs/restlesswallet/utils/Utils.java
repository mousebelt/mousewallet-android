package com.norestlabs.restlesswallet.utils;

import java.util.Random;

public class Utils {

    public static void randomArray(String[] arr) {
        Random rnd = new Random();
        for (int i = arr.length - 1; i > 0; i --) {
            final int index = rnd.nextInt(i + 1);
            String a = arr[index];
            arr[index] = arr[i];
            arr[i] = a;
        }
    }
}

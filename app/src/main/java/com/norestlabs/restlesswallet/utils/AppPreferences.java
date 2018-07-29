package com.norestlabs.restlesswallet.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import tgio.rncryptor.RNCryptorNative;

public class AppPreferences {

    public static final String PREFERENCE_NAME = "RESTLESS_WALLET_PREFERENCE";
    private static final String PREFERENCE_MNEMONIC = "MNEMONIC";
    private static final String PREFERENCE_IS_SAME_MNEMONIC = "IS_SAME_MNEMONIC";
    private static final String PREFERENCE_PINCODE = "PINCODE";

    private static final String CHARSET = "UTF-8";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String KEY_TRANSFORMATION = "AES/ECB/PKCS5Padding";
    private static final String SECRET_KEY_HASH_TRANSFORMATION = "SHA-256";
    private final Cipher keyWriter;

    private final SharedPreferences preferences;

    private String pincode;

    /**
     * This will initialize an instance of the AppPreferences class
     *
     * @param context           your current context
     * @param preferenceName    name of preferences file (preferenceName.xml)
     */
    public AppPreferences(Context context, String preferenceName) {
        this.preferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);

        try {
            keyWriter = Cipher.getInstance(KEY_TRANSFORMATION);
            initCiphers("restless-wallet");
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private void initCiphers(String secureKey) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec secretKey = getSecretKey(secureKey);

        keyWriter.init(Cipher.ENCRYPT_MODE, secretKey);
    }

    private SecretKeySpec getSecretKey(String key) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] keyBytes = createKeyBytes(key);
        return new SecretKeySpec(keyBytes, TRANSFORMATION);
    }

    private byte[] createKeyBytes(String key) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(SECRET_KEY_HASH_TRANSFORMATION);
        md.reset();
        final byte[] keyBytes = md.digest(key.getBytes(CHARSET));
        return keyBytes;
    }

    public boolean setPin(String value) {
        final String encrypted = toKey(value);
        if (encrypted == null) {
            preferences.edit().remove(PREFERENCE_PINCODE).apply();
            pincode = null;
            return false;
        } else {
            preferences.edit().putString(PREFERENCE_PINCODE, encrypted).apply();
            return true;
        }
    }

    public String getPin() {
        if (pincode == null) {
            pincode = preferences.getString(PREFERENCE_PINCODE, null);
        }
        return pincode;
    }

    public boolean isPincodeMatch(String pincode) {
        final String encrypted = toKey(pincode);
        final String key = getPin();
        return encrypted != null && key != null && encrypted.contentEquals(key);
    }

    public boolean setMnemonic(String value) {
        final String key = getPin();
        if (value == null || key == null) {
            preferences.edit().remove(PREFERENCE_MNEMONIC).apply();
            return false;
        } else {
            final String oldMnemonic = getMnemonic();
            preferences.edit().putBoolean(PREFERENCE_IS_SAME_MNEMONIC, oldMnemonic != null && value.equals(oldMnemonic)).apply();
            preferences.edit().putString(PREFERENCE_MNEMONIC, encrypt(value, key)).apply();
            return true;
        }
    }

    public String getMnemonic() {
        final String encrypted = preferences.getString(PREFERENCE_MNEMONIC, null);
        final String key = getPin();
        if (encrypted == null || key == null) {
            return null;
        } else {
            //TODO: should be removed
//            return "target crater noble virus album surge kidney tennis snow click faculty robust";//for ETH Test
            return "garbage alone hidden dizzy account novel essay cotton nephew first vital drink";//for LTC Test
//            return decrypt(encrypted, key);
        }
    }

    public boolean isSameMnemonic() {
        return preferences.getBoolean(PREFERENCE_IS_SAME_MNEMONIC, false);
    }

    private String toKey(String pincode) {
        return encrypt(pincode, keyWriter);
    }

    private String encrypt(String value, Cipher writer) {
        byte[] secureValue;
        try {
            if (value == null) {
                return null;
            } else {
                secureValue = convert(writer, value.getBytes(CHARSET));
            }
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        if (secureValue == null) {
            return null;
        } else {
            return Base64.encodeToString(secureValue, Base64.NO_WRAP);
        }
    }

    private String decrypt(String securedEncodedValue, Cipher reader) {
        byte[] securedValue = Base64.decode(securedEncodedValue, Base64.NO_WRAP);
        byte[] value = convert(reader, securedValue);
        if (value == null) {
            return null;
        } else {
            try {
                return new String(value, CHARSET);
            } catch (UnsupportedEncodingException e) {
                return null;
            }
        }
    }

    private static byte[] convert(Cipher cipher, byte[] bs) {
        try {
            return cipher.doFinal(bs);
        } catch (Exception e) {
            return null;
        }
    }

    private String encrypt(String value, String key) {
        RNCryptorNative rncryptor = new RNCryptorNative();
        try {
            return new String(rncryptor.encrypt(value, key), CHARSET);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    private String decrypt(String value, String key) {
        RNCryptorNative rncryptor = new RNCryptorNative();
        return rncryptor.decrypt(value, key);
    }

}

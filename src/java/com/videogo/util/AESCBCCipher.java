/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.util;

import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESCBCCipher {
    private static final String HEX = "0123456789ABCDEF";
    private static byte[] iv = "0123456789ABCDEF".getBytes();
    private static IvParameterSpec IV = new IvParameterSpec(iv);
    private static final String CIPHERMODEPADDING = "AES/CBC/PKCS5Padding";

    public static String encrypt(String key, String src) throws Exception {
        byte[] rawKey = AESCBCCipher.getRawKey(key);
        byte[] result = AESCBCCipher.encrypt(rawKey, src.getBytes());
        return AESCBCCipher.toHex(result);
    }

    public static String decrypt(String key, String encrypted) throws Exception {
        byte[] rawKey = AESCBCCipher.getRawKey(key);
        byte[] enc = AESCBCCipher.toByte(encrypted);
        byte[] result = AESCBCCipher.decrypt(rawKey, enc);
        return new String(result);
    }

    private static byte[] getRawKey(String key) throws Exception {
        SecretKeySpec keys = new SecretKeySpec(key.getBytes(), "AES");
        byte[] raw = keys.getEncoded();
        return raw;
    }

    private static byte[] encrypt(byte[] key, byte[] src) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance(CIPHERMODEPADDING);
        cipher.init(1, (Key)skeySpec, IV);
        byte[] encrypted = cipher.doFinal(src);
        return encrypted;
    }

    private static byte[] decrypt(byte[] key, byte[] encrypted) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance(CIPHERMODEPADDING);
        cipher.init(2, (Key)skeySpec, IV);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    public static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; ++i) {
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
        }
        return result;
    }

    public static String toHex(byte[] buf) {
        if (buf == null) {
            return "";
        }
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; ++i) {
            AESCBCCipher.appendHex(result, buf[i]);
        }
        return result.toString();
    }

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt(b >> 4 & 0xF)).append(HEX.charAt(b & 0xF));
    }
}


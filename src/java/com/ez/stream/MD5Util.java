/*
 * Decompiled with CFR 0.152.
 */
package com.ez.stream;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    protected static char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    protected static MessageDigest messagedigest = null;

    public static String getMD5String(String s) {
        if (s == null) {
            return null;
        }
        return MD5Util.getMD5String(s.getBytes());
    }

    public static boolean checkPassword(String password, String md5PwdStr) {
        String s = MD5Util.getMD5String(password);
        return s.equals(md5PwdStr);
    }

    public static synchronized String getMD5String(byte[] bytes) {
        try {
            messagedigest.reset();
            messagedigest.update(bytes);
            return MD5Util.bufferToHex(messagedigest.digest());
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String bufferToHex(byte[] bytes) {
        if (bytes.length == 0) {
            return "";
        }
        return MD5Util.bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte[] bytes, int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; ++l) {
            MD5Util.appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 0xF0) >> 4];
        char c1 = hexDigits[bt & 0xF];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

    public static String md5Crypto(String src) {
        char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] btInput = src.getBytes();
            messagedigest.reset();
            messagedigest.update(btInput);
            byte[] md = messagedigest.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; ++i) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xF];
                str[k++] = hexDigits[byte0 & 0xF];
            }
            return new String(str);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static {
        try {
            messagedigest = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException nsaex) {
            nsaex.printStackTrace();
        }
    }
}


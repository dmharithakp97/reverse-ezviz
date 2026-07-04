/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 *  android.util.Base64
 */
package com.videogo.util;

import android.text.TextUtils;
import android.util.Base64;
import com.videogo.util.LogUtil;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESCipher {
    private static final String HEX = "0123456789ABCDEF";

    public static String decrypt_URL_SAFE(String key, String encrypted) {
        if (TextUtils.isEmpty((CharSequence)encrypted)) {
            return encrypted;
        }
        try {
            byte[] enc = Base64.decode((String)encrypted, (int)8);
            byte[] result = AESCipher.decryptt_URL_SAFE(key.getBytes(), enc);
            return new String(result);
        }
        catch (Exception e) {
            LogUtil.printErrStackTrace("AESCipher", e.fillInStackTrace());
            return null;
        }
    }

    private static byte[] decryptt_URL_SAFE(byte[] key, byte[] encrypted) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
        cipher.init(2, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    private static byte[] decrypt(byte[] key, byte[] encrypted) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(2, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    public static String decrypt(String key, String encrypted) throws Exception {
        byte[] rawKey = AESCipher.getRawKey(key);
        byte[] enc = AESCipher.toByte(encrypted);
        byte[] result = AESCipher.decrypt(rawKey, enc);
        return new String(result);
    }

    private static byte[] getRawKey(String key) throws Exception {
        SecretKeySpec keys = new SecretKeySpec(key.getBytes(), "AES");
        byte[] raw = keys.getEncoded();
        return raw;
    }

    public static String toHex(String txt) {
        return AESCipher.toHex(txt.getBytes());
    }

    public static String fromHex(String hex) {
        return new String(AESCipher.toByte(hex));
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
            AESCipher.appendHex(result, buf[i]);
        }
        return result.toString();
    }

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt(b >> 4 & 0xF)).append(HEX.charAt(b & 0xF));
    }

    public static String parseByte2HexStr(byte[] buf) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; ++i) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; ++i) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte)(high * 16 + low);
        }
        return result;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.util;

import com.videogo.util.LogUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
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

    public static String getMD5String16(String s) {
        return MD5Util.getMD5String(s).substring(8, 24).toLowerCase();
    }

    public static boolean checkPassword(String password, String md5PwdStr) {
        String s = MD5Util.getMD5String(password);
        return s.equals(md5PwdStr);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static String getFileMD5String(File file) throws IOException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            byte[] buffer = new byte[4096];
            int numRead = 0;
            while ((numRead = ((InputStream)fis).read(buffer, 0, 4096)) > 0) {
                messagedigest.update(buffer, 0, numRead);
            }
        }
        finally {
            if (fis != null) {
                try {
                    ((InputStream)fis).close();
                    fis = null;
                }
                catch (IOException e) {
                    LogUtil.printErrStackTrace("MD5Util", e.fillInStackTrace());
                }
            }
        }
        return MD5Util.bufferToHex(messagedigest.digest());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void main(String[] args) {
        String fileMD5 = "";
        if (args == null || args[0] == null) {
            return;
        }
        File file = new File(args[0]);
        try {
            fileMD5 = MD5Util.getFileMD5String(file);
        }
        catch (IOException e) {
            LogUtil.printErrStackTrace("MD5Util", e.fillInStackTrace());
        }
        File md5File = new File("MD5.txt");
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(md5File);
            ((OutputStream)outputStream).write(fileMD5.getBytes());
        }
        catch (Exception e) {
            LogUtil.printErrStackTrace("MD5Util", e.fillInStackTrace());
        }
        finally {
            if (outputStream != null) {
                try {
                    ((OutputStream)outputStream).close();
                }
                catch (IOException e) {
                    LogUtil.printErrStackTrace("MD5Util", e.fillInStackTrace());
                }
            }
        }
    }

    public static String getFileMD5String_old(File file) throws IOException {
        FileInputStream in = new FileInputStream(file);
        FileChannel ch = in.getChannel();
        MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0L, file.length());
        messagedigest.update(byteBuffer);
        return MD5Util.bufferToHex(messagedigest.digest());
    }

    public static String getMD5String(byte[] bytes) {
        messagedigest.update(bytes);
        return MD5Util.bufferToHex(messagedigest.digest());
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
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
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
            LogUtil.printErrStackTrace("MD5Util", e.fillInStackTrace());
            return null;
        }
    }

    static {
        try {
            messagedigest = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e) {
            LogUtil.printErrStackTrace("MD5Util", e.fillInStackTrace());
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 */
package com.ez.stream;

import android.text.TextUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {
    public static String byteArray2String(byte[] data) {
        if (data == null) {
            return null;
        }
        StringBuilder bld = new StringBuilder();
        for (byte d : data) {
            if (d == 0) break;
            bld.append((char)d);
        }
        return bld.toString();
    }

    public static String getFileName(String secret, String devSerial, int channelIndex) {
        StringBuilder sb = new StringBuilder();
        sb.append("/");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.CHINA);
        sb.append(formatter.format(new Date()));
        sb.append("_");
        sb.append("dev_");
        sb.append(devSerial);
        sb.append("_");
        sb.append("chn_");
        sb.append(channelIndex);
        if (!TextUtils.isEmpty((CharSequence)secret)) {
            sb.append("_");
            sb.append("key_");
            sb.append(secret);
        }
        return sb.toString();
    }
}


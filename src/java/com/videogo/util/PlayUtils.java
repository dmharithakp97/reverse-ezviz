/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 */
package com.videogo.util;

import android.text.TextUtils;
import com.videogo.openapi.BaseAPI;
import com.videogo.openapi.bean.EZPlayURLParams;
import com.videogo.util.AESCipher;
import com.videogo.util.LogUtil;
import com.videogo.util.Utils;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class PlayUtils {
    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static EZPlayURLParams getEZPlayURLParams(String url) {
        String verifyCode;
        EZPlayURLParams urlparams = null;
        if (!Utils.isEZOpenProtocol(url)) return urlparams;
        urlparams = new EZPlayURLParams();
        String stringkey = url.replace("ezopen://", "");
        String[] stringKeys = stringkey.split("@");
        if (stringKeys.length > 1 && !TextUtils.isEmpty((CharSequence)(verifyCode = PlayUtils.getVerifyCode(stringKeys[0])))) {
            urlparams.verifyCode = verifyCode;
        }
        String hosts = "";
        hosts = stringKeys.length > 1 ? stringKeys[1] : stringKeys[0];
        String[] strings = hosts.split("/");
        if (strings.length < 3) {
            return null;
        }
        urlparams.host = strings[0];
        urlparams.deviceSerial = strings[1];
        String param = strings[2];
        String[] s1 = param.split("\\?");
        LogUtil.d("EZPlayURLParams", s1[0]);
        String[] s2 = s1[0].split("\\.");
        if (s2.length < 2) {
            return null;
        }
        urlparams.cameraNo = s2[0];
        if (s2.length == 2) {
            if (s2[1].equalsIgnoreCase("live")) {
                urlparams.type = 1;
                urlparams.videoLevel = 1;
            } else {
                if (!s2[1].equalsIgnoreCase("rec")) return null;
                urlparams.type = 2;
            }
        } else if (s2[2].equalsIgnoreCase("live")) {
            urlparams.type = 1;
            urlparams.videoLevel = s2[1].equalsIgnoreCase("HD") ? 2 : 1;
        } else {
            if (!s2[2].equalsIgnoreCase("rec")) return null;
            urlparams.type = 2;
            if (s2[1].equalsIgnoreCase("cloud")) {
                urlparams.recodeType = 2;
            } else if (s2[1].equalsIgnoreCase("local")) {
                urlparams.recodeType = 1;
            }
        }
        if (s1.length <= 1) return urlparams;
        Map<String, String[]> paramsMap = PlayUtils.getParamsMap(s1[1], "utf-8");
        if (paramsMap.containsKey("mute")) {
            urlparams.mute = paramsMap.get("mute").equals("true");
        }
        if (paramsMap.containsKey("begin")) {
            // empty if block
        }
        if (!paramsMap.containsKey("end")) return urlparams;
        return urlparams;
    }

    private static Map<String, String[]> getParamsMap(String queryString, String enc) {
        HashMap<String, String[]> paramsMap = new HashMap<String, String[]>();
        if (queryString != null && queryString.length() > 0) {
            int ampersandIndex;
            int lastAmpersandIndex = 0;
            do {
                String[] newValues;
                String subStr;
                if ((ampersandIndex = queryString.indexOf(38, lastAmpersandIndex) + 1) > 0) {
                    subStr = queryString.substring(lastAmpersandIndex, ampersandIndex - 1);
                    lastAmpersandIndex = ampersandIndex;
                } else {
                    subStr = queryString.substring(lastAmpersandIndex);
                }
                String[] paramPair = subStr.split("=");
                String param = paramPair[0];
                String value = paramPair.length == 1 ? "" : paramPair[1];
                try {
                    value = URLDecoder.decode(value, enc);
                }
                catch (UnsupportedEncodingException unsupportedEncodingException) {
                    // empty catch block
                }
                if (paramsMap.containsKey(param)) {
                    String[] values = (String[])paramsMap.get(param);
                    int len = values.length;
                    newValues = new String[len + 1];
                    System.arraycopy(values, 0, newValues, 0, len);
                    newValues[len] = value;
                } else {
                    newValues = new String[]{value};
                }
                paramsMap.put(param, newValues);
            } while (ampersandIndex > 0);
        }
        return paramsMap;
    }

    private static String getVerifyCode(String string) {
        if (TextUtils.isEmpty((CharSequence)string)) {
            return null;
        }
        String[] keys = string.split(":");
        if (keys.length > 1 && "AES".equals(keys[0])) {
            try {
                return AESCipher.decrypt(BaseAPI.getInstance().getAppKey(), keys[1]);
            }
            catch (Exception e) {
                e.printStackTrace();
                return keys[1];
            }
        }
        return string;
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 *  android.text.TextUtils
 */
package com.ezviz.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.ezviz.sdk.configwifi.common.LogUtil;
import java.security.MessageDigest;
import java.util.UUID;

public class AppUtil {
    private static String TAG = "AppUtil";
    private Context mContext = null;
    private static final String EMPTY_STRING = "";
    private static final String WIFI_CONFIG_DEVICEID_STRING = "WIFI_CONFIG_DEVICEID_STRING";
    private static AppUtil instance = null;
    private SharedPreferences mSharePreferences;
    private SharedPreferences.Editor mEditor;

    private AppUtil() {
    }

    public static synchronized AppUtil getInstance(Context context) {
        if (instance == null) {
            instance = new AppUtil();
            AppUtil.instance.mContext = context;
            AppUtil.instance.mSharePreferences = context.getSharedPreferences("VideoGo", 0);
            AppUtil.instance.mEditor = AppUtil.instance.mSharePreferences.edit();
        }
        return instance;
    }

    public String getTerminalId() {
        String terminalId = instance.getStringFromSp(WIFI_CONFIG_DEVICEID_STRING);
        if (TextUtils.isEmpty((CharSequence)terminalId)) {
            terminalId = this.getRandomUUID();
            this.setStringToSp(WIFI_CONFIG_DEVICEID_STRING, terminalId);
        }
        return terminalId;
    }

    private String getRandomUUID() {
        String terminalId = null;
        String uuid = UUID.randomUUID().toString();
        if (!TextUtils.isEmpty((CharSequence)(uuid = uuid.replaceAll("\\-", EMPTY_STRING)))) {
            terminalId = this.md5Crypto(uuid);
        }
        if (!TextUtils.isEmpty(terminalId)) {
            terminalId = this.md5Crypto(terminalId);
        }
        if (!TextUtils.isEmpty((CharSequence)terminalId)) {
            LogUtil.d(TAG, "getTerminalId: " + terminalId);
        }
        return terminalId;
    }

    private String md5Crypto(String src) {
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
            e.printStackTrace();
            return null;
        }
    }

    private void setStringToSp(String key, String value) {
        if (this.mSharePreferences == null) {
            LogUtil.e(TAG, "failed to save " + key + "(" + value + ")");
            return;
        }
        this.mSharePreferences.edit().putString(key, value).apply();
    }

    private String getStringFromSp(String key) {
        if (this.mSharePreferences == null) {
            LogUtil.e(TAG, "failed to get " + key);
            return null;
        }
        return this.mSharePreferences.getString(key, EMPTY_STRING);
    }
}


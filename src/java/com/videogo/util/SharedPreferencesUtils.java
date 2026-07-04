/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 *  android.text.TextUtils
 *  android.util.Log
 *  com.google.gson.Gson
 */
package com.videogo.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.videogo.ecdh.EcdhKeyInfo;

public class SharedPreferencesUtils {
    private static final String TAG = "SharedPreferencesUtils";
    public static final String INIT_TIME = "init_time";
    public static final String OPEN_DEBUG = "open_debug";
    public static final String INIT_PER_MINUTE = "init_per_minute";
    public static final String UPLOAD_LOG = "upload_log";
    private static final String ECDH_KEY_INFO = "ecdh_key_info";
    private boolean isUpLoadLog = false;

    private static SharedPreferences getSharedPreferences(Context context) {
        SharedPreferences sharedPreference = null;
        sharedPreference = context.getSharedPreferences("videoGo", 0);
        return sharedPreference;
    }

    public static long getInitTime(Context context) {
        SharedPreferences pref = SharedPreferencesUtils.getSharedPreferences(context);
        long mInitTime = pref.getLong(INIT_TIME, -1L);
        if (mInitTime == -1L) {
            mInitTime = System.currentTimeMillis();
            SharedPreferencesUtils.setInitTime(context, mInitTime);
        }
        return mInitTime;
    }

    public static void setInitTime(Context context, long initTime) {
        SharedPreferences pref = SharedPreferencesUtils.getSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(INIT_TIME, initTime);
        editor.commit();
    }

    public static int getInitCountPerMinute(Context context) {
        SharedPreferences pref = SharedPreferencesUtils.getSharedPreferences(context);
        int mInitCountPerMinute = pref.getInt(INIT_PER_MINUTE, 0);
        SharedPreferencesUtils.setInitCountPerMinute(context, ++mInitCountPerMinute);
        Log.d((String)TAG, (String)("getInitCountPerMinute = " + mInitCountPerMinute));
        return mInitCountPerMinute;
    }

    public static void setInitCountPerMinute(Context context, int InitCountPerMinute) {
        SharedPreferences pref = SharedPreferencesUtils.getSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(INIT_PER_MINUTE, InitCountPerMinute);
        editor.commit();
    }

    public static boolean isUploadLog(Context context) {
        SharedPreferences pref = SharedPreferencesUtils.getSharedPreferences(context);
        boolean uploadlog = pref.getBoolean(UPLOAD_LOG, false);
        return uploadlog;
    }

    public static void setUploadLog(Context context, boolean uploadlog) {
        SharedPreferences pref = SharedPreferencesUtils.getSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(UPLOAD_LOG, uploadlog);
        editor.commit();
    }

    public static boolean isOpenDebug(Context context) {
        SharedPreferences pref = SharedPreferencesUtils.getSharedPreferences(context);
        boolean uploadlog = pref.getBoolean(OPEN_DEBUG, false);
        return uploadlog;
    }

    public static void setOpenDebug(Context context, boolean debug) {
        SharedPreferences pref = SharedPreferencesUtils.getSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(OPEN_DEBUG, debug);
        editor.commit();
    }

    public static void setEcdhKeyInfo(Context context, EcdhKeyInfo ezEcdhKeyInfo) {
        SharedPreferences pref = SharedPreferencesUtils.getSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(ECDH_KEY_INFO, new Gson().toJson((Object)ezEcdhKeyInfo));
        editor.commit();
    }

    public static EcdhKeyInfo getEcdhKeyInfo(Context context) {
        SharedPreferences pref = SharedPreferencesUtils.getSharedPreferences(context);
        String value = pref.getString(ECDH_KEY_INFO, null);
        if (!TextUtils.isEmpty((CharSequence)value)) {
            return (EcdhKeyInfo)new Gson().fromJson(value, EcdhKeyInfo.class);
        }
        return null;
    }
}


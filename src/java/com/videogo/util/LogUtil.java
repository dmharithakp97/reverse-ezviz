/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package com.videogo.util;

import android.util.Log;
import com.videogo.constant.Config;

public class LogUtil {
    private static LogCallback mLogCallback;

    public static void setLogCallback(LogCallback callback) {
        mLogCallback = callback;
    }

    public static void d(String tag, String content) {
        if (Config.LOGGING) {
            Log.d((String)tag, (String)content);
        }
        if (mLogCallback != null) {
            mLogCallback.onLog(tag, content);
        }
    }

    public static void e(String tag, String content) {
        Log.e((String)tag, (String)content);
        if (mLogCallback != null) {
            mLogCallback.onLog(tag, content);
        }
    }

    public static void i(String tag, String content) {
        Log.i((String)tag, (String)content);
        if (mLogCallback != null) {
            mLogCallback.onLog(tag, content);
        }
    }

    public static void v(String tag, String content) {
        if (Config.LOGGING) {
            Log.v((String)tag, (String)content);
        }
        if (mLogCallback != null) {
            mLogCallback.onLog(tag, content);
        }
    }

    public static void w(String tag, String content) {
        if (Config.LOGGING) {
            Log.w((String)tag, (String)content);
        }
        if (mLogCallback != null) {
            mLogCallback.onLog(tag, content);
        }
    }

    public static void d(String tag, String content, Exception e) {
        if (Config.LOGGING) {
            Log.d((String)tag, (String)content, (Throwable)e);
        }
        if (mLogCallback != null) {
            mLogCallback.onLog(tag, content);
        }
    }

    public static void printErrStackTrace(String tag, Throwable throwable) {
        if (Config.LOGGING && throwable != null && throwable.getMessage() != null) {
            Log.d((String)tag, (String)throwable.getMessage());
        }
    }

    public static void printErrStackTrace(String tag, Throwable throwable, String format, Object ... obj) {
        if (Config.LOGGING) {
            Log.d((String)tag, (String)throwable.getMessage());
        }
    }

    public static void e(String tag, String content, Exception e) {
        Log.e((String)tag, (String)content, (Throwable)e);
        if (mLogCallback != null) {
            mLogCallback.onLog(tag, content);
        }
    }

    public static void w(String tag, String content, Exception e) {
        if (Config.LOGGING) {
            Log.w((String)tag, (String)content, (Throwable)e);
        }
        if (mLogCallback != null) {
            mLogCallback.onLog(tag, content);
        }
    }

    public static void w(String tag, Exception ex) {
        if (Config.LOGGING) {
            Log.w((String)tag, (Throwable)ex);
        }
    }

    public static interface LogCallback {
        public void onLog(String var1, String var2);
    }
}


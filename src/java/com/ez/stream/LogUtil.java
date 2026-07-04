/*
 * Decompiled with CFR 0.152.
 */
package com.ez.stream;

import com.ez.stream.EZStreamClientManager;
import com.ez.stream.NativeApi;

public class LogUtil {
    public static boolean IS_LOG = false;
    public static boolean IS_XLOG = false;
    public static final int LOG_LEVEL_DEBUG = 1;
    public static final int LOG_LEVEL_INFO = 2;
    public static final int LOG_LEVEL_WARN = 3;
    public static final int LOG_LEVEL_ERROR = 4;
    private static EZStreamClientManager.LogCallback mCallback;

    public static void d(String tag, String msg) {
        LogUtil.onLog(tag, 1, msg);
    }

    public static void i(String tag, String msg) {
        LogUtil.onLog(tag, 2, msg);
    }

    public static void e(String tag, String msg) {
        LogUtil.onLog(tag, 4, msg);
    }

    public static void v(String tag, String msg) {
        LogUtil.onLog(tag, 2, msg);
    }

    public static void w(String tag, String msg) {
        LogUtil.onLog(tag, 3, msg);
    }

    static void onLog(String tag, int level, String msg) {
        if (!IS_LOG && !IS_XLOG) {
            return;
        }
        if (tag == null || msg == null) {
            return;
        }
        if (mCallback != null) {
            mCallback.onLog(tag, level, msg);
            return;
        }
        NativeApi.logPrint(tag, msg);
    }

    public static void setLogCallback(EZStreamClientManager.LogCallback callback) {
        mCallback = callback;
    }
}


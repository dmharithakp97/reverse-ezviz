/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package com.ezviz.sdk.configwifi.common;

import android.util.Log;
import com.ezviz.sdk.configwifi.Config;

public class LogUtil {
    public static void debugLog(String tag, String content) {
        if (Config.LOGGING) {
            Log.i((String)tag, (String)content);
        }
    }

    public static void errorLog(String tag, String content) {
        Log.e((String)tag, (String)content);
    }

    public static void infoLog(String tag, String content) {
        if (Config.LOGGING) {
            Log.i((String)tag, (String)content);
        }
    }

    public static void verboseLog(String tag, String content) {
        if (Config.LOGGING) {
            Log.v((String)tag, (String)content);
        }
    }

    public static void warnLog(String tag, String content) {
        if (Config.LOGGING) {
            Log.w((String)tag, (String)content);
        }
    }

    public static void debugLog(String tag, String content, Exception e) {
        if (Config.LOGGING) {
            Log.d((String)tag, (String)content, (Throwable)e);
        }
    }

    public static void errorLog(String tag, String content, Exception e) {
        Log.e((String)tag, (String)content, (Throwable)e);
    }

    public static void warnLog(String tag, String content, Exception e) {
        if (Config.LOGGING) {
            Log.w((String)tag, (String)content, (Throwable)e);
        }
    }

    public static void warnLog(String tag, Exception ex) {
        if (Config.LOGGING) {
            Log.w((String)tag, (Throwable)ex);
        }
    }

    public static void d(String tag, String content) {
        if (Config.LOGGING) {
            Log.d((String)tag, (String)content);
        }
    }

    public static void e(String tag, String content) {
        Log.e((String)tag, (String)content);
    }

    public static void i(String tag, String content) {
        Log.i((String)tag, (String)content);
    }

    public static void v(String tag, String content) {
        if (Config.LOGGING) {
            Log.v((String)tag, (String)content);
        }
    }

    public static void w(String tag, String content) {
        if (Config.LOGGING) {
            Log.w((String)tag, (String)content);
        }
    }

    public static void d(String tag, String content, Exception e) {
        if (Config.LOGGING) {
            Log.d((String)tag, (String)content, (Throwable)e);
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
    }

    public static void w(String tag, String content, Exception e) {
        if (Config.LOGGING) {
            Log.w((String)tag, (String)content, (Throwable)e);
        }
    }

    public static void w(String tag, Exception ex) {
        if (Config.LOGGING) {
            Log.w((String)tag, (Throwable)ex);
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.ez.stream;

public class EZGrayConfig {
    private static final String TAG = "EZGrayConfig";
    public static final int PING_DEFAULT_TIMES = 3;
    public static final int PINT_DEFAULT_TIMEOUT = 3;
    public static final int PING_DEFAULT_TIMEINTERVAL = 1;
    public static int pingCount = 3;
    public static int pingTimeout = 3;
    public static float pingInterval = 1.0f;

    public static void setGrayConfig(String config) {
        if (config != null) {
            pingCount = EZGrayConfig.getValueForKey(config, "apc", 1, 10, 3);
            pingTimeout = EZGrayConfig.getValueForKey(config, "apo", 1, 15, 3);
            pingInterval = (float)EZGrayConfig.getValueForKey(config, "api", 200, 2000, 1000) / 1000.0f;
        }
    }

    private static int getValueForKey(String params, String key, int min, int max, int defaultValue) {
        int startKeyPos = params.indexOf(key);
        if (startKeyPos == -1) {
            return defaultValue;
        }
        int startValuePos = params.indexOf(":", startKeyPos);
        int stopValuePos = params.indexOf(";", startKeyPos);
        if (stopValuePos == -1) {
            stopValuePos = params.length();
        }
        try {
            String value = params.substring(startValuePos + 1, stopValuePos);
            int iValue = Integer.parseInt(value);
            if (iValue >= min && iValue <= max) {
                return iValue;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }
}


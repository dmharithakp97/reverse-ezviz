/*
 * Decompiled with CFR 0.152.
 */
package com.ezviz.utils;

import com.ezviz.sdk.configwifi.mixedconfig.NetUtil;

public class NativeApi {
    public static native void setLogPrintEnable(boolean var0);

    public static native boolean generateWifiConfigWave(String var0, byte[] var1, byte[] var2, int var3);

    public static native int startSeedTest(String var0, int var1, int var2, NetUtil.onSpeedListener var3);

    public static native void cancelSeedTest();

    public static native long startTest();

    public static native void stopTest(long var0);

    static {
        System.loadLibrary("ezutils");
    }
}


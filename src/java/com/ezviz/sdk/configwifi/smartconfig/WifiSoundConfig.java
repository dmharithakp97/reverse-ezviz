/*
 * Decompiled with CFR 0.152.
 */
package com.ezviz.sdk.configwifi.smartconfig;

import com.ezviz.utils.NativeApi;

public class WifiSoundConfig {
    static String TAG = "EZUtils";

    public static boolean generateWifiConfigWave(String fileName, String ssid, String password, int productId) {
        if (fileName == null || ssid == null || password == null) {
            return false;
        }
        return NativeApi.generateWifiConfigWave(fileName, ssid.getBytes(), password.getBytes(), productId);
    }

    public static boolean generateWifiConfigWave(String fileName, byte[] ssid, byte[] password, int productId) {
        if (fileName == null || ssid == null || password == null) {
            return false;
        }
        return NativeApi.generateWifiConfigWave(fileName, ssid, password, productId);
    }
}


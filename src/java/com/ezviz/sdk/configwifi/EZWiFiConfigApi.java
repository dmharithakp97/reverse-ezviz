/*
 * Decompiled with CFR 0.152.
 */
package com.ezviz.sdk.configwifi;

import com.ezviz.sdk.configwifi.common.EZConfigWifiCallback;
import com.ezviz.sdk.configwifi.mixedconfig.BanjourDeviceInfo;

public interface EZWiFiConfigApi {
    public boolean setParams(String var1, String var2, String var3);

    public int startSmartConfig();

    public int stopSmartConfig();

    public void startSoundWaveConfig();

    public void stopSoundWaveConfig();

    public boolean startSADPSearchResult(SadpDeviceFoundListener var1);

    public boolean startAPConfigSearchResult(EZConfigWifiCallback var1);

    public boolean startAPHttpConfigSearchResult(EZConfigWifiCallback var1);

    public boolean stopSADPSearch();

    public void startBonjourResult(BanjourDeviceFoundListener var1);

    public void stopBonjour();

    public void startAPConfig(String var1);

    public void startAPConfig(String var1, String var2, String var3, boolean var4);

    public void stopAPConfig();

    public static interface BanjourDeviceFoundListener {
        public void onDeviceFound(BanjourDeviceInfo var1);
    }

    public static interface SadpDeviceFoundListener {
        public void onDeviceFound(String var1);
    }
}


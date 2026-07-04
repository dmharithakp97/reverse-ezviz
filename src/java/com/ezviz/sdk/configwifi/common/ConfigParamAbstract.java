/*
 * Decompiled with CFR 0.152.
 */
package com.ezviz.sdk.configwifi.common;

import com.ezviz.sdk.configwifi.common.ConfigParamInterface;

public abstract class ConfigParamAbstract
implements ConfigParamInterface {
    public String routerWifiSsid;
    public String routerWifiPwd;
    public String deviceSerial;

    public String toJson() {
        return null;
    }
}


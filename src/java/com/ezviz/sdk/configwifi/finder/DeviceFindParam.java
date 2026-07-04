/*
 * Decompiled with CFR 0.152.
 */
package com.ezviz.sdk.configwifi.finder;

import com.ezviz.sdk.configwifi.common.ConfigParamAbstract;

public class DeviceFindParam {
    public String serial;
    public String verifyCode;
    public String wifiName;
    public String wifiPwd;

    public DeviceFindParam() {
    }

    public DeviceFindParam(ConfigParamAbstract configParam) {
        this.serial = configParam.deviceSerial;
        this.wifiName = configParam.routerWifiSsid;
        this.wifiPwd = configParam.routerWifiPwd;
    }
}


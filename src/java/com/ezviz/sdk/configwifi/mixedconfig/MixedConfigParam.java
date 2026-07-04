/*
 * Decompiled with CFR 0.152.
 */
package com.ezviz.sdk.configwifi.mixedconfig;

import com.ezviz.sdk.configwifi.common.ConfigParamAbstract;
import com.ezviz.sdk.configwifi.smartconfig.SmartConfigParam;
import com.ezviz.sdk.configwifi.soundwave.SoundWaveConfigParam;

public class MixedConfigParam
extends ConfigParamAbstract {
    public int mode = 0;

    public MixedConfigParam() {
    }

    public MixedConfigParam(SmartConfigParam param) {
        this.obtainConfigParamInfo(param);
    }

    public MixedConfigParam(SoundWaveConfigParam param) {
        this.obtainConfigParamInfo(param);
    }

    private void obtainConfigParamInfo(ConfigParamAbstract param) {
        this.routerWifiSsid = param.routerWifiSsid;
        this.routerWifiPwd = param.routerWifiPwd;
        this.deviceSerial = param.deviceSerial;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.ezviz.sdk.configwifi.mixedconfig;

import com.ezviz.sdk.configwifi.mixedconfig.BanjourDeviceStateEnum;

public class BanjourDeviceInfo {
    public String name;
    public String deviceType;
    public String deviceSerial;
    private BanjourDeviceStateEnum deviceState;

    public BanjourDeviceStateEnum getDeviceState() {
        return this.deviceState;
    }

    public void setDeviceState(BanjourDeviceStateEnum deviceState) {
        this.deviceState = deviceState;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeviceType() {
        return this.deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceSerial() {
        return this.deviceSerial;
    }

    public void setDeviceSerial(String deviceSerial) {
        this.deviceSerial = deviceSerial;
    }
}


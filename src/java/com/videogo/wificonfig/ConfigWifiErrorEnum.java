/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.ezviz.sdk.configwifi.EZConfigWifiErrorEnum
 */
package com.videogo.wificonfig;

import com.ezviz.sdk.configwifi.EZConfigWifiErrorEnum;

public enum ConfigWifiErrorEnum {
    CONFIG_TIMEOUT(EZConfigWifiErrorEnum.CONFIG_TIMEOUT),
    WRONG_DEVICE_VERIFY_CODE(EZConfigWifiErrorEnum.WRONG_DEVICE_VERIFY_CODE),
    MAY_LACK_LOCATION_PERMISSION(EZConfigWifiErrorEnum.MAY_LACK_LOCATION_PERMISSION);

    public int code;
    public String description;

    private ConfigWifiErrorEnum(EZConfigWifiErrorEnum error) {
        this.code = error.code;
        this.description = error.description;
    }
}


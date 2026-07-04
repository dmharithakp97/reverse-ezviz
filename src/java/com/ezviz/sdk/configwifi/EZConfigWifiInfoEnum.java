/*
 * Decompiled with CFR 0.152.
 */
package com.ezviz.sdk.configwifi;

public enum EZConfigWifiInfoEnum {
    CONNECTING_TO_WIFI(53, "device is connecting to platform"),
    CONNECTING_SENT_CONFIGURATION_TO_DEVICE(54, "sent wifi configuration to device"),
    CONNECTED_TO_WIFI(55, "device is connected to wifi"),
    CONNECTED_TO_PLATFORM(60, "device is connected to platform");

    public int code;
    public String description;

    private EZConfigWifiInfoEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }
}


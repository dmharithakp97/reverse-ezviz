/*
 * Decompiled with CFR 0.152.
 */
package com.ezviz.sdk.configwifi;

import com.ezviz.sdk.configwifi.Config;

public enum EZConfigWifiErrorEnum {
    WRONG_CONFIG_PARAM(105, "wrong config param, please check them again"),
    LAST_CONFIG_EXECUTING(110, "last config task is executing, please call stop config wifi interface before start config again"),
    PHONE_NOT_CONNECTED_TO_TARGET_WIFI(111, "sdk can not de detect device's status because the phone do not connect to target wifi"),
    WRONG_DEVICE_VERIFY_CODE(120, "you send wrong device verify code to sdk, please again"),
    CONFIG_TIMEOUT(15, "config wifi timeout, default timeout is " + Config.mTimeoutSecond + "s"),
    MAY_LACK_LOCATION_PERMISSION(501, "failed to scan any wifi, please check if you grant location permission and open the position switch"),
    AP_CONFIG_IS_EXECUTING(502, "ap config is executing, please unInit it first, and then init is again"),
    NOT_FIND_DEVICE_HOTSPOT(505, "not find target device's hotspot, device do not support ap config mode or not change to ap config mode"),
    USER_REFUSED_CONNECTION_REQUEST(506, "user refused to connect to target wifi, you need to guide user to manually connect to target wifi  and restart"),
    FAILED_TO_AUTO_ENABLE_WIFI(507, "failed to enable wifi automatically, you need to guide user to manually enable wifi and restart"),
    CAN_NOT_SEND_CONFIGURATION_TO_DEVICE(605, "sdk can not send configuration to device because the phone do not connect to any valid wifi, which can access internet"),
    PHONE_MEDIA_VALUE_NOT_MAX(705, "phone media volume is not max, and it may cause failure of sound save config"),
    SERVER_EXCEPTION(801, "server exception"),
    NETWORK_EXCEPTION(802, "network exception");

    public int code;
    public String description;

    private EZConfigWifiErrorEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }
}


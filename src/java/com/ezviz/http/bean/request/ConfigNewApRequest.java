/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.SerializedName
 */
package com.ezviz.http.bean.request;

import com.google.gson.annotations.SerializedName;

public class ConfigNewApRequest {
    @SerializedName(value="token")
    public String token;
    @SerializedName(value="lbs_domain")
    public String lbsDomain;
    @SerializedName(value="device_id")
    public String deviceId;
    @SerializedName(value="wifi_info")
    public WifiInfo ezWifiInfo;

    public static class WifiInfo {
        public String ssid;
        public String password;
    }
}


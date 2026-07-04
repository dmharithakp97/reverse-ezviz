/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.SerializedName
 */
package com.ezviz.http.model;

import com.google.gson.annotations.SerializedName;

public class EzWifiInfo {
    @SerializedName(value="ssid")
    public String ssid;
    @SerializedName(value="signal_strength")
    public String signalStrength;
    @SerializedName(value="security_mode")
    public String securityMode;
}


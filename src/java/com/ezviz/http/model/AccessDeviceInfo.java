/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.SerializedName
 */
package com.ezviz.http.model;

import com.google.gson.annotations.SerializedName;

public class AccessDeviceInfo {
    @SerializedName(value="ap_version")
    public String apVersion;
    @SerializedName(value="dev_subserial")
    public String devSubserial;
    @SerializedName(value="dev_type")
    public String devType;
    @SerializedName(value="dev_firmwareversion")
    public String devFirmwareversion;
    @SerializedName(value="dev_mac")
    public String devMac;

    public String getApVersion() {
        return this.apVersion;
    }

    public void setApVersion(String apVersion) {
        this.apVersion = apVersion;
    }

    public String getDevSubserial() {
        return this.devSubserial;
    }

    public void setDevSubserial(String devSubserial) {
        this.devSubserial = devSubserial;
    }

    public String getDevType() {
        return this.devType;
    }

    public void setDevType(String devType) {
        this.devType = devType;
    }

    public String getDevFirmwareversion() {
        return this.devFirmwareversion;
    }

    public void setDevFirmwareversion(String devFirmwareversion) {
        this.devFirmwareversion = devFirmwareversion;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.openapi.model;

import com.videogo.openapi.bean.BaseInfo;

public class EZDeviceAddDeviceInfo
extends BaseInfo {
    private String mDeviceSN;

    public String getDeviceSN() {
        return this.mDeviceSN;
    }

    public void setDeviceSN(String devSN) {
        this.mDeviceSN = devSN;
    }
}


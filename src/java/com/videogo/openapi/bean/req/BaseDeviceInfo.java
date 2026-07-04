/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.openapi.bean.req;

import com.videogo.openapi.bean.BaseInfo;

public class BaseDeviceInfo
extends BaseInfo {
    public static final String DEVICEID = "deviceId";
    private String deviceId;

    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}


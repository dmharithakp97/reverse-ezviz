/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.openapi.bean;

import com.videogo.openapi.annotation.HttpParam;
import com.videogo.util.LocalInfo;

public class BaseHeaderInfo {
    @HttpParam(name="accessToken")
    private String accessToken = LocalInfo.getInstance().getEZAccesstoken().getAccessTokenOrHttpToken();
    @HttpParam(name="deviceSerial")
    private String deviceSerial;
    @HttpParam(name="localIndex")
    private String localIndex;

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setDeviceSerial(String deviceSerial) {
        this.deviceSerial = deviceSerial;
    }

    public void setLocalIndex(String localIndex) {
        this.localIndex = localIndex;
    }
}


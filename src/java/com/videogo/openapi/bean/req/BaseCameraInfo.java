/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.openapi.bean.req;

import com.videogo.openapi.annotation.HttpParam;
import com.videogo.openapi.bean.BaseInfo;

public class BaseCameraInfo
extends BaseInfo {
    public static final String CAMERAID = "cameraId";
    @HttpParam(name="cameraId")
    private String cameraId;

    public String getCameraId() {
        return this.cameraId;
    }

    public void setCameraId(String cameraId) {
        this.cameraId = cameraId;
    }
}


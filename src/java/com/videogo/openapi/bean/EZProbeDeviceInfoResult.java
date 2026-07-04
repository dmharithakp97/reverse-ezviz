/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.openapi.bean;

import com.videogo.exception.BaseException;
import com.videogo.openapi.bean.EZProbeDeviceInfo;

public class EZProbeDeviceInfoResult {
    private EZProbeDeviceInfo mEZProbeDeviceInfo;
    private BaseException mBaseException;

    public EZProbeDeviceInfo getEZProbeDeviceInfo() {
        return this.mEZProbeDeviceInfo;
    }

    public void setEZProbeDeviceInfo(EZProbeDeviceInfo mEZProbeDeviceInfo) {
        this.mEZProbeDeviceInfo = mEZProbeDeviceInfo;
    }

    public BaseException getBaseException() {
        return this.mBaseException;
    }

    public void setBaseException(BaseException exception) {
        this.mBaseException = exception;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.openapi.bean.req;

import com.videogo.openapi.annotation.HttpParam;
import com.videogo.openapi.bean.BaseInfo;

public class BaseAlarmInfo
extends BaseInfo {
    @HttpParam(name="alarmId")
    private String alarmId;

    public String getAlarmId() {
        return this.alarmId;
    }

    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }
}


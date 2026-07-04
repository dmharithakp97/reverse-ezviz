/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.openapi.model;

import com.videogo.openapi.bean.BaseInfo;
import com.videogo.openapi.model.BaseRequset;
import com.videogo.openapi.model.EZAlarmDeleteMultipleAlarmsInfo;
import com.videogo.restful.NameValuePair;
import java.util.List;

public class EZAlarmDeleteMultipleAlarmsReq
extends BaseRequset {
    public static final String URL = "/api/alarm/sdk/deleteAlarm";
    private static final String field0 = "alarmId";
    private EZAlarmDeleteMultipleAlarmsInfo mInfo;

    @Override
    public List<NameValuePair> buidParams(BaseInfo info) {
        this.addPublicParams(info);
        if (!(info instanceof EZAlarmDeleteMultipleAlarmsInfo)) {
            return null;
        }
        this.mInfo = (EZAlarmDeleteMultipleAlarmsInfo)info;
        this.mInfo.fixHttpToken();
        this.nvps.add(new NameValuePair(field0, this.mInfo.getDeleteString()));
        return this.nvps;
    }
}


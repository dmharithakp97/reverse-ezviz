/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.openapi.model;

import com.videogo.openapi.bean.BaseInfo;
import com.videogo.openapi.model.BaseRequset;
import com.videogo.openapi.model.EZAlarmDeleteMultipleAlarmsInfo;
import com.videogo.restful.NameValuePair;
import java.util.List;

public class EZDeviceAddDeviceReq
extends BaseRequset {
    public static final String URL = "device/sdk/addDevice";
    private static final String field0 = "deviceSn";
    private EZAlarmDeleteMultipleAlarmsInfo mInfo;

    @Override
    public List<NameValuePair> buidParams(BaseInfo info) {
        this.addPublicParams(info);
        if (!(info instanceof EZAlarmDeleteMultipleAlarmsInfo)) {
            return null;
        }
        this.mInfo = (EZAlarmDeleteMultipleAlarmsInfo)info;
        this.nvps.add(new NameValuePair(field0, this.mInfo.getDeleteString()));
        return this.nvps;
    }
}


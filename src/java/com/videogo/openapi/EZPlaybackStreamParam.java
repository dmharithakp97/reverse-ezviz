/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.openapi;

import com.videogo.openapi.bean.EZCloudRecordFile;
import com.videogo.openapi.bean.EZDeviceRecordFile;

public class EZPlaybackStreamParam {
    public int recordSource;
    public EZCloudRecordFile ezCloudRecordFile;
    public EZDeviceRecordFile ezDeviceRecordFile;

    public static EZPlaybackStreamParam createBy(EZCloudRecordFile ezCloudRecordFile) {
        EZPlaybackStreamParam param = new EZPlaybackStreamParam();
        param.recordSource = 1;
        param.ezCloudRecordFile = ezCloudRecordFile;
        return param;
    }

    public static EZPlaybackStreamParam createBy(EZDeviceRecordFile ezDeviceRecordFile) {
        EZPlaybackStreamParam param = new EZPlaybackStreamParam();
        param.recordSource = 2;
        param.ezDeviceRecordFile = ezDeviceRecordFile;
        return param;
    }
}


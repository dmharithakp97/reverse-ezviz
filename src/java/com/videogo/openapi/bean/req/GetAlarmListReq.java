/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.openapi.bean.req;

import com.videogo.openapi.annotation.HttpParam;
import com.videogo.openapi.bean.BaseInfo;

public class GetAlarmListReq
extends BaseInfo {
    @HttpParam(name="deviceSerial")
    private String deviceSerial;
    @HttpParam(name="cameraNo")
    private int cameraNo = 1;
    @HttpParam(name="startTime")
    private String startTime;
    @HttpParam(name="endTime")
    private String endTime;
    @HttpParam(name="alarmType")
    private int alarmType;
    @HttpParam(name="status")
    private int status;
    @HttpParam(name="pageStart")
    private int pageStart;
    @HttpParam(name="pageSize")
    private int pageSize;
    @HttpParam(name="version")
    private String version = "2.0";

    public String getDeviceSerial() {
        return this.deviceSerial;
    }

    public void setDeviceSerial(String deviceSerial) {
        this.deviceSerial = deviceSerial;
    }

    public int getCameraNo() {
        return this.cameraNo;
    }

    public void setCameraNo(int cameraNo) {
        this.cameraNo = cameraNo;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getAlarmType() {
        return this.alarmType;
    }

    public void setAlarmType(int alarmType) {
        this.alarmType = alarmType;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPageStart() {
        return this.pageStart;
    }

    public void setPageStart(int pageStart) {
        this.pageStart = pageStart;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}


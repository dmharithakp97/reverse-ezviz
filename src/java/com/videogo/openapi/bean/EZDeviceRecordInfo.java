/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.openapi.bean;

import com.videogo.openapi.bean.EZDeviceRecordFile;
import java.util.Calendar;
import java.util.List;

public class EZDeviceRecordInfo {
    private List<EZDeviceRecordFile> deviceRecordFileList;
    private boolean hasMore;
    private Calendar nextFileTime;
    private boolean fromNvr;
    private String deviceSerial;

    public List<EZDeviceRecordFile> getDeviceRecordFileList() {
        return this.deviceRecordFileList;
    }

    public void setDeviceRecordFileList(List<EZDeviceRecordFile> deviceRecordFileList) {
        this.deviceRecordFileList = deviceRecordFileList;
    }

    public boolean isHasMore() {
        return this.hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public Calendar getNextFileTime() {
        return this.nextFileTime;
    }

    public void setNextFileTime(Calendar nextFileTime) {
        this.nextFileTime = nextFileTime;
    }

    public boolean isFromNvr() {
        return this.fromNvr;
    }

    public void setFromNvr(boolean fromNvr) {
        this.fromNvr = fromNvr;
    }

    public String getDeviceSerial() {
        return this.deviceSerial;
    }

    public void setDeviceSerial(String deviceSerial) {
        this.deviceSerial = deviceSerial;
    }
}


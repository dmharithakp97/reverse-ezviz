/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.openapi.bean.resp;

import com.videogo.openapi.annotation.Serializable;

public class CloudFile {
    @Serializable(name="fileId")
    private String fileId;
    @Serializable(name="startTime")
    private String startTime;
    @Serializable(name="endTime")
    private String endTime;

    public String getEndTime() {
        return this.endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getFileId() {
        return this.fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
}


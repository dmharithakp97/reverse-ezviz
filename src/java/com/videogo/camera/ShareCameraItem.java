/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.camera;

public class ShareCameraItem {
    private String uuid;
    private String beginTime;
    private String endTime;
    private String deviceSN;
    private int channelNo;
    private String password;
    private int viewedCount;
    private int viewingCount;
    private int likeCount;
    private String url;

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getBeginTime() {
        return this.beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDeviceSN() {
        return this.deviceSN;
    }

    public void setDeviceSN(String deviceSN) {
        this.deviceSN = deviceSN;
    }

    public int getChannelNo() {
        return this.channelNo;
    }

    public void setChannelNo(int channelNo) {
        this.channelNo = channelNo;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getViewedCount() {
        return this.viewedCount;
    }

    public void setViewedCount(int viewedCount) {
        this.viewedCount = viewedCount;
    }

    public int getViewingCount() {
        return this.viewingCount;
    }

    public void setViewingCount(int viewingCount) {
        this.viewingCount = viewingCount;
    }

    public int getLikeCount() {
        return this.likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String toString() {
        return "ShareCameraItem [uuid=" + this.uuid + ", beginTime=" + this.beginTime + ", endTime=" + this.endTime + ", deviceSN=" + this.deviceSN + ", channelNo=" + this.channelNo + ", password=" + this.password + ", viewedCount=" + this.viewedCount + ", viewingCount=" + this.viewingCount + ", likeCount=" + this.likeCount + ", url=" + this.url + "]";
    }
}


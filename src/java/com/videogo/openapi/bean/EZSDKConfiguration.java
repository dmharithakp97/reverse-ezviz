/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.openapi.bean;

import com.videogo.openapi.annotation.Serializable;

public class EZSDKConfiguration {
    @Serializable(name="pushAddr")
    private String pushAddr;
    @Serializable(name="streamType")
    private int streamType;
    @Serializable(name="pushAuthAddr")
    private String pushAuthAddr;
    @Serializable(name="dataCollect")
    private int dataCollect;
    private StreamLimitInfoEntity streamLimitInfo;

    public void setPushAddr(String pushAddr) {
        this.pushAddr = pushAddr;
    }

    public void setStreamType(int streamType) {
        this.streamType = streamType;
    }

    public void setPushAuthAddr(String pushAuthAddr) {
        this.pushAuthAddr = pushAuthAddr;
    }

    public void setDataCollect(int dataCollect) {
        this.dataCollect = dataCollect;
    }

    public void setStreamLimitInfo(StreamLimitInfoEntity streamLimitInfo) {
        this.streamLimitInfo = streamLimitInfo;
    }

    public String getPushAddr() {
        return this.pushAddr;
    }

    public int getStreamType() {
        return this.streamType;
    }

    public String getPushAuthAddr() {
        return this.pushAuthAddr;
    }

    public int getDataCollect() {
        return this.dataCollect;
    }

    public StreamLimitInfoEntity getStreamLimitInfo() {
        return this.streamLimitInfo;
    }

    public String toString() {
        return "EZSDKConfiguration{pushAddr='" + this.pushAddr + '\'' + ", streamType=" + this.streamType + ", pushAuthAddr='" + this.pushAuthAddr + '\'' + ", dataCollect=" + this.dataCollect + ", streamLimitInfo=" + this.streamLimitInfo + '}';
    }

    public static class StreamLimitInfoEntity {
        @Serializable(name="limitTime")
        private int limitTime;
        @Serializable(name="streamTimeLimitSwitch")
        private String streamTimeLimitSwitch;

        public void setLimitTime(int limitTime) {
            this.limitTime = limitTime;
        }

        public void setStreamTimeLimitSwitch(String streamTimeLimitSwitch) {
            this.streamTimeLimitSwitch = streamTimeLimitSwitch;
        }

        public int getLimitTime() {
            return this.limitTime;
        }

        public String getStreamTimeLimitSwitch() {
            return this.streamTimeLimitSwitch;
        }
    }
}


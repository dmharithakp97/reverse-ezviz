/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.openapi.bean;

public class EZStreamLimitInfo {
    private int streamType;
    private int dataCollect;
    private StreamLimitInfoEntity streamLimitInfo;

    public void setStreamType(int streamType) {
        this.streamType = streamType;
    }

    public void setDataCollect(int dataCollect) {
        this.dataCollect = dataCollect;
    }

    public void setStreamLimitInfo(StreamLimitInfoEntity streamLimitInfo) {
        this.streamLimitInfo = streamLimitInfo;
    }

    public int getStreamType() {
        return this.streamType;
    }

    public int getDataCollect() {
        return this.dataCollect;
    }

    public StreamLimitInfoEntity getStreamLimitInfo() {
        return this.streamLimitInfo;
    }

    public String toString() {
        return "EZStreamLimitInfo{streamType=" + this.streamType + ", dataCollect=" + this.dataCollect + ", streamLimitInfo=" + this.streamLimitInfo + '}';
    }

    public static class StreamLimitInfoEntity {
        private int limitTime;
        private int streamTimeLimitSwitch;

        public void setLimitTime(int limitTime) {
            this.limitTime = limitTime;
        }

        public void setStreamTimeLimitSwitch(int streamTimeLimitSwitch) {
            this.streamTimeLimitSwitch = streamTimeLimitSwitch;
        }

        public int getLimitTime() {
            return this.limitTime;
        }

        public int getStreamTimeLimitSwitch() {
            return this.streamTimeLimitSwitch;
        }

        public String toString() {
            return "StreamLimitInfoEntity{limitTime=" + this.limitTime + ", streamTimeLimitSwitch=" + this.streamTimeLimitSwitch + '}';
        }
    }
}


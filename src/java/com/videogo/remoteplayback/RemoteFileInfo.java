/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.remoteplayback;

import java.util.Calendar;

public class RemoteFileInfo {
    private int mType = 0;
    private Calendar mStartTime = null;
    private Calendar mStopTime = null;
    private String mFileName = null;
    private long mFileSize = 0L;
    private String channelType;

    public String getChannelType() {
        return this.channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public void setFileType(int type) {
        this.mType = type;
    }

    public int getFileType() {
        return this.mType;
    }

    public Calendar getStartTime() {
        return this.mStartTime;
    }

    public void setStartTime(Calendar startTime) {
        this.mStartTime = startTime;
    }

    public void setStopTime(Calendar stopTime) {
        this.mStopTime = stopTime;
    }

    public Calendar getStopTime() {
        return this.mStopTime;
    }

    public String getFileName() {
        return this.mFileName;
    }

    public void setFileName(String mFileName) {
        this.mFileName = mFileName;
    }

    public long getFileSize() {
        return this.mFileSize;
    }

    public void setFileSize(long mFileSize) {
        this.mFileSize = mFileSize;
    }

    public RemoteFileInfo copy() {
        RemoteFileInfo copy = new RemoteFileInfo();
        copy.setFileType(this.getFileType());
        copy.setStartTime(this.getStartTime());
        copy.setStopTime(this.getStopTime());
        copy.setFileSize(this.getFileSize());
        copy.setFileName(this.getFileName());
        copy.setChannelType(this.channelType);
        return copy;
    }
}


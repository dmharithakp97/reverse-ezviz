/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package com.videogo.openapi.bean;

import android.os.Parcel;
import android.os.Parcelable;
import com.videogo.openapi.annotation.Serializable;
import com.videogo.util.Utils;
import java.util.Calendar;

public class EZDeviceRecordFile
implements Parcelable {
    @Serializable(name="begin")
    private String begin;
    @Serializable(name="end")
    private String end;
    private Calendar mStartTime = null;
    private Calendar mStopTime = null;
    @Serializable(name="type")
    private int type;
    private boolean isIPCRecordDirectQuery;
    @Serializable(name="channelType")
    private String cameraType;
    private int seq;
    public static final Parcelable.Creator<EZDeviceRecordFile> CREATOR = new Parcelable.Creator<EZDeviceRecordFile>(){

        public EZDeviceRecordFile createFromParcel(Parcel source) {
            return new EZDeviceRecordFile(source);
        }

        public EZDeviceRecordFile[] newArray(int size) {
            return new EZDeviceRecordFile[size];
        }
    };

    public String getBegin() {
        return this.begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return this.end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public int getSeq() {
        return this.seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public Calendar getStartTime() {
        if (this.mStartTime == null) {
            this.mStartTime = Utils.convert19Calender(this.begin);
        }
        return this.mStartTime;
    }

    public void setStartTime(Calendar startTime) {
        this.mStartTime = startTime;
    }

    public void setStopTime(Calendar stopTime) {
        this.mStopTime = stopTime;
    }

    public Calendar getStopTime() {
        if (this.mStopTime == null) {
            this.mStopTime = Utils.convert19Calender(this.end);
        }
        return this.mStopTime;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isIPCRecordDirectQuery() {
        return this.isIPCRecordDirectQuery;
    }

    public void setIPCRecordDirectQuery(boolean isIPCRecordDirectQuery) {
        this.isIPCRecordDirectQuery = isIPCRecordDirectQuery;
    }

    public String getCameraType() {
        return this.cameraType;
    }

    public void setCameraType(String cameraType) {
        this.cameraType = cameraType;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.begin);
        dest.writeString(this.end);
        dest.writeSerializable((java.io.Serializable)this.mStartTime);
        dest.writeSerializable((java.io.Serializable)this.mStopTime);
        dest.writeInt(this.type);
        dest.writeString(this.cameraType);
    }

    public EZDeviceRecordFile() {
    }

    protected EZDeviceRecordFile(Parcel in) {
        this.begin = in.readString();
        this.end = in.readString();
        this.mStartTime = (Calendar)in.readSerializable();
        this.mStopTime = (Calendar)in.readSerializable();
        this.type = in.readInt();
        this.cameraType = in.readString();
    }
}


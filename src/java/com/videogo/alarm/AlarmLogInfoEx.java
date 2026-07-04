/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable$Creator
 */
package com.videogo.alarm;

import android.os.Parcel;
import android.os.Parcelable;
import com.videogo.alarm.AlarmLogInfo;
import com.videogo.openapi.bean.resp.CameraInfo;

public class AlarmLogInfoEx
extends AlarmLogInfo {
    public static final int ALARMTYPE = 1;
    public static final int MESSAGETYPE = 2;
    public static final int DEVICETYPE = 3;
    public static final int SYSTEMTYPE = 4;
    private int alarmNum = 1;
    private int mLeaveLen = 0;
    private CameraInfo cameraInfo = null;
    public static final Parcelable.Creator<AlarmLogInfoEx> CREATOR = new Parcelable.Creator<AlarmLogInfoEx>(){

        public AlarmLogInfoEx createFromParcel(Parcel in) {
            return new AlarmLogInfoEx(in);
        }

        public AlarmLogInfoEx[] newArray(int size) {
            return new AlarmLogInfoEx[size];
        }
    };

    public CameraInfo getCameraInfo() {
        return this.cameraInfo;
    }

    public void setCameraInfo(CameraInfo cameraInfo) {
        this.cameraInfo = cameraInfo;
    }

    public AlarmLogInfoEx() {
    }

    public int getAlarmNum() {
        return this.alarmNum;
    }

    public void setAlarmNum(int alarmNum) {
        this.alarmNum = alarmNum;
    }

    public int getLeaveLen() {
        return this.mLeaveLen;
    }

    public void setLeaveLen(int leaveLen) {
        this.mLeaveLen = leaveLen;
    }

    protected AlarmLogInfoEx(Parcel in) {
        super(in);
        this.alarmNum = in.readInt();
        this.mLeaveLen = in.readInt();
        this.cameraInfo = (CameraInfo)in.readValue(CameraInfo.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.alarmNum);
        dest.writeInt(this.mLeaveLen);
        dest.writeValue((Object)this.cameraInfo);
    }
}


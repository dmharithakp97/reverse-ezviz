/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  com.hikvision.netsdk.NET_DVR_DEVICEINFO_V30
 */
package com.ezviz.hcnetsdk;

import android.os.Parcel;
import android.os.Parcelable;
import com.hikvision.netsdk.NET_DVR_DEVICEINFO_V30;

public class EZLoginDeviceInfo
implements Parcelable {
    private int byAlarmInPortNum;
    private int byAlarmOutPortNum;
    private int byDiskNum;
    private int byDVRType;
    private int byChanNum;
    private int byStartChan;
    private int byAudioChanNum;
    private int byIPChanNum;
    private int byStartDChan;
    private int byZeroChanNum;
    private int loginId = -1;
    public static final Parcelable.Creator<EZLoginDeviceInfo> CREATOR = new Parcelable.Creator<EZLoginDeviceInfo>(){

        public EZLoginDeviceInfo createFromParcel(Parcel source) {
            return new EZLoginDeviceInfo(source);
        }

        public EZLoginDeviceInfo[] newArray(int size) {
            return new EZLoginDeviceInfo[size];
        }
    };

    public EZLoginDeviceInfo(NET_DVR_DEVICEINFO_V30 deviceinfo_v30, int loginId) {
        this.byAlarmInPortNum = deviceinfo_v30.byAlarmInPortNum;
        this.byAlarmOutPortNum = deviceinfo_v30.byAlarmOutPortNum;
        this.byDiskNum = deviceinfo_v30.byDiskNum;
        this.byDVRType = deviceinfo_v30.byDVRType;
        this.byChanNum = deviceinfo_v30.byChanNum;
        this.byStartChan = deviceinfo_v30.byStartChan;
        this.byAudioChanNum = deviceinfo_v30.byAudioChanNum;
        this.byIPChanNum = deviceinfo_v30.byIPChanNum;
        this.byZeroChanNum = deviceinfo_v30.byZeroChanNum;
        this.byStartDChan = deviceinfo_v30.byStartDChan;
        this.loginId = loginId;
    }

    public int getByAlarmInPortNum() {
        return this.byAlarmInPortNum;
    }

    public void setByAlarmInPortNum(int byAlarmInPortNum) {
        this.byAlarmInPortNum = byAlarmInPortNum;
    }

    public int getByAlarmOutPortNum() {
        return this.byAlarmOutPortNum;
    }

    public void setByAlarmOutPortNum(int byAlarmOutPortNum) {
        this.byAlarmOutPortNum = byAlarmOutPortNum;
    }

    public int getByDiskNum() {
        return this.byDiskNum;
    }

    public void setByDiskNum(int byDiskNum) {
        this.byDiskNum = byDiskNum;
    }

    public int getByDVRType() {
        return this.byDVRType;
    }

    public void setByDVRType(int byDVRType) {
        this.byDVRType = byDVRType;
    }

    public int getByChanNum() {
        return this.byChanNum;
    }

    public void setByChanNum(int byChanNum) {
        this.byChanNum = byChanNum;
    }

    public int getByStartChan() {
        return this.byStartChan;
    }

    public void setByStartChan(int byStartChan) {
        this.byStartChan = byStartChan;
    }

    public int getByAudioChanNum() {
        return this.byAudioChanNum;
    }

    public void setByAudioChanNum(int byAudioChanNum) {
        this.byAudioChanNum = byAudioChanNum;
    }

    public int getByIPChanNum() {
        return this.byIPChanNum;
    }

    public void setByIPChanNum(int byIPChanNum) {
        this.byIPChanNum = byIPChanNum;
    }

    public int getByStartDChan() {
        return this.byStartDChan;
    }

    public void setByStartDChan(int byStartDChan) {
        this.byStartDChan = byStartDChan;
    }

    public int getByZeroChanNum() {
        return this.byZeroChanNum;
    }

    public void setByZeroChanNum(int byZeroChanNum) {
        this.byZeroChanNum = byZeroChanNum;
    }

    public int getLoginId() {
        return this.loginId;
    }

    public void setLoginId(int loginId) {
        this.loginId = loginId;
    }

    public static Parcelable.Creator<EZLoginDeviceInfo> getCREATOR() {
        return CREATOR;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.byAlarmInPortNum);
        dest.writeInt(this.byAlarmOutPortNum);
        dest.writeInt(this.byDiskNum);
        dest.writeInt(this.byDVRType);
        dest.writeInt(this.byChanNum);
        dest.writeInt(this.byStartChan);
        dest.writeInt(this.byAudioChanNum);
        dest.writeInt(this.byIPChanNum);
        dest.writeInt(this.byStartDChan);
        dest.writeInt(this.byZeroChanNum);
        dest.writeInt(this.loginId);
    }

    protected EZLoginDeviceInfo(Parcel in) {
        this.byAlarmInPortNum = in.readInt();
        this.byAlarmOutPortNum = in.readInt();
        this.byDiskNum = in.readInt();
        this.byDVRType = in.readInt();
        this.byChanNum = in.readInt();
        this.byStartChan = in.readInt();
        this.byAudioChanNum = in.readInt();
        this.byIPChanNum = in.readInt();
        this.byStartDChan = in.readInt();
        this.byZeroChanNum = in.readInt();
        this.loginId = in.readInt();
    }
}


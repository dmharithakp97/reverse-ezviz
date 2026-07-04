/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package com.videogo.device;

import android.os.Parcel;
import android.os.Parcelable;
import com.videogo.util.LocalInfo;

public class PeripheralInfo
implements Parcelable {
    private int id = -1;
    private String channelSerial = "";
    private int channelNo = -1;
    private String deviceSerial = "";
    private int channelState = -1;
    private String channelType = "";
    private String channelTypeStr = "";
    private String location = "";
    private int zfStatus = -1;
    private int uvStatus = -1;
    private int iwcStatus = -1;
    private int olStatus = -1;
    private int extInt = -1;
    private String extStr = "";
    private String createTime;
    private String updateTime;
    private String picPath = "";
    private String alarmEnableStatus;
    private boolean atHomeEnable = false;
    private boolean outDoorEnable;
    private boolean sleepEnable;
    public static final Parcelable.Creator<PeripheralInfo> CREATOR = new Parcelable.Creator<PeripheralInfo>(){

        public PeripheralInfo createFromParcel(Parcel in) {
            return new PeripheralInfo(in);
        }

        public PeripheralInfo[] newArray(int size) {
            return new PeripheralInfo[size];
        }
    };

    public PeripheralInfo() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChannelSerial() {
        return this.channelSerial;
    }

    public void setChannelSerial(String channelSerial) {
        this.channelSerial = channelSerial;
    }

    public int getChannelNo() {
        return this.channelNo;
    }

    public void setChannelNo(int channelNo) {
        this.channelNo = channelNo;
    }

    public String getDeviceSerial() {
        return this.deviceSerial;
    }

    public void setDeviceSerial(String deviceSerial) {
        this.deviceSerial = deviceSerial;
    }

    public int getChannelState() {
        return this.channelState;
    }

    public void setChannelState(int channelState) {
        this.channelState = channelState;
    }

    public String getChannelType() {
        return this.channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getChannelTypeStr() {
        return this.channelTypeStr;
    }

    public void setChannelTypeStr(String channelTypeStr) {
        this.channelTypeStr = channelTypeStr;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocationt(String location) {
        this.location = location;
    }

    public int getZfStatus() {
        return this.zfStatus;
    }

    public void setZfStatus(int zfStatus) {
        this.zfStatus = zfStatus;
    }

    public int getUvStatus() {
        return this.uvStatus;
    }

    public void setUvStatus(int uvStatus) {
        this.uvStatus = uvStatus;
    }

    public int getIwcStatus() {
        return this.iwcStatus;
    }

    public void setIwcStatus(int iwcStatus) {
        this.iwcStatus = iwcStatus;
    }

    public int getOlStatus() {
        return this.olStatus;
    }

    public void setOlStatus(int olStatus) {
        this.olStatus = olStatus;
    }

    public int getExtInt() {
        return this.extInt;
    }

    public void setExtInt(int extInt) {
        this.extInt = extInt;
    }

    public String getExtStr() {
        return this.extStr;
    }

    public void setExtStr(String extStr) {
        this.extStr = extStr;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getPicPath() {
        return LocalInfo.getInstance().getServAddr() + this.picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public String getAlarmEnableStatus() {
        return this.alarmEnableStatus;
    }

    public void setAlarmEnableStatus(String alarmEnableStatus) {
        this.alarmEnableStatus = alarmEnableStatus;
    }

    public boolean isAtHomeEnable() {
        return this.atHomeEnable;
    }

    public void setAtHomeEnable(boolean atHomeEnable) {
        this.atHomeEnable = atHomeEnable;
    }

    public boolean isOutDoorEnable() {
        return this.outDoorEnable;
    }

    public void setOutDoorEnable(boolean outDoorEnable) {
        this.outDoorEnable = outDoorEnable;
    }

    public boolean isSleepEnable() {
        return this.sleepEnable;
    }

    public void setSleepEnable(boolean sleepEnable) {
        this.sleepEnable = sleepEnable;
    }

    protected PeripheralInfo(Parcel in) {
        this.id = in.readInt();
        this.channelSerial = in.readString();
        this.channelNo = in.readInt();
        this.deviceSerial = in.readString();
        this.channelState = in.readInt();
        this.channelType = in.readString();
        this.channelTypeStr = in.readString();
        this.location = in.readString();
        this.zfStatus = in.readInt();
        this.uvStatus = in.readInt();
        this.iwcStatus = in.readInt();
        this.olStatus = in.readInt();
        this.extInt = in.readInt();
        this.extStr = in.readString();
        this.createTime = in.readString();
        this.updateTime = in.readString();
        this.picPath = in.readString();
        this.alarmEnableStatus = in.readString();
        this.atHomeEnable = in.readByte() != 0;
        this.outDoorEnable = in.readByte() != 0;
        this.sleepEnable = in.readByte() != 0;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.channelSerial);
        dest.writeInt(this.channelNo);
        dest.writeString(this.deviceSerial);
        dest.writeInt(this.channelState);
        dest.writeString(this.channelType);
        dest.writeString(this.channelTypeStr);
        dest.writeString(this.location);
        dest.writeInt(this.zfStatus);
        dest.writeInt(this.uvStatus);
        dest.writeInt(this.iwcStatus);
        dest.writeInt(this.olStatus);
        dest.writeInt(this.extInt);
        dest.writeString(this.extStr);
        dest.writeString(this.createTime);
        dest.writeString(this.updateTime);
        dest.writeString(this.picPath);
        dest.writeString(this.alarmEnableStatus);
        dest.writeByte((byte)(this.atHomeEnable ? 1 : 0));
        dest.writeByte((byte)(this.outDoorEnable ? 1 : 0));
        dest.writeByte((byte)(this.sleepEnable ? 1 : 0));
    }
}


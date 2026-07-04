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
import com.videogo.alarm.BaseMessageInfo;

public class AlarmLogInfo
extends BaseMessageInfo {
    private String alarmLogId = "";
    private String objectName = "";
    private String deviceSerial = "";
    private String channelType = "";
    private int channelNo = 0;
    private String alarmOccurTime = "";
    private int alarmType = 0;
    private String alarmPicUrl = "";
    private String alarmRecUrl = "";
    private int checkState = -1;
    private String logInfo = "";
    private String alarmStartTime = "";
    private boolean isCloud = false;
    private boolean isEncryption = false;
    private String checkSum;
    public static final Parcelable.Creator<AlarmLogInfo> CREATOR = new Parcelable.Creator<AlarmLogInfo>(){

        public AlarmLogInfo createFromParcel(Parcel in) {
            return new AlarmLogInfo(in);
        }

        public AlarmLogInfo[] newArray(int size) {
            return new AlarmLogInfo[size];
        }
    };

    public AlarmLogInfo() {
    }

    public void setAlarmLogId(String alarmLogId) {
        this.alarmLogId = alarmLogId;
    }

    public String getAlarmLogId() {
        return this.alarmLogId;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getObjectName() {
        return this.objectName;
    }

    public void setDeviceSerial(String deviceSerial) {
        this.deviceSerial = deviceSerial;
    }

    public String getDeviceSerial() {
        return this.deviceSerial;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getChannelType() {
        return this.channelType;
    }

    public void setChannelNo(int channelNo) {
        this.channelNo = channelNo;
    }

    public int getChannelNo() {
        return this.channelNo;
    }

    public void setAlarmType(int alarmType) {
        this.alarmType = alarmType;
    }

    public int getAlarmType() {
        return this.alarmType;
    }

    public void setAlarmStartTime(String alarmStartTime) {
        this.alarmStartTime = alarmStartTime;
    }

    public String getAlarmStartTime() {
        return this.alarmStartTime;
    }

    public void setAlarmPicUrl(String alarmPicUrl) {
        this.alarmPicUrl = alarmPicUrl;
    }

    public String getAlarmPicUrl() {
        return this.alarmPicUrl;
    }

    public void setAlarmRecUrl(String alarmRecUrl) {
        this.alarmRecUrl = alarmRecUrl;
    }

    public String getAlarmRecUrl() {
        return this.alarmRecUrl;
    }

    public void setCheckState(int checkState) {
        this.checkState = checkState;
    }

    public int getCheckState() {
        return this.checkState;
    }

    public void setLogInfo(String logInfo) {
        this.logInfo = logInfo;
    }

    public String getLogInfo() {
        return this.logInfo;
    }

    public void setAlarmOccurTime(String alarmOccurTime) {
        this.alarmOccurTime = alarmOccurTime;
    }

    public String getAlarmOccurTime() {
        return this.alarmOccurTime;
    }

    public void setAlarmIsCloud(boolean flag) {
        this.isCloud = flag;
    }

    public boolean getAlarmCloud() {
        return this.isCloud;
    }

    public void setAlarmIsEncyption(boolean flag) {
        this.isEncryption = flag;
    }

    public boolean getAlarmEncryption() {
        return this.isEncryption;
    }

    public String getCheckSum() {
        return this.checkSum;
    }

    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum;
    }

    public void copy(AlarmLogInfo alarmLogInfo) {
        this.alarmLogId = alarmLogInfo.alarmLogId;
        this.objectName = alarmLogInfo.objectName;
        this.deviceSerial = alarmLogInfo.deviceSerial;
        this.channelType = alarmLogInfo.channelType;
        this.channelNo = alarmLogInfo.channelNo;
        this.alarmOccurTime = alarmLogInfo.alarmOccurTime;
        this.alarmStartTime = alarmLogInfo.alarmStartTime;
        this.alarmType = alarmLogInfo.alarmType;
        this.alarmPicUrl = alarmLogInfo.alarmPicUrl;
        this.alarmRecUrl = alarmLogInfo.alarmRecUrl;
        this.checkState = alarmLogInfo.checkState;
        this.logInfo = alarmLogInfo.logInfo;
        this.isEncryption = alarmLogInfo.isEncryption;
        this.isCloud = alarmLogInfo.isCloud;
        this.checkSum = alarmLogInfo.checkSum;
    }

    protected AlarmLogInfo(Parcel in) {
        super(in);
        this.alarmLogId = in.readString();
        this.objectName = in.readString();
        this.deviceSerial = in.readString();
        this.channelType = in.readString();
        this.channelNo = in.readInt();
        this.alarmOccurTime = in.readString();
        this.alarmType = in.readInt();
        this.alarmPicUrl = in.readString();
        this.alarmRecUrl = in.readString();
        this.checkState = in.readInt();
        this.logInfo = in.readString();
        this.alarmStartTime = in.readString();
        this.isCloud = in.readByte() != 0;
        this.isEncryption = in.readByte() != 0;
        this.checkSum = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.alarmLogId);
        dest.writeString(this.objectName);
        dest.writeString(this.deviceSerial);
        dest.writeString(this.channelType);
        dest.writeInt(this.channelNo);
        dest.writeString(this.alarmOccurTime);
        dest.writeInt(this.alarmType);
        dest.writeString(this.alarmPicUrl);
        dest.writeString(this.alarmRecUrl);
        dest.writeInt(this.checkState);
        dest.writeString(this.logInfo);
        dest.writeString(this.alarmStartTime);
        dest.writeByte((byte)(this.isCloud ? 1 : 0));
        dest.writeByte((byte)(this.isEncryption ? 1 : 0));
        dest.writeString(this.checkSum);
    }
}


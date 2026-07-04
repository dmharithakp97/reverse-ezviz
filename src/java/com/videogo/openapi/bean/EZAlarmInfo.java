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

public class EZAlarmInfo
implements Parcelable {
    @Serializable(name="alarmId")
    private String alarmId;
    @Serializable(name="alarmName")
    private String alarmName;
    @Serializable(name="channelNo")
    private int cameraNo = 0;
    @Serializable(name="alarmType")
    private int alarmType = 0;
    @Serializable(name="alarmPicUrl")
    private String alarmPicUrl = "";
    @Serializable(name="isChecked")
    private int isRead = 0;
    @Serializable(name="alarmStart")
    private String alarmStartTime = "";
    @Serializable(name="isEncrypt")
    private int isEncrypt = 0;
    @Serializable(name="delayTime")
    private int delayTime;
    @Serializable(name="preTime")
    private int preTime;
    @Serializable(name="deviceSerial")
    private String deviceSerial;
    @Serializable(name="customerType")
    private String customerType;
    @Serializable(name="customerInfo")
    private String customerInfo;
    @Serializable(name="deviceName")
    private String deviceName;
    @Serializable(name="category")
    private String category;
    @Serializable(name="recState")
    private int recState;
    @Serializable(name="crypt")
    private int crypt;
    @Serializable(name="checksum")
    private String checksum;
    public static final Parcelable.Creator<EZAlarmInfo> CREATOR = new Parcelable.Creator<EZAlarmInfo>(){

        public EZAlarmInfo createFromParcel(Parcel source) {
            return new EZAlarmInfo(source);
        }

        public EZAlarmInfo[] newArray(int size) {
            return new EZAlarmInfo[size];
        }
    };

    public String getAlarmId() {
        return this.alarmId;
    }

    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }

    public String getAlarmName() {
        return this.alarmName;
    }

    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
    }

    public int getIsRead() {
        return this.isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public int getCameraNo() {
        return this.cameraNo;
    }

    public void setCameraNo(int cameraNo) {
        this.cameraNo = cameraNo;
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

    public int getDelayTime() {
        return this.delayTime;
    }

    public void setDelayTime(int delayTime) {
        this.delayTime = delayTime;
    }

    public int getPreTime() {
        return this.preTime;
    }

    public void setPreTime(int preTime) {
        this.preTime = preTime;
    }

    public String getDeviceSerial() {
        return this.deviceSerial;
    }

    public void setDeviceSerial(String deviceSerial) {
        this.deviceSerial = deviceSerial;
    }

    public int getIsEncrypt() {
        return this.isEncrypt;
    }

    public void setIsEncrypt(int isEncrypt) {
        this.isEncrypt = isEncrypt;
    }

    public String getCustomerType() {
        return this.customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getCustomerInfo() {
        return this.customerInfo;
    }

    public void setCustomerInfo(String customerInfo) {
        this.customerInfo = customerInfo;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getRecState() {
        return this.recState;
    }

    public void setRecState(int recState) {
        this.recState = recState;
    }

    public int getCrypt() {
        return this.crypt;
    }

    public void setCrypt(int crypt) {
        this.crypt = crypt;
    }

    public String getChecksum() {
        return this.checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.alarmId);
        dest.writeString(this.alarmName);
        dest.writeInt(this.cameraNo);
        dest.writeInt(this.alarmType);
        dest.writeString(this.alarmPicUrl);
        dest.writeInt(this.isRead);
        dest.writeString(this.alarmStartTime);
        dest.writeInt(this.isEncrypt);
        dest.writeInt(this.delayTime);
        dest.writeInt(this.preTime);
        dest.writeString(this.deviceSerial);
        dest.writeString(this.customerType);
        dest.writeString(this.customerInfo);
        dest.writeString(this.deviceName);
        dest.writeString(this.category);
        dest.writeInt(this.recState);
        dest.writeInt(this.crypt);
        dest.writeString(this.checksum);
    }

    public EZAlarmInfo() {
    }

    protected EZAlarmInfo(Parcel in) {
        this.alarmId = in.readString();
        this.alarmName = in.readString();
        this.cameraNo = in.readInt();
        this.alarmType = in.readInt();
        this.alarmPicUrl = in.readString();
        this.isRead = in.readInt();
        this.alarmStartTime = in.readString();
        this.isEncrypt = in.readInt();
        this.delayTime = in.readInt();
        this.preTime = in.readInt();
        this.deviceSerial = in.readString();
        this.customerType = in.readString();
        this.customerInfo = in.readString();
        this.deviceName = in.readString();
        this.category = in.readString();
        this.recState = in.readInt();
        this.crypt = in.readInt();
        this.checksum = in.readString();
    }
}


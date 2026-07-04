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
import com.videogo.openapi.bean.EZCloudServicePackageInfo;
import java.util.List;

public class EZDeviceCloudServiceInfo
implements Parcelable {
    @Serializable(name="ownerId")
    private String ownerId;
    @Serializable(name="serial")
    private String deviceSerial;
    @Serializable(name="channelNo")
    private int mChannelNo;
    @Serializable(name="devicePicUrl")
    private String picUrl;
    @Serializable(name="storageTime")
    private int storageTime;
    @Serializable(name="storageTimeUnit")
    private int storageTimeUnit;
    @Serializable(name="expireTime")
    private String expireTime;
    @Serializable(name="expireDate")
    private String expireDate;
    @Serializable(name="devModel")
    private String devModel;
    @Serializable(name="devName")
    private String devName;
    @Serializable(name="onlineStatus")
    private int onlineStatus;
    @Serializable(name="status")
    private int status;
    @Serializable(name="expireDay")
    private int expireDay;
    @Serializable(name="validDay")
    private int validDay;
    @Serializable(name="createTime")
    private String createTime;
    @Serializable(name="designateDevice")
    private boolean designateDevice;
    @Serializable(name="subscribe")
    private boolean subscribe;
    @Serializable(name="supportSubscribe")
    private boolean supportSubscribe;
    @Serializable(name="supportCard")
    private boolean supportCard;
    @Serializable(name="openTryCloudShield")
    private int openTryCloudShield;
    @Serializable(name="serviceList")
    private List<EZCloudServicePackageInfo> serviceList;
    public static final Parcelable.Creator<EZDeviceCloudServiceInfo> CREATOR = new Parcelable.Creator<EZDeviceCloudServiceInfo>(){

        public EZDeviceCloudServiceInfo createFromParcel(Parcel in) {
            return new EZDeviceCloudServiceInfo(in);
        }

        public EZDeviceCloudServiceInfo[] newArray(int size) {
            return new EZDeviceCloudServiceInfo[size];
        }
    };

    public EZDeviceCloudServiceInfo() {
    }

    public String getOwnerId() {
        return this.ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getDeviceSerial() {
        return this.deviceSerial;
    }

    public void setDeviceSerial(String deviceSerial) {
        this.deviceSerial = deviceSerial;
    }

    public int getmChannelNo() {
        return this.mChannelNo;
    }

    public void setmChannelNo(int mChannelNo) {
        this.mChannelNo = mChannelNo;
    }

    public String getPicUrl() {
        return this.picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public int getStorageTime() {
        return this.storageTime;
    }

    public void setStorageTime(int storageTime) {
        this.storageTime = storageTime;
    }

    public int getStorageTimeUnit() {
        return this.storageTimeUnit;
    }

    public void setStorageTimeUnit(int storageTimeUnit) {
        this.storageTimeUnit = storageTimeUnit;
    }

    public String getExpireTime() {
        return this.expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public String getExpireDate() {
        return this.expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getDevModel() {
        return this.devModel;
    }

    public void setDevModel(String devModel) {
        this.devModel = devModel;
    }

    public String getDevName() {
        return this.devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public int getOnlineStatus() {
        return this.onlineStatus;
    }

    public void setOnlineStatus(int onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getExpireDay() {
        return this.expireDay;
    }

    public void setExpireDay(int expireDay) {
        this.expireDay = expireDay;
    }

    public int getValidDay() {
        return this.validDay;
    }

    public void setValidDay(int validDay) {
        this.validDay = validDay;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public boolean isDesignateDevice() {
        return this.designateDevice;
    }

    public void setDesignateDevice(boolean designateDevice) {
        this.designateDevice = designateDevice;
    }

    public boolean isSubscribe() {
        return this.subscribe;
    }

    public void setSubscribe(boolean subscribe) {
        this.subscribe = subscribe;
    }

    public boolean isSupportSubscribe() {
        return this.supportSubscribe;
    }

    public void setSupportSubscribe(boolean supportSubscribe) {
        this.supportSubscribe = supportSubscribe;
    }

    public boolean isSupportCard() {
        return this.supportCard;
    }

    public void setSupportCard(boolean supportCard) {
        this.supportCard = supportCard;
    }

    public int getOpenTryCloudShield() {
        return this.openTryCloudShield;
    }

    public void setOpenTryCloudShield(int openTryCloudShield) {
        this.openTryCloudShield = openTryCloudShield;
    }

    public List<EZCloudServicePackageInfo> getServiceList() {
        return this.serviceList;
    }

    public void setServiceList(List<EZCloudServicePackageInfo> serviceList) {
        this.serviceList = serviceList;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
    }

    protected EZDeviceCloudServiceInfo(Parcel in) {
    }
}


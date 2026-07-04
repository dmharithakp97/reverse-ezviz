/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package com.videogo.openapi.bean.resp;

import android.os.Parcel;
import android.os.Parcelable;
import com.videogo.openapi.annotation.Serializable;

public class CameraInfo
implements Parcelable {
    @Serializable(name="deviceId")
    private String deviceId;
    @Serializable(name="deviceSerial")
    private String deviceSerial;
    @Serializable(name="deviceName")
    private String deviceName;
    @Serializable(name="cameraId")
    private String cameraId;
    @Serializable(name="cameraNo")
    private int cameraNo;
    @Serializable(name="cameraName")
    private String cameraName;
    @Serializable(name="status")
    private int status;
    @Serializable(name="isShared")
    private int isShared;
    @Serializable(name="picUrl")
    private String picUrl;
    @Serializable(name="isEncrypt")
    private int isEncrypt;
    @Serializable(name="defence")
    private int defence;
    public static final Parcelable.Creator<CameraInfo> CREATOR = new Parcelable.Creator<CameraInfo>(){

        public CameraInfo createFromParcel(Parcel in) {
            return new CameraInfo(in);
        }

        public CameraInfo[] newArray(int size) {
            return new CameraInfo[size];
        }
    };

    public CameraInfo() {
    }

    public String getPicUrl() {
        return this.picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getCameraId() {
        return this.cameraId;
    }

    public void setCameraId(String cameraId) {
        this.cameraId = cameraId;
    }

    public int getCameraNo() {
        return this.cameraNo;
    }

    public void setCameraNo(int cameraNo) {
        this.cameraNo = cameraNo;
    }

    public String getCameraName() {
        return this.cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIsShared() {
        return this.isShared;
    }

    public void setIsShared(int isShared) {
        this.isShared = isShared;
    }

    public int getIsEncrypt() {
        return this.isEncrypt;
    }

    public void setIsEncrypt(int isEncrypt) {
        this.isEncrypt = isEncrypt;
    }

    public int getDefence() {
        return this.defence;
    }

    public void setDefence(int defence) {
        this.defence = defence;
    }

    public String getDeviceSerial() {
        return this.deviceSerial;
    }

    public void setDeviceSerial(String deviceSerial) {
        this.deviceSerial = deviceSerial;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    protected CameraInfo(Parcel in) {
        this.deviceId = in.readString();
        this.deviceSerial = in.readString();
        this.deviceName = in.readString();
        this.cameraId = in.readString();
        this.cameraNo = in.readInt();
        this.cameraName = in.readString();
        this.status = in.readInt();
        this.isShared = in.readInt();
        this.picUrl = in.readString();
        this.isEncrypt = in.readInt();
        this.defence = in.readInt();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.deviceId);
        dest.writeString(this.deviceSerial);
        dest.writeString(this.deviceName);
        dest.writeString(this.cameraId);
        dest.writeInt(this.cameraNo);
        dest.writeString(this.cameraName);
        dest.writeInt(this.status);
        dest.writeInt(this.isShared);
        dest.writeString(this.picUrl);
        dest.writeInt(this.isEncrypt);
        dest.writeInt(this.defence);
    }
}


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

@Serializable
public class EZHiddnsDeviceInfo
implements Parcelable {
    @Serializable(name="upnpMappingMode")
    private int upnpMappingMode;
    @Serializable(name="mappingHiddnsHttpPort")
    private int mappingHiddnsHttpPort;
    @Serializable(name="hiddnsHttpPort")
    private int hiddnsHttpPort;
    @Serializable(name="hiddnsHttpsPort")
    private int hiddnsHttpsPort;
    @Serializable(name="mappingHiddnsCmdPort")
    private int mappingHiddnsCmdPort;
    @Serializable(name="hiddnsCmdPort")
    private int hiddnsCmdPort;
    @Serializable(name="hiddnsRtspPort")
    private int hiddnsRtspPort;
    @Serializable(name="domain")
    private String domain;
    @Serializable(name="deviceIp")
    private String deviceIp;
    @Serializable(name="subSerial")
    private String subSerial;
    @Serializable(name="serial")
    private String serial;
    @Serializable(name="deviceName")
    private String deviceName;
    public static final Parcelable.Creator<EZHiddnsDeviceInfo> CREATOR = new Parcelable.Creator<EZHiddnsDeviceInfo>(){

        public EZHiddnsDeviceInfo createFromParcel(Parcel source) {
            return new EZHiddnsDeviceInfo(source);
        }

        public EZHiddnsDeviceInfo[] newArray(int size) {
            return new EZHiddnsDeviceInfo[size];
        }
    };

    public EZHiddnsDeviceInfo() {
    }

    public String getDeviceIp() {
        return this.deviceIp;
    }

    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
    }

    public String getSubSerial() {
        return this.subSerial;
    }

    public void setSubSerial(String subSerial) {
        this.subSerial = subSerial;
    }

    public String getSerial() {
        return this.serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public int getUpnpMappingMode() {
        return this.upnpMappingMode;
    }

    public void setUpnpMappingMode(int upnpMappingMode) {
        this.upnpMappingMode = upnpMappingMode;
    }

    public int getMappingHiddnsHttpPort() {
        return this.mappingHiddnsHttpPort;
    }

    public void setMappingHiddnsHttpPort(int mappingHiddnsHttpPort) {
        this.mappingHiddnsHttpPort = mappingHiddnsHttpPort;
    }

    public int getHiddnsHttpPort() {
        return this.hiddnsHttpPort;
    }

    public void setHiddnsHttpPort(int hiddnsHttpPort) {
        this.hiddnsHttpPort = hiddnsHttpPort;
    }

    public int getHiddnsHttpsPort() {
        return this.hiddnsHttpsPort;
    }

    public void setHiddnsHttpsPort(int hiddnsHttpsPort) {
        this.hiddnsHttpsPort = hiddnsHttpsPort;
    }

    public int getMappingHiddnsCmdPort() {
        return this.mappingHiddnsCmdPort;
    }

    public void setMappingHiddnsCmdPort(int mappingHiddnsCmdPort) {
        this.mappingHiddnsCmdPort = mappingHiddnsCmdPort;
    }

    public int getHiddnsCmdPort() {
        return this.hiddnsCmdPort;
    }

    public void setHiddnsCmdPort(int hiddnsCmdPort) {
        this.hiddnsCmdPort = hiddnsCmdPort;
    }

    public int getHiddnsRtspPort() {
        return this.hiddnsRtspPort;
    }

    public void setHiddnsRtspPort(int hiddnsRtspPort) {
        this.hiddnsRtspPort = hiddnsRtspPort;
    }

    public String getDomain() {
        return this.domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.upnpMappingMode);
        dest.writeInt(this.mappingHiddnsHttpPort);
        dest.writeInt(this.hiddnsHttpPort);
        dest.writeInt(this.hiddnsHttpsPort);
        dest.writeInt(this.mappingHiddnsCmdPort);
        dest.writeInt(this.hiddnsCmdPort);
        dest.writeInt(this.hiddnsRtspPort);
        dest.writeString(this.domain);
        dest.writeString(this.deviceIp);
        dest.writeString(this.subSerial);
        dest.writeString(this.serial);
        dest.writeString(this.deviceName);
    }

    protected EZHiddnsDeviceInfo(Parcel in) {
        this.upnpMappingMode = in.readInt();
        this.mappingHiddnsHttpPort = in.readInt();
        this.hiddnsHttpPort = in.readInt();
        this.hiddnsHttpsPort = in.readInt();
        this.mappingHiddnsCmdPort = in.readInt();
        this.hiddnsCmdPort = in.readInt();
        this.hiddnsRtspPort = in.readInt();
        this.domain = in.readString();
        this.deviceIp = in.readString();
        this.subSerial = in.readString();
        this.serial = in.readString();
        this.deviceName = in.readString();
    }
}


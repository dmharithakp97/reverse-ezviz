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
import com.videogo.openapi.annotation.Serializable;

@Serializable
public class DeviceWifiInfo
implements Parcelable {
    @Serializable(name="ssid")
    private String ssid;
    @Serializable(name="netType")
    private String netType;
    @Serializable(name="signal")
    private int signal;
    public static final Parcelable.Creator<DeviceWifiInfo> CREATOR = new Parcelable.Creator<DeviceWifiInfo>(){

        public DeviceWifiInfo createFromParcel(Parcel in) {
            return new DeviceWifiInfo(in);
        }

        public DeviceWifiInfo[] newArray(int size) {
            return new DeviceWifiInfo[size];
        }
    };

    public DeviceWifiInfo() {
    }

    public String getSsid() {
        return this.ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getNetType() {
        return this.netType;
    }

    public void setNetType(String netType) {
        this.netType = netType;
    }

    public int getSignal() {
        return this.signal;
    }

    public void setSignal(int signal) {
        this.signal = signal;
    }

    protected DeviceWifiInfo(Parcel in) {
        this.ssid = in.readString();
        this.netType = in.readString();
        this.signal = in.readInt();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ssid);
        dest.writeString(this.netType);
        dest.writeInt(this.signal);
    }
}


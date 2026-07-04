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

public class DeviceSwitchInfo
implements Parcelable {
    @Serializable(name="subSerial")
    private String subSerial;
    @Serializable(name="channelNo")
    private int channelNo;
    @Serializable(name="type")
    private int type;
    @Serializable(name="enable")
    private boolean enable;
    public static final Parcelable.Creator<DeviceSwitchInfo> CREATOR = new Parcelable.Creator<DeviceSwitchInfo>(){

        public DeviceSwitchInfo createFromParcel(Parcel in) {
            return new DeviceSwitchInfo(in);
        }

        public DeviceSwitchInfo[] newArray(int size) {
            return new DeviceSwitchInfo[size];
        }
    };

    public DeviceSwitchInfo() {
    }

    public String getSubSerial() {
        return this.subSerial;
    }

    public void setSubSerial(String subSerial) {
        this.subSerial = subSerial;
    }

    public int getChannelNo() {
        return this.channelNo;
    }

    public void setChannelNo(int channelNo) {
        this.channelNo = channelNo;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isEnable() {
        return this.enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    protected DeviceSwitchInfo(Parcel in) {
        this.subSerial = in.readString();
        this.channelNo = in.readInt();
        this.type = in.readInt();
        this.enable = in.readInt() == 1;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.subSerial);
        dest.writeInt(this.channelNo);
        dest.writeInt(this.type);
        dest.writeInt(this.enable ? 1 : 0);
    }
}


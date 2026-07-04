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

public class DeviceOnlineInfo
implements Parcelable {
    @Serializable(name="subSerial")
    private String subSerial;
    @Serializable(name="onlinePlan")
    private int onlinePlan;
    @Serializable(name="onlineTimeBegin")
    private String onlineTimeBegin;
    @Serializable(name="onlineTimeEnd")
    private String onlineTimeEnd;
    @Serializable(name="onlineWeek")
    private String onlineWeek;
    public static final Parcelable.Creator<DeviceOnlineInfo> CREATOR = new Parcelable.Creator<DeviceOnlineInfo>(){

        public DeviceOnlineInfo createFromParcel(Parcel in) {
            return new DeviceOnlineInfo(in);
        }

        public DeviceOnlineInfo[] newArray(int size) {
            return new DeviceOnlineInfo[size];
        }
    };

    public DeviceOnlineInfo() {
    }

    public String getSubSerial() {
        return this.subSerial;
    }

    public void setSubSerial(String subSerial) {
        this.subSerial = subSerial;
    }

    public int getOnlinePlan() {
        return this.onlinePlan;
    }

    public void setOnlinePlan(int onlinePlan) {
        this.onlinePlan = onlinePlan;
    }

    public void setOnlineTimeBegin(String onlineTimeBegin) {
        this.onlineTimeBegin = onlineTimeBegin;
    }

    public String getOnlineTimeBegin() {
        return this.onlineTimeBegin;
    }

    public void setOnlineTimeEnd(String onlineTimeEnd) {
        this.onlineTimeEnd = onlineTimeEnd;
    }

    public String getOnlineWeek() {
        return this.onlineWeek;
    }

    public void setOnlineWeek(String onlineWeek) {
        this.onlineWeek = onlineWeek;
    }

    public String getOnlineTimeEnd() {
        return this.onlineTimeEnd;
    }

    protected DeviceOnlineInfo(Parcel in) {
        this.onlinePlan = in.readInt();
        this.onlineTimeBegin = in.readString();
        this.onlineTimeEnd = in.readString();
        this.onlineWeek = in.readString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.onlinePlan);
        dest.writeString(this.onlineTimeBegin);
        dest.writeString(this.onlineTimeEnd);
        dest.writeString(this.onlineWeek);
    }
}


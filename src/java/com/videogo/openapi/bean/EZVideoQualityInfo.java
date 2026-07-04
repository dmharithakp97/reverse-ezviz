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
public class EZVideoQualityInfo
implements Parcelable {
    @Serializable(name="videoQualityName")
    private String videoQualityName;
    @Serializable(name="videoLevel")
    private int videoLevel;
    @Serializable(name="streamType")
    private int streamType;
    public static final Parcelable.Creator<EZVideoQualityInfo> CREATOR = new Parcelable.Creator<EZVideoQualityInfo>(){

        public EZVideoQualityInfo createFromParcel(Parcel source) {
            return new EZVideoQualityInfo(source);
        }

        public EZVideoQualityInfo[] newArray(int size) {
            return new EZVideoQualityInfo[size];
        }
    };

    public String getVideoQualityName() {
        return this.videoQualityName;
    }

    public void setVideoQualityName(String videoQualityName) {
        this.videoQualityName = videoQualityName;
    }

    public int getVideoLevel() {
        return this.videoLevel;
    }

    public void setVideoLevel(int videoLevel) {
        this.videoLevel = videoLevel;
    }

    public int getStreamType() {
        return this.streamType;
    }

    public void setStreamType(int streamType) {
        this.streamType = streamType;
    }

    public EZVideoQualityInfo() {
    }

    public EZVideoQualityInfo(String videoQualityName, int videoLevel) {
        this.videoQualityName = videoQualityName;
        this.videoLevel = videoLevel;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.videoQualityName);
        dest.writeInt(this.videoLevel);
        dest.writeInt(this.streamType);
    }

    protected EZVideoQualityInfo(Parcel in) {
        this.videoQualityName = in.readString();
        this.videoLevel = in.readInt();
        this.streamType = in.readInt();
    }
}


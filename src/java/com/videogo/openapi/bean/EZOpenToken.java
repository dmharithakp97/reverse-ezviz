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

public class EZOpenToken
implements Parcelable {
    @Serializable(name="accessToken")
    private String accessToken;
    @Serializable(name="openauthAddr")
    private String openauthAddr;
    @Serializable(name="openapiAddr")
    private String openapiAddr;
    @Serializable(name="expireTime")
    private long expireTime;
    @Serializable(name="area")
    private int area;
    public static final Parcelable.Creator<EZOpenToken> CREATOR = new Parcelable.Creator<EZOpenToken>(){

        public EZOpenToken createFromParcel(Parcel source) {
            return new EZOpenToken(source);
        }

        public EZOpenToken[] newArray(int size) {
            return new EZOpenToken[size];
        }
    };

    public String getAccessToken() {
        return this.accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getOpenauthAddr() {
        return this.openauthAddr;
    }

    public void setOpenauthAddr(String openauthAddr) {
        this.openauthAddr = openauthAddr;
    }

    public String getOpenapiAddr() {
        return this.openapiAddr;
    }

    public void setOpenapiAddr(String openapiAddr) {
        this.openapiAddr = openapiAddr;
    }

    public long getExpireTime() {
        return this.expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public int getArea() {
        return this.area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.accessToken);
        dest.writeString(this.openauthAddr);
        dest.writeString(this.openapiAddr);
        dest.writeLong(this.expireTime);
        dest.writeInt(this.area);
    }

    public EZOpenToken() {
    }

    protected EZOpenToken(Parcel in) {
        this.accessToken = in.readString();
        this.openauthAddr = in.readString();
        this.openapiAddr = in.readString();
        this.expireTime = in.readLong();
        this.area = in.readInt();
    }
}


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

public class EZAccessToken
implements Parcelable {
    private String accessToken;
    private long expire;
    public static final Parcelable.Creator<EZAccessToken> CREATOR = new Parcelable.Creator<EZAccessToken>(){

        public EZAccessToken createFromParcel(Parcel source) {
            return new EZAccessToken(source);
        }

        public EZAccessToken[] newArray(int size) {
            return new EZAccessToken[size];
        }
    };

    public String getAccessToken() {
        return this.accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getExpire() {
        return this.expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.accessToken);
        dest.writeLong(this.expire);
    }

    public EZAccessToken() {
    }

    protected EZAccessToken(Parcel in) {
        this.accessToken = in.readString();
        this.expire = in.readLong();
    }
}


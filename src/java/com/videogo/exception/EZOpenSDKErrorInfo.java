/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package com.videogo.exception;

import android.os.Parcel;
import android.os.Parcelable;
import com.videogo.openapi.annotation.Serializable;

public class EZOpenSDKErrorInfo
implements Parcelable {
    @Serializable(name="moduleCode")
    public String moduleCode;
    @Serializable(name="detailCode")
    public String detailCode;
    @Serializable(name="description")
    public String description;
    @Serializable(name="solution")
    public String solution;
    @Serializable(name="updateTime")
    public long updateTime;
    public static final Parcelable.Creator<EZOpenSDKErrorInfo> CREATOR = new Parcelable.Creator<EZOpenSDKErrorInfo>(){

        public EZOpenSDKErrorInfo createFromParcel(Parcel source) {
            return new EZOpenSDKErrorInfo(source);
        }

        public EZOpenSDKErrorInfo[] newArray(int size) {
            return new EZOpenSDKErrorInfo[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.moduleCode);
        dest.writeString(this.detailCode);
        dest.writeString(this.description);
        dest.writeString(this.solution);
        dest.writeLong(this.updateTime);
    }

    public EZOpenSDKErrorInfo() {
    }

    protected EZOpenSDKErrorInfo(Parcel in) {
        this.moduleCode = in.readString();
        this.detailCode = in.readString();
        this.description = in.readString();
        this.solution = in.readString();
        this.updateTime = in.readLong();
    }
}


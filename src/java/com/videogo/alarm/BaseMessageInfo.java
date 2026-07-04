/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package com.videogo.alarm;

import android.os.Parcel;
import android.os.Parcelable;

public class BaseMessageInfo
implements Parcelable {
    private int notifyType;
    public static final Parcelable.Creator<BaseMessageInfo> CREATOR = new Parcelable.Creator<BaseMessageInfo>(){

        public BaseMessageInfo createFromParcel(Parcel in) {
            return new BaseMessageInfo(in);
        }

        public BaseMessageInfo[] newArray(int size) {
            return new BaseMessageInfo[size];
        }
    };

    public BaseMessageInfo() {
    }

    public int getNotifyType() {
        return this.notifyType;
    }

    public void setNotifyType(int notifyType) {
        this.notifyType = notifyType;
    }

    public void copy(BaseMessageInfo pushInfo) {
        this.notifyType = pushInfo.notifyType;
    }

    protected BaseMessageInfo(Parcel in) {
        this.notifyType = in.readInt();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.notifyType);
    }
}


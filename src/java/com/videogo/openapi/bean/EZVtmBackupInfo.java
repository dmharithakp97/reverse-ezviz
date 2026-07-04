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

public class EZVtmBackupInfo
implements Parcelable {
    @Serializable(name="ip")
    private String vtmIp;
    @Serializable(name="port")
    private int vtmPort;
    public static final Parcelable.Creator<EZVtmBackupInfo> CREATOR = new Parcelable.Creator<EZVtmBackupInfo>(){

        public EZVtmBackupInfo createFromParcel(Parcel in) {
            return new EZVtmBackupInfo(in);
        }

        public EZVtmBackupInfo[] newArray(int size) {
            return new EZVtmBackupInfo[size];
        }
    };

    public EZVtmBackupInfo() {
    }

    public String getVtmIp() {
        return this.vtmIp;
    }

    public void setVtmIp(String vtmIp) {
        this.vtmIp = vtmIp;
    }

    public int getVtmPort() {
        return this.vtmPort;
    }

    public void setVtmPort(int vtmPort) {
        this.vtmPort = vtmPort;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.vtmIp);
        parcel.writeInt(this.vtmPort);
    }

    protected EZVtmBackupInfo(Parcel in) {
        this.vtmIp = in.readString();
        this.vtmPort = in.readInt();
    }
}


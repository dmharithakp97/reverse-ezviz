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

public class EZRelayServeInfo
implements Parcelable {
    @Serializable(name="domain")
    private String domain;
    @Serializable(name="port")
    private int port;
    @Serializable(name="key")
    private String key;
    @Serializable(name="version")
    private int version;
    public static final Parcelable.Creator<EZRelayServeInfo> CREATOR = new Parcelable.Creator<EZRelayServeInfo>(){

        public EZRelayServeInfo createFromParcel(Parcel in) {
            return new EZRelayServeInfo(in);
        }

        public EZRelayServeInfo[] newArray(int size) {
            return new EZRelayServeInfo[size];
        }
    };

    public String getDomain() {
        return this.domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.domain);
        dest.writeInt(this.port);
        dest.writeString(this.key);
        dest.writeInt(this.version);
    }

    public EZRelayServeInfo() {
    }

    protected EZRelayServeInfo(Parcel in) {
        this.domain = in.readString();
        this.port = in.readInt();
        this.key = in.readString();
        this.version = in.readInt();
    }
}


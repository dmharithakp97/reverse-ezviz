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

public class EZBWCheckAddressInfo
implements Parcelable {
    @Serializable(name="id")
    private String opId = null;
    @Serializable(name="url")
    private String url = null;
    @Serializable(name="publicKey")
    private String publicKey = null;
    @Serializable(name="version")
    private int version;
    @Serializable(name="expireTime")
    private String expireTime = null;
    @Serializable(name="operator")
    private String isp = null;
    @Serializable(name="exIp")
    private String exIp = null;
    public static final Parcelable.Creator<EZBWCheckAddressInfo> CREATOR = new Parcelable.Creator<EZBWCheckAddressInfo>(){

        public EZBWCheckAddressInfo createFromParcel(Parcel in) {
            return new EZBWCheckAddressInfo(in);
        }

        public EZBWCheckAddressInfo[] newArray(int size) {
            return new EZBWCheckAddressInfo[size];
        }
    };

    public EZBWCheckAddressInfo() {
    }

    public String getOpId() {
        return this.opId;
    }

    public void setOpId(String opId) {
        this.opId = opId;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPublicKey() {
        return this.publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getExpireTime() {
        return this.expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public String getIsp() {
        return this.isp;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }

    public String getExIp() {
        return this.exIp;
    }

    public void setExIp(String exIp) {
        this.exIp = exIp;
    }

    protected EZBWCheckAddressInfo(Parcel in) {
        this.opId = in.readString();
        this.url = in.readString();
        this.publicKey = in.readString();
        this.version = in.readInt();
        this.expireTime = in.readString();
        this.isp = in.readString();
        this.exIp = in.readString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.opId);
        dest.writeString(this.url);
        dest.writeString(this.publicKey);
        dest.writeInt(this.version);
        dest.writeString(this.expireTime);
        dest.writeString(this.isp);
        dest.writeString(this.exIp);
    }

    public int describeContents() {
        return 0;
    }
}


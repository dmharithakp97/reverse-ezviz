/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable$Creator
 */
package com.videogo.alarm;

import android.os.Parcel;
import android.os.Parcelable;
import com.videogo.alarm.BaseMessageInfo;

public class NoticeInfo
extends BaseMessageInfo {
    private int infoType = 0;
    private String infoContant = null;
    private String url1 = null;
    private String url2 = null;
    public static final Parcelable.Creator<NoticeInfo> CREATOR = new Parcelable.Creator<NoticeInfo>(){

        public NoticeInfo createFromParcel(Parcel in) {
            return new NoticeInfo(in);
        }

        public NoticeInfo[] newArray(int size) {
            return new NoticeInfo[size];
        }
    };

    public NoticeInfo() {
    }

    public int getInfoType() {
        return this.infoType;
    }

    public void setInfoType(int infoType) {
        this.infoType = infoType;
    }

    public String getInfoContant() {
        return this.infoContant;
    }

    public void setInfoContant(String infoContant) {
        this.infoContant = infoContant;
    }

    public String getUrl1() {
        return this.url1;
    }

    public void setUrl1(String url1) {
        this.url1 = url1;
    }

    public String getUrl2() {
        return this.url2;
    }

    public void setUrl2(String url2) {
        this.url2 = url2;
    }

    public void copy(NoticeInfo noticeInfo) {
        this.infoType = noticeInfo.infoType;
        this.infoContant = noticeInfo.infoContant;
        this.url1 = noticeInfo.url1;
        this.url2 = noticeInfo.url2;
    }

    protected NoticeInfo(Parcel in) {
        super(in);
        this.infoType = in.readInt();
        this.infoContant = in.readString();
        this.url1 = in.readString();
        this.url2 = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.infoType);
        dest.writeString(this.infoContant);
        dest.writeString(this.url1);
        dest.writeString(this.url2);
    }
}


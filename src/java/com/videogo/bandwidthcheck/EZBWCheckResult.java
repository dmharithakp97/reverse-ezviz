/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package com.videogo.bandwidthcheck;

import android.os.Parcel;
import android.os.Parcelable;
import com.videogo.util.DateTimeUtil;
import java.util.Calendar;

public class EZBWCheckResult
implements Parcelable {
    public String deviceSerial;
    public int cameraNo;
    public String isp = null;
    public String exIp = null;
    public int checkType;
    public float downloadSpeed;
    public float uploadSpeed;
    public int latency;
    public int loss;
    public int result;
    public String checkTime;
    public String networkName;
    public static final Parcelable.Creator<EZBWCheckResult> CREATOR = new Parcelable.Creator<EZBWCheckResult>(){

        public EZBWCheckResult createFromParcel(Parcel in) {
            return new EZBWCheckResult(in);
        }

        public EZBWCheckResult[] newArray(int size) {
            return new EZBWCheckResult[size];
        }
    };

    public EZBWCheckResult() {
    }

    public EZBWCheckResult(String deviceSerial, int cameraNo, int checkType, String exIp, String isp) {
        this.deviceSerial = deviceSerial;
        this.cameraNo = cameraNo;
        this.checkType = checkType;
        this.exIp = exIp;
        this.isp = isp;
        this.checkTime = DateTimeUtil.calendarToYMDHMSString(Calendar.getInstance());
    }

    protected EZBWCheckResult(Parcel in) {
        this.deviceSerial = in.readString();
        this.cameraNo = in.readInt();
        this.exIp = in.readString();
        this.isp = in.readString();
        this.checkType = in.readInt();
        this.downloadSpeed = in.readFloat();
        this.uploadSpeed = in.readFloat();
        this.latency = in.readInt();
        this.loss = in.readInt();
        this.result = in.readInt();
        this.checkTime = in.readString();
        this.networkName = in.readString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.deviceSerial);
        dest.writeInt(this.cameraNo);
        dest.writeString(this.exIp);
        dest.writeString(this.isp);
        dest.writeInt(this.checkType);
        dest.writeFloat(this.downloadSpeed);
        dest.writeFloat(this.uploadSpeed);
        dest.writeInt(this.latency);
        dest.writeInt(this.loss);
        dest.writeInt(this.result);
        dest.writeString(this.checkTime);
        dest.writeString(this.networkName);
    }
}


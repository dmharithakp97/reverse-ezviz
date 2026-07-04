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

public class EZDetectorInfo
implements Parcelable {
    @Serializable(name="detectorSerial")
    private String detectorSerial;
    @Serializable(name="detectorType")
    private String detectorType;
    @Serializable(name="detectorState")
    private int detectorState = 1;
    @Serializable(name="detectorTypeName")
    private String detectorTypeName;
    @Serializable(name="zfStatus")
    private int faultZoneStatus;
    @Serializable(name="uvStatus")
    private int underVoltageStatus;
    @Serializable(name="iwcStatus")
    private int wirelessInterferenceStatus;
    @Serializable(name="olStatus")
    private int offlineStatus;
    @Serializable(name="atHomeEnable")
    private int atHomeEnable;
    @Serializable(name="outerEnable")
    private int outerEnable;
    @Serializable(name="sleepEnable")
    private int sleepEnable;
    @Serializable(name="location")
    private String location;
    public static final Parcelable.Creator<EZDetectorInfo> CREATOR = new Parcelable.Creator<EZDetectorInfo>(){

        public EZDetectorInfo createFromParcel(Parcel source) {
            return new EZDetectorInfo(source);
        }

        public EZDetectorInfo[] newArray(int size) {
            return new EZDetectorInfo[size];
        }
    };

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDetectorSerial(String detectorSerial) {
        this.detectorSerial = detectorSerial;
    }

    public void setDetectorType(String detectorType) {
        this.detectorType = detectorType;
    }

    public void setDetectorState(int detectorState) {
        this.detectorState = detectorState;
    }

    public void setDetectorTypeName(String detectorTypeName) {
        this.detectorTypeName = detectorTypeName;
    }

    public void setFaultZoneStatus(int faultZoneStatus) {
        this.faultZoneStatus = faultZoneStatus;
    }

    public void setUnderVoltageStatus(int underVoltageStatus) {
        this.underVoltageStatus = underVoltageStatus;
    }

    public void setWirelessInterferenceStatus(int wirelessInterferenceStatus) {
        this.wirelessInterferenceStatus = wirelessInterferenceStatus;
    }

    public void setOfflineStatus(int offlineStatus) {
        this.offlineStatus = offlineStatus;
    }

    public void setAtHomeEnable(int atHomeEnable) {
        this.atHomeEnable = atHomeEnable;
    }

    public void setOuterEnable(int outerEnable) {
        this.outerEnable = outerEnable;
    }

    public void setSleepEnable(int sleepEnable) {
        this.sleepEnable = sleepEnable;
    }

    public String getDetectorSerial() {
        return this.detectorSerial;
    }

    public String getDetectorType() {
        return this.detectorType;
    }

    public int getDetectorState() {
        return this.detectorState;
    }

    public String getDetectorTypeName() {
        return this.detectorTypeName;
    }

    public int getFaultZoneStatus() {
        return this.faultZoneStatus;
    }

    public int getUnderVoltageStatus() {
        return this.underVoltageStatus;
    }

    public int getWirelessInterferenceStatus() {
        return this.wirelessInterferenceStatus;
    }

    public int getOfflineStatus() {
        return this.offlineStatus;
    }

    public int getAtHomeEnable() {
        return this.atHomeEnable;
    }

    public int getOuterEnable() {
        return this.outerEnable;
    }

    public int getSleepEnable() {
        return this.sleepEnable;
    }

    public EZDetectorInfo() {
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.detectorSerial);
        dest.writeString(this.detectorType);
        dest.writeInt(this.detectorState);
        dest.writeString(this.detectorTypeName);
        dest.writeInt(this.faultZoneStatus);
        dest.writeInt(this.underVoltageStatus);
        dest.writeInt(this.wirelessInterferenceStatus);
        dest.writeInt(this.offlineStatus);
        dest.writeInt(this.atHomeEnable);
        dest.writeInt(this.outerEnable);
        dest.writeInt(this.sleepEnable);
        dest.writeString(this.location);
    }

    protected EZDetectorInfo(Parcel in) {
        this.detectorSerial = in.readString();
        this.detectorType = in.readString();
        this.detectorState = in.readInt();
        this.detectorTypeName = in.readString();
        this.faultZoneStatus = in.readInt();
        this.underVoltageStatus = in.readInt();
        this.wirelessInterferenceStatus = in.readInt();
        this.offlineStatus = in.readInt();
        this.atHomeEnable = in.readInt();
        this.outerEnable = in.readInt();
        this.sleepEnable = in.readInt();
        this.location = in.readString();
    }
}


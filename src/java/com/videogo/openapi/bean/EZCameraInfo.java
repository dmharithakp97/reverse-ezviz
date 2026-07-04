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
import com.videogo.openapi.EZConstants;
import com.videogo.openapi.annotation.Serializable;
import com.videogo.openapi.bean.EZVideoQualityInfo;
import java.util.ArrayList;

public class EZCameraInfo
implements Parcelable {
    @Serializable(name="deviceSerial")
    protected String deviceSerial = null;
    @Serializable(name="cameraNo")
    protected int cameraNo = 0;
    @Serializable(name="cameraName")
    protected String cameraName = null;
    @Serializable(name="isShared")
    protected int isShared = 0;
    @Serializable(name="cameraCover")
    protected String cameraCover = null;
    @Serializable(name="videoLevel")
    protected int videoLevel = 0;
    protected ArrayList<EZVideoQualityInfo> videoQualityInfos;
    protected int permission = -1;
    public static final Parcelable.Creator<EZCameraInfo> CREATOR = new Parcelable.Creator<EZCameraInfo>(){

        public EZCameraInfo createFromParcel(Parcel source) {
            return new EZCameraInfo(source);
        }

        public EZCameraInfo[] newArray(int size) {
            return new EZCameraInfo[size];
        }
    };

    public EZCameraInfo() {
    }

    public String getDeviceSerial() {
        return this.deviceSerial;
    }

    public int getCameraNo() {
        return this.cameraNo;
    }

    public String getCameraName() {
        return this.cameraName;
    }

    public int getIsShared() {
        return this.isShared;
    }

    public String getCameraCover() {
        return this.cameraCover;
    }

    public void setDeviceSerial(String deviceSerial) {
        this.deviceSerial = deviceSerial;
    }

    public void setCameraNo(int cameraNo) {
        this.cameraNo = cameraNo;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    public void setIsShared(int isShared) {
        this.isShared = isShared;
    }

    public void setCameraCover(String cameraCover) {
        this.cameraCover = cameraCover;
    }

    public void setVideoLevel(int videoLevel) {
        this.videoLevel = videoLevel;
    }

    public ArrayList<EZVideoQualityInfo> getVideoQualityInfos() {
        return this.videoQualityInfos;
    }

    public void setVideoQualityInfos(ArrayList<EZVideoQualityInfo> videoQualityInfos) {
        this.videoQualityInfos = videoQualityInfos;
    }

    public int getPermission() {
        return this.permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    private void setVideoLevel(EZConstants.EZVideoLevel videoLevel) {
        if (videoLevel == EZConstants.EZVideoLevel.VIDEO_LEVEL_FLUNET) {
            this.setVideoLevel(0);
        } else if (videoLevel == EZConstants.EZVideoLevel.VIDEO_LEVEL_BALANCED) {
            this.setVideoLevel(1);
        } else if (videoLevel == EZConstants.EZVideoLevel.VIDEO_LEVEL_HD) {
            this.setVideoLevel(2);
        } else if (videoLevel == EZConstants.EZVideoLevel.VIDEO_LEVEL_SUPERCLEAR) {
            this.setVideoLevel(3);
        }
    }

    public EZConstants.EZVideoLevel getVideoLevel() {
        switch (this.videoLevel) {
            case 0: {
                return EZConstants.EZVideoLevel.VIDEO_LEVEL_FLUNET;
            }
            case 1: {
                return EZConstants.EZVideoLevel.VIDEO_LEVEL_BALANCED;
            }
            case 2: {
                return EZConstants.EZVideoLevel.VIDEO_LEVEL_HD;
            }
            case 3: {
                return EZConstants.EZVideoLevel.VIDEO_LEVEL_SUPERCLEAR;
            }
            case 4: {
                return EZConstants.EZVideoLevel.VIDEO_LEVEL_EXTREMECLEAR;
            }
            case 5: {
                return EZConstants.EZVideoLevel.VIDEO_LEVEL_3K;
            }
            case 6: {
                return EZConstants.EZVideoLevel.VIDEO_LEVEL_4K;
            }
        }
        return EZConstants.EZVideoLevel.VIDEO_LEVEL_FLUNET;
    }

    public boolean isCamera() {
        return true;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.deviceSerial);
        dest.writeInt(this.cameraNo);
        dest.writeString(this.cameraName);
        dest.writeInt(this.isShared);
        dest.writeString(this.cameraCover);
        dest.writeInt(this.videoLevel);
        dest.writeTypedList(this.videoQualityInfos);
        dest.writeInt(this.permission);
    }

    protected EZCameraInfo(Parcel in) {
        this.deviceSerial = in.readString();
        this.cameraNo = in.readInt();
        this.cameraName = in.readString();
        this.isShared = in.readInt();
        this.cameraCover = in.readString();
        this.videoLevel = in.readInt();
        this.videoQualityInfos = in.createTypedArrayList(EZVideoQualityInfo.CREATOR);
        this.permission = in.readInt();
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.text.TextUtils
 */
package com.videogo.openapi.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.videogo.openapi.EZConstants;
import com.videogo.openapi.annotation.Serializable;
import com.videogo.openapi.bean.EZCameraInfo;
import com.videogo.openapi.bean.EZVideoQualityInfo;
import com.videogo.util.LogUtil;
import java.util.List;

public class EZSubDeviceInfo
extends EZCameraInfo
implements Parcelable {
    @Serializable(name="resourceType")
    private int resourceType = 0;
    @Serializable(name="resourceName")
    protected String resourceName = null;
    @Serializable(name="supportExtShort")
    private String supportExtShort = null;
    private String[] supportExtValues;
    public static final Parcelable.Creator<EZSubDeviceInfo> CREATOR = new Parcelable.Creator<EZSubDeviceInfo>(){

        public EZSubDeviceInfo createFromParcel(Parcel source) {
            return new EZSubDeviceInfo(source);
        }

        public EZSubDeviceInfo[] newArray(int size) {
            return new EZSubDeviceInfo[size];
        }
    };

    public EZSubDeviceInfo() {
    }

    public int getResourceType() {
        return this.resourceType;
    }

    public void setResourceType(int resourceType) {
        this.resourceType = resourceType;
    }

    public String getResourceName() {
        return this.resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    private void setSupportExtShort(String supportExtShort) {
        this.supportExtShort = supportExtShort;
        if (this.supportExtShort != null) {
            this.supportExtValues = this.supportExtShort.split("\\|");
        }
    }

    private String getSupportExtShort() {
        return this.supportExtShort;
    }

    public EZConstants.EZTalkbackCapability isSupportTalk() {
        int value = this.getSupportInt(2);
        switch (value) {
            case 0: {
                return EZConstants.EZTalkbackCapability.EZTalkbackNoSupport;
            }
            case 1: 
            case 4: {
                return EZConstants.EZTalkbackCapability.EZTalkbackFullDuplex;
            }
            case 3: {
                return EZConstants.EZTalkbackCapability.EZTalkbackHalfDuplex;
            }
        }
        return EZConstants.EZTalkbackCapability.EZTalkbackNoSupport;
    }

    public boolean isSupportPTZ() {
        return this.getSupportInt(30) == 1 || this.getSupportInt(31) == 1;
    }

    public boolean isSupportZoom() {
        return this.getSupportInt(33) == 1;
    }

    public boolean isSupportAudioOnOff() {
        return this.getSupportInt(63) == 1;
    }

    public boolean isSupportMirrorCenter() {
        return this.getSupportInt(37) == 1;
    }

    public boolean isSupportSoundWave() {
        return this.getSupportInt(93) == 1;
    }

    public boolean isSupportPlaybackRate() {
        return this.getSupportInt(149) == 1;
    }

    public boolean isSupportDirectInnerRelaySpeed() {
        return this.getSupportInt(68) == 1;
    }

    public boolean isSupportSDRecordDownload() {
        return this.getSupportInt(260) == 1;
    }

    public boolean isSupportSdCover() {
        return this.getSupportInt(483) == 1;
    }

    public boolean isSupportMultiChannel() {
        String supportMultiChannelTypeValue = this.getSupportValue(719);
        boolean isSupportMultiChannel = !"-1".equals(supportMultiChannelTypeValue) && supportMultiChannelTypeValue.split("-").length >= 2;
        boolean isSupportMultiPlay = this.getSupportInt(665) > 0;
        return isSupportMultiChannel || isSupportMultiPlay;
    }

    public boolean isSupportDeviceAutoVideolevel() {
        return this.getSupportInt(729) == 1;
    }

    public int getSupportInt(int index) {
        String value = this.getSupportValue(index);
        if (!TextUtils.isEmpty((CharSequence)value)) {
            try {
                int number = Integer.parseInt(value);
                if (index == 50 && number == -1) {
                    number = 2;
                }
                return Math.max(number, 0);
            }
            catch (NumberFormatException e) {
                LogUtil.printErrStackTrace("EZDeviceInfo", e.fillInStackTrace());
            }
        }
        return 0;
    }

    public String getSupportValue(int index) {
        if (this.supportExtValues == null && !TextUtils.isEmpty((CharSequence)this.getSupportExtShort())) {
            this.setSupportExtShort(this.getSupportExtShort());
        }
        if (this.supportExtValues != null && index > 0 && index <= this.supportExtValues.length) {
            return this.supportExtValues[index - 1];
        }
        return "";
    }

    @Override
    public boolean isCamera() {
        return this.resourceType == 50;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.deviceSerial);
        dest.writeInt(this.cameraNo);
        dest.writeString(this.cameraName);
        dest.writeInt(this.isShared);
        dest.writeString(this.cameraCover);
        dest.writeInt(this.videoLevel);
        dest.writeTypedList((List)this.videoQualityInfos);
        dest.writeInt(this.permission);
        dest.writeInt(this.resourceType);
        dest.writeString(this.resourceName);
        dest.writeString(this.supportExtShort);
    }

    protected EZSubDeviceInfo(Parcel in) {
        this.deviceSerial = in.readString();
        this.cameraNo = in.readInt();
        this.cameraName = in.readString();
        this.isShared = in.readInt();
        this.cameraCover = in.readString();
        this.videoLevel = in.readInt();
        this.videoQualityInfos = in.createTypedArrayList(EZVideoQualityInfo.CREATOR);
        this.permission = in.readInt();
        this.resourceType = in.readInt();
        this.resourceName = in.readString();
        this.supportExtShort = in.readString();
    }
}


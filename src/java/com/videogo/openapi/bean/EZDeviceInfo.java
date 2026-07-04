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
import com.videogo.openapi.bean.EZDetectorInfo;
import com.videogo.openapi.bean.EZSubDeviceInfo;
import com.videogo.util.LogUtil;
import java.util.List;

public class EZDeviceInfo
implements Parcelable {
    @Serializable(name="deviceSerial")
    private String deviceSerial = null;
    @Serializable(name="deviceName")
    private String deviceName = null;
    @Serializable(name="deviceType")
    private String deviceType = null;
    @Serializable(name="category")
    private String category = null;
    @Serializable(name="addTime")
    private long addTime;
    @Serializable(name="status")
    private int status = 1;
    @Serializable(name="isEncrypt")
    private int isEncrypt;
    @Serializable(name="defence")
    private int defence = 0;
    @Serializable(name="supportExtShort")
    private String supportExtShort = null;
    private String[] supportExtValues;
    @Serializable(name="deviceVersion")
    private String deviceVersion = null;
    @Serializable(name="deviceCover")
    private String deviceCover = null;
    @Serializable(name="cameraNum")
    private int cameraNum;
    @Serializable(name="detectorNum")
    private int detectorNum;
    @Serializable(name="supportChannelNums")
    private int supportChannelNums;
    @Serializable(name="offlineNotify")
    private int offlineNotify;
    private List<EZCameraInfo> cameraInfoList = null;
    private List<EZDetectorInfo> detectorInfoList = null;
    private List<EZSubDeviceInfo> subDeviceInfoList = null;
    public static final Parcelable.Creator<EZDeviceInfo> CREATOR = new Parcelable.Creator<EZDeviceInfo>(){

        public EZDeviceInfo createFromParcel(Parcel source) {
            return new EZDeviceInfo(source);
        }

        public EZDeviceInfo[] newArray(int size) {
            return new EZDeviceInfo[size];
        }
    };

    public EZDeviceInfo() {
    }

    public String getDeviceSerial() {
        return this.deviceSerial;
    }

    public void setDetectorNum(int detectorNum) {
        this.detectorNum = detectorNum;
    }

    public void setDeviceSerial(String deviceSerial) {
        this.deviceSerial = deviceSerial;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setIsEncrypt(int isEncrypt) {
        this.isEncrypt = isEncrypt;
    }

    public void setDefence(int defence) {
        this.defence = defence;
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

    private void setSupportExtValues(String[] supportExtValues) {
        this.supportExtValues = supportExtValues;
    }

    private String[] getSupportExtValues() {
        if (this.supportExtShort != null) {
            this.supportExtValues = this.supportExtShort.split("\\|");
        }
        return this.supportExtValues;
    }

    public void setDeviceVersion(String deviceVersion) {
        this.deviceVersion = deviceVersion;
    }

    public void setDeviceCover(String deviceCover) {
        this.deviceCover = deviceCover;
    }

    public void setCameraNum(int cameraNum) {
        this.cameraNum = cameraNum;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public String getDeviceType() {
        return this.deviceType;
    }

    public int getIsEncrypt() {
        return this.isEncrypt;
    }

    public int getDefence() {
        return this.defence;
    }

    public String getDeviceVersion() {
        return this.deviceVersion;
    }

    public String getDeviceCover() {
        return this.deviceCover;
    }

    public int getCameraNum() {
        return this.cameraNum;
    }

    public int getDetectorNum() {
        return this.detectorNum;
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

    public boolean isSupportDefence() {
        return this.getSupportInt(1) == 1;
    }

    public boolean isSupportDefencePlan() {
        return this.getSupportInt(3) == 1;
    }

    public boolean isSupportPTZ() {
        return this.getSupportInt(30) == 1 || this.getSupportInt(31) == 1;
    }

    public boolean isSupportZoom() {
        return this.getSupportInt(33) == 1;
    }

    public boolean isSupportUpgrade() {
        return this.getSupportInt(10) == 1;
    }

    public boolean isSupportMirrorCenter() {
        return this.getSupportInt(37) == 1;
    }

    public boolean isSupportAudioOnOff() {
        return this.getSupportInt(63) == 1;
    }

    public boolean isSupportSoundWave() {
        return this.getSupportInt(93) == 1;
    }

    public boolean isSupportPTZFocus() {
        return this.getSupportInt(99) == 1;
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

    public boolean isSupportVideoMeeting() {
        int value = this.getSupportInt(818);
        return value == 1 || value == 2;
    }

    public void setCameraInfoList(List<EZCameraInfo> cameraInfoList) {
        this.cameraInfoList = cameraInfoList;
    }

    public List<EZCameraInfo> getCameraInfoList() {
        return this.cameraInfoList;
    }

    public void setDetectorInfoList(List<EZDetectorInfo> detectorInfoList) {
        this.detectorInfoList = detectorInfoList;
    }

    public List<EZDetectorInfo> getDetectorInfoList() {
        return this.detectorInfoList;
    }

    public List<EZSubDeviceInfo> getSubDeviceInfoList() {
        return this.subDeviceInfoList;
    }

    public void setSubDeviceInfoList(List<EZSubDeviceInfo> subDeviceInfoList) {
        this.subDeviceInfoList = subDeviceInfoList;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getAddTime() {
        return this.addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
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

    public int getSupportChannelNums() {
        return this.supportChannelNums;
    }

    public void setSupportChannelNums(int supportChannelNums) {
        this.supportChannelNums = supportChannelNums;
    }

    public int getOfflineNotify() {
        return this.offlineNotify;
    }

    public void setOfflineNotify(int offlineNotify) {
        this.offlineNotify = offlineNotify;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.deviceSerial);
        dest.writeString(this.deviceName);
        dest.writeString(this.deviceType);
        dest.writeString(this.category);
        dest.writeLong(this.addTime);
        dest.writeInt(this.status);
        dest.writeInt(this.isEncrypt);
        dest.writeInt(this.defence);
        dest.writeString(this.supportExtShort);
        dest.writeStringArray(this.supportExtValues);
        dest.writeString(this.deviceVersion);
        dest.writeString(this.deviceCover);
        dest.writeInt(this.cameraNum);
        dest.writeInt(this.detectorNum);
        dest.writeInt(this.supportChannelNums);
        dest.writeInt(this.offlineNotify);
        dest.writeTypedList(this.cameraInfoList);
        dest.writeTypedList(this.detectorInfoList);
        dest.writeTypedList(this.subDeviceInfoList);
    }

    protected EZDeviceInfo(Parcel in) {
        this.deviceSerial = in.readString();
        this.deviceName = in.readString();
        this.deviceType = in.readString();
        this.category = in.readString();
        this.addTime = in.readLong();
        this.status = in.readInt();
        this.isEncrypt = in.readInt();
        this.defence = in.readInt();
        this.supportExtShort = in.readString();
        this.supportExtValues = in.createStringArray();
        this.deviceVersion = in.readString();
        this.deviceCover = in.readString();
        this.cameraNum = in.readInt();
        this.detectorNum = in.readInt();
        this.supportChannelNums = in.readInt();
        this.offlineNotify = in.readInt();
        this.cameraInfoList = in.createTypedArrayList(EZCameraInfo.CREATOR);
        this.detectorInfoList = in.createTypedArrayList(EZDetectorInfo.CREATOR);
        this.subDeviceInfoList = in.createTypedArrayList(EZSubDeviceInfo.CREATOR);
    }
}


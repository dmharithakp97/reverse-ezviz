/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package com.videogo.camera;

import android.os.Parcel;
import android.os.Parcelable;
import com.videogo.openapi.annotation.Serializable;
import com.videogo.openapi.bean.EZVideoQualityInfo;
import com.videogo.util.LocalInfo;
import java.util.ArrayList;
import java.util.Arrays;

public class CameraInfo
implements Parcelable {
    @Serializable(name="cameraId")
    protected String mCameraID = "";
    protected String mCameraName = "";
    @Serializable(name="cameraNo")
    protected int mChannelNo = -1;
    @Serializable(name="strCameraNo")
    protected String szChnlIndex;
    @Serializable(name="deviceSerial")
    protected String mDeviceSN = "";
    protected String mBigThumbnailUrl = "";
    protected String mMidThumbnailUrl = "";
    protected String mSmallThumbnailUrl = "";
    protected boolean mIsAdded = false;
    protected int mAlarmCount = 0;
    @Serializable(name="type")
    protected int mType = -1;
    public static final int VIDEO_LEVEL_FLUNET = 0;
    public static final int VIDEO_LEVEL_BALANCED = 1;
    public static final int VIDEO_LEVEL_HD = 2;
    @Serializable(name="videoLevel")
    protected int videoLevel = -1;
    @Serializable(name="capability")
    protected String capability = "";
    protected String defenceStartTime = "00:00";
    protected String defenceStopTime = "n00:00";
    protected String defencePeriod = "0,1,2,3,4";
    protected int defencePlanEnable = 0;
    @Serializable(name="permission")
    protected int permission = 3;
    protected double latitude;
    protected double longitude;
    protected int defenceLbsEnable = 0;
    protected int defenceRadius;
    protected int defence;
    protected String fullModel;
    @Serializable(name="status")
    protected int status;
    private String thirdDevId;
    public static final int CAMERA_SHARE_NO = 0;
    public static final int CAMERA_SHARE_START = 1;
    public static final int CAMERA_SHARE_RECEIVED = 2;
    public static final int CAMERA_SHARE_OVER = 3;
    public static final int CAMERA_SHARE_NOT_IN_TIME = 4;
    public static final int CAMERA_SHARE_BY_BARCODE = 5;
    @Serializable(name="isShared")
    private int isShared;
    private String adUrl = "";
    @Serializable(name="forceStreamType")
    private int forceStreamType;
    @Serializable(name="streamBiz")
    private int streamBiz = 5;
    @Serializable(name="streamBizUrl")
    private String streamBizUrl = null;
    @Serializable(name="rtmpUrl")
    private String rtmpUrl;
    @Serializable(name="rtmpTimespan")
    private String rtmpTimespan;
    public ArrayList<EZVideoQualityInfo> videoQualityInfos;
    public static final Parcelable.Creator<CameraInfo> CREATOR = new Parcelable.Creator<CameraInfo>(){

        public CameraInfo createFromParcel(Parcel source) {
            return new CameraInfo(source);
        }

        public CameraInfo[] newArray(int size) {
            return new CameraInfo[size];
        }
    };

    public CameraInfo() {
    }

    public int getStreamBiz() {
        return this.streamBiz;
    }

    public void setStreamBiz(int streamBiz) {
        this.streamBiz = streamBiz;
    }

    public String getAdUrl() {
        return this.adUrl;
    }

    public void setAdUrl(String adUrl) {
        this.adUrl = adUrl;
    }

    public String getCameraID() {
        return this.mCameraID;
    }

    public void setCameraID(String cameraID) {
        this.mCameraID = cameraID;
    }

    public void setCameraName(String cameraName) {
        this.mCameraName = cameraName;
    }

    public String getCameraName() {
        return this.mCameraName;
    }

    public void setChannelNo(int channelNo) {
        this.mChannelNo = channelNo;
    }

    public String getSzChnlIndex() {
        return this.szChnlIndex;
    }

    public void setSzChnlIndex(String szChnlIndex) {
        this.szChnlIndex = szChnlIndex;
    }

    public int getChannelNo() {
        return this.mChannelNo;
    }

    public void setDeviceSN(String deviceSN) {
        this.mDeviceSN = deviceSN;
    }

    public String getDeviceID() {
        return this.mDeviceSN;
    }

    public String getBigThumbnailUrl() {
        return LocalInfo.getInstance().getServAddr() + this.mBigThumbnailUrl;
    }

    public String getBigThumbnailUrl2() {
        return this.mBigThumbnailUrl;
    }

    public void setBigThumbnailUrl(String bigThumbnailUrl) {
        this.mBigThumbnailUrl = bigThumbnailUrl;
    }

    public String getMidThumbnailUrl() {
        return LocalInfo.getInstance().getServAddr() + this.mMidThumbnailUrl;
    }

    public void setMidThumbnailUrl(String midThumbnailUrl) {
        this.mMidThumbnailUrl = midThumbnailUrl;
    }

    public String getSmallThumbnailUrl() {
        return LocalInfo.getInstance().getServAddr() + this.mSmallThumbnailUrl;
    }

    public String getSmallThumbnailUrl2() {
        return this.mSmallThumbnailUrl;
    }

    public void setSmallThumbnailUrl(String smallThumbnailUrl) {
        this.mSmallThumbnailUrl = smallThumbnailUrl;
    }

    public boolean isAdded() {
        return this.mIsAdded;
    }

    public void setAdded(boolean isAdded) {
        this.mIsAdded = isAdded;
    }

    public int getAlarmCount() {
        return this.mAlarmCount;
    }

    public void setAlarmCount(int alarmCount) {
        this.mAlarmCount = alarmCount;
    }

    public void setType(int mType) {
        this.mType = mType;
    }

    public int getType() {
        return this.mType;
    }

    public void setVideoLevel(int videoLevel) {
        this.videoLevel = videoLevel;
    }

    public int getVideoLevel() {
        return this.videoLevel;
    }

    public String getCapability() {
        return this.capability;
    }

    public void setCapability(String capability) {
        this.capability = capability;
    }

    public void setDefenceStartTime(String defenceStartTime) {
        this.defenceStartTime = defenceStartTime;
    }

    public String getDefenceStartTime() {
        return this.defenceStartTime;
    }

    public void setDefenceStopTime(String defenceStopTime) {
        this.defenceStopTime = defenceStopTime;
    }

    public String getDefenceStopTime() {
        return this.defenceStopTime;
    }

    public void setDefencePeriod(String defencePeriod) {
        if (defencePeriod != null) {
            Object[] defenceDates = defencePeriod.split(",");
            if (defenceDates.length > 0) {
                Arrays.sort(defenceDates);
                StringBuffer buf = new StringBuffer();
                for (int i = 0; i < defenceDates.length; ++i) {
                    if (i > 0) {
                        buf.append(",");
                    }
                    buf.append((String)defenceDates[i]);
                }
                this.defencePeriod = buf.toString();
            } else {
                this.defencePeriod = defencePeriod;
            }
        }
    }

    public String getDefencePeriod() {
        return this.defencePeriod;
    }

    public void setDefencePlanEnable(int defencePlanEnable) {
        this.defencePlanEnable = defencePlanEnable;
    }

    public int getDefencePlanEnable() {
        return this.defencePlanEnable;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public int getPermission() {
        return this.permission;
    }

    public void setDefenceLbsEnable(int defenceLbsEnable) {
        this.defenceLbsEnable = defenceLbsEnable;
    }

    public int getDefenceLbsEnable() {
        return this.defenceLbsEnable;
    }

    public void setDefenceRadius(int defenceRadius) {
        this.defenceRadius = defenceRadius;
    }

    public int getDefenceRadius() {
        return this.defenceRadius;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setDefence(int defence) {
        this.defence = defence;
    }

    public int getDefence() {
        return this.defence;
    }

    public String getFullModel() {
        return this.fullModel;
    }

    public void setFullModel(String fullModel) {
        this.fullModel = fullModel;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getThirdDevId() {
        return this.thirdDevId;
    }

    public void setThirdDevId(String thirdDevId) {
        this.thirdDevId = thirdDevId;
    }

    public int getIsShared() {
        return this.isShared;
    }

    public void setIsShared(int isShared) {
        this.isShared = isShared;
    }

    public int getForceStreamType() {
        return this.forceStreamType;
    }

    public void setForceStreamType(int forceStreamType) {
        this.forceStreamType = forceStreamType;
    }

    public String getRtmpUrl() {
        return this.rtmpUrl;
    }

    public void setRtmpUrl(String rtmpUrl) {
        this.rtmpUrl = rtmpUrl;
    }

    public String getRtmpTimespan() {
        return this.rtmpTimespan;
    }

    public void setRtmpTimespan(String rtmpTimespan) {
        this.rtmpTimespan = rtmpTimespan;
    }

    public String getStreamBizUrl() {
        return this.streamBizUrl;
    }

    public void setStreamBizUrl(String streamBizUrl) {
        this.streamBizUrl = streamBizUrl;
    }

    public void copy(CameraInfo cameraInfo) {
        this.mAlarmCount = cameraInfo.mAlarmCount;
        this.mCameraID = cameraInfo.mCameraID;
        this.mCameraName = cameraInfo.mCameraName;
        this.mChannelNo = cameraInfo.mChannelNo;
        this.mDeviceSN = cameraInfo.mDeviceSN;
        this.mSmallThumbnailUrl = cameraInfo.mSmallThumbnailUrl;
        this.mMidThumbnailUrl = cameraInfo.mMidThumbnailUrl;
        this.mBigThumbnailUrl = cameraInfo.mBigThumbnailUrl;
        this.mIsAdded = cameraInfo.mIsAdded;
        this.mType = cameraInfo.mType;
        this.videoLevel = cameraInfo.videoLevel;
        this.capability = cameraInfo.capability;
        this.defenceStartTime = cameraInfo.defenceStartTime;
        this.defenceStopTime = cameraInfo.defenceStopTime;
        this.defencePeriod = cameraInfo.defencePeriod;
        this.defencePlanEnable = cameraInfo.defencePlanEnable;
        this.permission = cameraInfo.permission;
        this.latitude = cameraInfo.latitude;
        this.longitude = cameraInfo.longitude;
        this.defenceLbsEnable = cameraInfo.defenceLbsEnable;
        this.defenceRadius = cameraInfo.defenceRadius;
        this.defence = cameraInfo.defence;
        this.fullModel = cameraInfo.fullModel;
        this.status = cameraInfo.status;
        this.thirdDevId = cameraInfo.thirdDevId;
        this.isShared = cameraInfo.isShared;
        this.forceStreamType = cameraInfo.forceStreamType;
        this.streamBiz = cameraInfo.streamBiz;
        this.videoQualityInfos = cameraInfo.videoQualityInfos;
        this.rtmpUrl = cameraInfo.rtmpUrl;
        this.rtmpTimespan = cameraInfo.rtmpTimespan;
        this.szChnlIndex = cameraInfo.szChnlIndex == null ? "" : cameraInfo.szChnlIndex;
        this.streamBizUrl = cameraInfo.streamBizUrl;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mCameraID);
        dest.writeString(this.mCameraName);
        dest.writeInt(this.mChannelNo);
        dest.writeString(this.mDeviceSN);
        dest.writeString(this.mBigThumbnailUrl);
        dest.writeString(this.mMidThumbnailUrl);
        dest.writeString(this.mSmallThumbnailUrl);
        dest.writeByte(this.mIsAdded ? (byte)1 : 0);
        dest.writeInt(this.mAlarmCount);
        dest.writeInt(this.mType);
        dest.writeInt(this.videoLevel);
        dest.writeString(this.capability);
        dest.writeString(this.defenceStartTime);
        dest.writeString(this.defenceStopTime);
        dest.writeString(this.defencePeriod);
        dest.writeInt(this.defencePlanEnable);
        dest.writeInt(this.permission);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeInt(this.defenceLbsEnable);
        dest.writeInt(this.defenceRadius);
        dest.writeInt(this.defence);
        dest.writeString(this.fullModel);
        dest.writeInt(this.status);
        dest.writeString(this.thirdDevId);
        dest.writeInt(this.isShared);
        dest.writeString(this.adUrl);
        dest.writeInt(this.forceStreamType);
        dest.writeInt(this.streamBiz);
        dest.writeString(this.rtmpUrl);
        dest.writeString(this.rtmpTimespan);
        dest.writeTypedList(this.videoQualityInfos);
        dest.writeString(this.szChnlIndex);
        dest.writeString(this.streamBizUrl);
    }

    protected CameraInfo(Parcel in) {
        this.mCameraID = in.readString();
        this.mCameraName = in.readString();
        this.mChannelNo = in.readInt();
        this.mDeviceSN = in.readString();
        this.mBigThumbnailUrl = in.readString();
        this.mMidThumbnailUrl = in.readString();
        this.mSmallThumbnailUrl = in.readString();
        this.mIsAdded = in.readByte() != 0;
        this.mAlarmCount = in.readInt();
        this.mType = in.readInt();
        this.videoLevel = in.readInt();
        this.capability = in.readString();
        this.defenceStartTime = in.readString();
        this.defenceStopTime = in.readString();
        this.defencePeriod = in.readString();
        this.defencePlanEnable = in.readInt();
        this.permission = in.readInt();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.defenceLbsEnable = in.readInt();
        this.defenceRadius = in.readInt();
        this.defence = in.readInt();
        this.fullModel = in.readString();
        this.status = in.readInt();
        this.thirdDevId = in.readString();
        this.isShared = in.readInt();
        this.adUrl = in.readString();
        this.forceStreamType = in.readInt();
        this.streamBiz = in.readInt();
        this.rtmpUrl = in.readString();
        this.rtmpTimespan = in.readString();
        this.videoQualityInfos = in.createTypedArrayList(EZVideoQualityInfo.CREATOR);
        this.szChnlIndex = in.readString();
        this.streamBizUrl = in.readString();
    }
}


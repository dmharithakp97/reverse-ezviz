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
import java.util.Calendar;

public class EZCloudRecordFile
implements Parcelable {
    @Serializable(name="file_id")
    private String fileId;
    @Serializable(name="start_time")
    private Calendar startTime;
    @Serializable(name="stop_time")
    private Calendar stopTime;
    @Serializable(name="coverPic")
    private String coverPic;
    @Serializable(name="downloadPath")
    private String downloadPath;
    @Serializable(name="key_checksum")
    private String encryption;
    @Serializable(name="file_size")
    private long fileSize;
    @Serializable(name="dev_serial")
    private String deviceSerial;
    @Serializable(name="channel_no")
    private int cameraNo;
    @Serializable(name="cloud_type")
    private int cloudType;
    @Serializable(name="iStorageVersion")
    private int iStorageVersion = -100;
    @Serializable(name="videoType")
    private int videoType = -100;
    @Serializable(name="spaceId")
    private long spaceId;
    public static final Parcelable.Creator<EZCloudRecordFile> CREATOR = new Parcelable.Creator<EZCloudRecordFile>(){

        public EZCloudRecordFile createFromParcel(Parcel source) {
            return new EZCloudRecordFile(source);
        }

        public EZCloudRecordFile[] newArray(int size) {
            return new EZCloudRecordFile[size];
        }
    };

    public String getDeviceSerial() {
        return this.deviceSerial;
    }

    public void setDeviceSerial(String deviceSerial) {
        this.deviceSerial = deviceSerial;
    }

    public int getCameraNo() {
        return this.cameraNo;
    }

    public void setCameraNo(int cameraNo) {
        this.cameraNo = cameraNo;
    }

    public String getCoverPic() {
        return this.coverPic;
    }

    public void setCoverPic(String coverPic) {
        this.coverPic = coverPic;
    }

    public String getDownloadPath() {
        return this.downloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    public Calendar getStartTime() {
        return this.startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public Calendar getStopTime() {
        return this.stopTime;
    }

    public void setStopTime(Calendar stopTime) {
        this.stopTime = stopTime;
    }

    public String getFileId() {
        return this.fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getEncryption() {
        return this.encryption;
    }

    public void setEncryption(String keyChecksum) {
        this.encryption = keyChecksum;
    }

    public long getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public int getCloudType() {
        return this.cloudType;
    }

    public void setCloudType(int cloudType) {
        this.cloudType = cloudType;
    }

    public int getiStorageVersion() {
        return this.iStorageVersion;
    }

    public void setiStorageVersion(int iStorageVersion) {
        this.iStorageVersion = iStorageVersion;
    }

    public int getVideoType() {
        return this.videoType;
    }

    public void setVideoType(int videoType) {
        this.videoType = videoType;
    }

    public long getSpaceId() {
        return this.spaceId;
    }

    public void setSpaceId(long spaceId) {
        this.spaceId = spaceId;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fileId);
        dest.writeSerializable((java.io.Serializable)this.startTime);
        dest.writeSerializable((java.io.Serializable)this.stopTime);
        dest.writeString(this.coverPic);
        dest.writeString(this.downloadPath);
        dest.writeString(this.encryption);
        dest.writeInt(this.iStorageVersion);
        dest.writeInt(this.videoType);
        dest.writeLong(this.spaceId);
    }

    public EZCloudRecordFile() {
    }

    protected EZCloudRecordFile(Parcel in) {
        this.fileId = in.readString();
        this.startTime = (Calendar)in.readSerializable();
        this.stopTime = (Calendar)in.readSerializable();
        this.coverPic = in.readString();
        this.downloadPath = in.readString();
        this.encryption = in.readString();
        this.iStorageVersion = in.readInt();
        this.videoType = in.readInt();
        this.spaceId = in.readLong();
    }
}


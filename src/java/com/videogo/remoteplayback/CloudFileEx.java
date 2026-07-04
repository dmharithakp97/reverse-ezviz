/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.remoteplayback;

import com.videogo.openapi.annotation.Serializable;

public class CloudFileEx {
    @Serializable(name="id")
    private long id;
    @Serializable(name="file_id")
    private String fileId;
    @Serializable(name="dev_serial")
    private String serial;
    @Serializable(name="channel_no")
    private int channelNo;
    @Serializable(name="file_type")
    private int fileType;
    @Serializable(name="file_name")
    private int fileName;
    @Serializable(name="start_time")
    private String startTime;
    @Serializable(name="stop_time")
    private String stopTime;
    @Serializable(name="owner_id")
    private String ownerId;
    @Serializable(name="cloud_type")
    private int cloudType;
    @Serializable(name="file_index")
    private String fileIndex;
    @Serializable(name="crypt")
    private int crypt;
    @Serializable(name="key_checksum")
    private String keyChecksum;
    @Serializable(name="file_size")
    private String fileSize;
    @Serializable(name="locked")
    private int locked;
    @Serializable(name="create_time")
    private String createTime;
    @Serializable(name="videoLong")
    private long videoLong;
    @Serializable(name="coverPic")
    private String coverPic;
    @Serializable(name="downloadPath")
    private String downloadPath;
    @Serializable(name="type")
    private int type;
    @Serializable(name="istorageVersion")
    private int iStorageVersion = -100;
    @Serializable(name="videoType")
    private int videoType = -100;

    public int getFileName() {
        return this.fileName;
    }

    public void setFileName(int fileName) {
        this.fileName = fileName;
    }

    public String getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public int getLocked() {
        return this.locked;
    }

    public void setLocked(int locked) {
        this.locked = locked;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public long getVideoLong() {
        return this.videoLong;
    }

    public void setVideoLong(long videoLong) {
        this.videoLong = videoLong;
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

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFileId() {
        return this.fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getSerial() {
        return this.serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public int getChannelNo() {
        return this.channelNo;
    }

    public void setChannelNo(int channelNo) {
        this.channelNo = channelNo;
    }

    public int getFileType() {
        return this.fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public int getCrypt() {
        return this.crypt;
    }

    public void setCrypt(int crypt) {
        this.crypt = crypt;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStopTime() {
        return this.stopTime;
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }

    public String getOwnerId() {
        return this.ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getFileIndex() {
        return this.fileIndex;
    }

    public void setFileIndex(String fileIndex) {
        this.fileIndex = fileIndex;
    }

    public int getCloudType() {
        return this.cloudType;
    }

    public void setCloudType(int cloudType) {
        this.cloudType = cloudType;
    }

    public String getKeyChecksum() {
        return this.keyChecksum;
    }

    public void setKeyChecksum(String keyChecksum) {
        this.keyChecksum = keyChecksum;
    }

    public CloudFileEx copy() {
        CloudFileEx cloudFile = new CloudFileEx();
        cloudFile.setFileId(this.getFileId());
        cloudFile.setSerial(this.getSerial());
        cloudFile.setChannelNo(this.getChannelNo());
        cloudFile.setFileType(this.getFileType());
        cloudFile.setCrypt(this.getCrypt());
        cloudFile.setStartTime(this.getStartTime());
        cloudFile.setStopTime(this.getStopTime());
        cloudFile.setFileIndex(this.getFileIndex());
        cloudFile.setOwnerId(this.getOwnerId());
        cloudFile.setCloudType(this.getCloudType());
        cloudFile.setKeyChecksum(this.getKeyChecksum());
        return cloudFile;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" fileId:").append(this.fileId).append(" deviceSerial:").append(this.serial).append(" cameraNo:").append(this.channelNo).append(" fileType:").append(this.fileType).append(" startTime:").append(this.startTime).append(" stopTime:").append(this.stopTime).append(" cloudType:").append(this.cloudType).append(" fileIndex:").append(this.fileIndex).append(" ownerId:").append(this.ownerId).append(" crypt:").append(this.crypt).append(" iStorageVersion:").append(this.iStorageVersion).append(" videoType:").append(this.videoType).append(" keyChecksum:").append(this.keyChecksum);
        return sb.toString();
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
}


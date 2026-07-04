/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.remoteplayback;

import com.videogo.openapi.annotation.Serializable;
import java.util.Map;

public class SDKCloudFileEx {
    @Serializable(name="id")
    private long id;
    @Serializable(name="fileId")
    private String fileId;
    @Serializable(name="deviceSerial")
    private String serial;
    @Serializable(name="channelNo")
    private int channelNo;
    @Serializable(name="fileType")
    private int fileType;
    @Serializable(name="startTime")
    private String startTime;
    @Serializable(name="stopTime")
    private String stopTime;
    @Serializable(name="ownerId")
    private String ownerId;
    @Serializable(name="cloudType")
    private int cloudType;
    @Serializable(name="fileIndex")
    private String fileIndex;
    @Serializable(name="crypt")
    private int crypt;
    @Serializable(name="keyChecksum")
    private String keyChecksum;
    @Serializable(name="fileSize")
    private String fileSize;
    @Serializable(name="locked")
    private int locked;
    @Serializable(name="createTime")
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
    @Serializable(name="spaceId")
    private long spaceId;
    private String uploadURL;
    private String storageId;
    private Map<String, Object> formMap;

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

    public String getUploadURL() {
        return this.uploadURL;
    }

    public void setUploadURL(String uploadURL) {
        this.uploadURL = uploadURL;
    }

    public String getStorageId() {
        return this.storageId;
    }

    public void setStorageId(String storageId) {
        this.storageId = storageId;
    }

    public Map<String, Object> getFormMap() {
        return this.formMap;
    }

    public void setFormMap(Map<String, Object> formMap) {
        this.formMap = formMap;
    }

    public SDKCloudFileEx copy() {
        SDKCloudFileEx cloudFile = new SDKCloudFileEx();
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
}


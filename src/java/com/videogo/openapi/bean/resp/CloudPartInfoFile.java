/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap
 */
package com.videogo.openapi.bean.resp;

import android.graphics.Bitmap;
import com.videogo.openapi.annotation.Serializable;
import com.videogo.openapi.bean.resp.CloudFile;
import com.videogo.remoteplayback.RemoteFileInfo;
import com.videogo.util.DateTimeUtil;
import com.videogo.util.Utils;
import java.util.Calendar;

public class CloudPartInfoFile
implements Comparable<CloudPartInfoFile> {
    private String fileId;
    private String startTime;
    private String endTime;
    private int position;
    private String fileName;
    private long fileSize;
    private String downloadPath;
    private String keyCheckSum;
    private boolean isCloud;
    private int fileType;
    private String picUrl;
    private long startMillis;
    private long endMillis;
    private long spaceId;
    private String deviceSerial;
    private int cameraNo;
    private String begin;
    private String end;
    private Bitmap bitmap;
    @Serializable(name="cloudType")
    private int cloudType;
    @Serializable(name="iStorageVersion")
    private int iStorageVersion = -100;
    @Serializable(name="videoType")
    private int videoType = -100;

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

    public String getPicUrl() {
        return this.picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public long getStartMillis() {
        return this.startMillis;
    }

    public void setStartMillis(long startMillis) {
        this.startMillis = startMillis;
    }

    public long getEndMillis() {
        return this.endMillis;
    }

    public void setEndMillis(long endMillis) {
        this.endMillis = endMillis;
    }

    public int getFileType() {
        return this.fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public String getKeyCheckSum() {
        return this.keyCheckSum;
    }

    public void setKeyCheckSum(String keyCheckSum) {
        this.keyCheckSum = keyCheckSum;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getDownloadPath() {
        return this.downloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    public boolean isCloud() {
        return this.isCloud;
    }

    public void setCloud(boolean isCloud) {
        this.isCloud = isCloud;
    }

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getFileId() {
        return this.fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
        this.startMillis = this.getCalendar(startTime).getTimeInMillis();
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
        this.endMillis = this.getCalendar(endTime).getTimeInMillis();
    }

    public String getBegin() {
        return this.begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return this.end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public int compareTo(CloudPartInfoFile another) {
        if (this == another) {
            return 0;
        }
        if (another != null) {
            long anotherLong;
            long otherLong = Long.parseLong(this.startTime);
            if (otherLong <= (anotherLong = Long.parseLong(another.getStartTime()))) {
                return -1;
            }
            return 1;
        }
        return -1;
    }

    private Calendar getCalendar(String time) {
        return DateTimeUtil.parseStringToCalendar(time, "yyyyMMddHHmmss");
    }

    public RemoteFileInfo getRemoteFileInfo() {
        RemoteFileInfo info = new RemoteFileInfo();
        info.setFileName(this.fileName);
        info.setFileSize(this.fileSize);
        info.setFileType(this.fileType);
        info.setStartTime(Utils.convert14Calender(this.startTime));
        info.setStopTime(Utils.convert14Calender(this.endTime));
        return info;
    }

    public CloudFile getCloudFile() {
        CloudFile cloudFile = new CloudFile();
        cloudFile.setFileId(this.fileId);
        cloudFile.setStartTime(this.startTime);
        cloudFile.setEndTime(this.endTime);
        return cloudFile;
    }

    public CloudPartInfoFile copy() {
        CloudPartInfoFile cloudFile = new CloudPartInfoFile();
        cloudFile.setFileId(this.fileId);
        cloudFile.setStartTime(this.startTime);
        cloudFile.setEndTime(this.endTime);
        cloudFile.setDownloadPath(this.downloadPath);
        cloudFile.setFileName(this.fileName);
        cloudFile.setKeyCheckSum(this.keyCheckSum);
        cloudFile.setPicUrl(this.picUrl);
        cloudFile.setFileSize(this.fileSize);
        cloudFile.setCloud(true);
        return cloudFile;
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
}


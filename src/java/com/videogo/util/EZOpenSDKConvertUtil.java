/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.util;

import com.videogo.openapi.bean.EZCloudRecordFile;
import com.videogo.remoteplayback.CloudFileEx;
import com.videogo.remoteplayback.SDKCloudFileEx;
import com.videogo.util.Utils;

public class EZOpenSDKConvertUtil {
    public static void convertCloudFileEx2EZCloudFile(EZCloudRecordFile dst, CloudFileEx src) {
        dst.setCloudType(src.getCloudType());
        dst.setCoverPic(src.getCoverPic());
        dst.setDownloadPath(src.getDownloadPath());
        dst.setFileId(src.getFileId());
        dst.setFileSize(Long.parseLong(src.getFileSize()));
        dst.setEncryption(src.getKeyChecksum());
        dst.setDeviceSerial(src.getSerial());
        dst.setCameraNo(src.getChannelNo());
        dst.setStartTime(Utils.convert14Calender(src.getStartTime()));
        dst.setStopTime(Utils.convert14Calender(src.getStopTime()));
        dst.setiStorageVersion(src.getiStorageVersion());
        dst.setVideoType(src.getVideoType());
    }

    public static void convertSDKCloudFileEx2EZCloudFile(EZCloudRecordFile dst, SDKCloudFileEx src) {
        dst.setCoverPic(src.getCoverPic());
        dst.setDownloadPath(src.getDownloadPath());
        dst.setFileId(src.getFileId());
        dst.setFileSize(Long.parseLong(src.getFileSize()));
        dst.setEncryption(src.getKeyChecksum());
        dst.setStartTime(Utils.convert14Calender(src.getStartTime()));
        dst.setStopTime(Utils.convert14Calender(src.getStopTime()));
        dst.setCloudType(src.getCloudType());
        dst.setiStorageVersion(src.getiStorageVersion());
        dst.setVideoType(src.getVideoType());
        dst.setSpaceId(src.getSpaceId());
    }
}


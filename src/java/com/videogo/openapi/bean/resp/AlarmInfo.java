/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.openapi.bean.resp;

import com.videogo.util.LogUtil;
import com.videogo.util.Utils;

public class AlarmInfo {
    private String alarmId;
    private String alarmName;
    private int alarmType;
    private String alarmPicUrl;
    private String alarmStart;
    private int isCloud = 0;
    private int isEncryption = 0;
    private String checkSum;
    private String deviceSerial = "";
    private int mChannelNo;
    private boolean bIsRead;

    public String getAlarmId() {
        return this.alarmId;
    }

    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }

    public String getAlarmName() {
        return this.alarmName;
    }

    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
    }

    public int getAlarmType() {
        return this.alarmType;
    }

    public void setAlarmType(int alarmType) {
        this.alarmType = alarmType;
    }

    public String getAlarmPicUrl() {
        return this.alarmPicUrl;
    }

    public void setAlarmPicUrl(String alarmPicUrl) {
        this.alarmPicUrl = alarmPicUrl;
        try {
            this.isEncryption = Integer.parseInt(Utils.getUrlValue(alarmPicUrl, "isEncrypted=", "&"));
        }
        catch (NumberFormatException e) {
            LogUtil.printErrStackTrace("AlarmInfo", e.fillInStackTrace());
        }
        try {
            this.isCloud = Integer.parseInt(Utils.getUrlValue(alarmPicUrl, "isCloudStored=", "&"));
        }
        catch (NumberFormatException e) {
            LogUtil.printErrStackTrace("AlarmInfo", e.fillInStackTrace());
        }
    }

    public String getAlarmStart() {
        return this.alarmStart;
    }

    public void setAlarmStart(String alarmStart) {
        this.alarmStart = alarmStart;
    }

    public void setAlarmIsCloud(boolean flag) {
        this.isCloud = flag ? 1 : 0;
    }

    public boolean getAlarmCloud() {
        return this.isCloud == 1;
    }

    public void setAlarmIsEncyption(boolean flag) {
        this.isEncryption = flag ? 1 : 0;
    }

    public boolean getAlarmEncryption() {
        return this.isEncryption == 1;
    }

    public String getCheckSum() {
        return this.checkSum;
    }

    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum;
    }

    public void setDeviceSerial(String deviceSerial) {
        this.deviceSerial = deviceSerial;
    }

    public String getDeviceSerial() {
        return this.deviceSerial;
    }

    public int getChannelNo() {
        return this.mChannelNo;
    }

    public void setChannelNo(int ChannelNo) {
        this.mChannelNo = ChannelNo;
    }

    public boolean isRead() {
        return this.bIsRead;
    }

    public void setIsRead(boolean isRead) {
        this.bIsRead = isRead;
    }
}


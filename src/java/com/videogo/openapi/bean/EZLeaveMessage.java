/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.openapi.bean;

import com.videogo.openapi.annotation.Serializable;
import java.util.Calendar;

public class EZLeaveMessage {
    @Serializable(name="messageId")
    private String msgId;
    @Serializable(name="deviceSerial")
    private String deviceSerial;
    @Serializable(name="duration")
    private int duration;
    @Serializable(name="contentType")
    private int contentType;
    @Serializable(name="msgDirection")
    private int msgDirection;
    @Serializable(name="senderType")
    private int senderType;
    @Serializable(name="senderName")
    private int senderName;
    @Serializable(name="msgPicUrl")
    private String msgPicUrl;
    @Serializable(name="status")
    private int msgStatus;
    @Serializable(name="deviceName")
    private String deviceName;
    @Serializable(name="createTime")
    private String internalCreateTime;
    @Serializable(name="updateTime")
    private String internalUpdateTime;
    @Serializable(name="cloudServerUrl")
    private String cloudServerUrl;
    private Calendar createTime;
    private Calendar updateTime;

    public String getMsgId() {
        return this.msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getDeviceSerial() {
        return this.deviceSerial;
    }

    public void setDeviceSerial(String deviceSerial) {
        this.deviceSerial = deviceSerial;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getContentType() {
        return this.contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public int getMsgDirection() {
        return this.msgDirection;
    }

    public void setMsgDirection(int msgDirection) {
        this.msgDirection = msgDirection;
    }

    public int getSenderType() {
        return this.senderType;
    }

    public void setSenderType(int senderType) {
        this.senderType = senderType;
    }

    public int getSenderName() {
        return this.senderName;
    }

    public void setSenderName(int senderName) {
        this.senderName = senderName;
    }

    public String getMsgPicUrl() {
        return this.msgPicUrl;
    }

    public void setMsgPicUrl(String msgPicUrl) {
        this.msgPicUrl = msgPicUrl;
    }

    public int getMsgStatus() {
        return this.msgStatus;
    }

    public void setMsgStatus(int msgStatus) {
        this.msgStatus = msgStatus;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getInternalCreateTime() {
        return this.internalCreateTime;
    }

    public String getInternalUpdateTime() {
        return this.internalUpdateTime;
    }

    public Calendar getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Calendar createTime) {
        this.createTime = createTime;
    }

    public Calendar getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Calendar updateTime) {
        this.updateTime = updateTime;
    }

    public String getCloudServerUrl() {
        return this.cloudServerUrl;
    }

    public void setCloudServerUrl(String cloudServerUrl) {
        this.cloudServerUrl = cloudServerUrl;
    }
}


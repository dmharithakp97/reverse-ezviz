/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.openapi.bean;

import java.util.Calendar;

public class EZPlayURLParams {
    public static String PROTOCOL_EZVIZ = "ezviz";
    public static String PROTOCOL_RTMP = "rtmp";
    public String host;
    public String deviceSerial;
    public String cameraNo;
    public int videoLevel = 1;
    public int type = 1;
    public Calendar startTime;
    public Calendar endTime;
    public int recodeType;
    public String verifyCode;
    public boolean mute;
    public String alarmId;
    public String url;
    public String vtdu;
    public String protocol;
    public String speed;
    public String scheme;
    public String bizType;
    public String platformId;
}


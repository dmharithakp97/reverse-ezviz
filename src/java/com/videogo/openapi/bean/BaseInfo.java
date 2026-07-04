/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Build$VERSION
 */
package com.videogo.openapi.bean;

import android.os.Build;
import com.videogo.constant.Config;
import com.videogo.constant.RobolectricConfig;
import com.videogo.openapi.BaseAPI;
import com.videogo.openapi.annotation.HttpParam;
import com.videogo.util.LocalInfo;

public class BaseInfo {
    @HttpParam(name="accessToken")
    private String accessToken;
    @HttpParam(name="clientType")
    private String clientType = String.valueOf(13);
    @HttpParam(name="featureCode")
    private String featureCode = LocalInfo.getInstance().getHardwareCode();
    @HttpParam(name="osVersion")
    private String osVersion = Build.VERSION.RELEASE;
    @HttpParam(name="netType")
    private String netType;
    @HttpParam(name="sdkVersion")
    private String sdkVersion;
    @HttpParam(name="appKey")
    private String appkey = BaseAPI.getInstance().getAppKey();
    @HttpParam(name="appID")
    private String appID;
    @HttpParam(name="appName")
    private String appName;

    public BaseInfo() {
        this.accessToken = LocalInfo.getInstance().getEZAccesstoken().getAccessTokenOrHttpToken();
        this.netType = BaseAPI.getInstance().getNetType();
        this.sdkVersion = "v5.27.3.20260319";
        this.appID = LocalInfo.getInstance().getPackageName();
        this.appName = LocalInfo.getInstance().getAppName();
        if (RobolectricConfig.ROBOLECRITIC_TEST) {
            this.osVersion = "1.0.0";
        }
    }

    public BaseInfo(boolean areaDomain) {
        this.netType = BaseAPI.getInstance().getNetType();
        this.sdkVersion = "v5.27.3.20260319";
        this.appID = LocalInfo.getInstance().getPackageName();
        this.appName = LocalInfo.getInstance().getAppName();
        this.accessToken = areaDomain ? LocalInfo.getInstance().getEZAccesstoken().getAccessTokenOrHttpToken() : null;
    }

    public String getSdkVersion() {
        return this.sdkVersion;
    }

    public void setSdkVersion(String sdkVersion) {
        this.sdkVersion = sdkVersion;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public void setFeatureCode(String featureCode) {
        this.featureCode = featureCode;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public void setNetType(String netType) {
        this.netType = netType;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public String getClientType() {
        return this.clientType;
    }

    public String getFeatureCode() {
        return this.featureCode;
    }

    public String getOsVersion() {
        return this.osVersion;
    }

    public String getNetType() {
        return this.netType;
    }

    public String getAppKey() {
        return this.appkey;
    }

    public String getAppID() {
        return this.appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getApiVersion() {
        return null;
    }

    public void fixHttpToken() {
        if (Config.ENABLE_SDK_TKTOKEN) {
            String httpToken;
            this.accessToken = httpToken = LocalInfo.getInstance().getEZAccesstoken().getHttpToken();
        }
    }

    public void fixDeviceToken(String deviceSerial) {
        if (Config.ENABLE_SDK_TKTOKEN) {
            String deviceToken;
            this.accessToken = deviceToken = LocalInfo.getInstance().getEZAccesstoken().getDeviceToken(deviceSerial);
        }
    }

    public void fixDeviceGlobalToken(String deviceSerial, int cameraNo) {
        if (Config.ENABLE_SDK_TKTOKEN) {
            String deviceToken;
            this.accessToken = deviceToken = LocalInfo.getInstance().getEZAccesstoken().getDeviceGlobalToken(deviceSerial, cameraNo);
        }
    }

    public void fixDeviceVideoToken(String deviceSerial, int cameraNo) {
        if (Config.ENABLE_SDK_TKTOKEN) {
            String deviceToken;
            this.accessToken = deviceToken = LocalInfo.getInstance().getEZAccesstoken().getDeviceVideoToken(deviceSerial, cameraNo);
        }
    }

    public void fixAppName2Empty() {
        this.appName = "";
    }
}


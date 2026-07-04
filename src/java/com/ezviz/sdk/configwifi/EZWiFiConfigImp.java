/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.text.TextUtils
 */
package com.ezviz.sdk.configwifi;

import android.content.Context;
import android.text.TextUtils;
import com.ezviz.sdk.configwifi.EZWiFiConfig;
import com.ezviz.sdk.configwifi.EZWiFiConfigApi;
import com.ezviz.sdk.configwifi.ap.ApConfigController;
import com.ezviz.sdk.configwifi.apHttp.ApHttpConfigController;
import com.ezviz.sdk.configwifi.mixedconfig.BanjourDeviceInfo;
import com.ezviz.sdk.configwifi.mixedconfig.BanjourDeviceStateEnum;
import java.util.Timer;
import java.util.TimerTask;

public class EZWiFiConfigImp {
    private static final String TAG = "EZWiFiConfigImp";
    private static EZWiFiConfigImp s_EZWiFiConfigImp;
    private static EZWiFiConfigApi mEZWiFiConfig;
    private EZConfigWifiCallback mCallback;
    private String mDeviceSerial;
    private String mSSID;
    private String mPassword;
    private Context mContext;
    private int mTimeoutsecond = 60;
    private Timer mOvertimeTimer;

    public EZWiFiConfigImp(Context context) {
        this.mContext = context;
        mEZWiFiConfig = EZWiFiConfig.getInstance(context);
    }

    public static EZWiFiConfigImp getInstance(Context context) {
        if (s_EZWiFiConfigImp == null) {
            s_EZWiFiConfigImp = new EZWiFiConfigImp(context);
        }
        return s_EZWiFiConfigImp;
    }

    public void setTimeout(int mTimeoutsecond) {
        this.mTimeoutsecond = mTimeoutsecond;
    }

    public void setmCallback(EZConfigWifiCallback mCallback) {
        this.mCallback = mCallback;
    }

    public boolean setParams(String deviceSerial, String ssid, String password) {
        this.mCallback = null;
        this.mDeviceSerial = deviceSerial;
        this.mSSID = ssid;
        this.mPassword = password;
        return true;
    }

    public void setAPConfigWifiCallback(com.ezviz.sdk.configwifi.common.EZConfigWifiCallback configWifiCallback) {
        mEZWiFiConfig.startAPConfigSearchResult(configWifiCallback);
    }

    public void setAPHttpConfigWifiCallback(com.ezviz.sdk.configwifi.common.EZConfigWifiCallback configWifiCallback) {
        mEZWiFiConfig.startAPHttpConfigSearchResult(configWifiCallback);
    }

    public void startConfigWifi(int mode) {
        mEZWiFiConfig.setParams(this.mDeviceSerial, this.mSSID, this.mPassword);
        if (mode >> 1 == 1) {
            mEZWiFiConfig.startSoundWaveConfig();
        }
        if (mode == 0 || mode == 1 || (mode ^ 2) == 1) {
            mEZWiFiConfig.startSmartConfig();
        }
        mEZWiFiConfig.startBonjourResult(new EZWiFiConfigApi.BanjourDeviceFoundListener(){

            @Override
            public void onDeviceFound(BanjourDeviceInfo banjourDeviceInfo) {
                if (EZWiFiConfigImp.this.mCallback != null) {
                    EZWiFiConfigImp.this.mCallback.onStartConfigWifiCallback(banjourDeviceInfo.deviceSerial, banjourDeviceInfo.getDeviceState());
                }
            }
        });
        if (!TextUtils.isEmpty((CharSequence)this.mDeviceSerial)) {
            this.startOvertimeTimer(this.mTimeoutsecond * 1000, new Runnable(){

                @Override
                public void run() {
                    mEZWiFiConfig.stopBonjour();
                    mEZWiFiConfig.stopSoundWaveConfig();
                }
            });
        }
    }

    public void startAPConfigWifi(String verifyCode) {
        ApConfigController.getInstance().startAPConfigWifiWithSsid(this.mContext, this.mSSID, this.mPassword, this.mDeviceSerial, verifyCode);
    }

    public void startAPConfigWifi(String verifyCode, String deviceHotspotSSID, String deviceHotspotPwd, boolean isAutoConnectDeviceHotSpot) {
        ApConfigController.getInstance().startAPConfigWifiWithSsid(this.mContext, this.mSSID, this.mPassword, this.mDeviceSerial, verifyCode, deviceHotspotSSID, deviceHotspotPwd, isAutoConnectDeviceHotSpot);
    }

    public void stopAPConfigWifi() {
        ApConfigController.getInstance().stopAPConfigWifiWithSsid();
    }

    public void startAPHttpConfigWifi(String configToken, String deviceHotspotSSID, String deviceHotspotPwd, String lbsDomain, boolean isAutoConnectDeviceHotSpot) {
        ApHttpConfigController.getInstance().startNewApConfigWithToken(this.mContext, configToken, this.mSSID, this.mPassword, deviceHotspotSSID, deviceHotspotPwd, lbsDomain, isAutoConnectDeviceHotSpot);
    }

    public void stopAPHttpConfigWifi() {
        ApHttpConfigController.getInstance().stopNewApConfigWithToken();
    }

    public void stopConfigWifi() {
        if (this.mOvertimeTimer != null) {
            this.mOvertimeTimer.cancel();
            this.mOvertimeTimer = null;
        }
        mEZWiFiConfig.stopSmartConfig();
        mEZWiFiConfig.stopSoundWaveConfig();
        mEZWiFiConfig.stopBonjour();
    }

    private void startOvertimeTimer(long time, final Runnable run) {
        if (this.mOvertimeTimer != null) {
            this.mOvertimeTimer.cancel();
            this.mOvertimeTimer = null;
        }
        this.mOvertimeTimer = new Timer();
        this.mOvertimeTimer.schedule(new TimerTask(){

            @Override
            public void run() {
                run.run();
            }
        }, time);
    }

    static {
        mEZWiFiConfig = null;
    }

    public static interface EZConfigWifiCallback {
        public void onStartConfigWifiCallback(String var1, BanjourDeviceStateEnum var2);
    }
}


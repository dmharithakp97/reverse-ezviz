/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.SuppressLint
 *  android.content.Context
 *  android.net.wifi.WifiManager
 *  android.net.wifi.WifiManager$MulticastLock
 *  android.text.TextUtils
 *  com.hikvision.wifi.configuration.BaseUtil
 *  com.hikvision.wifi.configuration.DeviceDiscoveryListener
 *  com.hikvision.wifi.configuration.DeviceInfo
 *  com.hikvision.wifi.configuration.OneStepWifiConfigurationManager
 */
package com.ezviz.sdk.configwifi.mixedconfig;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import com.ezviz.sdk.configwifi.EZConfigWifiInfoEnum;
import com.ezviz.sdk.configwifi.EZWiFiConfig;
import com.ezviz.sdk.configwifi.EZWiFiConfigApi;
import com.ezviz.sdk.configwifi.common.EZConfigWifiCallback;
import com.ezviz.sdk.configwifi.common.LogUtil;
import com.hikvision.wifi.configuration.BaseUtil;
import com.hikvision.wifi.configuration.DeviceDiscoveryListener;
import com.hikvision.wifi.configuration.DeviceInfo;
import com.hikvision.wifi.configuration.OneStepWifiConfigurationManager;
import java.util.Timer;
import java.util.TimerTask;

@SuppressLint(value={"MissingPermission"})
public class EZBonjourController {
    private static final String TAG = EZBonjourController.class.getSimpleName();
    private String maskIpAddress;
    private Timer mOvertimeTimer;
    private OneStepWifiConfigurationManager mOneStepWifiConfigurationManager;
    private WifiManager.MulticastLock multicastLock;
    private Context mContext = null;
    private String mWifiSSID;
    private String mWifiPassword;
    private WifiManager.MulticastLock mMulticastLock;
    private EZConfigWifiCallback mEZStartConfigWifiCallback;
    private String mDeviceSerial;
    private EZWiFiConfigApi wiFiConfig;
    private int mode;
    private DeviceDiscoveryListener deviceDiscoveryListener = new DeviceDiscoveryListener(){

        public void onDeviceLost(DeviceInfo deviceInfo) {
        }

        public void onDeviceFound(DeviceInfo deviceInfo) {
            if (deviceInfo == null || deviceInfo.getState() == null) {
                LogUtil.d(TAG, "\u63a5\u6536\u5230\u65e0\u6548\u7684bonjour\u4fe1\u606f \u4e3a\u7a7a");
                return;
            }
            if (TextUtils.isEmpty((CharSequence)EZBonjourController.this.mDeviceSerial) || !TextUtils.isEmpty((CharSequence)EZBonjourController.this.mDeviceSerial) && EZBonjourController.this.mDeviceSerial.equals(deviceInfo.getSerialNo())) {
                LogUtil.d(TAG, "\u8bbe\u5907\u8fd4\u56de\u72b6\u6001\u4fe1\u606f " + deviceInfo.getState().name());
                if ("WIFI".equals(deviceInfo.getState().name())) {
                    LogUtil.d(TAG, "\u63a5\u6536\u5230\u8bbe\u5907\u8fde\u63a5\u4e0awifi\u4fe1\u606f " + deviceInfo.toString());
                    if (EZBonjourController.this.mEZStartConfigWifiCallback != null) {
                        EZBonjourController.this.mEZStartConfigWifiCallback.reportInfo(EZConfigWifiInfoEnum.CONNECTED_TO_WIFI);
                    }
                } else if ("PLAT".equals(deviceInfo.getState().name())) {
                    LogUtil.d(TAG, "\u8bbe\u5907\u8fde\u63a5\u4e0a\u5e73\u53f0\u4fe1\u606f " + deviceInfo.toString());
                    if (EZBonjourController.this.mEZStartConfigWifiCallback != null) {
                        EZBonjourController.this.mEZStartConfigWifiCallback.reportInfo(EZConfigWifiInfoEnum.CONNECTED_TO_PLATFORM);
                    }
                }
            }
        }

        public void onError(String error, int errorCode) {
            LogUtil.e(TAG, error + "errorCode:" + errorCode);
        }
    };

    public EZBonjourController(Context context, String deviceSerial, String wifiSSID, String wifiPassword, EZConfigWifiCallback back) {
        this.mContext = context;
        this.mWifiSSID = wifiSSID;
        this.mWifiPassword = wifiPassword;
        this.mEZStartConfigWifiCallback = back;
        this.mDeviceSerial = deviceSerial;
        if (this.mOneStepWifiConfigurationManager == null) {
            this.maskIpAddress = BaseUtil.getMaskIpAddress((Context)this.mContext.getApplicationContext());
            this.mOneStepWifiConfigurationManager = new OneStepWifiConfigurationManager(this.mContext, this.maskIpAddress);
            LogUtil.d(TAG, wifiSSID + " " + wifiPassword + " " + this.maskIpAddress);
        }
        this.wiFiConfig = EZWiFiConfig.getInstance(context);
        this.wiFiConfig.setParams(deviceSerial, wifiSSID, wifiPassword);
    }

    public EZBonjourController(Context context, String wifiSSID, String wifiPassword, DeviceDiscoveryListener listener) {
        this.mContext = context;
        this.mWifiSSID = wifiSSID;
        this.mWifiPassword = wifiPassword;
        this.deviceDiscoveryListener = listener;
        if (this.mOneStepWifiConfigurationManager == null) {
            this.maskIpAddress = BaseUtil.getMaskIpAddress((Context)this.mContext.getApplicationContext());
            this.mOneStepWifiConfigurationManager = new OneStepWifiConfigurationManager(this.mContext, this.maskIpAddress);
            LogUtil.d(TAG, wifiSSID + " " + wifiPassword + " " + this.maskIpAddress);
        }
        this.wiFiConfig = EZWiFiConfig.getInstance(context);
        this.wiFiConfig.setParams("", wifiSSID, wifiPassword);
    }

    public int startConfigWifi(int mode) {
        this.mode = mode;
        WifiManager wifi = (WifiManager)this.mContext.getSystemService("wifi");
        this.mMulticastLock = wifi.createMulticastLock("videogo_multicate_lock");
        this.mMulticastLock.setReferenceCounted(true);
        this.mMulticastLock.acquire();
        if (this.mEZStartConfigWifiCallback != null) {
            this.mEZStartConfigWifiCallback.reportInfo(EZConfigWifiInfoEnum.CONNECTING_TO_WIFI);
        }
        Thread thr = new Thread(){

            @Override
            public void run() {
                EZBonjourController.this.startBonjour();
            }
        };
        thr.start();
        return 0;
    }

    public int stopConfig() {
        if (this.mMulticastLock != null) {
            this.mMulticastLock.release();
            this.mMulticastLock = null;
        }
        this.stopConfigAndBonjour();
        return 0;
    }

    private synchronized int sendConfigData() {
        int startSendConfigData = this.mOneStepWifiConfigurationManager.startConfig(this.mWifiSSID, this.mWifiPassword);
        if (startSendConfigData == 2) {
            LogUtil.d(TAG, "\u5f00\u59cb\u5411\u7f51\u5173\u5730\u5740: " + this.maskIpAddress + " \u53d1\u9001\u6570\u636e: ssid: " + this.mWifiSSID + " key:" + this.mWifiPassword);
        } else if (startSendConfigData == 3) {
            LogUtil.d(TAG, "\u8c03\u7528\u53d1\u9001\u63a5\u53e3: \u53c2\u6570\u5f02\u5e38");
        } else if (startSendConfigData == 1) {
            LogUtil.d(TAG, "\u6b63\u5728\u53d1\u9001\uff0c\u8bf7\u7a0d\u5019...");
        }
        return startSendConfigData;
    }

    public void startBonjour() {
        if (this.mode >> 1 == 1) {
            this.wiFiConfig.startSoundWaveConfig();
        }
        if (this.mode == 0 || this.mode == 1 || (this.mode ^ 2) == 1) {
            this.sendConfigData();
        }
        this.acquireWifiLock();
        if (this.mOneStepWifiConfigurationManager != null) {
            this.mOneStepWifiConfigurationManager.startBonjour(this.deviceDiscoveryListener);
        }
    }

    private void stopBonjour() {
        this.releaseWifiLock();
        this.stopConfigAndBonjour();
    }

    private synchronized void stopConfigAndBonjour() {
        if (this.mode >> 1 == 1) {
            this.wiFiConfig.stopSoundWaveConfig();
        }
        if (this.mOneStepWifiConfigurationManager != null) {
            this.mOneStepWifiConfigurationManager.stopConfig();
            this.mOneStepWifiConfigurationManager.stopSmartBonjour();
            LogUtil.d(TAG, "stopConfigAndBonjour is invoked...");
        }
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

    private void acquireWifiLock() {
        if (this.multicastLock != null) {
            return;
        }
        WifiManager wifi = (WifiManager)this.mContext.getSystemService("wifi");
        this.multicastLock = wifi.createMulticastLock("videogo_multicate_lock");
        this.multicastLock.setReferenceCounted(true);
        this.multicastLock.acquire();
    }

    private void releaseWifiLock() {
        if (this.multicastLock != null) {
            this.multicastLock.release();
            this.multicastLock = null;
        }
    }
}


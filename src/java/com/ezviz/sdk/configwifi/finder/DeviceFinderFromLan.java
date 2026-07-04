/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.net.wifi.ScanResult
 *  android.net.wifi.WifiManager
 *  android.util.Log
 *  com.hikvision.sadp.DeviceFindCallBack
 *  com.hikvision.sadp.SADP_DEVICE_INFO
 *  com.hikvision.sadp.Sadp
 */
package com.ezviz.sdk.configwifi.finder;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;
import com.ezviz.sdk.configwifi.WiFiUtils;
import com.ezviz.sdk.configwifi.ap.WiFi;
import com.ezviz.sdk.configwifi.common.ConfigWifiTaskManager;
import com.ezviz.sdk.configwifi.common.LogUtil;
import com.ezviz.sdk.configwifi.finder.DeviceFindParam;
import com.ezviz.sdk.configwifi.finder.DeviceFinderAbstract;
import com.hikvision.sadp.DeviceFindCallBack;
import com.hikvision.sadp.SADP_DEVICE_INFO;
import com.hikvision.sadp.Sadp;

public class DeviceFinderFromLan
extends DeviceFinderAbstract {
    private static final String TAG = DeviceFinderFromLan.class.getSimpleName();
    private static final int CONNECT_WIFI_INTERVAL = 10000;
    private static final int CONNECT_WIFI_TOTAL_TIME = 40000;
    private static Context mContext;
    private static DeviceFinderFromLan mDeviceFinderFromLan;
    private boolean isFinding = false;
    private DeviceFindCallBack mSadpCallback = new DeviceFindCallBack(){

        public void fDeviceFindCallBack(SADP_DEVICE_INFO sadp_device_info) {
            Log.d((String)TAG, (String)"find new device...");
            String serial = WiFiUtils.byteArray2String(sadp_device_info.szSerialNO);
            if (WiFiUtils.isConnectedToTargetWifi(mContext, DeviceFinderFromLan.this.mParam.wifiName)) {
                if (serial != null && DeviceFinderFromLan.this.mParam.serial != null && serial.contains(DeviceFinderFromLan.this.mParam.serial)) {
                    LogUtil.i(TAG, "find target device " + serial + " in wifi, named with " + DeviceFinderFromLan.this.mParam.wifiName);
                    ConfigWifiTaskManager.getInstance().submit(new Runnable(){

                        @Override
                        public void run() {
                            if (DeviceFinderFromLan.this.mCallback != null) {
                                DeviceFinderFromLan.this.mCallback.onFind(DeviceFinderFromLan.this.mParam.serial);
                            }
                        }
                    });
                    DeviceFinderFromLan.this.isFinding = false;
                } else {
                    LogUtil.d(TAG, "find other device " + serial + " in wifi, named with " + DeviceFinderFromLan.this.mParam.wifiName);
                }
            }
        }
    };

    public static void init(Context context) {
        mContext = context;
    }

    public static DeviceFinderFromLan getInstance() {
        return mDeviceFinderFromLan;
    }

    @Override
    public void start(DeviceFindParam param) {
        super.start(param);
        if (this.checkWifi()) {
            this.tryToSearchTargetDevice();
        } else {
            ConfigWifiTaskManager.getInstance().submit(new Runnable(){

                @Override
                public void run() {
                    DeviceFinderFromLan.this.tryToConnectToRouter();
                }
            });
        }
    }

    @Override
    public void stop() {
        super.stop();
    }

    private boolean checkWifi() {
        LogUtil.d(TAG, "Enter checkWifi");
        boolean ret = WiFiUtils.isConnectedToTargetWifi(mContext, this.mParam.wifiName);
        LogUtil.i(TAG, "is connected to target wifi ? " + ret);
        return ret;
    }

    private void tryToConnectToRouter() {
        LogUtil.d(TAG, "Enter tryToConnectToRouter");
        final WifiManager wifiManager = (WifiManager)mContext.getApplicationContext().getSystemService("wifi");
        Runnable connectTask = new Runnable(){

            @Override
            public void run() {
                LogUtil.i(TAG, "try to connect to " + DeviceFinderFromLan.this.mParam.wifiName);
                ScanResult scanResult = WiFiUtils.getTargetWifiScanInfo(mContext, DeviceFinderFromLan.this.mParam.wifiName);
                if (scanResult != null) {
                    WiFi.configWifiInfo(wifiManager, DeviceFinderFromLan.this.mParam.wifiName, DeviceFinderFromLan.this.mParam.wifiPwd, scanResult);
                } else {
                    LogUtil.i(TAG, "not find wifi, named with " + DeviceFinderFromLan.this.mParam.wifiName);
                    wifiManager.startScan();
                }
            }
        };
        int retryCnt = 0;
        boolean ret = false;
        while (this.isFinding && retryCnt < 4) {
            if (WiFiUtils.isConnectedToTargetWifi(mContext, this.mParam.wifiName)) {
                LogUtil.i(TAG, "connected to " + this.mParam.wifiName + ", spent time for connecting is " + retryCnt * 10000);
                ret = true;
                break;
            }
            ConfigWifiTaskManager.getInstance().submit(connectTask);
            ++retryCnt;
            if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
            }
            try {
                Thread.sleep(10000L);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        LogUtil.i(TAG, "the result of connecting to " + this.mParam.wifiName + " ? " + ret);
        if (ret) {
            this.tryToSearchTargetDevice();
        }
    }

    private void tryToSearchTargetDevice() {
        LogUtil.d(TAG, "Enter tryToSearchTargetDevice");
        ConfigWifiTaskManager.getInstance().submit(new Runnable(){

            @Override
            public void run() {
                Sadp sadp = Sadp.getInstance();
                sadp.SADP_Stop();
                sadp.SADP_Clearup();
                if (sadp.SADP_Start_V30(DeviceFinderFromLan.this.mSadpCallback)) {
                    LogUtil.d(TAG, "start search, " + DeviceFinderFromLan.this.mParam.serial);
                    sadp.SADP_SetAutoRequestInterval(5);
                } else {
                    LogUtil.e(TAG, "failed to start search, " + DeviceFinderFromLan.this.mParam.serial);
                }
            }
        });
    }

    static {
        mDeviceFinderFromLan = new DeviceFinderFromLan();
    }
}


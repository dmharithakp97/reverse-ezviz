/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.SuppressLint
 *  android.app.Application
 *  android.content.Context
 *  android.net.ConnectivityManager
 *  android.net.Network
 *  android.net.NetworkInfo
 *  android.net.wifi.WifiInfo
 *  android.net.wifi.WifiManager
 *  android.os.Build$VERSION
 *  android.os.CountDownTimer
 *  android.util.Log
 */
package com.ezviz.sdk.configwifi.apHttp;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.Log;
import com.ezviz.http.exception.EzConfigWifiException;
import com.ezviz.sdk.configwifi.Config;
import com.ezviz.sdk.configwifi.EZConfigWifiErrorEnum;
import com.ezviz.sdk.configwifi.EZConfigWifiInfoEnum;
import com.ezviz.sdk.configwifi.EZWiFiConfigManager;
import com.ezviz.sdk.configwifi.ap.WiFiConnectResultListener;
import com.ezviz.sdk.configwifi.ap.WiFiConnectorAbstract;
import com.ezviz.sdk.configwifi.ap.WiFiConnectorManager;
import com.ezviz.sdk.configwifi.common.EZConfigWifiCallback;
import com.ezviz.sdk.configwifi.touchAp.StartNewApConfigCallback;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressLint(value={"MissingPermission"})
public class ApHttpConfigController {
    private static final String TAG = "ApHttpConfigController";
    private Context mContext;
    private ExecutorService cachedThreadPool;
    private EZConfigWifiCallback mHttpConfigCallback;
    @SuppressLint(value={"StaticFieldLeak"})
    private static ApHttpConfigController mHttpWifiConfig;
    private String mConfigToken;
    private String mLbsDomain;
    private String mSSID;
    private int mTargetWifiNetId;
    private String mPassword;
    private String mDeviceWifiSSID = null;
    private WifiManager mWifiManager;
    private boolean isAutoConnectDeviceHotSpot = false;
    private int mTimeOutSecond = Config.mTimeoutSecond;
    private volatile boolean isTimeOut;
    private WiFiConnectorAbstract mWiFiConnector;
    private CountDownTimer countDownTimer;
    private Runnable mTryToConfigWifiTask = new Runnable(){

        @Override
        public void run() {
            if (!ApHttpConfigController.this.isTimeOut) {
                ApHttpConfigController.this.bindAppToTargetDeviceNetwork();
                EZWiFiConfigManager.startNewApConfigWithToken((Application)ApHttpConfigController.this.mContext, ApHttpConfigController.this.mConfigToken, ApHttpConfigController.this.mSSID, ApHttpConfigController.this.mPassword, ApHttpConfigController.this.mLbsDomain, new StartNewApConfigCallback(){

                    @Override
                    public void onResponse(int statusCode, String statusDesc) {
                        Log.w((String)ApHttpConfigController.TAG, (String)"config wifi success");
                        if (Build.VERSION.SDK_INT < 29) {
                            boolean isSuccess = ApHttpConfigController.this.mWifiManager.enableNetwork(ApHttpConfigController.this.mTargetWifiNetId, true);
                            Log.w((String)ApHttpConfigController.TAG, (String)("connect to target wifi : " + isSuccess));
                        }
                        if (ApHttpConfigController.this.countDownTimer != null) {
                            ApHttpConfigController.this.countDownTimer.cancel();
                            ApHttpConfigController.this.countDownTimer = null;
                        }
                        if (ApHttpConfigController.this.mHttpConfigCallback != null) {
                            ApHttpConfigController.this.mHttpConfigCallback.reportInfo(EZConfigWifiInfoEnum.CONNECTING_SENT_CONFIGURATION_TO_DEVICE);
                        }
                        ApHttpConfigController.this.stopNewApConfigWithToken();
                    }

                    @Override
                    public void onError(EzConfigWifiException ezConfigWifiException) {
                        ApHttpConfigController.this.recoveryAppToNormalNetwork();
                        if (ApHttpConfigController.this.isAutoConnectDeviceHotSpot && !ApHttpConfigController.this.isConnectedToDeviceWifi() && ApHttpConfigController.this.mWiFiConnector != null && ApHttpConfigController.this.mWiFiConnector.isPaused()) {
                            Log.v((String)ApHttpConfigController.TAG, (String)"not connected to device wifi, try to connect again!");
                            ApHttpConfigController.this.mWiFiConnector.resume();
                        }
                        try {
                            Thread.sleep(5000L);
                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        ApHttpConfigController.this.cachedThreadPool.submit(ApHttpConfigController.this.mTryToConfigWifiTask);
                    }
                });
            }
        }
    };

    private ApHttpConfigController() {
        this.cachedThreadPool = Executors.newSingleThreadExecutor();
    }

    public static ApHttpConfigController getInstance() {
        if (mHttpWifiConfig == null) {
            mHttpWifiConfig = new ApHttpConfigController();
        }
        return mHttpWifiConfig;
    }

    public void setApHttpConfigCallback(EZConfigWifiCallback httpConfigCallback) {
        this.mHttpConfigCallback = httpConfigCallback;
    }

    private void bindAppToTargetDeviceNetwork() {
        try {
            ConnectivityManager mConnectivityManager = (ConnectivityManager)this.mContext.getSystemService("connectivity");
            if (mConnectivityManager == null) {
                return;
            }
            if (Build.VERSION.SDK_INT >= 23) {
                Network[] nets;
                boolean isSuc = false;
                for (Network net : nets = mConnectivityManager.getAllNetworks()) {
                    NetworkInfo networkInfo = mConnectivityManager.getNetworkInfo(net);
                    if (networkInfo == null || networkInfo.getType() != 1) continue;
                    mConnectivityManager.bindProcessToNetwork(net);
                    isSuc = true;
                }
                Log.d((String)TAG, (String)("the result of binding app net traffic to wifi is: " + isSuc));
            } else {
                Log.d((String)TAG, (String)"do not need  binding app net traffic to wifi");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint(value={"NewApi"})
    private void recoveryAppToNormalNetwork() {
        try {
            ConnectivityManager mConnectivityManager = (ConnectivityManager)this.mContext.getSystemService("connectivity");
            if (mConnectivityManager == null) {
                return;
            }
            mConnectivityManager.bindProcessToNetwork(null);
            Log.d((String)TAG, (String)"binding app net traffic to null (recovery normal)");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isConnectedToDeviceWifi() {
        boolean ret = false;
        if (this.mWifiManager != null && this.mWifiManager.getConnectionInfo() != null && this.mWifiManager.getConnectionInfo().getSSID() != null) {
            String currentWifiSSID = this.mWifiManager.getConnectionInfo().getSSID();
            Log.v((String)TAG, (String)("deviceWifiSSID: " + this.mDeviceWifiSSID));
            Log.v((String)TAG, (String)("currentWifiSSID: " + currentWifiSSID));
            if (currentWifiSSID != null && this.mDeviceWifiSSID != null) {
                ret = currentWifiSSID.contains(this.mDeviceWifiSSID);
            }
        }
        Log.e((String)TAG, (String)("isConnectedToDeviceWifi: " + ret));
        return ret;
    }

    public void stopNewApConfigWithToken() {
        Log.i((String)TAG, (String)"stopNewApConfigWithToken");
        if (this.countDownTimer != null) {
            this.countDownTimer.cancel();
            this.countDownTimer = null;
        }
        this.isTimeOut = true;
        if (this.isConnectedToDeviceWifi()) {
            this.recoveryAppToNormalNetwork();
        }
        this.cachedThreadPool.shutdown();
        if (this.mWiFiConnector != null) {
            this.mWiFiConnector.stop();
        }
    }

    public void startNewApConfigWithToken(Context context, String configToken, String targetWifiName, String targetWifiPwd, String deviceHotspotSSID, String deviceHotspotPwd, String lbsDomain, boolean isAutoConnectDeviceHotSpot) {
        Log.i((String)TAG, (String)"startNewApConfigWithToken");
        this.mContext = context;
        this.mConfigToken = configToken;
        this.mLbsDomain = lbsDomain;
        this.isAutoConnectDeviceHotSpot = isAutoConnectDeviceHotSpot;
        this.mWifiManager = (WifiManager)this.mContext.getApplicationContext().getSystemService("wifi");
        if (this.countDownTimer != null) {
            this.countDownTimer.cancel();
            this.countDownTimer = null;
        }
        this.isTimeOut = true;
        this.cachedThreadPool.shutdown();
        if (this.mWiFiConnector != null) {
            this.mWiFiConnector.stop();
        }
        if (this.cachedThreadPool.isShutdown()) {
            this.cachedThreadPool = Executors.newSingleThreadExecutor();
        }
        this.countDownTimer = new CountDownTimer(this.mTimeOutSecond * 1000, 1000L){

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                ApHttpConfigController.this.isTimeOut = true;
                if (ApHttpConfigController.this.mHttpConfigCallback != null) {
                    ApHttpConfigController.this.mHttpConfigCallback.reportError(EZConfigWifiErrorEnum.CONFIG_TIMEOUT);
                }
                ApHttpConfigController.this.stopNewApConfigWithToken();
            }
        };
        this.countDownTimer.start();
        WifiInfo mWifiInfo = this.mWifiManager.getConnectionInfo();
        this.isTimeOut = false;
        this.mSSID = targetWifiName;
        this.mPassword = targetWifiPwd;
        this.mTargetWifiNetId = mWifiInfo.getNetworkId();
        if (!isAutoConnectDeviceHotSpot) {
            this.cachedThreadPool.submit(this.mTryToConfigWifiTask);
            return;
        }
        this.mDeviceWifiSSID = deviceHotspotSSID;
        String mDeviceWifiPwd = deviceHotspotPwd;
        this.mWiFiConnector = WiFiConnectorManager.getWiFiConnectorByOsVersion(this.mContext, this.mDeviceWifiSSID, mDeviceWifiPwd);
        this.mWiFiConnector.setListener(new WiFiConnectResultListener(){

            @Override
            public void onSuccess() {
                Log.d((String)ApHttpConfigController.TAG, (String)"WiFiConnecter onSuccess");
                ApHttpConfigController.this.cachedThreadPool.submit(ApHttpConfigController.this.mTryToConfigWifiTask);
            }

            @Override
            public void onFailure(int errorCode) {
                Log.d((String)ApHttpConfigController.TAG, (String)("WiFiConnecter onFailure " + errorCode));
                ApHttpConfigController.this.mHttpConfigCallback.onError(errorCode, null);
            }
        });
        this.mWiFiConnector.start();
    }
}


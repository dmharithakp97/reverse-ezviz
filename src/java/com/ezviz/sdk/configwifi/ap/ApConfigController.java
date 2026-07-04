/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.SuppressLint
 *  android.content.Context
 *  android.net.ConnectivityManager
 *  android.net.Network
 *  android.net.NetworkInfo
 *  android.net.wifi.WifiInfo
 *  android.net.wifi.WifiManager
 *  android.os.Build$VERSION
 *  android.os.CountDownTimer
 *  android.text.TextUtils
 *  android.util.Log
 *  com.hikvision.netsdk.HCNetSDK
 *  com.hikvision.netsdk.NET_DVR_CONFIG
 *  com.hikvision.netsdk.NET_DVR_DEVICEINFO_V30
 *  com.hikvision.netsdk.NET_DVR_WIFI_CFG
 */
package com.ezviz.sdk.configwifi.ap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import com.ezviz.sdk.configwifi.Config;
import com.ezviz.sdk.configwifi.EZConfigWifiErrorEnum;
import com.ezviz.sdk.configwifi.EZConfigWifiInfoEnum;
import com.ezviz.sdk.configwifi.ap.WiFiConnectResultListener;
import com.ezviz.sdk.configwifi.ap.WiFiConnectorAbstract;
import com.ezviz.sdk.configwifi.ap.WiFiConnectorManager;
import com.ezviz.sdk.configwifi.common.EZConfigWifiCallback;
import com.hikvision.netsdk.HCNetSDK;
import com.hikvision.netsdk.NET_DVR_CONFIG;
import com.hikvision.netsdk.NET_DVR_DEVICEINFO_V30;
import com.hikvision.netsdk.NET_DVR_WIFI_CFG;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressLint(value={"MissingPermission"})
public class ApConfigController {
    private static final String TAG = "APWifiConfig";
    private Context mContext;
    private ExecutorService cachedThreadPool;
    private EZConfigWifiCallback mApConfigCallback;
    @SuppressLint(value={"StaticFieldLeak"})
    private static ApConfigController mAPWifiConfig;
    private String mSSID;
    private String mTargetWifiMacAddress;
    private int mTargetWifiNetId;
    private String mPassword;
    private String mVerifyCode;
    private String mDeviceWifiSSID = null;
    private WifiManager mWifiManager;
    private boolean isAutoConnectDeviceHotSpot = false;
    private int mTimeOutSecond = Config.mTimeoutSecond;
    private volatile boolean isTimeOut;
    private WiFiConnectorAbstract mWiFiConnector;
    private CountDownTimer countDownTimer;
    private Runnable mTryToLoginDeviceAndConfigWifiTask = new Runnable(){

        @Override
        public void run() {
            if (!ApConfigController.this.isTimeOut) {
                Log.d((String)ApConfigController.TAG, (String)"mTryToLoginDeviceAndConfigWifiTask");
                ApConfigController.this.bindAppToTargetDeviceNetwork();
                int userID = this.tryToLoginDevice(ApConfigController.this.mVerifyCode);
                if (userID >= 0 && this.tryToConfigWifi(userID, ApConfigController.this.mTargetWifiMacAddress, ApConfigController.this.mSSID.getBytes(), ApConfigController.this.mPassword)) {
                    Log.w((String)ApConfigController.TAG, (String)"config wifi success");
                    if (Build.VERSION.SDK_INT < 29) {
                        boolean isSuccess = ApConfigController.this.mWifiManager.enableNetwork(ApConfigController.this.mTargetWifiNetId, true);
                        Log.w((String)ApConfigController.TAG, (String)("connect to target wifi : " + isSuccess));
                    }
                    if (ApConfigController.this.countDownTimer != null) {
                        ApConfigController.this.countDownTimer.cancel();
                        ApConfigController.this.countDownTimer = null;
                    }
                    if (ApConfigController.this.mApConfigCallback != null) {
                        ApConfigController.this.mApConfigCallback.reportInfo(EZConfigWifiInfoEnum.CONNECTING_SENT_CONFIGURATION_TO_DEVICE);
                    }
                    ApConfigController.this.stopAPConfigWifiWithSsid();
                } else {
                    if (ApConfigController.this.isAutoConnectDeviceHotSpot && !ApConfigController.this.isConnectedToDeviceWifi() && ApConfigController.this.mWiFiConnector != null && ApConfigController.this.mWiFiConnector.isPaused()) {
                        Log.v((String)ApConfigController.TAG, (String)"not connected to device wifi, try to connect again!");
                        ApConfigController.this.mWiFiConnector.resume();
                    }
                    ApConfigController.this.recoveryAppToNormalNetwork();
                    try {
                        Thread.sleep(5000L);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ApConfigController.this.cachedThreadPool.submit(ApConfigController.this.mTryToLoginDeviceAndConfigWifiTask);
                }
            }
        }

        private int tryToLoginDevice(String verifyCode) {
            NET_DVR_DEVICEINFO_V30 deviceInfoV30 = new NET_DVR_DEVICEINFO_V30();
            int userID = HCNetSDK.getInstance().NET_DVR_Login_V30("192.168.8.1", 8000, "admin", verifyCode, deviceInfoV30);
            if (userID < 0 && HCNetSDK.getInstance().NET_DVR_GetLastError() == 1) {
                userID = HCNetSDK.getInstance().NET_DVR_Login_V30("192.168.8.1", 8000, "EZ_LOCAL_USER", this.encrypt16(verifyCode).substring(8, 24), deviceInfoV30);
                Log.e((String)ApConfigController.TAG, (String)("tryToLoginDevice retry userId : " + userID + " LastError : " + HCNetSDK.getInstance().NET_DVR_GetLastError()));
            }
            Log.e((String)ApConfigController.TAG, (String)("tryToLoginDevice userId : " + userID + " LastError : " + HCNetSDK.getInstance().NET_DVR_GetLastError()));
            return userID;
        }

        public String encrypt16(String encryptStr) {
            try {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                byte[] md5Bytes = md5.digest(encryptStr.getBytes());
                StringBuffer hexValue = new StringBuffer();
                for (int i = 0; i < md5Bytes.length; ++i) {
                    int val = md5Bytes[i] & 0xFF;
                    if (val < 16) {
                        hexValue.append("0");
                    }
                    hexValue.append(Integer.toHexString(val));
                }
                encryptStr = hexValue.toString();
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
            return encryptStr;
        }

        private boolean tryToConfigWifi(int iLogID, String mac, byte[] ssidBytes, String wifiPwd) {
            if (iLogID < 0) {
                return false;
            }
            NET_DVR_WIFI_CFG cfg = new NET_DVR_WIFI_CFG();
            boolean status = HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iLogID, 307, -1, (NET_DVR_CONFIG)cfg);
            if (!status) {
                this.handleErrorFromHCNETSDK();
                return false;
            }
            Log.w((String)ApConfigController.TAG, (String)"NET_DVR_GetDVRConfig success!");
            cfg.dwMode = 0;
            Arrays.fill(cfg.sEssid, (byte)0);
            System.arraycopy(ssidBytes, 0, cfg.sEssid, 0, Math.min(ssidBytes.length, cfg.sEssid.length));
            if (TextUtils.isEmpty((CharSequence)wifiPwd)) {
                cfg.dwSecurity = 0;
            } else {
                cfg.dwSecurity = 4;
                byte[] pwdBytes = wifiPwd.getBytes();
                Arrays.fill(cfg.wpa_psk.sKeyInfo, (byte)0);
                System.arraycopy(pwdBytes, 0, cfg.wpa_psk.sKeyInfo, 0, pwdBytes.length);
                cfg.wpa_psk.dwKeyLength = pwdBytes.length;
            }
            Log.w((String)ApConfigController.TAG, (String)("target wifi mac address is :" + mac));
            if (!TextUtils.isEmpty((CharSequence)mac)) {
                byte[] newMac = ApConfigController.getMacBytes(mac);
                System.arraycopy(newMac, 0, cfg.struEtherNet.byMACAddr, 0, newMac.length);
            }
            status = HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iLogID, 306, -1, (NET_DVR_CONFIG)cfg);
            HCNetSDK.getInstance().NET_DVR_Logout_V30(iLogID);
            if (!status) {
                this.handleErrorFromHCNETSDK();
                return false;
            }
            return true;
        }

        private int handleErrorFromHCNETSDK() {
            int errCode = HCNetSDK.getInstance().NET_DVR_GetLastError();
            EZConfigWifiErrorEnum errMsg = null;
            switch (errCode) {
                case 1: {
                    errMsg = EZConfigWifiErrorEnum.WRONG_DEVICE_VERIFY_CODE;
                    break;
                }
                case 7: {
                    break;
                }
            }
            if (ApConfigController.this.mApConfigCallback != null && errMsg != null) {
                ApConfigController.this.mApConfigCallback.reportError(errMsg);
            }
            return errCode;
        }
    };

    private ApConfigController() {
        this.cachedThreadPool = Executors.newSingleThreadExecutor();
        try {
            HCNetSDK.getInstance().NET_DVR_Init();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public static ApConfigController getInstance() {
        if (mAPWifiConfig == null) {
            mAPWifiConfig = new ApConfigController();
        }
        return mAPWifiConfig;
    }

    public void setApConfigCallback(EZConfigWifiCallback apConfigCallback) {
        this.mApConfigCallback = apConfigCallback;
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
                Log.d((String)TAG, (String)"do not need binding app net traffic to wifi");
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

    public void stopAPConfigWifiWithSsid() {
        Log.i((String)TAG, (String)"stopAPConfigWifiWithSsid");
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

    public void startAPConfigWifiWithSsid(Context context, String targetWifiName, String targetWifiPwd, String deviceSerial, String verifyCode) {
        this.startAPConfigWifiWithSsid(context, targetWifiName, targetWifiPwd, deviceSerial, verifyCode, "", "", true);
    }

    public void startAPConfigWifiWithSsid(Context context, String targetWifiName, String targetWifiPwd, String deviceSerial, String verifyCode, String deviceHotspotSSID, String deviceHotspotPwd, boolean isAutoConnectDeviceHotSpot) {
        Log.i((String)TAG, (String)"startAPConfigWifiWithSsid");
        this.mContext = context;
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
                ApConfigController.this.isTimeOut = true;
                if (ApConfigController.this.mApConfigCallback != null) {
                    ApConfigController.this.mApConfigCallback.reportError(EZConfigWifiErrorEnum.CONFIG_TIMEOUT);
                }
                ApConfigController.this.stopAPConfigWifiWithSsid();
            }
        };
        this.countDownTimer.start();
        WifiInfo mWifiInfo = this.mWifiManager.getConnectionInfo();
        this.isTimeOut = false;
        this.mSSID = targetWifiName;
        this.mPassword = targetWifiPwd;
        this.mVerifyCode = verifyCode;
        this.mTargetWifiMacAddress = mWifiInfo.getBSSID();
        this.mTargetWifiNetId = mWifiInfo.getNetworkId();
        if (!isAutoConnectDeviceHotSpot) {
            this.cachedThreadPool.submit(this.mTryToLoginDeviceAndConfigWifiTask);
            return;
        }
        this.mDeviceWifiSSID = TextUtils.isEmpty((CharSequence)deviceHotspotSSID) ? "EZVIZ_" + deviceSerial : deviceHotspotSSID;
        String mDeviceWifiPwd = TextUtils.isEmpty((CharSequence)deviceHotspotPwd) ? "EZVIZ_" + verifyCode : deviceHotspotPwd;
        this.mWiFiConnector = WiFiConnectorManager.getWiFiConnectorByOsVersion(this.mContext, this.mDeviceWifiSSID, mDeviceWifiPwd);
        this.mWiFiConnector.setListener(new WiFiConnectResultListener(){

            @Override
            public void onSuccess() {
                Log.d((String)ApConfigController.TAG, (String)"WiFiConnecter onSuccess");
                ApConfigController.this.cachedThreadPool.submit(ApConfigController.this.mTryToLoginDeviceAndConfigWifiTask);
            }

            @Override
            public void onFailure(int errorCode) {
                Log.d((String)ApConfigController.TAG, (String)("WiFiConnecter onFailure " + errorCode));
                ApConfigController.this.mApConfigCallback.onError(errorCode, null);
            }
        });
        this.mWiFiConnector.start();
    }

    private static byte[] getMacBytes(String mac) {
        byte[] macBytes = new byte[6];
        String[] strArr = mac.split(":");
        for (int i = 0; i < strArr.length; ++i) {
            int value = Integer.parseInt(strArr[i], 16);
            macBytes[i] = (byte)value;
        }
        return macBytes;
    }
}


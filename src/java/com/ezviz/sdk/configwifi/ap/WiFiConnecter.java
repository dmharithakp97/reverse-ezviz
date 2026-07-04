/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.SuppressLint
 *  android.content.BroadcastReceiver
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentFilter
 *  android.net.NetworkInfo
 *  android.net.NetworkInfo$DetailedState
 *  android.net.wifi.ScanResult
 *  android.net.wifi.SupplicantState
 *  android.net.wifi.WifiInfo
 *  android.net.wifi.WifiManager
 *  android.os.Handler
 *  android.os.Message
 *  android.text.TextUtils
 */
package com.ezviz.sdk.configwifi.ap;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import com.ezviz.sdk.configwifi.Config;
import com.ezviz.sdk.configwifi.EZConfigWifiErrorEnum;
import com.ezviz.sdk.configwifi.ap.ConnectionDetector;
import com.ezviz.sdk.configwifi.ap.WiFi;
import com.ezviz.sdk.configwifi.common.LogUtil;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WiFiConnecter {
    public static final int PARAM_ERROR = 1;
    public static final int PASSWORD_ERROR = 2;
    public static final int CONNECT_ERROR = 3;
    public static final int SCAN_ERROR = 4;
    public static final int COUNTOUT_ERROR = 5;
    private static final String TAG = WiFiConnecter.class.getSimpleName();
    private static final int WIFI_RESCAN_INTERVAL_MS = 15000;
    private static final int MAX_RETRY_COUNT = WiFiConnecter.getRetryCntByTimeout();
    private Context mContext;
    private WifiManager mWifiManager;
    private final IntentFilter mFilter;
    private final BroadcastReceiver mReceiver;
    private final Scanner mScanner;
    private ActionListener mListener;
    private String mSsid;
    private String mPassword;
    private boolean isRegistered;
    private int linkWifiResult = -1;
    private NetworkInfo.DetailedState detailedState = NetworkInfo.DetailedState.IDLE;
    private static boolean isActiveScan;
    private static boolean bySsidIgnoreCase;
    private static String connectSsid;
    public boolean isPaused = false;
    private static boolean isStrongConnectMode;
    private ExecutorService cachedThreadPool;
    private boolean foundAnyWifi;
    private boolean foundDeviceHotspot;

    public WiFiConnecter(Context context) {
        this.mContext = context;
        this.mWifiManager = (WifiManager)context.getSystemService("wifi");
        this.cachedThreadPool = Executors.newSingleThreadExecutor();
        this.mFilter = new IntentFilter();
        this.mFilter.addAction("android.net.wifi.SCAN_RESULTS");
        this.mFilter.addAction("android.net.wifi.supplicant.STATE_CHANGE");
        this.mFilter.addAction("android.net.wifi.STATE_CHANGE");
        this.mFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        this.mReceiver = new BroadcastReceiver(){

            public void onReceive(Context context, Intent intent) {
                WiFiConnecter.this.handleEvent(context, intent);
            }
        };
        bySsidIgnoreCase = true;
        this.mScanner = new Scanner();
    }

    public void connectStrongMode(String ssid, String password, ActionListener listener) {
        isStrongConnectMode = true;
        this.connect(ssid, password, listener);
    }

    public void connect(String ssid, String password, ActionListener listener) {
        WifiInfo info;
        this.mListener = listener;
        this.mSsid = ssid;
        this.mPassword = password;
        if (TextUtils.isEmpty((CharSequence)this.mSsid)) {
            if (listener != null) {
                listener.onFailure(1);
                listener.onFinished(false);
            }
            return;
        }
        if (listener != null) {
            listener.onStarted(ssid);
        }
        if (this.mWifiManager.isWifiEnabled() && (info = this.mWifiManager.getConnectionInfo()) != null && ConnectionDetector.isWifi(this.mContext) && info.getSSID() != null) {
            boolean ssidEquals = WiFiConnecter.isSsidEquals(this.mSsid, info.getSSID());
            if (ssidEquals) {
                connectSsid = null;
                if (listener != null) {
                    listener.onSuccess(info);
                    listener.onFinished(true);
                }
                return;
            }
            connectSsid = WiFiConnecter.convertToNoQuotedString(info.getSSID());
        }
        if (!this.isRegistered) {
            this.mContext.registerReceiver(this.mReceiver, this.mFilter);
            this.isRegistered = true;
        }
        this.mScanner.forceScan();
    }

    private static boolean isSsidEquals(String ssid, String SSID) {
        return WiFi.isSsidEquals(ssid, SSID, bySsidIgnoreCase);
    }

    private boolean isPasswordError() {
        return this.linkWifiResult == 1 || this.detailedState == NetworkInfo.DetailedState.AUTHENTICATING;
    }

    private void handleScanResults() {
        Runnable task = new Runnable(){

            @Override
            public void run() {
                LogUtil.d(TAG, "handleScanResults.isActiveScan =" + isActiveScan);
                if (!isActiveScan) {
                    return;
                }
                List results = WiFiConnecter.this.mWifiManager.getScanResults();
                LogUtil.d(TAG, "handleScanResults.getScanResults results.size =" + results.size());
                if (results.size() > 0 && !WiFiConnecter.this.foundAnyWifi) {
                    WiFiConnecter.this.foundAnyWifi = true;
                }
                for (ScanResult result : results) {
                    if (!isActiveScan) {
                        return;
                    }
                    LogUtil.d(TAG, "resultSsid =" + result.SSID + "  targetSsid = " + WiFiConnecter.this.mSsid);
                    boolean ssidEquals = WiFiConnecter.isSsidEquals(WiFiConnecter.this.mSsid, result.SSID);
                    if (!ssidEquals) continue;
                    boolean connectToNewNetwork = false;
                    try {
                        connectToNewNetwork = isStrongConnectMode ? WiFi.configWifiInfo(WiFiConnecter.this.mWifiManager, WiFiConnecter.this.mSsid, WiFiConnecter.this.mPassword, result) : WiFi.connectToNewNetwork(WiFiConnecter.this.mWifiManager, result, WiFiConnecter.this.mPassword);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (!connectToNewNetwork) {
                        WiFiConnecter.this.mScanner.post(new Runnable(){

                            @Override
                            public void run() {
                                if (WiFiConnecter.this.mListener != null) {
                                    WiFiConnecter.this.mListener.onFailure(WiFiConnecter.this.isPasswordError() ? 2 : 3);
                                    WiFiConnecter.this.mListener.onFinished(false);
                                }
                                WiFiConnecter.this.onPause();
                            }
                        });
                    }
                    if (WiFiConnecter.this.foundDeviceHotspot) break;
                    WiFiConnecter.this.foundDeviceHotspot = true;
                    break;
                }
                if (!WiFiConnecter.this.isPaused) {
                    WiFiConnecter.this.mScanner.sendEmptyMessageDelayed(0, 15000L);
                }
            }
        };
        try {
            this.cachedThreadPool.execute(task);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleEvent(Context context, Intent intent) {
        String action = intent.getAction();
        if (!"android.net.wifi.SCAN_RESULTS".equals(action) || !isActiveScan) {
            if ("android.net.wifi.STATE_CHANGE".equals(action)) {
                boolean ssidEquals;
                WifiInfo mWifiInfo;
                NetworkInfo mInfo = (NetworkInfo)intent.getParcelableExtra("networkInfo");
                if (null != mInfo) {
                    LogUtil.d(TAG, "NETWORK_STATE_CHANGED_ACTION state:" + mInfo.getState());
                    this.detailedState = mInfo.getDetailedState();
                    LogUtil.d(TAG, "NETWORK_STATE_CHANGED_ACTION DetailedState:" + this.detailedState);
                }
                if ((mWifiInfo = this.mWifiManager.getConnectionInfo()) != null && mWifiInfo.getSSID() != null && (ssidEquals = WiFiConnecter.isSsidEquals(this.mSsid, mWifiInfo.getSSID()))) {
                    isActiveScan = false;
                    if (this.mListener != null) {
                        this.mListener.onSuccess(mWifiInfo);
                        this.mListener.onFinished(true);
                    }
                    this.onPause();
                }
            } else if (action.equals("android.net.wifi.supplicant.STATE_CHANGE")) {
                SupplicantState state = SupplicantState.INVALID;
                try {
                    state = (SupplicantState)intent.getParcelableExtra("newState");
                    LogUtil.d(TAG, "SUPPLICANT_STATE_CHANGED_ACTION state:" + state);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                this.linkWifiResult = intent.getIntExtra("supplicantError", -1);
                LogUtil.d(TAG, "SUPPLICANT_STATE_CHANGED_ACTION linkWifiResult:" + this.linkWifiResult);
            }
        }
    }

    public void onPause() {
        LogUtil.v(TAG, "onPause");
        this.isPaused = true;
        if (this.isRegistered && this.mReceiver != null) {
            this.mContext.unregisterReceiver(this.mReceiver);
            this.isRegistered = false;
        }
        this.mScanner.pause();
        this.cachedThreadPool.shutdown();
    }

    public void onResume() {
        LogUtil.v(TAG, "onResume");
        this.isPaused = false;
        if (!this.isRegistered) {
            this.mContext.registerReceiver(this.mReceiver, this.mFilter);
            this.isRegistered = true;
        }
        this.cachedThreadPool = Executors.newSingleThreadExecutor();
        this.mScanner.resume();
    }

    public void destroy() {
        this.mListener = null;
        this.onPause();
    }

    private void checkScanError() {
        if (!this.foundAnyWifi) {
            if (this.mListener != null) {
                this.mListener.onFailure(EZConfigWifiErrorEnum.MAY_LACK_LOCATION_PERMISSION);
            }
        } else if (!this.foundDeviceHotspot) {
            if (this.mListener != null) {
                this.mListener.onFailure(EZConfigWifiErrorEnum.NOT_FIND_DEVICE_HOTSPOT);
            }
        } else {
            LogUtil.i(TAG, "not find any scan error");
        }
    }

    public static boolean isIsStrongConnectMode() {
        return isStrongConnectMode;
    }

    public static void setIsStrongConnectMode(boolean isStrongConnectMode) {
        WiFiConnecter.isStrongConnectMode = isStrongConnectMode;
    }

    public static String convertToNoQuotedString(String string) {
        if (TextUtils.isEmpty((CharSequence)string)) {
            return "";
        }
        int lastPos = string.length() - 1;
        if (lastPos > 2 && string.charAt(0) == '\"' && string.charAt(lastPos) == '\"') {
            return string.substring(1, lastPos);
        }
        return string;
    }

    private static int getRetryCntByTimeout() {
        int totalScanTime = Config.mTimeoutSecond;
        int scanIntervalTime = 15;
        int maxRetryCnt = totalScanTime / scanIntervalTime - 1;
        LogUtil.i(TAG, "max count of scanning wifi is " + maxRetryCnt);
        return maxRetryCnt;
    }

    static {
        isStrongConnectMode = false;
    }

    @SuppressLint(value={"HandlerLeak"})
    private class Scanner
    extends Handler {
        private int mRetry = 0;

        private Scanner() {
        }

        void resume() {
            if (!this.hasMessages(0)) {
                this.sendEmptyMessage(0);
            }
        }

        void forceScan() {
            this.removeMessages(0);
            this.sendEmptyMessage(0);
        }

        void pause() {
            this.mRetry = 0;
            isActiveScan = false;
            this.removeMessages(0);
        }

        public void handleMessage(Message message) {
            if (this.mRetry < MAX_RETRY_COUNT) {
                ++this.mRetry;
                isActiveScan = true;
                boolean isWifiEnabled = WiFiConnecter.this.mWifiManager.isWifiEnabled();
                LogUtil.i(TAG, "try to scan wifi for " + this.mRetry + "th time");
                if (!isWifiEnabled) {
                    try {
                        isWifiEnabled = WiFiConnecter.this.mWifiManager.setWifiEnabled(true);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                LogUtil.d(TAG, "setWifiEnabled:" + isWifiEnabled);
                if (isWifiEnabled) {
                    try {
                        WiFiConnecter.this.mWifiManager.startScan();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    LogUtil.i(TAG, "handleMessage: call handleScanResults");
                    WiFiConnecter.this.handleScanResults();
                }
            } else {
                this.mRetry = 0;
                isActiveScan = false;
                WiFiConnecter.this.checkScanError();
                if (WiFiConnecter.this.mListener != null) {
                    WiFiConnecter.this.mListener.onFailure(WiFiConnecter.this.isPasswordError() ? 2 : 5);
                }
                WiFiConnecter.this.onPause();
            }
        }
    }

    public static abstract class ActionListener
    implements ActionListenerInterface {
        @Override
        public void onStarted(String ssid) {
        }

        @Override
        public void onFinished(boolean isSuccessed) {
        }
    }

    public static interface ActionListenerInterface {
        public void onStarted(String var1);

        public void onSuccess(WifiInfo var1);

        public void onFailure(int var1);

        public void onFailure(EZConfigWifiErrorEnum var1);

        public void onFinished(boolean var1);
    }
}


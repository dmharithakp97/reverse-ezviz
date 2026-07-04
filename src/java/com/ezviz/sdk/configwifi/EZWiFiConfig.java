/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.media.MediaPlayer
 *  android.media.MediaPlayer$OnCompletionListener
 *  android.media.MediaPlayer$OnPreparedListener
 *  android.net.wifi.WifiManager
 *  android.net.wifi.WifiManager$MulticastLock
 *  android.text.TextUtils
 *  android.util.Log
 *  com.hikvision.sadp.DeviceFindCallBack
 *  com.hikvision.sadp.SADP_DEVICE_INFO
 *  com.hikvision.sadp.Sadp
 *  com.hikvision.wifi.configuration.BaseUtil
 *  com.hikvision.wifi.configuration.DeviceDiscoveryListener
 *  com.hikvision.wifi.configuration.DeviceInfo
 *  com.hikvision.wifi.configuration.OneStepWifiConfigurationManager
 */
package com.ezviz.sdk.configwifi;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;
import com.ezviz.sdk.configwifi.EZWiFiConfigApi;
import com.ezviz.sdk.configwifi.WiFiUtils;
import com.ezviz.sdk.configwifi.ap.ApConfigController;
import com.ezviz.sdk.configwifi.apHttp.ApHttpConfigController;
import com.ezviz.sdk.configwifi.common.EZConfigWifiCallback;
import com.ezviz.sdk.configwifi.mixedconfig.BanjourDeviceInfo;
import com.ezviz.sdk.configwifi.mixedconfig.BanjourDeviceStateEnum;
import com.ezviz.sdk.configwifi.smartconfig.WifiSoundConfig;
import com.hikvision.sadp.DeviceFindCallBack;
import com.hikvision.sadp.SADP_DEVICE_INFO;
import com.hikvision.sadp.Sadp;
import com.hikvision.wifi.configuration.BaseUtil;
import com.hikvision.wifi.configuration.DeviceDiscoveryListener;
import com.hikvision.wifi.configuration.DeviceInfo;
import com.hikvision.wifi.configuration.OneStepWifiConfigurationManager;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EZWiFiConfig
implements EZWiFiConfigApi {
    private static final String TAG = "EZWiFiConfig";
    private String maskIpAddress;
    private OneStepWifiConfigurationManager mOneStepWifiConfigurationManager;
    private WifiManager.MulticastLock multicastLock;
    private String mWifiSSID;
    private String mWifiPassword;
    private String mDeviceSerial;
    private Sadp mSadp = null;
    private Context mContext;
    static EZWiFiConfig mEZWiFiConfig = null;
    private MediaPlayer mMediaPlayer;
    private Thread mVoiceThread;
    private EZWiFiConfigApi.BanjourDeviceFoundListener mBanjourDeviceFoundListener;
    private ExecutorService cachedThreadPool;
    private EZWiFiConfigApi.SadpDeviceFoundListener mSadpDeviceFoundListener;
    private DeviceFindCallBack mDeviceFindCallBack = new DeviceFindCallBack(){

        public void fDeviceFindCallBack(SADP_DEVICE_INFO sadp_device_info) {
            String szSerialNO = WiFiUtils.byteArray2String(sadp_device_info.szSerialNO);
            Log.d((String)EZWiFiConfig.TAG, (String)("SadpDeviceFoundListener onDeviceFound==" + szSerialNO));
            if (!TextUtils.isEmpty((CharSequence)szSerialNO)) {
                Log.d((String)"startSadp", (String)szSerialNO);
                if (EZWiFiConfig.this.mSadpDeviceFoundListener != null) {
                    EZWiFiConfig.this.mSadpDeviceFoundListener.onDeviceFound(szSerialNO);
                }
            }
        }
    };
    private static final int KEY_TYPE_OPEN = 0;
    private static final int KEY_TYPE_WEP = 1;
    private static final int KEY_TYPE_AES = 2;
    private static final int KEY_TYPE_TKIP = 3;
    private static final int KEY_TYPE_NONE = 128;
    private DeviceDiscoveryListener deviceDiscoveryListener = new DeviceDiscoveryListener(){

        public void onDeviceLost(DeviceInfo deviceInfo) {
        }

        public void onDeviceFound(DeviceInfo deviceInfo) {
            Log.d((String)EZWiFiConfig.TAG, (String)("deviceDiscoveryListener  onDeviceFound==" + deviceInfo.getName() + "  " + deviceInfo.getSerialNo()));
            if (deviceInfo == null || deviceInfo.getState() == null) {
                Log.d((String)EZWiFiConfig.TAG, (String)"\u63a5\u6536\u5230\u65e0\u6548\u7684bonjour\u4fe1\u606f \u4e3a\u7a7a");
                return;
            }
            BanjourDeviceInfo banjourDeviceInfo = new BanjourDeviceInfo();
            banjourDeviceInfo.setName(deviceInfo.getName());
            banjourDeviceInfo.setDeviceSerial(deviceInfo.getSerialNo());
            banjourDeviceInfo.setDeviceType(deviceInfo.getType());
            if (!TextUtils.isEmpty((CharSequence)EZWiFiConfig.this.mDeviceSerial) && EZWiFiConfig.this.mDeviceSerial.equalsIgnoreCase(deviceInfo.getSerialNo()) || TextUtils.isEmpty((CharSequence)EZWiFiConfig.this.mDeviceSerial)) {
                if ("WIFI".equals(deviceInfo.getState().name())) {
                    banjourDeviceInfo.setDeviceState(BanjourDeviceStateEnum.DEVICE_WIFI_CONNECTED);
                    Log.d((String)EZWiFiConfig.TAG, (String)("\u63a5\u6536\u5230\u8bbe\u5907\u8fde\u63a5\u4e0awifi\u4fe1\u606f " + deviceInfo.toString()));
                    if (EZWiFiConfig.this.mBanjourDeviceFoundListener != null) {
                        EZWiFiConfig.this.mBanjourDeviceFoundListener.onDeviceFound(banjourDeviceInfo);
                    }
                    EZWiFiConfig.this.stopSoundWaveConfig();
                } else if ("PLAT".equals(deviceInfo.getState().name())) {
                    banjourDeviceInfo.setDeviceState(BanjourDeviceStateEnum.DEVICE_PLATFORM_REGISTED);
                    if (EZWiFiConfig.this.mBanjourDeviceFoundListener != null) {
                        EZWiFiConfig.this.mBanjourDeviceFoundListener.onDeviceFound(banjourDeviceInfo);
                    }
                }
            }
        }

        public void onError(String error, int errorCode) {
            Log.e((String)EZWiFiConfig.TAG, (String)(error + "errorCode:" + errorCode));
        }
    };

    public static EZWiFiConfigApi getInstance(Context context) {
        if (mEZWiFiConfig == null) {
            mEZWiFiConfig = new EZWiFiConfig(context.getApplicationContext());
        }
        return mEZWiFiConfig;
    }

    private EZWiFiConfig(Context context) {
        this.mContext = context;
        this.mSadp = Sadp.getInstance();
        this.initVoicePlayThread();
        this.cachedThreadPool = Executors.newSingleThreadExecutor();
        if (this.mOneStepWifiConfigurationManager == null) {
            this.maskIpAddress = BaseUtil.getMaskIpAddress((Context)this.mContext.getApplicationContext());
            this.mOneStepWifiConfigurationManager = new OneStepWifiConfigurationManager(this.mContext, this.maskIpAddress);
        }
    }

    @Override
    public boolean setParams(String deviceSerial, String ssid, String password) {
        if (TextUtils.isEmpty((CharSequence)ssid)) {
            return false;
        }
        this.mWifiSSID = ssid;
        this.mWifiPassword = password;
        this.mDeviceSerial = deviceSerial;
        if (this.mOneStepWifiConfigurationManager == null) {
            this.maskIpAddress = BaseUtil.getMaskIpAddress((Context)this.mContext.getApplicationContext());
            this.mOneStepWifiConfigurationManager = new OneStepWifiConfigurationManager(this.mContext, this.maskIpAddress);
            Log.d((String)TAG, (String)(ssid + " " + password + " " + this.maskIpAddress));
        }
        return true;
    }

    @Override
    public int startSmartConfig() {
        if (!this.checkParams()) {
            return -1;
        }
        this.cachedThreadPool.submit(new Runnable(){

            @Override
            public void run() {
                EZWiFiConfig.this.acquireWifiLock();
                EZWiFiConfig.this.sendConfigData();
            }
        });
        return 0;
    }

    @Override
    public boolean startSADPSearchResult(EZWiFiConfigApi.SadpDeviceFoundListener sadpDeviceFoundListener) {
        boolean ret = true;
        this.mSadpDeviceFoundListener = sadpDeviceFoundListener;
        this.cachedThreadPool.submit(new Runnable(){

            @Override
            public void run() {
                if (EZWiFiConfig.this.mSadp != null) {
                    int error = EZWiFiConfig.this.mSadp.SADP_GetLastError();
                    Log.d((String)EZWiFiConfig.TAG, (String)("SADP_GetLastError  " + error));
                    EZWiFiConfig.this.mSadp.SADP_Clearup();
                    boolean ret = EZWiFiConfig.this.mSadp.SADP_Start_V30(EZWiFiConfig.this.mDeviceFindCallBack);
                    Log.d((String)EZWiFiConfig.TAG, (String)("SADP_Start_V30  " + ret));
                    if (ret) {
                        EZWiFiConfig.this.mSadp.SADP_SetAutoRequestInterval(5);
                    }
                }
            }
        });
        return ret;
    }

    @Override
    public boolean startAPConfigSearchResult(EZConfigWifiCallback callback) {
        ApConfigController.getInstance().setApConfigCallback(callback);
        return true;
    }

    @Override
    public boolean startAPHttpConfigSearchResult(EZConfigWifiCallback callback) {
        ApHttpConfigController.getInstance().setApHttpConfigCallback(callback);
        return true;
    }

    @Override
    public int stopSmartConfig() {
        this.cachedThreadPool.submit(new Runnable(){

            @Override
            public void run() {
                EZWiFiConfig.this.releaseWifiLock();
                long b = System.currentTimeMillis();
                if (EZWiFiConfig.this.mOneStepWifiConfigurationManager != null) {
                    EZWiFiConfig.this.mOneStepWifiConfigurationManager.stopConfig();
                    Log.d((String)EZWiFiConfig.TAG, (String)"stopConfig is invoked...");
                }
                Log.d((String)EZWiFiConfig.TAG, (String)("unInit config time = " + (System.currentTimeMillis() - b)));
            }
        });
        return 0;
    }

    @Override
    public synchronized boolean stopSADPSearch() {
        boolean ret = true;
        this.mSadpDeviceFoundListener = null;
        this.cachedThreadPool.submit(new Runnable(){

            @Override
            public void run() {
                long d = System.currentTimeMillis();
                if (EZWiFiConfig.this.mSadp != null) {
                    boolean ret = EZWiFiConfig.this.mSadp.SADP_Stop();
                    Log.d((String)EZWiFiConfig.TAG, (String)("SADP_Stop  " + ret));
                }
                Log.d((String)EZWiFiConfig.TAG, (String)("stopSadp time = " + (System.currentTimeMillis() - d)));
            }
        });
        return ret;
    }

    @Override
    public void startSoundWaveConfig() {
        if (!this.checkParams()) {
            return;
        }
        this.cachedThreadPool.submit(this.mVoiceThread);
    }

    @Override
    public void stopSoundWaveConfig() {
        this.cachedThreadPool.submit(new Runnable(){

            @Override
            public void run() {
                long a = System.currentTimeMillis();
                if (EZWiFiConfig.this.mMediaPlayer != null) {
                    if (EZWiFiConfig.this.mMediaPlayer.isPlaying()) {
                        EZWiFiConfig.this.mMediaPlayer.stop();
                    }
                    EZWiFiConfig.this.mMediaPlayer.reset();
                    EZWiFiConfig.this.mMediaPlayer.release();
                    EZWiFiConfig.this.mMediaPlayer = null;
                }
                Log.d((String)EZWiFiConfig.TAG, (String)("stopVoiceWaveConfig time = " + (System.currentTimeMillis() - a)));
            }
        });
    }

    @Override
    public void startBonjourResult(EZWiFiConfigApi.BanjourDeviceFoundListener mBanjourDeviceFoundListener) {
        this.mBanjourDeviceFoundListener = mBanjourDeviceFoundListener;
        this.cachedThreadPool.submit(new Runnable(){

            @Override
            public void run() {
                EZWiFiConfig.this.acquireWifiLock();
                if (EZWiFiConfig.this.mOneStepWifiConfigurationManager != null) {
                    EZWiFiConfig.this.mOneStepWifiConfigurationManager.startBonjour(EZWiFiConfig.this.deviceDiscoveryListener);
                }
            }
        });
    }

    @Override
    public void stopBonjour() {
        this.mBanjourDeviceFoundListener = null;
        this.cachedThreadPool.execute(new Runnable(){

            @Override
            public void run() {
                EZWiFiConfig.this.releaseWifiLock();
                if (EZWiFiConfig.this.mOneStepWifiConfigurationManager != null) {
                    EZWiFiConfig.this.mOneStepWifiConfigurationManager.stopSmartBonjour();
                }
            }
        });
    }

    @Override
    public void startAPConfig(String verifyCode) {
        ApConfigController.getInstance().startAPConfigWifiWithSsid(this.mContext, this.mWifiSSID, this.mWifiPassword, this.mDeviceSerial, verifyCode);
    }

    @Override
    public void startAPConfig(String verifyCode, String routerNamePre, String routerPasswordPre, boolean isAutoConnectDeviceHotSpot) {
        ApConfigController.getInstance().startAPConfigWifiWithSsid(this.mContext, this.mWifiSSID, this.mWifiPassword, this.mDeviceSerial, verifyCode, routerNamePre, routerPasswordPre, isAutoConnectDeviceHotSpot);
    }

    @Override
    public void stopAPConfig() {
        ApConfigController.getInstance().stopAPConfigWifiWithSsid();
    }

    private synchronized int sendConfigData() {
        int startSendConfigData = this.mOneStepWifiConfigurationManager.startConfig(this.mWifiSSID, this.mWifiPassword);
        if (startSendConfigData == 2) {
            Log.d((String)TAG, (String)("maskIpAddress: " + this.maskIpAddress + "      ssid: " + this.mWifiSSID + " key:" + this.mWifiPassword));
        } else if (startSendConfigData == 3) {
            Log.d((String)TAG, (String)"error");
        } else if (startSendConfigData == 1) {
            Log.d((String)TAG, (String)"sending");
        }
        return startSendConfigData;
    }

    private void acquireWifiLock() {
        if (this.multicastLock != null) {
            return;
        }
        WifiManager wifi = (WifiManager)this.mContext.getSystemService("wifi");
        this.multicastLock = wifi.createMulticastLock("ez_config_demo_multicate_lock");
        this.multicastLock.setReferenceCounted(true);
        this.multicastLock.acquire();
    }

    private void releaseWifiLock() {
        if (this.multicastLock != null) {
            this.multicastLock.release();
            this.multicastLock = null;
        }
    }

    private byte[] getWifiConfigPassword(Context context, String wifiSSID, String wifiPassword) {
        byte[] pwd = !TextUtils.isEmpty((CharSequence)wifiPassword) ? wifiPassword.getBytes() : null;
        byte[] configPassword = new byte[7 + (pwd != null ? pwd.length : 0)];
        try {
            String security = WiFiUtils.getSSIDSecurity(context, wifiSSID);
            configPassword[0] = TextUtils.isEmpty((CharSequence)security) ? -128 : (security.equals("Open") ? 0 : (security.equals("WEP") ? 1 : (security.equals("WPA") || security.equals("WPA2") ? 2 : -128)));
            String macAddr = WiFiUtils.getWifiMacAddress(context);
            if (!TextUtils.isEmpty((CharSequence)macAddr)) {
                String[] bssid = macAddr.split(":");
                for (int i = 0; i < bssid.length && i < 6; ++i) {
                    configPassword[i + 1] = (byte)Integer.parseInt(bssid[i], 16);
                }
            }
            if (pwd != null) {
                System.arraycopy(pwd, 0, configPassword, 7, pwd.length);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return configPassword;
    }

    private boolean checkParams() {
        if (TextUtils.isEmpty((CharSequence)this.mWifiSSID)) {
            Log.d((String)TAG, (String)"ssid is null");
            return false;
        }
        return true;
    }

    private void initVoicePlayThread() {
        if (this.mVoiceThread == null) {
            this.mVoiceThread = new Thread(new Runnable(){

                @Override
                public void run() {
                    if (!EZWiFiConfig.this.checkParams()) {
                        return;
                    }
                    File filecache = EZWiFiConfig.this.mContext.getCacheDir();
                    File file = new File(filecache, "voicemsg.wav");
                    boolean ret = WifiSoundConfig.generateWifiConfigWave(file.getAbsolutePath(), EZWiFiConfig.this.mWifiSSID.getBytes(), EZWiFiConfig.this.getWifiConfigPassword(EZWiFiConfig.this.mContext, EZWiFiConfig.this.mWifiSSID, EZWiFiConfig.this.mWifiPassword), 0);
                    Log.d((String)"startVoiceWaveConfig", (String)("\u751f\u6210\u58f0\u6ce2\u6587\u4ef6\uff1a" + ret));
                    if (ret) {
                        try {
                            if (EZWiFiConfig.this.mMediaPlayer != null) {
                                EZWiFiConfig.this.mMediaPlayer.reset();
                            }
                            if (EZWiFiConfig.this.mMediaPlayer == null) {
                                EZWiFiConfig.this.mMediaPlayer = new MediaPlayer();
                                EZWiFiConfig.this.mMediaPlayer.setAudioStreamType(3);
                                EZWiFiConfig.this.mMediaPlayer.setLooping(false);
                                EZWiFiConfig.this.mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){

                                    public void onPrepared(MediaPlayer mediaPlayer) {
                                        mediaPlayer.start();
                                    }
                                });
                                EZWiFiConfig.this.mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){

                                    public void onCompletion(MediaPlayer mediaPlayer) {
                                        EZWiFiConfig.this.cachedThreadPool.submit(new Runnable(){

                                            @Override
                                            public void run() {
                                                try {
                                                    Thread.sleep(5000L);
                                                }
                                                catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                        EZWiFiConfig.this.cachedThreadPool.submit(EZWiFiConfig.this.mVoiceThread);
                                    }
                                });
                            }
                            EZWiFiConfig.this.mMediaPlayer.setDataSource(file.getAbsolutePath());
                            EZWiFiConfig.this.mMediaPlayer.prepareAsync();
                        }
                        catch (Throwable e) {
                            e.printStackTrace();
                            Log.d((String)"startVoiceWaveConfig", (String)("\u64ad\u653e\u5668\u8bbe\u7f6e\u58f0\u97f3\u5931\u8d25\uff1a" + e.getMessage()));
                        }
                    }
                }
            }, "voiceplay_thread");
        }
    }
}


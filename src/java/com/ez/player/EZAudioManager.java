/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.bluetooth.BluetoothAdapter
 *  android.content.BroadcastReceiver
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentFilter
 *  android.content.pm.PackageManager
 *  android.media.AudioDeviceCallback
 *  android.media.AudioDeviceInfo
 *  android.media.AudioManager
 *  android.os.Build$VERSION
 *  android.os.Handler
 *  android.os.Looper
 */
package com.ez.player;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.AudioDeviceCallback;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import com.ez.statistics.ScoState;
import com.ez.stream.LogUtil;
import java.lang.reflect.Method;
import java.util.Locale;

public class EZAudioManager {
    private static final int SPEAKER_MODE_HEADSET = 0;
    private static final int SPEAKER_MODE_SPEAKER = 1;
    private static final int SPEAKER_MODE_BLUETOOTH = 2;
    private static EZAudioManager manager = null;
    private static final String TAG = "EZAudioManager";
    private boolean init;
    private Context mContext;
    private AudioManager mAudioManager;
    private BroadcastReceiver mAudioReceiver;
    private Handler mMainHandler;
    private int mTempAudioRouter = -1;
    private int currentCheckTimes = 0;
    private final int checkTimes = 6;
    private int blueToothSCOState = 0;
    private long lastSCOStateTime = 0L;
    private long firstStartSCOTime = -1L;
    private long scoCostTime = -1L;
    private int originRouter = -1;
    private Runnable mCheckRunnable = new Runnable(){

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void run() {
            EZAudioManager.this.currentCheckTimes++;
            EZAudioManager eZAudioManager = EZAudioManager.this;
            synchronized (eZAudioManager) {
                EZAudioManager.this.setSpeakerMode(false);
                EZAudioManager.this.check();
            }
        }
    };
    private AudioDeviceCallback mAudioDeviceCallback;

    private EZAudioManager() {
    }

    public static EZAudioManager getInstance() {
        if (manager == null) {
            manager = new EZAudioManager();
        }
        return manager;
    }

    public synchronized void enableAutoRouter() {
        if (this.mAudioManager == null) {
            return;
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.media.ACTION_SCO_AUDIO_STATE_UPDATED");
        if (Build.VERSION.SDK_INT > 30) {
            this.mAudioDeviceCallback = new AudioDeviceCallback(){

                public void onAudioDevicesAdded(AudioDeviceInfo[] addedDevices) {
                    super.onAudioDevicesAdded(addedDevices);
                    EZAudioManager.this.updateModeInner(false, true);
                }

                public void onAudioDevicesRemoved(AudioDeviceInfo[] removedDevices) {
                    super.onAudioDevicesRemoved(removedDevices);
                    EZAudioManager.this.updateModeInner(false, true);
                }
            };
            this.mAudioManager.registerAudioDeviceCallback(this.mAudioDeviceCallback, null);
        } else {
            intentFilter.addAction("android.intent.action.HEADSET_PLUG");
            intentFilter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
            intentFilter.addAction("android.bluetooth.adapter.action.CONNECTION_STATE_CHANGED");
            intentFilter.addAction("android.intent.action.HDMI_PLUGGED");
        }
        if (this.mAudioReceiver == null) {
            this.initReceiver();
        }
        if (this.mAudioReceiver != null) {
            if (Build.VERSION.SDK_INT >= 26) {
                this.mContext.registerReceiver(this.mAudioReceiver, intentFilter, 4);
            } else {
                this.mContext.registerReceiver(this.mAudioReceiver, intentFilter);
            }
            LogUtil.i(TAG, "registerReceiver mAudioReceiver");
        }
    }

    public synchronized void init(Context context, AudioManager audioManager, boolean forceSpeakerOn) {
        if (this.init) {
            return;
        }
        LogUtil.i(TAG, "Init");
        this.blueToothSCOState = 0;
        this.init = true;
        this.mContext = context.getApplicationContext();
        this.mAudioManager = audioManager;
        this.mMainHandler = new Handler(Looper.getMainLooper());
        this.updateModeInner(forceSpeakerOn, true);
    }

    public synchronized void uninit() {
        if (this.mAudioReceiver != null) {
            LogUtil.i(TAG, "unregisterReceiver mAudioReceiver");
            this.mContext.unregisterReceiver(this.mAudioReceiver);
            this.mAudioReceiver = null;
        }
        if (this.mAudioManager != null) {
            this.mAudioManager.stopBluetoothSco();
            this.mAudioManager.setBluetoothScoOn(false);
            if (Build.VERSION.SDK_INT >= 23 && this.mAudioDeviceCallback != null) {
                this.mAudioManager.unregisterAudioDeviceCallback(this.mAudioDeviceCallback);
                this.mAudioDeviceCallback = null;
            }
        }
        this.currentCheckTimes = 0;
        if (this.mMainHandler != null) {
            this.mMainHandler.removeCallbacks(this.mCheckRunnable);
            this.mMainHandler = null;
        }
        this.init = false;
        this.mAudioManager = null;
        this.mContext = null;
        this.mTempAudioRouter = -1;
        this.blueToothSCOState = 0;
        this.lastSCOStateTime = 0L;
        this.firstStartSCOTime = -1L;
        this.scoCostTime = -1L;
        this.originRouter = -1;
    }

    private void initReceiver() {
        this.mAudioReceiver = new BroadcastReceiver(){

            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                LogUtil.i(EZAudioManager.TAG, "onReceive action " + action);
                switch (action) {
                    case "android.intent.action.HEADSET_PLUG": 
                    case "android.bluetooth.adapter.action.STATE_CHANGED": 
                    case "android.bluetooth.adapter.action.CONNECTION_STATE_CHANGED": {
                        EZAudioManager.this.updateModeInner(false, true);
                        break;
                    }
                    case "android.media.ACTION_SCO_AUDIO_STATE_UPDATED": {
                        EZAudioManager.this.blueToothSCOState = intent.getIntExtra("android.media.extra.SCO_AUDIO_STATE", 0);
                        LogUtil.i(EZAudioManager.TAG, "onReceive blueToothSCOState " + EZAudioManager.this.blueToothSCOState);
                        EZAudioManager.this.lastSCOStateTime = System.currentTimeMillis();
                        if (EZAudioManager.this.scoCostTime != -1L || EZAudioManager.this.blueToothSCOState != 1) break;
                        EZAudioManager.this.scoCostTime = EZAudioManager.this.lastSCOStateTime - EZAudioManager.this.firstStartSCOTime;
                    }
                }
            }
        };
    }

    public void setSpeakerPhoneOn(boolean on) {
        this.updateModeInner(on, false);
    }

    public ScoState getSCOConnectStatistics() {
        ScoState state = new ScoState();
        state.sco_t = (int)this.scoCostTime;
        state.sco_state = this.firstStartSCOTime == -1L ? -1 : this.blueToothSCOState;
        state.router = this.originRouter;
        return state;
    }

    private synchronized void updateModeInner(boolean forceSpeakerOn, boolean inner) {
        int lastRouter = this.mTempAudioRouter;
        if (forceSpeakerOn) {
            this.mTempAudioRouter = 1;
        } else if (this.isWiredHeadsetOn()) {
            this.mTempAudioRouter = 0;
        } else if (this.isBluetoothHeadsetOn()) {
            this.mTempAudioRouter = 2;
        } else {
            int n = this.mTempAudioRouter = inner ? 1 : 0;
        }
        if (this.originRouter == -1) {
            this.originRouter = this.mTempAudioRouter;
        }
        if (lastRouter != this.mTempAudioRouter && this.mTempAudioRouter == 2 && this.firstStartSCOTime == -1L) {
            this.firstStartSCOTime = System.currentTimeMillis();
        }
        this.setSpeakerMode(true);
    }

    private boolean isWiredHeadsetOn() {
        AudioDeviceInfo[] devices;
        if (this.mAudioManager == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT < 23) {
            return this.mAudioManager.isWiredHeadsetOn();
        }
        for (AudioDeviceInfo deviceInfo : devices = this.mAudioManager.getDevices(2)) {
            if (deviceInfo.getType() != 3 && deviceInfo.getType() != 4 && deviceInfo.getType() != 11 && deviceInfo.getType() != 22 && deviceInfo.getType() != 9) continue;
            return true;
        }
        return false;
    }

    private boolean isBluetoothHeadsetOn() {
        boolean permission2;
        if (this.mAudioManager == null) {
            return false;
        }
        PackageManager pm = this.mContext.getPackageManager();
        if (Build.VERSION.SDK_INT < 23) {
            return this.mAudioManager.isBluetoothScoOn() || this.mAudioManager.isBluetoothA2dpOn();
        }
        if (Build.VERSION.SDK_INT > 30) {
            AudioDeviceInfo[] devices;
            if (!pm.hasSystemFeature("android.hardware.audio.output")) {
                return false;
            }
            for (AudioDeviceInfo deviceInfo : devices = this.mAudioManager.getDevices(2)) {
                if (deviceInfo.getType() != 7 && deviceInfo.getType() != 8) continue;
                return true;
            }
            return false;
        }
        boolean bl = permission2 = 0 == pm.checkPermission("android.permission.BLUETOOTH", this.mContext.getPackageName());
        if (permission2) {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            Class<BluetoothAdapter> bluetoothAdapterClass = BluetoothAdapter.class;
            try {
                Method method = bluetoothAdapterClass.getDeclaredMethod("getConnectionState", null);
                method.setAccessible(true);
                int state = (Integer)method.invoke((Object)bluetoothAdapter, (Object[])null);
                return state == 2 || state == 1;
            }
            catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    private void setSpeakerMode(boolean check) {
        LogUtil.i(TAG, String.format(Locale.CHINA, "setSpeakerMode %d loopTime:%d needCheck %d", this.mTempAudioRouter, this.currentCheckTimes, check ? 1 : 0));
        if (this.mAudioManager == null) {
            return;
        }
        switch (this.mTempAudioRouter) {
            case 1: {
                if (this.mAudioManager.isBluetoothScoOn()) {
                    this.mAudioManager.stopBluetoothSco();
                    this.mAudioManager.setBluetoothScoOn(false);
                }
                if (this.mAudioManager.isSpeakerphoneOn()) break;
                this.mAudioManager.setSpeakerphoneOn(true);
                break;
            }
            case 2: {
                if (!this.mAudioManager.isBluetoothScoAvailableOffCall()) break;
                if (this.blueToothSCOState == 1 || this.blueToothSCOState == 2 && this.lastSCOStateTime + 3000L > System.currentTimeMillis()) {
                    return;
                }
                LogUtil.i(TAG, "startBluetoothSco ");
                this.mAudioManager.stopBluetoothSco();
                this.mAudioManager.startBluetoothSco();
                this.mAudioManager.setBluetoothScoOn(true);
                this.mAudioManager.setSpeakerphoneOn(false);
                break;
            }
            case 0: {
                if (this.mAudioManager.isBluetoothScoOn()) {
                    this.mAudioManager.stopBluetoothSco();
                    this.mAudioManager.setBluetoothScoOn(false);
                }
                if (!this.mAudioManager.isSpeakerphoneOn()) break;
                this.mAudioManager.setSpeakerphoneOn(false);
            }
        }
        if (check && this.mMainHandler != null) {
            this.mMainHandler.postDelayed(new Runnable(){

                @Override
                public void run() {
                    EZAudioManager.this.startCheck();
                }
            }, 1000L);
        }
    }

    private void startCheck() {
        if (this.mAudioManager != null && this.mMainHandler != null) {
            this.currentCheckTimes = 0;
            this.mMainHandler.removeCallbacks(this.mCheckRunnable);
            this.check();
        }
    }

    private void check() {
        if (this.currentCheckTimes < 6 && this.mMainHandler != null) {
            this.mMainHandler.postDelayed(this.mCheckRunnable, 1000L);
        }
    }
}


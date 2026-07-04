/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.media.AudioManager
 */
package com.ezviz.sdk.configwifi.mixedconfig;

import android.content.Context;
import android.media.AudioManager;
import com.ezviz.sdk.configwifi.EZConfigWifiErrorEnum;
import com.ezviz.sdk.configwifi.WiFiUtils;
import com.ezviz.sdk.configwifi.common.ConfigExecutorAbstract;
import com.ezviz.sdk.configwifi.common.EZConfigWifiCallback;
import com.ezviz.sdk.configwifi.common.LogUtil;
import com.ezviz.sdk.configwifi.mixedconfig.EZBonjourController;
import com.ezviz.sdk.configwifi.mixedconfig.MixedConfigMode;
import com.ezviz.sdk.configwifi.mixedconfig.MixedConfigParam;

public class MixedConfigExecutor
extends ConfigExecutorAbstract {
    private static final String TAG = MixedConfigExecutor.class.getSimpleName();
    private static EZBonjourController mEZBonjourController;
    private static final MixedConfigExecutor mInstance;
    private MixedConfigParam mParam;
    private EZConfigWifiCallback mCallback;

    public static MixedConfigExecutor getInstance() {
        return mInstance;
    }

    public void init(Context context) {
        this.mContext = context;
    }

    @Override
    public void setCallback(EZConfigWifiCallback callback) {
        super.setCallback(callback);
        this.mCallback = callback;
    }

    public void setParam(MixedConfigParam param) {
        super.setCommonParam(param);
        this.mParam = param;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void start() throws Exception {
        super.start();
        if (!this.checkParam(this.mParam)) {
            this.reportError(EZConfigWifiErrorEnum.WRONG_CONFIG_PARAM);
            this.isExecuting = false;
            return;
        }
        if (this.isUseSmartConfig(this.mParam.mode) && !WiFiUtils.isConnectedToAnyValidWifi(this.mContext)) {
            this.reportError(EZConfigWifiErrorEnum.CAN_NOT_SEND_CONFIGURATION_TO_DEVICE);
            return;
        }
        if (mEZBonjourController == null) {
            mEZBonjourController = new EZBonjourController(this.mContext, this.mParam.deviceSerial, this.mParam.routerWifiSsid, this.mParam.routerWifiPwd, this.mCallback);
        }
        EZBonjourController eZBonjourController = mEZBonjourController;
        synchronized (eZBonjourController) {
            if (mEZBonjourController != null) {
                mEZBonjourController.startConfigWifi(this.mParam.mode);
                this.isExecuting = true;
            }
        }
        if (this.isUseSoundWaveConfig(this.mParam.mode) && !this.isAdjustVolumeToMax()) {
            this.reportError(EZConfigWifiErrorEnum.PHONE_MEDIA_VALUE_NOT_MAX);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void stop() {
        if (mEZBonjourController != null) {
            EZBonjourController eZBonjourController = mEZBonjourController;
            synchronized (eZBonjourController) {
                if (mEZBonjourController != null) {
                    mEZBonjourController.stopConfig();
                    mEZBonjourController = null;
                }
            }
        }
        this.isExecuting = false;
    }

    private boolean isAdjustVolumeToMax() {
        AudioManager manager = (AudioManager)this.mContext.getSystemService("audio");
        int max = manager.getStreamMaxVolume(3);
        LogUtil.d(TAG, "max volume is " + max);
        int current = manager.getStreamVolume(3);
        LogUtil.d(TAG, "current volume is " + current);
        return current == max;
    }

    private boolean isUseSmartConfig(int mode) {
        int targetMode = MixedConfigMode.EZWiFiConfigSmart;
        return targetMode == (targetMode & mode);
    }

    private boolean isUseSoundWaveConfig(int mode) {
        int targetMode = MixedConfigMode.EZWiFiConfigWave;
        return targetMode == (targetMode & mode);
    }

    static {
        mInstance = new MixedConfigExecutor();
    }
}


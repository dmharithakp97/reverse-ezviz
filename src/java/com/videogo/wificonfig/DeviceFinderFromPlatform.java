/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.ezviz.sdk.configwifi.common.LogUtil
 *  com.ezviz.sdk.configwifi.finder.DeviceFindParam
 *  com.ezviz.sdk.configwifi.finder.DeviceFinderAbstract
 */
package com.videogo.wificonfig;

import com.ezviz.sdk.configwifi.common.LogUtil;
import com.ezviz.sdk.configwifi.finder.DeviceFindParam;
import com.ezviz.sdk.configwifi.finder.DeviceFinderAbstract;
import com.videogo.openapi.EzvizAPI;
import com.videogo.openapi.bean.EZProbeDeviceInfoResult;
import com.videogo.wificonfig.QueryTaskManager;

public class DeviceFinderFromPlatform
extends DeviceFinderAbstract {
    private static final String TAG = DeviceFinderFromPlatform.class.getSimpleName();
    private static DeviceFinderFromPlatform mInstance = new DeviceFinderFromPlatform();
    private long mQueryIntervalSecond = 5L;
    private long mQueryFindTimeoutSecond = 60L;
    private int mMaxRetryCnt = (int)(this.mQueryFindTimeoutSecond / this.mQueryIntervalSecond);
    private boolean mFound = false;
    private String apiUrl;

    public static DeviceFinderFromPlatform getInstance() {
        return mInstance;
    }

    public void start(final DeviceFindParam param) {
        LogUtil.i((String)TAG, (String)"start...");
        super.start(param);
        this.mFound = false;
        if (!QueryTaskManager.isInit()) {
            QueryTaskManager.init();
        }
        final Runnable queryTask = new Runnable(){

            @Override
            public void run() {
                EZProbeDeviceInfoResult result = EzvizAPI.getInstance().probeDeviceInfo(param.serial, null, DeviceFinderFromPlatform.this.apiUrl);
                if (DeviceFinderFromPlatform.this.checkDeviceStatusFromQueryResult(result)) {
                    LogUtil.i((String)TAG, (String)("device is online now..." + ((DeviceFinderFromPlatform)DeviceFinderFromPlatform.this).mParam.serial));
                    DeviceFinderFromPlatform.this.mFound = true;
                    DeviceFinderFromPlatform.this.isFinding = false;
                    if (DeviceFinderFromPlatform.this.mCallback != null) {
                        DeviceFinderFromPlatform.this.mCallback.onFind(param.serial);
                    }
                }
            }
        };
        QueryTaskManager.getInstance().submit(new Runnable(){

            @Override
            public void run() {
                int retryCnt = 0;
                while (DeviceFinderFromPlatform.this.isFinding && retryCnt < DeviceFinderFromPlatform.this.mMaxRetryCnt) {
                    LogUtil.d((String)TAG, (String)("try to find device from platform, " + ((DeviceFinderFromPlatform)DeviceFinderFromPlatform.this).mParam.serial));
                    QueryTaskManager.getInstance().submit(queryTask);
                    ++retryCnt;
                    try {
                        Thread.sleep(DeviceFinderFromPlatform.this.mQueryIntervalSecond * 1000L);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (!DeviceFinderFromPlatform.this.mFound) {
                    LogUtil.e((String)TAG, (String)"stopped querying, device is still offline");
                    if (DeviceFinderFromPlatform.this.mCallback != null) {
                        DeviceFinderFromPlatform.this.mCallback.onTimeout(param.serial);
                    }
                }
                DeviceFinderFromPlatform.this.stop();
            }
        });
        LogUtil.i((String)TAG, (String)"started");
    }

    private boolean checkDeviceStatusFromQueryResult(EZProbeDeviceInfoResult result) {
        if (result == null) {
            return false;
        }
        if (result.getBaseException() == null) {
            return true;
        }
        int errorCode = result.getBaseException().getErrorCode();
        if (errorCode == 120020) {
            return true;
        }
        LogUtil.i((String)TAG, (String)("device is offline..." + this.mParam.serial));
        return false;
    }

    public void stop() {
        super.stop();
        if (QueryTaskManager.isInit()) {
            QueryTaskManager.unInit();
        }
        this.mFound = false;
        this.isFinding = false;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    private DeviceFinderFromPlatform() {
    }
}


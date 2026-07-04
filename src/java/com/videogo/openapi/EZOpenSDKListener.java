/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.ezviz.sdk.configwifi.common.EZConfigWifiCallback
 */
package com.videogo.openapi;

import com.ezviz.sdk.configwifi.common.EZConfigWifiCallback;
import com.videogo.errorlayer.ErrorInfo;
import com.videogo.openapi.EZConstants;

public class EZOpenSDKListener {

    public static interface LogCallback {
        public void onLog(String var1, String var2);
    }

    public static interface OriginDataCallback {
        public void onData(int var1, byte[] var2, int var3);
    }

    public static enum EZStreamDownloadError {
        ERROR_EZSTREAM_DOWNLOAD_START,
        ERROR_EZSTREAM_DOWNLOAD_STOP,
        ERROR_EZSTREAM_DOWNLOAD_VERIFYCODE,
        ERROR_EZSTREAM_DOWNLOAD_SYSTRANSFORM,
        ERROR_EZSTREAM_DOWNLOAD_MAX_CONNECTIONS,
        ERROR_EZSTREAM_DOWNLOAD;

    }

    public static abstract class EZStreamDownloadCallbackEx
    implements EZStreamDownloadCallback {
        public void onErrorCode(int code) {
        }

        public void onDownloadingSize(long downloadSize) {
        }
    }

    public static interface EZStreamDownloadCallback {
        public void onSuccess(String var1);

        public void onError(EZStreamDownloadError var1);
    }

    public static interface EZStreamRecordCallback {
        public void onRecordSuccess();

        public void onRecordFail();
    }

    public static abstract class EZStartConfigWifiCallback
    extends EZConfigWifiCallback
    implements EZStartConfigWifiCallbackInterface {
    }

    public static interface EZStartConfigWifiCallbackInterface {
        public void onStartConfigWifiCallback(String var1, EZConstants.EZWifiConfigStatus var2);
    }

    public static interface EZLeaveMessageFlowCallback {
        public void onLeaveMessageFlowCallback(int var1, byte[] var2, int var3, String var4);
    }

    public static interface EZStandardFlowCallback {
        public void onStandardFlowCallback(int var1, byte[] var2, int var3);
    }

    public static interface EZPushServerListener {
        public void onStartPushServerSuccess(boolean var1, ErrorInfo var2);
    }
}


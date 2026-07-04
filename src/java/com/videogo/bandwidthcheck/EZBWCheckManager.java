/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.ez.bwcheck.BWCheckManager
 *  com.ez.bwcheck.BWCheckManager$BWCheckReqInfo2B
 *  com.ez.bwcheck.BWCheckManager$BWCheckResult
 *  com.ez.bwcheck.BWCheckManager$BWCheckType
 *  com.ez.bwcheck.BWCheckManager$OnMsgCallback
 */
package com.videogo.bandwidthcheck;

import com.ez.bwcheck.BWCheckManager;
import com.videogo.bandwidthcheck.EZBWCheckResult;
import com.videogo.errorlayer.ErrorInfo;
import com.videogo.errorlayer.ErrorLayer;
import com.videogo.exception.BaseException;
import com.videogo.openapi.EZConstants;
import com.videogo.openapi.PlayAPI;
import com.videogo.openapi.bean.EZBWCheckAddressInfo;
import com.videogo.util.LogUtil;
import java.util.Locale;

public class EZBWCheckManager {
    private static final String TAG = EZBWCheckManager.class.getSimpleName();
    public static final int expireTime = 604800;
    private static EZBWCheckManager mInstance = null;
    private BWCheckManager bwCheckManager = null;
    private boolean isExecuting = false;

    public static EZBWCheckManager getInstance() {
        if (mInstance == null) {
            mInstance = new EZBWCheckManager();
        }
        return mInstance;
    }

    private EZBWCheckManager() {
    }

    public void startBWCheck(final int bwCheckType, String deviceSerial, int cameraNo, int checkTime, String bwCheckToken, final EZBWCheckResultCallback callback) throws BaseException {
        LogUtil.d(TAG, "Enter startBWCheck, bwCheckType: " + bwCheckType);
        if (callback == null) {
            LogUtil.e(TAG, "EZBWCheckResultCallback is null, startBWCheck failed.");
            return;
        }
        if (this.isExecuting) {
            String errorMsg = "last bandwidth check is executing, startBWCheck failed.";
            LogUtil.e(TAG, errorMsg);
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400909);
            throw new BaseException(errorMsg, errorInfo);
        }
        this.isExecuting = true;
        if ((bwCheckType & EZConstants.EZBWCheckType.EZBWCheckClientUpward) > 0 && (bwCheckType & EZConstants.EZBWCheckType.EZBWCheckClientDownward) > 0) {
            EZBWCheckAddressInfo downwardAddressInfo;
            EZBWCheckAddressInfo upwardAddressInfo;
            try {
                upwardAddressInfo = PlayAPI.getInstance().getBWCheckAddress(EZConstants.EZBWCheckType.EZBWCheckClientUpward, deviceSerial, cameraNo, checkTime, 604800, bwCheckToken);
                downwardAddressInfo = PlayAPI.getInstance().getBWCheckAddress(EZConstants.EZBWCheckType.EZBWCheckClientDownward, deviceSerial, cameraNo, checkTime, 604800, bwCheckToken);
            }
            catch (BaseException e) {
                this.stopBWCheck();
                throw e;
            }
            if (upwardAddressInfo == null || downwardAddressInfo == null) {
                LogUtil.e(TAG, "upwardAddressInfo or downwardAddressInfo is null");
                return;
            }
            final EZBWCheckResult result = new EZBWCheckResult(deviceSerial, cameraNo, bwCheckType, upwardAddressInfo.getExIp(), upwardAddressInfo.getIsp());
            this.bwCheckManager = new BWCheckManager();
            this.startBWCheckForClient(EZConstants.EZBWCheckType.EZBWCheckClientUpward, upwardAddressInfo, result, callback, new BWCheckManager.OnMsgCallback(){

                public void onBWCheckResult(BWCheckManager.BWCheckResult upwardResult) {
                    super.onBWCheckResult(upwardResult);
                    LogUtil.d(TAG, String.format(Locale.US, "bandwidth check completed. bwCheckType: %d, currentCheckType: %d, result: %d", bwCheckType, upwardResult.checkType.getValue(), upwardResult.result));
                    if (!EZBWCheckManager.this.isExecuting) {
                        LogUtil.d(TAG, "isExecuting is false, return");
                        return;
                    }
                    result.uploadSpeed = upwardResult.uploadSpeed;
                    result.loss = upwardResult.loss;
                    result.latency = upwardResult.latency;
                    if (upwardResult.result != 0) {
                        EZBWCheckManager.this.bwCheckCallbackWithResult(result, upwardResult.result, callback);
                        return;
                    }
                    EZBWCheckManager.this.startBWCheckForClient(EZConstants.EZBWCheckType.EZBWCheckClientDownward, downwardAddressInfo, result, callback, new BWCheckManager.OnMsgCallback(){

                        public void onBWCheckResult(BWCheckManager.BWCheckResult downwardResult) {
                            super.onBWCheckResult(downwardResult);
                            String checkLog = "bandwidth check completed. bwCheckType: %d, currentCheckType: %d, result: %d";
                            LogUtil.d(TAG, String.format(Locale.US, checkLog, bwCheckType, downwardResult.checkType.getValue(), downwardResult.result));
                            if (!EZBWCheckManager.this.isExecuting) {
                                LogUtil.d(TAG, "isExecuting is false, return");
                                return;
                            }
                            result.downloadSpeed = downwardResult.downloadSpeed;
                            result.loss = Math.max(result.loss, downwardResult.loss);
                            result.latency = Math.max(result.latency, downwardResult.latency);
                            EZBWCheckManager.this.bwCheckCallbackWithResult(result, downwardResult.result, callback);
                        }
                    });
                }
            });
        } else {
            EZBWCheckAddressInfo addressInfo;
            try {
                addressInfo = PlayAPI.getInstance().getBWCheckAddress(bwCheckType, deviceSerial, cameraNo, checkTime, 604800, bwCheckToken);
            }
            catch (BaseException e) {
                this.stopBWCheck();
                throw e;
            }
            if (addressInfo == null) {
                LogUtil.e(TAG, "addressInfo is null");
                return;
            }
            final EZBWCheckResult result = new EZBWCheckResult(deviceSerial, cameraNo, bwCheckType, addressInfo.getExIp(), addressInfo.getIsp());
            this.isExecuting = true;
            this.bwCheckManager = new BWCheckManager();
            this.startBWCheckForClient(bwCheckType, addressInfo, result, callback, new BWCheckManager.OnMsgCallback(){

                public void onBWCheckResult(BWCheckManager.BWCheckResult checkResult) {
                    super.onBWCheckResult(checkResult);
                    String checkLog = "bandwidth check completed. bwCheckType: %d, currentCheckType: %d, result: %d";
                    LogUtil.d(TAG, String.format(Locale.US, checkLog, bwCheckType, checkResult.checkType.getValue(), checkResult.result));
                    if (!EZBWCheckManager.this.isExecuting) {
                        LogUtil.d(TAG, "isExecuting is false, return");
                        return;
                    }
                    result.downloadSpeed = checkResult.downloadSpeed;
                    result.uploadSpeed = checkResult.uploadSpeed;
                    result.loss = checkResult.loss;
                    result.latency = checkResult.latency;
                    EZBWCheckManager.this.bwCheckCallbackWithResult(result, checkResult.result, callback);
                }
            });
        }
    }

    public void stopBWCheck() {
        LogUtil.d(TAG, "Enter stopBWCheck");
        this.isExecuting = false;
        if (this.bwCheckManager != null) {
            this.bwCheckManager.release();
        }
    }

    private void startBWCheckForClient(int checkType, EZBWCheckAddressInfo bwCheckAddressInfo, EZBWCheckResult result, EZBWCheckResultCallback callback, BWCheckManager.OnMsgCallback msgCallback) {
        LogUtil.d(TAG, "Enter startBWCheckForClient");
        BWCheckManager.BWCheckReqInfo2B bwCheckReqInfo = new BWCheckManager.BWCheckReqInfo2B();
        bwCheckReqInfo.opid = bwCheckAddressInfo.getOpId();
        bwCheckReqInfo.url = bwCheckAddressInfo.getUrl();
        bwCheckReqInfo.pbkey = bwCheckAddressInfo.getPublicKey().getBytes();
        bwCheckReqInfo.keyver = bwCheckAddressInfo.getVersion();
        this.bwCheckManager.setOnMsgCallback(msgCallback);
        int ret = this.bwCheckManager.startCheck2B(bwCheckReqInfo, checkType == 1 ? BWCheckManager.BWCheckType.BWCheckType_TCP_UPLOAD : BWCheckManager.BWCheckType.BWCheckType_TCP_DOWNLOAD);
        LogUtil.d(TAG, "start bandwidth check, ret: " + ret + ", checkType: " + checkType);
        if (ret > 0) {
            LogUtil.e(TAG, "start bandwidth check failed");
            result.result = ret;
            this.bwCheckCallbackWithResult(result, ret, callback);
        }
    }

    private void bwCheckCallbackWithResult(EZBWCheckResult result, int ret, EZBWCheckResultCallback callback) {
        result.result = ret;
        if (callback != null) {
            callback.onBWCheckResult(result);
        }
        this.stopBWCheck();
    }

    public static abstract class EZBWCheckResultCallback {
        public void onBWCheckResult(EZBWCheckResult result) {
        }
    }
}


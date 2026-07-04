/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 *  com.sun.jna.Pointer
 */
package com.ez.p2ptrans;

import android.text.TextUtils;
import com.ez.jna.EZP2PDevFileJNA;
import com.ez.jna.EZStreamSDKJNA;
import com.ez.p2ptrans.EZDevFileCallback;
import com.ez.p2ptrans.EZP2PBaseFetcher;
import com.ez.stream.LogUtil;
import com.sun.jna.Pointer;

public class EZP2PDevFileFetcher {
    private EZDevFileCallback mCallback;
    private static final String TAG = "EZP2PDevFileFetcher";
    private long mClient;
    private String mStatistics;
    private EZP2PDevFileJNA.RespCallback mRespCallback = new EZP2PDevFileJNA.RespCallback(){

        @Override
        public void onResp(EZP2PDevFileJNA.EZP2PDevFileResp.ByReference rsp, Pointer fileData) {
            if (rsp.result == 0) {
                LogUtil.i(EZP2PDevFileFetcher.TAG, "onResp fileNum:" + rsp.fileNum + " fileLen:" + rsp.fileTotalSize);
            } else {
                LogUtil.i(EZP2PDevFileFetcher.TAG, "onResp failed result = " + rsp.result);
            }
            if (EZP2PDevFileFetcher.this.mCallback != null) {
                byte[] data = null;
                if (rsp.result == 0 && rsp.curSize > 0) {
                    data = new byte[rsp.curSize];
                    fileData.read(0L, data, 0, rsp.curSize);
                }
                EZP2PDevFileFetcher.this.mCallback.onRespData(rsp, data);
            }
        }
    };
    private EZP2PBaseFetcher.MsgCallback mMsgCallback = new EZP2PBaseFetcher.MsgCallback(){

        @Override
        public void onMsg(int msg, Pointer pointer) {
            LogUtil.i(EZP2PDevFileFetcher.TAG, "onMsg = " + msg);
            if (EZP2PDevFileFetcher.this.mCallback != null) {
                EZP2PDevFileFetcher.this.mCallback.onMsg(msg);
            }
        }
    };
    private EZP2PBaseFetcher.ErrorCallback mErrorCallback = new EZP2PBaseFetcher.ErrorCallback(){

        @Override
        public void onError(int errorCode, Pointer pointer) {
            LogUtil.e(EZP2PDevFileFetcher.TAG, "onError = " + errorCode);
            if (EZP2PDevFileFetcher.this.mCallback != null) {
                EZP2PDevFileFetcher.this.mCallback.onError(errorCode);
            }
        }
    };

    public EZP2PDevFileFetcher(EZP2PBaseFetcher.EZP2PTransParam transParam) {
        if (transParam == null || TextUtils.isEmpty((CharSequence)transParam.relayAddr_) || TextUtils.isEmpty((CharSequence)transParam.serial_) || TextUtils.isEmpty((CharSequence)transParam.token_)) {
            return;
        }
        this.mClient = EZStreamSDKJNA.sEZStreamSDKJNA.createP2PDevFileFetcher(transParam.toJNA());
    }

    public synchronized void setCallback(EZDevFileCallback callback) {
        this.mCallback = callback;
        if (this.mClient != 0L) {
            EZStreamSDKJNA.sEZStreamSDKJNA.setP2PDevFileCallback(this.mClient, this.mMsgCallback, this.mErrorCallback, this.mRespCallback);
        }
    }

    public synchronized void start() {
        if (this.mClient != 0L) {
            EZStreamSDKJNA.sEZStreamSDKJNA.startP2PDevFileTask(this.mClient);
        }
    }

    public synchronized void stop() {
        if (this.mClient != 0L) {
            EZStreamSDKJNA.sEZStreamSDKJNA.stopP2PDevFileTask(this.mClient);
            if (this.mStatistics == null) {
                this.mStatistics = EZStreamSDKJNA.sEZStreamSDKJNA.getP2PDevFileBuildStatistics(this.mClient);
            }
            EZStreamSDKJNA.sEZStreamSDKJNA.destroyP2PDevFileFetcher(this.mClient);
            this.mClient = 0L;
        }
    }

    public synchronized int requestP2PDevFile(EZP2PDevFileJNA.EZP2PDevFileReq req) {
        if (this.mClient != 0L) {
            return EZStreamSDKJNA.sEZStreamSDKJNA.sendP2PDevFileRequest(this.mClient, req);
        }
        return -1;
    }

    public synchronized String getBuildStatistics() {
        if (this.mClient != 0L) {
            this.mStatistics = EZStreamSDKJNA.sEZStreamSDKJNA.getP2PDevFileBuildStatistics(this.mClient);
        }
        return this.mStatistics;
    }
}


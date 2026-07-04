/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 *  com.sun.jna.Pointer
 */
package com.ez.p2ptrans;

import android.text.TextUtils;
import com.ez.jna.EZP2PRecordCoverJNA;
import com.ez.jna.EZStreamSDKJNA;
import com.ez.p2ptrans.EZP2PBaseFetcher;
import com.ez.p2ptrans.RecordCoverCallback;
import com.ez.stream.LogUtil;
import com.sun.jna.Pointer;

public class RecordCoverFetcher {
    private static String TAG = "RecordCoverFetcher";
    private RecordCoverCallback mCallback;
    private long mClient;
    private String mStatistics;
    private EZP2PRecordCoverJNA.RespCallback mRespCallback = new EZP2PRecordCoverJNA.RespCallback(){

        @Override
        public void onResp(EZP2PRecordCoverJNA.EZRecordResp rsp, Pointer picData, int picLength) {
            LogUtil.d(TAG, "onData len = " + picLength);
            if (RecordCoverFetcher.this.mCallback != null) {
                byte[] data = new byte[picLength];
                picData.read(0L, data, 0, picLength);
                RecordCoverFetcher.this.mCallback.onRespData(rsp, data);
            }
        }
    };
    private EZP2PBaseFetcher.MsgCallback mMsgCallback = new EZP2PBaseFetcher.MsgCallback(){

        @Override
        public void onMsg(int msg, Pointer pointer) {
            LogUtil.i(TAG, "onMsg = " + msg);
            if (RecordCoverFetcher.this.mCallback != null) {
                RecordCoverFetcher.this.mCallback.onMsg(msg);
            }
        }
    };
    private EZP2PBaseFetcher.ErrorCallback mErrorCallback = new EZP2PBaseFetcher.ErrorCallback(){

        @Override
        public void onError(int errorCode, Pointer pointer) {
            LogUtil.e(TAG, "onError = " + errorCode);
            if (RecordCoverFetcher.this.mCallback != null) {
                RecordCoverFetcher.this.mCallback.onError(errorCode);
            }
        }
    };

    public RecordCoverFetcher(EZP2PBaseFetcher.EZP2PTransParam transParam) {
        if (transParam == null || TextUtils.isEmpty((CharSequence)transParam.relayAddr_) || TextUtils.isEmpty((CharSequence)transParam.serial_) || TextUtils.isEmpty((CharSequence)transParam.token_)) {
            return;
        }
        this.mClient = EZStreamSDKJNA.sEZStreamSDKJNA.createRecordCoverFetcher(transParam.toJNA());
    }

    public synchronized void setCallback(RecordCoverCallback callback) {
        this.mCallback = callback;
        if (this.mClient != 0L) {
            EZStreamSDKJNA.sEZStreamSDKJNA.setRecordCoverCallback(this.mClient, this.mMsgCallback, this.mErrorCallback, this.mRespCallback);
        }
    }

    public synchronized void start() {
        if (this.mClient != 0L) {
            EZStreamSDKJNA.sEZStreamSDKJNA.startRecordCoverTask(this.mClient);
        }
    }

    public synchronized void stop() {
        if (this.mClient != 0L) {
            EZStreamSDKJNA.sEZStreamSDKJNA.stopRecordCoverTask(this.mClient);
            if (this.mStatistics == null) {
                this.mStatistics = EZStreamSDKJNA.sEZStreamSDKJNA.getRecordCoverBuildStatistics(this.mClient);
            }
            EZStreamSDKJNA.sEZStreamSDKJNA.destroyRecordCoverFetcher(this.mClient);
            this.mClient = 0L;
        }
    }

    public synchronized int requestRecordCover(EZP2PRecordCoverJNA.EZRecordReq req) {
        if (this.mClient != 0L && req != null) {
            return EZStreamSDKJNA.sEZStreamSDKJNA.sendRecordFetcherRequest(this.mClient, req);
        }
        return -1;
    }

    public synchronized String getBuildStatistics() {
        if (this.mClient != 0L) {
            this.mStatistics = EZStreamSDKJNA.sEZStreamSDKJNA.getRecordCoverBuildStatistics(this.mClient);
        }
        return this.mStatistics;
    }
}


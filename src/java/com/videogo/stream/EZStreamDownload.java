/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Handler
 *  android.os.Message
 *  com.ez.stream.DownloadCloudParam
 *  com.ez.stream.EZStreamCallback
 *  com.ez.stream.EZStreamClient
 *  com.ez.stream.EZStreamClientManager
 */
package com.videogo.stream;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.ez.stream.DownloadCloudParam;
import com.ez.stream.EZStreamCallback;
import com.ez.stream.EZStreamClient;
import com.ez.stream.EZStreamClientManager;
import com.videogo.openapi.EZOpenSDKListener;
import com.videogo.openapi.PlayAPI;
import com.videogo.openapi.bean.EZLeaveMessage;
import com.videogo.stream.EZStreamParamHelp;
import com.videogo.util.LogUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class EZStreamDownload
implements EZStreamCallback {
    private EZStreamClient downloadClient = EZStreamClientManager.create((Context)PlayAPI.mApplication.getApplicationContext()).createCASClient();
    private DownloadCloudParam downloadParam;
    private Handler handler = null;
    private EZOpenSDKListener.EZLeaveMessageFlowCallback leaveMessageFlowCallback;
    private static final String TAG = "EZStreamDownload";
    private ByteArrayOutputStream mByteArrayOutputStream;
    private EZLeaveMessage mEZLeaveMessage;
    public static final int MSG_LEAVEMSG_DOWNLOAD_FAIL = 301;
    public static final int MSG_LEAVEMSG_DOWNLOAD_SUCCESS = 302;

    public EZStreamDownload() {
        if (this.downloadClient == null) {
            LogUtil.d(TAG, "downloadClient create is null");
            return;
        }
        this.downloadClient.setCallback((EZStreamCallback)this);
    }

    public void release() {
        if (this.downloadClient != null) {
            EZStreamClientManager.create((Context)PlayAPI.mApplication.getApplicationContext()).destroyClient(this.downloadClient);
            this.downloadClient = null;
        }
        this.handler = null;
        this.leaveMessageFlowCallback = null;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void setLeaveMessageFlowCallback(EZOpenSDKListener.EZLeaveMessageFlowCallback leaveMessageFlowCallback) {
        this.leaveMessageFlowCallback = leaveMessageFlowCallback;
    }

    public void start(EZLeaveMessage msg) {
        if (msg == null) {
            return;
        }
        this.mEZLeaveMessage = msg;
        if (this.downloadClient == null) {
            LogUtil.d(TAG, "downloadClient is null");
            return;
        }
        this.mByteArrayOutputStream = new ByteArrayOutputStream();
        this.downloadParam = EZStreamParamHelp.getDownloadParam(this.mEZLeaveMessage);
        this.downloadClient.startDownloadFromCloud(this.downloadParam);
    }

    public void stop() {
        LogUtil.d(TAG, "downloadClient stop: ");
        if (this.downloadClient == null) {
            LogUtil.d(TAG, "downloadClient  is null");
            return;
        }
        this.downloadClient.stopDownloadFromCloud();
        if (this.mByteArrayOutputStream != null) {
            try {
                this.mByteArrayOutputStream.close();
            }
            catch (IOException e) {
                LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
            }
        }
        this.release();
    }

    public void onDataCallBack(int var1, byte[] var2, int var3) {
        LogUtil.d(TAG, "Enter onDataCallBack: ");
        if (var1 == 100) {
            LogUtil.d(TAG, "onDataCallBack: STREAM_TYPE_END");
            this.sendMessage(this.handler, 302);
            if (this.leaveMessageFlowCallback != null) {
                byte[] bytes = this.mByteArrayOutputStream.toByteArray();
                this.leaveMessageFlowCallback.onLeaveMessageFlowCallback(this.mEZLeaveMessage.getContentType(), bytes, bytes.length, this.mEZLeaveMessage.getMsgId());
            }
            this.stop();
            return;
        }
        if (this.mByteArrayOutputStream != null) {
            try {
                this.mByteArrayOutputStream.write(var2);
            }
            catch (IOException e) {
                LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
            }
        }
    }

    public void onMessageCallBack(int msg, int result) {
        LogUtil.d(TAG, "onMessageCallBack description:" + msg + ", result:" + result);
        if (msg == 1) {
            this.sendMessage(this.handler, 301);
            this.stop();
        }
    }

    public void onStatisticsCallBack(int var1, String var2) {
    }

    private void sendMessage(Handler handler, int what) {
        if (this.mEZLeaveMessage == null) {
            return;
        }
        Message msg = Message.obtain();
        msg.what = what;
        msg.obj = this.mEZLeaveMessage.getMsgId();
        handler.sendMessage(msg);
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.ez.stream;

import com.ez.stream.DownloadCloudParam;
import com.ez.stream.EZStreamCallback;
import com.ez.stream.IClient;
import com.ez.stream.IVoiceStream;
import com.ez.stream.InitParam;
import com.ez.stream.JsonUtils;
import com.ez.stream.NativeApi;
import com.ez.stream.UploadVoiceParam;

public class EZStreamClient
implements IClient,
IVoiceStream {
    long mNativeClient = 0L;

    EZStreamClient(long nativeClient) {
        this.mNativeClient = nativeClient;
    }

    @Override
    public int startPreview() {
        int ret = 2;
        if (this.mNativeClient != 0L) {
            ret = NativeApi.startPreview(this.mNativeClient);
        }
        return ret;
    }

    @Override
    public int stopPreview() {
        int ret = 2;
        if (this.mNativeClient != 0L) {
            ret = NativeApi.stopPreview(this.mNativeClient);
        }
        return ret;
    }

    @Override
    public int setCallback(EZStreamCallback callback) {
        int ret = 2;
        if (this.mNativeClient != 0L) {
            ret = NativeApi.setCallback(this.mNativeClient, callback);
        }
        return ret;
    }

    @Override
    public int startVoiceTalk() {
        int ret = -1;
        if (this.mNativeClient != 0L) {
            ret = NativeApi.startVoiceTalk(this.mNativeClient);
        }
        return ret;
    }

    @Override
    public String startVoiceTalkV2() {
        String ret = null;
        if (this.mNativeClient != 0L) {
            ret = NativeApi.startVoiceTalkV2(this.mNativeClient);
        }
        return ret;
    }

    @Override
    public int stopVoiceTalk() {
        int ret = 2;
        if (this.mNativeClient != 0L) {
            ret = NativeApi.stopVoiceTalk(this.mNativeClient);
        }
        return ret;
    }

    @Override
    public int inputVoiceTalkData(byte[] voiceData, int iVoiceDataLen, int iVoiceCmdType) {
        int ret = 2;
        if (this.mNativeClient != 0L) {
            ret = NativeApi.inputVoiceTalkData(this.mNativeClient, voiceData, iVoiceDataLen, iVoiceCmdType);
        }
        return ret;
    }

    @Override
    public int startPlayback(String startTime, String stopTime, String cloudFileId) {
        int ret = 2;
        if (this.mNativeClient != 0L) {
            ret = NativeApi.startPlayback(this.mNativeClient, startTime, stopTime, cloudFileId);
        }
        return ret;
    }

    @Override
    public int stopPlayback() {
        int ret = 2;
        if (this.mNativeClient != 0L) {
            ret = NativeApi.stopPlayback(this.mNativeClient);
        }
        return ret;
    }

    @Override
    public void release() {
        if (this.mNativeClient != 0L) {
            int ret = NativeApi.destroyClient(this.mNativeClient);
            this.mNativeClient = 0L;
        }
    }

    @Override
    public int switchMic(int micType) {
        int ret = 2;
        if (this.mNativeClient != 0L) {
            ret = NativeApi.switchMic(this.mNativeClient, micType);
        }
        return ret;
    }

    @Override
    public int getClientType() {
        int ret = -1;
        if (this.mNativeClient != 0L) {
            ret = NativeApi.getClientType(this.mNativeClient);
        }
        return ret;
    }

    @Override
    public int updateParam(InitParam initParam) {
        int ret = 2;
        if (this.mNativeClient != 0L && initParam != null) {
            ret = NativeApi.updateParam(this.mNativeClient, initParam);
        }
        return ret;
    }

    public int startUpload2Cloud(UploadVoiceParam param) {
        int ret = 2;
        if (this.mNativeClient != 0L && param != null) {
            ret = NativeApi.startUpload2Cloud(this.mNativeClient, param);
        }
        return ret;
    }

    public int inputData2Cloud(byte[] data, int iDataLen) {
        int ret = 2;
        if (this.mNativeClient != 0L && data != null) {
            ret = NativeApi.inputData2Cloud(this.mNativeClient, data, iDataLen);
        }
        return ret;
    }

    public int stopUpload2Cloud() {
        int ret = 2;
        if (this.mNativeClient != 0L) {
            ret = NativeApi.stopUpload2Cloud(this.mNativeClient);
        }
        return ret;
    }

    @Override
    public int startDownloadFromCloud(DownloadCloudParam param) {
        int ret = 2;
        if (this.mNativeClient != 0L && param != null) {
            ret = NativeApi.startDownloadFromCloud(this.mNativeClient, param);
        }
        return ret;
    }

    @Override
    public int stopDownloadFromCloud() {
        int ret = 2;
        if (this.mNativeClient != 0L) {
            ret = NativeApi.stopDownloadFromCloud(this.mNativeClient);
        }
        return ret;
    }

    @Override
    public int setPlaybackRate(int rate) {
        int ret = 2;
        if (this.mNativeClient != 0L) {
            ret = NativeApi.setPlaybackRate(this.mNativeClient, rate);
        }
        return ret;
    }

    @Override
    public IClient.DevInfo getDevInfo(boolean isForce) {
        IClient.DevInfo dev = null;
        if (this.mNativeClient == 0L) {
            return null;
        }
        String devInfoString = NativeApi.getDevInfo(this.mNativeClient, isForce);
        if (devInfoString != null) {
            dev = JsonUtils.fromJson(devInfoString, IClient.DevInfo.class);
        }
        return dev;
    }

    @Override
    public void setPlayPort(int playPort) {
        if (this.mNativeClient != 0L) {
            NativeApi.setPlayPort(this.mNativeClient, playPort);
        }
    }

    @Override
    public void setDataCallback2Java(boolean enble) {
        if (this.mNativeClient != 0L) {
            NativeApi.setDataCallback2Java(this.mNativeClient, enble);
        }
    }

    @Override
    public void setPlaybackConvert(int videoBitrate, int resolution, int videoFrameRate) {
        if (this.mNativeClient != 0L) {
            NativeApi.setPlaybackConvert(this.mNativeClient, videoBitrate, resolution, videoFrameRate);
        }
    }

    @Override
    public int cloudPlaybackControl(int op, String szBeginTime, int iPlaySpeed) {
        int ret = 3;
        if (this.mNativeClient != 0L) {
            ret = NativeApi.cloudPlaybackControl(this.mNativeClient, op, szBeginTime, iPlaySpeed);
        }
        return ret;
    }

    @Override
    public int isQosTalk() {
        return 0;
    }
}


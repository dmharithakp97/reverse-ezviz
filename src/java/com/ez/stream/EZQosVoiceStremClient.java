/*
 * Decompiled with CFR 0.152.
 */
package com.ez.stream;

import com.ez.jna.EZStreamSDKJNA;
import com.ez.stream.EZStreamCallback;
import com.ez.stream.EZStreamClientManager;
import com.ez.stream.EZTaskMgr;
import com.ez.stream.IVoiceStream;
import com.ez.stream.LogUtil;

public class EZQosVoiceStremClient
implements IVoiceStream {
    public static final String TAG = "EZ_PLAYER_SDK";
    EZStreamSDKJNA.MsgCallback mMsgCallback = new EZStreamSDKJNA.MsgCallback(){

        @Override
        public int onMsg(int msg, long lValue) {
            LogUtil.d(EZQosVoiceStremClient.TAG, "onMsg msg = " + msg + ",lValue = " + lValue);
            if (EZQosVoiceStremClient.this.mCallBack != null) {
                EZQosVoiceStremClient.this.mCallBack.onMessageCallBack(msg, (int)lValue);
            }
            return 0;
        }
    };
    EZStreamSDKJNA.NetStatusCallback mNetStatusCallback = new EZStreamSDKJNA.NetStatusCallback(){

        @Override
        public int onNetStatus(int nRttUs, int nRealRttUs, int nBitRate, int cLossFraction, int cLossFraction2, int nAudioTonQ, int nAudioRTQ, int nAudioFluQ) {
            LogUtil.d(EZQosVoiceStremClient.TAG, "nRttUs=" + nRttUs + ",nRealRttUs=" + nRealRttUs + ",nBitRate=" + nBitRate + ",cLossFraction=" + cLossFraction + ",cLossFraction2=" + cLossFraction2 + ",nAudioTonQ=" + nAudioTonQ + ",nAudioRTQ=" + nAudioRTQ + ",nAudioFluQ=" + nAudioFluQ);
            String log = "rtt = " + nRealRttUs / 1000 + "ms,bitrate = " + nBitRate + ",plp = " + (double)cLossFraction / 2.56 + "%";
            if (EZQosVoiceStremClient.this.mCallBack != null) {
                EZQosVoiceStremClient.this.mCallBack.onStatisticsCallBack(101, log);
            }
            return 0;
        }
    };
    private long mTalk = 0L;
    private EZStreamCallback mCallBack;
    EZTaskMgr mEZTaskMgr = null;

    public EZQosVoiceStremClient(EZStreamClientManager manager, EZStreamSDKJNA.EZ_TALK_PARAM.ByReference param) {
        this.mEZTaskMgr = manager.getTaskMgr();
        this.mTalk = EZStreamSDKJNA.sEZStreamSDKJNA.ez_talk_create(param, this.mMsgCallback, this.mNetStatusCallback);
    }

    @Override
    public int startVoiceTalk() {
        int ret = -1;
        if (this.mTalk != 0L) {
            ret = EZStreamSDKJNA.sEZStreamSDKJNA.ez_talk_start(this.mTalk);
        }
        LogUtil.d(TAG, "startVoiceTalk ret = " + ret);
        return ret;
    }

    @Override
    public String startVoiceTalkV2() {
        return null;
    }

    @Override
    public int stopVoiceTalk() {
        if (this.mTalk != 0L) {
            EZStreamSDKJNA.sEZStreamSDKJNA.ez_talk_stop(this.mTalk);
            String statistics = EZQosVoiceStremClient.getStatistics(this.mTalk);
            LogUtil.d(TAG, statistics);
            if (this.mCallBack != null) {
                this.mCallBack.onStatisticsCallBack(12, statistics);
            }
        }
        return 0;
    }

    @Override
    public synchronized int inputVoiceTalkData(byte[] voiceData, int iVoiceDataLen, int iVoiceCmdType) {
        EZQosVoiceStremClient.inputData(this.mTalk, voiceData, 0, iVoiceDataLen);
        return 0;
    }

    @Override
    public synchronized int setCallback(EZStreamCallback callback) {
        this.mCallBack = callback;
        if (this.mTalk != 0L) {
            EZQosVoiceStremClient.setDataCallback(this.mTalk, callback);
        }
        return 0;
    }

    @Override
    public int isQosTalk() {
        return 1;
    }

    @Override
    public synchronized void release() {
        if (this.mTalk != 0L) {
            EZQosVoiceStremClient.setDataCallback(this.mTalk, null);
            EZStreamSDKJNA.sEZStreamSDKJNA.ez_talk_destroy(this.mTalk);
            this.mTalk = 0L;
        }
    }

    @Override
    public int switchMic(int micType) {
        return 3;
    }

    public void setRspTimeout(int timeOut) {
        if (this.mTalk != 0L) {
            EZStreamSDKJNA.sEZStreamSDKJNA.ez_talk_set_timeOut(this.mTalk, timeOut);
        }
    }

    public void setClientSession(String session) {
        if (this.mTalk != 0L) {
            EZStreamSDKJNA.sEZStreamSDKJNA.ez_talk_set_clientSession(this.mTalk, session);
        }
    }

    public static native void inputData(long var0, byte[] var2, int var3, int var4);

    public static native void setDataCallback(long var0, EZStreamCallback var2);

    public static native String getStatistics(long var0);
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.media.AudioManager
 *  android.os.Handler
 *  android.text.TextUtils
 */
package com.ez.player;

import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.text.TextUtils;
import com.ez.jna.EZStreamSDKJNA;
import com.ez.player.EZAudioManager;
import com.ez.statistics.BaseVoiceTalkStatistics;
import com.ez.statistics.DirectVoiceTalkStatistics;
import com.ez.statistics.PingCheckDef;
import com.ez.statistics.PingImp;
import com.ez.statistics.QosTalkStatistics;
import com.ez.statistics.RootVoiceTalkStatistics;
import com.ez.statistics.ScoState;
import com.ez.statistics.TTSVoiceTalkStatistics;
import com.ez.stream.EZQosVoiceStremClient;
import com.ez.stream.EZStreamCallback;
import com.ez.stream.EZStreamClientManager;
import com.ez.stream.EZTaskMgr;
import com.ez.stream.IVoiceStream;
import com.ez.stream.InitParam;
import com.ez.stream.JsonUtils;
import com.ez.stream.LogUtil;
import com.ez.stream.QoSTalkParam;
import com.ez.stream.VoiceTalk;
import java.io.File;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class EZVoiceTalk
implements EZStreamCallback {
    private static final String TAG = "EZVoiceTalk";
    IVoiceStream mStreamClient;
    EZStreamClientManager mManager = null;
    EZTaskMgr mEZTaskMgr = null;
    VoiceTalk mVoiceTalk;
    InitParam mInitParam = null;
    QoSTalkParam mQosTalkParam = null;
    Handler mHandler;
    ReentrantLock mListenerLock = new ReentrantLock();
    BaseVoiceTalkStatistics mCurrentStreamStatistics = null;
    RootVoiceTalkStatistics mRootStatistics = new RootVoiceTalkStatistics();
    private volatile boolean mIsStoped = false;
    private boolean pingFinish = true;
    private final Object pingLock = new Object();
    private OnLoudnessListener mOnLoudnessListener;
    private float mLoudnessInterVal;
    private boolean mPlayAgc;
    private boolean mCaptureAgc = true;
    private int ezAudioEncodeType = -1;
    private Context mContext;
    OnVoiceTalkListener mListener;
    volatile boolean mWriteFile = false;
    volatile String mWriteFileDir;
    private boolean enableEZ3AAlgorithm = false;
    private String vqeModel;

    public RootVoiceTalkStatistics getStatistics() {
        return this.mRootStatistics;
    }

    public void setOnLoudnessListener(OnLoudnessListener onLoudnessListener, float interVal) {
        this.mOnLoudnessListener = onLoudnessListener;
        this.mLoudnessInterVal = interVal;
    }

    public void setOnSoundPlayedListener(OnSoundPlayedListener onSoundPlayedListener, String model8, String model16) {
        if (this.mVoiceTalk != null) {
            this.mVoiceTalk.setOnSoundPlayedListener(onSoundPlayedListener, model8, model16);
        }
    }

    public void setOnVoiceTalkListener(OnVoiceTalkListener listener) {
        try {
            this.mListenerLock.lock();
            this.mListener = listener;
        }
        finally {
            this.mListenerLock.unlock();
        }
    }

    @Override
    public void onDataCallBack(int datatype, byte[] data, int len) {
        int ret;
        if (datatype == 3 && (ret = this.processRemoteVoiceData(data, len)) == 2022) {
            this.onError(ret);
        }
    }

    void onError(final int detailErrorCode) {
        if (this.mListener == null) {
            return;
        }
        this.mHandler.post(new Runnable(){

            @Override
            public void run() {
                try {
                    EZVoiceTalk.this.mListenerLock.lock();
                    if (EZVoiceTalk.this.mListener != null) {
                        EZVoiceTalk.this.mListener.onError(EZVoiceTalk.this, detailErrorCode);
                    }
                }
                finally {
                    EZVoiceTalk.this.mListenerLock.unlock();
                }
            }
        });
    }

    void onNeedToken() {
        if (this.mListener == null) {
            return;
        }
        this.mHandler.post(new Runnable(){

            @Override
            public void run() {
                try {
                    EZVoiceTalk.this.mListenerLock.lock();
                    if (EZVoiceTalk.this.mListener != null) {
                        EZVoiceTalk.this.mListener.onNeedToken(EZVoiceTalk.this);
                    }
                }
                finally {
                    EZVoiceTalk.this.mListenerLock.unlock();
                }
            }
        });
    }

    @Override
    public void onMessageCallBack(int msg, int result) {
        LogUtil.d(TAG, "ezmessage:msg = " + msg + ",result = " + result);
        if (this.mIsStoped) {
            return;
        }
        switch (msg) {
            case 1: {
                if (result == 0) break;
                this.onError(result);
                if (this.mCurrentStreamStatistics == null) break;
                this.mCurrentStreamStatistics.decd = result;
                break;
            }
            case 3: {
                this.onNeedToken();
            }
        }
    }

    @Override
    public void onStatisticsCallBack(int statisticsType, final String statistics) {
        LogUtil.d(TAG, "onStatisticsCallBack = " + statistics);
        switch (statisticsType) {
            case 6: {
                TTSVoiceTalkStatistics ttsStatistics = JsonUtils.fromJson(statistics, TTSVoiceTalkStatistics.class);
                if (ttsStatistics == null) break;
                ttsStatistics.uuid = this.mRootStatistics.uuid;
                ttsStatistics.seq = this.mRootStatistics.mSeq++;
                this.mCurrentStreamStatistics = ttsStatistics;
                if (ttsStatistics.r == 0) {
                    this.mRootStatistics.talkType = 2;
                }
                this.mRootStatistics.r = ttsStatistics.r;
                this.mRootStatistics.lst = ttsStatistics.lst;
                this.mRootStatistics.lslid = ttsStatistics.lslid;
                this.mRootStatistics.lsctype = ttsStatistics.lsctype;
                this.mRootStatistics.mTTSVoiceTalkStatisticsList.add(ttsStatistics);
                break;
            }
            case 7: {
                DirectVoiceTalkStatistics directStatistics = JsonUtils.fromJson(statistics, DirectVoiceTalkStatistics.class);
                if (directStatistics == null) break;
                directStatistics.uuid = this.mRootStatistics.uuid;
                directStatistics.seq = this.mRootStatistics.mSeq++;
                this.mCurrentStreamStatistics = directStatistics;
                if (directStatistics.r == 0) {
                    this.mRootStatistics.talkType = directStatistics.type;
                }
                this.mRootStatistics.r = directStatistics.r;
                this.mRootStatistics.mDirectVoiceTalkStatisticsList.add(directStatistics);
                break;
            }
            case 12: {
                QosTalkStatistics qosStatistics = JsonUtils.fromJson(statistics, QosTalkStatistics.class);
                if (qosStatistics == null) break;
                qosStatistics.uuid = this.mRootStatistics.uuid;
                qosStatistics.seq = this.mRootStatistics.mSeq++;
                qosStatistics.ver = this.mInitParam.iQosTalkVersion;
                this.mCurrentStreamStatistics = qosStatistics;
                if (qosStatistics.r == 0) {
                    this.mRootStatistics.talkType = 4;
                }
                this.mRootStatistics.r = qosStatistics.r;
                this.mRootStatistics.mQosTalkStatisticsList.add(qosStatistics);
                break;
            }
            case 101: {
                if (this.mListener == null) {
                    return;
                }
                this.mHandler.post(new Runnable(){

                    @Override
                    public void run() {
                        try {
                            EZVoiceTalk.this.mListenerLock.lock();
                            if (EZVoiceTalk.this.mListener != null) {
                                EZVoiceTalk.this.mListener.onNetStatus(statistics);
                            }
                        }
                        finally {
                            EZVoiceTalk.this.mListenerLock.unlock();
                        }
                    }
                });
            }
        }
    }

    public EZVoiceTalk(EZStreamClientManager manager, InitParam param) {
        this(manager, param, null);
    }

    public EZVoiceTalk(EZStreamClientManager manager, InitParam param, QoSTalkParam qoSTalkParam) {
        boolean mCodec = true;
        this.mManager = manager;
        this.mEZTaskMgr = manager.getTaskMgr();
        if (this.mEZTaskMgr == null) {
            throw new RuntimeException();
        }
        this.mHandler = new Handler(this.mEZTaskMgr.getLooper());
        this.mInitParam = param;
        this.mRootStatistics.sn = this.mInitParam.szDevSerial;
        LogUtil.d(TAG, "EZVoiceTalk param = " + this.mInitParam);
        this.mQosTalkParam = qoSTalkParam;
    }

    private void initStreamClient(boolean isFullDuplex) {
        if (this.supportQosTalk(isFullDuplex)) {
            byte[] serverIP;
            EZStreamSDKJNA.EZ_TALK_PARAM.ByReference p = new EZStreamSDKJNA.EZ_TALK_PARAM.ByReference();
            p.iClientType = 3;
            p.iChannelNumber = this.mInitParam.iChannelNumber;
            byte[] sn = this.mInitParam.szDevSerial.getBytes();
            System.arraycopy(sn, 0, p.szDevSerial, 0, sn.length);
            byte[] uuid = !TextUtils.isEmpty((CharSequence)this.mInitParam.szLid) ? this.mInitParam.szLid.getBytes() : this.mInitParam.szHardwareCode.getBytes();
            System.arraycopy(uuid, 0, p.szHardwareCode, 0, uuid.length);
            if (this.mQosTalkParam == null) {
                p.iServerPort = this.mInitParam.iQosTakPort;
                serverIP = this.mInitParam.szQosTaklIP.getBytes();
                System.arraycopy(serverIP, 0, p.szServerIP, 0, serverIP.length);
            } else {
                p.iServerPort = this.mQosTalkParam.serverPort;
                serverIP = this.mQosTalkParam.serverIp.getBytes();
                System.arraycopy(serverIP, 0, p.szServerIP, 0, serverIP.length);
                p.m_iPublicKeyVersion = this.mQosTalkParam.publicKeyVersion;
                if (!TextUtils.isEmpty((CharSequence)this.mQosTalkParam.publicKey)) {
                    byte[] keyByte = this.mQosTalkParam.publicKey.getBytes();
                    System.arraycopy(keyByte, 0, p.m_szPublicKey, 0, keyByte.length);
                }
            }
            if (!TextUtils.isEmpty((CharSequence)this.mInitParam.szStreamToken)) {
                byte[] tokenBytes = this.mInitParam.szStreamToken.getBytes();
                System.arraycopy(tokenBytes, 0, p.szStreamToken, 0, tokenBytes.length);
            }
            if (!TextUtils.isEmpty((CharSequence)this.mInitParam.szCallingId)) {
                p.iTalkType = this.mInitParam.iTalkType;
                byte[] callingId = this.mInitParam.szCallingId.getBytes();
                System.arraycopy(callingId, 0, p.szCallingId, 0, callingId.length);
            }
            p.write();
            EZQosVoiceStremClient client = new EZQosVoiceStremClient(this.mManager, p);
            client.setClientSession(this.mInitParam.szClientSession);
            this.mStreamClient = client;
        } else {
            this.mStreamClient = this.mManager.createClient(this.mInitParam);
        }
        this.mStreamClient.setCallback(this);
    }

    public void release() {
        this.stopVoiceTalk();
        if (this.mStreamClient != null) {
            this.mStreamClient.release();
            this.mStreamClient = null;
        }
    }

    private void pingTask() {
        this.pingFinish = false;
        PingCheckDef.PingCheckReq req = new PingCheckDef.PingCheckReq();
        req.uuid = this.mRootStatistics.uuid;
        req.eventTime = System.currentTimeMillis();
        req.vtmHost = this.mInitParam.szTtsIP;
        req.type = 2;
        req.errCode = 0;
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 1L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(10));
        PingCheckDef.PingCheckRsp[] pingCheckRsp = new PingCheckDef.PingCheckRsp[1];
        executor.execute(() -> {
            PingCheckDef.PingCheckRsp[] pingCheckRspArray = pingCheckRsp;
            synchronized (pingCheckRsp) {
                this.mRootStatistics.mPingStatistics = PingImp.getInstance().startPingTask(req);
                this.pingFinish = true;
                // ** MonitorExit[var3_3] (shouldn't be in output)
                return;
            }
        });
        executor.shutdown();
    }

    public void enableRouter(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public void setEZAudioEncodeType(int ezAudioEncodeType) {
        this.ezAudioEncodeType = ezAudioEncodeType;
    }

    public int getEZAudioEncodeType() {
        return this.mVoiceTalk != null ? this.mVoiceTalk.getEZAudioEncodeType() : -1;
    }

    public int startVoiceTalk(AudioManager manager, boolean isFullDuplex, EZAudioParam param) {
        return this.startVoiceTalk(manager, isFullDuplex, param, true);
    }

    public int startVoiceTalk(AudioManager manager, boolean isFullDuplex, EZAudioParam param, boolean forceSpeakerOn) {
        int ret = 1;
        this.mIsStoped = false;
        if (manager == null || this.mInitParam == null) {
            this.onError(2);
            return ret;
        }
        this.mRootStatistics.stream_bt = System.currentTimeMillis();
        this.initStreamClient(isFullDuplex);
        if (this.supportQosTalk(isFullDuplex)) {
            this.mVoiceTalk = new VoiceTalk(manager, this.mStreamClient, isFullDuplex, param, forceSpeakerOn);
            this.initVoiceTalkCommon();
            ret = this.mVoiceTalk.start();
            this.mRootStatistics.talkType = 4;
        }
        boolean isQos2TTS = false;
        if (ret != 0 && ret != 60301) {
            isQos2TTS = this.supportQosTalk(isFullDuplex);
            this.mRootStatistics.talkType = 2;
            if (this.mVoiceTalk != null) {
                this.mVoiceTalk.stop();
                this.mVoiceTalk = null;
                this.mStreamClient.release();
                this.mStreamClient = this.mManager.createClient(this.mInitParam);
                this.mStreamClient.setCallback(this);
            }
            if (!isFullDuplex || !this.mInitParam.support_new_talk) {
                this.mVoiceTalk = new VoiceTalk(manager, this.mStreamClient, isFullDuplex, param, forceSpeakerOn);
                this.initVoiceTalkCommon();
                this.mRootStatistics.semi = isFullDuplex ? 2 : 1;
                ret = this.mVoiceTalk.start();
            } else {
                this.mVoiceTalk = new VoiceTalk(manager, this.mStreamClient, true, param, forceSpeakerOn);
                this.initVoiceTalkCommon();
                ret = this.mVoiceTalk.startV2();
            }
        }
        this.mRootStatistics.r = ret;
        if (ret != 0) {
            this.mRootStatistics.talkType = -1;
        }
        if (this.mVoiceTalk != null) {
            this.mRootStatistics.stream_et = this.mVoiceTalk.stream_et;
            this.mRootStatistics.eg_mode = this.mVoiceTalk.eg_mode;
            if (this.mRootStatistics.eg_mode == 1 && isQos2TTS) {
                this.mRootStatistics.eg_mode = 2;
            }
            this.mRootStatistics.engine_bt = this.mVoiceTalk.engine_bt;
            this.mRootStatistics.engine_et = this.mVoiceTalk.engine_et;
        }
        return ret;
    }

    private void initVoiceTalkCommon() {
        if (!TextUtils.isEmpty((CharSequence)this.mWriteFileDir)) {
            this.mVoiceTalk.setWriteFileEX(this.mWriteFile, this.mWriteFileDir);
        }
        if (this.mOnLoudnessListener != null && this.mLoudnessInterVal > 0.0f) {
            this.mVoiceTalk.setOnLoudnessListener(this.mOnLoudnessListener, this.mLoudnessInterVal);
        }
        this.mVoiceTalk.switchAgc(this.mPlayAgc, 1);
        this.mVoiceTalk.switchAgc(this.mCaptureAgc, 2);
        this.mVoiceTalk.setEZ3AAlgorithmModel(this.vqeModel);
        this.mVoiceTalk.enableEZ3AAlgorithm(this.enableEZ3AAlgorithm);
        if (this.mContext != null) {
            this.mVoiceTalk.enableRouter(this.mContext);
        }
        this.mVoiceTalk.setEZAudioEncodeType(this.ezAudioEncodeType);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public int stopVoiceTalk() {
        int ret = 2;
        this.mIsStoped = true;
        if (this.mVoiceTalk != null) {
            ScoState scoState = EZAudioManager.getInstance().getSCOConnectStatistics();
            if (scoState != null) {
                this.mRootStatistics.router = scoState.router;
                this.mRootStatistics.sco_state = scoState.sco_state;
                this.mRootStatistics.sco_t = scoState.sco_t;
            }
            this.mVoiceTalk.stop();
            this.mRootStatistics.flowin = this.mVoiceTalk.getFlowIn();
            this.mRootStatistics.flowout = this.mVoiceTalk.getFlowOut();
            this.mVoiceTalk = null;
            ret = 0;
        }
        Object object = this.pingLock;
        synchronized (object) {
            if (!this.pingFinish) {
                try {
                    this.pingLock.wait(6000L);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }

    private int processRemoteVoiceData(byte[] voiceData, int iVoiceDataLen) {
        if (this.mVoiceTalk != null) {
            return this.mVoiceTalk.processRemoteVoiceData(voiceData, iVoiceDataLen);
        }
        return 3;
    }

    public void updateVoiceTalkButtonPressStatus(boolean isPressed) {
        if (this.mVoiceTalk != null) {
            this.mVoiceTalk.updateVoiceTalkButtonPressStatus(isPressed);
        }
    }

    public int changeTalkPatten(boolean isFullDuplex) {
        if (this.mVoiceTalk != null) {
            return this.mVoiceTalk.changeTalkPatten(isFullDuplex);
        }
        return 3;
    }

    public void setVoiceVolume(int nVolume) {
        int ret = 0;
        if (this.mVoiceTalk != null) {
            ret = this.mVoiceTalk.setVoiceVolume(nVolume);
        }
    }

    public void openVoiceTalkMicrophone() {
        if (this.mVoiceTalk != null) {
            this.mVoiceTalk.openVoiceTalkMicrophone();
        }
    }

    public void closeVoiceTalkMicrophone() {
        if (this.mVoiceTalk != null) {
            this.mVoiceTalk.closeVoiceTalkMicrophone();
        }
    }

    public void openVoiceTalkSpeaker() {
        if (this.mVoiceTalk != null) {
            this.mVoiceTalk.openVoiceTalkSpeaker();
        }
    }

    public void closeVoiceTalkSpeaker() {
        if (this.mVoiceTalk != null) {
            this.mVoiceTalk.closeVoiceTalkSpeaker();
        }
    }

    @Deprecated
    public void setVoiceTallkWriteFile(boolean bIsWrite) {
    }

    public void setVoiceTalkWriteFileEX(boolean bIsWrite, String dir) {
        this.mWriteFile = bIsWrite;
        this.mWriteFileDir = dir != null && !dir.endsWith("/") ? dir + "/" : dir;
    }

    public void setSpeakerphoneOn(boolean on) {
        if (this.mVoiceTalk != null) {
            this.mVoiceTalk.setSpeakerphoneOn(on);
        }
    }

    public boolean isSpeakerphoneOn() {
        boolean isSpeakerphoneOn = true;
        if (this.mVoiceTalk != null) {
            isSpeakerphoneOn = this.mVoiceTalk.isSpeakerphoneOn();
        }
        return isSpeakerphoneOn;
    }

    @Deprecated
    public void setPlayAgcOn(boolean on) {
    }

    public int switchAgc(boolean open, int mode) {
        if (mode == 1) {
            this.mPlayAgc = open;
        } else if (mode == 2) {
            this.mCaptureAgc = open;
        } else {
            return -1;
        }
        if (this.mVoiceTalk != null) {
            return this.mVoiceTalk.switchAgc(open, mode);
        }
        return 0;
    }

    public void setSpeakerMode(int speakerMode) {
        if (this.mVoiceTalk != null) {
            this.mVoiceTalk.setSpeakerMode(speakerMode);
        }
    }

    public int getSpeakerMode() {
        return this.mVoiceTalk != null ? this.mVoiceTalk.getSpeakerMode() : 1;
    }

    public void setQosRspTimeout(int timeout) {
        if (this.mStreamClient instanceof EZQosVoiceStremClient) {
            LogUtil.d(TAG, "setQosRspTimeout = " + timeout);
            ((EZQosVoiceStremClient)this.mStreamClient).setRspTimeout(timeout);
        }
    }

    public void setEZ3AAlgorithmModel(String model) {
        File file = new File(model);
        if (file.exists() && file.isFile()) {
            this.vqeModel = model;
        }
    }

    public void enableEZ3AAlgorithm(boolean enable) {
        this.enableEZ3AAlgorithm = enable;
    }

    private boolean supportQosTalk(boolean isFullDuplex) {
        return isFullDuplex && (this.mInitParam.iQosTalkVersion > 0 || this.mInitParam.iQosTalkVersion == -101) && this.mInitParam.iQosTakPort > 0 && this.mInitParam.szQosTaklIP != null;
    }

    public int openPitchChanger(boolean bPitchChangeEnable, int nPitchChangeLevel) {
        return this.mVoiceTalk == null ? 1 : this.mVoiceTalk.openPitchChanger(bPitchChangeEnable, nPitchChangeLevel);
    }

    public int getSessionId() {
        return this.mVoiceTalk == null ? -1 : this.mVoiceTalk.getSessionId();
    }

    public int getSoundMode() {
        return 0;
    }

    public int getTalkType() {
        return this.mRootStatistics.talkType;
    }

    public int switchMic(int micType) {
        return this.mStreamClient.switchMic(micType);
    }

    public static interface OnLoudnessListener {
        public void onReportLoudness(float var1);
    }

    public static interface OnSoundPlayedListener {
        public void onSoundLowTip(float var1);
    }

    public static interface OnVoiceTalkListener {
        public boolean onError(EZVoiceTalk var1, int var2);

        public void onNeedToken(EZVoiceTalk var1);

        public void onNetStatus(String var1);
    }

    public static class EZAudioParam {
        public float aec = 0.0f;
        public int volume = 0;
        public boolean isFastDenoise = false;
        public boolean systemAEC = true;
        public boolean useTip = false;
        public String tipPath;
        public boolean captureOnly = false;

        public EZAudioParam(float aec, int volume, boolean isFastDenoise) {
            this.aec = aec;
            this.volume = volume;
            this.isFastDenoise = isFastDenoise;
        }

        public EZAudioParam() {
        }
    }
}


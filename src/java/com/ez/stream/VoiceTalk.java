/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.media.AudioManager
 *  android.media.MediaExtractor
 *  android.os.Handler
 *  android.os.Looper
 *  android.text.TextUtils
 *  android.util.Log
 *  com.mediaplayer.audio.AudioBaseCallBack
 *  com.mediaplayer.audio.AudioCodecParam
 *  com.mediaplayer.audio.AudioEngine
 *  com.mediaplayer.audio.AudioEngineCallBack$EnergyDBCallBack
 *  com.mediaplayer.audio.AudioEngineCallBack$ErrorInfoCallBack
 *  com.mediaplayer.audio.AudioEngineCallBack$RecordDataCallBack
 *  com.mediaplayer.audio.AudioEngineCallBack$VoiceAutoDetectCallBack
 */
package com.ez.stream;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaExtractor;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import com.ez.player.EZAudioManager;
import com.ez.player.EZVoiceTalk;
import com.ez.stream.IVoiceStream;
import com.ez.stream.JsonUtils;
import com.ez.stream.LogUtil;
import com.ez.stream.NativeApi;
import com.mediaplayer.audio.AudioBaseCallBack;
import com.mediaplayer.audio.AudioCodecParam;
import com.mediaplayer.audio.AudioEngine;
import com.mediaplayer.audio.AudioEngineCallBack;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class VoiceTalk
implements AudioEngineCallBack.ErrorInfoCallBack {
    public static final int SPEAKER_MODE_HEADSET = 0;
    public static final int SPEAKER_MODE_SPEAKER = 1;
    public static final int SPEAKER_MODE_BLUETOOTH = 2;
    public static final String TAG = "VoiceTalk";
    public static final int VOICETALK_BUTTON_NORMAL_CMD = 16640;
    public static final int VOICETALK_BUTTON_PRESS_CMD = 16896;
    public static final int VOICETALK_BUTTON_UNPRESS_CMD = 16897;
    public static final int AUDIO_CODE_TYPE_G722_1 = 0;
    public static final int AUDIO_CODE_TYPE_G711_MU = 1;
    public static final int AUDIO_CODE_TYPE_G711_A = 2;
    public static final int AUDIO_CODE_TYPE_G723 = 3;
    public static final int AUDIO_CODE_TYPE_MP1L2 = 4;
    public static final int AUDIO_CODE_TYPE_MP2L2 = 5;
    public static final int AUDIO_CODE_TYPE_G726 = 6;
    public static final int AUDIO_CODE_TYPE_AAC = 7;
    public static final int AUDIO_CODE_TYPE_OPUS_8K = 19;
    public static final int AUDIO_CODE_TYPE_OPUS_16K = 20;
    public static final int AUDIO_CODE_TYPE_OPUS_48K = 21;
    public static final int AUDIO_CODE_TYPE_RAW = 99;
    AudioCodecParam mAudioCodecParamRecoder = null;
    AudioCodecParam mAudioCodecParamPlayer = null;
    AudioEngine mAudioEngine = null;
    IVoiceStream mStreamClient;
    volatile boolean mIsTalking = false;
    volatile int mCmdType = 16897;
    volatile int mCurrentCmdType = 0;
    boolean mProcessAudio = true;
    boolean mIsFullDuplex = true;
    boolean mForceSpeakerOn = true;
    boolean mUseNewProtocol = false;
    boolean mMicrophoneOn = true;
    int mPayload = 1;
    AudioManager mAudioManager = null;
    int mAudioMode = -2;
    private boolean mIsSpeakerphoneOn = true;
    private EZVoiceTalk.EZAudioParam mAudioParam;
    private MediaExtractor mAudioExtractor;
    private EZVoiceTalk.OnLoudnessListener mOnLoudnessListener;
    private float mLoudnessInterVal;
    private EZVoiceTalk.OnSoundPlayedListener mOnSoundPlayedListener;
    private String mModelForSampleRate8K;
    private String mModelForSampleRate16K;
    private boolean mPlayAgc;
    private boolean mCaptureAgc = true;
    private long lastRoutePrintTime = 0L;
    private int flowin;
    private int flowout;
    public long engine_bt = -1L;
    public long engine_et = -1L;
    public long stream_et = -1L;
    public int eg_mode = 0;
    SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyyMMdd_HH:mm:ss", Locale.ENGLISH);
    private boolean enableEZ3AAlgorithm = false;
    private String vqeModel;
    private long howlingClient;
    private Context mContext;
    private int ezAudioEncodeType = -1;
    private int tempEZEncodeAudioType = -1;
    private final Object startSyncObj = new Object();
    volatile boolean mSpeakerOn = true;
    final Object mStatusLock = new Object();
    private Runnable mPressDelayRunnable;
    private Runnable mUnPressDelayRunnable;
    private Handler mMainHandler;
    volatile boolean mWriteFile = false;
    volatile String mWriteFileDir;
    private float maxLoudness = 0.0f;
    private int vadFlag = -1;
    final int sProfile = 2;
    final int sFreqIdx = 8;
    final int sChanCfg = 1;

    public void setEZ3AAlgorithmModel(String model) {
        this.vqeModel = model;
    }

    public void enableEZ3AAlgorithm(boolean enable) {
        this.enableEZ3AAlgorithm = enable;
    }

    public void setEZAudioEncodeType(int ezAudioEncodeType) {
        this.ezAudioEncodeType = ezAudioEncodeType;
    }

    public int getEZAudioEncodeType() {
        return this.tempEZEncodeAudioType;
    }

    public void enableRouter(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public void onErrorInfo(String s, String s1) {
    }

    public VoiceTalk(AudioManager manager, IVoiceStream client, boolean isFullDuplex, EZVoiceTalk.EZAudioParam param, boolean forceSpeakerOn) {
        this.mStreamClient = client;
        this.mAudioParam = param == null ? new EZVoiceTalk.EZAudioParam() : param;
        this.mAudioManager = manager;
        this.mAudioCodecParamRecoder = new AudioCodecParam();
        this.mAudioCodecParamRecoder.nCodecType = 2;
        this.mAudioCodecParamRecoder.nBitWidth = 2;
        this.mAudioCodecParamRecoder.nSampleRate = 8000;
        this.mAudioCodecParamRecoder.nChannel = 1;
        this.mAudioCodecParamRecoder.nBitRate = 16000;
        this.mAudioCodecParamRecoder.nVolume = 100;
        this.mAudioCodecParamPlayer = new AudioCodecParam();
        this.mAudioCodecParamPlayer.nCodecType = 2;
        this.mAudioCodecParamPlayer.nBitWidth = 2;
        this.mAudioCodecParamPlayer.nSampleRate = 8000;
        this.mAudioCodecParamPlayer.nChannel = 1;
        this.mAudioCodecParamPlayer.nBitRate = 16000;
        this.mAudioCodecParamPlayer.nVolume = 100;
        this.mAudioEngine = new AudioEngine(this.mAudioParam.captureOnly ? 2 : 3);
        Log.i((String)TAG, (String)("AudioEngine version " + this.mAudioEngine.getVersion()));
        this.mIsFullDuplex = isFullDuplex;
        this.mForceSpeakerOn = forceSpeakerOn;
        if (this.mAudioParam.useTip && !TextUtils.isEmpty((CharSequence)this.mAudioParam.tipPath)) {
            try {
                this.mAudioExtractor = new MediaExtractor();
                this.mAudioExtractor.setDataSource(this.mAudioParam.tipPath);
                int selectTrack = -1;
                for (int i = 0; i < this.mAudioExtractor.getTrackCount(); ++i) {
                    if (!this.mAudioExtractor.getTrackFormat(i).getString("mime").startsWith("audio/")) continue;
                    this.mAudioExtractor.selectTrack(i);
                    selectTrack = i;
                    break;
                }
                if (selectTrack == -1) {
                    this.mAudioExtractor.release();
                    this.mAudioExtractor = null;
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public VoiceTalk(AudioManager manager, IVoiceStream client, boolean isFullDuplex, EZVoiceTalk.EZAudioParam param) {
        this(manager, client, isFullDuplex, param, true);
    }

    private void getAudioCodecTypeParam(int encodeType) {
        switch (encodeType) {
            case 7: {
                this.mAudioCodecParamPlayer.nCodecType = 6;
                this.mAudioCodecParamRecoder.nCodecType = 6;
                this.mAudioCodecParamPlayer.nSampleRate = 16000;
                this.mAudioCodecParamRecoder.nSampleRate = 16000;
                this.mAudioCodecParamPlayer.nBitRate = 32000;
                this.mAudioCodecParamRecoder.nBitRate = 32000;
                break;
            }
            case 2: {
                this.mAudioCodecParamPlayer.nCodecType = 1;
                this.mAudioCodecParamRecoder.nCodecType = 1;
                this.mAudioCodecParamPlayer.nSampleRate = 8000;
                this.mAudioCodecParamRecoder.nSampleRate = 8000;
                this.mAudioCodecParamPlayer.nBitRate = 64000;
                this.mAudioCodecParamRecoder.nBitRate = 64000;
                break;
            }
            case 0: {
                this.mAudioCodecParamPlayer.nCodecType = 3;
                this.mAudioCodecParamRecoder.nCodecType = 3;
                this.mAudioCodecParamPlayer.nSampleRate = 16000;
                this.mAudioCodecParamRecoder.nSampleRate = 16000;
                this.mAudioCodecParamPlayer.nBitRate = 16000;
                this.mAudioCodecParamRecoder.nBitRate = 16000;
                break;
            }
            case 3: {
                this.mAudioCodecParamPlayer.nCodecType = 7;
                this.mAudioCodecParamRecoder.nCodecType = 7;
                this.mAudioCodecParamPlayer.nSampleRate = 16000;
                this.mAudioCodecParamRecoder.nSampleRate = 16000;
                this.mAudioCodecParamPlayer.nBitRate = 16000;
                this.mAudioCodecParamRecoder.nBitRate = 16000;
                break;
            }
            case 6: {
                this.mAudioCodecParamPlayer.nCodecType = 4;
                this.mAudioCodecParamRecoder.nCodecType = 4;
                this.mAudioCodecParamPlayer.nSampleRate = 8000;
                this.mAudioCodecParamRecoder.nSampleRate = 8000;
                this.mAudioCodecParamPlayer.nBitRate = 16000;
                this.mAudioCodecParamRecoder.nBitRate = 16000;
                break;
            }
            case 4: 
            case 5: {
                this.mAudioCodecParamPlayer.nCodecType = 5;
                this.mAudioCodecParamRecoder.nCodecType = 5;
                this.mAudioCodecParamPlayer.nSampleRate = 16000;
                this.mAudioCodecParamRecoder.nSampleRate = 16000;
                this.mAudioCodecParamPlayer.nBitRate = 64000;
                this.mAudioCodecParamRecoder.nBitRate = 64000;
                break;
            }
            case 19: {
                this.mAudioCodecParamPlayer.nCodecType = 9;
                this.mAudioCodecParamRecoder.nCodecType = 9;
                this.mAudioCodecParamPlayer.nSampleRate = 8000;
                this.mAudioCodecParamRecoder.nSampleRate = 8000;
                this.mAudioCodecParamPlayer.nBitRate = 16000;
                this.mAudioCodecParamRecoder.nBitRate = 16000;
                break;
            }
            case 20: {
                this.mAudioCodecParamPlayer.nCodecType = 9;
                this.mAudioCodecParamRecoder.nCodecType = 9;
                this.mAudioCodecParamPlayer.nSampleRate = 16000;
                this.mAudioCodecParamRecoder.nSampleRate = 16000;
                this.mAudioCodecParamPlayer.nBitRate = 16000;
                this.mAudioCodecParamRecoder.nBitRate = 16000;
                break;
            }
            case 21: {
                this.mAudioCodecParamPlayer.nCodecType = 9;
                this.mAudioCodecParamRecoder.nCodecType = 9;
                this.mAudioCodecParamPlayer.nSampleRate = 48000;
                this.mAudioCodecParamRecoder.nSampleRate = 48000;
                this.mAudioCodecParamPlayer.nBitRate = 16000;
                this.mAudioCodecParamRecoder.nBitRate = 16000;
                break;
            }
            default: {
                this.mAudioCodecParamPlayer.nCodecType = 2;
                this.mAudioCodecParamRecoder.nCodecType = 2;
                this.mAudioCodecParamPlayer.nSampleRate = 8000;
                this.mAudioCodecParamRecoder.nSampleRate = 8000;
                this.mAudioCodecParamPlayer.nBitRate = 64000;
                this.mAudioCodecParamRecoder.nBitRate = 64000;
            }
        }
        if (this.mStreamClient.isQosTalk() > 0) {
            this.mAudioCodecParamPlayer.nCodecType = 0;
        }
    }

    private int getAudioCodecType(int encodeType) {
        int encodec = 6;
        switch (encodeType) {
            case 7: {
                encodec = 6;
                break;
            }
            case 2: {
                encodec = 1;
                break;
            }
            case 0: {
                encodec = 3;
                break;
            }
            case 3: {
                encodec = 7;
                break;
            }
            case 6: {
                encodec = 4;
                break;
            }
            case 4: 
            case 5: {
                encodec = 5;
                break;
            }
            case 19: 
            case 20: 
            case 21: {
                encodec = 9;
                break;
            }
            default: {
                encodec = 2;
            }
        }
        return encodec;
    }

    private int getsample(int sampleIndex) {
        int sample = 16000;
        switch (sampleIndex) {
            case 1: {
                sample = 8000;
                break;
            }
            case 2: {
                sample = 16000;
                break;
            }
            case 3: {
                sample = 32000;
            }
        }
        return sample;
    }

    private int getBitRate(int bitrateIndex) {
        int bitrate = 32000;
        switch (bitrateIndex) {
            case 1: {
                bitrate = 8000;
                break;
            }
            case 2: {
                bitrate = 16000;
                break;
            }
            case 3: {
                bitrate = 32000;
                break;
            }
            case 4: {
                bitrate = 48000;
                break;
            }
            case 5: {
                bitrate = 64000;
            }
        }
        return bitrate;
    }

    private int getTracks(int tracksIndex) {
        int tracks = 1;
        switch (tracksIndex) {
            case 1: {
                tracks = 1;
                break;
            }
            case 2: {
                tracks = 2;
            }
        }
        return tracks;
    }

    private int getAudioCodecParam(String resphone) {
        if (resphone == null) {
            return 40000;
        }
        EZ_VOICE_PARAM param = JsonUtils.fromJson(resphone, EZ_VOICE_PARAM.class);
        if (param.ret != 0) {
            return param.ret;
        }
        if (param.encode == -1 || param.bitrate == 0 || param.payload == 0 || param.sample == 0 || param.tracks == 0) {
            return 40000;
        }
        this.mAudioCodecParamRecoder.nCodecType = this.mAudioCodecParamPlayer.nCodecType = this.getAudioCodecType(param.encode);
        this.mAudioCodecParamRecoder.nSampleRate = this.mAudioCodecParamPlayer.nSampleRate = this.getsample(param.sample);
        this.mAudioCodecParamRecoder.nBitRate = this.mAudioCodecParamPlayer.nBitRate = this.getBitRate(param.bitrate);
        this.mAudioCodecParamRecoder.nChannel = this.mAudioCodecParamPlayer.nChannel = this.getTracks(param.tracks);
        this.mPayload = param.payload;
        LogUtil.i(TAG, "param1 = " + resphone);
        return 0;
    }

    private void closeAudioEngine() {
        if (this.mAudioEngine != null) {
            this.mAudioEngine.stopPlay();
            this.mAudioEngine.stopRecord();
            this.mAudioEngine.close();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public int start() {
        int ret = 2;
        this.mUseNewProtocol = false;
        if (this.mStreamClient == null) {
            return ret;
        }
        LogUtil.i(TAG, "start ezAudioEncodeType = " + this.ezAudioEncodeType);
        if (this.ezAudioEncodeType < 0) {
            ret = this.mStreamClient.startVoiceTalk();
            this.eg_mode = 0;
            this.stream_et = System.currentTimeMillis();
            if (ret < 0) {
                ret = -ret;
                return ret;
            }
            this.tempEZEncodeAudioType = ret;
            this.getAudioCodecTypeParam(ret);
            ret = this.startIn();
        } else {
            this.eg_mode = 1;
            AtomicInteger localRet = new AtomicInteger(2023);
            ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 1L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(10));
            executor.execute(() -> {
                this.getAudioCodecTypeParam(this.ezAudioEncodeType);
                int startInRet = this.startIn();
                Object object = this.startSyncObj;
                synchronized (object) {
                    localRet.set(startInRet);
                    this.startSyncObj.notify();
                }
            });
            executor.shutdown();
            int linkRet = this.mStreamClient.startVoiceTalk();
            this.stream_et = System.currentTimeMillis();
            Object object = this.startSyncObj;
            synchronized (object) {
                if (localRet.get() == 2023) {
                    try {
                        this.startSyncObj.wait(5000L);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            ret = 0;
            if (linkRet < 0) {
                this.closeAudioEngine();
                this.resetMode();
                ret = -linkRet;
            } else if (localRet.get() != 0) {
                ret = localRet.get();
            }
            if (ret == 0) {
                this.tempEZEncodeAudioType = linkRet;
                if (this.tempEZEncodeAudioType != this.ezAudioEncodeType) {
                    this.eg_mode = 2;
                    LogUtil.i(TAG, String.format(Locale.CHINA, "reset AudioEngine preset[encodeType:%d] real[encodeType:%d]", this.ezAudioEncodeType, this.tempEZEncodeAudioType));
                    this.getAudioCodecTypeParam(this.tempEZEncodeAudioType);
                    this.closeAudioEngine();
                    ret = this.startIn();
                }
            }
        }
        this.setTalkStatus(ret);
        return ret;
    }

    public int startV2() {
        int ret = 1;
        String response = null;
        this.mUseNewProtocol = true;
        if (this.mStreamClient == null) {
            return ret;
        }
        response = this.mStreamClient.startVoiceTalkV2();
        if (response == null) {
            return 40000;
        }
        ret = this.getAudioCodecParam(response);
        if (ret != 0) {
            return ret;
        }
        ret = this.startIn();
        if (ret == 2016) {
            try {
                Thread.sleep(1000L);
                ret = this.startIn();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.setTalkStatus(ret);
        return ret;
    }

    int startIn() {
        this.engine_bt = System.currentTimeMillis();
        if (this.mAudioManager != null && this.mIsFullDuplex) {
            this.mAudioMode = this.mAudioManager.getMode();
            this.mIsSpeakerphoneOn = this.mAudioManager.isSpeakerphoneOn();
            try {
                this.mAudioManager.setMode(3);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (this.mContext != null && this.mAudioManager != null) {
            EZAudioManager.getInstance().init(this.mContext, this.mAudioManager, this.mForceSpeakerOn);
        } else if (this.mForceSpeakerOn) {
            this.setSpeakerphoneOn(true);
        }
        int ret = this.mAudioEngine.open();
        if (!this.mIsFullDuplex) {
            this.mAudioEngine.setIntercomType();
        }
        if (!TextUtils.isEmpty((CharSequence)this.mWriteFileDir)) {
            this.mAudioEngine.setWriteFileEx(this.mWriteFile, this.mWriteFileDir);
        }
        if (ret != 0) {
            this.closeAudioEngine();
            this.mStreamClient.stopVoiceTalk();
            ret = this.getTalkError(ret);
            return ret;
        }
        ret = this.switchAgc(this.mPlayAgc, 1);
        LogUtil.d(TAG, "switchAgc play " + this.mPlayAgc + " ret " + ret);
        ret = this.switchAgc(this.mCaptureAgc, 2);
        LogUtil.d(TAG, "switchAgc capture " + this.mCaptureAgc + " ret " + ret);
        if (this.enableEZ3AAlgorithm && this.mAudioCodecParamRecoder.nSampleRate == 16000) {
            this.mAudioEngine.setEZ3AAlgorithmModel(this.vqeModel);
            this.mAudioEngine.enable3AEZAlgorithm(3);
            this.mAudioEngine.openAGC(2, true, 26);
            this.mAudioEngine.openAGC(1, false, 26);
        }
        this.mAudioEngine.setAECType(this.mAudioParam.systemAEC ? 1 : 2);
        ret = this.mAudioEngine.setAudioParam(this.mAudioCodecParamRecoder, 1);
        if (ret == 0) {
            ret = this.mAudioEngine.setAudioCallBack((AudioBaseCallBack)new AudioEngineCallBack.RecordDataCallBack(){

                public void onRecordDataCallBack(byte[] bytes, int i) {
                    VoiceTalk.this.sendVoiceData(bytes, i);
                }
            }, 2);
        }
        if (this.mOnLoudnessListener != null && this.mLoudnessInterVal > 0.0f) {
            int nSamplePointSize = Math.min(Math.max((int)(this.mLoudnessInterVal * (float)this.mAudioCodecParamRecoder.nSampleRate), 256), 10240);
            LogUtil.d(TAG, "nSamplePointSize = " + nSamplePointSize);
            this.mAudioEngine.setCaptureDBCallBack(new AudioEngineCallBack.EnergyDBCallBack(){

                public void onEnergyData(float loudness) {
                    if (VoiceTalk.this.mIsTalking && VoiceTalk.this.mOnLoudnessListener != null) {
                        VoiceTalk.this.mOnLoudnessListener.onReportLoudness(loudness);
                    }
                }
            }, nSamplePointSize);
        }
        if (ret == 0) {
            try {
                ret = this.mAudioEngine.startRecord();
            }
            catch (IllegalStateException e) {
                ret = -2147483632;
                this.closeAudioEngine();
                this.mStreamClient.stopVoiceTalk();
            }
        }
        if (ret != 0) {
            this.closeAudioEngine();
            this.mStreamClient.stopVoiceTalk();
            ret = this.getTalkError(ret);
            return ret;
        }
        if (!this.mAudioParam.captureOnly) {
            ret = this.mAudioEngine.setAudioParam(this.mAudioCodecParamPlayer, 2);
            if (ret == 0) {
                ret = this.mAudioEngine.setAudioCallBack((AudioBaseCallBack)this, 4);
            }
            if (ret == 0) {
                ret = this.mAudioEngine.startPlay();
            }
            if (ret != 0) {
                this.closeAudioEngine();
                this.mStreamClient.stopVoiceTalk();
                ret = this.getTalkError(ret);
                return ret;
            }
        }
        EZAudioManager.getInstance().enableAutoRouter();
        LogUtil.i(TAG, "isSpeakerphoneOn " + this.mAudioManager.isSpeakerphoneOn());
        if (this.mOnSoundPlayedListener != null) {
            this.initVad();
        }
        ret = this.getTalkError(ret);
        this.engine_et = System.currentTimeMillis();
        return ret;
    }

    private void setTalkStatus(int ret) {
        if (ret == 0) {
            this.mIsTalking = true;
            if (this.mIsTalking) {
                this.mAudioManager.requestAudioFocus(null, this.mAudioManager.getMode(), 1);
            }
        }
    }

    public void stop() {
        this.mIsTalking = false;
        this.closeAudioEngine();
        if (this.mStreamClient != null) {
            this.mStreamClient.stopVoiceTalk();
        }
        this.resetMode();
        EZAudioManager.getInstance().uninit();
        if (this.mAudioExtractor != null) {
            this.mAudioExtractor.release();
            this.mAudioExtractor = null;
        }
        if (this.howlingClient != 0L) {
            NativeApi.EZVQEDestroyHandle(this.howlingClient);
            this.howlingClient = 0L;
        }
    }

    private void resetMode() {
        if (this.mAudioMode != -2 && this.mAudioManager != null) {
            try {
                this.mAudioManager.setMode(this.mAudioMode);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            if (this.mForceSpeakerOn) {
                this.setSpeakerphoneOn(this.mIsSpeakerphoneOn);
            }
            this.mAudioManager.abandonAudioFocus(null);
        }
    }

    private void sendVoiceData(byte[] pDataBuffer, int iDataSize) {
        if (!this.mIsTalking || this.mStreamClient == null || pDataBuffer == null || iDataSize < 4) {
            return;
        }
        this.flowout += iDataSize;
        if (this.mIsFullDuplex) {
            this.mCurrentCmdType = 16640;
            this.mCmdType = 16640;
        }
        if (this.mUseNewProtocol) {
            this.mStreamClient.inputVoiceTalkData(pDataBuffer, iDataSize, this.mPayload);
        } else {
            int sendCmd;
            int cmdType = this.mCmdType;
            if (cmdType == 16897) {
                Arrays.fill(pDataBuffer, (byte)0);
            } else if (this.mIsFullDuplex && !this.mMicrophoneOn) {
                byte zeroData;
                switch (this.mAudioCodecParamRecoder.nCodecType) {
                    default: {
                        zeroData = 0;
                        break;
                    }
                    case 1: {
                        zeroData = -43;
                        break;
                    }
                    case 2: {
                        zeroData = -1;
                    }
                }
                Arrays.fill(pDataBuffer, zeroData);
            }
            int n = sendCmd = !this.mIsFullDuplex && this.mCurrentCmdType != cmdType ? cmdType : 16640;
            if (this.mIsFullDuplex && this.mAudioCodecParamRecoder.nCodecType == 6 && this.mAudioExtractor != null) {
                TIP_DATA tipData = this.getTipDataIfNecessary();
                if (tipData != null && tipData.len > 0 && tipData.data != null) {
                    this.mStreamClient.inputVoiceTalkData(tipData.data, tipData.len, sendCmd);
                } else {
                    this.mStreamClient.inputVoiceTalkData(pDataBuffer, iDataSize, sendCmd);
                }
            } else {
                this.mStreamClient.inputVoiceTalkData(pDataBuffer, iDataSize, sendCmd);
            }
            if (this.mCurrentCmdType != cmdType) {
                this.mCurrentCmdType = cmdType;
            }
        }
    }

    private TIP_DATA getTipDataIfNecessary() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(2000);
        int len = this.mAudioExtractor.readSampleData(byteBuffer, 0);
        if (len > 0) {
            TIP_DATA tipData = new TIP_DATA();
            this.mAudioExtractor.advance();
            if (!this.containADTS(byteBuffer.array(), len)) {
                int outLen = len + 7;
                byte[] outData = new byte[outLen];
                this.addADTStoPacket(outData, outLen);
                System.arraycopy(byteBuffer.array(), 0, outData, 7, len);
                tipData.len = outLen;
                TIP_DATA.access$302(tipData, outData);
            } else {
                tipData.len = len;
                TIP_DATA.access$302(tipData, byteBuffer.array());
            }
            return tipData;
        }
        this.mAudioExtractor.release();
        this.mAudioExtractor = null;
        return null;
    }

    private boolean containADTS(byte[] data, int len) {
        if (len > 7) {
            return data[0] == -1 && (data[1] & 0xF0) == 240;
        }
        return false;
    }

    public int processRemoteVoiceData(byte[] pDataBuffer, int iDataSize) {
        long now;
        if (this.mAudioParam.captureOnly) {
            return 0;
        }
        if (!this.mIsTalking) {
            return 3;
        }
        if (pDataBuffer == null || iDataSize < 4) {
            return 2018;
        }
        if (this.mIsFullDuplex) {
            this.mCmdType = 16640;
        }
        if (this.lastRoutePrintTime + 2000L < (now = System.currentTimeMillis())) {
            this.lastRoutePrintTime = now;
            if (this.mAudioManager != null) {
                int maxVolume1 = this.mAudioManager.getStreamMaxVolume(0);
                int tempVolume1 = this.mAudioManager.getStreamVolume(0);
                int maxVolume2 = this.mAudioManager.getStreamMaxVolume(1);
                int tempVolume2 = this.mAudioManager.getStreamVolume(1);
                int maxVolume3 = this.mAudioManager.getStreamMaxVolume(3);
                int tempVolume3 = this.mAudioManager.getStreamVolume(3);
                int v1 = tempVolume1 * 100 / maxVolume1;
                int v2 = tempVolume2 * 100 / maxVolume2;
                int v3 = tempVolume3 * 100 / maxVolume3;
                LogUtil.i(TAG, String.format(Locale.CHINA, "isSpeakerMode = %d v_call = %d v_sys = %d v_music = %d", this.mAudioManager.isSpeakerphoneOn() ? 1 : 0, v1, v2, v3));
            }
        }
        this.flowin += iDataSize;
        if (this.mSpeakerOn) {
            int ret = this.mAudioEngine.inputData(pDataBuffer, iDataSize);
            return this.getTalkError(ret);
        }
        return 0;
    }

    public void updateVoiceTalkButtonPressStatus(boolean isPressed) {
        if (this.mIsFullDuplex) {
            return;
        }
        this.cancelPendingDelayTasks();
        if (this.mMainHandler == null) {
            this.mMainHandler = new Handler(Looper.getMainLooper());
        }
        if (isPressed) {
            this.mSpeakerOn = false;
            if (this.mPressDelayRunnable == null) {
                this.mPressDelayRunnable = new Runnable(){

                    /*
                     * WARNING - Removed try catching itself - possible behaviour change.
                     */
                    @Override
                    public void run() {
                        Object object = VoiceTalk.this.mStatusLock;
                        synchronized (object) {
                            VoiceTalk.this.mCmdType = 16896;
                        }
                    }
                };
            }
            this.mMainHandler.postDelayed(this.mPressDelayRunnable, 500L);
        } else {
            this.mSpeakerOn = true;
            if (this.mUnPressDelayRunnable == null) {
                this.mUnPressDelayRunnable = new Runnable(){

                    /*
                     * WARNING - Removed try catching itself - possible behaviour change.
                     */
                    @Override
                    public void run() {
                        Object object = VoiceTalk.this.mStatusLock;
                        synchronized (object) {
                            VoiceTalk.this.mCmdType = 16897;
                        }
                    }
                };
            }
            this.mMainHandler.post(this.mUnPressDelayRunnable);
        }
    }

    private void cancelPendingDelayTasks() {
        if (this.mPressDelayRunnable != null) {
            this.mMainHandler.removeCallbacks(this.mPressDelayRunnable);
        }
        if (this.mUnPressDelayRunnable != null) {
            this.mMainHandler.removeCallbacks(this.mUnPressDelayRunnable);
        }
    }

    public int changeTalkPatten(boolean isFullDuplex) {
        if (this.mIsFullDuplex == isFullDuplex) {
            return 0;
        }
        this.closeAudioEngine();
        this.mIsFullDuplex = isFullDuplex;
        int ret = this.startIn();
        if (ret != 0) {
            this.resetMode();
        }
        if (!this.mIsFullDuplex) {
            this.mCmdType = 16897;
        }
        return ret;
    }

    public int setVoiceVolume(int nVolume) {
        int ret = 0;
        if (this.mAudioEngine != null) {
            ret = this.mAudioEngine.setVolume(nVolume);
        }
        return ret;
    }

    public void setWriteFile(boolean bIsWrite) {
        this.mWriteFile = bIsWrite;
    }

    public void setWriteFileEX(boolean bIsWrite, String dir) {
        this.mWriteFile = bIsWrite;
        this.mWriteFileDir = dir;
    }

    public void openVoiceTalkMicrophone() {
        this.mMicrophoneOn = true;
    }

    public void closeVoiceTalkMicrophone() {
        this.mMicrophoneOn = false;
    }

    public void openVoiceTalkSpeaker() {
        this.mSpeakerOn = true;
    }

    public void closeVoiceTalkSpeaker() {
        this.mSpeakerOn = false;
    }

    int getTalkError(int error) {
        int ret = 1;
        switch (error) {
            case 0: {
                ret = 0;
                break;
            }
            case -2147483648: {
                ret = 2000;
                break;
            }
            case -2147483647: {
                ret = 2001;
                break;
            }
            case -2147483646: {
                ret = 2002;
                break;
            }
            case -2147483645: {
                ret = 2003;
                break;
            }
            case -2147483644: {
                ret = 2004;
                break;
            }
            case -2147483643: {
                ret = 2005;
                break;
            }
            case -2147483642: {
                ret = 2006;
                break;
            }
            case -2147483641: {
                ret = 2007;
                break;
            }
            case -2147483640: {
                ret = 2008;
                break;
            }
            case -2147483639: {
                ret = 2009;
                break;
            }
            case -2147483632: {
                ret = 2016;
                break;
            }
            case -2147483631: {
                ret = 2017;
                break;
            }
            case -2147483630: {
                ret = 2018;
                break;
            }
            case -2147483629: {
                ret = 2019;
                break;
            }
            case -2147483628: {
                ret = 2020;
                break;
            }
            case -2147483627: {
                ret = 2021;
                break;
            }
            case -2147483626: {
                ret = 2022;
            }
        }
        return ret;
    }

    public void setSpeakerphoneOn(boolean on) {
        if (this.mContext != null) {
            EZAudioManager.getInstance().setSpeakerPhoneOn(on);
        } else {
            this.mAudioManager.setSpeakerphoneOn(on);
        }
    }

    public boolean isSpeakerphoneOn() {
        return this.mAudioManager.isSpeakerphoneOn();
    }

    public void setSpeakerMode(int mode) {
        switch (mode) {
            case 1: {
                this.mAudioManager.stopBluetoothSco();
                this.mAudioManager.setBluetoothScoOn(false);
                this.mAudioManager.setSpeakerphoneOn(true);
                break;
            }
            case 2: {
                if (this.mAudioManager.isBluetoothScoOn()) break;
                this.mAudioManager.setBluetoothScoOn(true);
                this.mAudioManager.setSpeakerphoneOn(false);
                this.mAudioManager.startBluetoothSco();
                break;
            }
            case 0: {
                this.mAudioManager.stopBluetoothSco();
                this.mAudioManager.setBluetoothScoOn(false);
                this.mAudioManager.setSpeakerphoneOn(false);
            }
        }
    }

    public int getSpeakerMode() {
        boolean speakerphoneOn = this.mAudioManager.isSpeakerphoneOn();
        boolean bluetoothOn = this.mAudioManager.isBluetoothScoOn();
        if (speakerphoneOn) {
            return 1;
        }
        if (bluetoothOn) {
            return 2;
        }
        return 0;
    }

    public int switchAgc(boolean bSwitch, int mode) {
        if (mode == 1) {
            this.mPlayAgc = bSwitch;
        } else if (mode == 2) {
            this.mCaptureAgc = bSwitch;
        } else {
            return -1;
        }
        return this.mAudioEngine != null ? this.mAudioEngine.openAGC(mode, bSwitch, 26) : 0;
    }

    public int openPitchChanger(boolean bPitchChangeEnable, int nPitchChangeLevel) {
        if (this.mAudioEngine != null) {
            LogUtil.d(TAG, "bPitchChangeEnable = " + bPitchChangeEnable + " nPitchChangeLevel = " + nPitchChangeLevel);
            return this.mAudioEngine.openPitchShifer(bPitchChangeEnable, nPitchChangeLevel, 3);
        }
        return 1;
    }

    public int getSessionId() {
        return this.mAudioEngine == null ? -1 : this.mAudioEngine.getAECSessionID();
    }

    public int getFlowIn() {
        return this.flowin;
    }

    public int getFlowOut() {
        return this.flowout;
    }

    public void setOnLoudnessListener(EZVoiceTalk.OnLoudnessListener onLoudnessListener, float interVal) {
        this.mOnLoudnessListener = onLoudnessListener;
        this.mLoudnessInterVal = interVal;
    }

    public void setOnSoundPlayedListener(EZVoiceTalk.OnSoundPlayedListener onSoundPlayedListener, String model8, String model16) {
        this.mOnSoundPlayedListener = onSoundPlayedListener;
        this.mModelForSampleRate8K = model8;
        this.mModelForSampleRate16K = model16;
        this.initVad();
    }

    private void initVad() {
        if (this.mAudioEngine == null || this.mAudioCodecParamPlayer == null) {
            return;
        }
        if (this.mOnSoundPlayedListener == null) {
            this.mAudioEngine.setPlayDBCallBack(null, 512);
            int ret = this.mAudioEngine.setVoiceAutoDetectCallBack(0, null, 0, null);
            LogUtil.d(TAG, "setVoiceAutoDetectCallBack cancel " + ret);
            return;
        }
        int sampleRate = 0;
        if (this.mAudioCodecParamPlayer.nSampleRate == 8000 && !TextUtils.isEmpty((CharSequence)this.mModelForSampleRate8K)) {
            sampleRate = 8000;
        } else if (this.mAudioCodecParamPlayer.nSampleRate == 16000 && !TextUtils.isEmpty((CharSequence)this.mModelForSampleRate16K)) {
            sampleRate = 16000;
        }
        if (sampleRate != 0) {
            int ret = this.mAudioEngine.setPlayDBCallBack(new AudioEngineCallBack.EnergyDBCallBack(){

                public void onEnergyData(float loudness) {
                    if (VoiceTalk.this.vadFlag == 1) {
                        if (VoiceTalk.this.maxLoudness == 0.0f) {
                            VoiceTalk.this.maxLoudness = loudness;
                        }
                        VoiceTalk.this.maxLoudness = Math.max(VoiceTalk.this.maxLoudness, loudness);
                    } else if (VoiceTalk.this.vadFlag == 4) {
                        EZVoiceTalk.OnSoundPlayedListener onSoundPlayedListener = VoiceTalk.this.mOnSoundPlayedListener;
                        if (onSoundPlayedListener != null && VoiceTalk.this.maxLoudness < -25.0f) {
                            LogUtil.d(VoiceTalk.TAG, "onMaxSoundness = " + VoiceTalk.this.maxLoudness);
                            onSoundPlayedListener.onSoundLowTip(VoiceTalk.this.maxLoudness);
                        }
                        VoiceTalk.this.maxLoudness = 0.0f;
                    }
                }
            }, 512);
            LogUtil.d(TAG, "setPlayDBCallBack = " + ret);
            ret = this.mAudioEngine.setVoiceAutoDetectCallBack(0, new AudioEngineCallBack.VoiceAutoDetectCallBack(){

                public void onVoiceAutoDetectData(int ae_end, int vad_flag, byte[] voicedata, int voicelen, int relative_start_frame, int relative_end_frame, int speech_cutoff) {
                    if (VoiceTalk.this.vadFlag != 1 && (vad_flag == 1 || vad_flag == 2)) {
                        VoiceTalk.this.vadFlag = 1;
                        VoiceTalk.this.maxLoudness = 0.0f;
                    } else if (VoiceTalk.this.vadFlag != 4 && (vad_flag == 3 || vad_flag == 4)) {
                        VoiceTalk.this.vadFlag = 4;
                    }
                }
            }, 0, sampleRate == 8000 ? this.mModelForSampleRate8K : this.mModelForSampleRate16K);
            LogUtil.d(TAG, "setVoiceAutoDetectCallBack = " + ret);
        }
    }

    private void addADTStoPacket(byte[] packet, int packetLen) {
        packet[0] = -1;
        packet[1] = -7;
        packet[2] = 96;
        packet[3] = (byte)(64 + (packetLen >> 11));
        packet[4] = (byte)((packetLen & 0x7FF) >> 3);
        packet[5] = (byte)(((packetLen & 7) << 5) + 31);
        packet[6] = -4;
    }

    public static class EZ_VOICE_PARAM {
        public int encode;
        public int sample;
        public int bitrate;
        public int payload;
        public int tracks;
        public int ret;
    }

    private static class TIP_DATA {
        private int len;
        private byte[] data;

        private TIP_DATA() {
        }

        static /* synthetic */ byte[] access$302(TIP_DATA x0, byte[] x1) {
            x0.data = x1;
            return x1;
        }
    }
}


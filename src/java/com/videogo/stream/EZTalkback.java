/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.media.AudioManager
 *  android.os.Handler
 *  android.os.Message
 *  android.util.Log
 *  com.ez.player.EZVoiceTalk
 *  com.ez.player.EZVoiceTalk$EZAudioParam
 *  com.ez.player.EZVoiceTalk$OnLoudnessListener
 *  com.ez.player.EZVoiceTalk$OnVoiceTalkListener
 *  com.ez.stream.EZStreamClientManager
 *  com.ez.stream.InitParam
 */
package com.videogo.stream;

import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.ez.player.EZVoiceTalk;
import com.ez.stream.EZStreamClientManager;
import com.ez.stream.InitParam;
import com.videogo.constant.Config;
import com.videogo.device.DeviceInfoEx;
import com.videogo.device.DeviceManager;
import com.videogo.errorlayer.ErrorInfo;
import com.videogo.errorlayer.ErrorLayer;
import com.videogo.exception.BaseException;
import com.videogo.openapi.PlayAPI;
import com.videogo.stream.EZStreamParamHelp;
import com.videogo.util.LogUtil;
import java.io.File;
import java.util.List;

public class EZTalkback {
    private static final String TAG = "EZTalkback";
    EZVoiceTalk mEZVoiceTalk;
    private Handler mHandler;
    private TalkBackSuccessCallback talkBackSuccessCallback;
    private EZStreamParamHelp mEZStreamParamHelp;

    public void setHandler(Handler handler) {
        this.mHandler = handler;
    }

    public void setTalkBackSuccessCallback(TalkBackSuccessCallback talkBackSuccessCallback) {
        this.talkBackSuccessCallback = talkBackSuccessCallback;
    }

    public EZTalkback(EZStreamParamHelp streamParamHelp) {
        this.mEZStreamParamHelp = streamParamHelp;
        try {
            InitParam initParam = streamParamHelp.getInitParam(6);
            if (initParam == null) {
                LogUtil.d(TAG, "Talkback. initParam is null");
            }
            if (initParam != null) {
                LogUtil.d(TAG, "Talkback. initParam is " + initParam.toString());
            }
            this.mEZVoiceTalk = new EZVoiceTalk(EZStreamClientManager.create((Context)PlayAPI.mApplication.getApplicationContext()), initParam);
            if (Config.STREAMDEBUGGING) {
                String filePath = PlayAPI.mApplication.getExternalFilesDir(null).getPath() + "/talkback/";
                File file = new File(filePath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                this.mEZVoiceTalk.setVoiceTalkWriteFileEX(Config.STREAMDEBUGGING, filePath);
            }
            this.mEZVoiceTalk.setQosRspTimeout(streamParamHelp.getiQosRspTimeout());
            this.mEZVoiceTalk.setOnVoiceTalkListener(new EZVoiceTalk.OnVoiceTalkListener(){

                public boolean onError(EZVoiceTalk ezVoiceTalk, int i) {
                    EZTalkback.this.onVoiceTalkError(i);
                    return false;
                }

                public void onNeedToken(EZVoiceTalk ezVoiceTalk) {
                    LogUtil.d(EZTalkback.TAG, "streamsdk. start talkback. onNeedToken");
                    try {
                        if (EZTalkback.this.resetTokens()) {
                            EZTalkback.this.start();
                        }
                    }
                    catch (BaseException e) {
                        ErrorInfo errorInfo1 = e.getErrorInfo();
                        EZTalkback.this.sendMessage(114, e.getErrorCode(), errorInfo1);
                        LogUtil.printErrStackTrace(EZTalkback.TAG, e.fillInStackTrace());
                    }
                }

                public void onNetStatus(String s) {
                    LogUtil.d(EZTalkback.TAG, "onNetStatus: " + s);
                }
            });
        }
        catch (BaseException e) {
            LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
        }
    }

    private void onVoiceTalkError(int errorCode) {
        if (errorCode == 0) {
            LogUtil.d(TAG, "streamsdk. start talkback succeeded");
            if (this.talkBackSuccessCallback != null) {
                this.talkBackSuccessCallback.talkBackSuccess();
            }
            this.handlePlaySuccess();
        } else {
            this.handlePlayerFailed(errorCode);
        }
    }

    public void release() {
        if (this.mEZVoiceTalk != null) {
            this.mEZVoiceTalk.release();
        }
    }

    public boolean start() {
        boolean isDeviceTalkBack = this.mEZStreamParamHelp.isDeviceTalkBack();
        DeviceInfoEx deviceInfoEx = this.mEZStreamParamHelp.getDeviceInfo();
        int supportChannelTalk = deviceInfoEx != null ? deviceInfoEx.getSupportChannelTalk() : 0;
        Log.d((String)"aaaa", (String)(supportChannelTalk + "bbbbbbbbbbbbb"));
        if (!isDeviceTalkBack && supportChannelTalk == 0) {
            ErrorInfo errorInfo = new ErrorInfo();
            errorInfo.errorCode = 510000;
            Message message = Message.obtain();
            message.what = 114;
            message.arg1 = errorInfo.errorCode;
            message.obj = errorInfo;
            this.mHandler.sendMessage(message);
            return false;
        }
        boolean bFullDuplex = true;
        if (isDeviceTalkBack) {
            int nSupport = this.mEZStreamParamHelp.supportTalkType();
            if (nSupport == 3) {
                bFullDuplex = false;
            } else if (nSupport != 1 && nSupport != 4) {
                this.handlePlayerFailed(400025);
                return false;
            }
        }
        AudioManager audiomanage = (AudioManager)PlayAPI.mApplication.getSystemService("audio");
        EZVoiceTalk.EZAudioParam audioParam = new EZVoiceTalk.EZAudioParam();
        audioParam.systemAEC = this.mEZStreamParamHelp.useSystemAEC;
        audioParam.captureOnly = this.mEZStreamParamHelp.deviceInfo.getDevProtoEnum() == 6;
        LogUtil.i(TAG, "streamsdk. start talkback. " + (bFullDuplex ? "FullDuplex" : "HalfDuplex"));
        int errorCode = this.mEZVoiceTalk.startVoiceTalk(audiomanage, bFullDuplex, audioParam);
        this.onVoiceTalkError(errorCode);
        return true;
    }

    public void stop() {
        if (this.mEZVoiceTalk == null) {
            return;
        }
        this.mEZVoiceTalk.stopVoiceTalk();
        this.handleStopSuccess();
    }

    public void openVoiceTalkMicrophone() {
        this.mEZVoiceTalk.openVoiceTalkMicrophone();
    }

    public void closeVoiceTalkMicrophone() {
        this.mEZVoiceTalk.closeVoiceTalkMicrophone();
    }

    public void setVoiceTalkLoudnessCallback(EZVoiceTalk.OnLoudnessListener onLoudnessListener, float interVal) {
        if (this.mEZVoiceTalk == null) {
            return;
        }
        this.mEZVoiceTalk.setOnLoudnessListener(onLoudnessListener, interVal);
    }

    public void startVoiceChange(boolean bPitchChangeEnable, final int nPitchChangeLevel, final TalkBackVoiceChangeCallback callback) {
        if (PlayAPI.getInstance().isUsingGlobalSDK()) {
            return;
        }
        if (this.mEZVoiceTalk == null) {
            if (callback != null) {
                callback.onVoiceChange(false, new ErrorInfo(-1, "voice talk player is null."));
            }
            return;
        }
        if (!bPitchChangeEnable) {
            this.mEZVoiceTalk.openPitchChanger(false, nPitchChangeLevel);
        } else {
            new Thread(new Runnable(){

                @Override
                public void run() {
                    boolean ret;
                    block4: {
                        ret = false;
                        try {
                            ret = PlayAPI.getInstance().isSupportTalkVoicePermission(((EZTalkback)EZTalkback.this).mEZStreamParamHelp.deviceSerial, ((EZTalkback)EZTalkback.this).mEZStreamParamHelp.cameraNo);
                        }
                        catch (BaseException e) {
                            e.printStackTrace();
                            ErrorInfo errorInfo = new ErrorInfo(e.getErrorInfo().errorCode, e.getErrorInfo().description);
                            if (callback == null) break block4;
                            callback.onVoiceChange(false, errorInfo);
                        }
                    }
                    if (ret) {
                        EZTalkback.this.mEZVoiceTalk.openPitchChanger(true, nPitchChangeLevel);
                        if (callback != null) {
                            callback.onVoiceChange(true, null);
                        }
                    }
                }
            }).start();
        }
    }

    protected void sendMessage(int msg, int arg1, Object obj) {
        if (this.mHandler != null) {
            Message message = Message.obtain();
            message.what = msg;
            message.arg1 = arg1;
            message.obj = obj;
            this.mHandler.sendMessage(message);
        }
    }

    public void setTalkbackStatus(boolean bPressed) {
        if (this.mEZVoiceTalk == null) {
            return;
        }
        LogUtil.d(TAG, "Half duplex. Pressed? " + bPressed);
        this.mEZVoiceTalk.updateVoiceTalkButtonPressStatus(bPressed);
    }

    public void setSpeakerphoneOn(boolean on) {
        if (this.mEZVoiceTalk != null) {
            this.mEZVoiceTalk.setSpeakerphoneOn(on);
        }
    }

    public boolean isSpeakerphoneOn() {
        boolean isSpeakerphoneOn = true;
        if (this.mEZVoiceTalk != null) {
            isSpeakerphoneOn = this.mEZVoiceTalk.isSpeakerphoneOn();
        }
        return isSpeakerphoneOn;
    }

    public void setTalkRemoteMuted(boolean muted) {
        if (this.mEZVoiceTalk != null) {
            if (muted) {
                this.mEZVoiceTalk.closeVoiceTalkSpeaker();
            } else {
                this.mEZVoiceTalk.openVoiceTalkSpeaker();
            }
        }
    }

    protected void handlePlayerFailed(int errorcode) {
        LogUtil.d(TAG, "streamsdk. start talkback. handlePlayerFailed errorcode =" + errorcode);
        ErrorInfo errorInfo = null;
        switch (errorcode) {
            case 7: 
            case 8: {
                if (!Config.ENABLE_SDK_TKTOKEN) {
                    EZStreamClientManager.create((Context)PlayAPI.mApplication).clearTokens();
                    try {
                        this.resetTokens();
                        this.start();
                        return;
                    }
                    catch (BaseException e) {
                        ErrorInfo errorInfo1 = e.getErrorInfo();
                        this.sendMessage(114, e.getErrorCode(), errorInfo1);
                        LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
                        break;
                    }
                }
                errorInfo = ErrorLayer.getErrorLayer(31, errorcode);
                this.sendMessage(114, errorInfo.errorCode, errorInfo);
                break;
            }
            case 400025: {
                errorInfo = ErrorLayer.getErrorLayer(2, errorcode);
                this.sendMessage(114, errorInfo.errorCode, errorInfo);
                break;
            }
            default: {
                errorInfo = ErrorLayer.getErrorLayer(31, errorcode);
                this.sendMessage(114, errorInfo.errorCode, errorInfo);
            }
        }
    }

    protected void handlePlaySuccess() {
        LogUtil.d(TAG, "streamsdk. start talkback. handlePlaySuccess");
        this.sendMessage(113, 0, null);
    }

    protected void handlePlayFinished() {
        LogUtil.d(TAG, "streamsdk. start talkback. handlePlayFinished");
        this.sendMessage(133, 0, null);
    }

    protected void handleStopSuccess() {
        LogUtil.d(TAG, "streamsdk. start talkback. handleStopSuccess");
        this.sendMessage(115, 0, null);
    }

    private boolean resetTokens() throws BaseException {
        List<String> tokenList = DeviceManager.getInstance().getStreamToken(true);
        if (tokenList != null && tokenList.size() > 0) {
            EZStreamClientManager.create((Context)PlayAPI.mApplication.getApplicationContext()).setTokens(tokenList.toArray(new String[tokenList.size()]));
            return true;
        }
        return false;
    }

    public static interface TalkBackSuccessCallback {
        public void talkBackSuccess();
    }

    public static interface TalkBackVoiceChangeCallback {
        public void onVoiceChange(boolean var1, ErrorInfo var2);
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Bitmap
 *  android.graphics.BitmapFactory
 *  android.graphics.SurfaceTexture
 *  android.os.Handler
 *  android.os.Message
 *  android.text.TextUtils
 *  android.view.SurfaceHolder
 *  com.ez.player.EZMediaPlayer
 *  com.ez.player.EZMediaPlayer$EZOSDTime
 *  com.ez.player.EZMediaPlayer$MediaError
 *  com.ez.player.EZMediaPlayer$MediaInfo
 *  com.ez.player.EZMediaPlayer$OnCompletionListener
 *  com.ez.player.EZMediaPlayer$OnErrorListener
 *  com.ez.player.EZMediaPlayer$OnInfoListener
 *  com.ez.player.EZVoiceTalk$OnLoudnessListener
 *  com.ez.stream.EZGetPercentInfo
 *  com.ez.stream.EZStreamClientManager
 *  com.ez.stream.InitParam
 *  com.ez.stream.SystemTransformSim
 *  org.MediaPlayer.PlayM4.Player
 */
package com.videogo.openapi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import com.ez.player.EZMediaPlayer;
import com.ez.player.EZVoiceTalk;
import com.ez.stream.EZGetPercentInfo;
import com.ez.stream.EZStreamClientManager;
import com.ez.stream.InitParam;
import com.ez.stream.SystemTransformSim;
import com.ezviz.npcsdk.NpcPlayer;
import com.videogo.errorlayer.ErrorInfo;
import com.videogo.errorlayer.ErrorLayer;
import com.videogo.exception.BaseException;
import com.videogo.exception.InnerException;
import com.videogo.exception.PlaySDKException;
import com.videogo.openapi.ConfigLoader;
import com.videogo.openapi.EZConstants;
import com.videogo.openapi.EZMediaPlayerEx;
import com.videogo.openapi.EZOpenSDKListener;
import com.videogo.openapi.EZPlaybackStreamParam;
import com.videogo.openapi.PlayAPI;
import com.videogo.openapi.bean.EZCloudRecordFile;
import com.videogo.openapi.bean.EZDeviceDetailPublicInfo;
import com.videogo.openapi.bean.EZDeviceRecordFile;
import com.videogo.openapi.bean.EZLeaveMessage;
import com.videogo.stream.EZPlayWaterMarkConfig;
import com.videogo.stream.EZStreamCtrl;
import com.videogo.stream.EZStreamParamHelp;
import com.videogo.stream.EZTalkback;
import com.videogo.util.LogUtil;
import com.videogo.util.VideoTransUtil;
import com.videogo.widget.CustomRect;
import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.MediaPlayer.PlayM4.Player;

public class EZPlayer
implements EZMediaPlayer.OnCompletionListener,
EZMediaPlayer.OnErrorListener,
EZMediaPlayer.OnInfoListener {
    private static final String TAG = "EZPlayer";
    private EZStreamCtrl streamCtrl = null;
    private ExecutorService cachedThreadPool;
    private EZMediaPlayer mLanPlayer;
    private Handler mHandler;
    private SurfaceHolder mSurfaceHolder;
    private SurfaceHolder mSurfaceHolder2;
    private SurfaceHolder[] mSurfaceHolders;
    private SurfaceTexture mSurfaceTexture;
    private SurfaceTexture mSurfaceTexture2;
    private NpcPlayer mNpcPlayer;
    private boolean isNpcPlayer = false;
    private String mPlayerUrl;
    private boolean isAudioOnly = false;
    private String mOutTempFilepath;
    private String mOutFilepath;
    private String mVerifyCode;
    private EZStreamParamHelp paramHelp;
    private EZOpenSDKListener.EZStreamDownloadCallback mStreamDownloadCallback;
    private ConfigLoader.PlayConfig mPlayConfig = new ConfigLoader.PlayConfig();

    public EZPlayer(EZStreamParamHelp paramHelp) {
        this.cachedThreadPool = Executors.newSingleThreadExecutor();
        try {
            this.streamCtrl = new EZStreamCtrl(paramHelp, null, this.mPlayConfig);
            this.paramHelp = paramHelp;
        }
        catch (BaseException e) {
            LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
        }
    }

    public EZPlayer(String url) {
        this.cachedThreadPool = Executors.newSingleThreadExecutor();
        this.mPlayerUrl = url;
        if (!TextUtils.isEmpty((CharSequence)url) && url.startsWith("ysproto://")) {
            try {
                this.streamCtrl = new EZStreamCtrl(null, url, this.mPlayConfig);
            }
            catch (BaseException e) {
                e.printStackTrace();
            }
        } else {
            this.isNpcPlayer = true;
        }
    }

    public EZPlayer() {
        this.cachedThreadPool = Executors.newSingleThreadExecutor();
        try {
            this.streamCtrl = new EZStreamCtrl(null, this.mPlayConfig);
        }
        catch (BaseException e) {
            LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
        }
    }

    public synchronized void release() {
        if (this.cachedThreadPool != null) {
            this.cachedThreadPool.shutdown();
            this.cachedThreadPool.shutdownNow();
            this.cachedThreadPool = null;
        }
        if (this.mLanPlayer != null) {
            this.mLanPlayer.release();
            return;
        }
        if (this.mNpcPlayer != null) {
            this.mNpcPlayer.stop();
            this.mNpcPlayer = null;
        }
        if (this.streamCtrl == null) {
            return;
        }
        this.streamCtrl.release();
        this.streamCtrl = null;
    }

    public boolean setSurfaceEx(SurfaceTexture surfaceTexture) {
        return this.setSurfaceEx(surfaceTexture, 0);
    }

    public boolean setSurfaceEx(SurfaceTexture surfaceTexture, int streamId) {
        if (streamId == 0) {
            if (this.mSurfaceTexture != null && surfaceTexture == this.mSurfaceTexture) {
                return false;
            }
            this.mSurfaceTexture = surfaceTexture;
            if (this.mNpcPlayer != null) {
                this.mNpcPlayer.setDisplay(surfaceTexture);
                return true;
            }
            if (this.streamCtrl == null) {
                return false;
            }
            if (this.mSurfaceHolder != null) {
                this.streamCtrl.setSurfaceHold(null);
            }
            this.streamCtrl.setSurfaceEx(surfaceTexture);
            return true;
        }
        if (streamId == 1) {
            if (this.mSurfaceTexture2 != null && surfaceTexture == this.mSurfaceTexture2) {
                return false;
            }
            this.mSurfaceTexture2 = surfaceTexture;
            if (this.mNpcPlayer != null) {
                this.mNpcPlayer.setDisplay(surfaceTexture, streamId);
                return true;
            }
            if (this.streamCtrl == null) {
                return false;
            }
            if (this.mSurfaceHolder2 != null) {
                this.streamCtrl.setSurfaceHold(null, streamId);
            }
            this.streamCtrl.setSurfaceEx(surfaceTexture, streamId);
            this.paramHelp.isMultiChannelDevice = true;
            return true;
        }
        return false;
    }

    public boolean setSurfaceHold(SurfaceHolder surfaceHolder) {
        return this.setSurfaceHold(surfaceHolder, 0);
    }

    public boolean setSurfaceHold(SurfaceHolder surfaceHolder, int streamId) {
        if (surfaceHolder != null && !surfaceHolder.getSurface().isValid()) {
            return false;
        }
        if (streamId == 0) {
            this.mSurfaceHolder = surfaceHolder;
            if (this.mNpcPlayer != null) {
                this.mNpcPlayer.setDisplay(surfaceHolder);
                return true;
            }
            if (this.mLanPlayer != null) {
                this.mLanPlayer.setDisplay(this.mSurfaceHolder);
                return true;
            }
            if (this.streamCtrl == null) {
                return false;
            }
            if (this.mSurfaceTexture != null) {
                this.streamCtrl.setSurfaceEx(null);
            }
            this.streamCtrl.setSurfaceHold(surfaceHolder);
            return true;
        }
        if (streamId == 1) {
            this.mSurfaceHolder2 = surfaceHolder;
            if (this.mNpcPlayer != null) {
                this.mNpcPlayer.setDisplay(surfaceHolder, streamId);
                return true;
            }
            if (this.mLanPlayer != null) {
                this.mLanPlayer.setDisplay(this.mSurfaceHolder, streamId);
                return true;
            }
            if (this.streamCtrl == null) {
                return false;
            }
            if (this.mSurfaceTexture != null) {
                this.streamCtrl.setSurfaceEx(null, streamId);
            }
            this.streamCtrl.setSurfaceHold(surfaceHolder, streamId);
            this.paramHelp.isMultiChannelDevice = true;
            return true;
        }
        return false;
    }

    public boolean setHandler(Handler handler) {
        if (this.mLanPlayer != null || this.isNpcPlayer) {
            this.mHandler = handler;
            return true;
        }
        if (this.streamCtrl == null) {
            return false;
        }
        this.streamCtrl.setHandler(handler);
        return true;
    }

    public boolean startRealPlay() {
        if (this.isNpcPlayer) {
            this.mNpcPlayer = NpcPlayer.create(this.mPlayerUrl);
            this.mNpcPlayer.setHandler(this.mHandler);
            if (this.mSurfaceTexture != null) {
                this.mNpcPlayer.setDisplay(this.mSurfaceTexture);
            } else {
                this.mNpcPlayer.setDisplay(this.mSurfaceHolder);
            }
            this.mNpcPlayer.setAudioOnly(this.isAudioOnly);
            this.mNpcPlayer.start();
            return true;
        }
        if (this.mLanPlayer != null) {
            this.cachedThreadPool.submit(new Runnable(){

                @Override
                public void run() {
                    if (EZPlayer.this.mLanPlayer != null) {
                        EZPlayer.this.mLanPlayer.setDisplay(EZPlayer.this.mSurfaceHolder);
                        EZPlayer.this.mLanPlayer.start();
                    }
                }
            });
            return true;
        }
        if (this.streamCtrl == null) {
            return false;
        }
        this.streamCtrl.startRealPlay();
        return true;
    }

    public boolean stopRealPlay() {
        if (this.isNpcPlayer && this.mNpcPlayer != null) {
            this.mNpcPlayer.stop();
            this.mNpcPlayer = null;
            return true;
        }
        if (this.mLanPlayer != null) {
            this.cachedThreadPool.submit(new Runnable(){

                @Override
                public void run() {
                    if (EZPlayer.this.mLanPlayer != null) {
                        EZPlayer.this.mLanPlayer.stop();
                        EZPlayer.this.sendMessage(133, 0, null);
                    }
                }
            });
            return true;
        }
        if (this.streamCtrl == null) {
            return false;
        }
        this.streamCtrl.stopRealPlay();
        return true;
    }

    public void setPlayVerifyCode(String verifyCode) {
        if (this.mLanPlayer != null) {
            this.mLanPlayer.setSecretKey(verifyCode);
            return;
        }
        if (this.streamCtrl == null) {
            return;
        }
        this.mVerifyCode = verifyCode;
        this.streamCtrl.setPlayKey(verifyCode);
    }

    public boolean openSound() {
        if (this.streamCtrl == null) {
            return false;
        }
        return this.streamCtrl.soundCtrl(true);
    }

    public boolean closeSound() {
        if (this.streamCtrl == null) {
            return false;
        }
        return this.streamCtrl.soundCtrl(false);
    }

    public boolean startVoiceTalk(boolean isDeviceTalkBack) {
        if (this.streamCtrl == null) {
            return false;
        }
        this.paramHelp.setIsDeviceTalkBack(isDeviceTalkBack);
        this.streamCtrl.startTalkback();
        return true;
    }

    public boolean startVoiceTalk() {
        return this.startVoiceTalk(true);
    }

    public boolean stopVoiceTalk() {
        if (this.streamCtrl == null) {
            return false;
        }
        this.streamCtrl.stopTalkback();
        return true;
    }

    public void setVoiceTalkLoudnessCallback(EZVoiceTalk.OnLoudnessListener onLoudnessListener, float interval) {
        if (this.streamCtrl == null) {
            return;
        }
        this.streamCtrl.setVoiceTalkLoudnessCallback(onLoudnessListener, interval);
    }

    public void startVoiceChange(EZConstants.EZVoiceChangeType voiceChangeType, EZTalkback.TalkBackVoiceChangeCallback callback) {
        if (this.streamCtrl == null) {
            return;
        }
        this.streamCtrl.startVoiceChange(voiceChangeType, callback);
    }

    public void setVoiceTalkStatus(boolean pressed) {
        if (this.streamCtrl == null) {
            return;
        }
        this.streamCtrl.setTalkbackStatus(pressed);
    }

    public void setTalkRemoteMuted(boolean muted) {
        if (this.streamCtrl == null) {
            return;
        }
        this.streamCtrl.setTalkRemoteMuted(muted);
    }

    public void setUseSystemAEC(boolean useSystemAEC) {
        if (this.paramHelp != null) {
            this.paramHelp.useSystemAEC = useSystemAEC;
        }
    }

    public void openVoiceTalkMicrophone() {
        if (this.streamCtrl == null) {
            return;
        }
        this.streamCtrl.openVoiceTalkMicrophone();
    }

    public void closeVoiceTalkMicrophone() {
        if (this.streamCtrl == null) {
            return;
        }
        this.streamCtrl.closeVoiceTalkMicrophone();
    }

    public void setVoiceTalkCallingId(String callingId) {
        if (this.streamCtrl == null) {
            return;
        }
        if (this.paramHelp != null) {
            this.paramHelp.voiceTalkCallingID = callingId;
        }
    }

    public boolean startPlayback(EZCloudRecordFile cloudFile) {
        if (this.streamCtrl == null || cloudFile.getStartTime() == null || cloudFile.getStopTime() == null) {
            return false;
        }
        this.streamCtrl.startCloudPlayback(cloudFile);
        return true;
    }

    public boolean startPlayback(EZDeviceRecordFile deviceFile) {
        if (this.streamCtrl == null || deviceFile.getStartTime() == null || deviceFile.getStopTime() == null) {
            return false;
        }
        this.paramHelp.setAIPlayback(false);
        this.streamCtrl.startLocalPlayback(deviceFile.getStartTime(), deviceFile.getStopTime());
        return true;
    }

    public boolean startPlayback(Calendar startTime, Calendar stopTime) {
        if (this.streamCtrl == null || startTime == null || stopTime == null) {
            return false;
        }
        this.paramHelp.setAIPlayback(false);
        this.streamCtrl.startLocalPlayback(startTime, stopTime);
        return true;
    }

    public boolean startAIPlayback(EZDeviceRecordFile deviceFile) {
        if (this.streamCtrl == null || deviceFile.getStartTime() == null || deviceFile.getStopTime() == null) {
            return false;
        }
        this.paramHelp.setAIPlayback(true);
        this.streamCtrl.startLocalPlayback(deviceFile.getStartTime(), deviceFile.getStopTime());
        return true;
    }

    public boolean pausePlayback() {
        if (this.streamCtrl == null) {
            return false;
        }
        return this.streamCtrl.pausePlayback();
    }

    public boolean resumePlayback() {
        if (this.streamCtrl == null) {
            return false;
        }
        return this.streamCtrl.resumePlayback();
    }

    public boolean seekPlayback(Calendar offsetTime) {
        if (this.streamCtrl == null) {
            return false;
        }
        return this.streamCtrl.seekPlayback(offsetTime);
    }

    public Calendar getOSDTime() {
        if (this.mLanPlayer != null) {
            EZMediaPlayer.EZOSDTime ezosdTime = this.mLanPlayer.getOSDTime();
            if (ezosdTime == null) {
                return null;
            }
            GregorianCalendar mOSDTime = new GregorianCalendar();
            mOSDTime.set(ezosdTime.year, ezosdTime.month - 1, ezosdTime.day, ezosdTime.hour, ezosdTime.min, ezosdTime.sec);
            return mOSDTime;
        }
        if (this.streamCtrl == null) {
            return null;
        }
        return this.streamCtrl.getOSDTime();
    }

    public boolean stopPlayback() {
        if (this.streamCtrl == null) {
            return false;
        }
        this.streamCtrl.stopPlayback();
        return true;
    }

    public boolean setPlaybackRate(EZConstants.EZPlaybackRate rate) {
        return this.setPlaybackRate(rate, 0);
    }

    public boolean setPlaybackRate(EZConstants.EZPlaybackRate rate, int mode) {
        LogUtil.d(TAG, "EZPlaybackRate: " + (Object)((Object)rate));
        LogUtil.d(TAG, "mode: " + mode);
        if (this.streamCtrl == null) {
            return false;
        }
        return this.streamCtrl.setPlaybackRate(rate.value, mode);
    }

    @Deprecated
    public boolean setCloudPlaybackRate(EZConstants.EZCloudPlaybackRate rate) {
        if (this.streamCtrl == null) {
            return false;
        }
        return this.streamCtrl.setPlaybackRate(rate.value, 0);
    }

    public void startPlaybackV2(EZPlaybackStreamParam param) {
        if (param == null) {
            LogUtil.e(TAG, "invalid param!");
            return;
        }
        switch (param.recordSource) {
            case 1: {
                this.startPlayback(param.ezCloudRecordFile);
                break;
            }
            case 2: {
                this.startPlayback(param.ezDeviceRecordFile);
                break;
            }
            default: {
                LogUtil.e(TAG, "unknown record source!");
            }
        }
    }

    public void setStreamDownloadCallback(EZOpenSDKListener.EZStreamDownloadCallback mStreamDownloadCallback) {
        this.mStreamDownloadCallback = mStreamDownloadCallback;
    }

    public boolean startLocalRecordWithFile(String recordFile) {
        File out;
        if (this.streamCtrl == null) {
            return false;
        }
        this.mOutFilepath = recordFile;
        this.mOutTempFilepath = recordFile + "_temp";
        boolean isMultiChannelDevice = this.paramHelp != null && this.paramHelp.isMultiChannelDevice;
        LogUtil.d(TAG, "mOutFilepath is " + recordFile);
        LogUtil.d(TAG, "isMultiChannelDevice is " + isMultiChannelDevice);
        boolean ret = this.streamCtrl.startRecordFile(this.mOutTempFilepath, isMultiChannelDevice ? -1 : 0);
        if (ret) {
            return true;
        }
        File file = new File(this.mOutTempFilepath);
        if (file.exists()) {
            file.delete();
        }
        if ((out = new File(this.mOutFilepath)).exists()) {
            out.delete();
        }
        return false;
    }

    public boolean stopLocalRecord() {
        LogUtil.d(TAG, "stopLocalRecord");
        if (this.streamCtrl == null) {
            return false;
        }
        boolean isMultiChannelDevice = this.paramHelp != null && this.paramHelp.isMultiChannelDevice;
        this.streamCtrl.stopRecordFile(isMultiChannelDevice ? -1 : 0);
        VideoTransUtil.TransPsToMp4(this.mOutTempFilepath, this.mVerifyCode, this.mOutFilepath, isMultiChannelDevice, this.mStreamDownloadCallback);
        return true;
    }

    public boolean startLocalRecordWithFileEx(String recordFile) {
        if (this.streamCtrl == null) {
            return false;
        }
        this.mOutFilepath = recordFile;
        LogUtil.d(TAG, "mOutFilepath is " + this.mOutFilepath);
        if (this.streamCtrl.startRecordFile(this.mOutFilepath, 0)) {
            return true;
        }
        File file = new File(this.mOutFilepath);
        if (file.exists()) {
            boolean isSuc = file.delete();
            LogUtil.i(TAG, "try to delete old record file " + isSuc);
        }
        return false;
    }

    public boolean stopLocalRecordEx() {
        if (this.streamCtrl == null) {
            return false;
        }
        this.streamCtrl.stopRecordFile(0);
        File file = new File(this.mOutFilepath);
        if (file.exists()) {
            if (this.mStreamDownloadCallback != null) {
                this.mStreamDownloadCallback.onSuccess(this.mOutFilepath);
            }
        } else {
            if (this.mStreamDownloadCallback != null) {
                this.mStreamDownloadCallback.onError(EZOpenSDKListener.EZStreamDownloadError.ERROR_EZSTREAM_DOWNLOAD_STOP);
            }
            return false;
        }
        return true;
    }

    public boolean setWaterMarkFont(EZPlayWaterMarkConfig waterMarkConfig) {
        if (this.streamCtrl == null) {
            return false;
        }
        return this.streamCtrl.setWaterMarkFont(waterMarkConfig);
    }

    public void clearWaterMarkFont() {
        if (this.streamCtrl == null) {
            return;
        }
        this.streamCtrl.clearWaterMarkFont();
    }

    public Bitmap captureRenderPicture(int width, int height) {
        File tmpPicFile;
        Bitmap targetBitmap = null;
        String tmpPic = PlayAPI.mApplication.getExternalCacheDir() + "/0_OpenSDK/tmp/" + System.currentTimeMillis() + ".tmp";
        int ret = this.captureRenderPicture(tmpPic, width, height);
        LogUtil.d(TAG, "ret of captureRenderPicture: " + ret);
        if (ret == 0) {
            targetBitmap = BitmapFactory.decodeFile((String)tmpPic);
        }
        if ((tmpPicFile = new File(tmpPic)).exists()) {
            LogUtil.d(TAG, "delete tmpPic: " + tmpPicFile.delete());
        }
        return targetBitmap;
    }

    public int captureRenderPicture(String fileNameWithPath, int width, int height) {
        File filepath = new File(fileNameWithPath);
        File parent = filepath.getParentFile();
        if (parent != null && (!parent.exists() || parent.isFile())) {
            parent.mkdirs();
        }
        if (this.mLanPlayer != null) {
            return this.mLanPlayer.captureWithRender(fileNameWithPath, 0, width, height);
        }
        if (this.streamCtrl == null) {
            return -1;
        }
        return this.streamCtrl.captureRenderPicture(fileNameWithPath, 0, width, height);
    }

    public boolean startRenderRecordWithFile(String recordFile) {
        File out;
        if (this.streamCtrl == null) {
            return false;
        }
        this.mOutFilepath = recordFile;
        this.mOutTempFilepath = recordFile + "_temp";
        LogUtil.d(TAG, "mOutFilepath is " + recordFile);
        boolean ret = this.streamCtrl.startRenderRecordFile(this.mOutTempFilepath);
        if (ret) {
            return true;
        }
        File file = new File(this.mOutTempFilepath);
        if (file.exists()) {
            file.delete();
        }
        if ((out = new File(this.mOutFilepath)).exists()) {
            out.delete();
        }
        return false;
    }

    public boolean stopRenderRecord() {
        LogUtil.d(TAG, "stopRenderRecord");
        if (this.streamCtrl == null) {
            return false;
        }
        this.streamCtrl.stopRendeRecordFile();
        VideoTransUtil.TransPsToMp4(this.mOutTempFilepath, this.mVerifyCode, this.mOutFilepath, false, this.mStreamDownloadCallback);
        return true;
    }

    public synchronized void setHardDecode(boolean enable) {
        this.mPlayConfig.isHardDecodeFirst = enable;
    }

    public int getDecodeType() {
        return Player.getInstance().getDecoderType(this.getPlayPort());
    }

    public int getPlayPort() {
        if (this.streamCtrl != null) {
            return this.streamCtrl.getPlayPort();
        }
        return -1;
    }

    public Bitmap capturePicture() {
        return this.capturePicture(0);
    }

    public Bitmap capturePicture(int streamId) {
        File tmpPicFile;
        Bitmap targetBitmap = null;
        String tmpPic = PlayAPI.mApplication.getExternalCacheDir() + "/0_OpenSDK/tmp/" + System.currentTimeMillis() + ".tmp";
        int ret = this.capturePicture(tmpPic, streamId);
        LogUtil.d(TAG, "ret of capturePicture: " + ret);
        if (ret == 0) {
            targetBitmap = BitmapFactory.decodeFile((String)tmpPic);
        }
        if ((tmpPicFile = new File(tmpPic)).exists()) {
            LogUtil.d(TAG, "delete tmpPic: " + tmpPicFile.delete());
        }
        return targetBitmap;
    }

    public int capturePicture(String fileNameWithPath) {
        return this.capturePicture(fileNameWithPath, 0);
    }

    public int capturePicture(String fileNameWithPath, int streamId) {
        File filepath = new File(fileNameWithPath);
        File parent = filepath.getParentFile();
        if (parent != null && (!parent.exists() || parent.isFile())) {
            parent.mkdirs();
        }
        if (this.mLanPlayer != null) {
            return this.mLanPlayer.capture(fileNameWithPath, streamId);
        }
        if (this.streamCtrl == null) {
            return -1;
        }
        return this.streamCtrl.capturePicture(fileNameWithPath, streamId);
    }

    public int getStreamFetchType() {
        if (this.streamCtrl == null) {
            return -1;
        }
        return this.streamCtrl.getStreamFetchType();
    }

    public long getStreamFlow() {
        if (this.mLanPlayer != null) {
            return this.mLanPlayer.getStreamFlow();
        }
        if (this.streamCtrl == null) {
            return 0L;
        }
        return this.streamCtrl.getStreamFlow();
    }

    public EZDeviceDetailPublicInfo getDeviceDetailInfo() {
        if (this.streamCtrl == null) {
            return null;
        }
        return this.streamCtrl.getDeviceDetailInfo();
    }

    public boolean setIntelAnalysis(boolean enable) {
        if (this.streamCtrl == null) {
            return false;
        }
        return this.streamCtrl.setIntelAnalysis(enable);
    }

    public void setPlayerDisableP2P() {
        if (this.paramHelp != null) {
            this.paramHelp.isPlayerDisableP2P = true;
        }
    }

    public void enableDeviceAutoVideoLevel() {
        if (this.paramHelp != null) {
            this.paramHelp.isAutoDeviceVideoLevel = true;
        }
    }

    public void setVideoQuality(EZConstants.EZVideoQuality videoQuality) {
        if (this.paramHelp != null) {
            this.paramHelp.videoQuality = videoQuality;
        }
    }

    public void setStreamToken(String streamToken) {
        if (this.paramHelp != null) {
            this.paramHelp.streamToken = streamToken;
            LogUtil.i(TAG, String.format("[\u9884\u64cd\u4f5c]\u8bbe\u7f6estreamToken(%s)", streamToken));
        }
    }

    public void setPlayerViewRotation(EZConstants.EZPlayerViewRotationAngle rotationAngle) {
        if (this.streamCtrl == null) {
            return;
        }
        this.streamCtrl.setPlayerViewRotation(rotationAngle);
    }

    public boolean setDisplayRegion(long left, long top, long right, long bottom) {
        return this.setDisplayRegion(left, top, right, bottom, 0);
    }

    public boolean setDisplayRegion(long left, long top, long right, long bottom, int streamId) {
        if (this.streamCtrl == null) {
            LogUtil.w(TAG, "mediaPlayer is null");
            return false;
        }
        return this.streamCtrl.setDisplayRegion(left, top, right, bottom, streamId);
    }

    @Deprecated
    public void setDisplayRegion(boolean enable, CustomRect original, CustomRect current) throws PlaySDKException, InnerException {
        this.setDisplayRegion(enable, original, current, 0);
    }

    @Deprecated
    public void setDisplayRegion(boolean enable, CustomRect original, CustomRect current, int streamId) throws PlaySDKException, InnerException {
        if (this.streamCtrl == null) {
            return;
        }
        this.streamCtrl.setDisplayRegion(enable, original, current, streamId);
    }

    public void setAudioOnly(boolean audioOnly) {
        this.isAudioOnly = audioOnly;
    }

    public void setSpeakerphoneOn(boolean on) {
        if (this.streamCtrl != null) {
            this.streamCtrl.setSpeakerphoneOn(on);
        }
    }

    public boolean isSpeakerphoneOn() {
        boolean isSpeakerphoneOn = true;
        if (this.streamCtrl != null) {
            isSpeakerphoneOn = this.streamCtrl.isSpeakerphoneOn();
        }
        return isSpeakerphoneOn;
    }

    public synchronized void setOriginDataCallback(EZOpenSDKListener.OriginDataCallback callback) {
        this.mPlayConfig.mOriginDataCallback = callback;
    }

    public boolean tryTransPsToMp4(String psFilePath, String mp4FilePath) {
        return this.tryTransPsToMp4(psFilePath, mp4FilePath, null);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean tryTransPsToMp4(String psFilePath, String mp4FilePath, String verifyCode) {
        boolean finishedToTrans = false;
        SystemTransformSim trans = null;
        try {
            trans = SystemTransformSim.create((int)5, (String)psFilePath, (String)mp4FilePath);
            int errCode = trans.start(verifyCode);
            if (errCode != 0) {
                LogUtil.e(TAG, "failed to start trans, error code is " + errCode);
                boolean bl = false;
                return bl;
            }
            boolean notOccurredError = true;
            do {
                EZGetPercentInfo percent = trans.getPercent();
                LogUtil.d(TAG, "percent of trans is " + percent.percent);
                if (percent.ret == 4096) {
                    LogUtil.e(TAG, "unexpected resolution");
                    notOccurredError = false;
                }
                if (percent.ret != 0 || percent.percent == -1) {
                    LogUtil.e(TAG, "unexpected error");
                    notOccurredError = false;
                }
                if (percent.percent == 100) {
                    finishedToTrans = true;
                }
                Thread.sleep(100L);
            } while (!finishedToTrans && notOccurredError);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (trans != null) {
                trans.stop();
                trans.release();
            }
        }
        return finishedToTrans;
    }

    public void getLeaveMessageData(EZLeaveMessage msg, EZOpenSDKListener.EZLeaveMessageFlowCallback leaveMessageFlowCallback) {
        if (this.streamCtrl == null) {
            return;
        }
        this.streamCtrl.startLeaveMsgDownload(msg);
    }

    public void setLeaveMessageFlowCallback(EZOpenSDKListener.EZLeaveMessageFlowCallback leaveMessageFlowCallback) {
        if (this.streamCtrl == null) {
            return;
        }
        this.streamCtrl.setLeaveMsgCallback(leaveMessageFlowCallback);
    }

    public void refreshPlay() {
        this.refreshPlay(0);
    }

    public void refreshPlay(int streamId) {
        if (this.streamCtrl == null) {
            return;
        }
        this.streamCtrl.refreshPlay(streamId);
    }

    public void setDemuxModel(int demuxModel) {
        if (this.streamCtrl == null) {
            return;
        }
        this.streamCtrl.setDemuxModel(demuxModel);
    }

    public void enableSmoothPlay(EZConstants.EZSmoothPlayMode smoothPlayMode) {
        this.mPlayConfig.enableSmoothPlay = true;
        this.mPlayConfig.smoothPlayMode = smoothPlayMode.mode;
    }

    public void setIPCRecordDirectQuery(boolean isIPCRecordDirectQuery) {
        if (this.streamCtrl == null) {
            return;
        }
        if (this.paramHelp != null) {
            this.paramHelp.isIPCRecordDirectQuery = isIPCRecordDirectQuery;
        }
    }

    public boolean setSurfaceHolds(SurfaceHolder[] surfaceHolders) {
        this.mSurfaceHolders = surfaceHolders;
        this.streamCtrl.setSurfaceHolds(surfaceHolders);
        return true;
    }

    public void openFecCorrect(EZConstants.EZFecCorrectType fecCorrectType, EZConstants.EZFecPlaceType fecPlaceType) {
        this.streamCtrl.openFecCorrect(fecCorrectType, fecPlaceType);
    }

    public boolean onFecTouchDown(int index, float x, float y) {
        return this.streamCtrl.onFecTouchDown(index, x, y);
    }

    public void onFecTouchMove(int index, float x, float y) {
        this.streamCtrl.onFecTouchMove(index, x, y);
    }

    public void onFecTouchUp(int index) {
        this.streamCtrl.onFecTouchUp(index);
    }

    public void onFecTouchStartScale(int index, float distance) {
        this.streamCtrl.onFecTouchStartScale(index, distance);
    }

    public void onFecTouchScale(int index, float scale, float maxScale) {
        this.streamCtrl.onFecTouchScale(index, scale, maxScale);
    }

    public EZPlayer(int userId, int cameraNo, int streamType) {
        this.cachedThreadPool = Executors.newSingleThreadExecutor();
        InitParam initParam = new InitParam();
        initParam.iNetSDKUserId = userId;
        initParam.iNetSDKChannelNumber = cameraNo;
        initParam.iStreamType = streamType;
        initParam.iStreamTimeOut = 30000;
        this.mLanPlayer = new EZMediaPlayerEx(EZStreamClientManager.create((Context)PlayAPI.mApplication.getApplicationContext()), initParam);
        if (this.mPlayConfig != null) {
            ((EZMediaPlayerEx)this.mLanPlayer).setPlayConfig(this.mPlayConfig);
        }
        this.mLanPlayer.setCompletionListener((EZMediaPlayer.OnCompletionListener)this);
        this.mLanPlayer.setOnErrorListener((EZMediaPlayer.OnErrorListener)this);
        this.mLanPlayer.setOnInfoListener((EZMediaPlayer.OnInfoListener)this);
    }

    public void onCompletion(EZMediaPlayer mp) {
        LogUtil.d(TAG, "stop success");
        this.sendMessage(201, 0, null);
    }

    public boolean onError(EZMediaPlayer mp, EZMediaPlayer.MediaError error, int errorCode) {
        LogUtil.w(TAG, "mediaplayer onError. error is " + error + ", error code is " + errorCode);
        if (EZMediaPlayer.MediaError.MEDIA_ERROR_TIMEOUT == error) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400034);
            this.sendMessage(103, errorInfo.errorCode, errorInfo);
        } else {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(31, errorCode);
            this.sendMessage(103, errorInfo.errorCode, errorInfo);
        }
        return false;
    }

    public boolean onInfo(EZMediaPlayer mp, EZMediaPlayer.MediaInfo info, int index) {
        LogUtil.w(TAG, "mediaplayer onInfo. info is " + info);
        if (EZMediaPlayer.MediaInfo.MEDIA_INFO_VIDEO_SIZE_CHANGED == info) {
            this.sendMessage(134, 0, new StringBuffer().append(mp.getVideoWidth()).append(":").append(mp.getVideoHeight()).toString());
            this.sendMessage(102, 0, null);
        }
        return true;
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

    private static interface EZPlayCallBack {
        public void handlePlaySuccess();

        public void handlePlayFail(ErrorInfo var1);

        public void handleVideoSizeChange(int var1, int var2);
    }
}


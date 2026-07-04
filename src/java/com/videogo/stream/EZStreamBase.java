/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.SurfaceTexture
 *  android.os.Handler
 *  android.os.Message
 *  android.os.SystemClock
 *  android.text.TextUtils
 *  android.view.Surface
 *  android.view.SurfaceHolder
 *  com.ez.player.EZMediaPlayer
 *  com.ez.player.EZMediaPlayer$EZOSDTime
 *  com.ez.player.EZMediaPlayer$EZPlayerPrivateInfo
 *  com.ez.player.EZMediaPlayer$MediaError
 *  com.ez.player.EZMediaPlayer$MediaInfo
 *  com.ez.player.EZMediaPlayer$OnCompletionListener
 *  com.ez.player.EZMediaPlayer$OnConvertDataCallback
 *  com.ez.player.EZMediaPlayer$OnEZAdditionalInfoListener
 *  com.ez.player.EZMediaPlayer$OnErrorListener
 *  com.ez.player.EZMediaPlayer$OnInfoListener
 *  com.ez.statistics.BaseStreamStatistics
 *  com.ez.statistics.CloudPlaybackStatistics
 *  com.ez.statistics.DirectPlaybackStatistics
 *  com.ez.statistics.DirectPreviewStatistics
 *  com.ez.statistics.P2PPlaybackStatistics
 *  com.ez.statistics.P2PPreviewStatistics
 *  com.ez.statistics.PrivateStreamPlaybackStatistics
 *  com.ez.statistics.PrivateStreamPreviewStatistics
 *  com.ez.stream.EZStreamClient
 *  com.ez.stream.EZStreamClientManager
 *  com.ez.stream.InitParam
 *  com.ez.stream.VideoStreamInfo
 *  org.MediaPlayer.PlayM4.Player$MPRect
 */
package com.videogo.stream;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.Surface;
import android.view.SurfaceHolder;
import com.ez.player.EZMediaPlayer;
import com.ez.statistics.BaseStreamStatistics;
import com.ez.statistics.CloudPlaybackStatistics;
import com.ez.statistics.DirectPlaybackStatistics;
import com.ez.statistics.DirectPreviewStatistics;
import com.ez.statistics.P2PPlaybackStatistics;
import com.ez.statistics.P2PPreviewStatistics;
import com.ez.statistics.PrivateStreamPlaybackStatistics;
import com.ez.statistics.PrivateStreamPreviewStatistics;
import com.ez.stream.EZStreamClient;
import com.ez.stream.EZStreamClientManager;
import com.ez.stream.InitParam;
import com.ez.stream.VideoStreamInfo;
import com.videogo.constant.Config;
import com.videogo.device.DeviceManager;
import com.videogo.errorlayer.ErrorInfo;
import com.videogo.errorlayer.ErrorLayer;
import com.videogo.exception.BaseException;
import com.videogo.ezdclog.EZDcLogManager;
import com.videogo.ezdclog.params.BaseParams;
import com.videogo.ezdclog.params.BaseStreamStatisticFake;
import com.videogo.ezdclog.params.EZLogStreamBaseParams;
import com.videogo.ezdclog.params.EZLogStreamClientParams;
import com.videogo.ezdclog.params.EZLogStreamCloudParams;
import com.videogo.ezdclog.params.EZLogStreamLandirectParams;
import com.videogo.ezdclog.params.EZLogStreamP2pParams;
import com.videogo.ezdclog.params.EZLogStreamUpnpdirectParams;
import com.videogo.ezdclog.params.EZLogStreamVTDUParams;
import com.videogo.main.AppManager;
import com.videogo.openapi.ConfigLoader;
import com.videogo.openapi.EZConstants;
import com.videogo.openapi.EZMediaPlayerEx;
import com.videogo.openapi.EZOpenSDKListener;
import com.videogo.openapi.PlayAPI;
import com.videogo.openapi.bean.EZDevicePtzAngleInfo;
import com.videogo.openapi.bean.EZPMPlayPrivateTokenInfo;
import com.videogo.realplay.EZRealPlayDataCtrl;
import com.videogo.stream.EZP2PPermissionManager;
import com.videogo.stream.EZPlayWaterMarkConfig;
import com.videogo.stream.EZStreamParamHelp;
import com.videogo.util.JsonTools;
import com.videogo.util.LogUtil;
import com.videogo.util.MD5Util;
import com.videogo.util.PtzUtils;
import com.videogo.widget.CustomRect;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import org.MediaPlayer.PlayM4.Player;

public abstract class EZStreamBase
implements EZMediaPlayer.OnCompletionListener,
EZMediaPlayer.OnErrorListener,
EZMediaPlayer.OnInfoListener,
EZMediaPlayer.OnEZAdditionalInfoListener {
    protected final String TAG = "EZStreamBase";
    protected boolean isStop = false;
    protected EZMediaPlayer mediaPlayer = null;
    protected EZStreamClient streamClient = null;
    protected InitParam initParam = null;
    protected EZStreamParamHelp streamParamHelp;
    protected EZRealPlayDataCtrl dataCtrl = null;
    protected final Calendar mOSDTime = new GregorianCalendar();
    protected int streamSource = 0;
    SurfaceHolder mSurfaceHolder = null;
    SurfaceHolder mSurfaceHolder2 = null;
    SurfaceTexture mSurfaceTexture = null;
    SurfaceTexture mSurfaceTexture2 = null;
    Handler handler = null;
    private EZOpenSDKListener.EZStandardFlowCallback mStandardFlow = null;
    String secretKey;
    private boolean isPlaySuccess = false;
    public EZLogStreamClientParams mEZLogStreamClientParams;
    public ConfigLoader.PlayConfig playConfig;
    protected boolean isSeeking = false;
    protected Calendar seekTime;
    private EZMediaPlayer.OnConvertDataCallback convertDataCallbackFun = new EZMediaPlayer.OnConvertDataCallback(){

        public void onConvertData(int dataType, byte[] data, int dataLen) {
            if (EZStreamBase.this.mStandardFlow != null) {
                EZStreamBase.this.mStandardFlow.onStandardFlowCallback(dataType, data, dataLen);
            }
        }
    };

    public EZLogStreamClientParams getEZLogStreamClientParams() {
        if (this.mEZLogStreamClientParams == null) {
            this.mEZLogStreamClientParams = new EZLogStreamClientParams();
            this.mEZLogStreamClientParams.dnt = AppManager.getInstance().getNatType();
            this.mEZLogStreamClientParams.cnt = this.streamParamHelp.deviceInfo != null ? this.streamParamHelp.deviceInfo.getNetType() : -1;
            this.mEZLogStreamClientParams.channel = this.streamParamHelp.cameraNo;
            this.mEZLogStreamClientParams.serial = this.streamParamHelp.deviceSerial;
            int n = this.mEZLogStreamClientParams.enc = this.streamParamHelp.deviceInfo == null ? 0 : this.streamParamHelp.deviceInfo.getIsEncrypt();
            if (this.initParam != null) {
                this.mEZLogStreamClientParams.vdLv = this.streamParamHelp.getDClogVideoLevel();
            }
        }
        return this.mEZLogStreamClientParams;
    }

    public void setEZLogStreamClientParams(EZLogStreamClientParams mEZLogStreamClientParams) {
        this.mEZLogStreamClientParams = mEZLogStreamClientParams;
    }

    public void submitVia(BaseStreamStatisticFake statisticFake) {
        if (statisticFake == null) {
            LogUtil.e("EZStreamBase", "submitVia: occur exception!");
            return;
        }
        EZLogStreamBaseParams ezLogStreamParams = null;
        BaseStreamStatistics streamStatistic = null;
        switch (statisticFake.via) {
            case 2: {
                ezLogStreamParams = new EZLogStreamVTDUParams();
                streamStatistic = (BaseStreamStatistics)JsonTools.fromJson(statisticFake.originInfo, PrivateStreamPreviewStatistics.class);
                break;
            }
            case 12: {
                ezLogStreamParams = new EZLogStreamVTDUParams();
                streamStatistic = (BaseStreamStatistics)JsonTools.fromJson(statisticFake.originInfo, PrivateStreamPlaybackStatistics.class);
                break;
            }
            case 0: 
            case 21: {
                ezLogStreamParams = new EZLogStreamLandirectParams();
                streamStatistic = (BaseStreamStatistics)JsonTools.fromJson(statisticFake.originInfo, DirectPreviewStatistics.class);
                break;
            }
            case 10: {
                ezLogStreamParams = new EZLogStreamLandirectParams();
                streamStatistic = (BaseStreamStatistics)JsonTools.fromJson(statisticFake.originInfo, DirectPlaybackStatistics.class);
                break;
            }
            case 1: 
            case 22: {
                ezLogStreamParams = new EZLogStreamUpnpdirectParams();
                streamStatistic = (BaseStreamStatistics)JsonTools.fromJson(statisticFake.originInfo, DirectPreviewStatistics.class);
                break;
            }
            case 11: {
                ezLogStreamParams = new EZLogStreamUpnpdirectParams();
                streamStatistic = (BaseStreamStatistics)JsonTools.fromJson(statisticFake.originInfo, DirectPlaybackStatistics.class);
                break;
            }
            case 7: 
            case 25: {
                ezLogStreamParams = new EZLogStreamP2pParams();
                streamStatistic = (BaseStreamStatistics)JsonTools.fromJson(statisticFake.originInfo, P2PPreviewStatistics.class);
                break;
            }
            case 8: 
            case 26: {
                ezLogStreamParams = new EZLogStreamP2pParams();
                streamStatistic = (BaseStreamStatistics)JsonTools.fromJson(statisticFake.originInfo, P2PPlaybackStatistics.class);
                break;
            }
            case 14: {
                ezLogStreamParams = new EZLogStreamCloudParams();
                streamStatistic = (BaseStreamStatistics)JsonTools.fromJson(statisticFake.originInfo, CloudPlaybackStatistics.class);
                break;
            }
        }
        if (ezLogStreamParams != null && streamStatistic != null) {
            ezLogStreamParams.opId = this.mediaPlayer.getUUID();
            ezLogStreamParams.cost = streamStatistic.t;
            ezLogStreamParams.err = streamStatistic.r;
            EZDcLogManager.getInstance().submit((BaseParams)ezLogStreamParams, streamStatistic);
        }
    }

    public void submitMainStreamStatistics() {
        LogUtil.d("EZStreamBase", "submitMainStreamStatistics begin");
        String strStatistic = this.mediaPlayer.getRootStatistics();
        if (strStatistic != null && !TextUtils.isEmpty((CharSequence)this.getEZLogStreamClientParams().opId)) {
            BaseStreamStatistics mainStreamStatistic = JsonTools.fromJson(strStatistic, BaseStreamStatistics.class);
            if (mainStreamStatistic != null) {
                LogUtil.d("EZStreamBase", "mainStatistic.via = " + mainStreamStatistic.via + ",mainStatistic._startTime = " + mainStreamStatistic._startTime + ",mainStatistic._endTime = " + mainStreamStatistic._endTime);
                this.getEZLogStreamClientParams().via = mainStreamStatistic.via;
                EZDcLogManager.getInstance().submit(this.getEZLogStreamClientParams());
                this.setEZLogStreamClientParams(null);
            } else {
                LogUtil.e("EZStreamBase", "submitMainStreamStatistics: occur exception!");
            }
        }
        LogUtil.d("EZStreamBase", "submitMainStreamStatistics end");
    }

    public void submitSubStreamStatistics() {
        LogUtil.d("EZStreamBase", "submitSubStreamStatistics begin");
        String[] statisticArray = this.mediaPlayer.getSubStatistics();
        if (statisticArray == null) {
            LogUtil.e("EZStreamBase", "submitSubStreamStatistics: occur exception!");
            return;
        }
        for (String strStatistic : statisticArray) {
            if (strStatistic == null) continue;
            BaseStreamStatisticFake subStreamStatisticFake = JsonTools.fromJson(strStatistic, BaseStreamStatisticFake.class);
            subStreamStatisticFake.originInfo = strStatistic;
            LogUtil.d("EZStreamBase", "subStreamStatisticFake.via = " + subStreamStatisticFake.via + "\n subStreamStatisticFake.originInfo = \n");
            this.submitVia(subStreamStatisticFake);
        }
        LogUtil.d("EZStreamBase", "submitSubStreamStatistics end");
    }

    public EZStreamBase(EZStreamParamHelp streamParamHelp, ConfigLoader.PlayConfig playConfig) {
        this.streamParamHelp = streamParamHelp;
        this.dataCtrl = new EZRealPlayDataCtrl();
        this.playConfig = playConfig;
    }

    public void release() {
        if (this.mediaPlayer != null) {
            this.mediaPlayer.setDisplay(null);
            this.mediaPlayer.release();
            this.setSurfaceHold(null);
            this.setSurfaceEx(null);
        }
    }

    protected abstract boolean start();

    private boolean prepare() {
        if (!this.initInitParam()) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400032);
            this.handlePlayerFailed(errorInfo);
            return false;
        }
        if (!this.createEZMediaPlayer()) {
            return false;
        }
        if (!this.checkKey()) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400036);
            this.handlePlayerFailed(errorInfo);
            return false;
        }
        if (!this.checkP2PPermisionIfNecessary()) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400907);
            this.handlePlayerFailed(errorInfo);
            return false;
        }
        if (this.playConfig != null) {
            ((EZMediaPlayerEx)this.mediaPlayer).setPlayConfig(this.playConfig);
        }
        if (this.mSurfaceHolder != null) {
            this.mediaPlayer.setDisplay(this.mSurfaceHolder);
        }
        if (this.mSurfaceHolder2 != null) {
            this.mediaPlayer.setDisplay(this.mSurfaceHolder2, 1);
        }
        if (this.mSurfaceTexture != null) {
            this.mediaPlayer.setDisplayEx(this.mSurfaceTexture);
        }
        if (this.mSurfaceTexture2 != null) {
            this.mediaPlayer.setDisplayEx(this.mSurfaceTexture2, 1);
        }
        if (this.streamParamHelp != null && this.streamParamHelp.deviceInfo.isSupportMultiChannel()) {
            this.mediaPlayer.setStreamCount(2);
        }
        this.mediaPlayer.setSecretKey(this.secretKey);
        this.mediaPlayer.setCompletionListener((EZMediaPlayer.OnCompletionListener)this);
        this.mediaPlayer.setOnErrorListener((EZMediaPlayer.OnErrorListener)this);
        this.mediaPlayer.setOnInfoListener((EZMediaPlayer.OnInfoListener)this);
        this.mediaPlayer.setOnEZInfoListener((EZMediaPlayer.OnEZAdditionalInfoListener)this);
        this.getEZLogStreamClientParams().start_t = System.currentTimeMillis();
        return true;
    }

    public boolean startRealPlay() {
        boolean ret = this.prepare();
        if (ret) {
            this.mediaPlayer.start();
        }
        return ret;
    }

    public boolean startPlayback(List<VideoStreamInfo> videoStreamInfoList) {
        boolean ret = this.prepare();
        if (ret) {
            this.mediaPlayer.startPlayback(videoStreamInfoList);
        }
        return ret;
    }

    public void stop() {
        if (this.streamParamHelp != null) {
            this.streamParamHelp.removeCycleVerifyPermissionCallback();
        }
        if (this.mediaPlayer == null) {
            return;
        }
        this.mediaPlayer.stop();
        this.getEZLogStreamClientParams().stop_t = System.currentTimeMillis();
        this.submitMainStreamStatistics();
        this.submitSubStreamStatistics();
    }

    public void setSurfaceHold(SurfaceHolder sh) {
        this.setSurfaceHold(sh, 0);
    }

    public void setSurfaceHold(SurfaceHolder sh, int streamId) {
        if (streamId == 0) {
            if (this.mSurfaceHolder != null && sh == this.mSurfaceHolder) {
                return;
            }
            this.mSurfaceHolder = sh;
            if (this.mediaPlayer != null) {
                this.mediaPlayer.setDisplay(this.mSurfaceHolder, streamId);
            }
        } else if (streamId == 1) {
            if (this.mSurfaceHolder2 != null && sh == this.mSurfaceHolder2) {
                return;
            }
            this.mSurfaceHolder2 = sh;
            if (this.mediaPlayer != null) {
                this.mediaPlayer.setDisplay(this.mSurfaceHolder2, streamId);
            }
        }
    }

    public void setSurfaceEx(SurfaceTexture sh) {
        this.setSurfaceEx(sh, 0);
    }

    public void setSurfaceEx(SurfaceTexture sh, int streamId) {
        if (streamId == 0) {
            if (this.mSurfaceTexture != null && sh == this.mSurfaceTexture) {
                return;
            }
            this.mSurfaceTexture = sh;
            if (this.mediaPlayer != null) {
                this.mediaPlayer.setDisplayEx(sh, streamId);
            }
        } else if (streamId == 1) {
            if (this.mSurfaceTexture2 != null && sh == this.mSurfaceTexture2) {
                return;
            }
            this.mSurfaceTexture2 = sh;
            if (this.mediaPlayer != null) {
                this.mediaPlayer.setDisplayEx(sh, streamId);
            }
        }
    }

    public void refreshPlay(int streamId) {
        if (this.mediaPlayer != null) {
            this.mediaPlayer.refreshPlay(streamId);
        }
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void setStreamSource(int streamSource) {
        this.streamSource = streamSource;
    }

    public int getStreamSource() {
        return this.streamSource;
    }

    private boolean initInitParam() {
        try {
            this.initParam = this.streamParamHelp.getInitParam(this.streamSource);
            if (this.initParam == null) {
                LogUtil.d("EZStreamBase", "STREAMBASE. initParam is null");
            }
            if (this.initParam != null) {
                this.getEZLogStreamClientParams().vdLv = this.streamParamHelp.getDClogVideoLevel();
                LogUtil.d("EZStreamBase", "STREAMBASE. initParam is " + this.initParam.toString());
            }
        }
        catch (BaseException e) {
            LogUtil.printErrStackTrace("EZStreamBase", e.fillInStackTrace());
        }
        return this.initParam != null;
    }

    protected boolean setDisPlay() {
        int nCount;
        for (nCount = 0; !(this.mSurfaceHolder != null && this.mSurfaceHolder.getSurface() != null && this.mSurfaceHolder.getSurface().isValid() || this.mSurfaceTexture != null || nCount >= 100); ++nCount) {
            LogUtil.w("EZStreamBase", "waiting for surfaceview not create.");
            SystemClock.sleep((long)50L);
        }
        if (nCount == 100) {
            return false;
        }
        if (this.mSurfaceHolder != null) {
            this.mediaPlayer.setDisplay(this.mSurfaceHolder);
        } else if (this.mSurfaceTexture != null) {
            this.mediaPlayer.setDisplayEx(this.mSurfaceTexture);
        }
        return true;
    }

    public void setPlayKey(String playKey) {
        this.secretKey = playKey;
    }

    public int capturePicture(String fileNameWithPath, int streamId) {
        if (this.mediaPlayer == null) {
            return -1;
        }
        return this.mediaPlayer.capture(fileNameWithPath, streamId);
    }

    public boolean startRecordFile(String recordFile, int streamId) {
        if (this.mediaPlayer == null) {
            return false;
        }
        return this.mediaPlayer.startRecordingEx(recordFile, streamId);
    }

    public void stopRecordFile(int streamId) {
        if (this.mediaPlayer == null) {
            return;
        }
        this.mediaPlayer.stopRecordingEx(streamId);
    }

    public boolean setWaterMarkFont(EZPlayWaterMarkConfig waterMarkConfig) {
        if (this.mediaPlayer == null) {
            return false;
        }
        int ret = this.mediaPlayer.setWaterMarkFont(waterMarkConfig.convert2EZPlayWaterMarkInfo());
        if (ret != 0) {
            LogUtil.e("EZStreamBase", "setWaterMarkFont failed");
        }
        return ret == 0;
    }

    public void clearWaterMarkFont() {
        if (this.mediaPlayer == null) {
            return;
        }
        int ret = this.mediaPlayer.setWaterMarkFont(null);
        LogUtil.e("EZStreamBase", "clearWaterMarkFont");
    }

    public int captureRenderPicture(String fileNameWithPath, int streamId, int width, int height) {
        if (this.mediaPlayer == null) {
            return -1;
        }
        return this.mediaPlayer.captureWithRender(fileNameWithPath, streamId, width, height);
    }

    public boolean startRenderRecordFile(String recordFile) {
        if (this.mediaPlayer == null) {
            return false;
        }
        return this.mediaPlayer.startRenderRecording(recordFile) == 0;
    }

    public void stopRenderRecordFile() {
        if (this.mediaPlayer == null) {
            return;
        }
        this.mediaPlayer.stopRenderRecording();
    }

    public int getPlayPort() {
        if (this.mediaPlayer == null) {
            return -1;
        }
        return this.mediaPlayer.getPort();
    }

    public boolean soundCtrl(boolean bOpen) {
        if (this.mediaPlayer == null) {
            return false;
        }
        if (bOpen) {
            this.mediaPlayer.playSound();
        } else {
            this.mediaPlayer.stopSound();
        }
        return true;
    }

    public Calendar getOSDTime() {
        if (this.mediaPlayer == null) {
            return null;
        }
        if (this.isSeeking) {
            return this.seekTime;
        }
        EZMediaPlayer.EZOSDTime ezosdTime = this.mediaPlayer.getOSDTime();
        if (ezosdTime == null || ezosdTime.year == 0) {
            return null;
        }
        this.mOSDTime.set(ezosdTime.year, ezosdTime.month - 1, ezosdTime.day, ezosdTime.hour, ezosdTime.min, ezosdTime.sec);
        return this.mOSDTime;
    }

    public boolean setDisplayRegion(long left, long top, long right, long bottom, int streamId) {
        if (this.mediaPlayer == null) {
            LogUtil.w("EZStreamBase", "mediaPlayer is null");
            return false;
        }
        if (streamId == 0) {
            if (this.mSurfaceHolder != null) {
                return this.mediaPlayer.setDisplayRegion(0, this.mSurfaceHolder.getSurface(), left, top, right, bottom);
            }
            if (this.mSurfaceTexture != null) {
                return this.mediaPlayer.setDisplayRegion(0, new Surface(this.mSurfaceTexture), left, top, right, bottom);
            }
        } else if (streamId == 1) {
            if (this.mSurfaceHolder2 != null) {
                return this.mediaPlayer.setDisplayRegion(0, this.mSurfaceHolder2.getSurface(), left, top, right, bottom, streamId);
            }
            if (this.mSurfaceTexture2 != null) {
                return this.mediaPlayer.setDisplayRegion(0, new Surface(this.mSurfaceTexture2), left, top, right, bottom, streamId);
            }
        }
        return false;
    }

    public void setDisplayRegion(boolean enable, CustomRect original, CustomRect current, int streamId) {
        if (this.mediaPlayer == null) {
            LogUtil.w("EZStreamBase", "mediaPlayer is null");
            return;
        }
        if (!enable) {
            int origin = -1;
            if (streamId == 0) {
                if (this.mSurfaceHolder != null) {
                    this.mediaPlayer.setDisplayRegion(0, this.mSurfaceHolder.getSurface(), (long)origin, (long)origin, (long)origin, (long)origin);
                }
                if (this.mSurfaceTexture != null) {
                    this.mediaPlayer.setDisplayRegion(0, new Surface(this.mSurfaceTexture), (long)origin, (long)origin, (long)origin, (long)origin);
                }
            } else if (streamId == 1) {
                if (this.mSurfaceHolder2 != null) {
                    this.mediaPlayer.setDisplayRegion(0, this.mSurfaceHolder2.getSurface(), (long)origin, (long)origin, (long)origin, (long)origin, streamId);
                }
                if (this.mSurfaceTexture2 != null) {
                    this.mediaPlayer.setDisplayRegion(0, new Surface(this.mSurfaceTexture2), (long)origin, (long)origin, (long)origin, (long)origin, streamId);
                }
            }
        } else {
            int nMPw = this.mediaPlayer.getVideoWidth(streamId);
            int nMPh = this.mediaPlayer.getVideoHeight(streamId);
            float ratio = (float)((double)original.getWidth() * 1.0 / (double)current.getWidth());
            float w = ratio * (float)nMPw;
            float h = ratio * (float)nMPh;
            float left = (float)((double)((float)nMPw * Math.abs(current.getLeft() - original.getLeft())) * 1.0 / (double)current.getWidth());
            float top = (float)((double)((float)nMPh * Math.abs(current.getTop() - original.getTop())) * 1.0 / (double)current.getHeight());
            float right = left + w;
            float bottom = top + h;
            Player.MPRect oRect = new Player.MPRect();
            oRect.left = 0;
            oRect.top = 0;
            oRect.right = nMPw;
            oRect.bottom = nMPh;
            Player.MPRect cRect = new Player.MPRect();
            cRect.left = (int)left;
            cRect.top = (int)top;
            cRect.right = (int)right;
            cRect.bottom = (int)bottom;
            CustomRect.judgeRect(oRect, cRect);
            if (streamId == 0) {
                if (this.mSurfaceHolder != null && !this.mediaPlayer.setDisplayRegion(0, this.mSurfaceHolder.getSurface(), (long)cRect.left, (long)cRect.top, (long)cRect.right, (long)cRect.bottom)) {
                    LogUtil.e("EZStreamBase", "setDisplayRegion failed");
                } else if (this.mSurfaceTexture != null && !this.mediaPlayer.setDisplayRegion(0, new Surface(this.mSurfaceTexture), (long)cRect.left, (long)cRect.top, (long)cRect.right, (long)cRect.bottom)) {
                    LogUtil.e("EZStreamBase", "setDisplayRegion failed");
                }
            } else if (streamId == 1) {
                if (this.mSurfaceHolder2 != null && !this.mediaPlayer.setDisplayRegion(0, this.mSurfaceHolder2.getSurface(), (long)cRect.left, (long)cRect.top, (long)cRect.right, (long)cRect.bottom, streamId)) {
                    LogUtil.e("EZStreamBase", "setDisplayRegion failed");
                } else if (this.mSurfaceTexture2 != null && !this.mediaPlayer.setDisplayRegion(0, new Surface(this.mSurfaceTexture2), (long)cRect.left, (long)cRect.top, (long)cRect.right, (long)cRect.bottom, streamId)) {
                    LogUtil.e("EZStreamBase", "setDisplayRegion failed");
                }
            }
        }
    }

    protected synchronized void sendMessage(int msg, int arg1, Object obj) {
        try {
            if (this.handler != null && !this.isStop) {
                Message message = Message.obtain();
                message.what = msg;
                message.arg1 = arg1;
                message.obj = obj;
                this.handler.sendMessage(message);
            }
        }
        catch (Exception e) {
            LogUtil.printErrStackTrace("EZStreamBase", e.fillInStackTrace());
        }
    }

    protected abstract boolean createEZMediaPlayer();

    protected abstract void handlePlayerFailed(ErrorInfo var1);

    protected abstract void handlePlayPrepared();

    protected abstract void handlePlaySuccess();

    protected abstract void handlePlayFinished();

    protected abstract void handlePlayStart();

    protected abstract void handleVideoSizeChange(int var1, int var2);

    protected abstract void handlePlayAdditionalInfo(EZDevicePtzAngleInfo var1);

    protected abstract void handlePlayPrivateTokenInfo(EZPMPlayPrivateTokenInfo var1);

    private boolean resetTokens() throws BaseException {
        if (Config.ENABLE_SDK_TKTOKEN) {
            LogUtil.d("EZStreamBase", "stream with user's token, but there is no stream token");
            return false;
        }
        List<String> tokenList = DeviceManager.getInstance().getStreamToken();
        if (tokenList != null && tokenList.size() > 0) {
            EZStreamClientManager.create((Context)PlayAPI.mApplication.getApplicationContext()).setTokens(tokenList.toArray(new String[tokenList.size()]));
            return true;
        }
        return false;
    }

    public void onCompletion(EZMediaPlayer mp) {
        LogUtil.d("EZStreamBase", "stop success");
        this.handlePlayFinished();
    }

    public boolean onError(EZMediaPlayer mp, EZMediaPlayer.MediaError error, int errorCode) {
        LogUtil.w("EZStreamBase", "mediaplayer onError. error is " + error + ", error code is " + errorCode);
        if (!(Config.ENABLE_SDK_TKTOKEN || EZMediaPlayer.MediaError.MEDIA_ERROR_INVALID_TOKEN != error && errorCode != 8)) {
            EZStreamClientManager.create((Context)PlayAPI.mApplication).clearTokens();
            try {
                if (!this.resetTokens()) {
                    ErrorInfo errorInfo = ErrorLayer.getErrorLayer(31, errorCode);
                    this.handlePlayerFailed(errorInfo);
                    return true;
                }
            }
            catch (BaseException e) {
                ErrorInfo errorInfo = e.getErrorInfo();
                this.handlePlayerFailed(errorInfo);
                LogUtil.printErrStackTrace("EZStreamBase", e.fillInStackTrace());
                return true;
            }
            Thread thr = new Thread(new Runnable(){

                @Override
                public void run() {
                    if (EZStreamBase.this.mediaPlayer != null) {
                        EZStreamBase.this.mediaPlayer.start();
                    }
                }
            });
            thr.start();
        } else if (EZMediaPlayer.MediaError.MEDIA_ERROR_SECRET_KEY == error) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400036);
            this.handlePlayerFailed(errorInfo);
        } else if (EZMediaPlayer.MediaError.MEDIA_ERROR_OUTOF_MEMORY == error) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400300);
            this.handlePlayerFailed(errorInfo);
        } else if (EZMediaPlayer.MediaError.MEDIA_ERROR_TIMEOUT == error) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400034);
            this.handlePlayerFailed(errorInfo);
        } else if (errorCode == 17101 || errorCode == 17102 || errorCode == 18101 || errorCode == 18102 || errorCode == 19101 || errorCode == 19102) {
            LogUtil.i("EZStreamBase", "filtered errorCode from ezviz stream sdk: " + errorCode);
        } else {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(31, errorCode);
            this.handlePlayerFailed(errorInfo);
        }
        return true;
    }

    public boolean onInfo(EZMediaPlayer mp, EZMediaPlayer.MediaInfo info, int index) {
        LogUtil.w("EZStreamBase", "mediaplayer onInfo. info is " + info);
        if (this.mediaPlayer == null) {
            LogUtil.w("EZStreamBase", "mediaplayer onInfo. mediaPlayer is null");
            return false;
        }
        if (EZMediaPlayer.MediaInfo.MEDIA_INFO_PLAY_PREPARED == info) {
            this.handlePlayPrepared();
        } else if (EZMediaPlayer.MediaInfo.MEDIA_INFO_START_PLAYING == info) {
            this.handlePlayStart();
        } else if (EZMediaPlayer.MediaInfo.MEDIA_INFO_VIDEO_SIZE_CHANGED == info) {
            if (!this.isPlaySuccess) {
                this.isPlaySuccess = true;
                int streamType = this.mediaPlayer.getClientType();
                LogUtil.d("EZStreamBase", "play success. current play type is " + this.streamSource + ". stream type is " + streamType);
                this.sendMessage(300, streamType, null);
                this.handlePlaySuccess();
            } else {
                LogUtil.d("EZStreamBase", "EZPlayback. Has been playing a success");
            }
            this.handleVideoSizeChange(mp.getVideoWidth(), mp.getVideoHeight());
        } else if (EZMediaPlayer.MediaInfo.MEDIA_INFO_NEED_TOKENS == info) {
            try {
                this.resetTokens();
            }
            catch (BaseException e) {
                LogUtil.printErrStackTrace("EZStreamBase", e.fillInStackTrace());
            }
        }
        switch (info) {
            case MEDIA_INFO_CLOUD_LOWER_PLAY_SPEED: {
                LogUtil.i("EZStreamBase", "lower quick playback speed");
                this.sendMessage(222, -1, null);
                break;
            }
            case MEDIA_INFO_AUTO_IMPROVE_DEFINITION: {
                LogUtil.i("EZStreamBase", "auto videoLevel improve");
                this.sendMessage(136, -1, null);
                break;
            }
            case MEDIA_INFO_AUTO_REDUCE_DEFINITION: {
                LogUtil.i("EZStreamBase", "auto videoLevel reduce");
                this.sendMessage(137, -1, null);
            }
        }
        return true;
    }

    public void onAdditionalInfo(EZMediaPlayer.EZPlayerPrivateInfo ezPlayerPrivateInfo) {
        int nType = ezPlayerPrivateInfo.nType;
        byte[] bytes = ezPlayerPrivateInfo.data;
        if (nType == 4097 || nType == 4099) {
            EZDevicePtzAngleInfo ptzAngleInfo = new EZDevicePtzAngleInfo(PtzUtils.convertBytesToInt(bytes, 0, 4), PtzUtils.convertBytesToInt(bytes, 4, 4), PtzUtils.convertBytesToInt(bytes, 8, 4), PtzUtils.convertBytesToInt(bytes, 12, 4), PtzUtils.convertBytesToInt(bytes, 16, 4), PtzUtils.convertBytesToInt(bytes, 20, 4), PtzUtils.convertBytesToInt(bytes, 24, 4), PtzUtils.convertBytesToInt(bytes, 28, 4));
            this.handlePlayAdditionalInfo(ptzAngleInfo);
        } else if (nType == 4192) {
            EZPMPlayPrivateTokenInfo tokenInfo = new EZPMPlayPrivateTokenInfo(PtzUtils.convertBytesToInt(bytes, 0, 4) == 1, new String(bytes, 4, 8));
            this.handlePlayPrivateTokenInfo(tokenInfo);
        }
    }

    public long getStreamFlow() {
        if (this.mediaPlayer != null) {
            return this.mediaPlayer.getStreamFlow();
        }
        return 0L;
    }

    public int getStreamFetchType() {
        if (this.mediaPlayer != null) {
            return this.mediaPlayer.getClientType();
        }
        return -1;
    }

    public int getVideoEncodeType() {
        if (this.mediaPlayer != null) {
            return this.mediaPlayer.getVideoEncodeType();
        }
        return -1;
    }

    public int getVideoWidth() {
        if (this.mediaPlayer != null) {
            return this.mediaPlayer.getVideoWidth();
        }
        return 0;
    }

    public int getVideoHeight() {
        if (this.mediaPlayer != null) {
            return this.mediaPlayer.getVideoHeight();
        }
        return 0;
    }

    boolean checkKey() {
        boolean ret = true;
        if (this.streamSource == 9 ? this.initParam == null || TextUtils.isEmpty((CharSequence)this.initParam.szPermanetkey) : this.streamParamHelp == null || !this.streamParamHelp.isCameraEncrypt() || this.initParam == null) {
            return true;
        }
        if (this.secretKey == null) {
            return false;
        }
        String md1 = MD5Util.getMD5String(this.secretKey);
        String md2 = MD5Util.getMD5String(md1);
        ret = this.initParam.szPermanetkey.compareToIgnoreCase(md2) == 0;
        return ret;
    }

    boolean checkP2PPermisionIfNecessary() {
        if (this.streamParamHelp == null) {
            return false;
        }
        if (!this.streamParamHelp.verifyPermission(this.streamSource)) {
            return false;
        }
        this.streamParamHelp.setCycleVerifyPermissionCallback(this.streamSource, new EZP2PPermissionManager.PermissionVerifyCallBack(){

            @Override
            public void onVerifyResult(boolean hasPermission) {
                if (!hasPermission) {
                    EZStreamBase.this.stop();
                    ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400907);
                    EZStreamBase.this.handlePlayerFailed(errorInfo);
                }
            }
        });
        return true;
    }

    public boolean setPlaybackRate(int rate, int mode) {
        if (this.mediaPlayer == null) {
            return false;
        }
        int ret = this.mediaPlayer.setPlaybackRate(rate, mode);
        if (ret != 0) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(31, ret);
            LogUtil.e("EZStreamBase", "setPlaybackRate failed, error : " + errorInfo.toString());
        }
        return ret == 0;
    }

    public int setCloudPlaybackRate(int rate) {
        if (this.mediaPlayer == null) {
            return -1;
        }
        int ret = -1;
        if (this.mediaPlayer.setPlaybackRate(rate, 2) == 0) {
            ret = 0;
        }
        return ret;
    }

    public void setHard(boolean enable) {
        if (this.mediaPlayer == null) {
            return;
        }
        this.mediaPlayer.setHard(enable);
    }

    public boolean setIntelAnalysis(boolean enable) {
        if (this.mediaPlayer == null) {
            return false;
        }
        int status = this.mediaPlayer.setIntelData(enable ? 1 : 0);
        return status == 0;
    }

    public void setPlayerViewRotation(EZConstants.EZPlayerViewRotationAngle rotationAngle) {
        if (this.mediaPlayer == null) {
            return;
        }
        if (this.streamParamHelp.isMultiChannelDevice) {
            LogUtil.e("EZStreamBase", "setPlayerViewRotation not support multiple channel Device");
            return;
        }
        this.mediaPlayer.setRotation(rotationAngle.angle);
    }

    public void setDemuxModel(int demuxModel) {
        if (this.mediaPlayer == null) {
            return;
        }
        this.mediaPlayer.setDemuxModel(demuxModel);
    }

    protected boolean isInnerSurfaceHolderError(int errorCode) {
        return errorCode == 400037 || errorCode == 321022 || errorCode == 322008;
    }
}


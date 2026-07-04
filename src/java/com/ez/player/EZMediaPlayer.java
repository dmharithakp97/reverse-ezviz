/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Rect
 *  android.graphics.SurfaceTexture
 *  android.os.Handler
 *  android.text.TextUtils
 *  android.util.Log
 *  android.view.Surface
 *  android.view.SurfaceHolder
 */
package com.ez.player;

import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import com.ez.player.EZMediaCallback;
import com.ez.player.EZPlayWaterMarkInfo;
import com.ez.player.EZStreamDataCallback;
import com.ez.player.PlayEffectType;
import com.ez.stream.DownloadCloudParam;
import com.ez.stream.EZAutoDefReportParam;
import com.ez.stream.EZStreamClientManager;
import com.ez.stream.EZTaskMgr;
import com.ez.stream.InitParam;
import com.ez.stream.LogUtil;
import com.ez.stream.NativeApi;
import com.ez.stream.Utils;
import com.ez.stream.VideoStreamInfo;
import java.io.File;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class EZMediaPlayer
implements EZMediaCallback,
EZStreamDataCallback {
    static final String TAG = "EZMediaPlayer";
    long mhMedia = 0L;
    EZStreamClientManager mManager;
    EZTaskMgr mEZTaskMgr = null;
    Handler mHandler = null;
    volatile boolean mIsEncrypt = false;
    final Object mDestroyLock = new Object();
    private String mDevSerial;
    private int mChn;
    private String mSecretKey;
    private boolean isEnlinkDevice = false;
    private String mDebugDir;
    private OnStreamDataListener mOnStreamDataListener;
    private OnInfoListener mOnInfoListener;
    OnErrorListener mOnErrorListener;
    ReentrantLock mErrorListenerLock = new ReentrantLock();
    OnCompletionListener mOnCompletionListener;
    OnDelayListener mOnDelayListener;
    public static final int EZ_PLAY_RATE_1_16 = 9;
    public static final int EZ_PLAY_RATE_1_8 = 7;
    public static final int EZ_PLAY_RATE_1_4 = 5;
    public static final int EZ_PLAY_RATE_1_2 = 3;
    public static final int EZ_PLAY_RATE_0 = 1;
    public static final int EZ_PLAY_RATE_2 = 2;
    public static final int EZ_PLAY_RATE_4 = 4;
    public static final int EZ_PLAY_RATE_8 = 6;
    public static final int EZ_PLAY_RATE_16 = 8;
    public static final int EZ_PLAY_RATE_32 = 10;
    volatile int mCurrentRate = 1;
    private RetryStrategyListener retryStrategyListener;

    public void setOnEZInfoListener(OnEZAdditionalInfoListener listener) {
        NativeApi.setEZInfoCallback(this.mhMedia, listener);
    }

    public void setOnStreamDataListener(OnStreamDataListener listener) {
        this.mOnStreamDataListener = listener;
        NativeApi.setStreamDataCallback(this.mhMedia, this);
    }

    public void setOnInfoListener(OnInfoListener listener) {
        this.mOnInfoListener = listener;
    }

    public void setOnErrorListener(OnErrorListener listener) {
        try {
            this.mErrorListenerLock.lock();
            this.mOnErrorListener = listener;
        }
        finally {
            this.mErrorListenerLock.unlock();
        }
    }

    public void setCompletionListener(OnCompletionListener listener) {
        this.mOnCompletionListener = listener;
    }

    public void setDelayListener(OnDelayListener listener) {
        this.mOnDelayListener = listener;
    }

    public void enableAutoDefinitionDetect() {
        NativeApi.enableAutoDefinitionDetect(this.mhMedia);
    }

    public void setAutoDefinitionDetectParam(int[] param) {
        NativeApi.setAutoDefinitionDetectParam(this.mhMedia, param);
    }

    public EZAutoDefReportParam getAutoDefReportParam() {
        EZAutoDefReportParam param;
        int ret;
        if (this.mhMedia != 0L && (ret = NativeApi.getAutoDefReportParam(this.mhMedia, param = new EZAutoDefReportParam())) == 0) {
            return param;
        }
        return null;
    }

    public void setOnDisplayListener(OnDisplayListener displayListener) {
        NativeApi.setDisplayCallback(this.mhMedia, displayListener);
    }

    public void setRenderListener(OnRenderListener renderListener) {
        NativeApi.setRenderCallback(this.mhMedia, renderListener);
    }

    public void setRenderFrameInfoListener(OnRenderFrameInfoListener renderFrameInfoListener) {
        NativeApi.setRenderFrameInfoCallback(this.mhMedia, renderFrameInfoListener);
    }

    private void initManager(EZStreamClientManager manager) {
        this.mManager = manager;
        this.mEZTaskMgr = manager.getTaskMgr();
        this.mHandler = new Handler(this.mEZTaskMgr.getLooper(), null);
    }

    public EZMediaPlayer(EZStreamClientManager manager, InitParam initParam) {
        this(manager, initParam, false);
    }

    public EZMediaPlayer(EZStreamClientManager manager, InitParam initParam, boolean ezlinkDevice) {
        this.isEnlinkDevice = ezlinkDevice;
        this.initManager(manager);
        this.setDataSource(initParam);
    }

    public EZMediaPlayer(EZStreamClientManager manager, String url, boolean forSqual) {
        this(manager, url, UrlType.TYPE_SQUARE);
    }

    public EZMediaPlayer(EZStreamClientManager manager, String fileName) {
        this(manager, fileName, UrlType.TYPE_FILE);
    }

    public EZMediaPlayer(EZStreamClientManager manager, DownloadCloudParam cloudParam) {
        this.initManager(manager);
        this.setDataSource(cloudParam);
    }

    public EZMediaPlayer(EZStreamClientManager manager, String url, UrlType type) {
        this.initManager(manager);
        switch (type) {
            case TYPE_FILE: {
                this.setDataSource(url);
                break;
            }
            case TYPE_SQUARE: {
                this.setDataSourceWithUrl(url);
                break;
            }
            case TYPE_NETPROTOCOL: {
                if (this.mhMedia > 0L) {
                    NativeApi.destroyHandle(this.mhMedia);
                }
                this.mhMedia = NativeApi.createNetProtocolHandle(url);
                NativeApi.setMediaCallback(this.mhMedia, this);
            }
        }
    }

    public void setStreamCount(int streamCount) {
        NativeApi.setStreamCount(this.mhMedia, streamCount);
    }

    public int setRotation(int rotation) {
        return NativeApi.setRotateEffect(this.mhMedia, rotation);
    }

    public void setDemuxModel(int demuxModel) {
        NativeApi.setDemuxModel(this.mhMedia, demuxModel);
    }

    public void setDebugStreamDir(String dir) {
        this.mDebugDir = dir;
    }

    public synchronized void start() {
        if (this.mhMedia != 0L) {
            if (!TextUtils.isEmpty((CharSequence)this.mDebugDir)) {
                this.setStreamSavePath(this.mDebugDir + Utils.getFileName(this.mSecretKey, this.mDevSerial, this.mChn));
            }
            NativeApi.start(this.mhMedia);
        }
    }

    public synchronized void stop() {
        if (this.mhMedia != 0L) {
            NativeApi.stop(this.mhMedia);
        }
    }

    private void setDataSource(InitParam param) {
        if (this.mhMedia != 0L) {
            NativeApi.destroyHandle(this.mhMedia);
            this.mhMedia = 0L;
        }
        LogUtil.d(TAG, "param.iStreamSource = " + param.iStreamSource);
        this.mDevSerial = param.szDevSerial;
        this.mChn = param.iChannelNumber;
        if (this.isEnlinkDevice) {
            this.mhMedia = NativeApi.createEZLinkHandle(param);
        } else {
            switch (param.iStreamSource) {
                case 0: {
                    this.mhMedia = NativeApi.createPreviewHandle(param);
                    break;
                }
                case 2: {
                    this.mhMedia = NativeApi.createPlaybackHandle(param);
                    break;
                }
                case 8: {
                    this.mhMedia = NativeApi.createPlaybackHandleEx(param);
                    break;
                }
                case 3: {
                    this.mhMedia = NativeApi.createCloudHandle(param);
                    break;
                }
                case 9: {
                    this.mhMedia = NativeApi.createCloudHandleEx(param);
                    break;
                }
                default: {
                    LogUtil.e(TAG, "setDataSource. iStreamSource error. " + param.iStreamSource);
                }
            }
        }
        NativeApi.setMediaCallback(this.mhMedia, this);
    }

    private void setDataSource(DownloadCloudParam param) {
        if (this.mhMedia != 0L) {
            NativeApi.destroyHandle(this.mhMedia);
            this.mhMedia = 0L;
        }
        this.mhMedia = NativeApi.createRecordHandle(param);
        NativeApi.setMediaCallback(this.mhMedia, this);
    }

    private void setDataSourceWithUrl(String streamUrl) {
        if (this.mhMedia > 0L) {
            NativeApi.destroyHandle(this.mhMedia);
        }
        this.mhMedia = NativeApi.createPreviewHandleWithUrl(streamUrl);
        NativeApi.setMediaCallback(this.mhMedia, this);
    }

    private void setDataSource(String fileName) {
        if (this.mhMedia != 0L) {
            NativeApi.destroyHandle(this.mhMedia);
            this.mhMedia = 0L;
        }
        this.mhMedia = NativeApi.createLocalPlayHandle(fileName);
        NativeApi.setMediaCallback(this.mhMedia, this);
    }

    public void setDisplay(SurfaceHolder display) {
        this.setDisplay(display, 0);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setDisplay(SurfaceHolder display, int streamId) {
        LogUtil.d(TAG, "setDisplay = " + display);
        if (this.mhMedia == 0L) {
            return;
        }
        Surface surface = null;
        if (null != display) {
            surface = display.getSurface();
            if (null == surface) {
                Log.e((String)TAG, (String)"Surface null");
                return;
            }
            if (!surface.isValid()) {
                Log.e((String)TAG, (String)"Surface Invalid");
                return;
            }
        }
        Object object = this.mDestroyLock;
        synchronized (object) {
            if (this.mhMedia == 0L) {
                return;
            }
            NativeApi.setDisplayWindows(this.mhMedia, surface, streamId);
        }
    }

    public void setDisplayEx(SurfaceTexture display) {
        this.setDisplayEx(display, 0);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setDisplayEx(SurfaceTexture display, int streamId) {
        LogUtil.d(TAG, "setDisplayEx = " + display + " streamId " + streamId);
        if (this.mhMedia == 0L) {
            return;
        }
        Surface surface = display != null ? new Surface(display) : null;
        Object object = this.mDestroyLock;
        synchronized (object) {
            if (this.mhMedia == 0L) {
                return;
            }
            NativeApi.setDisplayWindows(this.mhMedia, surface, streamId);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setDisplayForStreamId(Surface surface, int streamId) {
        LogUtil.d(TAG, "setDisplayForRegion = " + surface + " streamId " + streamId);
        if (this.mhMedia == 0L) {
            return;
        }
        Object object = this.mDestroyLock;
        synchronized (object) {
            if (this.mhMedia == 0L) {
                return;
            }
            NativeApi.setDisplayWindows(this.mhMedia, surface, streamId);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public int setAssistantDisplay(SurfaceHolder display, int regionNum) {
        LogUtil.d(TAG, "setAssistantDisplay = " + display);
        if (this.mhMedia == 0L) {
            return 2;
        }
        if (regionNum < 1) {
            return 2;
        }
        Surface surface = null;
        if (null != display) {
            surface = display.getSurface();
            if (null == surface) {
                Log.e((String)TAG, (String)"Surface null");
                return 2;
            }
            if (!surface.isValid()) {
                Log.e((String)TAG, (String)"Surface Invalid");
                return 2;
            }
        }
        Object object = this.mDestroyLock;
        synchronized (object) {
            if (this.mhMedia == 0L) {
                return 2;
            }
            return NativeApi.setAssistantDisplayWindows(this.mhMedia, surface, regionNum);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public int setAssistantDisplayEx(SurfaceTexture display, int regionNum) {
        LogUtil.d(TAG, "addDisplayEx = " + display);
        if (this.mhMedia == 0L) {
            return 2;
        }
        if (regionNum < 1) {
            return 2;
        }
        Surface surface = display != null ? new Surface(display) : null;
        Object object = this.mDestroyLock;
        synchronized (object) {
            if (this.mhMedia == 0L) {
                return 2;
            }
            return NativeApi.setAssistantDisplayWindows(this.mhMedia, surface, regionNum);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public int setAssistantSurface(Surface surface, int regionNum) {
        LogUtil.d(TAG, "setAssistantDisplayForStreamId = " + surface);
        if (this.mhMedia == 0L) {
            return 2;
        }
        if (regionNum < 1) {
            return 2;
        }
        Object object = this.mDestroyLock;
        synchronized (object) {
            if (this.mhMedia == 0L) {
                return 2;
            }
            return NativeApi.setAssistantDisplayWindows(this.mhMedia, surface, regionNum);
        }
    }

    public int setEnableSuperEyeEffect(boolean enable, int regionNum, boolean keepEffect, boolean isSync) {
        LogUtil.d(TAG, "setEnableSuperEyeEffect = " + enable + " keepEffect = " + keepEffect + "isSync = " + isSync);
        if (this.mhMedia == 0L) {
            return 2;
        }
        return NativeApi.setEnableSuperEyeEffect(this.mhMedia, enable, regionNum, keepEffect, isSync);
    }

    public void enablePureAudio() {
        if (this.mhMedia != 0L) {
            NativeApi.enablePureAudio(this.mhMedia);
        }
    }

    public int setStreamStrategy(String strategy) {
        LogUtil.d(TAG, "setStreamStrategy = " + strategy);
        if (this.mhMedia == 0L) {
            return 2;
        }
        return NativeApi.setStreamStrategy(this.mhMedia, strategy);
    }

    public int setEZPlayerTimeoutConfig(String config) {
        LogUtil.d(TAG, "setEZPlayerTimeoutConfig = " + config);
        if (this.mhMedia == 0L) {
            return 2;
        }
        return NativeApi.setEZPlayerTimeoutConfig(this.mhMedia, config);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void release() {
        this.stop();
        EZMediaPlayer eZMediaPlayer = this;
        synchronized (eZMediaPlayer) {
            long hMediaTemp;
            Object object = this.mDestroyLock;
            synchronized (object) {
                hMediaTemp = this.mhMedia;
                this.mhMedia = 0L;
            }
            if (hMediaTemp != 0L) {
                NativeApi.destroyHandle(hMediaTemp);
            }
        }
    }

    public int getVideoWidth() {
        return this.getVideoWidth(0);
    }

    public int getVideoWidth(int streamId) {
        if (this.mhMedia == 0L) {
            return 0;
        }
        return NativeApi.getVideoWidth(this.mhMedia, streamId);
    }

    public int getVideoHeight() {
        return this.getVideoHeight(0);
    }

    public int getVideoHeight(int streamId) {
        if (this.mhMedia == 0L) {
            return 0;
        }
        return NativeApi.getVideoHeight(this.mhMedia, streamId);
    }

    public void setSecretKey(String secretKey) {
        if (this.mhMedia != 0L) {
            NativeApi.setSecretKey(this.mhMedia, secretKey);
            this.mSecretKey = secretKey;
        }
    }

    public int capture(String strFilePath) {
        return this.capture(strFilePath, 0);
    }

    public int capture(String strFilePath, int streamId) {
        if (this.mhMedia == 0L) {
            return 1;
        }
        if (strFilePath == null || strFilePath.length() == 0) {
            return 2;
        }
        int ret = NativeApi.capture(this.mhMedia, strFilePath, streamId);
        return ret;
    }

    public int captureWithRender(String strFilePath, int streamId, int width, int height) {
        return NativeApi.captureWithRender(this.mhMedia, strFilePath, streamId, width, height);
    }

    public boolean setDisplayRegion(int regionNum, Surface surface, long left, long top, long right, long bottom) {
        return this.setDisplayRegion(regionNum, surface, left, top, right, bottom, 0);
    }

    public boolean setDisplayRegion(int regionNum, Surface surface, long left, long top, long right, long bottom, int streamId) {
        if (this.mhMedia == 0L) {
            return false;
        }
        if (regionNum < 0) {
            return false;
        }
        return NativeApi.setDisplayRegion(this.mhMedia, regionNum, surface, left, top, right, bottom, streamId) == 0;
    }

    public boolean setDisplayRegionEx(int regionNum, Surface surface, float left, float top, float right, float bottom, int streamId) {
        if (this.mhMedia == 0L) {
            return false;
        }
        if (regionNum < 0) {
            return false;
        }
        return NativeApi.setDisplayRegionEx(this.mhMedia, regionNum, surface, left, top, right, bottom, streamId) == 0;
    }

    public int getCurrentDisplayRegion(Rect rect, int regionNum) {
        if (this.mhMedia == 0L) {
            return 1;
        }
        if (regionNum < 0) {
            return 2;
        }
        return NativeApi.getCurrentDisplayRegion(this.mhMedia, rect, regionNum);
    }

    public boolean isRecording() {
        return this.isRecording(0);
    }

    public boolean isRecording(int streamId) {
        if (this.mhMedia == 0L) {
            return false;
        }
        return NativeApi.isRecording(this.mhMedia);
    }

    public boolean isPlaying() {
        if (this.mhMedia == 0L) {
            return false;
        }
        return NativeApi.isPlaying(this.mhMedia);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void playSound() {
        if (this.mhMedia == 0L) {
            return;
        }
        Object object = this.mDestroyLock;
        synchronized (object) {
            if (this.mhMedia == 0L) {
                return;
            }
            NativeApi.playSound(this.mhMedia);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void stopSound() {
        if (this.mhMedia == 0L) {
            return;
        }
        Object object = this.mDestroyLock;
        synchronized (object) {
            if (this.mhMedia == 0L) {
                return;
            }
            NativeApi.stopSound(this.mhMedia);
        }
    }

    public void setHard(boolean enable) {
        this.setHard(enable, false);
    }

    public void setHard(boolean enable, boolean forceHard) {
        if (this.mhMedia == 0L) {
            return;
        }
        NativeApi.setHard(this.mhMedia, enable, forceHard);
    }

    public boolean isHard() {
        if (this.mhMedia == 0L) {
            return false;
        }
        return NativeApi.isHard(this.mhMedia);
    }

    public int setGlobalBaseTime(EZOSDTime time) {
        if (this.mhMedia == 0L) {
            return 1;
        }
        return NativeApi.setGlobalBaseTime(this.mhMedia, time);
    }

    public EZOSDTime getOSDTime() {
        if (this.mhMedia == 0L) {
            return null;
        }
        EZOSDTime ezosdTime = new EZOSDTime();
        int ret = NativeApi.getOSDTime(this.mhMedia, ezosdTime);
        if (ret != 0) {
            return null;
        }
        return ezosdTime;
    }

    public String getRootStatistics() {
        if (this.mhMedia == 0L) {
            return null;
        }
        return NativeApi.getRootStatisticsJson(this.mhMedia);
    }

    public String[] getSubStatistics() {
        if (this.mhMedia == 0L) {
            return null;
        }
        return NativeApi.getSubStatisticsJson(this.mhMedia);
    }

    public String getUUID() {
        if (this.mhMedia == 0L) {
            return null;
        }
        return NativeApi.getUUID(this.mhMedia);
    }

    public int getClientType() {
        if (this.mhMedia == 0L) {
            return -1;
        }
        return NativeApi.getMediaClientType(this.mhMedia);
    }

    public synchronized void pausePlayback() {
        if (this.mhMedia == 0L) {
            return;
        }
        NativeApi.pause(this.mhMedia);
    }

    public synchronized void resumePlayback() {
        if (this.mhMedia == 0L) {
            return;
        }
        NativeApi.resume(this.mhMedia);
    }

    public boolean isPlaybackPaused() {
        if (this.mhMedia == 0L) {
            return false;
        }
        return NativeApi.isPlaybackPaused(this.mhMedia);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public long getStreamFlow() {
        EZMediaPlayer eZMediaPlayer = this;
        synchronized (eZMediaPlayer) {
            if (this.mhMedia == 0L) {
                return 0L;
            }
            return NativeApi.getSumFlow(this.mhMedia);
        }
    }

    public int setPlaybackRate(int rate) {
        return this.setPlaybackRate(rate, 0, true);
    }

    public int setPlaybackRate(int rate, int fastPlayMode) {
        return this.setPlaybackRate(rate, fastPlayMode, true);
    }

    public int setPlaybackRate(int rate, int fastPlayMode, boolean isRecordHighSpeedFrameExtraction) {
        if (this.mhMedia == 0L) {
            return -1;
        }
        return NativeApi.setRate(this.mhMedia, rate, fastPlayMode, isRecordHighSpeedFrameExtraction);
    }

    public int getPlayTime() {
        if (this.mhMedia == 0L) {
            return 0;
        }
        return NativeApi.getPlayedTime(this.mhMedia);
    }

    public int getSourceBufferRemain() {
        if (this.mhMedia == 0L) {
            return 0;
        }
        return NativeApi.getSourceBufferRemain(this.mhMedia);
    }

    public long getFileTime() {
        if (this.mhMedia == 0L) {
            return 0L;
        }
        return NativeApi.getFileTime(this.mhMedia);
    }

    public boolean setPlayProgress(int progress) {
        if (this.mhMedia == 0L) {
            return false;
        }
        return NativeApi.setPlayProgress(this.mhMedia, progress);
    }

    public int getPlayProcess() {
        if (this.mhMedia == 0L) {
            return 0;
        }
        return NativeApi.getPlayProgress(this.mhMedia);
    }

    public boolean switchToHard(boolean switchToHard) {
        return false;
    }

    public void setPlaybackConvert(int videoBitrate, int resolution, int videoFrameRate) {
        if (this.mhMedia == 0L) {
            return;
        }
        NativeApi.setMediaPlaybackConvert(this.mhMedia, videoBitrate, resolution, videoFrameRate);
    }

    public int setSoundMode(int soundMode, int sessionId) {
        if (this.mhMedia == 0L) {
            return -1;
        }
        return NativeApi.setSoundMode(this.mhMedia, soundMode, sessionId);
    }

    public boolean startRecordingEx(String fileName) {
        return this.startRecordingEx(fileName, 0);
    }

    public boolean startRecordingEx(String fileName, int streamId) {
        if (this.mhMedia == 0L) {
            return false;
        }
        if (fileName == null || fileName.length() == 0) {
            return false;
        }
        File f = new File(fileName);
        String parent = f.getParent();
        if (parent != null) {
            File folder = new File(parent);
            folder.mkdirs();
        }
        int ret = NativeApi.startRecord(this.mhMedia, fileName, streamId);
        LogUtil.d(TAG, "startRecord ret = " + ret);
        return ret == 0;
    }

    public void stopRecordingEx() {
        this.stopRecordingEx(0);
    }

    public synchronized void stopRecordingEx(int streamId) {
        if (this.mhMedia == 0L) {
            return;
        }
        NativeApi.stopRecord(this.mhMedia, streamId);
    }

    public synchronized int setWaterMarkFont(EZPlayWaterMarkInfo markInfo) {
        if (this.mhMedia == 0L) {
            return 1;
        }
        return NativeApi.setWaterMarkFont(this.mhMedia, markInfo);
    }

    public synchronized int startRenderRecording(String fileName) {
        if (this.mhMedia == 0L) {
            return 2;
        }
        if (fileName == null || fileName.length() == 0) {
            return 2;
        }
        File f = new File(fileName);
        String parent = f.getParent();
        if (parent != null) {
            File folder = new File(parent);
            folder.mkdirs();
        }
        return NativeApi.startRenderRecording(this.mhMedia, fileName);
    }

    public synchronized void stopRenderRecording() {
        if (this.mhMedia == 0L) {
            return;
        }
        NativeApi.stopRenderRecording(this.mhMedia);
    }

    public synchronized int pauseCloudPlayback() {
        if (this.mhMedia == 0L) {
            return 1;
        }
        return NativeApi.pause(this.mhMedia);
    }

    public synchronized int resumeCloudPlayback() {
        if (this.mhMedia == 0L) {
            return 1;
        }
        return NativeApi.resume(this.mhMedia);
    }

    public synchronized int seekCloudPlayback(String seekTime) {
        if (this.mhMedia == 0L) {
            return 1;
        }
        return NativeApi.seekCloud(this.mhMedia, seekTime);
    }

    public boolean refreshPlay() {
        return this.refreshPlay(0, true);
    }

    public boolean refreshPlay(int streamId) {
        return this.refreshPlay(streamId, true);
    }

    public boolean refreshPlay(int streamId, boolean checkRender) {
        if (this.mhMedia == 0L) {
            return false;
        }
        return NativeApi.refreshPlayer(this.mhMedia, streamId, checkRender);
    }

    public boolean setHSParam(boolean enable, int nNotch, int nTime) {
        if (this.mhMedia == 0L) {
            return false;
        }
        return NativeApi.setHSParam(this.mhMedia, enable, nNotch, nTime);
    }

    public int enableNoiseSupress(boolean enable) {
        if (this.mhMedia == 0L) {
            return 2;
        }
        return NativeApi.enableNoiseSupress(this.mhMedia, enable);
    }

    public int enableVoiceEnhancement(boolean enable) {
        if (this.mhMedia == 0L) {
            return 2;
        }
        return NativeApi.enableVoiceEnhancement(this.mhMedia, enable);
    }

    public int set3AModelPath(String model) {
        if (this.mhMedia == 0L) {
            return 2;
        }
        if (model == null) {
            return 2;
        }
        if (TextUtils.isEmpty((CharSequence)model)) {
            return 2;
        }
        return NativeApi.set3AModelPath(this.mhMedia, model, this.mDebugDir != null ? this.mDebugDir : "");
    }

    public void setEnableDisplayCBAfterRender(boolean enable) {
        if (this.mhMedia == 0L) {
            return;
        }
        NativeApi.setEnableDisplayCBAfterRender(this.mhMedia, enable);
    }

    public void enableSmoothPlay(int mode) {
        if (this.mhMedia == 0L) {
            return;
        }
        NativeApi.enableSmoothPlay(this.mhMedia, mode);
    }

    public synchronized void startPlayback(List<VideoStreamInfo> videoStreamInfoList) {
        if (this.mhMedia == 0L) {
            return;
        }
        if (!TextUtils.isEmpty((CharSequence)this.mDebugDir)) {
            this.setStreamSavePath(this.mDebugDir + Utils.getFileName(this.mSecretKey, this.mDevSerial, this.mChn));
        }
        NativeApi.startPlayback(this.mhMedia, videoStreamInfoList);
    }

    public int seek(List<VideoStreamInfo> videoStreamInfoList) {
        return this.seek(videoStreamInfoList, 0);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public int seek(List<VideoStreamInfo> videoStreamInfoList, int newSeekMode) {
        int ret = 0;
        EZMediaPlayer eZMediaPlayer = this;
        synchronized (eZMediaPlayer) {
            if (this.mhMedia == 0L) {
                return 1;
            }
            ret = NativeApi.seek(this.mhMedia, videoStreamInfoList, newSeekMode);
        }
        return ret;
    }

    public synchronized int continuePlayback(List<VideoStreamInfo> videoStreamInfoList) {
        if (this.mhMedia == 0L) {
            return 1;
        }
        return NativeApi.continuePlayback(this.mhMedia, videoStreamInfoList);
    }

    @Override
    public void onErrorListener(final int errorType, final int errorCode) {
        LogUtil.d(TAG, "onError " + errorType + ", errorCode " + errorCode);
        if (this.mOnErrorListener != null) {
            this.mHandler.post(new Runnable(){

                @Override
                public void run() {
                    EZMediaPlayer.this.mOnErrorListener.onError(EZMediaPlayer.this, MediaError.values()[errorType], errorCode);
                }
            });
        }
    }

    @Override
    public void onInfoListener(int infoType, final int param) {
        final MediaInfo info = MediaInfo.values()[infoType];
        if (info == MediaInfo.MEDIA_INFO_PLAYING_FINISH) {
            if (this.mOnCompletionListener != null) {
                this.mHandler.post(new Runnable(){

                    @Override
                    public void run() {
                        EZMediaPlayer.this.mOnCompletionListener.onCompletion(EZMediaPlayer.this);
                    }
                });
            }
        } else if (this.mOnInfoListener != null) {
            this.mHandler.post(new Runnable(){

                @Override
                public void run() {
                    EZMediaPlayer.this.mOnInfoListener.onInfo(EZMediaPlayer.this, info, param);
                }
            });
        }
    }

    @Override
    public void onDelayListener(final int delayTime) {
        LogUtil.d(TAG, "onDelay " + delayTime);
        if (this.mOnDelayListener != null) {
            this.mHandler.post(new Runnable(){

                @Override
                public void run() {
                    EZMediaPlayer.this.mOnDelayListener.onDelay(EZMediaPlayer.this, delayTime);
                }
            });
        }
    }

    @Override
    public void onDataRefresh(int size) {
    }

    @Override
    public void onDataListener(int datatype, byte[] data, int len) {
        if (this.mOnStreamDataListener != null) {
            this.mOnStreamDataListener.onDataCallBack(datatype, data, len);
        }
    }

    public int openHumanDetect(int status) {
        if (this.mhMedia == 0L) {
            return 2;
        }
        return NativeApi.setIntelData(this.mhMedia, status);
    }

    public int setIntelData(int status) {
        if (this.mhMedia == 0L) {
            return 2;
        }
        return NativeApi.setIntelData(this.mhMedia, status);
    }

    public int renderPrivateData(int type, int subType, int enable) {
        if (this.mhMedia == 0L) {
            return 2;
        }
        return NativeApi.renderPrivateData(this.mhMedia, type, subType, enable);
    }

    public int setPrivateDataRenderSwitch(int type, int subType, int enable, int regionNum) {
        if (this.mhMedia == 0L) {
            return 2;
        }
        return NativeApi.setPrivateDataRenderSwitch(this.mhMedia, type, subType, enable, regionNum);
    }

    public int setOverlayFontPath(String path) {
        if (this.mhMedia == 0L) {
            return 2;
        }
        return NativeApi.setOverlayFontPath(this.mhMedia, path);
    }

    public int setSubWindow(int enable) {
        if (this.mhMedia == 0L) {
            return 2;
        }
        return NativeApi.setSubWindow(this.mhMedia, enable);
    }

    public int setSubText(int enable) {
        if (this.mhMedia == 0L) {
            return 2;
        }
        return NativeApi.setSubText(this.mhMedia, enable);
    }

    public int setPrivatePosInfo(int enable) {
        if (this.mhMedia == 0L) {
            return 2;
        }
        return NativeApi.setPrivatePosInfo(this.mhMedia, enable);
    }

    public int setPosBGRectColor(int alpha, int red, int green, int blue) {
        if (this.mhMedia == 0L) {
            return 2;
        }
        return NativeApi.setPosBGRectColor(this.mhMedia, alpha, red, green, blue);
    }

    public int getPort() {
        if (this.mhMedia == 0L) {
            return -1;
        }
        return NativeApi.getPort(this.mhMedia);
    }

    private void setStreamSavePath(String path) {
        if (this.mhMedia != 0L) {
            NativeApi.setStreamSaveDebugPath(this.mhMedia, path);
        }
    }

    public int setANRLevel(boolean enable, int level) {
        if (this.mhMedia != 0L) {
            return NativeApi.setANRParam(this.mhMedia, enable, level);
        }
        return 2;
    }

    public int setImagePostProcessParameter(PlayEffectType type, float value) {
        if (this.mhMedia != 0L) {
            return NativeApi.setImagePostProcessParameter(this.mhMedia, type.getType(), value);
        }
        return 2;
    }

    public int setRetryCount(int count) {
        if (this.mhMedia != 0L) {
            return NativeApi.setRetryCount(this.mhMedia, count);
        }
        return 2;
    }

    public int getHCNetSDKPlaybackHandle() {
        if (this.mhMedia != 0L) {
            return NativeApi.getHCNetSDKPlaybackHandle(this.mhMedia);
        }
        return 2;
    }

    public void updateInitParam(InitParam initParam) {
        if (this.mhMedia != 0L) {
            NativeApi.updateInitParam(this.mhMedia, initParam);
        }
    }

    public void setRetryStrategy(RetryStrategyListener retryStrategyListener) {
        this.retryStrategyListener = retryStrategyListener;
        if (this.mhMedia != 0L) {
            NativeApi.setRetryStrategy(this.mhMedia, retryStrategyListener);
        }
    }

    public int getVideoEncodeType() {
        if (this.mhMedia != 0L) {
            return NativeApi.getVideoEncodeType(this.mhMedia);
        }
        return -1;
    }

    public static interface OnInfoListener {
        public boolean onInfo(EZMediaPlayer var1, MediaInfo var2, int var3);
    }

    public static interface OnEZAdditionalInfoListener {
        public void onAdditionalInfo(EZPlayerPrivateInfo var1);
    }

    public static interface OnStreamDataListener {
        public void onDataCallBack(int var1, byte[] var2, int var3);
    }

    public static interface OnErrorListener {
        public boolean onError(EZMediaPlayer var1, MediaError var2, int var3);
    }

    public static interface OnCompletionListener {
        public void onCompletion(EZMediaPlayer var1);
    }

    public static interface OnDelayListener {
        public void onDelay(EZMediaPlayer var1, float var2);
    }

    public static interface OnDisplayListener {
        public void onDisplay(byte[] var1, int var2, int var3, int var4, int var5);
    }

    public static interface OnRenderListener {
        public void onRender(int var1, int var2, int var3);
    }

    public static interface OnRenderFrameInfoListener {
        public void onRealTimeFrameInfo(EZFrameRenderInfo var1);
    }

    public static enum UrlType {
        TYPE_FILE,
        TYPE_SQUARE,
        TYPE_NETPROTOCOL;

    }

    public static class EZOSDTime {
        public int year;
        public int month;
        public int day;
        public int hour;
        public int min;
        public int sec;
        public int ms;

        EZOSDTime() {
            this.year = 0;
            this.month = 0;
            this.day = 0;
            this.hour = 0;
            this.min = 0;
            this.sec = 0;
            this.ms = 0;
        }

        public EZOSDTime(int year, int month, int day, int hour, int min, int sec, int ms) {
            this.year = year;
            this.month = month;
            this.day = day;
            this.hour = hour;
            this.min = min;
            this.sec = sec;
            this.ms = ms;
        }
    }

    public static enum MediaInfo {
        MEDIA_INFO_VIDEO_SIZE_CHANGED,
        MEDIA_INFO_NEED_TOKENS,
        MEDIA_INFO_SWITCH_TO_DIRECT_INNER,
        MEDIA_INFO_SWITCH_TO_DIRECT_OUTER,
        MEDIA_INFO_SWITCH_TO_P2P,
        MEDIA_INFO_SWITCH_TO_PRIVATE_STREAM,
        MEDIA_INFO_SWITCH_TO_DIRECT_REVERSE,
        MEDIA_INFO_PLAY_PREPARED,
        MEDIA_INFO_RETRY_PLAYING,
        MEDIA_INFO_START_PLAYING,
        MEDIA_INFO_PLAYING_FINISH,
        MEDIA_INFO_CLOUD_IFRAME_CHANGE,
        MEDIA_INFO_CLOUD_LOWER_PLAY_SPEED,
        MEDIA_INFO_SWITCH_TO_PROXY,
        MEDIA_INFO_AUTO_IMPROVE_DEFINITION,
        MEDIA_INFO_AUTO_REDUCE_DEFINITION,
        MEDIA_INFO_HOWLING_EXIST,
        MEDIA_INFO_STREAM_COUNT_CHANGED;

    }

    public static interface RetryStrategyListener {
        public boolean needRetryForError(int var1);
    }

    public static class EZFrameRenderInfo {
        public int nRealTimeFrameRate;
        public int nFrameRate;
        public int nDecodeType;
        public int nDataType;
        public int nUsedTime;
    }

    public static interface OnConvertDataCallback {
        public void onConvertData(int var1, byte[] var2, int var3);
    }

    public static class EZPlayerPrivateInfo {
        public int nType;
        public int nStrVersion;
        public int nLength;
        public long nTimeStamp;
        public byte[] data;
        public int streamIds;
    }

    public static enum MediaError {
        MEDIA_ERROR_UNKNOWN,
        MEDIA_ERROR_INVALID_TOKEN,
        MEDIA_ERROR_OUTOF_MEMORY,
        MEDIA_ERROR_SECRET_KEY,
        MEDIA_ERROR_TIMEOUT,
        MEDIA_ERROR_NO_SURFACE;

    }
}


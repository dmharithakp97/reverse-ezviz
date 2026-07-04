/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.SurfaceTexture
 *  android.os.Handler
 *  android.os.Message
 *  android.text.TextUtils
 *  android.view.SurfaceHolder
 *  com.ez.player.EZVoiceTalk$OnLoudnessListener
 *  com.ez.stream.EZStreamClientManager
 */
package com.videogo.stream;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import com.ez.player.EZVoiceTalk;
import com.ez.stream.EZStreamClientManager;
import com.ezviz.hcnetsdk.EZHCNetStreamParam;
import com.ezviz.npcsdk.NpcPlayer;
import com.videogo.constant.Config;
import com.videogo.device.DeviceManager;
import com.videogo.exception.BaseException;
import com.videogo.main.AppManager;
import com.videogo.openapi.ConfigLoader;
import com.videogo.openapi.EZConstants;
import com.videogo.openapi.EZOpenSDKListener;
import com.videogo.openapi.PlayAPI;
import com.videogo.openapi.bean.EZCloudRecordFile;
import com.videogo.openapi.bean.EZDeviceDetailPublicInfo;
import com.videogo.openapi.bean.EZLeaveMessage;
import com.videogo.openapi.bean.EZPlayURLParams;
import com.videogo.stream.EZFecStreamBase;
import com.videogo.stream.EZPlayWaterMarkConfig;
import com.videogo.stream.EZPlayback;
import com.videogo.stream.EZRealPlay;
import com.videogo.stream.EZStreamBase;
import com.videogo.stream.EZStreamDownload;
import com.videogo.stream.EZStreamParamHelp;
import com.videogo.stream.EZTalkback;
import com.videogo.util.DateTimeUtil;
import com.videogo.util.LogUtil;
import com.videogo.widget.CustomRect;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class EZStreamCtrl {
    private final String TAG = "EZStreamCtrl";
    private SurfaceHolder surfaceHolder;
    private SurfaceHolder surfaceHolder2;
    private Handler handler;
    private SurfaceHolder[] surfaceHolders;
    private SurfaceTexture mSurfaceTexture;
    private SurfaceTexture mSurfaceTexture2;
    private String startTime = null;
    private String stopTime = null;
    private Calendar startTimeCalendar = null;
    private Calendar stopTimeCalendar = null;
    private EZCloudRecordFile cloudRecordFile = null;
    private String playKey = null;
    private int playbackRate;
    private EZOpenSDKListener.EZLeaveMessageFlowCallback leaveMessageFlowCallback;
    private EZLeaveMessage leaveMsg;
    private NpcPlayer mNpcPlayer;
    private boolean isNpcPlayer;
    private boolean isAudioOnly;
    EZStreamParamHelp streamParamHelp;
    String squereUrl;
    EZStreamBase streamLayer;
    EZTalkback talkback;
    EZVoiceTalk.OnLoudnessListener talkbackLoudnessListener;
    float talkbackLoudnessInterval;
    EZStreamDownload streamDownload;
    List<enumStreamTask> streamTaskList;
    Thread streamTaskThr;
    boolean streamTaskThrWorking;
    Lock taskLock;
    private long mStartRealPlayTime;
    private long mStartRealPlayBackTime;
    private long mStartTalkTime;
    private ConfigLoader.PlayConfig mPlayConfig;

    public EZStreamCtrl(EZStreamParamHelp streamParamHelp, String squereUrl, ConfigLoader.PlayConfig playConfig) throws BaseException {
        this.playbackRate = EZConstants.EZPlaybackRate.EZ_PLAYBACK_RATE_1.value;
        this.leaveMsg = null;
        this.isNpcPlayer = false;
        this.isAudioOnly = false;
        this.streamParamHelp = null;
        this.squereUrl = null;
        this.streamLayer = null;
        this.talkback = null;
        this.streamDownload = null;
        this.streamTaskList = null;
        this.streamTaskThr = null;
        this.streamTaskThrWorking = false;
        this.taskLock = null;
        this.mPlayConfig = playConfig;
        this.streamParamHelp = streamParamHelp;
        this.squereUrl = squereUrl;
        this.streamTaskList = new ArrayList<enumStreamTask>();
        this.streamTaskThrWorking = true;
        this.taskLock = new ReentrantLock();
        this.startJobTask();
        if (streamParamHelp != null && streamParamHelp.getEZPlayURLParams() != null && !TextUtils.isEmpty((CharSequence)streamParamHelp.getEZPlayURLParams().verifyCode)) {
            this.setPlayKey(streamParamHelp.getEZPlayURLParams().verifyCode);
        }
    }

    public EZStreamCtrl(EZHCNetStreamParam ezhcNetStreamParam, ConfigLoader.PlayConfig playConfig) throws BaseException {
        this.playbackRate = EZConstants.EZPlaybackRate.EZ_PLAYBACK_RATE_1.value;
        this.leaveMsg = null;
        this.isNpcPlayer = false;
        this.isAudioOnly = false;
        this.streamParamHelp = null;
        this.squereUrl = null;
        this.streamLayer = null;
        this.talkback = null;
        this.streamDownload = null;
        this.streamTaskList = null;
        this.streamTaskThr = null;
        this.streamTaskThrWorking = false;
        this.taskLock = null;
        this.mPlayConfig = playConfig;
        this.streamParamHelp = this.streamParamHelp;
        this.squereUrl = this.squereUrl;
        this.streamTaskList = new ArrayList<enumStreamTask>();
        this.streamTaskThrWorking = true;
        this.taskLock = new ReentrantLock();
        this.startJobTask();
        if (this.streamParamHelp != null && this.streamParamHelp.getEZPlayURLParams() != null && !TextUtils.isEmpty((CharSequence)this.streamParamHelp.getEZPlayURLParams().verifyCode)) {
            this.setPlayKey(this.streamParamHelp.getEZPlayURLParams().verifyCode);
        }
    }

    public void release() {
        this.taskLock.lock();
        ArrayList delList = new ArrayList();
        this.streamTaskList.removeAll(delList);
        if (this.talkback != null) {
            this.streamTaskList.add(enumStreamTask.STREAM_TASK_TALKBACK_STOP);
        }
        this.streamTaskList.add(enumStreamTask.STREAM_TASK_RELEASE);
        this.taskLock.unlock();
    }

    public EZStreamParamHelp getStreamParamHelp() {
        return this.streamParamHelp;
    }

    private void startJobTask() {
        this.streamTaskThr = new Thread(new Runnable(){

            @Override
            public void run() {
                try {
                    if (!Config.ENABLE_SDK_TKTOKEN && EZStreamClientManager.create((Context)PlayAPI.mApplication.getApplicationContext()).getLeftTokenCount() == 0) {
                        LogUtil.d("EZStreamCtrl", "no left token, need to set tokens");
                        List<String> tokenList = DeviceManager.getInstance().getStreamToken(true);
                        if (tokenList != null && tokenList.size() > 0) {
                            EZStreamClientManager.create((Context)PlayAPI.mApplication.getApplicationContext()).setTokens(tokenList.toArray(new String[tokenList.size()]));
                        }
                    }
                }
                catch (BaseException e) {
                    LogUtil.printErrStackTrace("EZStreamCtrl", e.fillInStackTrace());
                }
                while (EZStreamCtrl.this.streamTaskThrWorking) {
                    if (EZStreamCtrl.this.streamTaskList.size() == 0) {
                        try {
                            Thread.sleep(100L);
                        }
                        catch (InterruptedException e) {
                            LogUtil.printErrStackTrace("EZStreamCtrl", e.fillInStackTrace());
                        }
                        continue;
                    }
                    EZStreamCtrl.this.taskLock.lock();
                    enumStreamTask task = EZStreamCtrl.this.streamTaskList.get(0);
                    EZStreamCtrl.this.streamTaskList.remove(0);
                    EZStreamCtrl.this.taskLock.unlock();
                    EZStreamCtrl.this.doJob(task);
                }
            }
        });
        this.streamTaskThr.start();
    }

    protected void sendMessage(int msg, int arg1, Object obj) {
        if (this.handler != null) {
            Message message = Message.obtain();
            message.what = msg;
            message.arg1 = arg1;
            message.obj = obj;
            this.handler.sendMessage(message);
        }
    }

    private void setSurfaceWindow() {
        if (this.surfaceHolder != null) {
            this.streamLayer.setSurfaceHold(this.surfaceHolder);
            if (this.surfaceHolder2 != null) {
                this.streamLayer.setSurfaceHold(this.surfaceHolder2, 1);
            }
        } else if (this.mSurfaceTexture != null) {
            this.streamLayer.setSurfaceEx(this.mSurfaceTexture);
            if (this.mSurfaceTexture2 != null) {
                this.streamLayer.setSurfaceEx(this.mSurfaceTexture2, 1);
            }
        }
    }

    private void doJob(enumStreamTask task) {
        if (task == enumStreamTask.STREAM_TASK_REALPLAY_START || task == enumStreamTask.STREAM_TASK_PLAYBACKLOCAL_START || task == enumStreamTask.STREAM_TASK_PLAYBACKCLOUD_START || task == enumStreamTask.STREAM_TASK_TALKBACK_START) {
            long b = System.currentTimeMillis();
            LogUtil.d("EZStreamCtrl", "streamParamHelp.isRealCameraNo: " + this.streamParamHelp.isRealCameraNo);
            if (this.streamParamHelp.isRealCameraNo) {
                try {
                    this.streamParamHelp.readyParamInfo(task == enumStreamTask.STREAM_TASK_REALPLAY_START);
                }
                catch (BaseException e) {
                    LogUtil.printErrStackTrace("EZStreamCtrl", e.fillInStackTrace());
                    if (task == enumStreamTask.STREAM_TASK_REALPLAY_START) {
                        this.sendMessage(103, e.getErrorCode(), e.getErrorInfo());
                    } else if (task == enumStreamTask.STREAM_TASK_PLAYBACKLOCAL_START || task == enumStreamTask.STREAM_TASK_PLAYBACKCLOUD_START) {
                        this.sendMessage(206, e.getErrorCode(), e.getErrorInfo());
                    } else if (task == enumStreamTask.STREAM_TASK_TALKBACK_START) {
                        this.sendMessage(114, e.getErrorCode(), e.getErrorInfo());
                    }
                    return;
                }
            }
            try {
                this.streamParamHelp.readyParamInfoByDeviceSerial();
            }
            catch (BaseException e) {
                LogUtil.printErrStackTrace("EZStreamCtrl", e.fillInStackTrace());
                if (task == enumStreamTask.STREAM_TASK_REALPLAY_START) {
                    this.sendMessage(103, e.getErrorCode(), e.getErrorInfo());
                } else if (task == enumStreamTask.STREAM_TASK_PLAYBACKLOCAL_START || task == enumStreamTask.STREAM_TASK_PLAYBACKCLOUD_START) {
                    this.sendMessage(206, e.getErrorCode(), e.getErrorInfo());
                } else if (task == enumStreamTask.STREAM_TASK_TALKBACK_START) {
                    this.sendMessage(114, e.getErrorCode(), e.getErrorInfo());
                }
                return;
            }
        }
        switch (task) {
            case STREAM_TASK_REALPLAY_START: {
                if (this.streamLayer != null) {
                    this.streamLayer.release();
                    this.streamLayer = null;
                }
                if (this.mNpcPlayer != null) {
                    this.mNpcPlayer.stop();
                    this.mNpcPlayer = null;
                }
                if (this.streamParamHelp != null) {
                    this.streamParamHelp.updateCameraInfoEx();
                }
                boolean isMicroCloudMode = false;
                try {
                    isMicroCloudMode = AppManager.getInstance().getServerInfo().isMicroCloudMode();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                this.isNpcPlayer = !isMicroCloudMode && (this.streamParamHelp.isRtmpPlay() || this.streamParamHelp.getEZPlayURLParams() != null && EZPlayURLParams.PROTOCOL_RTMP.equals(this.streamParamHelp.getEZPlayURLParams().protocol));
                if (this.isNpcPlayer) {
                    LogUtil.d("EZStreamCtrl", "RTMP +" + this.streamParamHelp.cameraInfo.getRtmpUrl());
                    this.mNpcPlayer = NpcPlayer.create(this.streamParamHelp.cameraInfo.getRtmpUrl());
                    this.mNpcPlayer.initEZLogStreamClientParams();
                    this.mNpcPlayer.getEZLogStreamClientParams().serial = this.streamParamHelp.deviceSerial;
                    this.mNpcPlayer.getEZLogStreamClientParams().channel = this.streamParamHelp.cameraNo;
                    this.mNpcPlayer.getEZLogStreamClientParams().enc = this.streamParamHelp.deviceInfo == null ? 0 : this.streamParamHelp.deviceInfo.getIsEncrypt();
                    this.mNpcPlayer.getEZLogStreamClientParams().time = DateTimeUtil.formatTimeToString(this.mStartRealPlayTime, "yyyy-MM-dd HH:mm:ss.SSS");
                    this.mNpcPlayer.getEZLogStreamClientParams().prepCt = (int)(System.currentTimeMillis() - this.mStartRealPlayTime);
                    this.mNpcPlayer.getEZLogStreamClientParams().timebyLong = this.mStartRealPlayTime;
                    this.mNpcPlayer.setHandler(this.handler);
                    if (this.mSurfaceTexture != null) {
                        this.mNpcPlayer.setDisplay(this.mSurfaceTexture);
                    } else {
                        this.mNpcPlayer.setDisplay(this.surfaceHolder);
                    }
                    this.mNpcPlayer.setAudioOnly(this.isAudioOnly);
                    this.mNpcPlayer.start();
                    return;
                }
                this.streamLayer = new EZRealPlay(this.streamParamHelp, this.mPlayConfig);
                this.streamLayer.getEZLogStreamClientParams().time = DateTimeUtil.formatTimeToString(this.mStartRealPlayTime, "yyyy-MM-dd HH:mm:ss.SSS");
                this.streamLayer.getEZLogStreamClientParams().prepCt = (int)(System.currentTimeMillis() - this.mStartRealPlayTime);
                this.streamLayer.getEZLogStreamClientParams().timebyLong = this.mStartRealPlayTime;
                this.streamLayer.getEZLogStreamClientParams().requestCt = (int)this.streamParamHelp.requestCostTime;
                if (!EZRealPlay.class.isInstance(this.streamLayer)) break;
                this.streamLayer.setHandler(this.handler);
                this.setSurfaceWindow();
                this.streamLayer.setPlayKey(this.playKey);
                this.streamLayer.start();
                break;
            }
            case STREAM_TASK_SQUERELPLAY_START: {
                if (this.streamLayer != null) {
                    this.streamLayer.release();
                    this.streamLayer = null;
                }
                this.streamLayer = new EZRealPlay(this.squereUrl, this.mPlayConfig);
                if (!EZRealPlay.class.isInstance(this.streamLayer)) break;
                this.streamLayer.getEZLogStreamClientParams().time = DateTimeUtil.formatTimeToString(this.mStartRealPlayTime, "yyyy-MM-dd HH:mm:ss.SSS");
                this.streamLayer.getEZLogStreamClientParams().prepCt = (int)(System.currentTimeMillis() - this.mStartRealPlayTime);
                this.streamLayer.getEZLogStreamClientParams().timebyLong = this.mStartRealPlayTime;
                this.streamLayer.setHandler(this.handler);
                this.setSurfaceWindow();
                this.streamLayer.start();
                break;
            }
            case STREAM_TASK_PLAYBACKLOCAL_START: {
                if (this.streamLayer != null) {
                    this.streamLayer.release();
                    this.streamLayer = null;
                }
                int iStreamSource = 2;
                try {
                    if (DeviceManager.getInstance().getDeviceInfoExById(this.streamParamHelp.deviceSerial).getSupportSeekPlayback()) {
                        iStreamSource = 8;
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                this.streamLayer = new EZPlayback(this.streamParamHelp, iStreamSource, this.mPlayConfig);
                if (!EZPlayback.class.isInstance(this.streamLayer)) break;
                this.streamLayer.getEZLogStreamClientParams().time = DateTimeUtil.formatTimeToString(this.mStartRealPlayBackTime, "yyyy-MM-dd HH:mm:ss.SSS");
                this.streamLayer.getEZLogStreamClientParams().prepCt = (int)(System.currentTimeMillis() - this.mStartRealPlayBackTime);
                this.streamLayer.getEZLogStreamClientParams().timebyLong = this.mStartRealPlayBackTime;
                this.streamLayer.setHandler(this.handler);
                this.setSurfaceWindow();
                this.streamLayer.setPlayKey(this.playKey);
                ((EZPlayback)this.streamLayer).setTimeInfo(this.startTime, this.stopTime);
                ((EZPlayback)this.streamLayer).setStartTimeCalendar(this.startTimeCalendar);
                ((EZPlayback)this.streamLayer).setStopTimeCalendar(this.stopTimeCalendar);
                this.streamLayer.start();
                break;
            }
            case STREAM_TASK_PLAYBACKCLOUD_START: {
                if (this.streamLayer != null) {
                    this.streamLayer.release();
                    this.streamLayer = null;
                }
                this.streamParamHelp.spaceId = this.cloudRecordFile.getSpaceId();
                this.streamLayer = new EZPlayback(this.streamParamHelp, 9, this.mPlayConfig);
                if (!EZPlayback.class.isInstance(this.streamLayer)) break;
                this.streamLayer.getEZLogStreamClientParams().time = DateTimeUtil.formatTimeToString(this.mStartRealPlayBackTime, "yyyy-MM-dd HH:mm:ss.SSS");
                this.streamLayer.getEZLogStreamClientParams().prepCt = (int)(System.currentTimeMillis() - this.mStartRealPlayBackTime);
                this.streamLayer.getEZLogStreamClientParams().timebyLong = this.mStartRealPlayBackTime;
                this.streamLayer.setHandler(this.handler);
                this.setSurfaceWindow();
                this.streamLayer.setPlayKey(this.playKey);
                ((EZPlayback)this.streamLayer).setTimeInfo(this.startTime, this.stopTime);
                ((EZPlayback)this.streamLayer).setStopTimeCalendar(this.stopTimeCalendar);
                ((EZPlayback)this.streamLayer).setCloudInfo(this.cloudRecordFile);
                this.streamLayer.start();
                break;
            }
            case STREAM_TASK_PLAYBACK_PAUSE: {
                if (this.streamLayer == null || !EZPlayback.class.isInstance(this.streamLayer)) break;
                ((EZPlayback)this.streamLayer).pausePlayback();
                break;
            }
            case STREAM_TASK_PLAYBACKLOCAL_EX_SEEK: {
                if (this.streamLayer == null) break;
                ((EZPlayback)this.streamLayer).setTimeInfo(this.startTime, this.stopTime);
                int errorCode = ((EZPlayback)this.streamLayer).seekPlayback();
                if (errorCode <= 0 || this.streamLayer.seekTime == null || this.stopTimeCalendar == null) break;
                this.startLocalPlayback(this.streamLayer.seekTime, this.stopTimeCalendar);
                break;
            }
            case STREAM_TASK_PLAYBACKCLOUD_EX_SEEK: {
                if (this.streamLayer == null) break;
                ((EZPlayback)this.streamLayer).setTimeInfo(this.startTime, this.stopTime);
                ((EZPlayback)this.streamLayer).seekCloudPlayback();
                break;
            }
            case STREAM_TASK_REALPLAY_STOP: 
            case STREAM_TASK_PLAYBACK_STOP: {
                if (this.isNpcPlayer && this.mNpcPlayer != null) {
                    this.mNpcPlayer.stop();
                    this.mNpcPlayer = null;
                    return;
                }
                if (this.streamLayer == null) break;
                this.streamLayer.stop();
                this.streamLayer.release();
                this.streamLayer = null;
                break;
            }
            case STREAM_TASK_TALKBACK_START: {
                if (this.talkback != null) {
                    this.talkback.release();
                    this.talkback = null;
                }
                this.talkback = new EZTalkback(this.streamParamHelp);
                if (this.talkback == null) break;
                this.talkback.setHandler(this.handler);
                this.talkback.setTalkBackSuccessCallback(new EZTalkback.TalkBackSuccessCallback(){

                    @Override
                    public void talkBackSuccess() {
                        if (EZStreamCtrl.this.streamParamHelp != null && EZStreamCtrl.this.streamParamHelp.deviceInfo != null && EZStreamCtrl.this.streamParamHelp.deviceInfo.getDevProtoEnum() == 6 && EZStreamCtrl.this.streamLayer != null && EZStreamCtrl.this.streamLayer.mediaPlayer != null) {
                            EZStreamCtrl.this.streamLayer.mediaPlayer.setSoundMode(EZStreamCtrl.this.talkback.mEZVoiceTalk.getSoundMode(), EZStreamCtrl.this.talkback.mEZVoiceTalk.getSessionId());
                        }
                    }
                });
                this.talkback.setVoiceTalkLoudnessCallback(this.talkbackLoudnessListener, this.talkbackLoudnessInterval);
                this.talkback.start();
                break;
            }
            case STREAM_TASK_TALKBACK_STOP: {
                if (this.talkback == null) break;
                this.talkback.stop();
                if (this.talkback == null) break;
                this.talkback.release();
                this.talkback = null;
                break;
            }
            case STREAM_TASK_DOWNLOAD_START: {
                if (this.streamDownload == null) {
                    this.streamDownload = new EZStreamDownload();
                }
                if (this.streamDownload == null) {
                    return;
                }
                this.streamDownload.setHandler(this.handler);
                this.streamDownload.start(this.leaveMsg);
                break;
            }
            case STREAM_TASK_RELEASE: {
                if (this.streamLayer != null) {
                    this.streamLayer.release();
                    this.streamLayer = null;
                }
                if (this.talkback != null) {
                    this.talkback.release();
                    this.talkback = null;
                }
                if (this.isNpcPlayer && this.mNpcPlayer != null) {
                    this.mNpcPlayer.stop();
                    this.mNpcPlayer = null;
                }
                this.streamTaskThrWorking = false;
                break;
            }
        }
    }

    public void startRealPlay() {
        this.mStartRealPlayTime = System.currentTimeMillis();
        this.taskLock.lock();
        ArrayList<enumStreamTask> delList = new ArrayList<enumStreamTask>();
        for (enumStreamTask task : this.streamTaskList) {
            if (task != enumStreamTask.STREAM_TASK_REALPLAY_START && task != enumStreamTask.STREAM_TASK_SQUERELPLAY_START) continue;
            delList.add(task);
        }
        this.streamTaskList.removeAll(delList);
        this.streamTaskList.add(this.squereUrl == null ? enumStreamTask.STREAM_TASK_REALPLAY_START : enumStreamTask.STREAM_TASK_SQUERELPLAY_START);
        this.taskLock.unlock();
    }

    public void stopRealPlay() {
        this.taskLock.lock();
        ArrayList<enumStreamTask> delList = new ArrayList<enumStreamTask>();
        for (enumStreamTask task : this.streamTaskList) {
            if (task != enumStreamTask.STREAM_TASK_REALPLAY_START && task != enumStreamTask.STREAM_TASK_REALPLAY_STOP) continue;
            delList.add(task);
        }
        this.streamTaskList.removeAll(delList);
        this.streamTaskList.add(enumStreamTask.STREAM_TASK_REALPLAY_STOP);
        this.taskLock.unlock();
    }

    public void startLocalPlayback(Calendar startTime, Calendar stopTime) {
        this.mStartRealPlayBackTime = System.currentTimeMillis();
        this.startTime = DateTimeUtil.calendarToYMDTHMSZString(startTime);
        this.stopTime = DateTimeUtil.calendarToYMDTHMSZString(stopTime);
        this.playbackRate = EZConstants.EZPlaybackRate.EZ_PLAYBACK_RATE_1.value;
        this.startTimeCalendar = startTime;
        this.stopTimeCalendar = stopTime;
        this.taskLock.lock();
        ArrayList<enumStreamTask> delList = new ArrayList<enumStreamTask>();
        for (enumStreamTask task : this.streamTaskList) {
            if (task != enumStreamTask.STREAM_TASK_PLAYBACKLOCAL_START && task != enumStreamTask.STREAM_TASK_PLAYBACKCLOUD_START) continue;
            delList.add(task);
        }
        this.streamTaskList.removeAll(delList);
        this.streamTaskList.add(enumStreamTask.STREAM_TASK_PLAYBACKLOCAL_START);
        this.taskLock.unlock();
    }

    public void startCloudPlayback(EZCloudRecordFile cloudRecordFile) {
        this.mStartRealPlayBackTime = System.currentTimeMillis();
        this.startTime = DateTimeUtil.calendarToYMDTHMSZString(cloudRecordFile.getStartTime());
        this.stopTime = DateTimeUtil.calendarToYMDTHMSZString(cloudRecordFile.getStopTime());
        this.playbackRate = EZConstants.EZPlaybackRate.EZ_PLAYBACK_RATE_1.value;
        this.cloudRecordFile = cloudRecordFile;
        this.stopTimeCalendar = cloudRecordFile.getStopTime();
        this.taskLock.lock();
        ArrayList<enumStreamTask> delList = new ArrayList<enumStreamTask>();
        for (enumStreamTask task : this.streamTaskList) {
            if (task != enumStreamTask.STREAM_TASK_PLAYBACKLOCAL_START && task != enumStreamTask.STREAM_TASK_PLAYBACKCLOUD_START) continue;
            delList.add(task);
        }
        this.streamTaskList.removeAll(delList);
        this.streamTaskList.add(enumStreamTask.STREAM_TASK_PLAYBACKCLOUD_START);
        this.taskLock.unlock();
    }

    public void stopPlayback() {
        this.taskLock.lock();
        ArrayList<enumStreamTask> delList = new ArrayList<enumStreamTask>();
        for (enumStreamTask task : this.streamTaskList) {
            if (task != enumStreamTask.STREAM_TASK_PLAYBACKLOCAL_START && task != enumStreamTask.STREAM_TASK_PLAYBACKCLOUD_START && task != enumStreamTask.STREAM_TASK_PLAYBACK_STOP) continue;
            delList.add(task);
        }
        this.streamTaskList.removeAll(delList);
        this.streamTaskList.add(enumStreamTask.STREAM_TASK_PLAYBACK_STOP);
        this.taskLock.unlock();
    }

    public boolean pausePlayback() {
        if (this.streamLayer == null) {
            return false;
        }
        if (!EZPlayback.class.isInstance(this.streamLayer)) {
            return false;
        }
        this.taskLock.lock();
        ArrayList<enumStreamTask> delList = new ArrayList<enumStreamTask>();
        for (enumStreamTask task : this.streamTaskList) {
            if (task != enumStreamTask.STREAM_TASK_PLAYBACKLOCAL_START && task != enumStreamTask.STREAM_TASK_PLAYBACKCLOUD_START && task != enumStreamTask.STREAM_TASK_PLAYBACK_STOP && task != enumStreamTask.STREAM_TASK_PLAYBACK_PAUSE) continue;
            delList.add(task);
        }
        this.streamTaskList.removeAll(delList);
        this.streamTaskList.add(enumStreamTask.STREAM_TASK_PLAYBACK_PAUSE);
        this.taskLock.unlock();
        return true;
    }

    public boolean resumePlayback() {
        if (this.streamLayer == null) {
            return false;
        }
        if (!EZPlayback.class.isInstance(this.streamLayer)) {
            return false;
        }
        EZPlayback playback = (EZPlayback)this.streamLayer;
        boolean ret = playback.resumePlayback();
        if (this.streamLayer.getStreamSource() == 2 && this.playbackRate != EZConstants.EZPlaybackRate.EZ_PLAYBACK_RATE_1.value) {
            this.streamLayer.setPlaybackRate(this.playbackRate, 0);
        }
        return ret;
    }

    public boolean setDisplayRegion(long left, long top, long right, long bottom, int streamId) {
        if (this.streamLayer == null) {
            LogUtil.w("EZStreamCtrl", "mediaPlayer is null");
            return false;
        }
        return this.streamLayer.setDisplayRegion(left, top, right, bottom, streamId);
    }

    public void setDisplayRegion(boolean enable, CustomRect original, CustomRect current, int streamId) {
        if (this.streamLayer == null) {
            return;
        }
        this.streamLayer.setDisplayRegion(enable, original, current, streamId);
    }

    public void setSurfaceHold(SurfaceHolder sh) {
        this.setSurfaceHold(sh, 0);
    }

    public void setSurfaceHold(SurfaceHolder sh, int streamId) {
        if (streamId == 0) {
            this.surfaceHolder = sh;
            if (this.streamLayer == null) {
                return;
            }
            this.streamLayer.setSurfaceHold(this.surfaceHolder, streamId);
        } else if (streamId == 1) {
            this.surfaceHolder2 = sh;
            if (this.streamLayer == null) {
                return;
            }
            this.streamLayer.setSurfaceHold(this.surfaceHolder2, streamId);
        }
    }

    public void setSurfaceEx(SurfaceTexture sh) {
        this.setSurfaceEx(sh, 0);
    }

    public void setSurfaceEx(SurfaceTexture sh, int streamId) {
        if (streamId == 0) {
            this.mSurfaceTexture = sh;
            if (this.streamLayer == null) {
                return;
            }
            this.streamLayer.setSurfaceEx(sh, streamId);
        } else if (streamId == 1) {
            this.mSurfaceTexture2 = sh;
            if (this.streamLayer == null) {
                return;
            }
            this.streamLayer.setSurfaceEx(sh, streamId);
        }
    }

    public void setSurfaceHolds(SurfaceHolder[] shs) {
        this.surfaceHolders = shs;
        if (this.streamLayer == null) {
            return;
        }
        if (!(this.streamLayer instanceof EZFecStreamBase)) {
            return;
        }
        EZFecStreamBase fecRealPlay = (EZFecStreamBase)this.streamLayer;
        fecRealPlay.setSurfaceHolds(shs);
    }

    public void refreshPlay(int streamId) {
        if (this.streamLayer == null) {
            return;
        }
        this.streamLayer.refreshPlay(streamId);
    }

    public void openFecCorrect(EZConstants.EZFecCorrectType fecCorrectType, EZConstants.EZFecPlaceType fecPlaceType) {
        if (!(this.streamLayer instanceof EZFecStreamBase)) {
            return;
        }
        EZFecStreamBase fecRealPlay = (EZFecStreamBase)this.streamLayer;
        fecRealPlay.openFecCorrect(fecCorrectType, fecPlaceType, false);
    }

    public boolean onFecTouchDown(int index, float x, float y) {
        if (!(this.streamLayer instanceof EZFecStreamBase)) {
            return false;
        }
        EZFecStreamBase fecRealPlay = (EZFecStreamBase)this.streamLayer;
        return fecRealPlay.onFecTouchDown(index, x, y);
    }

    public void onFecTouchMove(int index, float x, float y) {
        if (!(this.streamLayer instanceof EZFecStreamBase)) {
            return;
        }
        EZFecStreamBase fecRealPlay = (EZFecStreamBase)this.streamLayer;
        fecRealPlay.onFecTouchMove(index, x, y);
    }

    public void onFecTouchUp(int index) {
        if (!(this.streamLayer instanceof EZFecStreamBase)) {
            return;
        }
        EZFecStreamBase fecRealPlay = (EZFecStreamBase)this.streamLayer;
        fecRealPlay.onFecTouchUp(index);
    }

    public void onFecTouchStartScale(int index, float distance) {
        if (!(this.streamLayer instanceof EZFecStreamBase)) {
            return;
        }
        EZFecStreamBase fecRealPlay = (EZFecStreamBase)this.streamLayer;
        fecRealPlay.onFecTouchStartScale(index, distance);
    }

    public void onFecTouchScale(int index, float scale, float maxScale) {
        if (!(this.streamLayer instanceof EZFecStreamBase)) {
            return;
        }
        EZFecStreamBase fecRealPlay = (EZFecStreamBase)this.streamLayer;
        fecRealPlay.onFecTouchScale(index, scale, maxScale);
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
        if (this.streamLayer != null) {
            this.streamLayer.setHandler(handler);
        }
    }

    public void setPlayKey(String playKey) {
        this.playKey = playKey;
    }

    public boolean soundCtrl(boolean bOpen) {
        if (this.streamLayer == null) {
            return false;
        }
        return this.streamLayer.soundCtrl(bOpen);
    }

    public Calendar getOSDTime() {
        if (this.streamLayer == null) {
            return null;
        }
        return this.streamLayer.getOSDTime();
    }

    public boolean seekPlayback(Calendar offsetTime) {
        this.mStartRealPlayBackTime = System.currentTimeMillis();
        if (this.streamLayer == null) {
            return false;
        }
        if (!EZPlayback.class.isInstance(this.streamLayer)) {
            return false;
        }
        if (offsetTime.compareTo(this.stopTimeCalendar) > 0) {
            return false;
        }
        this.startTime = DateTimeUtil.calendarToYMDTHMSZString(offsetTime);
        this.taskLock.lock();
        this.streamLayer.isSeeking = true;
        this.streamLayer.seekTime = offsetTime;
        ArrayList<enumStreamTask> delList = new ArrayList<enumStreamTask>();
        for (enumStreamTask task : this.streamTaskList) {
            if (task != enumStreamTask.STREAM_TASK_PLAYBACKLOCAL_START && task != enumStreamTask.STREAM_TASK_PLAYBACKCLOUD_START && task != enumStreamTask.STREAM_TASK_PLAYBACK_STOP) continue;
            delList.add(task);
        }
        this.streamTaskList.removeAll(delList);
        if (this.streamLayer.getStreamSource() == 2) {
            this.streamTaskList.add(enumStreamTask.STREAM_TASK_PLAYBACK_STOP);
            this.streamTaskList.add(enumStreamTask.STREAM_TASK_PLAYBACKLOCAL_START);
        } else if (this.streamLayer.getStreamSource() == 8) {
            this.streamTaskList.add(enumStreamTask.STREAM_TASK_PLAYBACKLOCAL_EX_SEEK);
        } else if (this.streamLayer.getStreamSource() == 9) {
            this.streamTaskList.add(enumStreamTask.STREAM_TASK_PLAYBACKCLOUD_EX_SEEK);
        }
        this.taskLock.unlock();
        return true;
    }

    public int capturePicture(String fileNameWithPath, int streamId) {
        if (this.streamLayer == null) {
            return -1;
        }
        return this.streamLayer.capturePicture(fileNameWithPath, streamId);
    }

    public boolean startRecordFile(String recordFile, int streamId) {
        if (this.streamLayer == null) {
            return false;
        }
        return this.streamLayer.startRecordFile(recordFile, streamId);
    }

    public void stopRecordFile(int streamId) {
        if (this.streamLayer == null) {
            return;
        }
        this.streamLayer.stopRecordFile(streamId);
    }

    public boolean setWaterMarkFont(EZPlayWaterMarkConfig waterMarkConfig) {
        if (this.streamLayer == null || waterMarkConfig == null || waterMarkConfig.fontArray == null || waterMarkConfig.fontArray.length == 0 || this.streamParamHelp == null || this.streamParamHelp.isMultiChannelDevice) {
            return false;
        }
        return this.streamLayer.setWaterMarkFont(waterMarkConfig);
    }

    public void clearWaterMarkFont() {
        if (this.streamLayer == null || this.streamParamHelp == null || this.streamParamHelp.isMultiChannelDevice) {
            return;
        }
        this.streamLayer.clearWaterMarkFont();
    }

    public int captureRenderPicture(String fileNameWithPath, int streamId, int width, int height) {
        if (this.streamLayer == null) {
            return -1;
        }
        return this.streamLayer.captureRenderPicture(fileNameWithPath, streamId, width, height);
    }

    public boolean startRenderRecordFile(String recordFile) {
        if (this.streamLayer == null || this.streamParamHelp == null || this.streamParamHelp.isMultiChannelDevice) {
            return false;
        }
        return this.streamLayer.startRenderRecordFile(recordFile);
    }

    public void stopRendeRecordFile() {
        if (this.streamLayer == null || this.streamParamHelp == null || this.streamParamHelp.isMultiChannelDevice) {
            return;
        }
        this.streamLayer.stopRenderRecordFile();
    }

    public void startTalkback() {
        this.taskLock.lock();
        ArrayList<enumStreamTask> delList = new ArrayList<enumStreamTask>();
        for (enumStreamTask task : this.streamTaskList) {
            if (task != enumStreamTask.STREAM_TASK_TALKBACK_START) continue;
            delList.add(task);
        }
        this.streamTaskList.removeAll(delList);
        this.streamTaskList.add(enumStreamTask.STREAM_TASK_TALKBACK_START);
        this.taskLock.unlock();
    }

    public void stopTalkback() {
        this.taskLock.lock();
        ArrayList<enumStreamTask> delList = new ArrayList<enumStreamTask>();
        for (enumStreamTask task : this.streamTaskList) {
            if (task != enumStreamTask.STREAM_TASK_TALKBACK_START && task != enumStreamTask.STREAM_TASK_TALKBACK_STOP) continue;
            delList.add(task);
        }
        this.streamTaskList.removeAll(delList);
        this.streamTaskList.add(enumStreamTask.STREAM_TASK_TALKBACK_STOP);
        if (this.streamParamHelp != null && this.streamParamHelp.deviceInfo != null && this.streamParamHelp.deviceInfo.getDevProtoEnum() == 6 && this.streamLayer != null && this.streamLayer.mediaPlayer != null) {
            this.streamLayer.mediaPlayer.setSoundMode(3, -1);
        }
        this.taskLock.unlock();
    }

    public void setTalkbackStatus(boolean bPressed) {
        if (this.talkback == null) {
            return;
        }
        this.talkback.setTalkbackStatus(bPressed);
    }

    public void setVoiceTalkLoudnessCallback(EZVoiceTalk.OnLoudnessListener onLoudnessListener, float interval) {
        this.talkbackLoudnessListener = onLoudnessListener;
        this.talkbackLoudnessInterval = interval;
    }

    public void startLeaveMsgDownload(EZLeaveMessage msg) {
        this.taskLock.lock();
        this.leaveMsg = msg;
        ArrayList<enumStreamTask> delList = new ArrayList<enumStreamTask>();
        for (enumStreamTask task : this.streamTaskList) {
            if (task == enumStreamTask.STREAM_TASK_DOWNLOAD_START && task == enumStreamTask.STREAM_TASK_DOWNLOAD_STOP) continue;
            delList.add(task);
        }
        this.streamTaskList.removeAll(delList);
        this.streamTaskList.add(enumStreamTask.STREAM_TASK_DOWNLOAD_START);
        this.taskLock.unlock();
    }

    public int getPlayPort() {
        if (this.streamLayer == null) {
            return -1;
        }
        return this.streamLayer.getPlayPort();
    }

    public void setLeaveMsgCallback(EZOpenSDKListener.EZLeaveMessageFlowCallback leaveMessageFlowCallback) {
        this.leaveMessageFlowCallback = leaveMessageFlowCallback;
        if (this.streamDownload != null) {
            this.streamDownload.setLeaveMessageFlowCallback(leaveMessageFlowCallback);
        }
    }

    public long getStreamFlow() {
        if (this.streamLayer == null) {
            return 0L;
        }
        return this.streamLayer.getStreamFlow();
    }

    public int getVideoEncodeType() {
        if (this.streamLayer == null) {
            return 0;
        }
        return this.streamLayer.getVideoEncodeType();
    }

    public int getStreamFetchType() {
        if (this.streamLayer == null) {
            return -1;
        }
        return this.streamLayer.getStreamFetchType();
    }

    public EZDeviceDetailPublicInfo getDeviceDetailInfo() {
        if (this.streamParamHelp == null || this.streamParamHelp.getDeviceInfo() == null || this.streamParamHelp.getCameraInfo() == null || this.streamLayer == null) {
            return null;
        }
        EZDeviceDetailPublicInfo detailPublicInfo = new EZDeviceDetailPublicInfo();
        detailPublicInfo.videoLevel = this.streamParamHelp.getCameraInfo().getVideoLevel();
        detailPublicInfo.videoQualityInfos = this.streamParamHelp.getCameraInfo().videoQualityInfos;
        detailPublicInfo.localCmdPort = this.streamParamHelp.getDeviceInfo().getLocalCmdPort();
        detailPublicInfo.isOwner = this.streamParamHelp.getDeviceInfo().getIsOwner();
        detailPublicInfo.videoEncodeType = this.streamLayer.getVideoEncodeType();
        detailPublicInfo.videoWidth = this.streamLayer.getVideoWidth();
        detailPublicInfo.videoHeight = this.streamLayer.getVideoHeight();
        return detailPublicInfo;
    }

    public boolean setPlaybackRate(int rate, int mode) {
        if (this.streamLayer == null || !(this.streamLayer instanceof EZPlayback)) {
            return false;
        }
        if (rate == this.playbackRate) {
            LogUtil.d("EZStreamCtrl", "set playback rate multiple, return");
            return true;
        }
        int playbackRateTemp = this.playbackRate;
        this.playbackRate = rate;
        boolean ret = this.streamLayer.setPlaybackRate(rate, mode);
        if (!ret) {
            this.playbackRate = playbackRateTemp;
        }
        return ret;
    }

    public void setHard(boolean enable) {
        if (this.streamLayer != null) {
            this.streamLayer.setHard(enable);
        }
    }

    public void setSpeakerphoneOn(boolean on) {
        if (this.talkback != null) {
            this.talkback.setSpeakerphoneOn(on);
        }
    }

    public boolean isSpeakerphoneOn() {
        boolean isSpeakerphoneOn = true;
        if (this.talkback != null) {
            isSpeakerphoneOn = this.talkback.isSpeakerphoneOn();
        }
        return isSpeakerphoneOn;
    }

    public void openVoiceTalkMicrophone() {
        if (this.talkback != null) {
            this.talkback.openVoiceTalkMicrophone();
        }
    }

    public void closeVoiceTalkMicrophone() {
        if (this.talkback != null) {
            this.talkback.closeVoiceTalkMicrophone();
        }
    }

    public void startVoiceChange(EZConstants.EZVoiceChangeType voiceChangeType, EZTalkback.TalkBackVoiceChangeCallback callback) {
        if (this.talkback != null) {
            this.talkback.startVoiceChange(voiceChangeType != EZConstants.EZVoiceChangeType.EZ_VOICE_CHANGE_TYPE_NORMAL, voiceChangeType.voiceType, callback);
        }
    }

    public void setTalkRemoteMuted(boolean muted) {
        if (this.talkback != null) {
            this.talkback.setTalkRemoteMuted(muted);
        }
    }

    public boolean setIntelAnalysis(boolean enable) {
        if (this.streamLayer == null) {
            return false;
        }
        return this.streamLayer.setIntelAnalysis(enable);
    }

    public void setPlayerViewRotation(EZConstants.EZPlayerViewRotationAngle rotationAngle) {
        if (this.streamLayer == null) {
            return;
        }
        this.streamLayer.setPlayerViewRotation(rotationAngle);
    }

    public void setDemuxModel(int demuxModel) {
        if (this.streamLayer == null) {
            return;
        }
        this.streamLayer.setDemuxModel(demuxModel);
    }

    protected static enum enumStreamTask {
        STREAM_TASK_REALPLAY_START(1),
        STREAM_TASK_REALPLAY_STOP(2),
        STREAM_TASK_SQUERELPLAY_START(3),
        STREAM_TASK_PLAYBACKLOCAL_START(4),
        STREAM_TASK_PLAYBACKCLOUD_START(5),
        STREAM_TASK_PLAYBACK_STOP(6),
        STREAM_TASK_TALKBACK_START(7),
        STREAM_TASK_TALKBACK_STOP(8),
        STREAM_TASK_DOWNLOAD_START(9),
        STREAM_TASK_DOWNLOAD_STOP(10),
        STREAM_TASK_RELEASE(11),
        STREAM_TASK_PLAYBACK_PAUSE(12),
        STREAM_TASK_PLAYBACKLOCAL_EX_SEEK(13),
        STREAM_TASK_PLAYBACKCLOUD_EX_SEEK(14);

        int task;

        private enumStreamTask(int task) {
            this.task = task;
        }

        public int getTask() {
            return this.task;
        }
    }
}


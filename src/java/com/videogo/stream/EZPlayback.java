/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  com.ez.player.EZMediaPlayer$EZOSDTime
 *  com.ez.statistics.RootStatistics
 *  com.ez.stream.EZStreamClientManager
 *  com.ez.stream.VideoStreamInfo
 */
package com.videogo.stream;

import android.content.Context;
import com.ez.player.EZMediaPlayer;
import com.ez.statistics.RootStatistics;
import com.ez.stream.EZStreamClientManager;
import com.ez.stream.VideoStreamInfo;
import com.videogo.errorlayer.ErrorInfo;
import com.videogo.errorlayer.ErrorLayer;
import com.videogo.ezdclog.EZDcLogManager;
import com.videogo.ezdclog.params.EZLogStreamClientParams;
import com.videogo.openapi.ConfigLoader;
import com.videogo.openapi.EZMediaPlayerEx;
import com.videogo.openapi.PlayAPI;
import com.videogo.openapi.bean.EZCloudRecordFile;
import com.videogo.openapi.bean.EZDevicePtzAngleInfo;
import com.videogo.openapi.bean.EZPMPlayPrivateTokenInfo;
import com.videogo.stream.EZFecStreamBase;
import com.videogo.stream.EZStreamParamHelp;
import com.videogo.util.DateTimeUtil;
import com.videogo.util.JsonTools;
import com.videogo.util.LogUtil;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class EZPlayback
extends EZFecStreamBase {
    protected final String TAG = "EZPlayback";
    private String startTime = null;
    private String stopTime = null;
    private Calendar startTimeCalendar = null;
    private Calendar stopTimeCalendar = null;
    private EZCloudRecordFile cloudRecordFile = null;

    public void setStartTimeCalendar(Calendar startTimeCalendar) {
        this.startTimeCalendar = startTimeCalendar;
    }

    public void setStopTimeCalendar(Calendar stopTimeCalendar) {
        this.stopTimeCalendar = stopTimeCalendar;
    }

    public EZPlayback(EZStreamParamHelp streamParamHelp, int iStreamSource, ConfigLoader.PlayConfig playConfig) {
        super(streamParamHelp, playConfig);
        this.streamSource = iStreamSource;
    }

    @Override
    protected boolean createEZMediaPlayer() {
        if (this.streamSource == 9) {
            this.initParam = this.streamParamHelp.paramAddCloudInfo(this.initParam, this.cloudRecordFile);
            if (this.cloudRecordFile != null) {
                this.initParam.szFileID = this.cloudRecordFile.getFileId();
                if (this.cloudRecordFile.getVideoType() >= -1 && this.cloudRecordFile.getiStorageVersion() >= -1) {
                    this.initParam.iCloudVideoType = this.cloudRecordFile.getVideoType();
                    this.initParam.iStorageVersion = this.cloudRecordFile.getiStorageVersion();
                } else {
                    LogUtil.e("EZPlayback", "createEZMediaPlayer: invalid iVideoType " + this.cloudRecordFile.getVideoType() + ", invalid iStorageVersion " + this.cloudRecordFile.getiStorageVersion());
                }
                if (this.cloudRecordFile.getSpaceId() > 0L) {
                    this.initParam.iBusType = 7;
                }
            }
        }
        if (this.startTime != null) {
            this.initParam.szStartTime = this.startTime;
        }
        if (this.stopTime != null) {
            this.initParam.szStopTime = this.stopTime;
        }
        LogUtil.d("EZPlayback", "playback initParam is " + this.initParam.toString());
        this.mediaPlayer = new EZMediaPlayerEx(EZStreamClientManager.create((Context)PlayAPI.mApplication.getApplicationContext()), this.initParam);
        this.getEZLogStreamClientParams().opId = this.mediaPlayer.getUUID();
        this.getEZLogStreamClientParams().plTp = 2;
        return true;
    }

    @Override
    public boolean start() {
        ArrayList<VideoStreamInfo> videoList = new ArrayList<VideoStreamInfo>();
        VideoStreamInfo currentVideo = new VideoStreamInfo();
        currentVideo.beginTime = this.startTime;
        currentVideo.endTime = this.stopTime;
        videoList.add(currentVideo);
        if (!super.startPlayback(videoList)) {
            return false;
        }
        LogUtil.d("EZPlayback", "streamsdk. start playback");
        return true;
    }

    @Override
    public void stop() {
        super.stop();
        this.handleStopSuccess();
    }

    public void setTimeInfo(String startTime, String stopTime) {
        this.startTime = startTime;
        this.stopTime = stopTime;
    }

    public void setCloudInfo(EZCloudRecordFile cloudRecordFile) {
        this.cloudRecordFile = cloudRecordFile;
    }

    public boolean pausePlayback() {
        if (this.mediaPlayer == null) {
            return false;
        }
        this.mediaPlayer.pausePlayback();
        return true;
    }

    public boolean resumePlayback() {
        if (this.mediaPlayer == null) {
            return false;
        }
        this.mediaPlayer.resumePlayback();
        return true;
    }

    public int seekPlayback() {
        int devProtoEnum;
        if (this.mediaPlayer == null) {
            return -1;
        }
        if (this.streamParamHelp != null && ((devProtoEnum = this.streamParamHelp.deviceInfo.getDevProtoEnum()) == 6 || devProtoEnum == 5)) {
            LogUtil.d("EZPlayback", "seekPlayback for nationalStandard device, return 1");
            return 1;
        }
        ArrayList<VideoStreamInfo> videoList = new ArrayList<VideoStreamInfo>();
        VideoStreamInfo currentVideo = new VideoStreamInfo();
        currentVideo.beginTime = this.startTime;
        currentVideo.endTime = this.stopTime;
        videoList.add(currentVideo);
        boolean supportSeekV2 = this.streamParamHelp != null && this.streamParamHelp.deviceInfo.getSupportSeekV2();
        LogUtil.d("EZPlayback", "seekPlayback newSeekMode: " + (supportSeekV2 ? 1 : 0));
        long beginTime = System.currentTimeMillis();
        int ret = supportSeekV2 && this.streamSource == 8 ? this.mediaPlayer.seek(videoList, 1) : this.mediaPlayer.seek(videoList);
        LogUtil.d("EZPlayback", "seekPlayback cost:" + (System.currentTimeMillis() - beginTime) + "ms, ret:" + ret);
        if (ret > 0) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(31, ret);
            LogUtil.e("EZPlayback", "seekPlayback failed, error : " + errorInfo.toString());
        }
        return ret;
    }

    public int seekCloudPlayback() {
        if (this.mediaPlayer == null) {
            return -1;
        }
        ArrayList<VideoStreamInfo> videoList = new ArrayList<VideoStreamInfo>();
        VideoStreamInfo currentVideo = new VideoStreamInfo();
        currentVideo.beginTime = this.startTime;
        currentVideo.endTime = this.stopTime;
        videoList.add(currentVideo);
        LogUtil.d("EZPlayback", "seekPlayback");
        long beginTime = System.currentTimeMillis();
        int ret = this.mediaPlayer.seek(videoList);
        LogUtil.d("EZPlayback", "seekPlayback cost:" + (System.currentTimeMillis() - beginTime) + "ms, ret:" + ret);
        if (ret > 0) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(31, ret);
            LogUtil.e("EZPlayback", "seekPlayback failed, error : " + errorInfo.toString());
        }
        return ret;
    }

    @Override
    public void release() {
        super.release();
    }

    @Override
    protected void handlePlayerFailed(ErrorInfo errorInfo) {
        if ((errorInfo.errorCode == 380102 || errorInfo.errorCode == 380101 || errorInfo.errorCode == 380355 || errorInfo.errorCode == 380356) && this.mediaPlayer != null) {
            EZMediaPlayer.EZOSDTime ezosdTime = this.mediaPlayer.getOSDTime();
            if (this.stopTimeCalendar != null && ezosdTime != null) {
                GregorianCalendar mOSDTime = new GregorianCalendar();
                mOSDTime.set(ezosdTime.year, ezosdTime.month - 1, ezosdTime.day, ezosdTime.hour, ezosdTime.min, ezosdTime.sec);
                LogUtil.d("EZPlayback", "EZPlayback. stopTimeCalendar= " + this.stopTimeCalendar.getTimeInMillis());
                LogUtil.d("EZPlayback", "EZPlayback. mOSDTime= " + mOSDTime.getTimeInMillis());
                LogUtil.d("EZPlayback", "EZPlayback. stopTimeCalendar - mOSDTime= " + (this.stopTimeCalendar.getTimeInMillis() - mOSDTime.getTimeInMillis()));
                long thresholdTime = 5000L;
                if (this.stopTimeCalendar.getTimeInMillis() - mOSDTime.getTimeInMillis() < 5000L) {
                    this.handlePlayFinished();
                    return;
                }
            } else {
                LogUtil.d("EZPlayback", "EZPlayback. handlePlayerFailed mediaPlayer.getOSDTime() = null");
            }
        }
        LogUtil.d("EZPlayback", "EZPlayback. handlePlayerFailed= " + errorInfo.errorCode);
        if (this.isSeeking) {
            this.isSeeking = false;
        }
        this.sendMessage(206, errorInfo.errorCode, errorInfo);
        if (!this.isInnerSurfaceHolderError(errorInfo.errorCode) && this.getEZLogStreamClientParams().cost == 0) {
            this.getEZLogStreamClientParams().errCd = errorInfo.errorCode;
            this.getEZLogStreamClientParams().cost = (int)(System.currentTimeMillis() - this.getEZLogStreamClientParams().timebyLong);
            EZDcLogManager.getInstance().submit(this.getEZLogStreamClientParams());
        }
    }

    @Override
    protected void handlePlaySuccess() {
        LogUtil.d("EZPlayback", "EZPlayback. handlePlaySuccess");
        this.sendMessage(205, 0, null);
        EZLogStreamClientParams params = this.getEZLogStreamClientParams();
        if (this.mediaPlayer != null && params.cost == 0) {
            params.cost = (int)(System.currentTimeMillis() - params.timebyLong);
            RootStatistics rootStatistics = JsonTools.fromJson(this.mediaPlayer.getRootStatistics(), RootStatistics.class);
            params.via = rootStatistics != null ? rootStatistics.mFirstVIA_OK : -1;
            params.errCd = 0;
        }
    }

    @Override
    protected void handlePlayFinished() {
        LogUtil.d("EZPlayback", "EZPlayback. handlePlayFinished");
        this.sendMessage(201, 0, null);
    }

    @Override
    protected void handlePlayPrepared() {
        LogUtil.d("EZPlayback", "EZPlayback. handlePlayPrepared");
        this.sendMessage(223, 0, null);
    }

    @Override
    protected void handlePlayStart() {
        LogUtil.d("EZPlayback", "EZPlayback. handlePlayStart");
        if (this.isSeeking) {
            this.isSeeking = false;
        }
        if (this.startTimeCalendar != null && this.streamParamHelp != null && this.streamParamHelp.deviceInfo.getDevProtoEnum() == 6) {
            EZMediaPlayer.EZOSDTime time = new EZMediaPlayer.EZOSDTime(this.startTimeCalendar.get(1), this.startTimeCalendar.get(2) + 1, this.startTimeCalendar.get(5), this.startTimeCalendar.get(11), this.startTimeCalendar.get(12), this.startTimeCalendar.get(13), this.startTimeCalendar.get(14));
            this.mediaPlayer.setGlobalBaseTime(time);
            LogUtil.d("EZPlayback", "setGlobalBaseTime for nationalStandard device, globalBaseTime:" + DateTimeUtil.calendarToHMSString(this.startTimeCalendar));
        }
        this.sendMessage(217, 0, null);
    }

    @Override
    protected void handleVideoSizeChange(int width, int height) {
        LogUtil.d("EZPlayback", "EZPlayback. handleVideoSizeChange");
        this.sendMessage(134, 0, new StringBuffer().append(width).append(":").append(height).toString());
    }

    @Override
    protected void handlePlayAdditionalInfo(EZDevicePtzAngleInfo info) {
    }

    @Override
    protected void handlePlayPrivateTokenInfo(EZPMPlayPrivateTokenInfo info) {
    }

    protected void handleStopSuccess() {
        LogUtil.d("EZPlayback", "EZPlayback. handleStopSuccess");
        this.sendMessage(221, 0, null);
    }
}


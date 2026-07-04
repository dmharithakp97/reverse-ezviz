/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  com.ez.statistics.RootStatistics
 *  com.ez.stream.EZStreamClientManager
 */
package com.videogo.stream;

import android.content.Context;
import com.ez.statistics.RootStatistics;
import com.ez.stream.EZStreamClientManager;
import com.videogo.errorlayer.ErrorInfo;
import com.videogo.ezdclog.EZDcLogManager;
import com.videogo.ezdclog.params.EZLogStreamClientParams;
import com.videogo.openapi.ConfigLoader;
import com.videogo.openapi.EZMediaPlayerEx;
import com.videogo.openapi.PlayAPI;
import com.videogo.openapi.bean.EZDevicePtzAngleInfo;
import com.videogo.openapi.bean.EZPMPlayPrivateTokenInfo;
import com.videogo.stream.EZFecStreamBase;
import com.videogo.stream.EZStreamParamHelp;
import com.videogo.util.JsonTools;
import com.videogo.util.LogUtil;

public class EZRealPlay
extends EZFecStreamBase {
    protected final String TAG = "EZRealPlay";
    private String mSquareUrl = null;

    public EZRealPlay(EZStreamParamHelp streamParamHelp, ConfigLoader.PlayConfig playConfig) {
        super(streamParamHelp, playConfig);
        this.streamSource = 0;
    }

    public EZRealPlay(String squereUrl, ConfigLoader.PlayConfig playConfig) {
        super(null, playConfig);
        this.streamSource = 1;
        this.mSquareUrl = squereUrl;
    }

    @Override
    public void release() {
        super.release();
    }

    @Override
    protected boolean createEZMediaPlayer() {
        this.mediaPlayer = this.mSquareUrl != null ? new EZMediaPlayerEx(EZStreamClientManager.create((Context)PlayAPI.mApplication.getApplicationContext()), this.mSquareUrl) : new EZMediaPlayerEx(EZStreamClientManager.create((Context)PlayAPI.mApplication.getApplicationContext()), this.initParam);
        if (this.streamParamHelp != null && this.streamParamHelp.isAutoDeviceVideoLevel) {
            this.mediaPlayer.enableAutoDefinitionDetect();
        }
        this.getEZLogStreamClientParams().opId = this.mediaPlayer.getUUID();
        this.getEZLogStreamClientParams().plTp = 1;
        return true;
    }

    @Override
    public boolean start() {
        if (this.streamParamHelp != null && this.streamParamHelp.needResetVideoLevel()) {
            this.streamParamHelp.setVideoQuality();
        }
        if (!super.startRealPlay()) {
            return false;
        }
        LogUtil.d("EZRealPlay", "streamsdk. start realplay");
        return true;
    }

    @Override
    public void stop() {
        super.stop();
        this.handleStopSuccess();
    }

    @Override
    protected void handlePlayerFailed(ErrorInfo errorInfo) {
        LogUtil.d("EZRealPlay", "EZRealPlay. handlePlayerFailed = " + errorInfo.errorCode);
        this.sendMessage(103, errorInfo.errorCode, errorInfo);
        EZLogStreamClientParams params = this.getEZLogStreamClientParams();
        if (!this.isInnerSurfaceHolderError(errorInfo.errorCode) && this.mediaPlayer != null && params.cost == 0) {
            params.errCd = errorInfo.errorCode;
            params.cost = (int)(System.currentTimeMillis() - params.timebyLong);
            RootStatistics rootStatistics = JsonTools.fromJson(this.mediaPlayer.getRootStatistics(), RootStatistics.class);
            params.via = rootStatistics != null ? rootStatistics.mFirstVIA_OK : -1;
            EZDcLogManager.getInstance().submit(params);
        }
    }

    @Override
    protected void handlePlaySuccess() {
        LogUtil.d("EZRealPlay", "EZRealPlay. handlePlaySuccess");
        this.sendMessage(102, 0, null);
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
        LogUtil.d("EZRealPlay", "EZRealPlay. handlePlayFinished");
        this.sendMessage(133, 0, null);
    }

    @Override
    protected void handlePlayPrepared() {
        LogUtil.d("EZRealPlay", "EZRealPlay. handlePlayPrepared");
        this.sendMessage(139, 0, null);
    }

    @Override
    protected void handlePlayStart() {
        LogUtil.d("EZRealPlay", "EZRealPlay. handlePlayStart");
    }

    @Override
    protected void handleVideoSizeChange(int width, int height) {
        LogUtil.d("EZRealPlay", "EZRealPlay. handleVideoSizeChange");
        this.sendMessage(134, 0, new StringBuffer().append(width).append(":").append(height).toString());
    }

    @Override
    protected void handlePlayAdditionalInfo(EZDevicePtzAngleInfo info) {
        this.sendMessage(122, 0, info);
    }

    @Override
    protected void handlePlayPrivateTokenInfo(EZPMPlayPrivateTokenInfo info) {
        this.sendMessage(135, 0, info);
    }

    protected void handleStopSuccess() {
        LogUtil.d("EZRealPlay", "EZRealPlay. handleStopSuccess");
        this.sendMessage(133, 0, null);
    }
}


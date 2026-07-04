/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.ez.player.EZFECMediaPlayer
 *  com.ez.player.EZMediaPlayer
 *  com.ez.stream.EZStreamClientManager
 *  com.ez.stream.InitParam
 *  com.ez.stream.VideoStreamInfo
 */
package com.videogo.openapi;

import com.ez.player.EZFECMediaPlayer;
import com.ez.player.EZMediaPlayer;
import com.ez.stream.EZStreamClientManager;
import com.ez.stream.InitParam;
import com.ez.stream.VideoStreamInfo;
import com.videogo.constant.Config;
import com.videogo.openapi.ConfigLoader;
import com.videogo.openapi.PlayAPI;
import java.io.File;
import java.util.List;

public class EZMediaPlayerEx
extends EZFECMediaPlayer {
    private ConfigLoader.PlayConfig mPlayConfig = null;

    public EZMediaPlayerEx(EZStreamClientManager manager, InitParam initParam) {
        super(manager, initParam);
        if (Config.STREAMDEBUGGING) {
            String filePath = PlayAPI.mApplication.getExternalFilesDir(null).getPath() + "/streams";
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            this.setDebugStreamDir(filePath);
        }
    }

    public EZMediaPlayerEx(EZStreamClientManager manager, String url, boolean forSqual) {
        super(manager, url, forSqual);
    }

    public EZMediaPlayerEx(EZStreamClientManager manager, String fileName) {
        super(manager, fileName);
    }

    public synchronized void start() {
        if (this.mPlayConfig != null) {
            ConfigLoader.loadPlayConfigToPlayer(this.mPlayConfig, (EZMediaPlayer)this);
        }
        super.start();
    }

    public synchronized void startPlayback(List<VideoStreamInfo> videoStreamInfoList) {
        if (this.mPlayConfig != null) {
            ConfigLoader.loadPlayConfigToPlayer(this.mPlayConfig, (EZMediaPlayer)this);
        }
        super.startPlayback(videoStreamInfoList);
    }

    public void setPlayConfig(ConfigLoader.PlayConfig playConfig) {
        this.mPlayConfig = playConfig;
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.ez.player.EZMediaPlayer
 *  com.ez.player.EZMediaPlayer$OnStreamDataListener
 */
package com.videogo.openapi;

import com.ez.player.EZMediaPlayer;
import com.videogo.openapi.EZOpenSDKListener;

public class ConfigLoader {
    public static final String TAG = "ConfigLoader";

    public static void loadPlayConfigToPlayer(final PlayConfig config, EZMediaPlayer player) {
        if (config == null || player == null) {
            return;
        }
        player.setHard(config.isHardDecodeFirst);
        if (config.enableSmoothPlay) {
            player.enableSmoothPlay(config.smoothPlayMode);
        }
        if (config.mOriginDataCallback != null) {
            player.setOnStreamDataListener(new EZMediaPlayer.OnStreamDataListener(){

                public void onDataCallBack(int datatype, byte[] data, int len) {
                    config.mOriginDataCallback.onData(datatype, data, len);
                }
            });
        }
    }

    public static class PlayConfig {
        public boolean isHardDecodeFirst = false;
        public boolean enableSmoothPlay;
        public int smoothPlayMode;
        public EZOpenSDKListener.OriginDataCallback mOriginDataCallback = null;
    }
}


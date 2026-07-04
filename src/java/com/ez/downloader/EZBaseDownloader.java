/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Handler
 */
package com.ez.downloader;

import android.os.Handler;
import com.ez.player.EZMediaCallback;
import com.ez.player.EZMediaPlayer;
import com.ez.stream.EZStreamClientManager;
import com.ez.stream.EZTaskMgr;
import com.ez.stream.InitParam;
import com.ez.stream.LogUtil;
import com.ez.stream.NativeApi;

public abstract class EZBaseDownloader
implements EZMediaCallback {
    private static final String TAG = "EZDownloader";
    private Handler mHandler;
    protected InitParam mInitParam;
    protected String dstFilePath;
    private OnMsgCallBack onMsgCallBack;
    private long mhMedia;

    public EZBaseDownloader(EZStreamClientManager ezStreamClientManager, InitParam initParam, String dstFilePath) {
        EZTaskMgr mEZTaskMgr = ezStreamClientManager.getTaskMgr();
        this.mHandler = new Handler(mEZTaskMgr.getLooper());
        this.mInitParam = initParam;
        this.dstFilePath = dstFilePath;
        this.initParams();
    }

    private void initParams() {
        if (this.mhMedia != 0L) {
            NativeApi.destroyDownloader(this.mhMedia);
            this.mhMedia = 0L;
        }
        this.mhMedia = this.createClient();
    }

    protected abstract long createClient();

    public int startDownload() {
        if (this.mhMedia != 0L) {
            int ret = NativeApi.startDownload(this.mhMedia);
            return ret;
        }
        return -1;
    }

    public void stopDownload() {
        if (this.mhMedia != 0L) {
            NativeApi.stopDownload(this.mhMedia);
        }
    }

    public String getDownloadStatistics() {
        if (this.mhMedia != 0L) {
            return NativeApi.getDownloadStatistics(this.mhMedia);
        }
        return null;
    }

    public void destroy() {
        if (this.mhMedia != 0L) {
            NativeApi.destroyDownloader(this.mhMedia);
            this.mhMedia = 0L;
        }
    }

    public void setMsgCallback(OnMsgCallBack callback) {
        if (this.mhMedia != 0L) {
            this.onMsgCallBack = callback;
            NativeApi.setDownloadCallback(this.mhMedia, this);
        }
    }

    @Override
    public void onErrorListener(final int errorType, final int errorCode) {
        LogUtil.d(TAG, "onError " + errorType + ", errorCode " + errorCode);
        if (this.onMsgCallBack != null) {
            this.mHandler.post(new Runnable(){

                @Override
                public void run() {
                    EZBaseDownloader.this.onMsgCallBack.onError(EZMediaPlayer.MediaError.values()[errorType], errorCode);
                }
            });
        }
    }

    @Override
    public void onInfoListener(final int infoType, int param) {
        LogUtil.d(TAG, "onInfo " + infoType);
        if (EZMediaPlayer.MediaInfo.values()[infoType] == EZMediaPlayer.MediaInfo.MEDIA_INFO_PLAYING_FINISH) {
            // empty if block
        }
        if (this.onMsgCallBack != null) {
            this.mHandler.post(new Runnable(){

                @Override
                public void run() {
                    EZBaseDownloader.this.onMsgCallBack.onInfo(EZMediaPlayer.MediaInfo.values()[infoType]);
                }
            });
        }
    }

    @Override
    public void onDelayListener(int delayTime) {
    }

    @Override
    public void onDataRefresh(int size) {
    }

    public static interface OnMsgCallBack {
        public void onError(EZMediaPlayer.MediaError var1, int var2);

        public void onInfo(EZMediaPlayer.MediaInfo var1);
    }
}


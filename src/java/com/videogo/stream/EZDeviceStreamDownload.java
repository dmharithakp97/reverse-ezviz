/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.text.TextUtils
 *  android.util.Log
 *  com.ez.downloader.EZBaseDownloader$OnMsgCallBack
 *  com.ez.downloader.EZDownloader
 *  com.ez.player.EZMediaPlayer$MediaError
 *  com.ez.player.EZMediaPlayer$MediaInfo
 *  com.ez.stream.EZStreamClientManager
 *  com.ez.stream.InitParam
 */
package com.videogo.stream;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.ez.downloader.EZBaseDownloader;
import com.ez.downloader.EZDownloader;
import com.ez.player.EZMediaPlayer;
import com.ez.stream.EZStreamClientManager;
import com.ez.stream.InitParam;
import com.videogo.errorlayer.ErrorInfo;
import com.videogo.errorlayer.ErrorLayer;
import com.videogo.exception.BaseException;
import com.videogo.openapi.EZConstants;
import com.videogo.openapi.EZOpenSDKListener;
import com.videogo.openapi.PlayAPI;
import com.videogo.openapi.bean.EZDeviceRecordFile;
import com.videogo.stream.EZStreamParamHelp;
import com.videogo.util.DateTimeUtil;
import com.videogo.util.LogUtil;
import com.videogo.util.VideoTransUtil;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EZDeviceStreamDownload {
    private static final String TAG = EZDeviceStreamDownload.class.getSimpleName();
    private InitParam initParam = null;
    private EZDownloader downloadClient;
    private EZOpenSDKListener.EZStreamDownloadCallback streamDownloadCallback;
    public ExecutorService executorService = Executors.newSingleThreadExecutor();
    public boolean isMultiChannelDevice;
    private String mDownloadPathTemp;
    private String mDownloadPath;
    private String mSecretKey;
    private String mStartTime = null;
    private String mStopTime = null;
    private int targetFileType = 5;

    public EZDeviceStreamDownload(String downloadPath, String deviceSerial, int cameraNumber, EZDeviceRecordFile recordFile) {
        new EZDeviceStreamDownload(downloadPath, deviceSerial, cameraNumber, recordFile, false);
    }

    public EZDeviceStreamDownload(String downloadPath, String deviceSerial, int cameraNumber, EZDeviceRecordFile recordFile, boolean isIPCRecordDirectQuery) {
        if (downloadPath == null || recordFile == null || deviceSerial == null || cameraNumber < 0) {
            LogUtil.e(TAG, "EZDeviceStreamDownload: invalid params!!!");
            return;
        }
        this.mDownloadPath = downloadPath;
        this.mDownloadPathTemp = this.mDownloadPath + "_temp";
        EZStreamClientManager clientManager = EZStreamClientManager.create((Context)PlayAPI.mApplication.getApplicationContext());
        EZStreamParamHelp streamParamHelp = new EZStreamParamHelp(deviceSerial, cameraNumber);
        try {
            streamParamHelp.isIPCRecordDirectQuery = isIPCRecordDirectQuery;
            streamParamHelp.readyParamInfo();
            this.initParam = streamParamHelp.getInitParam(5);
        }
        catch (BaseException e) {
            e.printStackTrace();
        }
        if (clientManager != null && this.initParam != null && this.mDownloadPathTemp != null) {
            this.mStartTime = DateTimeUtil.calendarToYMDTHMSZString(recordFile.getStartTime());
            this.mStopTime = DateTimeUtil.calendarToYMDTHMSZString(recordFile.getStopTime());
            this.initParam.szStartTime = this.mStartTime;
            this.initParam.szStopTime = this.mStopTime;
        } else {
            Log.e((String)TAG, (String)"EZDeviceStreamDownload: failed! please check your params.");
        }
    }

    public void setSecretKey(String mSecretKey) {
        this.mSecretKey = mSecretKey;
    }

    public void setStreamDownloadCallback(EZOpenSDKListener.EZStreamDownloadCallback streamDownloadCallback) {
        this.streamDownloadCallback = streamDownloadCallback;
    }

    public void setCompressVideoRecordParams(EZConstants.EZVideoRecordTypeEx videoRecordTypeEx, int frameInterval) {
        if (this.initParam != null) {
            if (frameInterval > 0) {
                this.initParam.iFrameInterval = frameInterval;
            }
            if (videoRecordTypeEx == EZConstants.EZVideoRecordTypeEx.EZ_VIDEO_RECORD_TYPE_COMPRESS_AUTO || videoRecordTypeEx == EZConstants.EZVideoRecordTypeEx.EZ_VIDEO_RECORD_TYPE_COMPRESS_CMR || videoRecordTypeEx == EZConstants.EZVideoRecordTypeEx.EZ_VIDEO_RECORD_TYPE_COMPRESS_MANUAL) {
                this.initParam.iSDCardVideoType = videoRecordTypeEx.recordType;
            }
            this.initParam.iStreamInhibit |= 5;
        }
    }

    public void setTargetFileType(int type) {
        if (type == 2 || type == 5) {
            this.targetFileType = type;
        }
    }

    public void setStreamToken(String streamToken) {
        if (!TextUtils.isEmpty((CharSequence)streamToken)) {
            this.initParam.szStreamToken = streamToken;
        }
    }

    public void setMultiChannelDevice(boolean multiChannelDevice) {
        this.isMultiChannelDevice = multiChannelDevice;
        if (this.isMultiChannelDevice) {
            this.initParam.iChannelNumber = 0;
        }
    }

    public synchronized void start() {
        EZStreamClientManager clientManager = EZStreamClientManager.create((Context)PlayAPI.mApplication.getApplicationContext());
        String targetPath = this.targetFileType == 5 ? this.mDownloadPathTemp : this.mDownloadPath;
        this.downloadClient = new EZDownloader(clientManager, this.initParam, targetPath);
        File targetFile = new File(this.mDownloadPath);
        File targetFileFolder = targetFile.getParentFile();
        boolean isTargetFileFolderExist = true;
        if (!targetFileFolder.exists()) {
            isTargetFileFolderExist = targetFileFolder.mkdirs();
            LogUtil.d(TAG, "try to create targetFile folder: " + isTargetFileFolderExist);
        }
        if (this.downloadClient == null || this.mStartTime == null || this.mStopTime == null || !isTargetFileFolderExist) {
            LogUtil.e(TAG, "start:failed!, please check your params!");
            if (this.streamDownloadCallback != null) {
                this.streamDownloadCallback.onError(EZOpenSDKListener.EZStreamDownloadError.ERROR_EZSTREAM_DOWNLOAD_START);
            }
            this.stop();
            return;
        }
        this.executorService.submit(new Runnable(){

            @Override
            public void run() {
                EZDeviceStreamDownload.this.downloadClient.setMsgCallback(new EZBaseDownloader.OnMsgCallBack(){

                    public void onError(EZMediaPlayer.MediaError mediaError, int errorCode) {
                        LogUtil.i(TAG, "from EZMediaPlayer, onError: " + mediaError.name() + ", " + errorCode);
                        this.handleDownloadError(true, errorCode);
                    }

                    public void onInfo(EZMediaPlayer.MediaInfo mediaInfo) {
                        LogUtil.d(TAG, "onInfo: " + mediaInfo.name());
                        if (EZMediaPlayer.MediaInfo.MEDIA_INFO_PLAYING_FINISH == mediaInfo) {
                            if (EZDeviceStreamDownload.this.targetFileType == 5) {
                                this.tryToTransPsToMp4();
                            } else {
                                LogUtil.d(TAG, "download-onSuccess: " + EZDeviceStreamDownload.this.mDownloadPath);
                                if (EZDeviceStreamDownload.this.streamDownloadCallback != null) {
                                    EZDeviceStreamDownload.this.streamDownloadCallback.onSuccess(EZDeviceStreamDownload.this.mDownloadPath);
                                }
                            }
                        }
                    }
                });
                LogUtil.d(TAG, "try to download video to " + EZDeviceStreamDownload.this.mDownloadPath);
                int ret = EZDeviceStreamDownload.this.downloadClient.startDownload();
                if (ret != 0) {
                    LogUtil.e(TAG, "downloadClient.startDownload: failed!");
                    this.handleDownloadError(false, ret);
                }
            }

            private void tryToTransPsToMp4() {
                VideoTransUtil.TransPsToMp4(EZDeviceStreamDownload.this.mDownloadPathTemp, EZDeviceStreamDownload.this.mSecretKey, EZDeviceStreamDownload.this.mDownloadPath, EZDeviceStreamDownload.this.isMultiChannelDevice, new EZOpenSDKListener.EZStreamDownloadCallback(){

                    @Override
                    public void onSuccess(String filepath) {
                        LogUtil.d(TAG, "tryToTransPsToMp4-onSuccess: " + filepath);
                        EZDeviceStreamDownload.this.tryToDeleteTempFile();
                        if (EZDeviceStreamDownload.this.streamDownloadCallback != null) {
                            EZDeviceStreamDownload.this.streamDownloadCallback.onSuccess(filepath);
                        }
                    }

                    @Override
                    public void onError(EZOpenSDKListener.EZStreamDownloadError code) {
                        Log.d((String)TAG, (String)("tryToTransPsToMp4-onError: " + code.name()));
                        EZDeviceStreamDownload.this.tryToDeleteTempFile();
                        EZDeviceStreamDownload.this.tryToDeleteTargetFile();
                        if (EZDeviceStreamDownload.this.streamDownloadCallback != null) {
                            EZDeviceStreamDownload.this.streamDownloadCallback.onError(code);
                        }
                    }
                });
            }

            private void handleDownloadError(boolean isDownloading, int errorCode) {
                ErrorInfo errorInfo = ErrorLayer.getErrorLayer(31, errorCode);
                if (errorInfo != null) {
                    LogUtil.e(TAG, "handleDownloadError: " + errorInfo.toString());
                }
                if (EZDeviceStreamDownload.this.streamDownloadCallback != null && errorInfo != null) {
                    if (EZDeviceStreamDownload.this.streamDownloadCallback instanceof EZOpenSDKListener.EZStreamDownloadCallbackEx) {
                        EZOpenSDKListener.EZStreamDownloadCallbackEx callbackEx = (EZOpenSDKListener.EZStreamDownloadCallbackEx)EZDeviceStreamDownload.this.streamDownloadCallback;
                        callbackEx.onErrorCode(errorInfo.errorCode);
                    }
                    switch (errorInfo.errorCode) {
                        case 380045: 
                        case 395416: {
                            EZDeviceStreamDownload.this.streamDownloadCallback.onError(EZOpenSDKListener.EZStreamDownloadError.ERROR_EZSTREAM_DOWNLOAD_MAX_CONNECTIONS);
                            break;
                        }
                        default: {
                            EZDeviceStreamDownload.this.streamDownloadCallback.onError(isDownloading ? EZOpenSDKListener.EZStreamDownloadError.ERROR_EZSTREAM_DOWNLOAD : EZOpenSDKListener.EZStreamDownloadError.ERROR_EZSTREAM_DOWNLOAD_START);
                        }
                    }
                }
                EZDeviceStreamDownload.this.stop();
            }
        });
    }

    private void tryToDeleteTempFile() {
        LogUtil.d(TAG, "tempFile path is: " + this.mDownloadPathTemp);
        File tmpFile = new File(this.mDownloadPathTemp);
        if (tmpFile.exists()) {
            LogUtil.d(TAG, "try to delete tempFile " + tmpFile.delete());
        }
    }

    private void tryToDeleteTargetFile() {
        LogUtil.d(TAG, "targetFile path is: " + this.mDownloadPath);
        File targetFile = new File(this.mDownloadPath);
        if (targetFile.exists()) {
            LogUtil.d(TAG, "try to delete targetFile " + targetFile.delete());
        }
    }

    public synchronized void stop() {
        LogUtil.d(TAG, "downloadClient stop: ");
        this.executorService.submit(new Runnable(){

            @Override
            public void run() {
                EZDeviceStreamDownload.this.mStartTime = null;
                EZDeviceStreamDownload.this.mStopTime = null;
                if (EZDeviceStreamDownload.this.downloadClient != null) {
                    EZDeviceStreamDownload.this.downloadClient.stopDownload();
                    EZDeviceStreamDownload.this.downloadClient.destroy();
                    EZDeviceStreamDownload.this.downloadClient = null;
                }
                EZDeviceStreamDownload.this.streamDownloadCallback = null;
                File temp2 = new File(EZDeviceStreamDownload.this.mDownloadPathTemp);
                if (temp2.exists()) {
                    temp2.delete();
                }
            }
        });
    }
}


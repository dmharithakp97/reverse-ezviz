/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.text.TextUtils
 *  android.util.Log
 *  com.ez.stream.DownloadCloudParam
 *  com.ez.stream.EZStreamCallback
 *  com.ez.stream.EZStreamClient
 *  com.ez.stream.EZStreamClientManager
 *  com.ez.stream.JsonUtils
 */
package com.videogo.stream;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.ez.stream.DownloadCloudParam;
import com.ez.stream.EZStreamCallback;
import com.ez.stream.EZStreamClient;
import com.ez.stream.EZStreamClientManager;
import com.ez.stream.JsonUtils;
import com.videogo.errorlayer.ErrorInfo;
import com.videogo.errorlayer.ErrorLayer;
import com.videogo.openapi.EZOpenSDKListener;
import com.videogo.openapi.PlayAPI;
import com.videogo.openapi.bean.EZCloudRecordFile;
import com.videogo.stream.EZStreamParamHelp;
import com.videogo.util.LogUtil;
import com.videogo.util.MD5Util;
import com.videogo.util.VideoTransUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EZCloudStreamDownload {
    private static final String TAG = EZCloudStreamDownload.class.getSimpleName();
    private EZStreamClient downloadClient;
    private DownloadCloudParam downloadParam;
    private EZOpenSDKListener.EZStreamDownloadCallback streamDownloadCallback;
    private EZCloudRecordFile mEZCloudRecordFile;
    private long downloadSize;
    public ExecutorService executorService = Executors.newSingleThreadExecutor();
    private FileOutputStream fileOutputStream = null;
    public boolean isMultiChannelDevice;
    private String mDeviceSerial;
    private String mDownloadPath;
    private String mDownloadPathFinal;
    private String mSecretKey;
    private int targetFileType = 5;
    EZStreamCallback callback = new EZStreamCallback(){

        public void onDataCallBack(int datatype, byte[] data, int len) {
            LogUtil.d(TAG, "Enter onDataCallBack: len=" + len + " datatype=" + datatype + " fileId=" + EZCloudStreamDownload.this.mEZCloudRecordFile.getFileId());
            if (1 == datatype) {
                EZCloudStreamDownload.this.receiveHead(data, len);
            } else if (2 == datatype) {
                EZCloudStreamDownload.this.receiveData(data, len);
            } else if (datatype == 100) {
                EZCloudStreamDownload.this.receiveEnd();
            }
        }

        public void onMessageCallBack(int msg, int result) {
            LogUtil.d(TAG, "onMessageCallBack description:" + msg + ", result:" + result);
            if (msg == 1) {
                EZCloudStreamDownload.this.stop();
            }
        }

        public void onStatisticsCallBack(int statisticsType, String statistics) {
        }
    };

    public EZCloudStreamDownload(String downloadPath, EZCloudRecordFile recordFile) {
        this.mEZCloudRecordFile = recordFile;
        this.mDownloadPathFinal = downloadPath;
        this.mDownloadPath = this.mDownloadPathFinal + "_temp";
        this.downloadClient = EZStreamClientManager.create((Context)PlayAPI.mApplication).createCASClient();
        if (this.downloadClient == null) {
            LogUtil.d(TAG, "downloadClient create is null");
            return;
        }
        LogUtil.d(TAG, "downloadClient create is  " + JsonUtils.toJson((Object)this.downloadClient));
    }

    public void setSecretKey(String mSecretKey) {
        this.mSecretKey = mSecretKey;
    }

    public void setStreamDownloadCallback(EZOpenSDKListener.EZStreamDownloadCallback streamDownloadCallback) {
        this.streamDownloadCallback = streamDownloadCallback;
    }

    public void setTargetFileType(int type) {
        if (type == 2 || type == 5) {
            this.targetFileType = type;
        }
    }

    public void setMultiChannelDevice(boolean multiChannelDevice) {
        this.isMultiChannelDevice = multiChannelDevice;
    }

    public synchronized void start() {
        if (this.downloadClient == null) {
            LogUtil.d(TAG, "downloadClient is null");
            if (this.streamDownloadCallback != null) {
                this.streamDownloadCallback.onError(EZOpenSDKListener.EZStreamDownloadError.ERROR_EZSTREAM_DOWNLOAD_START);
            }
            this.stop();
            return;
        }
        if (!TextUtils.isEmpty((CharSequence)this.mEZCloudRecordFile.getEncryption())) {
            boolean ret;
            if (this.mSecretKey == null) {
                if (this.streamDownloadCallback != null) {
                    this.streamDownloadCallback.onError(EZOpenSDKListener.EZStreamDownloadError.ERROR_EZSTREAM_DOWNLOAD_VERIFYCODE);
                }
                this.stop();
                return;
            }
            String md1 = MD5Util.getMD5String(this.mSecretKey);
            String md2 = MD5Util.getMD5String(md1);
            boolean bl = ret = this.mEZCloudRecordFile.getEncryption().compareToIgnoreCase(md2) == 0;
            if (!ret) {
                if (this.streamDownloadCallback != null) {
                    this.streamDownloadCallback.onError(EZOpenSDKListener.EZStreamDownloadError.ERROR_EZSTREAM_DOWNLOAD_VERIFYCODE);
                }
                this.stop();
                return;
            }
        }
        this.executorService.submit(new Runnable(){

            @Override
            public void run() {
                EZCloudStreamDownload.this.downloadClient.setCallback(EZCloudStreamDownload.this.callback);
                EZCloudStreamDownload.this.downloadParam = EZStreamParamHelp.getDownloadCloudRecordParam(EZCloudStreamDownload.this.mEZCloudRecordFile, EZCloudStreamDownload.this.isMultiChannelDevice);
                int ret = EZCloudStreamDownload.this.downloadClient.startDownloadFromCloud(EZCloudStreamDownload.this.downloadParam);
                if (ret != 0) {
                    ErrorInfo errorInfo = ErrorLayer.getErrorLayer(31, ret);
                    if (errorInfo != null) {
                        LogUtil.e(TAG, "onError: " + errorInfo.toString());
                    }
                    if (EZCloudStreamDownload.this.streamDownloadCallback != null && errorInfo != null) {
                        if (EZCloudStreamDownload.this.streamDownloadCallback instanceof EZOpenSDKListener.EZStreamDownloadCallbackEx) {
                            EZOpenSDKListener.EZStreamDownloadCallbackEx callbackEx = (EZOpenSDKListener.EZStreamDownloadCallbackEx)EZCloudStreamDownload.this.streamDownloadCallback;
                            callbackEx.onErrorCode(errorInfo.errorCode);
                        }
                        EZCloudStreamDownload.this.streamDownloadCallback.onError(EZOpenSDKListener.EZStreamDownloadError.ERROR_EZSTREAM_DOWNLOAD_START);
                        EZCloudStreamDownload.this.stop();
                    }
                }
            }
        });
    }

    public synchronized void stop() {
        LogUtil.d(TAG, "downloadClient stop: ");
        this.executorService.submit(new Runnable(){

            @Override
            public void run() {
                if (EZCloudStreamDownload.this.downloadClient != null) {
                    EZCloudStreamDownload.this.downloadClient.stopDownloadFromCloud();
                    EZCloudStreamDownload.this.downloadClient.release();
                    EZCloudStreamDownload.this.downloadClient = null;
                }
                EZCloudStreamDownload.this.streamDownloadCallback = null;
                if (EZCloudStreamDownload.this.fileOutputStream != null) {
                    try {
                        EZCloudStreamDownload.this.fileOutputStream.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    finally {
                        EZCloudStreamDownload.this.fileOutputStream = null;
                    }
                }
            }
        });
    }

    private void receiveHead(byte[] data, int len) {
        this.downloadSize = 0L;
        LogUtil.d(TAG, " data head");
        this.startSave(data, len);
    }

    private static byte[] getHeaderForRecording(int type, byte[] header) {
        if (type != 5 || header == null || header.length < 14) {
            return header;
        }
        if (header[13] == 113 && header[12] == 16 || header[13] == 113 && header[12] == 17 || header[13] == 32 && header[12] == 1) {
            return header;
        }
        byte[] bytes = new byte[header.length];
        for (int i = 0; i < 12 && i < header.length; ++i) {
            bytes[i] = header[i];
        }
        return bytes;
    }

    private void receiveData(byte[] data, int len) {
        this.startSave(data, len);
    }

    private synchronized boolean startSave(byte[] data, int len) {
        if (data == null || len == 0) {
            LogUtil.d(TAG, "startSave, bytes is null or len is " + len);
            this.stop();
            return false;
        }
        if (null == this.fileOutputStream) {
            try {
                String path = this.targetFileType == 5 ? this.mDownloadPath : this.mDownloadPathFinal;
                this.fileOutputStream = new FileOutputStream(path);
            }
            catch (FileNotFoundException e) {
                this.stop();
                LogUtil.e(TAG, "fileOutputStream init failed", e);
                if (this.streamDownloadCallback != null) {
                    this.streamDownloadCallback.onError(EZOpenSDKListener.EZStreamDownloadError.ERROR_EZSTREAM_DOWNLOAD_START);
                }
                return false;
            }
        }
        try {
            this.fileOutputStream.write(data);
            LogUtil.d(TAG, "startSave, bytes len is " + len);
            this.downloadSize += (long)len;
            if (this.streamDownloadCallback != null && this.streamDownloadCallback instanceof EZOpenSDKListener.EZStreamDownloadCallbackEx && this.downloadSize < this.mEZCloudRecordFile.getFileSize()) {
                EZOpenSDKListener.EZStreamDownloadCallbackEx callbackEx = (EZOpenSDKListener.EZStreamDownloadCallbackEx)this.streamDownloadCallback;
                callbackEx.onDownloadingSize(this.downloadSize);
            }
            return true;
        }
        catch (IOException e) {
            this.stop();
            LogUtil.e(TAG, "fileOutputStream write failed", e);
            if (this.streamDownloadCallback != null) {
                this.streamDownloadCallback.onError(EZOpenSDKListener.EZStreamDownloadError.ERROR_EZSTREAM_DOWNLOAD_START);
            }
            return false;
        }
    }

    private void receiveEnd() {
        if (this.targetFileType == 5) {
            this.tryToTransPsToMp4();
        } else {
            LogUtil.d(TAG, "download-onSuccess: ");
            this.stop();
            if (this.streamDownloadCallback != null) {
                this.streamDownloadCallback.onSuccess(this.mDownloadPathFinal);
            }
        }
    }

    private void tryToTransPsToMp4() {
        VideoTransUtil.TransPsToMp4(this.mDownloadPath, this.mSecretKey, this.mDownloadPathFinal, this.isMultiChannelDevice, new EZOpenSDKListener.EZStreamDownloadCallback(){

            @Override
            public void onSuccess(String filepath) {
                LogUtil.d(TAG, "tryToTransPsToMp4-onSuccess: " + filepath);
                EZCloudStreamDownload.this.stop();
                EZCloudStreamDownload.this.tryToDeleteFile(EZCloudStreamDownload.this.mDownloadPath);
                if (EZCloudStreamDownload.this.streamDownloadCallback != null) {
                    EZCloudStreamDownload.this.streamDownloadCallback.onSuccess(filepath);
                }
            }

            @Override
            public void onError(EZOpenSDKListener.EZStreamDownloadError code) {
                Log.d((String)TAG, (String)("tryToTransPsToMp4-onError: " + code.name()));
                EZCloudStreamDownload.this.stop();
                EZCloudStreamDownload.this.tryToDeleteFile(EZCloudStreamDownload.this.mDownloadPath);
                EZCloudStreamDownload.this.tryToDeleteFile(EZCloudStreamDownload.this.mDownloadPathFinal);
                if (EZCloudStreamDownload.this.streamDownloadCallback != null) {
                    EZCloudStreamDownload.this.streamDownloadCallback.onError(code);
                }
            }
        });
    }

    private void tryToDeleteFile(String downloadPath) {
        LogUtil.d(TAG, "delete File path is: " + downloadPath);
        File tmpFile = new File(downloadPath);
        if (tmpFile.exists()) {
            LogUtil.d(TAG, "try to delete file " + tmpFile.delete());
        }
    }
}


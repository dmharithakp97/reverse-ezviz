/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 *  com.ez.stream.EZMediaTransferInfo
 *  com.ez.stream.EZMediaTransferInfo$VideoInfo
 *  com.ez.stream.SystemTransformSim
 */
package com.videogo.util;

import android.text.TextUtils;
import com.ez.stream.EZMediaTransferInfo;
import com.ez.stream.SystemTransformSim;
import com.videogo.constant.Config;
import com.videogo.openapi.EZOpenSDKListener;
import com.videogo.util.LogUtil;
import java.io.File;
import java.util.ArrayList;

public class VideoTransUtil {
    private static final String TAG = VideoTransUtil.class.getSimpleName();

    public static void TransPsToMp4(String sourcePsPath, String psVerifyCode, String targetMp4Path, EZOpenSDKListener.EZStreamDownloadCallback callback) {
        VideoTransUtil.TransPsToMp4(sourcePsPath, psVerifyCode, targetMp4Path, false, callback);
    }

    public static void TransPsToMp4(final String sourcePsPath, final String psVerifyCode, final String targetMp4Path, final boolean isMultiChannelDevice, final EZOpenSDKListener.EZStreamDownloadCallback callback) {
        new Thread(new Runnable(){

            @Override
            public void run() {
                if (!TextUtils.isEmpty((CharSequence)sourcePsPath)) {
                    int count = isMultiChannelDevice ? 2 : 1;
                    for (int i = 0; i < count; ++i) {
                        File out;
                        SystemTransformSim systemTransformSim;
                        String outFilePath = targetMp4Path;
                        if (i == 1) {
                            int index = targetMp4Path.lastIndexOf(".");
                            outFilePath = outFilePath.substring(0, index) + "_1" + outFilePath.substring(index);
                        }
                        if ((systemTransformSim = SystemTransformSim.createEx((int)5, (String)sourcePsPath, (String)outFilePath)) == null) {
                            if (callback != null) {
                                callback.onError(EZOpenSDKListener.EZStreamDownloadError.ERROR_EZSTREAM_DOWNLOAD_SYSTRANSFORM);
                            }
                            return;
                        }
                        LogUtil.d(TAG, "mVerifyCode is " + psVerifyCode);
                        systemTransformSim.setSecretKey(psVerifyCode);
                        EZMediaTransferInfo targetInfo = new EZMediaTransferInfo();
                        targetInfo.nVideoStreamCount = 1;
                        targetInfo.stVideoInfo = new ArrayList();
                        EZMediaTransferInfo fileInfo = systemTransformSim.getFileInfo();
                        if (fileInfo == null) {
                            LogUtil.d(TAG, "systemTransformSim.getFileInfo() == null");
                            if (callback != null) {
                                callback.onError(EZOpenSDKListener.EZStreamDownloadError.ERROR_EZSTREAM_DOWNLOAD_SYSTRANSFORM);
                            }
                            return;
                        }
                        EZMediaTransferInfo.VideoInfo videoInfo = new EZMediaTransferInfo.VideoInfo();
                        if (fileInfo.stVideoInfo.size() > i) {
                            videoInfo.nTrackId = ((EZMediaTransferInfo.VideoInfo)fileInfo.stVideoInfo.get((int)i)).nTrackId;
                            targetInfo.stVideoInfo.add(videoInfo);
                        }
                        systemTransformSim.setTargetInfo(targetInfo);
                        int ret = systemTransformSim.startEx();
                        LogUtil.d(TAG, "systemTransformSim.start return " + ret);
                        if (ret == 0) {
                            File temp;
                            while (systemTransformSim.getPercent().percent >= 0 && systemTransformSim.getPercent().percent < 100) {
                                try {
                                    Thread.sleep(20L);
                                }
                                catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (systemTransformSim.getPercent().percent == 100) {
                                if (!(callback == null || isMultiChannelDevice && i == 0)) {
                                    LogUtil.d(TAG, "TransPsToMp4 success");
                                    callback.onSuccess(targetMp4Path);
                                }
                            } else if (callback != null) {
                                callback.onError(EZOpenSDKListener.EZStreamDownloadError.ERROR_EZSTREAM_DOWNLOAD_SYSTRANSFORM);
                            }
                            systemTransformSim.stop();
                            systemTransformSim.release();
                            if (isMultiChannelDevice && i == 0 || Config.STREAMDEBUGGING || !(temp = new File(sourcePsPath)).exists()) continue;
                            temp.delete();
                            continue;
                        }
                        if (callback != null) {
                            callback.onError(EZOpenSDKListener.EZStreamDownloadError.ERROR_EZSTREAM_DOWNLOAD_VERIFYCODE);
                        }
                        if (Config.STREAMDEBUGGING) continue;
                        File temp2 = new File(sourcePsPath);
                        if (temp2.exists()) {
                            temp2.delete();
                        }
                        if (!(out = new File(targetMp4Path)).exists()) continue;
                        out.delete();
                    }
                }
            }
        }).start();
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Rect
 *  android.view.Surface
 */
package com.ez.stream;

import android.graphics.Rect;
import android.view.Surface;
import com.ez.player.EZFECMediaPlayer;
import com.ez.player.EZMediaCallback;
import com.ez.player.EZMediaPlayer;
import com.ez.player.EZPlayWaterMarkInfo;
import com.ez.player.EZStreamDataCallback;
import com.ez.stream.DownloadCloudParam;
import com.ez.stream.EZAutoDefReportParam;
import com.ez.stream.EZEcdhKeyInfo;
import com.ez.stream.EZStreamCallback;
import com.ez.stream.EZStreamClientManager;
import com.ez.stream.EZTimeoutParam;
import com.ez.stream.InitParam;
import com.ez.stream.UploadVoiceParam;
import com.ez.stream.VideoStreamInfo;
import java.util.ArrayList;
import java.util.List;

public class NativeApi {
    static int initSDKEx(String soPath) {
        System.loadLibrary("encryptprotect");
        System.loadLibrary("hpr");
        System.loadLibrary("mbedtls");
        System.loadLibrary("jnidispatch");
        System.loadLibrary("PlayCtrl");
        System.loadLibrary("FormatConversion");
        System.loadLibrary("NPQos");
        if (soPath == null) {
            System.loadLibrary("ezstreamclient");
        } else {
            System.load(soPath);
        }
        return NativeApi.initSDK();
    }

    private static native int initSDK();

    static native int uninitSDK();

    static native int setTokens(String[] var0);

    static native int clearTokens();

    static native long createClient(InitParam var0);

    static native long createClientWithUrl(String var0);

    static native int destroyClient(long var0);

    static native int startPreview(long var0);

    static native int stopPreview(long var0);

    static native int setCallback(long var0, EZStreamCallback var2);

    static native int updateParam(long var0, InitParam var2);

    static native int startVoiceTalk(long var0);

    static native String startVoiceTalkV2(long var0);

    static native int stopVoiceTalk(long var0);

    static native int inputVoiceTalkData(long var0, byte[] var2, int var3, int var4);

    static native int switchMic(long var0, int var2);

    static native int startPlayback(long var0, String var2, String var3, String var4);

    static native int stopPlayback(long var0);

    static native int getClientType(long var0);

    static native String getDevInfo(long var0, boolean var2);

    static native void setPlayPort(long var0, int var2);

    static native void setDataCallback2Java(long var0, boolean var2);

    static native void setPlaybackConvert(long var0, int var2, int var3, int var4);

    static native int cloudPlaybackControl(long var0, int var2, String var3, int var4);

    static native long createCASClient();

    static native int setCASClientType(int var0);

    static native int setCASClientVersion(String var0);

    static native int startUpload2Cloud(long var0, UploadVoiceParam var2);

    static native int inputData2Cloud(long var0, byte[] var2, int var3);

    static native int stopUpload2Cloud(long var0);

    static native int startDownloadFromCloud(long var0, DownloadCloudParam var2);

    static native int stopDownloadFromCloud(long var0);

    static native int setPlaybackRate(long var0, int var2);

    static native int getLeftTokenCount();

    static native int clearPreconnectInfo(String var0);

    static native int setLogPrintEnable(boolean var0, boolean var1, int var2);

    static native void setLogCallback(EZStreamClientManager.LogCallback var0);

    static native void logPrint(String var0, String var1);

    static native String getVersion();

    static native boolean isP2PPreviewing(String var0, int var1);

    static native boolean isPlayingWithPreconnect(String var0);

    static native boolean isPreConnectionSucceed(String var0);

    static native int setGlobalCallback(EZStreamClientManager.GlobalCallback var0);

    static native int enableTokenCallback(boolean var0);

    static native int startServerOfReverseDirect(String var0, int var1, int var2);

    static native int stopServerOfReverseDirect();

    static native int clearDeviceListOfReverseDirect(String var0);

    static native int setP2PV3ConfigInfo(short[] var0, int var1, int var2);

    static native void setP2PPublicParam(int var0);

    static native void setLocalNetIp(String var0);

    static native void setTimeoutOptimize(boolean var0);

    static native void setTimeoutParam(EZTimeoutParam var0);

    static native void enableStreamClientETP();

    static native void enableStreamClientCMDEcdh();

    static native void enableTTSCMDEcdh();

    static native void setMtuConfig(int var0);

    static native int setP2PSelectInfo(String var0);

    static native String getP2PSelectInfo();

    static native List<String> selectP2PDevices(ArrayList<String> var0, int var1);

    static native ArrayList<String> getAllProcessedPreconnectSerials();

    static native ArrayList<String> getAllToDoPreconnectSerials();

    static native boolean isPreconnecting(String var0);

    public static native void startPreconnect(InitParam var0);

    public static native long createPreviewHandle(InitParam var0);

    public static native long createPreviewHandleWithUrl(String var0);

    public static native long createPlaybackHandle(InitParam var0);

    public static native long createPlaybackHandleEx(InitParam var0);

    public static native long createCloudHandle(InitParam var0);

    public static native long createCloudHandleEx(InitParam var0);

    public static native long createRecordHandle(DownloadCloudParam var0);

    public static native long createLocalPlayHandle(String var0);

    public static native long createNetProtocolHandle(String var0);

    public static native long createEZLinkHandle(InitParam var0);

    public static native void destroyHandle(long var0);

    public static native void start(long var0);

    public static native void stop(long var0);

    public static native void setDisplayWindows(long var0, Surface var2, int var3);

    public static native int setAssistantDisplayWindows(long var0, Surface var2, int var3);

    public static native int setEnableSuperEyeEffect(long var0, boolean var2, int var3, boolean var4, boolean var5);

    public static native void setSecretKey(long var0, String var2);

    public static native void setMediaCallback(long var0, EZMediaCallback var2);

    public static native void setStreamDataCallback(long var0, EZStreamDataCallback var2);

    public static native void setDisplayCallback(long var0, EZMediaPlayer.OnDisplayListener var2);

    public static native void setRenderCallback(long var0, EZMediaPlayer.OnRenderListener var2);

    public static native void setRenderFrameInfoCallback(long var0, EZMediaPlayer.OnRenderFrameInfoListener var2);

    public static native void setEZInfoCallback(long var0, EZMediaPlayer.OnEZAdditionalInfoListener var2);

    public static native int capture(long var0, String var2, int var3);

    public static native int captureWithRender(long var0, String var2, int var3, int var4, int var5);

    public static native int startRecord(long var0, String var2, int var3);

    public static native void stopRecord(long var0, int var2);

    public static native boolean isRecording(long var0);

    public static native int setWaterMarkFont(long var0, EZPlayWaterMarkInfo var2);

    public static native int startRenderRecording(long var0, String var2);

    public static native void stopRenderRecording(long var0);

    public static native int pause(long var0);

    public static native int resume(long var0);

    public static native void startPlayback(long var0, List<VideoStreamInfo> var2);

    public static native int seek(long var0, List<VideoStreamInfo> var2, int var3);

    public static native int continuePlayback(long var0, List<VideoStreamInfo> var2);

    public static native boolean isPlaybackPaused(long var0);

    public static native int setRate(long var0, int var2, int var3, boolean var4);

    public static native int seekCloud(long var0, String var2);

    public static native int getOSDTime(long var0, EZMediaPlayer.EZOSDTime var2);

    public static native int setGlobalBaseTime(long var0, EZMediaPlayer.EZOSDTime var2);

    public static native int getVideoWidth(long var0, int var2);

    public static native int getVideoHeight(long var0, int var2);

    public static native int setDisplayRegion(long var0, int var2, Surface var3, long var4, long var6, long var8, long var10, int var12);

    public static native int setDisplayRegionEx(long var0, int var2, Surface var3, float var4, float var5, float var6, float var7, int var8);

    public static native int getCurrentDisplayRegion(long var0, Rect var2, int var3);

    public static native int playSound(long var0);

    public static native int stopSound(long var0);

    public static native boolean isPlaying(long var0);

    public static native void enablePureAudio(long var0);

    public static native void setHard(long var0, boolean var2, boolean var3);

    public static native boolean isHard(long var0);

    public static native int getMediaClientType(long var0);

    public static native long getSumFlow(long var0);

    public static native int getFileTime(long var0);

    public static native int getPlayedTime(long var0);

    public static native int getSourceBufferRemain(long var0);

    public static native boolean setPlayProgress(long var0, int var2);

    public static native int getPlayProgress(long var0);

    public static native boolean refreshPlayer(long var0, int var2, boolean var3);

    public static native boolean setHSParam(long var0, boolean var2, int var3, int var4);

    public static native int enableNoiseSupress(long var0, boolean var2);

    public static native int enableVoiceEnhancement(long var0, boolean var2);

    public static native void enableSmoothPlay(long var0, int var2);

    public static native int set3AModelPath(long var0, String var2, String var3);

    public static native void setEnableDisplayCBAfterRender(long var0, boolean var2);

    public static native void setMediaPlaybackConvert(long var0, int var2, int var3, int var4);

    public static native String getRootStatisticsJson(long var0);

    public static native String[] getSubStatisticsJson(long var0);

    public static native String getUUID(long var0);

    public static native int setIntelData(long var0, int var2);

    public static native int renderPrivateData(long var0, int var2, int var3, int var4);

    public static native int setPrivateDataRenderSwitch(long var0, int var2, int var3, int var4, int var5);

    public static native int getPort(long var0);

    public static native int setANRParam(long var0, boolean var2, int var3);

    public static native int setSoundMode(long var0, int var2, int var3);

    public static native int setStreamStrategy(long var0, String var2);

    public static native int setEZPlayerTimeoutConfig(long var0, String var2);

    public static native long createDownloadClient(InitParam var0, String var1);

    public static native long createTimelapseDownloadClient(InitParam var0, String var1);

    public static native int startDownload(long var0);

    public static native int stopDownload(long var0);

    public static native int destroyDownloader(long var0);

    public static native void setDownloadCallback(long var0, EZMediaCallback var2);

    public static native String getDownloadStatistics(long var0);

    public static native void setPingCheckCapabilist(int[] var0);

    public static native int enableFEC(long var0);

    public static native int disableFEC(long var0);

    public static native int getFECPort(long var0, int var2, int var3);

    public static native int setFECWindow(long var0, int var2, Surface var3);

    public static native int setFECFisheyeParam(long var0, int var2, EZFECMediaPlayer.EZFISHEYE_PARAM var3);

    public static native int getFECFisheyeParam(long var0, int var2, EZFECMediaPlayer.EZFISHEYE_PARAM var3);

    public static native int getFECCurrentPTZPort(long var0, boolean var2, float var3, float var4);

    public static native int setFECCurrentPTZPort(long var0, int var2);

    public static native int setFECPTZOutLineShowMode(long var0, int var2);

    public static native int setFECDisplayCallback(long var0, int var2, EZFECMediaPlayer.PlayerFECDisplayCB var3);

    public static native int refreshFECPlay(long var0, int var2, int var3, boolean var4);

    public static native int deleteFECPort(long var0, int var2);

    public static native int setFECPTZParam(long var0, int var2, EZFECMediaPlayer.EZPTZParam var3);

    public static native int getFECPTZParam(long var0, int var2, EZFECMediaPlayer.EZPTZParam var3);

    public static native int setFECPTZZoom(long var0, int var2, float var3);

    public static native float getFECPTZZoom(long var0, int var2);

    public static native int ptzToWindow(long var0, int var2, EZFECMediaPlayer.EZPTZParam var3, EZFECMediaPlayer.EZPTZParam var4, EZFECMediaPlayer.EZPTZParam var5, EZFECMediaPlayer.EZPTZParam var6);

    public static native int setFECPTZColor(long var0, int var2, int var3, int var4, int var5, int var6);

    public static native int setEzvizSSLEffect(long var0, int var2, boolean var3);

    public static native int setFECWidthOffset(long var0, int var2, float var3);

    public static native float getFECWidthOffset(long var0, int var2);

    public static native int setFEC3DRotate(long var0, int var2, EZFECMediaPlayer.EZFECTransformElement var3);

    public static native int setFEC3DRotateABS(long var0, int var2, EZFECMediaPlayer.EZFECTransformElement var3);

    public static native int getFEC3DRotate(long var0, int var2, EZFECMediaPlayer.EZFECTransformElement var3);

    public static native int getFEC3DRotateSpecialViewInfo(long var0, int var2, int var3, EZFECMediaPlayer.EZFECTransformElement var4);

    public static native int setFECAnimation(long var0, int var2, int var3, int var4, int var5);

    public static native int setOverlayFontPath(long var0, String var2);

    public static native int setSubWindow(long var0, int var2);

    public static native int setSubText(long var0, int var2);

    public static native int setPrivatePosInfo(long var0, int var2);

    public static native int setPosBGRectColor(long var0, int var2, int var3, int var4, int var5);

    public static native int generateECDHKey(EZEcdhKeyInfo var0);

    public static native void setClientECDHKey(byte[] var0, int var1, byte[] var2, int var3);

    public static native void setSSLTryCount(int var0);

    public static native void setStreamSaveDebugPath(long var0, String var2);

    public static native void setStreamCount(long var0, int var2);

    public static native int setRotateEffect(long var0, int var2);

    public static native void setDemuxModel(long var0, int var2);

    public static native int setImagePostProcessParameter(long var0, int var2, float var3);

    public static native void enableAutoDefinitionDetect(long var0);

    public static native void setAutoDefinitionDetectParam(long var0, int[] var2);

    public static native int getAutoDefReportParam(long var0, EZAutoDefReportParam var2);

    public static native int setRetryCount(long var0, int var2);

    public static native long EZVQECreateHandle(int var0, int var1, int var2);

    public static native void EZVQEDestroyHandle(long var0);

    public static native void EZVQEEnableAIANR(long var0, int var2);

    public static native void EZVQEConfigDebugDir(long var0, String var2);

    public static native void EZVQESetModelPath(long var0, String var2);

    public static native int EZVQEGetFrameSize(long var0);

    public static native int EZVQEProcess(long var0, byte[] var2, int var3);

    public static native int getHCNetSDKPlaybackHandle(long var0);

    public static native void updateInitParam(long var0, InitParam var2);

    public static native void setRetryStrategy(long var0, EZMediaPlayer.RetryStrategyListener var2);

    public static native int getVideoEncodeType(long var0);
}


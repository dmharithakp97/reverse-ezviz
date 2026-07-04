/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 */
package com.hc.CASClient;

import android.content.Context;
import com.hc.CASClient.CASClientCallback;
import com.hc.CASClient.EZCASConfig;
import com.hc.CASClient.LastDetailError;
import com.hc.CASClient.PRE_CONN_STAT_INFO;
import com.hc.CASClient.ST_ADDRESS_INFO;
import com.hc.CASClient.ST_AUTODEFENCEBIND_INTO;
import com.hc.CASClient.ST_CAPTURE_PIC_INFO;
import com.hc.CASClient.ST_CHAN_GLINTLIGHT_INFO;
import com.hc.CASClient.ST_CLOUDFILE_INFO;
import com.hc.CASClient.ST_CLOUDREPLAY_INFO;
import com.hc.CASClient.ST_COLLECTLOG_INFO;
import com.hc.CASClient.ST_DEV_ALARM_SOUND_INFO;
import com.hc.CASClient.ST_DEV_BASIC_INFO;
import com.hc.CASClient.ST_DEV_DEFENCE_INFO;
import com.hc.CASClient.ST_DEV_FTP_INFO;
import com.hc.CASClient.ST_DEV_INFO;
import com.hc.CASClient.ST_DEV_PERMANENT_KEY;
import com.hc.CASClient.ST_DISPLAY_INFO;
import com.hc.CASClient.ST_FINDFILE_V17;
import com.hc.CASClient.ST_PLAYINFO_V17;
import com.hc.CASClient.ST_POSITION3D_INFO;
import com.hc.CASClient.ST_PTZ_INFO;
import com.hc.CASClient.ST_SEARCH_RECORD_INFO;
import com.hc.CASClient.ST_SERVER_INFO;
import com.hc.CASClient.ST_SETCRUISEPOSITION_INFO;
import com.hc.CASClient.ST_STORAGE_STATUS;
import com.hc.CASClient.ST_STREAM_INFO;
import java.util.List;

public class CASClient {
    public static final int NET_DVR_SYSHEAD = 1;
    public static final int NET_DVR_STREAMDATA = 2;
    public static final int NET_DVR_AUDIOSTREAMDATA = 3;
    public static final int NET_DVR_PLAYBACK_OVER = 100;
    public static final int NET_PLAYBACK_REALOVER = 200;
    public static final int STREAM_STATISTICS = 10;
    public static final int AUDIO_NOTIFY = 20;
    public static final int STREAM_NOTIFY = 30;
    public static final int VOICETALK_BUTTON_NORMAL_CMD = 16640;
    public static final int VOICETALK_BUTTON_PRESS_CMD = 16896;
    public static final int VOICETALK_BUTTON_UNPRESS_CMD = 16897;
    public static final int AUDIO_CODE_TYPE_G722_1 = 0;
    public static final int AUDIO_CODE_TYPE_G711_MU = 1;
    public static final int AUDIO_CODE_TYPE_G711_A = 2;
    public static final int AUDIO_CODE_TYPE_G723 = 3;
    public static final int AUDIO_CODE_TYPE_MP1L2 = 4;
    public static final int AUDIO_CODE_TYPE_MP2L2 = 5;
    public static final int AUDIO_CODE_TYPE_G726 = 6;
    public static final int AUDIO_CODE_TYPE_AAC = 7;
    public static final int AUDIO_CODE_TYPE_RAW = 99;
    public static final int PRE_P2P_ESTABLISHED = 1;
    public static final int PRE_P2P_DISCONNECTED_STREAM_DATA_STOPPED = 2;
    public static final int PRE_P2P_DISCONNECTED_NO_DATA_AFTER_PLAY = 3;
    public static final int PRE_P2P_DISCONNECTED = 4;
    private static CASClient mCASClient = null;

    private CASClient() {
    }

    public static CASClient getInstance() {
        if (null == mCASClient) {
            mCASClient = new CASClient();
        }
        return mCASClient;
    }

    public native boolean initLib(Context var1);

    public native boolean initCrashReport();

    public native boolean setLogPrint(boolean var1, boolean var2);

    public native boolean finiLib();

    public native int getLastError();

    public native int createSession(CASClientCallback var1);

    public native boolean setCallback(int var1, CASClientCallback var2);

    public native boolean destroySession(int var1);

    public native boolean isStoped(int var1);

    public native boolean start(int var1, ST_STREAM_INFO var2, int var3);

    public native boolean stop(int var1);

    public native boolean playbackStart(int var1, ST_STREAM_INFO var2, String var3, String var4);

    public native boolean playbackPause(int var1);

    public native boolean playbackResume(int var1);

    public native boolean playbackStop(int var1);

    public native boolean playbackChangeRate(int var1, int var2);

    public native int voiceTalkStartEx(int var1, ST_STREAM_INFO var2, int var3, int var4);

    public native boolean voiceTalkStop(int var1);

    public native boolean voiceTalkInputDataEx(int var1, byte[] var2, int var3, int var4);

    public native boolean getDevPermanentKey(ST_SERVER_INFO var1, String var2, ST_DEV_INFO var3, ST_DEV_PERMANENT_KEY var4);

    public native boolean formatDisk(ST_SERVER_INFO var1, String var2, ST_DEV_INFO var3, int var4, boolean var5);

    public native boolean getDevStorageStatus(String var1, ST_SERVER_INFO var2, ST_DEV_INFO var3, List<ST_STORAGE_STATUS> var4, boolean var5);

    public native boolean getDevFtpInfo(ST_SERVER_INFO var1, ST_DEV_INFO var2, ST_DEV_FTP_INFO var3);

    public native boolean devDefence(String var1, ST_SERVER_INFO var2, ST_DEV_INFO var3, ST_DEV_DEFENCE_INFO[] var4, int var5, boolean var6);

    public native boolean devUpgrade(String var1, ST_SERVER_INFO var2, ST_DEV_INFO var3, boolean var4);

    public native boolean getPlayInfo(int var1, List<ST_PLAYINFO_V17> var2);

    public native boolean cloudReplayStart(int var1, ST_SERVER_INFO var2, ST_CLOUDREPLAY_INFO var3);

    public native boolean playBackSeek(int var1, String var2);

    public native boolean getDevOperationCodeEx(ST_SERVER_INFO var1, String var2, String var3, String[] var4, int var5, List<ST_DEV_INFO> var6);

    public native boolean cloudUploadStart(int var1, ST_SERVER_INFO var2, ST_CLOUDFILE_INFO var3);

    public native boolean cloudInputData(int var1, byte[] var2, int var3);

    public native boolean cloudUploadStop(int var1);

    public native boolean cloudDownloadStart(int var1, ST_SERVER_INFO var2, ST_CLOUDREPLAY_INFO var3);

    public native boolean cloudDownloadStop(int var1);

    public native int serchRecordFileEx(ST_SERVER_INFO var1, String var2, ST_DEV_INFO var3, ST_SEARCH_RECORD_INFO var4, int var5, List<ST_FINDFILE_V17> var6, boolean var7);

    public native String serchRecordByMounth(ST_SERVER_INFO var1, String var2, ST_DEV_INFO var3, ST_SEARCH_RECORD_INFO var4, boolean var5);

    public native boolean setAlarmSound(String var1, ST_SERVER_INFO var2, ST_DEV_INFO var3, ST_DEV_ALARM_SOUND_INFO var4, boolean var5);

    public native boolean setGlintLight(String var1, ST_SERVER_INFO var2, ST_DEV_INFO var3, ST_CHAN_GLINTLIGHT_INFO[] var4, int var5, boolean var6);

    public native boolean queryGlintLight(String var1, ST_SERVER_INFO var2, ST_DEV_INFO var3, List<ST_CHAN_GLINTLIGHT_INFO> var4, boolean var5);

    public native boolean getLastDetailError(LastDetailError var1);

    public native boolean collectDevLogInfo(String var1, ST_SERVER_INFO var2, ST_DEV_INFO var3, ST_COLLECTLOG_INFO var4, boolean var5);

    public native boolean ptzCtrl(String var1, ST_SERVER_INFO var2, ST_DEV_INFO var3, ST_PTZ_INFO var4, boolean var5);

    public native boolean ptzPresetCtrl(String var1, ST_SERVER_INFO var2, ST_DEV_INFO var3, ST_PTZ_INFO var4, boolean var5);

    public native boolean capturePicture(String var1, ST_SERVER_INFO var2, ST_DEV_INFO var3, ST_CAPTURE_PIC_INFO var4, byte[] var5, int var6, boolean var7);

    public native boolean displayCtrl(String var1, ST_SERVER_INFO var2, ST_DEV_INFO var3, ST_DISPLAY_INFO var4, boolean var5);

    public native boolean forceIFrame(String var1, ST_SERVER_INFO var2, ST_DEV_INFO var3, int var4, int var5, boolean var6);

    public native boolean setSwitchEnable(String var1, ST_SERVER_INFO var2, ST_DEV_INFO var3, int var4, int var5, int var6, boolean var7);

    public native boolean addDetector(String var1, ST_SERVER_INFO var2, ST_DEV_INFO var3, String var4, int var5, String var6, String var7, String var8, boolean var9);

    public native boolean delDetector(String var1, ST_SERVER_INFO var2, ST_DEV_INFO var3, String var4, int var5, String var6, String var7, String var8, boolean var9);

    public native boolean position3D(String var1, ST_SERVER_INFO var2, ST_DEV_INFO var3, ST_POSITION3D_INFO var4, boolean var5);

    public native boolean setCruisePosition(String var1, ST_SERVER_INFO var2, ST_DEV_INFO var3, ST_SETCRUISEPOSITION_INFO var4, boolean var5);

    public native boolean bindBossMAC(String var1, ST_SERVER_INFO var2, ST_DEV_INFO var3, ST_AUTODEFENCEBIND_INTO var4, boolean var5);

    public native int queryBindBossMAC(String var1, ST_SERVER_INFO var2, ST_DEV_INFO var3, ST_AUTODEFENCEBIND_INTO var4, boolean var5);

    public native String getHardwareCodeFromware(Context var1, String var2, String var3);

    public native boolean startPreConnect(int var1, ST_STREAM_INFO var2);

    public native boolean playWithPreConnection(int var1, ST_STREAM_INFO var2, CASClientCallback var3);

    public native boolean stopPlayWithPreConnection(int var1, ST_STREAM_INFO var2);

    public native boolean isPreConnectionSucceed(int var1);

    public native boolean isPrePunching(int var1);

    public native boolean queryP2PDevAddress(int var1, ST_ADDRESS_INFO var2);

    public native boolean stopPreconnection(int var1);

    public native boolean getStatisticInformation(int var1, PRE_CONN_STAT_INFO var2);

    public native boolean setMicroscopeConfig(String var1, ST_SERVER_INFO var2, ST_DEV_INFO var3, int var4, int var5, int var6, int var7, boolean var8);

    public native boolean queryBasicInfo(ST_SERVER_INFO var1, ST_DEV_INFO var2, ST_DEV_BASIC_INFO var3, int var4);

    @Deprecated
    public native boolean setMax43PunchDevices(int var1);

    public boolean setIntConfig(EZCASConfig.IntType type, int value) {
        return this.setIntConfig(type.getType(), value);
    }

    public boolean setStringConfig(EZCASConfig.StringType type, String value) {
        return this.setStringConfig(type.getType(), value);
    }

    private native boolean setIntConfig(int var1, int var2);

    private native boolean setStringConfig(int var1, String var2);
}


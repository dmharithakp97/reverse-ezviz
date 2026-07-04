/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.sun.jna.Callback
 *  com.sun.jna.Library
 *  com.sun.jna.Native
 *  com.sun.jna.Structure
 *  com.sun.jna.Structure$ByReference
 *  com.sun.jna.Structure$ByValue
 */
package com.ez.jna;

import com.ez.jna.EZP2PDevFileJNA;
import com.ez.jna.EZP2PRecordCoverJNA;
import com.ez.p2ptrans.EZP2PBaseFetcher;
import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public interface EZStreamSDKJNA
extends Library {
    public static final EZStreamSDKJNA sEZStreamSDKJNA = (EZStreamSDKJNA)Native.loadLibrary((String)"ezstreamclient", EZStreamSDKJNA.class);

    public void ezstream_setPlaybackConvert(long var1, NET_DVR_COMPRESSION_INFO_V30.ByReference var3);

    public int ezstream_getDevInfo(long var1, boolean var3, EZ_DEV_INFO.ByReference var4);

    public void ezstream_updateDevInfoToCache(String var1, EZ_DEV_INFO.ByReference var2);

    public boolean ezstream_getDevInfoFromCache(String var1, EZ_DEV_INFO.ByReference var2);

    public long ezstream_createEZCASClient(boolean var1);

    public int ezstream_destroyEZCASClient(long var1);

    public int ezstream_transferViaP2P(long var1, EZ_P2PTRANSREQ_INFO.ByReference var3, EZ_P2PTRANSRSP_INFO.ByReference var4);

    public long ez_talk_create(EZ_TALK_PARAM.ByReference var1, MsgCallback var2, NetStatusCallback var3);

    public void ez_talk_destroy(long var1);

    public int ez_talk_start(long var1);

    public void ez_talk_stop(long var1);

    public void ez_talk_set_timeOut(long var1, int var3);

    public void ez_talk_set_clientSession(long var1, String var3);

    public long createRecordCoverFetcher(EZP2PBaseFetcher.EZP2PTransParamForAndroid.ByReference var1);

    public void destroyRecordCoverFetcher(long var1);

    public void setRecordCoverCallback(long var1, EZP2PBaseFetcher.MsgCallback var3, EZP2PBaseFetcher.ErrorCallback var4, EZP2PRecordCoverJNA.RespCallback var5);

    public void startRecordCoverTask(long var1);

    public void stopRecordCoverTask(long var1);

    public int sendRecordFetcherRequest(long var1, EZP2PRecordCoverJNA.EZRecordReq var3);

    public String getRecordCoverBuildStatistics(long var1);

    public long createP2PDevFileFetcher(EZP2PBaseFetcher.EZP2PTransParamForAndroid.ByReference var1);

    public void destroyP2PDevFileFetcher(long var1);

    public void setP2PDevFileCallback(long var1, EZP2PBaseFetcher.MsgCallback var3, EZP2PBaseFetcher.ErrorCallback var4, EZP2PDevFileJNA.RespCallback var5);

    public void startP2PDevFileTask(long var1);

    public void stopP2PDevFileTask(long var1);

    public int sendP2PDevFileRequest(long var1, EZP2PDevFileJNA.EZP2PDevFileReq var3);

    public String getP2PDevFileBuildStatistics(long var1);

    public static interface NetStatusCallback
    extends Callback {
        public int onNetStatus(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8);
    }

    public static interface MsgCallback
    extends Callback {
        public int onMsg(int var1, long var2);
    }

    public static class EZ_TALK_PARAM
    extends Structure {
        public int iClientType;
        public int iServerPort;
        public int iChannelNumber;
        public byte[] szHardwareCode = new byte[128];
        public byte[] szDevSerial = new byte[128];
        public byte[] szServerIP = new byte[128];
        public byte[] szFilePath = new byte[128];
        public byte[] m_szPublicKey = new byte[129];
        public int m_iPublicKeyVersion;
        public byte[] szStreamToken = new byte[513];
        public int iTalkType;
        public byte[] szCallingId = new byte[128];

        protected List<String> getFieldOrder() {
            return Arrays.asList("iClientType", "iServerPort", "iChannelNumber", "szHardwareCode", "szDevSerial", "szServerIP", "szFilePath", "m_szPublicKey", "m_iPublicKeyVersion", "szStreamToken", "iTalkType", "szCallingId");
        }

        public static class ByValue
        extends EZ_TALK_PARAM
        implements Structure.ByValue {
        }

        public static class ByReference
        extends EZ_TALK_PARAM
        implements Structure.ByReference {
        }
    }

    public static class EZ_P2PTRANSRSP_INFO
    extends Structure {
        public byte[] szContent = new byte[1024];
        public int iContentLen;

        protected List<String> getFieldOrder() {
            return Arrays.asList("szContent", "iContentLen");
        }

        public static class ByValue
        extends EZ_P2PTRANSRSP_INFO
        implements Structure.ByValue {
        }

        public static class ByReference
        extends EZ_P2PTRANSRSP_INFO
        implements Structure.ByReference {
        }
    }

    public static class EZ_P2PTRANSREQ_INFO
    extends Structure {
        public byte[] szDevSerial = new byte[128];
        public byte[] szSuperDevSerial = new byte[128];
        public int iDevChannel;
        public byte[] szContent = new byte[1024];
        public int iContentLen;
        public byte[] szUserId = new byte[64];
        public byte[] szServerGroup = new byte[256];
        public int usP2PKeyVer;
        public byte[] szP2PLinkKey = new byte[32];

        protected List<String> getFieldOrder() {
            return Arrays.asList("szDevSerial", "szSuperDevSerial", "iDevChannel", "szContent", "iContentLen", "szUserId", "szServerGroup", "usP2PKeyVer", "szP2PLinkKey");
        }

        public static class ByValue
        extends EZ_P2PTRANSREQ_INFO
        implements Structure.ByValue {
        }

        public static class ByReference
        extends EZ_P2PTRANSREQ_INFO
        implements Structure.ByReference {
        }
    }

    public static class EZ_DEV_INFO
    extends Structure {
        public byte[] szDevSerial = new byte[128];
        public byte[] szOperationCode = new byte[64];
        public byte[] szKey = new byte[64];
        public int iEncryptType;

        protected List<String> getFieldOrder() {
            return Arrays.asList("szDevSerial", "szOperationCode", "szKey", "iEncryptType");
        }

        public static class ByValue
        extends EZ_DEV_INFO
        implements Structure.ByValue {
        }

        public static class ByReference
        extends EZ_DEV_INFO
        implements Structure.ByReference {
        }
    }

    public static class NET_DVR_COMPRESSION_INFO_V30
    extends Structure {
        public byte byStreamType;
        public byte byResolution;
        public byte byBitrateType;
        public byte byPicQuality;
        public int dwVideoBitrate;
        public int dwVideoFrameRate;
        public short wIntervalFrameI;
        public byte byIntervalBPFrame;
        public byte byres1;
        public byte byVideoEncType;
        public byte byAudioEncType;
        public byte byVideoEncComplexity;
        public byte byEnableSvc;
        public byte byFormatType;
        public byte byAudioBitRate;
        public byte byStreamSmooth;
        public byte byAudioSamplingRate;
        public byte bySmartCodec;
        public byte byDepthMapEnable;
        public short wAverageVideoBitrate;

        protected List<String> getFieldOrder() {
            return Arrays.asList("byStreamType", "byResolution", "byBitrateType", "byPicQuality", "dwVideoBitrate", "dwVideoFrameRate", "wIntervalFrameI", "byIntervalBPFrame", "byres1", "byVideoEncType", "byAudioEncType", "byVideoEncComplexity", "byEnableSvc", "byFormatType", "byAudioBitRate", "byStreamSmooth", "byAudioSamplingRate", "bySmartCodec", "byDepthMapEnable", "wAverageVideoBitrate");
        }

        public static class ByValue
        extends NET_DVR_COMPRESSION_INFO_V30
        implements Structure.ByValue {
        }

        public static class ByReference
        extends NET_DVR_COMPRESSION_INFO_V30
        implements Structure.ByReference {
        }
    }
}


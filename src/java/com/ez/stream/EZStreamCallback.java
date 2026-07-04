/*
 * Decompiled with CFR 0.152.
 */
package com.ez.stream;

public interface EZStreamCallback {
    public void onDataCallBack(int var1, byte[] var2, int var3);

    public void onMessageCallBack(int var1, int var2);

    public void onStatisticsCallBack(int var1, String var2);

    public static final class EventType {
        public static final int EZ_VTDU_CACHE_TYPE = 11;
        public static final int EZ_EVENT_DEV_INFO_UPDATED = 100;
        public static final int EZ_P2P_NOTIFY_BASE = 19000;
        public static final int EZ_PRE_P2P_ESTABLISHED = 19001;
        public static final int EZ_PRE_P2P_DISCONNECTED_STREAM_DATA_STOPPED = 19002;
        public static final int EZ_PRE_P2P_DISCONNECTED_NO_DATA_AFTER_PLAY = 19003;
        public static final int EZ_PRE_P2P_DISCONNECTED = 19004;
        public static final int EZ_PRE_P2PSERVER_REDIRECT = 19100;
    }

    public static class StatisticsType {
        public static final int EZ_STATISTICS_DIRECT_PREVIEW = 0;
        public static final int EZ_STATISTICS_PRIVATE_STREAM_PREVIEW = 1;
        public static final int EZ_STATISTICS_P2P_PREVIEW = 2;
        public static final int EZ_STATISTICS_DIRECT_PLAYBACK = 3;
        public static final int EZ_STATISTICS_CLOUD_PLAYBACK = 4;
        public static final int EZ_STATISTICS_PRIVATE_STREAM_PLAYBACK = 5;
        public static final int EZ_STATISTICS_TTS_VOICE = 6;
        public static final int EZ_STATISTICS_DIRECT_VOICE = 7;
        public static final int EZ_STATISTICS_NETSDK_PREVIEW = 8;
        public static final int EZ_STATISTICS_NETSDK_PLAYBACK = 9;
        public static final int EZ_STATISTICS_P2P_PLAYBACK = 10;
        public static final int EZ_STATISTICS_P2P_VOICE = 11;
        public static final int EZ_STATISTICS_QOS_VOICE = 12;
        public static final int EZ_STATISTICS_QOS_TAKL_REAL_NET_STATUS = 101;
    }

    public static class MessageType {
        public static final int EZ_MSG_ERROR_RESULT = 1;
        public static final int EZ_MSG_P2P_STAUTS = 2;
        public static final int EZ_MSG_NEED_TOKKENS = 3;
        public static final int EZ_MSG_SWITCH_CLIENT_TYPE = 5;
        public static final int EZ_MSG_PRECONNECT_CLEARED_WHEN_PLAYING = 6;
        public static final int EZ_MSG_HCNET_EXCEPTION = 8;
    }

    public static class StreamDataType {
        public static final int EZ_STREAM_TYPE_IDLE = 0;
        public static final int EZ_STREAM_TYPE_HEADER = 1;
        public static final int EZ_STREAM_TYPE_DATA = 2;
        public static final int EZ_STREAM_TYPE_AUDIO_DATA = 3;
        public static final int EZ_STREAM_TYPE_STREAMKEY = 4;
        public static final int EZ_STREAM_TYPE_AESMD5 = 5;
        public static final int EZ_STREAM_TYPE_UDP_HEADER = 6;
        public static final int EZ_STREAM_TYPE_FIRST_DATA = 50;
        public static final int EZ_STREAM_TYPE_END = 100;
    }
}


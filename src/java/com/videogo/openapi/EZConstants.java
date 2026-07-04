/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.ezviz.sdk.configwifi.EZConfigWifiErrorEnum
 *  com.ezviz.sdk.configwifi.EZConfigWifiInfoEnum
 *  com.ezviz.sdk.configwifi.mixedconfig.MixedConfigMode
 */
package com.videogo.openapi;

import com.ezviz.sdk.configwifi.EZConfigWifiErrorEnum;
import com.ezviz.sdk.configwifi.EZConfigWifiInfoEnum;
import com.ezviz.sdk.configwifi.mixedconfig.MixedConfigMode;

public class EZConstants {
    public static final int EZ_PTZ_NO_ERROR = 100;
    public static final int MSG_VIDEO_SIZE_CHANGED = 134;
    public static final int MSG_GOT_STREAM_TYPE = 300;
    public static final int PTZ_SPEED_SLOW = 0;
    public static final int PTZ_SPEED_DEFAULT = 1;
    public static final int PTZ_SPEED_FAST = 2;
    public static final int PTZ_UP = 0;
    public static final int PTZ_DOWN = 1;
    public static final int PTZ_LEFT = 2;
    public static final int PTZ_RIGHT = 3;
    public static final int PTZ_FLIP = 4;
    public static final int PTZ_ZOOMIN = 8;
    public static final int PTZ_ZOOMOUT = 9;
    public static final int PTZ_FOCUS_NEAR = 10;
    public static final int PTZ_FOCUS_FAR = 11;
    public static final int PRESET_SET = 7;
    public static final int PRESET_CLEAN = 8;
    public static final int PRESET_GOTO = 9;
    public static final String EZPermisionTypeReal = "Real";
    public static final String EZPermisionTypeReplay = "Replay";

    public static enum EZSmoothPlayMode {
        EZ_SMOOTH_PLAY_MODE_AUTO_ADJUST_LATENCY(0),
        EZ_SMOOTH_PLAY_MODE_AUTO_INCREMENTAL_LATENCY(1),
        EZ_SMOOTH_PLAY_MODE_STATIC_LATENCY(2);

        public int mode;

        private EZSmoothPlayMode(int mode) {
            this.mode = mode;
        }
    }

    public static enum EZPlayerViewRotationAngle {
        EZ_PLAYER_VIEW_ROTATION_ANGLE_0(0),
        EZ_PLAYER_VIEW_ROTATION_ANGLE_90(90),
        EZ_PLAYER_VIEW_ROTATION_ANGLE_180(180),
        EZ_PLAYER_VIEW_ROTATION_ANGLE_270(270);

        public int angle;

        private EZPlayerViewRotationAngle(int angle) {
            this.angle = angle;
        }
    }

    public static class EZBWCheckType {
        public static int EZBWCheckClientUpward = 1;
        public static int EZBWCheckClientDownward = 2;
    }

    public static enum EZVoiceChangeType {
        EZ_VOICE_CHANGE_TYPE_MAN(-7),
        EZ_VOICE_CHANGE_TYPE_NORMAL(0),
        EZ_VOICE_CHANGE_TYPE_CLOWN(7);

        public int voiceType;

        private EZVoiceChangeType(int voiceType) {
            this.voiceType = voiceType;
        }
    }

    public static enum EZFecPlaceType {
        EZ_FEC_PLACE_NONE(0),
        EZ_FEC_PLACE_WALL(1),
        EZ_FEC_PLACE_FLOOR(2),
        EZ_FEC_PLACE_CEILING(3);

        public int fecPlaceType;

        private EZFecPlaceType(int fecPlaceType) {
            this.fecPlaceType = fecPlaceType;
        }
    }

    public static enum EZFecCorrectType {
        EZ_FEC_CORRECT_NONE(0),
        EZ_FEC_CORRECT_4PTZ(1),
        EZ_FEC_CORRECT_180(2),
        EZ_FEC_CORRECT_360(4),
        EZ_FEC_CORRECT_FISH(8),
        EZ_FEC_CORRECT_LAT(16),
        EZ_FEC_CORRECT_ARC_HOR(32),
        EZ_FEC_CORRECT_ARC_VER(64),
        EZ_FEC_CORRECT_PICINPIC(128),
        EZ_FEC_CORRECT_FULL5PTZ(256),
        EZ_FEC_CORRECT_5PTZ(512),
        EZ_FEC_CORRECT_CYC(1024),
        EZ_FEC_CORRECT_WIDEANGLE(2048),
        EZ_FEC_CORRECT_TILED(4096),
        EZ_FEC_CORRECT_LISTENINGRECOGNITION(8192);

        public int fecCorrectType;

        private EZFecCorrectType(int fecCorrectType) {
            this.fecCorrectType = fecCorrectType;
        }
    }

    public static enum EZCloudVideoType {
        EZ_CLOUD_VIDEO_TYPE_ALL(-1),
        EZ_CLOUD_VIDEO_TYPE_SERIES(1),
        EZ_CLOUD_VIDEO_TYPE_ACT(2);

        public int videoType;

        private EZCloudVideoType(int videoType) {
            this.videoType = videoType;
        }
    }

    public static enum EZVideoRecordLocation {
        EZ_VIDEO_RECORD_LOCATION_LOCAL(1),
        EZ_VIDEO_RECORD_LOCATION_CVR(2);

        public int location;

        private EZVideoRecordLocation(int location) {
            this.location = location;
        }
    }

    public static enum EZVideoRecordTypeEx {
        EZ_VIDEO_RECORD_TYPE_NONE(0),
        EZ_VIDEO_RECORD_TYPE_CMR(1),
        EZ_VIDEO_RECORD_TYPE_EVENT(2),
        EZ_VIDEO_RECORD_TYPE_CAR(3),
        EZ_VIDEO_RECORD_TYPE_PERSON(4),
        EZ_VIDEO_RECORD_TYPE_COMPRESS_AUTO(5),
        EZ_VIDEO_RECORD_TYPE_COMPRESS_CMR(6),
        EZ_VIDEO_RECORD_TYPE_COMPRESS_MANUAL(7);

        public int recordType;

        private EZVideoRecordTypeEx(int recordType) {
            this.recordType = recordType;
        }
    }

    public static enum EZVideoRecordType {
        EZ_VIDEO_RECORD_TYPE_ALL("all"),
        EZ_VIDEO_RECORD_TYPE_CMR("CMR"),
        EZ_VIDEO_RECORD_TYPE_Event("event");

        public String recordType;

        private EZVideoRecordType(String recordType) {
            this.recordType = recordType;
        }
    }

    @Deprecated
    public static enum EZCloudPlaybackRate {
        EZ_CLOUD_PLAYBACK_RATE_1(1.0f, 1),
        EZ_CLOUD_PLAYBACK_RATE_4(4.0f, 4),
        EZ_CLOUD_PLAYBACK_RATE_8(8.0f, 6),
        EZ_CLOUD_PLAYBACK_RATE_16(16.0f, 8),
        EZ_CLOUD_PLAYBACK_RATE_32(32.0f, 10);

        public double speed = 0.0;
        public int value = 0;

        private EZCloudPlaybackRate(float speed, int value) {
            this.speed = speed;
            this.value = value;
        }
    }

    public static enum EZPlaybackRate {
        EZ_PLAYBACK_RATE_16_1(0.0625, 9),
        EZ_PLAYBACK_RATE_8_1(0.125, 7),
        EZ_PLAYBACK_RATE_4_1(0.25, 5),
        EZ_PLAYBACK_RATE_2_1(0.5, 3),
        EZ_PLAYBACK_RATE_1(1.0, 1),
        EZ_PLAYBACK_RATE_2(2.0, 2),
        EZ_PLAYBACK_RATE_4(4.0, 4),
        EZ_PLAYBACK_RATE_8(8.0, 6),
        EZ_PLAYBACK_RATE_16(16.0, 8),
        EZ_PLAYBACK_RATE_32(32.0, 10);

        public double speed = 0.0;
        public int value = 0;

        private EZPlaybackRate(double speed, int value) {
            this.speed = speed;
            this.value = value;
        }
    }

    public static class EZWiFiConfigMode {
        public static int EZWiFiConfigSmart = MixedConfigMode.EZWiFiConfigSmart;
        public static int EZWiFiConfigWave = MixedConfigMode.EZWiFiConfigWave;
    }

    public static enum EZWifiConfigStatus {
        DEVICE_WIFI_CONNECTING(EZConfigWifiInfoEnum.CONNECTING_TO_WIFI),
        DEVICE_WIFI_CONNECTED(EZConfigWifiInfoEnum.CONNECTED_TO_WIFI),
        DEVICE_PLATFORM_REGISTED(EZConfigWifiInfoEnum.CONNECTED_TO_PLATFORM),
        TIME_OUT(EZConfigWifiErrorEnum.CONFIG_TIMEOUT);

        public int code;
        public String description;

        private EZWifiConfigStatus(int code, String description) {
            this.code = code;
            this.description = description;
        }

        private EZWifiConfigStatus(EZConfigWifiInfoEnum info) {
            this.code = info.code;
            this.description = info.description;
        }

        private EZWifiConfigStatus(EZConfigWifiErrorEnum error) {
            this.code = error.code;
            this.description = error.description;
        }
    }

    public static enum EZDefenceStatus {
        EZDefence_IPC_CLOSE(0),
        EZDefence_IPC_OPEN(1),
        EZDefence_ALARMHOST_SLEEP(0),
        EZDefence_ALARMHOST_ATHOME(8),
        EZDefence_ALARMHOST_OUTER(16);

        private int status;

        private EZDefenceStatus(int status) {
            this.status = status;
        }

        public int getStatus() {
            return this.status;
        }
    }

    public static final class EZNotificationDataType
    extends Enum<EZNotificationDataType> {
        private static final /* synthetic */ EZNotificationDataType[] $VALUES;

        public static EZNotificationDataType[] values() {
            return (EZNotificationDataType[])$VALUES.clone();
        }

        public static EZNotificationDataType valueOf(String name) {
            return Enum.valueOf(EZNotificationDataType.class, name);
        }

        private static /* synthetic */ EZNotificationDataType[] $values() {
            return new EZNotificationDataType[0];
        }

        static {
            $VALUES = EZNotificationDataType.$values();
        }
    }

    public static enum EZTalkbackCapability {
        EZTalkbackNoSupport(0),
        EZTalkbackFullDuplex(1),
        EZTalkbackHalfDuplex(3);

        private int capability;

        private EZTalkbackCapability(int capability) {
            this.capability = capability;
        }

        public int getCapability() {
            return this.capability;
        }
    }

    public static enum EZPTZDisplayCommand {
        EZPTZDisplayCommandFlip(4);

        private int mCommand;

        private EZPTZDisplayCommand(int command) {
            this.mCommand = command;
        }

        public int getCommand() {
            return this.mCommand;
        }
    }

    public static enum EZPTZAction {
        EZPTZActionSTART("START"),
        EZPTZActionSTOP("STOP");

        private String mAction = null;

        private EZPTZAction(String action) {
            this.mAction = action;
        }

        public String getAction() {
            return this.mAction;
        }
    }

    public static enum EZPTZCommand {
        EZPTZCommandLeft(2),
        EZPTZCommandRight(3),
        EZPTZCommandUp(0),
        EZPTZCommandDown(1),
        EZPTZCommandZoomIn(8),
        EZPTZCommandZoomOut(9),
        EZPTZCommandFocusNear(10),
        EZPTZCommandFocusFar(11);

        private int mCommand;

        private EZPTZCommand(int command) {
            this.mCommand = command;
        }

        public int getCommand() {
            return this.mCommand;
        }
    }

    public static enum EZVideoLevel {
        VIDEO_LEVEL_4K(6),
        VIDEO_LEVEL_3K(5),
        VIDEO_LEVEL_EXTREMECLEAR(4),
        VIDEO_LEVEL_SUPERCLEAR(3),
        VIDEO_LEVEL_HD(2),
        VIDEO_LEVEL_BALANCED(1),
        VIDEO_LEVEL_FLUNET(0);

        private int mVideoLevel;

        private EZVideoLevel(int videoLevel) {
            this.mVideoLevel = videoLevel;
        }

        public int getVideoLevel() {
            return this.mVideoLevel;
        }
    }

    public static enum EZVideoQuality {
        VIDEO_PRIORITY_NONE(0),
        VIDEO_PERFORMANCE_PRIORITY(1),
        VIDEO_QUALITY_PRIORITY(2);

        private int mVideoQuality;

        private EZVideoQuality(int mVideoQuality) {
        }
    }

    public static enum EZLeaveMsgType {
        EZLeaveMsgTypeVoice(1),
        EZLeaveMsgTypeVideo(2);

        private int mLeaveMsgType;

        private EZLeaveMsgType(int type) {
            this.mLeaveMsgType = type;
        }

        public int getLeaveMsgType() {
            return this.mLeaveMsgType;
        }
    }

    public static enum EZMessageType {
        EZMessageTypeAlarm(1),
        EZMessageTypeLeave(2);

        private int mMessageType;

        private EZMessageType(int type) {
            this.mMessageType = type;
        }

        public int getMessageType() {
            return this.mMessageType;
        }
    }

    public static enum EZMessageStatus {
        EZMessageStatusUnRead(1),
        EZMessageStatusRead(2);

        private int mMessageStatus;

        private EZMessageStatus(int status) {
            this.mMessageStatus = status;
        }

        public int getStatus() {
            return this.mMessageStatus;
        }

        public void setStatus(int mAlarmSTatus) {
            this.mMessageStatus = mAlarmSTatus;
        }
    }

    public static enum EZAlarmStatus {
        EZAlarmStatusRead(2);

        private int mAlarmStatus;

        private EZAlarmStatus(int status) {
            this.mAlarmStatus = status;
        }

        public int getAlarmStatus() {
            return this.mAlarmStatus;
        }

        public void setAlarmSTatus(int mAlarmSTatus) {
            this.mAlarmStatus = mAlarmSTatus;
        }
    }

    public static enum EZAlarmType {
        EZAlarmTypeAll(-1);

        private int mTypeId;

        private EZAlarmType(int id) {
            this.mTypeId = id;
        }

        public int getTypeId() {
            return this.mTypeId;
        }

        public void setTypeId(int mTypeId) {
            this.mTypeId = mTypeId;
        }
    }

    public static enum EZSMSType {
        EZSMSTypeSecure(2),
        EZSMSTypeOperate(3);

        private int mSmsType;

        private EZSMSType(int type) {
            this.mSmsType = type;
        }

        public int getSmsType() {
            return this.mSmsType;
        }
    }

    public static class EZPlaybackConstants {
        public static final int MSG_REMOTEPLAYBACK_PLAY_FINISH = 201;
        public static final int MSG_CAPTURE_PICTURE_SUCCESS = 202;
        public static final int MSG_CAPTURE_PICTURE_FAIL = 203;
        public static final int MSG_REMOTEPLAYBACK_PLAY_SUCCUSS = 205;
        public static final int MSG_REMOTEPLAYBACK_PLAY_FAIL = 206;
        public static final int MSG_REMOTEPLAYBACK_RATIO_CHANGED = 207;
        public static final int MSG_REMOTEPLAYBACK_CONNECTION_EXCEPTION = 208;
        public static final int MSG_REMOTEPLAYBACK_ENCRYPT_PASSWORD_ERROR = 209;
        public static final int MSG_REMOTEPLAYBACK_PASSWORD_ERROR = 210;
        public static final int MSG_START_RECORD_SUCCESS = 212;
        public static final int MSG_START_RECORD_FAIL = 213;
        public static final int MSG_REMOTEPLAYBACK_SEARCH_FILE_SUCCUSS = 214;
        public static final int MSG_REMOTEPLAYBACK_SEARCH_FILE_FAIL = 215;
        public static final int MSG_REMOTEPLAYBACK_SEARCH_NO_FILE = 216;
        public static final int MSG_REMOTEPLAYBACK_PLAY_START = 217;
        public static final int MSG_REMOTEPLAYBACK_CONNECTION_START = 218;
        public static final int MSG_REMOTEPLAYBACK_CONNECTION_SUCCESS = 219;
        public static final int MSG_GET_CAMERA_INFO_SUCCESS = 220;
        public static final int MSG_REMOTEPLAYBACK_STOP_SUCCESS = 221;
        public static final int MSG_REMOTE_PLAYBACK_RATE_LOWER = 222;
        public static final int MSG_REMOTE_PLAYBACK_PLAY_PREPARED = 223;
    }

    public class EZRealPlayConstants {
        public static final int TALK_HALF_DUPLEX = 3;
        public static final int TALK_FULL_DUPLEX = 1;
        public static final int MSG_GET_CAMERA_INFO_SUCCESS = 100;
        public static final int MSG_GET_CAMERA_INFO_FAIL = 101;
        public static final int MSG_REALPLAY_PLAY_SUCCESS = 102;
        public static final int MSG_REALPLAY_PLAY_FAIL = 103;
        public static final int MSG_SET_VEDIOMODE_SUCCESS = 105;
        public static final int MSG_SET_VEDIOMODE_FAIL = 106;
        public static final int MSG_START_RECORD_SUCCESS = 107;
        public static final int MSG_START_RECORD_FAIL = 108;
        public static final int MSG_REALPLAY_PASSWORD_ERROR = 111;
        public static final int MSG_REALPLAY_ENCRYPT_PASSWORD_ERROR = 112;
        public static final int MSG_REALPLAY_VOICETALK_SUCCESS = 113;
        public static final int MSG_REALPLAY_VOICETALK_FAIL = 114;
        public static final int MSG_REALPLAY_VOICETALK_STOP = 115;
        public static final int MSG_REALPLAY_UPDATE_TALK_VOLUME = 116;
        public static final int MSG_PTZ_GET_SUCCESS = 122;
        public static final int MSG_PTZ_SET_SUCCESS = 123;
        public static final int MSG_PTZ_SET_FAIL = 124;
        public static final int MSG_REALPLAY_PLAY_START = 125;
        public static final int MSG_REALPLAY_CONNECTION_START = 126;
        public static final int MSG_REALPLAY_CONNECTION_SUCCESS = 127;
        public static final int MSG_REALPLAY_CHECK_SUCCESS = 131;
        public static final int MSG_REALPLAY_CHECK_FAIL = 132;
        public static final int MSG_REALPLAY_STOP_SUCCESS = 133;
        public static final int MSG_REALPLAY_PLAY_PREPARED = 139;
        public static final int MSG_PRIVATE_TOKEN_GET_SUCCESS = 135;
        public static final int MSG_VIDEO_LEVEL_AUTO_IMPROVE = 136;
        public static final int MSG_VIDEO_LEVEL_AUTO_REDUCE = 137;
        public static final int MSG_SET_VEDIOMODE_AUTO_SUCCESS = 138;
    }
}


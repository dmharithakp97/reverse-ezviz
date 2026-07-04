/*
 * Decompiled with CFR 0.152.
 */
package com.ez.stream;

import com.ez.jna.EZStreamSDKJNA;
import com.ez.stream.DownloadCloudParam;
import com.ez.stream.EZStreamCallback;
import com.ez.stream.InitParam;
import com.ez.stream.Utils;

public interface IClient {
    public static final int CLIENT_TYPE_NONE = -1;
    public static final int CLIENT_TYPE_PRIVATE_STREAM = 0;
    public static final int CLIENT_TYPE_P2P = 1;
    public static final int CLIENT_TYPE_DIRECT_INNER = 2;
    public static final int CLIENT_TYPE_DIRECT_OUTER = 3;
    public static final int CLIENT_TYPE_CLOUD_PLAYBACK = 4;
    public static final int CLIENT_TYPE_CLOUD_RECORDING = 5;
    public static final int CLIENT_TYPE_DIRECT_REVERSE = 6;
    public static final int CLIENT_TYPE_HCNETSDK = 7;
    public static final int CLIENT_TYPE_ANT_PROXY = 8;
    public static final int GLOBAL_EVENT_PINGCHECK = 1;
    public static final int GLOBAL_EVENT_SEEK = 2;

    public int startPreview();

    public int stopPreview();

    public int setCallback(EZStreamCallback var1);

    public int startPlayback(String var1, String var2, String var3);

    public int stopPlayback();

    public void release();

    public int setPlaybackRate(int var1);

    public DevInfo getDevInfo(boolean var1);

    public int startDownloadFromCloud(DownloadCloudParam var1);

    public int stopDownloadFromCloud();

    public int updateParam(InitParam var1);

    public int getClientType();

    public void setPlayPort(int var1);

    public void setDataCallback2Java(boolean var1);

    public void setPlaybackConvert(int var1, int var2, int var3);

    public int cloudPlaybackControl(int var1, String var2, int var3);

    public static class DevInfo {
        public String szDevSerial;
        public String szOperationCode;
        public String szKey;
        public int iEncryptType;

        public static DevInfo readFrom(EZStreamSDKJNA.EZ_DEV_INFO info) {
            DevInfo dev = null;
            if (info != null) {
                dev = new DevInfo();
                info.read();
                dev.szDevSerial = Utils.byteArray2String(info.szDevSerial);
                dev.szOperationCode = Utils.byteArray2String(info.szOperationCode);
                dev.szKey = Utils.byteArray2String(info.szKey);
                dev.iEncryptType = info.iEncryptType;
            }
            return dev;
        }

        public void writeTo(EZStreamSDKJNA.EZ_DEV_INFO info) {
            byte[] data = null;
            boolean len = false;
            if (info != null) {
                data = this.szDevSerial.getBytes();
                System.arraycopy(data, 0, info.szDevSerial, 0, data.length);
                data = this.szOperationCode.getBytes();
                System.arraycopy(data, 0, info.szOperationCode, 0, data.length);
                data = this.szKey.getBytes();
                System.arraycopy(data, 0, info.szKey, 0, data.length);
                info.iEncryptType = this.iEncryptType;
                info.write();
            }
        }

        public EZStreamSDKJNA.EZ_DEV_INFO.ByReference toEZ_DEV_INFO() {
            EZStreamSDKJNA.EZ_DEV_INFO.ByReference info = new EZStreamSDKJNA.EZ_DEV_INFO.ByReference();
            byte[] data = this.szDevSerial.getBytes();
            System.arraycopy(data, 0, info.szDevSerial, 0, data.length);
            data = this.szOperationCode.getBytes();
            System.arraycopy(data, 0, info.szOperationCode, 0, data.length);
            data = this.szKey.getBytes();
            System.arraycopy(data, 0, info.szKey, 0, data.length);
            info.iEncryptType = this.iEncryptType;
            info.write();
            return info;
        }
    }

    public static class EZCloudPlaybackOp {
        public static final int EZ_CLOUD_PLAYBACK_OP_PLAY = 0;
        public static final int EZ_CLOUD_PLAYBACK_OP_PAUSE = 1;
        public static final int EZ_CLOUD_PLAYBACK_OP_RESUME = 2;
        public static final int EZ_CLOUD_PLAYBACK_OP_SPEED = 3;
        public static final int EZ_CLOUD_PLAYBACK_OP_SEEK = 4;
        public static final int EZ_CLOUD_PLAYBACK_OP_RETRY = 5;
    }
}


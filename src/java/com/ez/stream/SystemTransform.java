/*
 * Decompiled with CFR 0.152.
 */
package com.ez.stream;

import com.ez.stream.EZGetPercentInfo;
import com.ez.stream.EZMediaTransferInfo;

public class SystemTransform {
    public static final int TRANS_SYSTEM_NULL = 0;
    public static final int TRANS_SYSTEM_HIK = 1;
    public static final int TRANS_SYSTEM_MPEG2_PS = 2;
    public static final int TRANS_SYSTEM_MPEG2_TS = 3;
    public static final int TRANS_SYSTEM_RTP = 4;
    public static final int TRANS_SYSTEM_MPEG4 = 5;
    public static final int TRANS_SYSTEM_ASF = 6;
    public static final int TRANS_SYSTEM_AVI = 7;
    public static final int TRANS_SYSTEM_RAW = 16;
    public static final int MULTI_DATA = 0;
    public static final int AUDIO_DATA = 1;
    public static final int SECRET_NONE = 0;
    public static final int SECRET_AES = 1;

    public static native long create(byte[] var0, int var1, int var2, String var3, OutputDataCB var4);

    public static native long createEx(int var0, String var1);

    public static native int release(long var0);

    public static native int setSegmentInterval(long var0, int var2);

    public static native int start(long var0, String var2, String var3);

    public static native int startEx(long var0);

    public static native int inputData(long var0, int var2, byte[] var3, int var4);

    public static native EZGetPercentInfo getPercent(long var0);

    public static native int stop(long var0);

    public static native int setEncryptKey(long var0, int var2, String var3);

    public static native EZMediaTransferInfo getFileInfo(long var0, boolean var2);

    public static native void setTargetInfo(long var0, EZMediaTransferInfo var2, String var3);

    public static byte[] getHeaderForRecording(byte[] header, int type) {
        if (type == 5) {
            if (header[13] == 113 && header[12] == 16 || header[13] == 113 && header[12] == 17 || header[13] == 32 && header[12] == 1) {
                return header;
            }
            byte[] bytes = new byte[header.length];
            for (int i = 0; i < 12 && i < header.length; ++i) {
                bytes[i] = header[i];
            }
            return bytes;
        }
        return header;
    }

    public static interface OutputDataCB {
        public void onOutputData(byte[] var1, int var2, int var3, int var4);
    }
}


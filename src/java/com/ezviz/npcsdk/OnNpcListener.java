/*
 * Decompiled with CFR 0.152.
 */
package com.ezviz.npcsdk;

public interface OnNpcListener {
    public void onDataBack(int var1, byte[] var2, int var3);

    public void onAudioParamsDataBack(int var1, int var2, int var3, int var4, int var5, int var6);

    public void onError(int var1);

    public static class MessageType {
        public static final int EZ_MSG_TYPE_RESULT = 0;
        public static final int EZ_MSG_TYPE_HEADER = 1;
        public static final int EZ_MSG_TYPE_FIRST_DATA = 2;
        public static final int EZ_MSG_TYPE_VIDEO = 3;
        public static final int EZ_MSG_TYPE_AUDIO = 4;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.ez.stream;

import com.ez.stream.EZStreamCallback;

public interface IVoiceStream {
    public int startVoiceTalk();

    public String startVoiceTalkV2();

    public int stopVoiceTalk();

    public int inputVoiceTalkData(byte[] var1, int var2, int var3);

    public int setCallback(EZStreamCallback var1);

    public int isQosTalk();

    public void release();

    public int switchMic(int var1);
}


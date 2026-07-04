/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.media.MediaCodec
 *  android.media.MediaCodec$BufferInfo
 *  android.media.MediaFormat
 */
package com.ez.transcode;

import android.media.MediaCodec;
import android.media.MediaFormat;

public interface ConvertCallback {
    public void onError(String var1);

    public void onOutputFormatChanged(MediaCodec var1, MediaFormat var2);

    public void onInputBufferAvailable(MediaCodec var1, int var2);

    public void onOutputBufferAvailable(MediaCodec var1, int var2, MediaCodec.BufferInfo var3);
}


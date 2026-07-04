/*
 * Decompiled with CFR 0.152.
 */
package com.ez.transcode;

public class DataBuffer {
    public byte[] data;
    public int len;
    public long timeStamp;
    public long frameType;

    public DataBuffer(byte[] data, int len, long timeStamp, long frameType) {
        this.data = data;
        this.len = len;
        this.timeStamp = timeStamp;
        this.frameType = frameType;
    }
}


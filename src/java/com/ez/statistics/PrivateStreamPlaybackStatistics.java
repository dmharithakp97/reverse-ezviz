/*
 * Decompiled with CFR 0.152.
 */
package com.ez.statistics;

import com.ez.statistics.BasePreviewStatistics;

public class PrivateStreamPlaybackStatistics
extends BasePreviewStatistics {
    public String systemName = "app_video_playback_vtdu";
    public String vtmIP;
    public int vtmPort;
    public int t1;
    public int r1;
    public int t2;
    public int r2;
    public int t3;
    public int r3;
    public String vtduIP;
    public int vtduPort;
    public int t4;
    public int r4;
    public int t5;
    public int r5;
    public int connectvtdutime = -1;
    public int vtdusignaltime = -1;
    public int connectproxytime = -1;
    public int proxysignaltime = -1;
    public int connectvtmtime = -1;
    public int udpFlag = 0;
    public int firstTransDelay = -1;
    public int lagTimes = -1;
    public int maxDelay = -1;
    public int freqDelay = -1;
    public int maxLossPacketRate = -1;
    public int freqLossPacketRate = -1;
}


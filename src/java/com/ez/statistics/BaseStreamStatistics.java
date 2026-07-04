/*
 * Decompiled with CFR 0.152.
 */
package com.ez.statistics;

import com.ez.statistics.BaseStatistics;
import java.util.concurrent.atomic.AtomicBoolean;

public class BaseStreamStatistics
extends BaseStatistics {
    public int via = -1;
    public int b = -1;
    public int c = -1;
    public int d = -1;
    public int e = -1;
    public int t = -1;
    public int r = -1;
    public int decd = 0;
    public long sbt = -1L;
    public long sst = -1L;
    public long flow = 0L;
    public float delaySlight = 0.0f;
    public float delayMiddle = 0.0f;
    public float delaySerious = 0.0f;
    public AtomicBoolean __isDone = new AtomicBoolean(false);
    public String uuid;
    public long _startTime = -1L;
    public long _endTime = -1L;
}


/*
 * Decompiled with CFR 0.152.
 */
package com.ez.statistics;

import com.ez.statistics.BaseVoiceTalkStatistics;

public class QosTalkStatistics
extends BaseVoiceTalkStatistics {
    public String systemName;
    public int ver = 1;
    public int lastR = 0;
    public int rtt;
    public int rtt_0_10;
    public int rtt_10_20;
    public int rtt_20_50;
    public int rtt_50_100;
    public int rtt_0_250 = 0;
    public int rtt_250_500 = 0;
    public int rtt_500_1000 = 0;
    public int rtt_1000 = 0;
    public int rtt_max = 0;
    public int realRtt_0_250 = 0;
    public int realRtt_250_500 = 0;
    public int realRtt_500_1000 = 0;
    public int realRtt_1000 = 0;
    public int realRtt_max = 0;
    public int plp_0_10 = 0;
    public int plp_10_20 = 0;
    public int plp_20_30 = 0;
    public int plp_30 = 0;
    public int plp_max = 0;
    public int plp_average = 0;
    int frozenRate;
    public int total = 0;
    String hardwareCode;
    long timestap;
    String serverIP;
    int serverPort;
    int roomID;
    int nType;
}


/*
 * Decompiled with CFR 0.152.
 */
package com.ez.statistics;

public class PingCheckDef {

    public static class PingCheckReq {
        public String uuid;
        public long eventTime;
        public String vtmHost = "";
        public String netHost;
        public int errCode = 0;
        public int type = 0;
    }

    public static class PingCheckRsp {
        public String systemName = "app_netcheck_ping";
        public String uuid;
        public long eventTime;
        public int errorCode = 0;
        public int type = -1;
        public String vtmhost;
        public int iVtmSend;
        public int iVtmSuccess;
        public float vtmLost;
        public int iVtmAvg;
        public int iVtmMax;
        public int iVtmMin;
        public int iVtmStatus = 0;
        public String netHost;
        public int iNetSend;
        public int iNetSuccess;
        public float netLost;
        public int iNetAvg;
        public int iNetMax;
        public int iNetMin;
        public int iNetStatus = 0;
        public int isNetAvailable = -1;
    }

    public static class PingCheckSingleRsp {
        public String host = "";
        public int iSend = 5;
        public int iSuccess = 0;
        public int iAvg = 0;
        public int iMax = 0;
        public int iMin = 0;
    }
}


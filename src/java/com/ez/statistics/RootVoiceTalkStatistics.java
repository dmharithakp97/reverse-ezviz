/*
 * Decompiled with CFR 0.152.
 */
package com.ez.statistics;

import com.ez.statistics.BaseStatistics;
import com.ez.statistics.DirectVoiceTalkStatistics;
import com.ez.statistics.PingCheckDef;
import com.ez.statistics.QosTalkStatistics;
import com.ez.statistics.StatisticsGson;
import com.ez.statistics.TTSVoiceTalkStatistics;
import java.util.ArrayList;
import java.util.UUID;

public class RootVoiceTalkStatistics {
    public ArrayList<TTSVoiceTalkStatistics> mTTSVoiceTalkStatisticsList = new ArrayList();
    public ArrayList<DirectVoiceTalkStatistics> mDirectVoiceTalkStatisticsList = new ArrayList();
    public ArrayList<QosTalkStatistics> mQosTalkStatisticsList = new ArrayList();
    public PingCheckDef.PingCheckRsp mPingStatistics;
    public String uuid = UUID.randomUUID().toString();
    public int mSeq = 0;
    public int talkType = -1;
    public int r = -2;
    public String sn;
    public long lst = -1L;
    public String lslid;
    public int lsctype = -1;
    public int semi = -1;
    public int flowin = 0;
    public int flowout = 0;
    public long stream_bt = -1L;
    public long stream_et = -1L;
    public long engine_bt = -1L;
    public long engine_et = -1L;
    public int eg_mode = 0;
    public int router = -1;
    public int sco_t = -1;
    public int sco_state = -1;

    public ArrayList<String> getAllSubStatisticeJson() {
        ArrayList<String> subStatisticeJsons = new ArrayList<String>();
        BaseStatistics.flatmapStatistics(this.mDirectVoiceTalkStatisticsList, subStatisticeJsons);
        BaseStatistics.flatmapStatistics(this.mTTSVoiceTalkStatisticsList, subStatisticeJsons);
        BaseStatistics.flatmapStatistics(this.mQosTalkStatisticsList, subStatisticeJsons);
        return subStatisticeJsons;
    }

    private ArrayList<String> getAllTTSSubStatisticeJson() {
        ArrayList<String> subStatisticeJsons = new ArrayList<String>();
        BaseStatistics.flatmapStatistics(this.mTTSVoiceTalkStatisticsList, subStatisticeJsons);
        return subStatisticeJsons;
    }

    private ArrayList<String> getAllDirectSubStatisticeJson() {
        ArrayList<String> subStatisticeJsons = new ArrayList<String>();
        BaseStatistics.flatmapStatistics(this.mDirectVoiceTalkStatisticsList, subStatisticeJsons);
        return subStatisticeJsons;
    }

    private ArrayList<String> getAllmQosTalkStatisticsList() {
        ArrayList<String> subStatisticeJsons = new ArrayList<String>();
        BaseStatistics.flatmapStatistics(this.mQosTalkStatisticsList, subStatisticeJsons);
        return subStatisticeJsons;
    }

    public String getPingStaticJson() {
        return StatisticsGson.toJson(this.mPingStatistics);
    }

    public String getRootStaticJson() {
        RootVoice root = new RootVoice();
        root.r = this.r;
        root.talkType = this.talkType;
        root.uuid = this.uuid;
        root.sn = this.sn;
        root.lst = this.lst;
        root.lslid = this.lslid;
        root.lsctype = this.lsctype;
        root.semi = this.semi;
        root.flowin = this.flowin + 1023 >> 10;
        root.flowout = this.flowout + 1023 >> 10;
        root.stream_bt = this.stream_bt;
        root.stream_et = this.stream_et;
        root.engine_bt = this.engine_bt;
        root.engine_et = this.engine_et;
        root.eg_mode = this.eg_mode;
        root.router = this.router;
        root.sco_state = this.sco_state;
        root.sco_t = this.sco_t;
        return root.toJson();
    }

    static class RootVoice
    extends BaseStatistics {
        public int talkType = -1;
        public int r = -2;
        public String uuid = "";
        public String sn;
        public long lst = -1L;
        public String lslid;
        public int lsctype = -1;
        public int semi = -1;
        public int flowin = 0;
        public int flowout = 0;
        public long stream_bt = -1L;
        public long stream_et = -1L;
        public long engine_bt = -1L;
        public long engine_et = -1L;
        public int eg_mode = 0;
        public int router = -1;
        public int sco_t = -1;
        public int sco_state = -1;

        RootVoice() {
        }
    }
}


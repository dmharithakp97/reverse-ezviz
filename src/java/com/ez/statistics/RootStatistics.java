/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 */
package com.ez.statistics;

import com.ez.statistics.BaseStatistics;
import com.ez.statistics.CloudPlaybackStatistics;
import com.ez.statistics.DirectPlaybackStatistics;
import com.ez.statistics.DirectPreviewStatistics;
import com.ez.statistics.NetSDKPlaybackStatistics;
import com.ez.statistics.NetSDKPreviewStatistics;
import com.ez.statistics.P2PPlaybackStatistics;
import com.ez.statistics.P2PPreviewStatistics;
import com.ez.statistics.PrivateStreamPlaybackStatistics;
import com.ez.statistics.PrivateStreamPreviewStatistics;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.UUID;

public class RootStatistics {
    public ArrayList<DirectPreviewStatistics> mDirectPreviewStatisticsList = new ArrayList();
    public ArrayList<PrivateStreamPreviewStatistics> mPrivateStreamPreviewStatisticsList = new ArrayList();
    public ArrayList<P2PPreviewStatistics> mP2PPreviewStatisticsList = new ArrayList();
    public ArrayList<DirectPlaybackStatistics> mDirectPlaybackStatisticsList = new ArrayList();
    public ArrayList<P2PPlaybackStatistics> mP2PPlaybackStatisticsList = new ArrayList();
    public ArrayList<CloudPlaybackStatistics> mCloudPlaybackStatisticsList = new ArrayList();
    public ArrayList<PrivateStreamPlaybackStatistics> mPrivateStreamPlaybackStatisticsList = new ArrayList();
    public ArrayList<NetSDKPreviewStatistics> mNetSDKPreviewStatisticsList = new ArrayList();
    public ArrayList<NetSDKPlaybackStatistics> mNetSDKPlaybackStatisticsList = new ArrayList();
    public int mFirstVIA_OK = -1;
    public int mLastVIA = -1;
    public long mFirstStreamTime = -1L;
    public long mDisplayTime = -1L;
    public long start_st = -1L;
    public int r = -2;
    public String uuid = UUID.randomUUID().toString();
    public int mSeq = 0;

    public ArrayList<String> getAllSubStatisticeJson() {
        ArrayList<String> subStatisticeJsons = new ArrayList<String>();
        BaseStatistics.flatmapStatistics(this.mDirectPreviewStatisticsList, subStatisticeJsons);
        BaseStatistics.flatmapStatistics(this.mPrivateStreamPreviewStatisticsList, subStatisticeJsons);
        BaseStatistics.flatmapStatistics(this.mP2PPreviewStatisticsList, subStatisticeJsons);
        BaseStatistics.flatmapStatistics(this.mDirectPlaybackStatisticsList, subStatisticeJsons);
        BaseStatistics.flatmapStatistics(this.mCloudPlaybackStatisticsList, subStatisticeJsons);
        BaseStatistics.flatmapStatistics(this.mPrivateStreamPlaybackStatisticsList, subStatisticeJsons);
        BaseStatistics.flatmapStatistics(this.mNetSDKPreviewStatisticsList, subStatisticeJsons);
        BaseStatistics.flatmapStatistics(this.mNetSDKPlaybackStatisticsList, subStatisticeJsons);
        BaseStatistics.flatmapStatistics(this.mP2PPlaybackStatisticsList, subStatisticeJsons);
        return subStatisticeJsons;
    }

    public String getRootStaticJson() {
        RootStreamStatistics rootStreamStatistics = new RootStreamStatistics();
        rootStreamStatistics.via = this.mFirstVIA_OK == -1 && this.r != 0 ? this.mLastVIA : this.mFirstVIA_OK;
        rootStreamStatistics.data_t = this.mFirstStreamTime;
        rootStreamStatistics.display_t = this.mDisplayTime;
        rootStreamStatistics.uuid = this.uuid;
        rootStreamStatistics.start_st = this.start_st;
        rootStreamStatistics.r = this.r;
        Gson gson = new Gson();
        return gson.toJson((Object)rootStreamStatistics);
    }

    class RootStreamStatistics {
        public int via;
        public String uuid;
        public long data_t;
        public long display_t;
        public long start_st;
        public int r;

        RootStreamStatistics() {
        }
    }
}


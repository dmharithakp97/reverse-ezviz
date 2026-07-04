/*
 * Decompiled with CFR 0.152.
 */
package com.ez.statistics;

import com.ez.statistics.StatisticsGson;
import java.util.ArrayList;

public class BaseStatistics {
    public String toJson() {
        return StatisticsGson.toJson(this);
    }

    public static void flatmapStatistics(ArrayList<? extends BaseStatistics> list, ArrayList<String> subStatisticeJsons) {
        if (list != null) {
            for (BaseStatistics baseStatistics : list) {
                if (baseStatistics == null) continue;
                subStatisticeJsons.add(baseStatistics.toJson());
            }
        }
    }
}


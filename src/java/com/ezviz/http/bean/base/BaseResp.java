/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 */
package com.ezviz.http.bean.base;

import android.text.TextUtils;
import java.util.Map;

public class BaseResp {
    public int httpCode = -1;
    public Meta meta;
    public Page page;
    public String body;
    public String code;
    public String msg;

    public static class Page {
        public int limit;
        public int offset;
        public int totalResults;
        public boolean hasNext;
    }

    public static class Meta {
        public int code;
        public String message;
        public Map<String, String> moreInfo;

        public String getMoreInfoString(String key) {
            if (this.moreInfo == null) {
                return null;
            }
            return this.moreInfo.get(key);
        }

        public int getMoreInfoInt(String key) {
            String value = this.getMoreInfoString(key);
            if (!TextUtils.isEmpty((CharSequence)value)) {
                try {
                    return Integer.parseInt(value);
                }
                catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            return 0;
        }
    }
}


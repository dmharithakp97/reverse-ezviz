/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.json.JSONArray
 *  org.json.JSONException
 *  org.json.JSONObject
 */
package com.videogo.openapi.model.resp;

import com.videogo.exception.BaseException;
import com.videogo.openapi.model.BaseResponse;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BatchGetTokensResp
extends BaseResponse {
    private static final String TOKENARRAY = "tokenArray";

    @Override
    public Object parse(String reponse) throws BaseException, JSONException {
        if (this.parseCode(reponse)) {
            JSONObject all = new JSONObject(reponse);
            JSONArray array = all.getJSONArray(TOKENARRAY);
            ArrayList<String> items = new ArrayList<String>();
            for (int i = 0; i < array.length(); ++i) {
                if (array.isNull(i)) continue;
                String token = array.optString(i);
                items.add(token);
            }
            return items;
        }
        return null;
    }
}


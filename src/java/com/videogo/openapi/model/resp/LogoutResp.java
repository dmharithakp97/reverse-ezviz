/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.json.JSONException
 */
package com.videogo.openapi.model.resp;

import com.videogo.exception.BaseException;
import com.videogo.openapi.model.ApiResponse;
import org.json.JSONException;

public class LogoutResp
extends ApiResponse {
    public static final String URL = "/api/user/logout";

    @Override
    public Object parse(String reponse) throws BaseException, JSONException {
        return this.parseCode(reponse);
    }
}


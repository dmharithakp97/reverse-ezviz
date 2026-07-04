/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.ezdclog.params;

import com.videogo.ezdclog.params.BaseParams;
import com.videogo.openapi.annotation.HttpParam;

public class EZLogStreamBaseParams
extends BaseParams {
    @HttpParam(name="err")
    public int err;
    @HttpParam(name="opId")
    public String opId;
    @HttpParam(name="cost")
    public int cost;
}


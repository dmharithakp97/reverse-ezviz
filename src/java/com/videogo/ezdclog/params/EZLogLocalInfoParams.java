/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.ezdclog.params;

import com.videogo.ezdclog.params.BaseParams;
import com.videogo.openapi.annotation.HttpParam;

public class EZLogLocalInfoParams
extends BaseParams {
    @HttpParam(name="systemName")
    public String systemName = "opensdk_mobile_local_info";
    @HttpParam(name="os")
    public String os;
    @HttpParam(name="phoneType")
    public String phoneType;
}


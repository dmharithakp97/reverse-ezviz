/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.ezdclog.params;

import com.videogo.ezdclog.params.EZLogStreamBaseParams;
import com.videogo.openapi.annotation.HttpParam;

public class EZLogStreamP2pPreParams
extends EZLogStreamBaseParams {
    @HttpParam(name="systemName")
    public String systemName = "opensdk_mobile_p2p_pre";
    @HttpParam(name="cnt")
    public int cnt;
}


/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.openapi.bean.req;

import com.videogo.openapi.bean.BaseInfo;

public class BatchGetTokens
extends BaseInfo {
    private int count;

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}


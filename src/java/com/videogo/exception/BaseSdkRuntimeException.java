/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.exception;

public class BaseSdkRuntimeException
extends RuntimeException {
    public int errCode = 0;
    public String errMsg = null;

    public BaseSdkRuntimeException() {
    }

    public BaseSdkRuntimeException(String errMsg) {
        this.errMsg = errMsg;
    }
}


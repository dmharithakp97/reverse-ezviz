/*
 * Decompiled with CFR 0.152.
 */
package com.ez.player;

public class EZMediaPlayerException
extends Exception {
    String mMsg;
    int mErrorCode;

    public EZMediaPlayerException(String msg, int errorCode) {
        super(msg);
        this.mMsg = msg;
        this.mErrorCode = errorCode;
    }
}


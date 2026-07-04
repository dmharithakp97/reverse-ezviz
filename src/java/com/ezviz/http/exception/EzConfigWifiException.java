/*
 * Decompiled with CFR 0.152.
 */
package com.ezviz.http.exception;

public class EzConfigWifiException
extends Exception {
    public String message;
    public int errorCode;

    public EzConfigWifiException(int errorCode, String message) {
        this.message = message;
        this.errorCode = errorCode;
    }

    public EzConfigWifiException(int errorCode) {
        this(errorCode, "");
    }
}


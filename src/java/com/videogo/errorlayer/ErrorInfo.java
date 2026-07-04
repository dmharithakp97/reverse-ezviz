/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.errorlayer;

public class ErrorInfo {
    public String moduleCode;
    public int errorCode;
    public String description;
    public String solution;

    public ErrorInfo() {
    }

    public ErrorInfo(int errorCode, String description) {
        this.errorCode = errorCode;
        this.description = description;
    }

    public String toString() {
        return "ErrorInfo{moduleCode='" + this.moduleCode + '\'' + ", errorCode=" + this.errorCode + ", description='" + this.description + '\'' + ", solution='" + this.solution + '\'' + '}';
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.errorlayer;

import com.videogo.errorlayer.ErrorInfo;
import com.videogo.errorlayer.ErrorLayer;

public class NPCClientErrorLayer
extends ErrorLayer {
    @Override
    public ErrorInfo getErrorInfo(int errorCode) {
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.errorCode = errorCode < 0 ? 510000 + errorCode : 500000 + errorCode;
        errorInfo.description = this.getDescription(errorInfo.errorCode);
        return errorInfo;
    }

    @Override
    public String getDescription(int errorCode) {
        String description = "";
        switch (errorCode) {
            case 500000: {
                description = "no error";
                break;
            }
            case 500001: {
                description = "\u53c2\u6570\u9519\u8bef";
                break;
            }
            case 500002: {
                description = "\u8c03\u7528\u987a\u5e8f\u51fa\u9519";
                break;
            }
            case 500003: {
                description = "\u5206\u914d\u5185\u5b58\u5931\u8d25";
                break;
            }
            case 500004: {
                description = "\u7f13\u51b2\u533a\u6ea2\u51fa";
                break;
            }
            case 500005: {
                description = "\u7cfb\u7edf\u4e0d\u652f\u6301";
                break;
            }
            case 500006: {
                description = "\u65e0\u6548\u7aef\u53e3";
                break;
            }
            case 500101: {
                description = "\u6d41\u5173\u95ed";
                break;
            }
            case 500102: {
                description = "TRACK_CLOSE";
                break;
            }
            case 500103: {
                description = "\u521b\u5efa\u5931\u8d25";
                break;
            }
            case 500104: {
                description = "TRSCREATE_ERROR";
                break;
            }
            case 509999: {
                description = "FAIL_UNKNOWN";
                break;
            }
            default: {
                description = "npcclient\u672a\u77e5\u9519\u8bef";
            }
        }
        return description;
    }
}


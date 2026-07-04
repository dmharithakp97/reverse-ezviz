/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.errorlayer;

import com.videogo.errorlayer.ErrorInfo;
import com.videogo.errorlayer.ErrorLayer;

public class WebErrorLayer
extends ErrorLayer {
    @Override
    public ErrorInfo getErrorInfo(int errorCode) {
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.errorCode = errorCode < 0 ? 200000 + errorCode : 100000 + errorCode;
        errorInfo.description = this.getDescription(errorInfo.errorCode);
        return errorInfo;
    }

    @Override
    public String getDescription(int errorCode) {
        String description;
        switch (errorCode) {
            case 101001: {
                description = "\u7528\u6237\u540d\u4e0d\u5408\u6cd5";
                break;
            }
            case 101002: {
                description = "\u7528\u6237\u540d\u5df2\u5b58\u5728";
                break;
            }
            case 101006: {
                description = "\u8be5\u624b\u673a\u53f7\u5df2\u88ab\u6ce8\u518c";
                break;
            }
            case 101013: {
                description = "\u7528\u6237\u540d\u4e0d\u5b58\u5728";
                break;
            }
            case 101003: {
                description = "\u5bc6\u7801\u4e0d\u5408\u6cd5";
                break;
            }
            case 101004: {
                description = "\u5bc6\u7801\u4e0d\u80fd\u4e3a\u540c\u4e00\u5b57\u7b26\uff0c\u5982aaaaaaaa\u662f\u4e0d\u5141\u8bb8\u7684";
                break;
            }
            case 199999: {
                description = "\u670d\u52a1\u5668\u53ef\u80fd\u5728\u5f00\u5c0f\u5dee";
                break;
            }
            case 101010: {
                description = "\u83b7\u53d6\u9a8c\u8bc1\u7801\u5931\u8d25-------------------------------------------";
                break;
            }
            case 101009: {
                description = "\u7528\u6237\u540d\u548c\u624b\u673a\u4e0d\u5339\u914d";
                break;
            }
            case 101019: {
                description = "\u7528\u6237\u540d\u88ab\u591a\u6b21\u6ce8\u518c--------------------------------------------";
                break;
            }
            case 110001: {
                description = "";
                break;
            }
            case 110002: {
                description = "";
                break;
            }
            case 110003: {
                description = "";
                break;
            }
            case 110004: {
                description = "";
                break;
            }
            case 110005: {
                description = "";
                break;
            }
            case 110006: {
                description = "";
                break;
            }
            case 110007: {
                description = "";
                break;
            }
            case 110008: {
                description = "";
                break;
            }
            case 110009: {
                description = "";
                break;
            }
            case 110010: {
                description = "";
                break;
            }
            case 110011: {
                description = "";
                break;
            }
            case 101011: {
                description = "";
                break;
            }
            case 110012: {
                description = "";
                break;
            }
            case 110018: {
                description = "AppKey\u548cAccessToke\u4e0d\u5339\u914d\uff0c\u8bf7\u786e\u8ba4\u83b7\u53d6token\u7684\u63a5\u53e3\u7528\u7684appkey\uff0c\u548csdk\u521d\u59cb\u5316\u8bbe\u7f6e\u7684appkey\u662f\u5426\u4e00\u81f4\uff08\u90e8\u5206\u5f00\u53d1\u8005\u53c2\u7167sdk demo\uff0c\u53ef\u80fdappkey\u6ca1\u6709\u6539\u6210\u81ea\u5df1\u7684\uff09";
                break;
            }
            case 110029: {
                description = "\u63a5\u53e3\u8c03\u7528\u592a\u9891\u7e41";
                break;
            }
            case 120031: {
                description = "\u8d26\u53f7\u5f00\u542f\u4e86\u7ec8\u7aef\u7ed1\u5b9a\uff0c\u8bf7\u5230i.ys7.com\u4e2d\u89e3\u9664\u7ec8\u7aef\u7ed1\u5b9a";
                break;
            }
            case 101041: {
                description = "";
                break;
            }
            case 120003: {
                description = "";
                break;
            }
            case 120004: {
                description = "";
                break;
            }
            case 120005: {
                description = "";
                break;
            }
            case 120006: {
                description = "";
                break;
            }
            case 120001: {
                description = "";
                break;
            }
            case 120002: {
                description = "";
                break;
            }
            case 120007: {
                description = "";
                break;
            }
            case 120008: {
                description = "";
                break;
            }
            case 120018: {
                description = "";
                break;
            }
            case 120010: {
                description = "\u8bbe\u5907\u9a8c\u8bc1\u7801\u9519\u8bef\uff0c\u9a8c\u8bc1\u7801\u4f4d\u4e8e\u8bbe\u5907\u673a\u8eab\u4e0a\u76846\u4f4d\u5927\u5199\u5b57\u7b26";
                break;
            }
            case 120019: {
                description = "";
                break;
            }
            case 120020: {
                description = "";
                break;
            }
            case 120021: {
                description = "";
                break;
            }
            case 120022: {
                description = "";
                break;
            }
            case 120023: {
                description = "";
                break;
            }
            case 120024: {
                description = "";
                break;
            }
            case 120029: {
                description = "";
                break;
            }
            case 149999: {
                description = "";
                break;
            }
            case 150000: {
                description = "";
                break;
            }
            case 102000: {
                description = "";
                break;
            }
            case 102001: {
                description = "";
                break;
            }
            case 102002: {
                description = "";
                break;
            }
            case 102003: {
                description = "";
                break;
            }
            default: {
                description = "unknow error.";
            }
        }
        return description;
    }
}


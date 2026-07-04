/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.errorlayer;

import com.videogo.errorlayer.ErrorInfo;
import com.videogo.errorlayer.ErrorLayer;

public class StreamNewTtsErrorLayer
extends ErrorLayer {
    @Override
    public ErrorInfo getErrorInfo(int errorCode) {
        ErrorInfo errorInfo = new ErrorInfo();
        if (errorCode > 10000) {
            errorInfo.errorCode = 361000 + errorCode;
        }
        errorInfo.description = this.getDescription(errorInfo.errorCode);
        return errorInfo;
    }

    @Override
    public String getDescription(int errorCode) {
        String description = "";
        switch (errorCode) {
            case 360000: {
                description = "no error";
                break;
            }
            case 360001: {
                description = "\u5ba2\u6237\u7aef\u8bf7\u6c42\u8d85\u65f6";
                break;
            }
            case 360002: {
                description = "\u670d\u52a1\u5668\u5904\u7406\u8bf7\u6c42\u8d85\u65f6";
                break;
            }
            case 360003: {
                description = "TTS\u7684\u8bbe\u5907\u7aef\u53d1\u751f\u9519\u8bef";
                break;
            }
            case 360005: {
                description = "\u5ba2\u6237\u7aef\u53d1\u9001\u7684\u6d88\u606f\u9519\u8bef";
                break;
            }
            case 360006: {
                description = "\u5ba2\u6237\u7aef\u63a5\u6536\u53d1\u751f\u9519\u8bef";
                break;
            }
            case 360007: {
                description = "TTS\u5173\u95ed\u4e86\u4e0e\u5ba2\u6237\u7aef\u7684\u8fde\u63a5\uff0c\u6b64\u9519\u8bef\u4e00\u822c\u662f\u7531\u4e8e\u5173\u95ed\u5bf9\u8bb2\u548c\u6253\u5f00\u5bf9\u8bb2\u65f6\u95f4\u95f4\u9694\u8fc7\u4f4e\u5bfc\u81f4";
                break;
            }
            case 360013: {
                description = "\u8bbe\u5907\u5f00\u542f\u4e86\u9690\u79c1\u4fdd\u62a4";
                break;
            }
            case 360101: {
                description = "\u521b\u5efa\u5931\u8d25";
                break;
            }
            case 360102: {
                description = "\u521d\u59cb\u5316\u5931\u8d25";
                break;
            }
            case 361001: {
                description = "\u8bf7\u6c42\u7b49\u5f85\u8d85\u65f6(\u5ba2\u6237\u7aef)";
                break;
            }
            case 361002: {
                description = "\u8fde\u63a5TTS\u5931\u8d25(\u5ba2\u6237\u7aef)";
                break;
            }
            case 361003: {
                description = "TTS\u4e0e\u8bbe\u5907\u7684\u8fde\u63a5\u5f02\u5e38\u4e2d\u65ad(\u670d\u52a1\u5668\u7aef)";
                break;
            }
            case 361004: {
                description = "TTS\u5185\u90e8\u5904\u7406\u5f02\u5e38(\u670d\u52a1\u5668\u7aef)";
                break;
            }
            case 361005: {
                description = "TTS\u63a5\u53d7\u5230\u7684\u6d88\u606f\u7c7b\u578b\u4e0d\u6b63\u786e(\u5ba2\u6237\u7aef)";
                break;
            }
            case 361006: {
                description = "\u5ba2\u6237\u7aef\u9700\u8981\u91cd\u5b9a\u5411(\u670d\u52a1\u5668\u8fd4\u56de)";
                break;
            }
            case 361007: {
                description = "\u5ba2\u6237\u7aef\u7684URL\u683c\u5f0f\u9519\u8bef(\u670d\u52a1\u5668\u8fd4\u56de)";
                break;
            }
            case 361008: {
                description = "TTS\u9a8c\u8bc1token\u5931\u8d25(\u670d\u52a1\u5668\u8fd4\u56de)";
                break;
            }
            case 361009: {
                description = "\u64cd\u4f5c\u7801\u6216\u4fe1\u4ee4\u5bc6\u94a5\u4e0e\u8bbe\u5907\u4e0d\u5339\u914d";
                break;
            }
            case 361010: {
                description = "\u8bbe\u5907\u6b63\u5728\u5bf9\u8bb2\u4e2d(\u9891\u7e41\u8fc5\u901f\u5f00\u5173\u5bf9\u8bb2\u6bd4\u8f83\u5bb9\u6613\u62a5\u6b64\u9519\u8bef\uff0c\u8bbe\u5907\u7aef\u5173\u95ed\u5bf9\u8bb2\u8d44\u6e90\u9700\u8981\u4e00\u5c0f\u6bb5\u65f6\u95f4)";
                break;
            }
            case 361011: {
                description = "TTS\u53d1\u9001\u6216\u63a5\u53d7\u8bbe\u5907\u4fe1\u4ee4\u8d85\u65f6(\u670d\u52a1\u5668\u8fd4\u56de)";
                break;
            }
            case 361012: {
                description = "\u8bbe\u5907\u4e0d\u5728\u7ebf(\u670d\u52a1\u5668\u8fd4\u56de)";
                break;
            }
            case 361013: {
                description = "\u8bbe\u5907\u5904\u4e8e\u9690\u79c1\u4fdd\u62a4\u72b6\u6001(\u670d\u52a1\u5668\u8fd4\u56de)";
                break;
            }
            case 361014: {
                description = "\u5ba2\u6237\u7aef\u521b\u5efa\u7ebf\u7a0b\u5931\u8d25(\u5ba2\u6237\u7aef)";
                break;
            }
            case 361015: {
                description = "\u5ba2\u6237\u7aef\u5904\u7406URL\u5931\u8d25(\u5ba2\u6237\u7aef)";
                break;
            }
            case 361016: {
                description = "\u5ba2\u6237\u7aef\u83b7\u53d6\u7684\u91cd\u5b9a\u5411URL\u9519\u8bef(\u5ba2\u6237\u7aef)";
                break;
            }
            case 361017: {
                description = "token\u65e0\u6743\u9650(\u670d\u52a1\u5668\u8fd4\u56de)";
                break;
            }
            case 361018: {
                description = "session\u4e0d\u5b58\u5728(\u670d\u52a1\u5668\u8fd4\u56de)";
                break;
            }
            case 361019: {
                description = "token\u8ba4\u8bc1\u7684\u5176\u4ed6\u9519\u8bef(\u670d\u52a1\u5668\u8fd4\u56de)";
                break;
            }
            case 361100: {
                description = "\u5ba2\u6237\u7aef\u51fd\u6570\u4f20\u53c2\u9519\u8bef(\u5ba2\u6237\u7aef)";
                break;
            }
            case 361101: {
                description = "\u5ba2\u6237\u7aef\u521b\u5efasocket\u51fa\u9519(\u5ba2\u6237\u7aef)";
                break;
            }
            case 361102: {
                description = "\u5ba2\u6237\u7aef\u63a5\u6536\u4fe1\u4ee4socket\u5f02\u5e38(\u5ba2\u6237\u7aef)";
                break;
            }
            case 361103: {
                description = "\u5ba2\u6237\u7aef\u53d1\u9001\u4fe1\u4ee4socket\u5f02\u5e38(\u5ba2\u6237\u7aef)";
                break;
            }
            case 361104: {
                description = "\u5ba2\u6237\u7aef\u83b7\u77e5TTS\u5173\u95ed\u4e86\u94fe\u63a5(\u5ba2\u6237\u7aef)";
                break;
            }
            case 361105: {
                description = "TTS\u8fd4\u56de\u7684\u4fe1\u4ee4\u7c7b\u578b\u9519\u8bef(\u5ba2\u6237\u7aef)";
                break;
            }
            case 361106: {
                description = "TTS\u54cd\u5e94\u4e2d\u7684result\u4e3a\u8d1f\u503c(\u5ba2\u6237\u7aef)";
                break;
            }
            default: {
                description = "TTS\u5e93\u672a\u77e5\u9519\u8bef";
            }
        }
        return description;
    }
}


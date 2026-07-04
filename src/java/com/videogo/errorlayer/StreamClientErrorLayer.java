/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.errorlayer;

import com.videogo.errorlayer.ErrorInfo;
import com.videogo.errorlayer.ErrorLayer;

public class StreamClientErrorLayer
extends ErrorLayer {
    @Override
    public ErrorInfo getErrorInfo(int errorCode) {
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.errorCode = errorCode < 0 ? 400000 + errorCode : 390000 + errorCode;
        errorInfo.description = this.getDescription(errorInfo.errorCode);
        return errorInfo;
    }

    @Override
    public String getDescription(int errorCode) {
        String description = "";
        switch (errorCode) {
            case 390000: {
                description = "no error";
                break;
            }
            case 399997: {
                description = "\u79c1\u6709\u6d41\u5a92\u4f53\u53d6\u6d41\u8d85\u65f6";
                break;
            }
            case 390001: {
                description = "\u79c1\u6709\u6d41\u5a92\u4f53\u53d6\u6d41\u901a\u7528\u9519\u8bef";
                break;
            }
            case 390002: {
                description = "\u53c2\u6570\u4e3a\u7a7a\u6307\u9488";
                break;
            }
            case 390003: {
                description = "\u53c2\u6570\u65e0\u6548";
                break;
            }
            case 390004: {
                description = "\u4fe1\u4ee4\u6d88\u606f\u89e3\u6790\u975e\u6cd5";
                break;
            }
            case 390005: {
                description = "\u5185\u5b58\u8d44\u6e90\u4e0d\u8db3";
                break;
            }
            case 390006: {
                description = "\u534f\u8bae\u683c\u5f0f\u4e0d\u5bf9\u6216\u8005\u6d88\u606f\u4f53\u957f\u5ea6\u8d85\u8fc7\u4e0a\u9650";
                break;
            }
            case 390007: {
                description = "\u8bbe\u5907\u5e8f\u5217\u53f7\u957f\u5ea6\u4e0d\u5408\u6cd5";
                break;
            }
            case 390008: {
                description = "url\u957f\u5ea6\u4e0d\u5408\u6cd5";
                break;
            }
            case 390009: {
                description = "\u89e3\u6790vtm\u8fd4\u56devtdu\u5730\u5740\u4e0d\u5408\u6cd5";
                break;
            }
            case 3900010: {
                description = "\u89e3\u6790vtm\u8fd4\u56de\u7ea7\u8054vtdu\u5730\u5740\u4e0d\u5408\u6cd5";
                break;
            }
            case 390011: {
                description = "\u89e3\u6790vtm\u8fd4\u56de\u4f1a\u8bdd\u6807\u8bc6\u957f\u5ea6\u4e0d\u5408\u6cd5";
                break;
            }
            case 390012: {
                description = "vtdu\u8fd4\u56de\u6d41\u5934\u957f\u5ea6\u4e0d\u5408\u6cd5";
                break;
            }
            case 390013: {
                description = "vtdu\u4f1a\u8bdd\u957f\u5ea6\u975e\u6cd5";
                break;
            }
            case 390014: {
                description = "\u56de\u8c03\u51fd\u6570\u672a\u6ce8\u518c";
                break;
            }
            case 390015: {
                description = "vtdu\u6210\u529f\u54cd\u5e94\u672a\u643a\u5e26\u4f1a\u8bdd\u6807\u8bc6";
                break;
            }
            case 390016: {
                description = "vtdu\u6210\u529f\u54cd\u5e94\u672a\u643a\u5e26\u6d41\u5934";
                break;
            }
            case 390017: {
                description = "\u65e0\u6570\u636e\u6d41\uff0c\u5c1a\u672a\u4f7f\u7528";
                break;
            }
            case 390018: {
                description = "\u4fe1\u4ee4\u6d88\u606f\u4f53PB\u89e3\u6790\u5931\u8d25";
                break;
            }
            case 390019: {
                description = "\u4fe1\u4ee4\u6d88\u606f\u4f53PB\u5c01\u88c5\u5931\u8d25";
                break;
            }
            case 390020: {
                description = "\u7533\u8bf7\u7cfb\u7edf\u5185\u5b58\u8d44\u6e90\u5931\u8d25";
                break;
            }
            case 390021: {
                description = "vtdu\u5730\u5740\u5c1a\u672a\u83b7\u53d6\u5230";
                break;
            }
            case 390022: {
                description = "\u5ba2\u6237\u7aef\u5c1a\u672a\u652f\u6301";
                break;
            }
            case 390023: {
                description = "\u83b7\u53d6\u7cfb\u7edfsocket\u8d44\u6e90\u5931\u8d25";
                break;
            }
            case 390024: {
                description = "\u4e0a\u5c42\u586b\u5145\u7684StreamSsnId\u4e0d\u5339\u914d";
                break;
            }
            case 390025: {
                description = "\u8fde\u63a5\u670d\u52a1\u5668\u5931\u8d25";
                break;
            }
            case 390026: {
                description = "\u5ba2\u6237\u7aef\u8bf7\u6c42\u672a\u6536\u5230\u670d\u52a1\u7aef\u5e94\u7b54";
                break;
            }
            case 390027: {
                description = "\u94fe\u8def\u65ad\u5f00";
                break;
            }
            case 390028: {
                description = "\u6ca1\u6709\u53d6\u6d41\u94fe\u63a5";
                break;
            }
            case 390029: {
                description = "\u6d41\u6210\u529f\u505c\u6b62";
                break;
            }
            case 3900230: {
                description = "\u5ba2\u6237\u7aef\u9632\u4e32\u6d41\u6821\u9a8c\u5931\u8d25";
                break;
            }
            case 390031: {
                description = "\u5e94\u7528\u5c42tcp\u7c98\u5305\u5904\u7406\u7f13\u51b2\u533a\u6ee1";
                break;
            }
            case 390032: {
                description = "\u65e0\u6548\u72b6\u6001\u8fc1\u79fb";
                break;
            }
            case 390033: {
                description = "\u65e0\u6548\u5ba2\u6237\u7aef\u72b6\u6001";
                break;
            }
            case 390034: {
                description = "\u5411vtm\u53d6\u6d41\u6d41\u5a92\u4f53\u4fe1\u606f\u8bf7\u6c42\u8d85\u65f6";
                break;
            }
            case 390035: {
                description = "\u5411\u4ee3\u7406\u53d6\u6d41\u8bf7\u6c42\u8d85\u65f6";
                break;
            }
            case 390036: {
                description = "\u5411\u4ee3\u7406\u4fdd\u6d3b\u53d6\u6d41\u8bf7\u6c42\u8d85\u65f6";
                break;
            }
            case 390037: {
                description = "\u5411vtdu\u53d6\u6d41\u8bf7\u6c42\u8d85\u65f6";
                break;
            }
            case 390038: {
                description = "\u5411vtdu\u4fdd\u6d3b\u53d6\u6d41\u8bf7\u6c42\u8d85\u65f6";
                break;
            }
            case 395000: {
                description = "\u670d\u52a1\u7aef\u8d77\u59cb\u54cd\u5e94\u7801";
                break;
            }
            case 395402: {
                description = "\u56de\u653e\u627e\u4e0d\u5230\u5f55\u50cf\u6587\u4ef6";
                break;
            }
            case 395403: {
                description = "\u64cd\u4f5c\u7801\u6216\u4fe1\u4ee4\u5bc6\u94a5\u4e0e\u8bbe\u5907\u4e0d\u5339\u914d\uff0c\u9700\u8981\u91cd\u65b0\u83b7\u53d6\u8bbe\u5907\u64cd\u4f5c\u7801";
                break;
            }
            case 395404: {
                description = "\u8bbe\u5907\u4e0d\u5728\u7ebf";
                break;
            }
            case 395405: {
                description = "\u6d41\u5a92\u4f53\u5411\u8bbe\u5907\u53d1\u9001\u6216\u63a5\u53d7\u4fe1\u4ee4\u8d85\u65f6";
                break;
            }
            case 395406: {
                description = "token\u5931\u6548\uff0c\u91cd\u65b0\u542f\u52a8\u5ba2\u6237\u7aef\u53ef\u89e3\u51b3\u6b64\u95ee\u9898";
                break;
            }
            case 395407: {
                description = "\u5ba2\u6237\u7aef\u7684URL\u683c\u5f0f\u9519\u8bef";
                break;
            }
            case 395409: {
                description = "\u9884\u89c8\u5f00\u542f\u9690\u79c1\u4fdd\u62a4";
                break;
            }
            case 395410: {
                description = "\u8bbe\u5907\u8fbe\u5230\u6700\u5927\u8fde\u63a5\u6570";
                break;
            }
            case 395411: {
                description = "token\u65e0\u6743\u9650";
                break;
            }
            case 395412: {
                description = "session\u4e0d\u5b58\u5728";
                break;
            }
            case 395413: {
                description = "\u9a8c\u8bc1token\u7684\u503c\u5f02\u5e38";
                break;
            }
            case 395415: {
                description = "\u901a\u9053\u53f7\u9519\u8bef";
                break;
            }
            case 395451: {
                description = "\u8bbe\u5907\u4e0d\u652f\u6301\u7684\u7801\u6d41\u7c7b\u578b";
                break;
            }
            case 395452: {
                description = "\u8bbe\u5907\u8fde\u63a5\u9884\u89c8\u6d41\u5a92\u4f53\u670d\u52a1\u5668\u5931\u8d25";
                break;
            }
            case 395454: {
                description = "\u8bbe\u5907\u4e0e\u670d\u52a1\u5668\u4e4b\u95f4\u7684\u94fe\u8def\u65ad\u5f00";
                break;
            }
            case 395491: {
                description = "\u76f8\u540c\u8bf7\u6c42\u6b63\u5728\u5904\u7406\uff0c\u62d2\u7edd\u672c\u6b21\u5904\u7406";
                break;
            }
            case 395500: {
                description = "\u6d41\u5a92\u4f53\u670d\u52a1\u5668\u5185\u90e8\u5904\u7406\u9519\u8bef";
                break;
            }
            case 395503: {
                description = "vtm\u5206\u914dvtdu\u670d\u52a1\u5668\u5931\u8d25";
                break;
            }
            case 395544: {
                description = "\u8bbe\u5907\u8fd4\u56de\u65e0\u89c6\u9891\u6e90";
                break;
            }
            case 395545: {
                description = "\u8bbe\u5907\u5206\u4eab\u65f6\u95f4\u5df2\u7ecf\u7ed3\u675f";
                break;
            }
            case 399999: {
                description = "\u79c1\u6709\u6d41\u5a92\u4f53\u5e93\u6ca1\u6709\u521d\u59cb\u5316";
                break;
            }
            case 399998: {
                description = "create\u5931\u8d25";
                break;
            }
            case 395546: {
                description = "\u53d6\u6d41\u5e76\u53d1\u8def\u6570\u9650\u5236";
                break;
            }
            case 395416: {
                description = "\u8bbe\u5907\u8fbe\u5230\u6700\u5927\u8fde\u63a5\u6570";
                break;
            }
            default: {
                description = "streamclientsdk\u672a\u77e5\u9519\u8bef";
            }
        }
        return description;
    }
}


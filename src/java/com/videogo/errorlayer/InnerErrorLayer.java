/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.errorlayer;

import com.videogo.errorlayer.ErrorInfo;
import com.videogo.errorlayer.ErrorLayer;

public class InnerErrorLayer
extends ErrorLayer {
    @Override
    public ErrorInfo getErrorInfo(int errorCode) {
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.errorCode = errorCode;
        errorInfo.description = this.getDescription(errorInfo.errorCode);
        return errorInfo;
    }

    @Override
    public String getDescription(int errorCode) {
        String description = "";
        switch (errorCode) {
            case 400000: {
                description = "no error";
                break;
            }
            case 400001: {
                description = "\u53c2\u6570\u4e3a\u7a7a\uff0c\u53ef\u4ee5\u67e5\u770blogcat\u65e5\u5fd7\u5b9a\u4f4d\u5230\u5177\u4f53\u53c2\u6570";
                break;
            }
            case 400025: {
                description = "\u8bbe\u5907\u4e0d\u652f\u6301\u5bf9\u8bb2";
                break;
            }
            case 400028: {
                description = "\u7528\u6237\u4e3b\u52a8\u505c\u6b62";
                break;
            }
            case 400029: {
                description = "\u6ca1\u6709\u521d\u59cb\u5316\u6216\u8d44\u6e90\u88ab\u91ca\u653e";
                break;
            }
            case 400030: {
                description = "JSON\u89e3\u6790\u5f02\u5e38\uff0c\u53ef\u4ee5\u67e5\u770b\u65e5\u5fd7\u4e2d\u7684\u5f02\u5e38\u6253\u5370\u4fe1\u606f\u5b9a\u4f4d\u95ee\u9898";
                break;
            }
            case 400031: {
                description = "\u7f51\u7edc\u5f02\u5e38\uff0c\u8bf7\u6c42\u51fa\u73b0\u9519\u8bef";
                break;
            }
            case 400032: {
                description = "\u8bbe\u5907\u4fe1\u606f\u5f02\u5e38\u4e3a\u7a7a\uff0c\u5237\u65b0\u4e0b\u8bbe\u5907\u4fe1\u606f\u6216\u91cd\u65b0\u6253\u5f00app\u8bd5\u8bd5";
                break;
            }
            case 400100: {
                description = "\u672a\u77e5\u9519\u8bef\uff0c\u771f\u7684\u6536\u5230\u4e86\u8fd9\u79cd\u9519\u8bef\uff0c\u5e26\u4e0a\u65e5\u5fd7\u627e\u6211\u4eec\u7a0b\u5e8f\u5458GG\u5427";
                break;
            }
            case 400200: {
                description = "PlayerSDK\u51fa\u9519\uff0c\u771f\u7684\u6536\u5230\u4e86\u8fd9\u79cd\u95ee\u9898\uff0c\u5e26\u4e0a\u65e5\u5fd7\u627e\u6211\u4eec\u7a0b\u5e8f\u5458GG\u5427";
                break;
            }
            case 400002: {
                description = "\u53c2\u6570\u9519\u8bef";
                break;
            }
            case 400300: {
                description = "\u5185\u5b58\u6ea2\u51fa";
                break;
            }
            case 400035: {
                description = "\u89c6\u9891\u5df2\u52a0\u5bc6\uff0c\u9700\u8981\u8f93\u5165\u5bc6\u7801\uff0c\u521d\u59cb\u5bc6\u7801\u4e3a\u8bbe\u5907\u6807\u7b7e\u4e0a\u76846\u4f4d\u9a8c\u8bc1\u7801\u3002\u5f00\u53d1\u8005\u53ef\u4ee5\u8003\u8651\u5728\u83b7\u53d6\u5230\u9a8c\u8bc1\u7801\u540e\u91cd\u65b0\u64ad\u653e\u3002";
                break;
            }
            case 400036: {
                description = "\u89c6\u9891\u5bc6\u7801\u9519\u8bef\uff0c\u521d\u59cb\u5bc6\u7801\u4e3a\u8bbe\u5907\u6807\u7b7e\u4e0a\u76846\u4f4d\u9a8c\u8bc1\u7801\u3002\u5f00\u53d1\u8005\u53ef\u4ee5\u8003\u8651\u5728\u83b7\u53d6\u5230\u9a8c\u8bc1\u7801\u540e\u91cd\u65b0\u64ad\u653e\u3002";
                break;
            }
            case 400037: {
                description = "surfacehold\u9519\u8bef\uff0c\u8bf7\u68c0\u67e5\u662f\u5426\u662f\u64ad\u653e\u4e4b\u524d\u9500\u6bc1\u4e86surface\uff0c\u6536\u5230\u6b64\u9519\u8bef\u4e5f\u53ef\u4ee5\u91cd\u65b0\u5efa\u7acbsurface\u540e\u64ad\u653e";
                break;
            }
            case 400901: {
                description = "\u8bbe\u5907\u4e0d\u5728\u7ebf";
                break;
            }
            case 400902: {
                description = "AccessToken\u5f02\u5e38\uff0c\u53ef\u80fd\u662f\u8fc7\u671f\u6216\u6821\u9a8c\u5931\u8d25\uff0c\u8bf7\u5728\u91cd\u65b0\u83b7\u53d6accessToken\u540e\u91cd\u8bd5\u4e4b\u524d\u64cd\u4f5c";
                break;
            }
            case 400903: {
                description = "\u5f53\u524d\u8d26\u53f7\u5f00\u542f\u4e86\u7ec8\u7aef\u7ed1\u5b9a\uff0c\u53ea\u5141\u8bb8\u6307\u5b9a\u8bbe\u5907\u767b\u5f55\u64cd\u4f5c\uff0c\u8bf7\u767b\u5f55i.ys7.com\u4e0a\u89e3\u9664\u7ec8\u7aef\u7ed1\u5b9a";
                break;
            }
            case 400904: {
                description = "\u8bbe\u5907\u6b63\u5728\u5bf9\u8bb2\u4e2d\u3002\u505c\u6b62\u5bf9\u8bb2\u540e\u8bbe\u5907\u9700\u8981\u77ed\u6682\u7684\u65f6\u95f4\u91ca\u653e\u8d44\u6e90\uff0c\u505c\u6b62\u5bf9\u8bb2\u540e\u7acb\u523b\u91cd\u65b0\u5f00\u542f\u5bf9\u8bb2\u4e5f\u6709\u53ef\u80fd\u62a5\u6b64\u9519\u8bef";
                break;
            }
            case 400905: {
                description = "\u8bbe\u5907\u5f00\u542f\u4e86\u9690\u79c1\u4fdd\u62a4\uff0c\u4e0d\u5141\u8bb8\u9884\u89c8\u3001\u5bf9\u8bb2\u7b49";
            }
            case 410001: {
                description = "\u7528\u6237\u540d\u4e3a\u7a7a";
                break;
            }
            case 410002: {
                description = "\u7528\u6237\u540d\u957f\u5ea6\u5c0f\u4e8e\u6700\u5c0f\u9650\u5236";
                break;
            }
            case 410003: {
                description = "\u7528\u6237\u540d\u5168\u4e3a\u4e0b\u5212\u7ebf";
                break;
            }
            case 410004: {
                description = "\u7528\u6237\u540d\u957f\u5ea6\u8d85\u51fa\u6700\u957f\u9650\u5236";
                break;
            }
            case 410005: {
                description = "\u7528\u6237\u540d\u4e0d\u5408\u6cd5";
                break;
            }
            case 410006: {
                description = "\u7528\u6237\u540d\u5168\u4e3a\u6570\u5b57";
                break;
            }
            case 410007: {
                description = "\u5bc6\u7801\u4e3a\u7a7a";
                break;
            }
            case 410008: {
                description = "\u5bc6\u7801\u957f\u5ea6\u5c0f\u4e8e\u6700\u5c0f\u9650\u5236";
                break;
            }
            case 410009: {
                description = "\u5bc6\u7801\u957f\u5ea6\u8d85\u51fa\u6700\u957f\u9650\u5236";
                break;
            }
            case 410010: {
                description = "\u5bc6\u7801\u4e3a\u76f8\u540c\u5b57\u7b26";
                break;
            }
            case 410011: {
                description = "\u5bc6\u7801\u4e0d\u5408\u6cd5";
                break;
            }
            case 410012: {
                description = "\u65b0\u5bc6\u7801\u4e3a\u7a7a";
                break;
            }
            case 410013: {
                description = "\u65b0\u5bc6\u7801\u957f\u5ea6\u5c0f\u4e8e\u6700\u5c0f\u9650\u5236";
                break;
            }
            case 410014: {
                description = "\u65b0\u5bc6\u7801\u957f\u5ea6\u8d85\u51fa\u6700\u957f\u9650\u5236";
                break;
            }
            case 410015: {
                description = "\u65b0\u5bc6\u7801\u4e0d\u80fd\u4e3a\u76f8\u540c\u5b57\u7b26";
                break;
            }
            case 410016: {
                description = "\u65b0\u5bc6\u7801\u4e0d\u5408\u6cd5";
                break;
            }
            case 410017: {
                description = "\u786e\u8ba4\u5bc6\u7801\u4e3a\u7a7a";
                break;
            }
            case 410018: {
                description = "\u4e24\u6b21\u5bc6\u7801\u4e0d\u4e00\u81f4";
                break;
            }
            case 410019: {
                description = "\u7535\u8bdd\u53f7\u7801\u4e3a\u7a7a";
                break;
            }
            case 410020: {
                description = "\u7535\u8bdd\u53f7\u7801\u4e0d\u5408\u6cd5";
                break;
            }
            case 410021: {
                description = "\u624b\u673a\u53f7\u7801\u4e3a\u7a7a";
                break;
            }
            case 410022: {
                description = "\u624b\u673a\u53f7\u7801\u4e0d\u5408\u6cd5";
                break;
            }
            case 410023: {
                description = "\u9a8c\u8bc1\u7801\u4e3a\u7a7a";
                break;
            }
            case 410024: {
                description = "\u76d1\u63a7\u70b9\u540d\u79f0\u4e3a\u7a7a";
                break;
            }
            case 410025: {
                description = "\u76d1\u63a7\u70b9\u540d\u5b57\u4e0d\u5408\u6cd5";
                break;
            }
            case 410026: {
                description = "\u5e8f\u5217\u53f7\u4e3a\u7a7a";
                break;
            }
            case 410027: {
                description = "\u76d1\u63a7\u70b9\u4e0d\u5b58\u5728";
                break;
            }
            case 410028: {
                description = "\u8bbe\u5907\u5e8f\u5217\u53f7\u4e3a\u7a7a";
                break;
            }
            case 410029: {
                description = "\u76d1\u63a7\u70b9\u5bc6\u7801\u4e3a\u7a7a";
                break;
            }
            case 410030: {
                description = "\u5e8f\u5217\u53f7\u4e0d\u5408\u6cd5";
                break;
            }
            case 410031: {
                description = "\u90ae\u7bb1\u4e0d\u5408\u6cd5";
                break;
            }
            case 410032: {
                description = "\u90ae\u7bb1\u4e3a\u7a7a";
                break;
            }
            case 410033: {
                description = "\u8bbe\u5907\u4fe1\u606f\u4e3a\u7a7a";
                break;
            }
            case 410034: {
                description = "\u5ba1\u6838\u6ca1\u901a\u8fc7\u6216\u4e0d\u5728\u5206\u4eab\u65f6\u95f4\u5185";
                break;
            }
            default: {
                description = "Inner error";
            }
        }
        return description;
    }
}


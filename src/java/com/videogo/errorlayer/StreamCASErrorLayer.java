/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.errorlayer;

import com.videogo.errorlayer.ErrorInfo;
import com.videogo.errorlayer.ErrorLayer;

public class StreamCASErrorLayer
extends ErrorLayer {
    @Override
    public ErrorInfo getErrorInfo(int errorCode) {
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.errorCode = errorCode < 0 ? 390000 + errorCode : 380000 + errorCode;
        errorInfo.description = this.getDescription(errorInfo.errorCode);
        return errorInfo;
    }

    @Override
    public String getDescription(int errorCode) {
        String description = "";
        switch (errorCode) {
            case 380000: {
                description = "no error";
                break;
            }
            case 380001: {
                description = "\u672a\u77e5\u9519\u8bef";
                break;
            }
            case 380002: {
                description = "\u62a5\u6587\u53c2\u6570\u9519\u8bef";
                break;
            }
            case 380003: {
                description = "\u62a5\u6587\u89e3\u6790\u9519\u8bef\uff0c\u4e00\u822c\u90fd\u662f\u8bbe\u5907\u91cd\u65b0\u4e0a\u4e0b\u7ebf\u540e\uff0c\u8bbe\u5907\u64cd\u4f5c\u7801\u53d1\u751f\u4e86\u53d8\u5316\u5bfc\u81f4\u7684";
                break;
            }
            case 380006: {
                description = "\u975e\u6cd5\u547d\u4ee4\uff0c\u82e5sdk\u4e0d\u662f\u6700\u65b0\uff0c\u8bf7\u66f4\u65b0sdk";
                break;
            }
            case 380007: {
                description = "\u8fc7\u65f6\u547d\u4ee4\uff0c\u82e5sdk\u4e0d\u662f\u6700\u65b0\uff0c\u8bf7\u66f4\u65b0sdk";
                break;
            }
            case 380008: {
                description = "\u9519\u8bef\u547d\u4ee4\uff0c\u82e5sdk\u4e0d\u662f\u6700\u65b0\uff0c\u8bf7\u66f4\u65b0sdk";
                break;
            }
            case 380011: {
                description = "\u6821\u9a8c\u7801\u9519\u8bef";
                break;
            }
            case 380016: {
                description = "\u534f\u8bae\u7248\u672c\u9519\u8bef\uff0c\u8bf7\u5347\u7ea7\u8bbe\u5907\u7248\u672c\u548csdk\u7248\u672c\u6700\u65b0";
                break;
            }
            case 380017: {
                description = "\u534f\u8bae\u7248\u672c\u8fc7\u4f4e\uff0c\u8bf7\u5347\u7ea7\u8bbe\u5907\u7248\u672c\u548csdk\u7248\u672c\u6700\u65b0";
                break;
            }
            case 380018: {
                description = "\u534f\u8bae\u7248\u672c\u5df2\u88ab\u7981\u7528\uff0c\u8bf7\u5347\u7ea7\u8bbe\u5907\u7248\u672c\u548csdk\u7248\u672c\u6700\u65b0";
                break;
            }
            case 380021: {
                description = "\u5e8f\u5217\u53f7\u89e3\u6790\u51fa\u9519\uff0c\u8bf7\u68c0\u67e5\u7f51\u7edc\u662f\u5426\u4e0d\u7a33\u5b9a";
                break;
            }
            case 380022: {
                description = "\u5e8f\u5217\u53f7\u88ab\u7981\u6b62\uff0c\u8bf7\u68c0\u67e5\u7f51\u7edc\u662f\u5426\u4e0d\u7a33\u5b9a";
                break;
            }
            case 380023: {
                description = "\u5e8f\u5217\u53f7\u91cd\u590d\uff0c\u8bf7\u68c0\u67e5\u7f51\u7edc\u662f\u5426\u4e0d\u7a33\u5b9a";
                break;
            }
            case 380024: {
                description = "\u76f8\u540c\u5e8f\u5217\u53f7\u77ed\u65f6\u95f4\u5185\u5927\u91cf\u91cd\u590d\u8bf7\u6c42\uff0c\u8bf7\u68c0\u67e5\u7f51\u7edc\u662f\u5426\u4e0d\u7a33\u5b9a";
                break;
            }
            case 380025: {
                description = "\u5e8f\u5217\u53f7\u4e0d\u518d\u652f\u6301\uff0c\u8bf7\u68c0\u67e5\u7f51\u7edc\u662f\u5426\u4e0d\u7a33\u5b9a";
                break;
            }
            case 380031: {
                description = "\u672c\u5730\u65e0\u6cd5\u54cd\u5e94";
                break;
            }
            case 380032: {
                description = "\u672c\u5730\u4e3b\u52a8\u62d2\u7edd";
                break;
            }
            case 380033: {
                description = "\u65e0\u6cd5\u63a5\u53d7\u8bf7\u6c42";
                break;
            }
            case 380034: {
                description = "\u8bbe\u5907\u52a0\u5bc6\u7b97\u6cd5\u4e0d\u5339\u914d";
                break;
            }
            case 380036: {
                description = "\u8bbe\u5907\u7c7b\u578b\u9519\u8bef";
                break;
            }
            case 380037: {
                description = "\u8bbe\u5907\u7c7b\u578b\u4e0d\u518d\u652f\u6301";
                break;
            }
            case 380041: {
                description = "\u8bbe\u5907\u65e0\u6cd5\u54cd\u5e94\uff0c\u53ef\u80fd\u9700\u8981\u65ad\u6389\u90e8\u5206\u9884\u89c8\u6216\u56de\u653e\u5df2\u91ca\u653e\u8bbe\u5907\u7684\u8fde\u63a5\u6570";
                break;
            }
            case 380042: {
                description = "\u64cd\u4f5c\u7801\u9519\u8bef\uff0c\u91cd\u65b0\u83b7\u53d6\u64cd\u4f5c\u7801\u5373\u53ef";
                break;
            }
            case 380043: {
                description = "\u8bbe\u5907\u6216\u5e73\u53f0\u672a\u627e\u5230\u5bf9\u5e94\u7684\u52a0\u5bc6\u7b97\u6cd5";
                break;
            }
            case 380044: {
                description = "\u62d2\u7edd\u64cd\u4f5c";
                break;
            }
            case 380045: {
                description = "\u8bbe\u5907\u7aef\u6ca1\u6709\u53ef\u7528\u8d44\u6e90";
                break;
            }
            case 380046: {
                description = "\u901a\u9053\u9519\u8bef";
                break;
            }
            case 380047: {
                description = "\u4e0d\u652f\u6301\u7684\u547d\u4ee4\uff0c\u8bbe\u5907\u4e0d\u652f\u6301\u6b64\u64cd\u4f5c\u6216\u9700\u8981\u5347\u7ea7";
                break;
            }
            case 380048: {
                description = "\u6ca1\u6709\u6743\u9650";
                break;
            }
            case 380049: {
                description = "\u6ca1\u6709\u627e\u5230\u4f1a\u8bdd";
                break;
            }
            case 380051: {
                description = "\u8be5\u901a\u9053\u5df2\u5728\u53d1\u6d41";
                break;
            }
            case 380052: {
                description = "\u53d6\u6d41\u5730\u5740\u91cd\u590d";
                break;
            }
            case 380053: {
                description = "\u4e0d\u652f\u6301\u7684\u7801\u6d41\u7c7b\u578b";
                break;
            }
            case 380054: {
                description = "\u4e0d\u652f\u6301\u7684\u4f20\u8f93\u65b9\u5f0f";
                break;
            }
            case 380055: {
                description = "\u8fde\u63a5\u9884\u89c8\u6d41\u5a92\u4f53\u670d\u52a1\u5668\u5931\u8d25\uff0c\u8bf7\u91cd\u8bd5";
                break;
            }
            case 380056: {
                description = "\u67e5\u8be2\u8bbe\u5907\u516c\u7f51\u51fa\u53e3\u5730\u5740\u5931\u8d25\uff0c\u8bf7\u68c0\u67e5\u7f51\u7edc\u73af\u5883\u540e\u91cd\u8bd5\uff0c\u6216\u8005\u66f4\u6362\u8def\u7531\u5c1d\u8bd5";
                break;
            }
            case 380057: {
                description = "\u6e20\u9053nvr\u51fa\u9519\uff0c\u9519\u8bef\u672a\u77e5";
                break;
            }
            case 380061: {
                description = "\u67e5\u8be2\u5f55\u50cf\u7684\u5f00\u59cb\u65f6\u95f4\u9519\u8bef";
                break;
            }
            case 380062: {
                description = "\u67e5\u8be2\u5f55\u50cf\u7684\u7ed3\u675f\u65f6\u95f4\u9519\u8bef";
                break;
            }
            case 380063: {
                description = "\u67e5\u8be2\u5f55\u50cf\u5931\u8d25";
                break;
            }
            case 380066: {
                description = "\u4e0d\u652f\u6301\u7684\u56de\u653e\u7c7b\u578b";
                break;
            }
            case 380067: {
                description = "\u6ca1\u6709\u627e\u5230\u6587\u4ef6";
                break;
            }
            case 380068: {
                description = "\u56de\u653e\u7684\u5f00\u59cb\u65f6\u95f4\u9519\u8bef";
                break;
            }
            case 380069: {
                description = "\u56de\u653e\u7684\u7ed3\u675f\u65f6\u95f4\u9519\u8bef";
                break;
            }
            case 380070: {
                description = "\u8be5\u65f6\u95f4\u6bb5\u5185\u6ca1\u6709\u5f55\u50cf";
                break;
            }
            case 380071: {
                description = "\u8fde\u63a5\u56de\u653e\u670d\u52a1\u5668\u5931\u8d25\uff0c\u8bf7\u7a0d\u540e\u518d\u8bd5";
                break;
            }
            case 380076: {
                description = "\u4e0d\u652f\u6301\u7684\u8bed\u97f3\u7f16\u7801\u7c7b\u578b";
                break;
            }
            case 380077: {
                description = "\u8be5\u901a\u9053\u5df2\u5728\u5bf9\u8bb2\uff0c\u505c\u6b62\u5bf9\u8bb2\u540e\u8bbe\u5907\u9700\u8981\u77ed\u6682\u7684\u65f6\u95f4\u91ca\u653e\u8d44\u6e90";
                break;
            }
            case 380078: {
                description = "\u548c\u76ee\u7684\u5730\u5740\u5df2\u6709\u94fe\u63a5";
                break;
            }
            case 380079: {
                description = "\u4e0d\u652f\u6301\u5bf9\u8bb2";
                break;
            }
            case 380080: {
                description = "\u5bf9\u8bb2\u901a\u9053\u53f7\u9519\u8bef";
                break;
            }
            case 380081: {
                description = "\u8fde\u63a5\u5bf9\u8bb2\u670d\u52a1\u5668\u5931\u8d25\uff0c\u8bf7\u7a0d\u540e\u518d\u8bd5";
                break;
            }
            case 380082: {
                description = "\u8bbe\u5907\u7aef\u62d2\u7edd\u8fde\u63a5\uff0c\u91cd\u542f\u4e0b\u8bbe\u5907\u518d\u8bd5";
                break;
            }
            case 380083: {
                description = "\u8bbe\u5907\u8d44\u6e90\u53d7\u9650";
                break;
            }
            case 380086: {
                description = "\u6ca1\u6709\u672c\u5730\u5b58\u50a8";
                break;
            }
            case 380087: {
                description = "\u6b63\u5728\u683c\u5f0f\u5316";
                break;
            }
            case 380088: {
                description = "\u683c\u5f0f\u5316\u5931\u8d25";
                break;
            }
            case 380091: {
                description = "\u670d\u52a1\u5668\u62d2\u7edd\u8bbe\u5907\u5347\u7ea7\u8bf7\u6c42";
                break;
            }
            case 380092: {
                description = "\u6ca1\u6709\u627e\u5230\u8bf7\u6c42\u7684\u7248\u672c";
                break;
            }
            case 380093: {
                description = "\u4e0d\u9700\u8981\u5347\u7ea7";
                break;
            }
            case 380094: {
                description = "\u5347\u7ea7\u670d\u52a1\u5668\u4e0d\u5728\u7ebf";
                break;
            }
            case 380095: {
                description = "\u5347\u7ea7\u670d\u52a1\u5668\u8fbe\u5230\u6700\u5927\u8d1f\u8f7d\uff0c\u8bf7\u7a0d\u540e\u518d\u8bd5";
                break;
            }
            case 380101: {
                description = "\u6b63\u5728\u5347\u7ea7";
                break;
            }
            case 380102: {
                description = "\u5347\u7ea7\u5931\u8d25";
                break;
            }
            case 380103: {
                description = "\u5347\u7ea7\u5199Flash\u5931\u8d25";
                break;
            }
            case 380104: {
                description = "\u5347\u7ea7\u8bed\u8a00\u4e0d\u5339\u914d";
                break;
            }
            case 380106: {
                description = "\u5bc6\u7801\u66f4\u65b0\u5931\u8d25\uff0c\u6ca1\u6709\u5bf9\u5e94\u7528\u6237";
                break;
            }
            case 380107: {
                description = "\u539f\u59cb\u5bc6\u7801\u9519\u8bef";
                break;
            }
            case 380108: {
                description = "\u5bc6\u7801\u66f4\u65b0\u5931\u8d25\uff0c\u65b0\u5bc6\u7801\u89e3\u5bc6\u5931\u8d25";
                break;
            }
            case 380109: {
                description = "\u65b0\u5bc6\u7801\u4e0d\u7b26\u5408\u89c4\u5219";
                break;
            }
            case 380110: {
                description = "\u66f4\u65b0\u5bc6\u7801\u5931\u8d25\uff0c\u5199Flash\u5931\u8d25";
                break;
            }
            case 380111: {
                description = "\u66f4\u65b0\u5bc6\u7801\u5931\u8d25\uff0c\u5176\u4ed6\u539f\u56e0";
                break;
            }
            case 380116: {
                description = "\u9a8c\u8bc1\u5bc6\u7801\u5931\u8d25";
                break;
            }
            case 380121: {
                description = "\u8bbe\u5907\u4e0d\u5728\u7ebf";
                break;
            }
            case 380122: {
                description = "\u4e3a\u4e86\u4fdd\u62a4\u8bbe\u5907\uff0c\u62d2\u7edd\u8bf7\u6c42";
                break;
            }
            case 380123: {
                description = "\u8bbe\u5907\u7684\u8fde\u63a5\u6570\u8fbe\u5230\u4e0a\u9650";
                break;
            }
            case 380124: {
                description = "\u8981\u6c42\u5ba2\u6237\u7aef\u65ad\u5f00\u4e0e\u8bbe\u5907\u8fde\u63a5";
                break;
            }
            case 380125: {
                description = "\u8bbe\u5907\u62d2\u7edd\u5e73\u53f0\u53d1\u9001\u7684\u5ba2\u6237\u7aef\u8fde\u63a5\u8bf7\u6c42";
                break;
            }
            case 380126: {
                description = "\u670d\u52a1\u5668\u5411\u9a8c\u8bc1\u4e2d\u5fc3\u9a8c\u8bc1\u7528\u6237\u6743\u9650\u5931\u8d25";
                break;
            }
            case 380127: {
                description = "\u8bbe\u5907\u5f00\u542f\u4e86\u9690\u79c1\u4fdd\u62a4";
                break;
            }
            case 380128: {
                description = "\u8d26\u6237\u5f00\u542f\u4e86\u7ec8\u7aef\u7ed1\u5b9a\uff0c\u672a\u7ed1\u5b9a\u7684\u624b\u673a\u65e0\u6cd5\u767b\u5f55\uff0c\u8bf7\u901a\u8fc7i.ys7.com\u5173\u95ed\u7ec8\u7aef\u7ed1\u5b9a";
                break;
            }
            case 380131: {
                description = "\u4e0d\u652f\u6301\u7684\u5e03\u64a4\u9632\u7c7b\u578b\uff0c\u90e8\u5206\u6444\u50cf\u673a\u8bbe\u5907\u4e0d\u652f\u6301\u5728\u5bb6/\u5916\u51fa/\u7761\u7720\u6a21\u5f0f\u7684\u5207\u6362";
                break;
            }
            case 380132: {
                description = "\u5e03\u64a4\u9632\u5931\u8d25";
                break;
            }
            case 380133: {
                description = "\u5f3a\u5236\u5e03\u64a4\u9632\u5931\u8d25";
                break;
            }
            case 380134: {
                description = "\u9700\u8981\u5f3a\u5236\u5e03\u64a4\u9632\uff0c\u6536\u5230\u6b64\u9519\u8bef\u7801\u540e\uff0c\u53ef\u4ee5\u8c03\u7528\u5f3a\u5236\u5e03\u64a4\u9632\u63a5\u53e3";
                break;
            }
            case 380141: {
                description = "\u672a\u627e\u5230\u4e91\u5b58\u50a8\u670d\u52a1\u5668";
                break;
            }
            case 380142: {
                description = "\u6ca1\u6709\u5f00\u901a\u4e91\u5b58\u50a8";
                break;
            }
            case 380145: {
                description = "\u6587\u4ef6\u5df2\u5230\u7ed3\u5c3e";
                break;
            }
            case 380146: {
                description = "\u65e0\u6548\u7684session";
                break;
            }
            case 380147: {
                description = "\u65e0\u6548\u7684\u6587\u4ef6\uff0c\u53ef\u80fd\u662f\u8be5\u6587\u4ef6\u5728\u4e91\u5b58\u50a8\u670d\u52a1\u5668\u4e0a\u5df2\u8fc7\u5b58\u50a8\u671f\u9650";
                break;
            }
            case 380148: {
                description = "\u672a\u77e5\u7684\u4e91\u5b58\u50a8\u7c7b\u578b";
                break;
            }
            case 380149: {
                description = "\u4e0d\u652f\u6301\u7684\u6587\u4ef6\u7c7b\u578b";
                break;
            }
            case 380150: {
                description = "\u65e0\u6548\u7684\u6587\u4ef6";
                break;
            }
            case 380151: {
                description = "\u914d\u989d\u5df2\u6ee1";
                break;
            }
            case 380152: {
                description = "\u6587\u4ef6\u5df2\u6ee1";
                break;
            }
            case 380155: {
                description = "\u8f6f\u4ef6\u5347\u7ea7\u5e73\u53f0\u4e0d\u5339\u914d";
                break;
            }
            case 380156: {
                description = "\u8f6f\u4ef6\u5347\u7ea7\u7a7a\u95f4\u4e0d\u5339\u914d";
                break;
            }
            case 380157: {
                description = "\u8f6f\u4ef6\u5347\u7ea7\u5185\u5b58\u4e0d\u5339\u914d";
                break;
            }
            case 380158: {
                description = "\u8f6f\u4ef6\u5347\u7ea7\u4e3b\u7c7b\u578b\u4e0d\u5339\u914d";
                break;
            }
            case 380159: {
                description = "\u8f6f\u4ef6\u5347\u7ea7\u6b21\u7c7b\u578b\u4e0d\u5339\u914d";
                break;
            }
            case 380160: {
                description = "\u6587\u4ef6\u4e2a\u6570\u503c\u65e0\u6548";
                break;
            }
            case 380161: {
                description = "\u5347\u7ea7\u5305\u957f\u5ea6\u503c\u65e0\u6548";
                break;
            }
            case 380162: {
                description = "\u8f6f\u4ef6\u5347\u7ea7\u6821\u9a8c\u548c\u9519\u8bef";
                break;
            }
            case 380163: {
                description = "\u5347\u7ea7\u524d\u7aef\u6570\u636e\u6444\u50cf\u673a\u5931\u8d25";
                break;
            }
            case 380164: {
                description = "\u6ca1\u6709\u8d44\u6e90";
                break;
            }
            case 380165: {
                description = "\u6ca1\u6709\u6743\u9650";
                break;
            }
            case 380166: {
                description = "\u6b63\u5728\u91cd\u542f";
                break;
            }
            case 380167: {
                description = "\u6ca1\u6709\u5185\u5b58";
                break;
            }
            case 380168: {
                description = "\u53c2\u6570\u9519\u8bef";
                break;
            }
            case 380169: {
                description = "\u5347\u7ea7\u5305\u5934\u90e8\u6570\u636e\u9519\u8bef";
                break;
            }
            case 380170: {
                description = "\u4e0b\u8f7d\u5931\u8d25";
                break;
            }
            case 380171: {
                description = "\u8def\u5f84\u6216\u6587\u4ef6\u540d\u9519";
                break;
            }
            case 380172: {
                description = "\u4e0b\u8f7d\u53c2\u6570\u9519\u8bef";
                break;
            }
            case 380173: {
                description = "ftp\u5efa\u7acb\u547d\u4ee4\u51fa\u9519";
                break;
            }
            case 380174: {
                description = "ftp\u6267\u884c\u547d\u4ee4\u5931\u8d25";
                break;
            }
            case 380175: {
                description = "ftp\u8fde\u63a5\u521d\u59cb\u5316\u5931\u8d25";
                break;
            }
            case 380176: {
                description = "ftp\u5f02\u5e38\u4e2d\u65ad";
                break;
            }
            case 380177: {
                description = "ftp select\u51fa\u9519";
                break;
            }
            case 380178: {
                description = "ftp\u83b7\u53d6\u6570\u636e\u5957\u63a5\u5b57\u51fa\u9519";
                break;
            }
            case 380179: {
                description = "ftp\u63a5\u6536\u6570\u636e\u51fa\u9519";
                break;
            }
            case 380180: {
                description = "ftp\u7f13\u51b2\u533a\u51fa\u9519";
                break;
            }
            case 380181: {
                description = "\u4e0b\u8f7d\u6587\u4ef6\u6821\u9a8c\u5931\u8d25";
                break;
            }
            case 380182: {
                description = "ftp\u8fde\u63a5\u51fa\u9519";
                break;
            }
            case 380183: {
                description = "ftp \u767b\u9646\u5931\u8d25";
                break;
            }
            case 380184: {
                description = "\u83b7\u53d6\u6587\u4ef6\u4fe1\u606f\u5931\u8d25";
                break;
            }
            case 380186: {
                description = "\u8bbe\u5907\u672c\u5730\u6293\u56fe\u5931\u8d25";
                break;
            }
            case 380187: {
                description = "\u56fe\u7247\u7f13\u5b58\u7533\u8bf7\u5931\u8d25";
                break;
            }
            case 380188: {
                description = "PMS\u57df\u540d\u89e3\u6790\u9519\u8bef";
                break;
            }
            case 380189: {
                description = "PMS\u8fde\u63a5\u5931\u8d25";
                break;
            }
            case 380190: {
                description = "\u521b\u5efaPMS\u62a5\u6587\u9519\u8bef";
                break;
            }
            case 380191: {
                description = "PMS\u53d1\u9001\u6570\u636e\u9519\u8bef";
                break;
            }
            case 380192: {
                description = "PMS\u63a5\u6536\u6570\u636e\u9519\u8bef";
                break;
            }
            case 380193: {
                description = "PMS\u5e94\u7b54\u62a5\u6587\u89e3\u6790\u9519\u8bef";
                break;
            }
            case 380194: {
                description = "\u83b7\u53d6URL\u5931\u8d25";
                break;
            }
            case 380200: {
                description = "\u5ba2\u6237\u7aef\u9519\u8bef\u53f7";
                break;
            }
            case 380201: {
                description = "\u53c2\u6570\u9519\u8bef";
                break;
            }
            case 380202: {
                description = "\u5206\u914d\u8d44\u6e90\u5931\u8d25";
                break;
            }
            case 380203: {
                description = "\u53d1\u9001\u9519\u8bef";
                break;
            }
            case 380204: {
                description = "\u63a5\u6536\u9519\u8bef, \u5bf9\u65b9\u65ad\u5f00\u8fde\u63a5\u6240\u81f4";
                break;
            }
            case 380205: {
                description = "\u89e3\u6790\u62a5\u6587\u9519\u8bef";
                break;
            }
            case 380206: {
                description = "\u751f\u6210\u62a5\u6587\u9519\u8bef";
                break;
            }
            case 380207: {
                description = "\u521d\u59cb\u5316Socket\u5931\u8d25";
                break;
            }
            case 380208: {
                description = "\u521b\u5efasocket\u5931\u8d25";
                break;
            }
            case 380209: {
                description = "\u8fde\u63a5\u670d\u52a1\u5668\u5931\u8d25";
                break;
            }
            case 380210: {
                description = "cas\u5e93\u672a\u521d\u59cb\u5316";
                break;
            }
            case 380211: {
                description = "\u8d85\u8fc7CASCLIENT\u5e93\u652f\u6301\u7684\u6700\u5927\u6570";
                break;
            }
            case 380212: {
                description = "\u4fe1\u4ee4\u53d1\u9001\u8d85\u65f6";
                break;
            }
            case 380213: {
                description = "\u4fe1\u4ee4\u63a5\u6536\u8d85\u65f6\uff0c\u8d85\u65f6\u65f6\u95f4\u5185\u4fe1\u4ee4\u6ca1\u6709\u56de\u5e94";
                break;
            }
            case 380214: {
                description = "\u521b\u5efa\u6570\u636e\u5305packet\u5931\u8d25";
                break;
            }
            case 380215: {
                description = "\u89e3\u6790\u6570\u636e\u5305\u9519\u8bef";
                break;
            }
            case 380216: {
                description = "\u7528\u6237\u4e2d\u9014\u5f3a\u884c\u9000\u51fa";
                break;
            }
            case 380217: {
                description = "\u83b7\u53d6\u672c\u5730\u7aef\u53e3\u9519\u8bef";
                break;
            }
            case 380218: {
                description = "base64\u7f16\u7801\u51fa\u9519";
                break;
            }
            case 380219: {
                description = "base64\u7f16\u7801\u5931\u8d25";
                break;
            }
            case 380220: {
                description = "\u63a5\u6536\u6570\u636e\u9519\u8bef";
                break;
            }
            case 380221: {
                description = "AES\u52a0\u5bc6\u51fa\u9519";
                break;
            }
            case 380222: {
                description = "AES\u89e3\u5bc6\u51fa\u9519";
                break;
            }
            case 380223: {
                description = "\u4e0d\u652f\u6301\u7684\u64cd\u4f5c";
                break;
            }
            case 380224: {
                description = "P2P\u6253\u6d1e\u5931\u8d25";
                break;
            }
            case 380225: {
                description = "\u53d1\u9001\u6253\u6d1e\u5305\u5931\u8d25";
                break;
            }
            case 380226: {
                description = "\u7528\u6237\u5f3a\u5236\u4e2d\u6b62\u53d6\u6d41\u8fc7\u7a0b";
                break;
            }
            case 380227: {
                description = "\u7f13\u51b2\u533a\u6ee1";
                break;
            }
            case 380228: {
                description = "\u521d\u59cb\u5316ssl\u5931\u8d25";
                break;
            }
            case 380229: {
                description = "ssl\u8fde\u63a5\u5931\u8d25";
                break;
            }
            case 380249: {
                description = "\u8ba4\u8bc1\u7684\u5176\u4ed6\u9519\u8bef";
                break;
            }
            case 380250: {
                description = "\u8ba4\u8bc1\u7684\u6570\u636e\u5e93\u9519\u8bef";
                break;
            }
            case 380251: {
                description = "\u8ba4\u8bc1\u7684\u53c2\u6570\u9519\u8bef";
                break;
            }
            case 380252: {
                description = "\u8ba4\u8bc1\u7684\u6267\u884c\u5f02\u5e38";
                break;
            }
            case 380253: {
                description = "\u8ba4\u8bc1\u7684session\u4e0d\u6b63\u5e38";
                break;
            }
            case 380254: {
                description = "\u8ba4\u8bc1\u7684\u7f13\u5b58\u5f02\u5e38";
                break;
            }
            case 380255: {
                description = "\u8ba4\u8bc1\u7684\u65e0\u6743\u9650";
                break;
            }
            case 380260: {
                description = "\u6dfb\u52a0\u7684\u6444\u50cf\u673a\u548c\u8bbe\u5907\u4e0d\u5728\u540c\u4e00\u5c40\u57df\u7f51";
                break;
            }
            case 380261: {
                description = "\u6dfb\u52a0\u7684\u6444\u50cf\u673a\u88ab\u5176\u4ed6\u8bbe\u5907\u5173\u8054\u6216\u8d85\u65f6";
                break;
            }
            case 380262: {
                description = "\u6dfb\u52a0\u6444\u50cf\u673a\u7684\u5bc6\u7801\u9519\u8bef";
                break;
            }
            case 380263: {
                description = "\u5220\u9664\u6444\u50cf\u673a\u5931\u8d25";
                break;
            }
            case 380264: {
                description = "\u8bbe\u5907\u6dfb\u52a0\u7684\u6444\u50cf\u673a\u5df2\u6ee1";
                break;
            }
            case 380271: {
                description = "\u4e0d\u652f\u6301\u7684\u63a2\u6d4b\u5668\u7c7b\u578b";
                break;
            }
            case 380413: {
                description = "dba\u6216\u6570\u636e\u5e93\u8fde\u63a5\u9519\u8bef";
                break;
            }
            case 380414: {
                description = "\u7559\u8a00\u6587\u4ef6\u5df2\u6ee1";
                break;
            }
            case 380415: {
                description = "\u7559\u8a00\u6587\u4ef6\u5df2\u5b58\u5728";
                break;
            }
            case 380416: {
                description = "\u6587\u4ef6\u540d\u683c\u5f0f\u9519\u8bef";
                break;
            }
            case 380417: {
                description = "\u65e0\u6743\u9650\u8bbf\u95ee\u8be5\u6587\u4ef6";
                break;
            }
            case 380418: {
                description = "\u83b7\u53d6\u6587\u4ef6\u5931\u8d25";
                break;
            }
            case 380419: {
                description = "\u6587\u4ef6\u4e0d\u5b58\u5728";
                break;
            }
            case 380420: {
                description = "\u4e0b\u8f7d\u6587\u4ef6\u5931\u8d25";
                break;
            }
            case 380421: {
                description = "\u672a\u6536\u5230Manager\u56de\u5e94";
                break;
            }
            case 380500: {
                description = "\u6b63\u5728\u8c03\u7528\u9884\u7f6e\u70b9\uff0c\u952e\u63a7\u52a8\u4f5c\u65e0\u6548";
                break;
            }
            case 380501: {
                description = "\u5f53\u524d\u6b63\u5728\u58f0\u6e90\u5b9a\u4f4d";
                break;
            }
            case 380502: {
                description = "\u952e\u63a7\u52a8\u4f5c\u8d85\u65f6(\u5f53\u524d\u6b63\u5728\u8f68\u8ff9\u5de1\u822a)";
                break;
            }
            case 380503: {
                description = "\u5f53\u524d\u9884\u7f6e\u70b9\u4fe1\u606f\u65e0\u6548";
                break;
            }
            case 380504: {
                description = "\u8be5\u9884\u7f6e\u70b9\u5df2\u662f\u5f53\u524d\u4f4d\u7f6e";
                break;
            }
            case 380505: {
                description = "\u58f0\u6e90\u5b9a\u4f4d\u5df2\u5f00\u542f\uff0c\u4e0d\u5141\u8bb8\u8c03\u7528\u9884\u7f6e\u70b9";
                break;
            }
            case 380506: {
                description = "\u6b63\u5728\u8c03\u7528\u9884\u7f6e\u70b9";
                break;
            }
            case 380507: {
                description = "\u6b63\u5728\u5f00\u542f\u9690\u79c1\u906e\u853d";
                break;
            }
            case 380508: {
                description = "\u6b63\u5728\u5173\u95ed\u9690\u79c1\u906e\u853d";
                break;
            }
            case 380509: {
                description = "\u4e91\u53f0\u5f53\u524d\u64cd\u4f5c\u5931\u8d25";
                break;
            }
            case 380510: {
                description = "\u5f53\u524d\u9884\u7f6e\u70b9\u8d85\u8fc7\u6700\u5927\u4e2a\u6570";
                break;
            }
            case 380511: {
                description = "\u8bbe\u5907\u5904\u4e8e\u9690\u79c1\u906e\u853d\u72b6\u6001\uff08\u5173\u95ed\u955c\u5934\u540e\uff0c\u518d\u53bb\u64cd\u4f5c\u4e91\u53f0\u76f8\u5173\uff09";
                break;
            }
            case 380512: {
                description = "\u8bbe\u5907\u6b63\u5728\u955c\u50cf\u64cd\u4f5c\uff08\u8bbe\u5907\u955c\u50cf\u8981\u51e0\u79d2\u949f\uff0c\u9632\u6b62\u9891\u7e41\u955c\u50cf\u64cd\u4f5c\uff09";
                break;
            }
            case 380513: {
                description = "\u8bbe\u5907\u6b63\u5728\u952e\u63a7\u52a8\u4f5c\uff08\u4e0a\u4e0b\u5de6\u53f3\uff09(\u4e00\u4e2a\u5ba2\u6237\u7aef\u5728\u4e0a\u4e0b\u5de6\u53f3\u63a7\u5236\uff0c\u53e6\u5916\u4e00\u4e2a\u5728\u5f00\u5176\u5b83\u4e1c\u897f)";
                break;
            }
            case 380514: {
                description = "\u8bbe\u5907\u5904\u4e8e\u8bed\u97f3\u5bf9\u8bb2\u72b6\u6001(\u6b64\u65f6\u4e0d\u53ef\u4ee5\u64cd\u4f5c\u4e91\u53f0\uff0c\u8be5\u64cd\u4f5c\u7801\u4ec5\u7528\u4e8e\u4e91\u53f0\u64cd\u4f5c\u662f\uff09";
                break;
            }
            case 380515: {
                description = "\u8bbe\u5907\u4e91\u53f0\u65cb\u8f6c\u5230\u8fbe\u4e0a\u9650\u4f4d\uff0c\u5efa\u8baeapp\u5c42\u6536\u5230\u6b64\u9519\u8bef\u7801\u540e\u63d0\u793a\u7528\u6237\u6216\u7981\u6b62\u7528\u6237\u7ee7\u7eed\u64cd\u4f5c";
                break;
            }
            case 380516: {
                description = "\u8bbe\u5907\u4e91\u53f0\u65cb\u8f6c\u5230\u8fbe\u4e0b\u9650\u4f4d\uff0c\u5efa\u8baeapp\u5c42\u6536\u5230\u6b64\u9519\u8bef\u7801\u540e\u63d0\u793a\u7528\u6237\u6216\u7981\u6b62\u7528\u6237\u7ee7\u7eed\u64cd\u4f5c";
                break;
            }
            case 380517: {
                description = "\u8bbe\u5907\u4e91\u53f0\u65cb\u8f6c\u5230\u8fbe\u5de6\u9650\u4f4d\uff0c\u5efa\u8baeapp\u5c42\u6536\u5230\u6b64\u9519\u8bef\u7801\u540e\u63d0\u793a\u7528\u6237\u6216\u7981\u6b62\u7528\u6237\u7ee7\u7eed\u64cd\u4f5c";
                break;
            }
            case 380518: {
                description = "\u8bbe\u5907\u4e91\u53f0\u65cb\u8f6c\u5230\u8fbe\u53f3\u9650\u4f4d\uff0c\u5efa\u8baeapp\u5c42\u6536\u5230\u6b64\u9519\u8bef\u7801\u540e\u63d0\u793a\u7528\u6237\u6216\u7981\u6b62\u7528\u6237\u7ee7\u7eed\u64cd\u4f5c";
                break;
            }
            case 389999: {
                description = "\u672a\u77e5\u9519\u8bef";
                break;
            }
            case 389998: {
                description = "\u6ca1\u6709\u521d\u59cb\u5316";
                break;
            }
            case 389997: {
                description = "\u5934\u6570\u636e\u4e3a0";
                break;
            }
            case 389996: {
                description = "\u4e91\u5b58\u50a8\u5730\u5740\u4e3a\u7a7a";
                break;
            }
            case 389995: {
                description = "\u53d6\u6d41\u8d85\u65f6";
                break;
            }
            case 381101: {
                description = "socket\u9519\u8bef\uff0c\u53ef\u80fd\u662f\u672a\u6536\u5230\u53d6\u6d41\u7ed3\u675f\u6807\u8bb0\uff0c\u8bbe\u5907\u7aef\u5373\u5df2\u65ad\u5f00socket";
                break;
            }
            case 381102: {
                description = "socket\u63a5\u6536\u9519\u8bef\uff0c\u53ef\u80fd\u662f\u672a\u6536\u5230\u53d6\u6d41\u7ed3\u675f\u6807\u8bb0\uff0c\u8bbe\u5907\u7aef\u5373\u5df2\u65ad\u5f00socket";
                break;
            }
            case 381103: {
                description = "socket\u53d1\u9001\u9519\u8bef\uff0c\u53ef\u80fd\u662f\u672a\u6536\u5230\u53d6\u6d41\u7ed3\u675f\u6807\u8bb0\uff0c\u8bbe\u5907\u7aef\u5373\u5df2\u65ad\u5f00socket";
                break;
            }
            case 382101: {
                description = "\u97f3\u9891socket\u9519\u8bef";
                break;
            }
            case 382102: {
                description = "\u97f3\u9891\u6d41\u63a5\u6536\u9519\u8bef";
                break;
            }
            case 382103: {
                description = "\u97f3\u9891\u6d41\u53d1\u9001\u9519\u8bef";
                break;
            }
            case 383100: {
                description = "\u56de\u653e\u5b8c\u6210";
                break;
            }
            case 383200: {
                description = "\u670d\u52a1\u5668\u8fd4\u56de\u7684\u7ed3\u675f\u6807\u5fd7\uff0c\u8868\u793a\u771f\u6b63\u7ed3\u675f";
                break;
            }
            default: {
                description = "cas\u9519\u8bef\uff0c\u9519\u8bef\u672a\u77e5";
            }
        }
        return description;
    }
}


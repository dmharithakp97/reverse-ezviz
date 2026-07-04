/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Application
 *  android.text.TextUtils
 */
package com.videogo.auth;

import android.app.Application;
import android.text.TextUtils;
import com.videogo.constant.Config;
import com.videogo.openapi.EZPlatformType;
import com.videogo.openapi.bean.EZAccessToken;
import com.videogo.util.LocalInfo;
import com.videogo.util.LogUtil;

public class EZAuthApi {
    public static EZPlatformType platformType = EZPlatformType.EZPlatformTypeNULL;

    public void init(Application application, String appKey, String apiUrl, String webUrl) {
        LocalInfo.init(application, appKey);
        LocalInfo.getInstance().setServAddr(apiUrl);
        LocalInfo.getInstance().setAuthServAddr(webUrl);
    }

    public static boolean isLogin() {
        return Config.ENABLE_SDK_TKTOKEN ? !TextUtils.isEmpty((CharSequence)LocalInfo.getInstance().getEZAccesstoken().getAccessTokenOrHttpToken()) && platformType == EZPlatformType.EZPlatformTypeOPENSDK : !TextUtils.isEmpty((CharSequence)LocalInfo.getInstance().getEZAccesstoken().getAccessTokenOrHttpToken()) && (platformType != EZPlatformType.EZPlatformTypeGLOBALSDK || !TextUtils.isEmpty((CharSequence)LocalInfo.getInstance().getEZAccesstoken().getAreaDomain()));
    }

    public void logout() {
    }

    public void showSDKLog(boolean showLog) {
    }

    public void setLogCallback(LogUtil.LogCallback callback) {
        LogUtil.setLogCallback(callback);
    }

    public void setAccessToken(String accessToken) {
    }

    public void setServerUrl(String apiUrl, String webUrl) {
        LocalInfo.getInstance().setServAddr(apiUrl);
        LocalInfo.getInstance().setAuthServAddr(webUrl);
    }

    public EZAccessToken getEZAccessToken() {
        return null;
    }

    public void openLoginPage() {
    }

    public void openLoginPage(int flag) {
    }
}


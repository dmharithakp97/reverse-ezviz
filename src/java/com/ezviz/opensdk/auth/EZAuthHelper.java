/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.content.Intent
 *  android.net.Uri
 *  android.net.Uri$Builder
 *  android.text.TextUtils
 *  android.util.Log
 *  android.widget.Toast
 */
package com.ezviz.opensdk.auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.ezviz.opensdk.auth.EZAuthAPI;
import com.ezviz.opensdk.auth.EZAuthHandleActivity;
import com.videogo.exception.BaseException;
import com.videogo.openapi.BaseAPI;
import com.videogo.openapi.bean.EZAccessTokenInternal;
import com.videogo.util.LocalInfo;
import com.videogo.util.LogUtil;
import java.util.List;
import java.util.UUID;

public class EZAuthHelper {
    private static final String TAG = "EZAuthHelper";
    private static EZAuthHelper helper;
    private AuthReq authReq = new AuthReq();
    private OpenPageReq openPageReq = new OpenPageReq();
    private EZAuthHandleActivity authHandleActivity;

    protected static EZAuthHelper getHelper() {
        if (helper == null) {
            helper = new EZAuthHelper();
        }
        return helper;
    }

    private EZAuthHelper() {
    }

    protected void setAuthHandleActivity(EZAuthHandleActivity authHandleActivity) {
        this.authHandleActivity = authHandleActivity;
    }

    protected void sendAuthReq(Context context, EZAuthAPI.EZAuthPlatform ezAuthPatform) {
        boolean isValid;
        this.authReq = new AuthReq();
        this.authReq.appKey = BaseAPI.getInstance().getAppKey();
        this.authReq.bundleId = LocalInfo.getInstance().getPackageName();
        this.authReq.scope = "xxxx";
        this.authReq.state = UUID.randomUUID().toString();
        this.authReq.scheme = this.getScheme(ezAuthPatform);
        this.authReq.authority = "thirdAuth";
        this.authReq.authDomain = LocalInfo.getInstance().getOAuthServAddr();
        Uri uri = this.getAuthReqStringUri();
        Log.d((String)TAG, (String)("sendAuthReq getAuthReqStringUri = " + uri.toString()));
        Intent intent = new Intent("android.intent.action.VIEW", uri);
        List activities = context.getPackageManager().queryIntentActivities(intent, 0);
        boolean bl = isValid = !activities.isEmpty();
        if (isValid) {
            context.startActivity(intent);
        }
    }

    protected void sendOpenPageDeviceList(Context context, EZAuthAPI.EZAuthPlatform ezAuthPatform) {
        boolean isValid;
        this.openPageReq = new OpenPageReq();
        this.openPageReq.appKey = BaseAPI.getInstance().getAppKey();
        this.openPageReq.bundleId = LocalInfo.getInstance().getPackageName();
        this.openPageReq.page = "deviceList";
        this.openPageReq.scheme = this.getScheme(ezAuthPatform);
        this.openPageReq.authority = "openPage";
        this.openPageReq.authDomain = LocalInfo.getInstance().getOAuthServAddr();
        this.openPageReq.openId = LocalInfo.getInstance().getEZAccesstoken().getOpen_id();
        Uri uri = this.getOpenPageReqStringUri();
        Log.d((String)TAG, (String)("sendAuthReq getAuthReqStringUri = " + uri.toString()));
        Intent intent = new Intent("android.intent.action.VIEW", uri);
        List activities = context.getPackageManager().queryIntentActivities(intent, 0);
        boolean bl = isValid = !activities.isEmpty();
        if (isValid) {
            context.startActivity(intent);
        }
    }

    protected void sendOpenPageAlarmList(Context context, EZAuthAPI.EZAuthPlatform ezAuthPatform) {
        boolean isValid;
        this.openPageReq = new OpenPageReq();
        this.openPageReq.appKey = BaseAPI.getInstance().getAppKey();
        this.openPageReq.bundleId = LocalInfo.getInstance().getPackageName();
        this.openPageReq.page = "alarmList";
        this.openPageReq.scheme = this.getScheme(ezAuthPatform);
        this.openPageReq.authority = "openPage";
        this.openPageReq.authDomain = LocalInfo.getInstance().getOAuthServAddr();
        this.openPageReq.openId = LocalInfo.getInstance().getEZAccesstoken().getOpen_id();
        Uri uri = this.getOpenPageReqStringUri();
        Log.d((String)TAG, (String)("sendAuthReq getAuthReqStringUri = " + uri.toString()));
        Intent intent = new Intent("android.intent.action.VIEW", uri);
        List activities = context.getPackageManager().queryIntentActivities(intent, 0);
        boolean bl = isValid = !activities.isEmpty();
        if (isValid) {
            context.startActivity(intent);
        }
    }

    protected String getScheme(EZAuthAPI.EZAuthPlatform ezAuthPatform) {
        String string = null;
        if (ezAuthPatform == EZAuthAPI.EZAuthPlatform.EZVIZ) {
            string = "hikezvizinternational";
        } else if (ezAuthPatform == EZAuthAPI.EZAuthPlatform.EZVIZ_CHINA) {
            string = "hikezviz";
        } else if (ezAuthPatform == EZAuthAPI.EZAuthPlatform.HC_CONNECT) {
            string = "hikconnect";
        }
        return string;
    }

    protected Uri getAuthReqStringUri() {
        if (this.authReq != null) {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme(this.authReq.scheme);
            builder.authority(this.authReq.authority);
            builder.appendQueryParameter("appKey", this.authReq.appKey);
            builder.appendQueryParameter("bundleId", this.authReq.bundleId);
            builder.appendQueryParameter("scope", this.authReq.scope);
            builder.appendQueryParameter("state", this.authReq.state);
            builder.appendQueryParameter("authDomain", this.authReq.authDomain);
            return builder.build();
        }
        return null;
    }

    protected Uri getOpenPageReqStringUri() {
        if (this.openPageReq != null) {
            Intent intent = new Intent();
            Uri.Builder builder = new Uri.Builder();
            builder.scheme(this.openPageReq.scheme);
            builder.authority(this.openPageReq.authority);
            builder.appendQueryParameter("appKey", this.openPageReq.appKey);
            builder.appendQueryParameter("bundleId", this.openPageReq.bundleId);
            builder.appendQueryParameter("page", String.valueOf(this.openPageReq.page));
            builder.appendQueryParameter("openId", this.openPageReq.openId);
            builder.appendQueryParameter("authDomain", this.openPageReq.authDomain);
            return builder.build();
        }
        return null;
    }

    protected void analysisDate(Activity activity) {
        Uri uri = activity.getIntent().getData();
        if (uri != null) {
            this.dispatchUri(uri);
        }
    }

    private void dispatchUri(Uri uri) {
        System.out.println(uri.toString());
        try {
            String scheme = uri.getScheme();
            String domain = uri.getAuthority();
            if (this.authReq != null && (this.authReq.scheme + this.authReq.appKey).equalsIgnoreCase(scheme) && "authReturn".equalsIgnoreCase(domain)) {
                final String state = uri.getQueryParameter("state");
                String areaDomain = uri.getQueryParameter("areaDomain");
                String authDomain = uri.getQueryParameter("authDomain");
                final String authCode = uri.getQueryParameter("authCode");
                final String scope = uri.getQueryParameter("scope");
                if (TextUtils.isEmpty((CharSequence)this.authReq.state) || this.authReq.state.equalsIgnoreCase(state)) {
                    LocalInfo.getInstance().getEZAccesstoken().setAreaDomain(areaDomain);
                    LocalInfo.getInstance().getEZAccesstoken().setAreaAuthDomain(authDomain);
                    LocalInfo.getInstance().saveEZAccesstoken();
                    new Thread(new Runnable(){

                        @Override
                        public void run() {
                            try {
                                EZAccessTokenInternal ezAccessToken = BaseAPI.getInstance().OAuthCode(authCode, scope, state);
                                if (ezAccessToken != null) {
                                    if (EZAuthHelper.this.authHandleActivity != null) {
                                        EZAuthHelper.this.authHandleActivity.runOnUiThread(new Runnable(){

                                            @Override
                                            public void run() {
                                                EZAuthHelper.this.authHandleActivity.onAuthSuccess();
                                            }
                                        });
                                    }
                                } else if (EZAuthHelper.this.authHandleActivity != null) {
                                    EZAuthHelper.this.authHandleActivity.runOnUiThread(new Runnable(){

                                        @Override
                                        public void run() {
                                            EZAuthHelper.this.authHandleActivity.onAuthFail(1);
                                        }
                                    });
                                }
                            }
                            catch (BaseException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void authSuccess() {
        Intent intent = new Intent();
        BaseAPI.getInstance().setAccessToken(LocalInfo.getInstance().getEZAccesstoken().getAccessTokenOrHttpToken());
        intent.setAction("com.videogo.action.OAUTH_SUCCESS_ACTION");
        this.authHandleActivity.sendBroadcast(intent);
        LogUtil.d("EZAuthHandleActivity", "sendBroadcast:com.videogo.action.OAUTH_SUCCESS_ACTION");
        this.authHandleActivity.onAuthSuccess();
    }

    public void onAuthFail(int errorCode) {
        Toast.makeText((Context)this.authHandleActivity, (CharSequence)"Auth fail", (int)1).show();
        this.authHandleActivity.onAuthFail(errorCode);
    }

    class AuthReq {
        String scheme;
        String appKey;
        String bundleId;
        String scope;
        String state;
        String authority;
        String authDomain;

        AuthReq() {
        }
    }

    class OpenPageReq {
        String appKey;
        String bundleId;
        String openId;
        String page;
        String scheme;
        String authority;
        String authDomain;

        OpenPageReq() {
        }
    }

    static interface EZOAuthListener {
        public void onAuthSuccess();

        public void onAuthFail();
    }
}


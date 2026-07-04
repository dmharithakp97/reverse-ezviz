/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.AlertDialog
 *  android.app.AlertDialog$Builder
 *  android.content.Context
 *  android.content.DialogInterface
 *  android.content.DialogInterface$OnClickListener
 *  android.content.Intent
 *  android.graphics.Bitmap
 *  android.graphics.Color
 *  android.net.Uri
 *  android.net.http.SslError
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.text.TextUtils
 *  android.view.KeyEvent
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.ViewGroup$LayoutParams
 *  android.view.animation.Animation
 *  android.view.animation.Interpolator
 *  android.view.animation.LinearInterpolator
 *  android.view.animation.RotateAnimation
 *  android.webkit.DownloadListener
 *  android.webkit.JavascriptInterface
 *  android.webkit.SslErrorHandler
 *  android.webkit.WebView
 *  android.webkit.WebViewClient
 *  android.widget.FrameLayout$LayoutParams
 *  android.widget.ImageView
 *  android.widget.RelativeLayout
 *  android.widget.RelativeLayout$LayoutParams
 *  android.widget.Toast
 *  org.json.JSONException
 *  org.json.JSONObject
 */
package com.videogo.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.videogo.auth.EZAuthApi;
import com.videogo.constant.Config;
import com.videogo.openapi.BaseAPI;
import com.videogo.openapi.EZPlatformType;
import com.videogo.openapi.bean.EZAccessTokenInternal;
import com.videogo.openapi.model.req.WebLoginReq;
import com.videogo.restful.NameValuePair;
import com.videogo.util.HttpUtils;
import com.videogo.util.LocalInfo;
import com.videogo.util.LogUtil;
import com.videogo.util.Utils;
import com.videogo.widget.TitleBar;
import com.videogo.widget.WebViewEx;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONException;
import org.json.JSONObject;

public class EzvizWebViewActivity
extends Activity {
    private static String TAG = "EzvizWebViewActivity";
    public static final int WEBVIEW_ACTION_LOGIN = 0;
    public static final int WEBVIEW_ACTION_CLOUDPAGE = 3;
    public static final int WEBVIEW_ACTION_FORGETPASSWORD = 4;
    public static final String EXTRA_DEVICE_SERIAL = "serial";
    public static final String EXTRA_CAMERA_NO = "channel";
    public static final String OAUTH_SUCCESS = "/oauth/success";
    public static final String AUTHORIZE_SUCCESS = "/oauth/authorize/success";
    public static final String NOTICE = "/api/web/notice";
    public static final String DEVICE_UPGRADE = "upload";
    public static final String DEVICE_DISK_FORMAT = "diskFormat?";
    public static final String DEVICE_DISK_FORMAT_PROGRESS = "diskFormatProgress?";
    private static final String URL_CLOUD_PAGE = "/api/cloudpay/authEntry";
    private static final String URL_CLOUD_PAGE2 = "/api/cloud/cloudpay/authEntry";
    private static final String FORGET_PASSWORD = "/api/web/changePassword";
    private RelativeLayout mWebLayout;
    private TitleBar mTitleBar;
    private WebViewEx mWebView = null;
    private ImageView mProgressView;
    private static final int ID_WEBLAYOUT = 1;
    private static final int ID_TITLEBAR = 2;
    private static final int ID_WEBVIEW = 3;
    private static final int ID_PROGRESSVIEW = 4;
    private int mAction = 0;
    private Animation mRotateAnimation;
    private String mUrl = null;
    private BaseAPI mBaseAPI = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!this.initDate()) {
            return;
        }
        this.findViews();
        this.initTitleBar();
        this.initViews();
    }

    private boolean initDate() {
        this.mAction = this.getIntent().getIntExtra("com.videogo.EXTRA_WEBVIEW_ACTION", 0);
        if (!(this.mAction != 0 || LocalInfo.getInstance() == null || TextUtils.isEmpty((CharSequence)LocalInfo.getInstance().getEZAccesstoken().getAccessToken()) || EZAuthApi.platformType == EZPlatformType.EZPlatformTypeGLOBALSDK && TextUtils.isEmpty((CharSequence)LocalInfo.getInstance().getEZAccesstoken().getAreaDomain()))) {
            Intent intent = new Intent();
            BaseAPI.getInstance().setAccessToken(LocalInfo.getInstance().getEZAccesstoken().getAccessToken());
            intent.setAction("com.videogo.action.OAUTH_SUCCESS_ACTION");
            this.sendBroadcast(intent);
            LogUtil.d(TAG, "sendBroadcast:com.videogo.action.OAUTH_SUCCESS_ACTION");
            this.finish();
            return false;
        }
        this.mBaseAPI = BaseAPI.getInstance();
        this.mRotateAnimation = new RotateAnimation(0.0f, 720.0f, 1, 0.5f, 1, 0.5f);
        this.mRotateAnimation.setInterpolator((Interpolator)new LinearInterpolator());
        this.mRotateAnimation.setDuration(1200L);
        this.mRotateAnimation.setRepeatCount(-1);
        this.mRotateAnimation.setRepeatMode(1);
        return true;
    }

    private void findViews() {
        this.mWebLayout = new RelativeLayout((Context)this);
        this.mWebLayout.setId(1);
        this.mWebLayout.setBackgroundColor(Color.rgb((int)240, (int)240, (int)243));
        FrameLayout.LayoutParams webLayoutLp = new FrameLayout.LayoutParams(-1, -1);
        this.addContentView((View)this.mWebLayout, (ViewGroup.LayoutParams)webLayoutLp);
        this.mTitleBar = new TitleBar((Context)this);
        this.mTitleBar.setId(2);
        RelativeLayout.LayoutParams titleBarLp = new RelativeLayout.LayoutParams(-1, -2);
        this.mWebLayout.addView((View)this.mTitleBar, (ViewGroup.LayoutParams)titleBarLp);
        this.mWebView = new WebViewEx((Context)this);
        this.mWebView.getSettings().setAllowFileAccess(false);
        this.mWebView.setId(3);
        RelativeLayout.LayoutParams webViewLp = new RelativeLayout.LayoutParams(-1, -1);
        webViewLp.addRule(3, 2);
        this.mWebLayout.addView((View)this.mWebView, (ViewGroup.LayoutParams)webViewLp);
    }

    private void initTitleBar() {
        boolean showBackButton = Config.SHOW_WEBVIEW_BACK_BUTTON;
        if (showBackButton) {
            this.mTitleBar.addBackButton(new View.OnClickListener(){

                public void onClick(View v) {
                    if (EzvizWebViewActivity.this.mWebView.canGoBack()) {
                        EzvizWebViewActivity.this.mWebView.goBack();
                    } else {
                        EzvizWebViewActivity.this.onBackPressed();
                    }
                }
            });
        }
        this.mProgressView = this.mTitleBar.addRightProgress();
        this.mProgressView.setId(4);
        this.mProgressView.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                if (EzvizWebViewActivity.this.mProgressView.getAnimation() == null) {
                    EzvizWebViewActivity.this.loadUrl(true);
                }
            }
        });
    }

    private void initViews() {
        this.mWebView.getSettings().setJavaScriptEnabled(true);
        this.mWebView.getSettings().setBuiltInZoomControls(true);
        this.mWebView.getSettings().setSupportZoom(true);
        this.mWebView.addJavascriptInterface((Object)this, "deviceOperate");
        this.mWebView.setWebViewClient(new MyWebViewClient());
        this.mWebView.setDownloadListener(new MyDownloadListener());
        this.loadUrl(false);
    }

    private void loadUrl(boolean reload) {
        if (reload) {
            this.mWebView.reload();
            return;
        }
        String deviceSerial = null;
        ArrayList<NameValuePair> nvp = null;
        Intent intent = this.getIntent();
        switch (this.mAction) {
            case 0: {
                long time = System.currentTimeMillis();
                this.mUrl = this.mBaseAPI.getOpenWebUrl() + "/oauth/authorize" + WebLoginReq.getReqData();
                int areaID = this.getIntent().getIntExtra("Param", -1);
                if (areaID >= 0) {
                    this.mUrl = this.mUrl + "&areaId=" + areaID;
                }
                this.mUrl = this.mUrl + "&timesnap=" + Long.toString(time);
                this.mWebView.loadUrl(this.mUrl);
                break;
            }
            case 3: {
                this.mUrl = this.mBaseAPI.getServerUrl() + URL_CLOUD_PAGE2;
                Intent i = this.getIntent();
                deviceSerial = i.getStringExtra(EXTRA_DEVICE_SERIAL);
                int cameraChannel = i.getIntExtra(EXTRA_CAMERA_NO, -1);
                nvp = new ArrayList();
                nvp.add(new NameValuePair("accessToken", LocalInfo.getInstance().getEZAccesstoken().getAccessToken()));
                nvp.add(new NameValuePair("clientType", String.valueOf(13)));
                nvp.add(new NameValuePair("featureCode", LocalInfo.getInstance().getHardwareCode()));
                nvp.add(new NameValuePair("osVersion", Build.VERSION.RELEASE));
                nvp.add(new NameValuePair("clientVersion", "v5.27.3.20260319"));
                nvp.add(new NameValuePair("netType", BaseAPI.getInstance().getNetType()));
                nvp.add(new NameValuePair("sdkVersion", "v5.27.3.20260319"));
                nvp.add(new NameValuePair("deviceSerial", deviceSerial));
                nvp.add(new NameValuePair("channelNo", String.valueOf(cameraChannel)));
                nvp.add(new NameValuePair("appKey", BaseAPI.getInstance().getAppKey()));
                nvp.add(new NameValuePair("sessionId", LocalInfo.getInstance().getEZAccesstoken().getAccessToken()));
                nvp.add(new NameValuePair(EXTRA_DEVICE_SERIAL, deviceSerial));
                nvp.add(new NameValuePair("cf", "osa1"));
                this.mWebView.postUrl(this.mUrl, HttpUtils.getPostParam(nvp));
                break;
            }
            case 4: {
                this.mUrl = this.mBaseAPI.getServerUrl() + FORGET_PASSWORD;
                nvp = new ArrayList<NameValuePair>();
                nvp.add(new NameValuePair("accessToken", LocalInfo.getInstance().getEZAccesstoken().getAccessToken()));
                nvp.add(new NameValuePair("clientType", String.valueOf(13)));
                nvp.add(new NameValuePair("featureCode", LocalInfo.getInstance().getHardwareCode()));
                nvp.add(new NameValuePair("sdkVersion", "v5.27.3.20260319"));
                nvp.add(new NameValuePair("netType", BaseAPI.getInstance().getNetType()));
                nvp.add(new NameValuePair("osVersion", Build.VERSION.RELEASE));
                nvp.add(new NameValuePair("appKey", BaseAPI.getInstance().getAppKey()));
                this.mWebView.postUrl(this.mUrl, HttpUtils.getPostParam(nvp));
                break;
            }
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4 && this.mWebView.canGoBack()) {
            this.mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @JavascriptInterface
    public void evaluate(String operate_str, String data) {
        LogUtil.i(TAG, "evaluate, operator:" + operate_str + ", data:" + data);
        if (TextUtils.isEmpty((CharSequence)operate_str)) {
            LogUtil.i(TAG, "evaluate receive null operate_str, return");
            return;
        }
        if (operate_str.equalsIgnoreCase("changeTitle")) {
            try {
                JSONObject titleObj = new JSONObject(data);
                final String title = titleObj.optString("val");
                if (!TextUtils.isEmpty((CharSequence)title)) {
                    this.runOnUiThread(new Runnable(){

                        @Override
                        public void run() {
                            EzvizWebViewActivity.this.mTitleBar.setTitle(title);
                        }
                    });
                }
            }
            catch (JSONException e) {
                LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
            }
        } else if (operate_str.equalsIgnoreCase("postMessage")) {
            LogUtil.i(TAG, "evaluate: postMessage");
            if (TextUtils.isEmpty((CharSequence)data)) {
                return;
            }
            try {
                JSONObject rootObj = new JSONObject(data);
                final String type = rootObj.optString("type");
                int resultCode = rootObj.optInt("resultCode");
                String resultMsg = rootObj.optString("resultMsg");
                if (resultCode == 0) {
                    this.runOnUiThread(new Runnable(){

                        @Override
                        public void run() {
                            Toast.makeText((Context)EzvizWebViewActivity.this, (CharSequence)"\u4fee\u6539\u5bc6\u7801\u6210\u529f", (int)0).show();
                            EzvizWebViewActivity.this.finish();
                        }
                    });
                } else {
                    this.runOnUiThread(new Runnable(){

                        @Override
                        public void run() {
                            Toast.makeText((Context)EzvizWebViewActivity.this, (CharSequence)(type + " fail"), (int)0).show();
                        }
                    });
                }
            }
            catch (JSONException e) {
                LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
            }
        }
    }

    private void getRequestParam(String operate_str, String paramId) {
        String jsUrl = "javascript:window.deviceOperate.evaluate('" + operate_str + "',YsOpenSdkBridge.requestParam[\"" + paramId + "\"])";
        LogUtil.i(TAG, jsUrl);
        this.mWebView.loadUrl(jsUrl);
    }

    private class MyWebViewClient
    extends WebViewClient {
        private MyWebViewClient() {
        }

        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
            AlertDialog.Builder builder = new AlertDialog.Builder((Context)EzvizWebViewActivity.this);
            builder.setMessage((CharSequence)"SSL verification is abnormal, whether to continue.");
            builder.setPositiveButton((CharSequence)"continue", new DialogInterface.OnClickListener(){

                public void onClick(DialogInterface dialog, int which) {
                    handler.proceed();
                }
            });
            builder.setNegativeButton((CharSequence)"cancel", new DialogInterface.OnClickListener(){

                public void onClick(DialogInterface dialog, int which) {
                    handler.cancel();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            super.doUpdateVisitedHistory(view, url, isReload);
        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (EzvizWebViewActivity.this.mWebView.getTitle() != null) {
                EzvizWebViewActivity.this.mTitleBar.setTitle(EzvizWebViewActivity.this.mWebView.getTitle());
            }
            EzvizWebViewActivity.this.mProgressView.clearAnimation();
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            LogUtil.i(TAG, "onPageStarted " + url);
            super.onPageStarted(view, url, favicon);
            EzvizWebViewActivity.this.mTitleBar.setTitle("loading...");
            EzvizWebViewActivity.this.mProgressView.startAnimation(EzvizWebViewActivity.this.mRotateAnimation);
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            LogUtil.i(TAG, "shouldOverrideUrlLoading" + url);
            if (!this.checkUrlLoading(url)) {
                return super.shouldOverrideUrlLoading(view, url);
            }
            return true;
        }

        private String[] getOperateNameFromUrl(String url) {
            Pattern p = Pattern.compile("operate\\/(.*)\\?paramId=(.*)&");
            Matcher m = p.matcher(url);
            m.find();
            String[] result = new String[]{m.group(1), m.group(2)};
            return result;
        }

        private boolean checkUrlLoading(String url) {
            LogUtil.i(TAG, "checkUrlLoading=" + url);
            if (url.contains("ysopensdkbridge")) {
                String[] params = this.getOperateNameFromUrl(url);
                String operate_str = params[0];
                String param_id = params[1];
                LogUtil.i(TAG, "checkUrlLoading, operate code is:" + operate_str + ", paramId is:" + param_id);
                if (!TextUtils.isEmpty((CharSequence)operate_str) && !TextUtils.isEmpty((CharSequence)param_id)) {
                    EzvizWebViewActivity.this.getRequestParam(operate_str, param_id);
                }
                return true;
            }
            if (url.contains(EzvizWebViewActivity.OAUTH_SUCCESS) || url.contains(EzvizWebViewActivity.AUTHORIZE_SUCCESS)) {
                Uri uri = Uri.parse((String)url);
                String accessToken = Utils.getUrlValue(url, "access_token=", "&");
                String strExpire = Utils.getUrlValue(url, "expires_in=", "&");
                String areaDomain = Utils.getUrlValue(url, "areaDomain=", "&");
                String scope = Utils.getUrlValue(url, "scope=", "&");
                String state = Utils.getUrlValue(url, "state=", "&");
                String refresh_token = Utils.getUrlValue(url, "refresh_token=", "&");
                String open_id = Utils.getUrlValue(url, "open_id=", "&");
                String authdomain = Utils.getUrlValue(url, "authDomain=", "&");
                long intExpire = 0L;
                try {
                    intExpire = Long.valueOf(strExpire);
                }
                catch (Exception e) {
                    LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
                }
                if (accessToken != null) {
                    EZAccessTokenInternal ezAccessToken = new EZAccessTokenInternal();
                    ezAccessToken.setAccessToken(accessToken);
                    ezAccessToken.setExpire(intExpire);
                    ezAccessToken.setRefresh_token(refresh_token);
                    ezAccessToken.setScope(scope);
                    ezAccessToken.setState(state);
                    ezAccessToken.setOpen_id(open_id);
                    ezAccessToken.setAreaDomain(areaDomain);
                    ezAccessToken.setAreaAuthDomain(authdomain);
                    LocalInfo.getInstance().setEZAccessToken(ezAccessToken);
                    EzvizWebViewActivity.this.mBaseAPI.setAccessToken(accessToken);
                    LogUtil.d(TAG, "t:" + accessToken.substring(0, 5) + " expire:" + intExpire);
                    Intent intent = new Intent();
                    intent.setAction("com.videogo.action.OAUTH_SUCCESS_ACTION");
                    EzvizWebViewActivity.this.sendBroadcast(intent);
                    LogUtil.d(TAG, "sendBroadcast:com.videogo.action.OAUTH_SUCCESS_ACTION");
                    EzvizWebViewActivity.this.finish();
                    return true;
                }
            } else if (url.contains(EzvizWebViewActivity.NOTICE)) {
                int resultCode = 0;
                try {
                    resultCode = Integer.valueOf(Utils.getUrlValue(url, "resultCode=", "&"));
                }
                catch (NumberFormatException e) {
                    LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
                }
            }
            return false;
        }
    }

    private class MyDownloadListener
    implements DownloadListener {
        private MyDownloadListener() {
        }

        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            Uri uri = Uri.parse((String)url);
            Intent intent = new Intent("android.intent.action.VIEW", uri);
            EzvizWebViewActivity.this.startActivity(intent);
        }
    }

    private final class androidJSAdapter {
        private androidJSAdapter() {
        }

        @JavascriptInterface
        public void showSource(String html) {
            LogUtil.d("HTML", html);
        }
    }
}


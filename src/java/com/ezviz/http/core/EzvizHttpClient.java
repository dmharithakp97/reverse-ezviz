/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 *  android.text.TextUtils
 *  com.google.gson.Gson
 *  okhttp3.Call
 *  okhttp3.Callback
 *  okhttp3.OkHttpClient
 *  okhttp3.OkHttpClient$Builder
 *  okhttp3.Request
 *  okhttp3.Response
 */
package com.ezviz.http.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.ezviz.http.bean.base.BaseResp;
import com.ezviz.http.core.EzvizCallback;
import com.ezviz.http.exception.EzConfigWifiException;
import com.ezviz.sdk.configwifi.EZConfigWifiErrorEnum;
import com.ezviz.sdk.configwifi.common.LogUtil;
import com.google.gson.Gson;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.net.ssl.SSLHandshakeException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class EzvizHttpClient {
    private static final String TAG = "EzvizHttpClient";
    private OkHttpClient mCurrentHttpClient;
    private boolean mCheckHttpCert = true;
    private static final Object INIT_LOCK = new Object();
    private static final int CONNECT_TIMEOUT = 30000;
    private static final int RESPONSE_TIMEOUT = 30000;
    private Context mContext;
    public static final String LACK_OF_SYSTEMT_CERT = "lack_of_system_cert";
    private boolean mLackOfSystemCer;
    private SharedPreferences sp;

    public EzvizHttpClient(Context context) {
        this.mContext = context;
        this.sp = context.getSharedPreferences("VideoGo", 0);
        this.mLackOfSystemCer = this.sp.getBoolean(LACK_OF_SYSTEMT_CERT, false);
        this.mCurrentHttpClient = this.getClient();
    }

    private OkHttpClient getClient() {
        if (this.mCurrentHttpClient != null) {
            return this.mCurrentHttpClient;
        }
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30000L, TimeUnit.MILLISECONDS).readTimeout(30000L, TimeUnit.MILLISECONDS);
        return builder.build();
    }

    public Call newCall(Request request) {
        return this.mCurrentHttpClient.newCall(request);
    }

    public void ezEnqueue(Request request, final EzvizCallback ezvizCallback) {
        this.mCurrentHttpClient.newCall(request).enqueue(new Callback(){

            public void onFailure(Call call, IOException e) {
                ezvizCallback.onException(new EzConfigWifiException(EZConfigWifiErrorEnum.NETWORK_EXCEPTION.code, e.getMessage()));
                if (e instanceof SSLHandshakeException && e.getMessage() != null && e.getMessage().contains("java.security.cert.CertPathValidatorException")) {
                    EzvizHttpClient.this.mLackOfSystemCer = true;
                    EzvizHttpClient.this.mCurrentHttpClient = null;
                    EzvizHttpClient.this.mCurrentHttpClient = EzvizHttpClient.this.getClient();
                    SharedPreferences.Editor edit = EzvizHttpClient.this.sp.edit();
                    edit.putBoolean(EzvizHttpClient.LACK_OF_SYSTEMT_CERT, true);
                    edit.apply();
                }
            }

            public void onResponse(Call call, Response response) throws IOException {
                String responseStr = response.body().string();
                LogUtil.d(EzvizHttpClient.TAG, "onResponse <<<  " + responseStr);
                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    BaseResp baseResp = (BaseResp)gson.fromJson(responseStr, BaseResp.class);
                    if (baseResp != null && baseResp.meta != null) {
                        if (baseResp.meta.code == 200) {
                            ezvizCallback.onSuccess(responseStr);
                        } else {
                            ezvizCallback.onException(new EzConfigWifiException(baseResp.meta.code, baseResp.meta.message));
                        }
                    } else if (baseResp != null) {
                        int code = -1;
                        if (!TextUtils.isEmpty((CharSequence)baseResp.code)) {
                            code = Integer.parseInt(baseResp.code);
                        }
                        if (code == 200) {
                            ezvizCallback.onSuccess(responseStr);
                        } else {
                            ezvizCallback.onException(new EzConfigWifiException(code, baseResp.msg));
                        }
                    } else {
                        ezvizCallback.onException(new EzConfigWifiException(-1));
                    }
                } else {
                    ezvizCallback.onException(new EzConfigWifiException(response.code(), "server exception"));
                }
            }
        });
    }

    public OkHttpClient getHttpClient() {
        return this.mCurrentHttpClient;
    }

    private OkHttpClient.Builder getSystemCerHttpClient() {
        return new OkHttpClient.Builder();
    }

    private List<InputStream> getCertificateIsFromPemFile(String certName, InputStream certFileIs) {
        ArrayList<InputStream> certIsList = new ArrayList<InputStream>();
        try {
            byte[] buffer = new byte[certFileIs.available()];
            if (certFileIs.read(buffer) != 0) {
                String certFileStr = new String(buffer);
                LogUtil.d(TAG, "size of " + certName + " is " + certFileStr.length());
                Matcher matcher = Pattern.compile("(-----BEGIN CERTIFICATE-----[\\s|\\S]*?-----END CERTIFICATE-----)").matcher(certFileStr);
                while (matcher.find()) {
                    ByteArrayInputStream certFileChildIs = new ByteArrayInputStream(matcher.group().getBytes());
                    certIsList.add(certFileChildIs);
                }
                LogUtil.d(TAG, "load " + certName + ", find " + certIsList.size() + " certs");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return certIsList;
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.SuppressLint
 *  android.content.Context
 *  android.text.TextUtils
 *  okhttp3.FormBody$Builder
 *  okhttp3.MediaType
 *  okhttp3.MultipartBody
 *  okhttp3.MultipartBody$Builder
 *  okhttp3.MultipartBody$Part
 *  okhttp3.OkHttpClient
 *  okhttp3.OkHttpClient$Builder
 *  okhttp3.Request$Builder
 *  okhttp3.RequestBody
 *  okhttp3.Response
 *  org.json.JSONException
 *  org.json.JSONObject
 */
package com.videogo.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import com.videogo.constant.Config;
import com.videogo.errorlayer.ErrorInfo;
import com.videogo.errorlayer.ErrorLayer;
import com.videogo.exception.BaseException;
import com.videogo.openapi.BaseAPI;
import com.videogo.openapi.bean.BaseHeaderInfo;
import com.videogo.openapi.bean.BaseInfo;
import com.videogo.openapi.model.BaseResponse;
import com.videogo.restful.NameValuePair;
import com.videogo.util.LocalInfo;
import com.videogo.util.LogUtil;
import com.videogo.util.ReflectionUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.net.ssl.SSLHandshakeException;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

public class RestfulUtils {
    public static final String TAG = "RestfulUtils";
    private static final int CONNECT_TIMEOUT = 30000;
    private static final int RESPONSE_TIMEOUT = 30000;
    private static final Object INIT_LOCK = new Object();
    @SuppressLint(value={"StaticFieldLeak"})
    private static RestfulUtils mHttpUtils = null;
    private Context mContext;
    private boolean mCheckHttpCert = true;
    private boolean mLackOfSystemCer;
    private OkHttpClient mCurrentHttpClient;

    public static RestfulUtils getInstance() {
        return mHttpUtils;
    }

    private RestfulUtils(Context context) {
        this.mContext = context;
        this.mLackOfSystemCer = LocalInfo.getInstance().isLackOfSystemCert();
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

    public Object postData(List<NameValuePair> list, String url, BaseResponse resp) throws BaseException {
        try {
            return this.postData(list, url, resp, false);
        }
        catch (BaseException e) {
            if (!(e.getErrorCode() != 400902 || Config.ENABLE_SDK_TKTOKEN || TextUtils.isEmpty((CharSequence)LocalInfo.getInstance().getEZAccesstoken().getAccessTokenOrHttpToken()) || TextUtils.isEmpty((CharSequence)LocalInfo.getInstance().getEZAccesstoken().getRefresh_token()))) {
                boolean ret = false;
                try {
                    ret = BaseAPI.getInstance().refreshToken();
                }
                catch (BaseException e2) {
                    LogUtil.printErrStackTrace(TAG, e2);
                }
                if (ret) {
                    for (int i = 0; i < list.size(); ++i) {
                        if (!"accessToken".equalsIgnoreCase(list.get(i).getName())) continue;
                        list.remove(i);
                        list.add(new NameValuePair("accessToken", LocalInfo.getInstance().getEZAccesstoken().getAccessTokenOrHttpToken()));
                        break;
                    }
                    return this.postData(list, url, resp, false);
                }
            }
            throw e;
        }
    }

    public Object postDataDirectUrl(List<NameValuePair> list, String url, BaseResponse resp) throws BaseException {
        try {
            return this.postData(list, url, resp, true);
        }
        catch (BaseException e) {
            if (!(e.getErrorCode() != 400902 || Config.ENABLE_SDK_TKTOKEN || TextUtils.isEmpty((CharSequence)LocalInfo.getInstance().getEZAccesstoken().getAccessTokenOrHttpToken()) || TextUtils.isEmpty((CharSequence)LocalInfo.getInstance().getEZAccesstoken().getRefresh_token()))) {
                boolean ret = false;
                try {
                    ret = BaseAPI.getInstance().refreshToken();
                }
                catch (BaseException e2) {
                    LogUtil.printErrStackTrace(TAG, e2);
                }
                if (ret) {
                    for (int i = 0; i < list.size(); ++i) {
                        if (!"accessToken".equalsIgnoreCase(list.get(i).getName())) continue;
                        list.remove(i);
                        list.add(new NameValuePair("accessToken", LocalInfo.getInstance().getEZAccesstoken().getAccessTokenOrHttpToken()));
                        break;
                    }
                    return this.postData(list, url, resp, true);
                }
            }
            throw e;
        }
    }

    private Object postData(List<NameValuePair> list, String url, BaseResponse resp, boolean directUrl) throws BaseException {
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        for (NameValuePair nameValuePair : list) {
            paramMap.put(nameValuePair.getName(), nameValuePair.getValue());
        }
        return this.post(null, paramMap, url, resp, true, directUrl);
    }

    public static Object parse(String response, BaseResponse baseResponse) throws BaseException {
        block5: {
            ErrorInfo errorInfo;
            if (TextUtils.isEmpty((CharSequence)response) && (errorInfo = ErrorLayer.getErrorLayer(2, 400030)) != null) {
                LogUtil.d(TAG, "IO Error", new BaseException("IO Error", errorInfo.errorCode, errorInfo));
                throw new BaseException("IO Error", errorInfo.errorCode, errorInfo);
            }
            try {
                return baseResponse.parse(response);
            }
            catch (JSONException e) {
                LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
                ErrorInfo errorInfo2 = ErrorLayer.getErrorLayer(2, 400030);
                if (errorInfo2 != null) {
                    throw new BaseException("IO Error", errorInfo2.errorCode, errorInfo2);
                }
            }
            catch (IllegalAccessError e) {
                LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
                ErrorInfo errorInfo3 = ErrorLayer.getErrorLayer(2, 400030);
                if (errorInfo3 == null) break block5;
                throw new BaseException("IO Error", errorInfo3.errorCode, errorInfo3);
            }
        }
        return null;
    }

    public Object post(BaseInfo param, String url, BaseResponse resp) throws BaseException {
        return this.post(null, param, url, resp, false);
    }

    public Object post(BaseHeaderInfo baseHeaderInfo, BaseInfo param, String url, BaseResponse resp) throws BaseException {
        try {
            return this.post(baseHeaderInfo, param, url, resp, false);
        }
        catch (BaseException e) {
            if (!(e.getErrorCode() != 400902 || Config.ENABLE_SDK_TKTOKEN || TextUtils.isEmpty((CharSequence)LocalInfo.getInstance().getEZAccesstoken().getAccessTokenOrHttpToken()) || TextUtils.isEmpty((CharSequence)LocalInfo.getInstance().getEZAccesstoken().getRefresh_token()))) {
                boolean ret = false;
                try {
                    ret = BaseAPI.getInstance().refreshToken();
                }
                catch (BaseException e2) {
                    LogUtil.printErrStackTrace(TAG, e2);
                }
                if (ret) {
                    param.setAccessToken(LocalInfo.getInstance().getEZAccesstoken().getAccessTokenOrHttpToken());
                    return this.post(baseHeaderInfo, param, url, resp, false);
                }
            }
            throw e;
        }
    }

    public Object post(BaseHeaderInfo baseHeaderInfo, BaseInfo param, String url, BaseResponse resp, boolean directUrl) throws BaseException {
        HashMap<String, Object> headerMap = new HashMap<String, Object>();
        if (baseHeaderInfo != null) {
            for (ReflectionUtils.NameObjectParam objectParam : ReflectionUtils.convObjectToParams(baseHeaderInfo)) {
                headerMap.put(objectParam.name, objectParam.object);
            }
        }
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        for (ReflectionUtils.NameObjectParam objectParam : ReflectionUtils.convObjectToParams(param)) {
            paramMap.put(objectParam.name, objectParam.object);
        }
        return this.post(headerMap, paramMap, url, resp, true, directUrl);
    }

    public Object post(Map<String, Object> headerMap, Map<String, Object> paramMap, String url, BaseResponse resp, boolean authInterface, boolean directUrl) throws BaseException {
        return this.post(headerMap, paramMap, url, resp, authInterface, directUrl, true);
    }

    public Object post(Map<String, Object> headerMap, Map<String, Object> paramMap, String url, BaseResponse resp, boolean authInterface, boolean directUrl, boolean checkError) throws BaseException {
        block20: {
            OkHttpClient httpClient = this.getClient();
            String requestUrl = !directUrl ? (authInterface ? BaseAPI.getInstance().getServerUrl() + url : BaseAPI.getInstance().getOriginalServAddr() + url) : url;
            try {
                if (!this.mCheckHttpCert) {
                    LogUtil.e(TAG, "Ignore https certificate!");
                }
                LogUtil.d(TAG, "Request  >>>  " + requestUrl + "\n" + paramMap);
                Request.Builder requestBuilder = new Request.Builder().url(requestUrl);
                if (headerMap != null) {
                    for (String string : headerMap.keySet()) {
                        requestBuilder.header(string, String.valueOf(headerMap.get(string)));
                    }
                }
                FormBody.Builder formBodyBuilder = new FormBody.Builder();
                for (String key : paramMap.keySet()) {
                    formBodyBuilder.add(key, String.valueOf(paramMap.get(key)));
                }
                requestBuilder.post((RequestBody)formBodyBuilder.build());
                Response response = httpClient.newCall(requestBuilder.build()).execute();
                String content = "fail to get response from server!";
                if (response.body() != null) {
                    content = response.body().string();
                }
                String logContent = content;
                if (requestUrl.contains("api/device/detail") || content.contains("api/device/sdk/detail")) {
                    try {
                        int supportExShortStartIndex = logContent.indexOf("supportExtShort");
                        int supportExShortEndIndex = logContent.indexOf("\"", supportExShortStartIndex + 18);
                        String part1 = logContent.substring(0, supportExShortStartIndex + 18);
                        String part2 = logContent.substring(supportExShortEndIndex);
                        String supportExtShortValue = logContent.substring(supportExShortStartIndex - 1, supportExShortEndIndex + 1);
                        LogUtil.d(TAG, supportExtShortValue);
                        logContent = part1.concat("\u8bf7\u67e5\u770b\u4e0a\u65b9supportExtShort\u65e5\u5fd7").concat(part2);
                        int supportExStartIndex = logContent.indexOf("supportExt");
                        int supportExEndIndex = logContent.indexOf("}", supportExStartIndex + 13);
                        String part3 = logContent.substring(0, supportExStartIndex + 13);
                        String part4 = logContent.substring(supportExEndIndex + 1);
                        String supportExtValue = logContent.substring(supportExStartIndex - 1, supportExEndIndex + 1);
                        LogUtil.d(TAG, supportExtValue);
                        logContent = part3.concat("\u8bf7\u67e5\u770b\u4e0a\u65b9supportExt\u65e5\u5fd7").concat(part4);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                LogUtil.d(TAG, "Response <<<  " + requestUrl + "\n" + logContent);
                if (!response.isSuccessful()) {
                    if (checkError) {
                        ErrorInfo errorInfo = ErrorLayer.getErrorLayer(1, 150000);
                        int errCode = -1;
                        if (errorInfo != null) {
                            errCode = errorInfo.errorCode;
                        }
                        throw new BaseException("server error!", errCode, errorInfo);
                    }
                    return null;
                }
                return RestfulUtils.parse(content, resp);
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            catch (IllegalStateException e) {
                e.printStackTrace();
            }
            catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            catch (SecurityException e) {
                e.printStackTrace();
            }
            catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
            catch (RuntimeException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
                if (!checkError) break block20;
                if (e instanceof SSLHandshakeException && e.getMessage() != null && e.getMessage().contains("java.security.cert.CertPathValidatorException")) {
                    this.mLackOfSystemCer = true;
                    this.mCurrentHttpClient = null;
                    this.mCurrentHttpClient = this.getClient();
                    LocalInfo.getInstance().setLackOfSystemCert(true);
                }
                ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400031);
                if (errorInfo == null) break block20;
                throw new BaseException("IO Error", errorInfo.errorCode, errorInfo);
            }
        }
        return null;
    }

    public Object post(String paramString, String url, BaseResponse resp, boolean authInterface, boolean directUrl, boolean checkError) throws BaseException {
        block14: {
            OkHttpClient httpClient = this.getClient();
            String requestUrl = !directUrl ? (authInterface ? BaseAPI.getInstance().getServerUrl() + url : BaseAPI.getInstance().getOriginalServAddr() + url) : url;
            try {
                if (!this.mCheckHttpCert) {
                    LogUtil.e(TAG, "Ignore https certificate!");
                }
                LogUtil.d(TAG, "Request  >>>  " + requestUrl + "\n" + paramString);
                RequestBody requestBody = RequestBody.create((MediaType)MediaType.get((String)"application/json; charset=utf-8"), (String)paramString);
                Request.Builder requestBuilder = new Request.Builder().url(requestUrl).post(requestBody);
                Response response = httpClient.newCall(requestBuilder.build()).execute();
                String content = "fail to get response from server!";
                if (response.body() != null) {
                    content = response.body().string();
                }
                LogUtil.d(TAG, "Response <<<  " + requestUrl + "\n" + content);
                if (!response.isSuccessful()) {
                    if (checkError) {
                        ErrorInfo errorInfo = ErrorLayer.getErrorLayer(1, 150000);
                        int errCode = -1;
                        if (errorInfo != null) {
                            errCode = errorInfo.errorCode;
                        }
                        throw new BaseException("server error!", errCode, errorInfo);
                    }
                    return null;
                }
                return RestfulUtils.parse(content, resp);
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            catch (IllegalStateException e) {
                e.printStackTrace();
            }
            catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            catch (SecurityException e) {
                e.printStackTrace();
            }
            catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
            catch (RuntimeException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
                if (!checkError) break block14;
                if (e instanceof SSLHandshakeException && e.getMessage() != null && e.getMessage().contains("java.security.cert.CertPathValidatorException")) {
                    this.mLackOfSystemCer = true;
                    this.mCurrentHttpClient = null;
                    this.mCurrentHttpClient = this.getClient();
                    LocalInfo.getInstance().setLackOfSystemCert(true);
                }
                ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400031);
                if (errorInfo == null) break block14;
                throw new BaseException("IO Error", errorInfo.errorCode, errorInfo);
            }
        }
        return null;
    }

    public boolean post(byte[] byteArray, String url, Map<String, Object> paramMap, String fileName, boolean authInterface, boolean directUrl, boolean checkError) throws BaseException {
        block15: {
            OkHttpClient httpClient = this.getClient();
            String requestUrl = !directUrl ? (authInterface ? BaseAPI.getInstance().getServerUrl() + url : BaseAPI.getInstance().getOriginalServAddr() + url) : url;
            try {
                if (!this.mCheckHttpCert) {
                    LogUtil.e(TAG, "Ignore https certificate!");
                }
                LogUtil.d(TAG, "Request  >>>  " + requestUrl);
                MultipartBody.Builder multipartBodyBuild = new MultipartBody.Builder().setType(MultipartBody.FORM);
                for (String key : paramMap.keySet()) {
                    LogUtil.d(TAG, key + " :  " + paramMap.get(key));
                    multipartBodyBuild.addFormDataPart(key, String.valueOf(paramMap.get(key)));
                }
                RequestBody requestBody = RequestBody.create((MediaType)MediaType.parse((String)"image/jpeg"), (byte[])byteArray);
                MultipartBody.Part filePart = MultipartBody.Part.createFormData((String)"file", (String)fileName, (RequestBody)requestBody);
                multipartBodyBuild.addPart(filePart);
                Request.Builder requestBuilder = new Request.Builder().url(requestUrl).post((RequestBody)multipartBodyBuild.build());
                Response response = httpClient.newCall(requestBuilder.build()).execute();
                String content = "fail to get response from server!";
                if (response.body() != null) {
                    content = response.body().string();
                }
                LogUtil.d(TAG, "Response <<<  " + requestUrl + "\n" + content);
                if (!response.isSuccessful()) {
                    if (checkError) {
                        ErrorInfo errorInfo = ErrorLayer.getErrorLayer(1, 150000);
                        int errCode = -1;
                        if (errorInfo != null) {
                            errCode = errorInfo.errorCode;
                        }
                        throw new BaseException("server error!", errCode, errorInfo);
                    }
                    return false;
                }
                return true;
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            catch (IllegalStateException e) {
                e.printStackTrace();
            }
            catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            catch (SecurityException e) {
                e.printStackTrace();
            }
            catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
            catch (RuntimeException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
                if (!checkError) break block15;
                if (e instanceof SSLHandshakeException && e.getMessage() != null && e.getMessage().contains("java.security.cert.CertPathValidatorException")) {
                    this.mLackOfSystemCer = true;
                    this.mCurrentHttpClient = null;
                    this.mCurrentHttpClient = this.getClient();
                    LocalInfo.getInstance().setLackOfSystemCert(true);
                }
                ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400031);
                if (errorInfo == null) break block15;
                throw new BaseException("IO Error", errorInfo.errorCode, errorInfo);
            }
        }
        return false;
    }

    public Object get(String url, BaseInfo header, BaseResponse resp, boolean checkError) throws BaseException {
        block18: {
            OkHttpClient httpClient = this.getClient();
            String requestUrl = BaseAPI.getInstance().getServerUrl() + url;
            try {
                if (!this.mCheckHttpCert) {
                    LogUtil.e(TAG, "Ignore https certificate!");
                }
                LogUtil.d(TAG, "Request  >>>  " + requestUrl);
                if (header != null) {
                    HashMap<String, Object> headerMap = new HashMap<String, Object>();
                    for (ReflectionUtils.NameObjectParam objectParam : ReflectionUtils.convObjectToParams(header)) {
                        headerMap.put(objectParam.name, objectParam.object);
                    }
                    LogUtil.d(TAG, "header  >>>  " + headerMap);
                }
                Request.Builder requestBuilder = new Request.Builder().url(requestUrl).get();
                for (ReflectionUtils.NameObjectParam objectParam : ReflectionUtils.convObjectToParams(header)) {
                    requestBuilder.header(objectParam.name, String.valueOf(objectParam.object));
                }
                Response response = httpClient.newCall(requestBuilder.build()).execute();
                String content = "fail to get response from server!";
                if (response.body() != null) {
                    content = response.body().string();
                }
                LogUtil.d(TAG, "Response <<<  " + requestUrl + "\n" + content);
                try {
                    JSONObject jSONObject = new JSONObject(content);
                }
                catch (JSONException ex) {
                    ex.printStackTrace();
                    if (checkError) {
                        ErrorInfo errorInfo = ErrorLayer.getErrorLayer(1, 150000);
                        int errCode = -1;
                        if (errorInfo != null) {
                            errCode = errorInfo.errorCode;
                        }
                        throw new BaseException("server error!", errCode, errorInfo);
                    }
                    return null;
                }
                return RestfulUtils.parse(content, resp);
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            catch (IllegalStateException e) {
                e.printStackTrace();
            }
            catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            catch (SecurityException e) {
                e.printStackTrace();
            }
            catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
            catch (RuntimeException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
                if (!checkError) break block18;
                if (e instanceof SSLHandshakeException && e.getMessage() != null && e.getMessage().contains("java.security.cert.CertPathValidatorException")) {
                    this.mLackOfSystemCer = true;
                    this.mCurrentHttpClient = null;
                    this.mCurrentHttpClient = this.getClient();
                    LocalInfo.getInstance().setLackOfSystemCert(true);
                }
                ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400031);
                if (errorInfo == null) break block18;
                throw new BaseException("IO Error", errorInfo.errorCode, errorInfo);
            }
        }
        return null;
    }

    public Object get(String url, BaseResponse resp, boolean checkError) throws BaseException {
        block15: {
            OkHttpClient httpClient = this.getClient();
            String requestUrl = BaseAPI.getInstance().getServerUrl() + url;
            try {
                if (!this.mCheckHttpCert) {
                    LogUtil.e(TAG, "Ignore https certificate!");
                }
                Request.Builder requestBuilder = new Request.Builder().url(requestUrl).get();
                Response response = httpClient.newCall(requestBuilder.build()).execute();
                String content = "fail to get response from server!";
                if (response.body() != null) {
                    content = response.body().string();
                }
                LogUtil.d(TAG, "Response <<<  " + requestUrl + "\n" + content);
                try {
                    JSONObject jSONObject = new JSONObject(content);
                }
                catch (JSONException ex) {
                    ex.printStackTrace();
                    if (checkError) {
                        ErrorInfo errorInfo = ErrorLayer.getErrorLayer(1, 150000);
                        int errCode = -1;
                        if (errorInfo != null) {
                            errCode = errorInfo.errorCode;
                        }
                        throw new BaseException("server error!", errCode, errorInfo);
                    }
                    return null;
                }
                return RestfulUtils.parse(content, resp);
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            catch (IllegalStateException e) {
                e.printStackTrace();
            }
            catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            catch (SecurityException e) {
                e.printStackTrace();
            }
            catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
            catch (RuntimeException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
                if (!checkError) break block15;
                if (e instanceof SSLHandshakeException && e.getMessage() != null && e.getMessage().contains("java.security.cert.CertPathValidatorException")) {
                    this.mLackOfSystemCer = true;
                    this.mCurrentHttpClient = null;
                    this.mCurrentHttpClient = this.getClient();
                    LocalInfo.getInstance().setLackOfSystemCert(true);
                }
                ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400031);
                if (errorInfo == null) break block15;
                throw new BaseException("IO Error", errorInfo.errorCode, errorInfo);
            }
        }
        return null;
    }

    public Object postWithoutCheckError(BaseInfo param, String url, BaseResponse resp) throws BaseException {
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        for (ReflectionUtils.NameObjectParam objectParam : ReflectionUtils.convObjectToParams(param)) {
            paramMap.put(objectParam.name, objectParam.object);
        }
        return this.post(null, paramMap, url, resp, param.getAccessToken() != null, false, false);
    }

    public static synchronized void init(Context context) {
        if (mHttpUtils == null) {
            mHttpUtils = new RestfulUtils(context);
        }
    }
}


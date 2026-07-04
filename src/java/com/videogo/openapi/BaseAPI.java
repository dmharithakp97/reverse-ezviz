/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.SuppressLint
 *  android.app.Application
 *  android.content.Context
 *  android.content.Intent
 *  android.text.TextUtils
 *  org.json.JSONArray
 *  org.json.JSONException
 *  org.json.JSONObject
 */
package com.videogo.openapi;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.videogo.constant.Config;
import com.videogo.errorlayer.ErrorInfo;
import com.videogo.errorlayer.ErrorLayer;
import com.videogo.exception.BaseException;
import com.videogo.exception.EZOpenSDKErrorInfo;
import com.videogo.main.EzvizWebViewActivity;
import com.videogo.openapi.annotation.HttpParam;
import com.videogo.openapi.bean.BaseInfo;
import com.videogo.openapi.bean.EZAccessToken;
import com.videogo.openapi.bean.EZAccessTokenInternal;
import com.videogo.openapi.bean.EZAreaInfo;
import com.videogo.openapi.bean.EZProbeDeviceInfo;
import com.videogo.openapi.bean.EZUserInfo;
import com.videogo.openapi.model.ApiResponse;
import com.videogo.openapi.model.BaseResponse;
import com.videogo.openapi.model.req.WebLoginReq;
import com.videogo.openapi.model.resp.LogoutResp;
import com.videogo.util.HttpUtils;
import com.videogo.util.LocalInfo;
import com.videogo.util.LocalValidate;
import com.videogo.util.LogUtil;
import com.videogo.util.MD5Util;
import com.videogo.util.MathUtils;
import com.videogo.util.ReflectionUtils;
import com.videogo.util.RestfulUtils;
import com.videogo.util.Utils;
import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint(value={"MissingPermission"})
public abstract class BaseAPI {
    public static final String TAG = "BaseAPI";
    public static final String STR_NATIVE_PARAM_ERROR = "\u53c2\u6570\u9519\u8bef!";
    private static BaseAPI mBaseAPI = null;
    private boolean isUsingGlobalSDK;
    public static Application mApplication = null;
    private LocalInfo mLocalInfo = null;
    private String mNetType;
    private String mWebUrl = "";
    private String mAppKey = "";
    private String mThridToken = "";
    protected RestfulUtils mRestfulUtils = null;
    protected LocalValidate mLocalValidate = null;
    private int mAreaId = -1;
    public static final String API_RESULT = "result";
    public static final String API_CODE = "code";
    public static final String API_MSG = "description";
    public static final String API_DATA = "data";
    private static String exterVer = "";
    private boolean isInit = false;
    private Object mWaitObject = new Object();
    private int refreshTokenCount = 0;

    public static String getExterVer() {
        return exterVer;
    }

    public BaseAPI(Application application, String appKey, boolean isUsingGlobal) {
        mApplication = application;
        this.mAppKey = appKey;
        this.isUsingGlobalSDK = isUsingGlobal;
        HttpUtils.init(application);
        this.mLocalInfo = LocalInfo.getInstance();
        this.mNetType = Utils.getNetTypeName((Context)application);
        if (this.mNetType == null) {
            this.mNetType = "UNKNOWN";
        }
        RestfulUtils.init((Context)application);
        this.mRestfulUtils = RestfulUtils.getInstance();
        this.mLocalValidate = new LocalValidate();
        mBaseAPI = this;
    }

    public void setUserCode(String userCode) {
        this.mLocalInfo.setUserCode(userCode);
    }

    public String getUserCode() {
        return this.mLocalInfo.getUserCode();
    }

    public static BaseAPI getInstance() {
        return mBaseAPI;
    }

    protected void releaseSDK() {
        mBaseAPI = null;
    }

    public String getAppKey() {
        return this.mAppKey;
    }

    public void setAppKey(String appKey) {
        this.mAppKey = appKey;
    }

    public String getNetType() {
        return this.mNetType;
    }

    public void setNetType(String netType) {
        this.mNetType = netType;
    }

    public void setThridToken(String thridToken) {
        this.mThridToken = thridToken;
    }

    public String getThridToken() {
        return this.mThridToken;
    }

    protected void initParams() {
        new Thread(new Runnable(){

            @Override
            public void run() {
                if (!Config.ENABLE_SDK_TKTOKEN) {
                    BaseAPI.this.checkTokenExpire();
                }
                new Thread(new Runnable(){

                    @Override
                    public void run() {
                        BaseAPI.this.initErrorInfoList();
                    }
                }).start();
            }
        }).start();
    }

    public abstract void initErrorInfoList();

    private void checkTokenExpire() {
        long b = LocalInfo.getInstance().getEZAccesstoken().getExpire() - System.currentTimeMillis();
        LogUtil.d(TAG, "Expire = " + LocalInfo.getInstance().getEZAccesstoken().getExpire());
        LogUtil.d(TAG, "currentTimeMillis = " + System.currentTimeMillis());
        LogUtil.d(TAG, "currentTimeMillis - Expire = " + b);
        if (!TextUtils.isEmpty((CharSequence)LocalInfo.getInstance().getEZAccesstoken().getAccessTokenOrHttpToken()) && !TextUtils.isEmpty((CharSequence)LocalInfo.getInstance().getEZAccesstoken().getRefresh_token())) {
            if (b > 0L && b <= 86400000L) {
                new Thread(new Runnable(){

                    @Override
                    public void run() {
                        try {
                            BaseAPI.this.refreshToken();
                        }
                        catch (BaseException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            } else if (b <= 0L) {
                try {
                    this.refreshToken();
                }
                catch (BaseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public final void setAccessToken(String accessToken) {
        LogUtil.d(TAG, "Enter setAccessToken: " + accessToken);
        String token = LocalInfo.getInstance().getEZAccesstoken().getAccessToken();
        if (TextUtils.isEmpty((CharSequence)accessToken)) {
            LogUtil.e(TAG, "accessToken is null, logout");
            this.mLocalInfo.setEZAccessToken(null);
            this.clearCacheData();
            return;
        }
        this.mLocalInfo.setAccessToken(accessToken);
        if (!this.isInit) {
            this.isInit = true;
            this.initParams();
        }
        if (!TextUtils.equals((CharSequence)token, (CharSequence)accessToken)) {
            LogUtil.e(TAG, "accessToken is switch");
            this.clearCacheData();
            this.initParams();
        } else {
            this.clearStreamTokens();
        }
        LogUtil.d(TAG, "Exit setAccessToken: ");
    }

    public final void setHttpToken(String httpToken) {
        LogUtil.d(TAG, "Enter setHttpToken: " + httpToken);
        String token = LocalInfo.getInstance().getEZAccesstoken().getHttpToken();
        if (TextUtils.isEmpty((CharSequence)httpToken)) {
            LogUtil.e(TAG, "httpToken is null, logout");
            this.mLocalInfo.setEZAccessToken(null);
            this.clearCacheData();
            return;
        }
        this.mLocalInfo.setHttpToken(httpToken);
        if (!this.isInit) {
            this.isInit = true;
            this.initParams();
        }
        if (!TextUtils.equals((CharSequence)token, (CharSequence)httpToken)) {
            LogUtil.e(TAG, "httpTokens is switch");
            this.clearCacheData();
            this.initParams();
        } else {
            this.clearStreamTokens();
        }
        LogUtil.d(TAG, "Exit setHttpToken: ");
    }

    public void setDeviceToken(String deviceSerial, String deviceToken) {
        this.mLocalInfo.setDeviceToken(deviceSerial, deviceToken);
    }

    public void setDeviceToken(String deviceSerial, int cameraNo, String deviceGlobalToken, String deviceVideoToken) {
        this.mLocalInfo.setDeviceToken(deviceSerial, cameraNo, deviceGlobalToken, deviceVideoToken);
    }

    public EZAccessToken getEZAccessToken() {
        EZAccessToken ezAccessToken = new EZAccessToken();
        if (this.mLocalInfo == null || TextUtils.isEmpty((CharSequence)this.mLocalInfo.getEZAccesstoken().getAccessToken())) {
            return null;
        }
        ezAccessToken.setAccessToken(this.mLocalInfo.getEZAccesstoken().getAccessToken());
        ezAccessToken.setExpire(this.mLocalInfo.getEZAccesstoken().getExpire());
        return ezAccessToken;
    }

    public void setServerUrl(String apiUrl, String webUrl) {
        this.mLocalInfo.setServAddr(apiUrl);
        this.mWebUrl = webUrl;
        LocalInfo.getInstance().setAuthServAddr(this.mWebUrl);
    }

    public void preSetToken() {
        EZAccessTokenInternal tokenObject = LocalInfo.getInstance().getEZAccesstoken();
        if (tokenObject != null) {
            if (Config.ENABLE_SDK_TKTOKEN) {
                if (tokenObject.getHttpToken() != null) {
                    LogUtil.i(TAG, "httpToken is not null");
                    this.setHttpToken(tokenObject.getHttpToken());
                }
            } else if (tokenObject.getAccessToken() != null) {
                LogUtil.i(TAG, "accessToken is not null");
                this.setAccessToken(tokenObject.getAccessToken());
            }
        }
    }

    public void setAreaDomain(String areaDomain) {
        this.mLocalInfo.getEZAccesstoken().setAreaDomain(areaDomain);
    }

    public String getServerUrl() {
        return this.mLocalInfo.getServAddr();
    }

    public String getOriginalServAddr() {
        return this.mLocalInfo.getOriginalServAddr();
    }

    public String getOpenWebUrl() {
        return this.mWebUrl;
    }

    public void gotoLoginPage(int flag) {
        this.gotoLoginPage(true, this.mAreaId, flag);
    }

    public void gotoLoginPage(boolean logout, int areaId, int flag) {
        LogUtil.i(TAG, "Enter gotoLoginPage: ");
        if (Config.ENABLE_SDK_TKTOKEN) {
            return;
        }
        this.mAreaId = areaId;
        if (logout) {
            this.setAccessToken("");
            new Thread(){

                @Override
                public void run() {
                    BaseAPI.this.logout();
                }
            }.start();
        }
        Intent intent = new Intent((Context)mApplication, EzvizWebViewActivity.class);
        intent.setFlags(flag < 0 ? 0x10000000 : flag);
        intent.putExtra("Param", areaId);
        intent.putExtra("com.videogo.EXTRA_WEBVIEW_ACTION", 0);
        mApplication.startActivity(intent);
    }

    public void logout() {
        LogUtil.i(TAG, "Enter logout: ");
        try {
            this.logoutAccount();
        }
        catch (BaseException baseException) {
            // empty catch block
        }
        if (Config.ENABLE_SDK_TKTOKEN) {
            this.setHttpToken("");
        } else {
            this.setAccessToken("");
        }
    }

    protected void clearCacheData() {
    }

    protected void clearStreamTokens() {
    }

    public boolean resultForAPICall(Object object) {
        if (object == null) {
            return false;
        }
        Boolean ret = (Boolean)object;
        if (ret != null) {
            return ret;
        }
        return false;
    }

    public static boolean parserCode(String reponse) throws BaseException, JSONException {
        String RESULT = API_RESULT;
        int SUSCCEED = 200;
        String CODE = API_CODE;
        String MSG = API_MSG;
        String MSG2 = "msg";
        JSONObject jsonObject = new JSONObject(reponse);
        JSONObject result = jsonObject.getJSONObject(API_RESULT);
        int resultCode = result.optInt(API_CODE, 400030);
        String resultDesc = result.optString(API_MSG, null);
        if (resultDesc == null) {
            resultDesc = result.optString("msg", "Resp Error:" + resultCode);
        }
        if (resultCode == 200) {
            return true;
        }
        if (resultCode == 400030) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400030);
            throw new BaseException("IO Error", errorInfo.errorCode, errorInfo);
        }
        ErrorInfo errorInfo = ErrorLayer.getErrorLayer(1, resultCode);
        if (errorInfo.description == null || errorInfo.description.length() == 0) {
            errorInfo.description = resultDesc;
        }
        throw new BaseException(resultDesc, errorInfo.errorCode, errorInfo);
    }

    public boolean logoutAccount() throws BaseException {
        Object result = this.mRestfulUtils.post(new BaseInfo(), "/api/user/logout", new LogoutResp());
        return this.resultForAPICall(result);
    }

    public byte[] decryptData(byte[] inputData, String verifyCode, int cryptType) {
        LogUtil.i(TAG, "Enter decryptData checkSum = " + (TextUtils.isEmpty((CharSequence)verifyCode) ? "NULL" : verifyCode) + " crypt = " + cryptType);
        byte[] inputTrueData = null;
        if (cryptType == 2) {
            String head = new String(inputData, 0, 6);
            String head2 = new String(inputData, 0, 12);
            if (head.equals("hikpic")) {
                byte[] versionByte = Arrays.copyOfRange(inputData, 6, 8);
                short version = MathUtils.byteToShort(versionByte);
                byte[] enctypte = Arrays.copyOfRange(inputData, 8, 9);
                int picLenth = MathUtils.getLength(Arrays.copyOfRange(inputData, 9, 13));
                inputTrueData = Arrays.copyOfRange(inputData, 13, picLenth + 13);
            } else if (head2.equals("hiklittlepic")) {
                int picLenth = MathUtils.getLength(Arrays.copyOfRange(inputData, 14, 18));
                inputTrueData = Arrays.copyOfRange(inputData, 18, picLenth + 18);
            } else {
                inputTrueData = inputData;
            }
        } else if (cryptType == 1) {
            inputTrueData = inputData;
        }
        byte[] outputData = null;
        if (inputTrueData == null || inputTrueData.length <= 48) {
            return null;
        }
        if (verifyCode != null) {
            try {
                byte[] secretKey;
                if (cryptType == 1) {
                    String filePwd = new String(inputTrueData, 16, 32);
                    String md5Pwd = MD5Util.getMD5String(MD5Util.getMD5String(verifyCode));
                    if (!filePwd.equals(md5Pwd)) {
                        return null;
                    }
                    secretKey = Arrays.copyOf(verifyCode.getBytes(), 16);
                } else {
                    char[] passwordChars = verifyCode.toCharArray();
                    secretKey = new byte[16];
                    for (int i = 0; i < passwordChars.length / 2; ++i) {
                        if (i >= 16) continue;
                        secretKey[i] = (byte)((MathUtils.cryptHexToInt(passwordChars[2 * i]) << 4) + MathUtils.cryptHexToInt(passwordChars[2 * i + 1]));
                    }
                }
                byte[] pureContent = Arrays.copyOfRange(inputTrueData, 48, inputTrueData.length);
                SecretKeySpec key = new SecretKeySpec(secretKey, "AES");
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                byte[] ivByte = new byte[]{48, 49, 50, 51, 52, 53, 54, 55, 0, 0, 0, 0, 0, 0, 0, 0};
                IvParameterSpec iv = new IvParameterSpec(ivByte);
                cipher.init(2, (Key)key, iv);
                outputData = cipher.doFinal(pureContent);
            }
            catch (Exception e) {
                LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
            }
        }
        return outputData;
    }

    public EZUserInfo getEZUserInfo() throws BaseException {
        LogUtil.i(TAG, "Enter getEZUserInfo");
        String urlString = "/api/user/getUserInfo";
        BaseInfo baseInfo = new BaseInfo(){};
        baseInfo.fixHttpToken();
        EZUserInfo userInfo = (EZUserInfo)this.mRestfulUtils.post(baseInfo, urlString, new ApiResponse(){

            @Override
            public Object parse(String reponse) throws BaseException, JSONException {
                boolean isOk = this.parseCode(reponse);
                if (!isOk) {
                    return null;
                }
                JSONObject rootObj = new JSONObject(reponse);
                JSONObject resultObj = rootObj.getJSONObject(BaseAPI.API_RESULT);
                JSONObject dataObj = resultObj.optJSONObject(BaseAPI.API_DATA);
                if (dataObj != null) {
                    EZUserInfo user = new EZUserInfo();
                    ReflectionUtils.convJSONToObject(dataObj, user);
                    return user;
                }
                return null;
            }
        });
        LogUtil.i(TAG, "getEZUserInfo:" + userInfo);
        return userInfo;
    }

    public EZProbeDeviceInfo probeDeviceInfo(final String deviceSerial) throws BaseException {
        LogUtil.i(TAG, "Enter probeDeviceInfo");
        int nRet = this.mLocalValidate.localValidatDeviceSerial(deviceSerial);
        if (nRet != 0) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, nRet);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        String urlString = "/api/device/searchDeviceInfo";
        BaseInfo baseInfo = new BaseInfo(){
            @HttpParam(name="deviceSerial")
            private String mDeviceSerial;
            {
                this.mDeviceSerial = deviceSerial;
            }
        };
        baseInfo.fixDeviceToken(deviceSerial);
        Object result = this.mRestfulUtils.post(baseInfo, urlString, new ApiResponse(){

            @Override
            public Object parse(String response) throws BaseException, JSONException {
                boolean isOk = this.parseCode(response);
                if (isOk) {
                    JSONObject rootObj = new JSONObject(response);
                    JSONObject resultObj = rootObj.getJSONObject(BaseAPI.API_RESULT);
                    JSONObject dataObj = resultObj.optJSONObject(BaseAPI.API_DATA);
                    if (dataObj != null) {
                        EZProbeDeviceInfo info = new EZProbeDeviceInfo();
                        ReflectionUtils.convJSONToObject(dataObj, info);
                        return info;
                    }
                    return null;
                }
                return null;
            }
        });
        if (result != null) {
            LogUtil.i(TAG, ((EZProbeDeviceInfo)result).toString());
            return (EZProbeDeviceInfo)result;
        }
        return null;
    }

    public void openChangePasswordPage() {
        LogUtil.i(TAG, "Enter openChangePasswordPage");
        if (Config.ENABLE_SDK_TKTOKEN) {
            return;
        }
        Intent intent = new Intent((Context)mApplication, EzvizWebViewActivity.class);
        intent.setFlags(0x10000000);
        intent.putExtra("com.videogo.EXTRA_WEBVIEW_ACTION", 4);
        mApplication.startActivity(intent);
    }

    public List<EZAreaInfo> getAreaList() throws BaseException {
        Object obj = this.mRestfulUtils.post(new BaseInfo(false){}, "/api/area/list", new ApiResponse(){

            @Override
            public Object parse(String response) throws BaseException, JSONException {
                boolean isOk = this.parseCode(response);
                if (isOk) {
                    JSONObject rootObj = new JSONObject(response);
                    JSONObject resultObj = rootObj.getJSONObject(BaseAPI.API_RESULT);
                    JSONArray arrayObj = resultObj.optJSONArray(BaseAPI.API_DATA);
                    if (arrayObj != null && arrayObj.length() > 0) {
                        ArrayList<EZAreaInfo> areaInfoList = new ArrayList<EZAreaInfo>();
                        int len = arrayObj.length();
                        for (int i = 0; i < len; ++i) {
                            EZAreaInfo areaInfo = new EZAreaInfo();
                            JSONObject data = arrayObj.getJSONObject(i);
                            ReflectionUtils.convJSONToObject(data, areaInfo);
                            areaInfoList.add(areaInfo);
                        }
                        return areaInfoList;
                    }
                    return null;
                }
                return null;
            }
        });
        if (obj != null) {
            if ((List)obj != null && ((List)obj).size() > 0) {
                for (int i = 0; i < ((List)obj).size(); ++i) {
                }
            }
            return (List)obj;
        }
        return null;
    }

    public String getTerminalId() {
        String terminalId = null;
        if (!TextUtils.isEmpty((CharSequence)LocalInfo.getInstance().getHardwareCode())) {
            terminalId = MD5Util.md5Crypto(LocalInfo.getInstance().getHardwareCode());
        }
        if (!TextUtils.isEmpty(terminalId)) {
            terminalId = MD5Util.md5Crypto(terminalId);
        }
        if (!TextUtils.isEmpty((CharSequence)terminalId)) {
            LogUtil.d(TAG, "getTerminalId: " + terminalId);
        }
        return terminalId;
    }

    public List<EZOpenSDKErrorInfo> getErrorList(final String timestamp) throws BaseException {
        LogUtil.i(TAG, "Enter getErrorList");
        Object object = this.mRestfulUtils.post(new BaseInfo(){
            @HttpParam(name="time")
            private String time;
            {
                this.time = timestamp;
            }
        }, "/api/sdk/error/list", new ApiResponse(){

            @Override
            public Object parse(String response) throws BaseException, JSONException {
                boolean isOk = this.parseCodeHttp(response);
                if (isOk) {
                    JSONObject rootObject = new JSONObject(response);
                    JSONArray resultObject = rootObject.optJSONArray(BaseAPI.API_DATA);
                    ArrayList<EZOpenSDKErrorInfo> errorInfos = new ArrayList<EZOpenSDKErrorInfo>();
                    if (resultObject != null && resultObject.length() > 0) {
                        int count = resultObject.length();
                        for (int y = 0; y < count; ++y) {
                            EZOpenSDKErrorInfo errorInfo = new EZOpenSDKErrorInfo();
                            JSONObject jsonObject = resultObject.optJSONObject(y);
                            ReflectionUtils.convJSONToObject(jsonObject, errorInfo);
                            errorInfos.add(errorInfo);
                        }
                    }
                    return errorInfos;
                }
                return null;
            }
        });
        if (object != null) {
            return (List)object;
        }
        return null;
    }

    public EZOpenSDKErrorInfo getErrorInfo(final String detailcode) throws BaseException {
        LogUtil.i(TAG, "Enter getErrorInfo");
        Object object = this.mRestfulUtils.postWithoutCheckError(new BaseInfo(){
            @HttpParam(name="detailCode")
            private String detailCode;
            {
                this.detailCode = detailcode;
            }
        }, "/api/sdk/error/report", new ApiResponse(){

            @Override
            public Object parse(String response) throws BaseException, JSONException {
                boolean isOk = this.paserCodeHttpNoReport(response);
                if (isOk) {
                    JSONObject rootObject = new JSONObject(response);
                    EZOpenSDKErrorInfo errorInfo = null;
                    JSONObject jsonObject = rootObject.optJSONObject(BaseAPI.API_DATA);
                    if (jsonObject != null) {
                        errorInfo = new EZOpenSDKErrorInfo();
                        ReflectionUtils.convJSONToObject(jsonObject, errorInfo);
                    }
                    return errorInfo;
                }
                return null;
            }
        });
        if (object != null) {
            return (EZOpenSDKErrorInfo)object;
        }
        return null;
    }

    public EZAccessTokenInternal OAuthCode(final String authCode, final String scopeStr, final String stateStr) throws BaseException {
        LogUtil.i(TAG, "Enter OAuthCode");
        Object object = this.mRestfulUtils.post(null, new BaseInfo(){
            @HttpParam(name="authCode")
            private String oauthCode;
            @HttpParam(name="grant_type")
            private String grant_type;
            @HttpParam(name="client_id")
            private String client_id;
            @HttpParam(name="bundleId")
            private String bundleId;
            @HttpParam(name="redirect_uri")
            private String redirect_uri;
            @HttpParam(name="scope")
            private String scope;
            @HttpParam(name="state")
            private String state;
            {
                this.oauthCode = authCode;
                this.grant_type = "auth_code";
                this.client_id = BaseAPI.getInstance().getAppKey();
                this.bundleId = LocalInfo.getInstance().getPackageName();
                this.redirect_uri = "default";
                this.scope = scopeStr;
                this.state = stateStr;
            }
        }, LocalInfo.getInstance().getOAuthServAddr() + "/oauth/code", new ApiResponse(){

            @Override
            public Object parse(String response) throws BaseException, JSONException {
                boolean isOk = this.parseCodeHttp(response);
                if (isOk) {
                    JSONObject rootObject = new JSONObject(response);
                    JSONObject resultObject = rootObject.optJSONObject(BaseAPI.API_DATA);
                    LocalInfo.getInstance().getEZAccesstoken().setExpire(Long.parseLong(resultObject.optString("expires_in")));
                    LocalInfo.getInstance().getEZAccesstoken().setAccessToken(resultObject.optString("access_token"));
                    LocalInfo.getInstance().getEZAccesstoken().setOpen_id(resultObject.optString("openId"));
                    LocalInfo.getInstance().getEZAccesstoken().setRefresh_token(resultObject.optString("refresh_token"));
                    LocalInfo.getInstance().getEZAccesstoken().setState(resultObject.optString("state"));
                    LocalInfo.getInstance().getEZAccesstoken().setScope(resultObject.optString("scope"));
                    LocalInfo.getInstance().saveEZAccesstoken();
                    return LocalInfo.getInstance().getEZAccesstoken();
                }
                return null;
            }
        }, true);
        if (object != null) {
            return (EZAccessTokenInternal)object;
        }
        return null;
    }

    public void setVparamForLoginPage(String vParam) {
        WebLoginReq.setmVparam(vParam);
    }

    public Object get(String url, BaseResponse resp) throws BaseException {
        return this.mRestfulUtils.get(url, resp, true);
    }

    public Object get(String url, BaseInfo header, BaseResponse resp) throws BaseException {
        return this.mRestfulUtils.get(url, header, resp, true);
    }

    public boolean isUsingGlobalSDK() {
        return this.isUsingGlobalSDK;
    }

    public void setUsingGlobalSDK(boolean isUsingGlobalSDK) {
        this.isUsingGlobalSDK = isUsingGlobalSDK;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public Boolean refreshToken() throws BaseException {
        if (Config.ENABLE_SDK_TKTOKEN) {
            LogUtil.d(TAG, "stream with user's token, but there is no stream token");
            return false;
        }
        LogUtil.i(TAG, "Enter refreshToken");
        ++this.refreshTokenCount;
        Object object = this.mWaitObject;
        synchronized (object) {
            if (this.refreshTokenCount > 1) {
                --this.refreshTokenCount;
                return true;
            }
            if (TextUtils.isEmpty((CharSequence)LocalInfo.getInstance().getEZAccesstoken().getAccessTokenOrHttpToken()) || TextUtils.isEmpty((CharSequence)LocalInfo.getInstance().getEZAccesstoken().getRefresh_token())) {
                return false;
            }
            final String state_UUID = UUID.randomUUID().toString();
            Object object2 = this.mRestfulUtils.post(null, new BaseInfo(){
                @HttpParam(name="refresh_token")
                private String refresh_token = LocalInfo.getInstance().getEZAccesstoken().getRefresh_token();
                @HttpParam(name="client_id")
                private String client_id = BaseAPI.getInstance().getAppKey();
                @HttpParam(name="grant_type")
                private String grant_type = "refresh_token";
                @HttpParam(name="open_id")
                private String open_id = LocalInfo.getInstance().getEZAccesstoken().getOpen_id();
                @HttpParam(name="bundleId")
                private String bundleId = LocalInfo.getInstance().getPackageName();
                @HttpParam(name="scope")
                private String scope = LocalInfo.getInstance().getEZAccesstoken().getScope();
                @HttpParam(name="state")
                private String state = state_UUID;
            }, LocalInfo.getInstance().getOAuthServAddr() + "/oauth/token/refreshToken", new ApiResponse(){

                @Override
                public Object parse(String response) throws BaseException, JSONException {
                    boolean isOk = this.parseCodeHttp(response);
                    if (isOk) {
                        JSONObject rootObject = new JSONObject(response);
                        JSONObject resultObject = rootObject.optJSONObject(BaseAPI.API_DATA);
                        LocalInfo.getInstance().getEZAccesstoken().setExpire(Long.parseLong(resultObject.optString("expires_in")));
                        LocalInfo.getInstance().getEZAccesstoken().setAccessToken(resultObject.optString("access_token"));
                        LocalInfo.getInstance().getEZAccesstoken().setOpen_id(resultObject.optString("openId"));
                        LocalInfo.getInstance().getEZAccesstoken().setRefresh_token(resultObject.optString("refresh_token"));
                        LocalInfo.getInstance().getEZAccesstoken().setState(resultObject.optString("state"));
                        LocalInfo.getInstance().getEZAccesstoken().setScope(resultObject.optString("scope"));
                        LocalInfo.getInstance().saveEZAccesstoken();
                        return true;
                    }
                    return false;
                }
            }, true);
            return this.resultForAPICall(object2);
        }
    }
}


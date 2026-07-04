/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Application
 *  android.content.Context
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 *  android.content.pm.ApplicationInfo
 *  android.content.pm.PackageInfo
 *  android.content.pm.PackageManager
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.content.res.Resources
 *  android.location.Location
 *  android.os.Build
 *  android.os.Environment
 *  android.text.TextUtils
 *  android.util.Base64
 *  com.google.gson.Gson
 */
package com.videogo.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import com.google.gson.Gson;
import com.videogo.constant.Config;
import com.videogo.openapi.bean.EZAccessTokenInternal;
import com.videogo.util.AESCBCCipher;
import com.videogo.util.LogUtil;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LocalInfo {
    private static String TAG = "LocalInfo";
    private SharedPreferences mSharePreferences;
    private SharedPreferences.Editor mEditor;
    public static final String EMPTY_STRING = "";
    public static final String OAUTH = "oauth";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String USER_CODE = "user_code";
    public static final String AREA_DOMAIN = "areaDomain";
    public static final String FILE_PATH = "file_path";
    public static final String DATE = "date";
    public static final String TODAY_FLOW = "today_flow";
    public static final String MONTH_FLOW = "month_flow";
    public static final String TOTLE_FLOW = "totle_flow";
    public static final String HARD_CODE = "hardwareCode";
    public static final String HARD_NAME = "hardwareName";
    public static final String SHOW_DEMO = "show_demo";
    public static final String PUSH_ADDR = "pushAddr";
    public static final String IS_BIG_CAMERALIST = "is_big_cameralist";
    private static String CUSTOM_SERVER_URL = "";
    private static String CUSTOM_AUTH_SERVER_URL = "";
    public static final String IS_NET_WARN = "is_net_warn";
    public static final String IS_MESSAGE_PUSH = "is_message_push";
    public static final String REPORT_LAST_TIME = "report_last_time";
    public static final String CHECK_VERSION_LAST_TIME = "check_version_last_time";
    public static final String PTZ_PROMPT_COUNT = "PTZ_PROMPT_COUNT";
    public static final String IS_INTL = "is_intl";
    public static final String AD_URL = "ad_url";
    public static final String LACK_OF_SYSTEMT_CERT = "lack_of_system_cert";
    private String mOAuth = null;
    private String mUserCode = "";
    private String mFilePath = "";
    private String mDate = "";
    private long mCurFlow = 0L;
    private long mTodayFlow = 0L;
    private long mMonthFlow = 0L;
    private long mTotleFlow = 0L;
    private int mLimitFlow = 0;
    private boolean mIsMessagePush = true;
    private boolean mIsNetWarn = true;
    private Context mContext = null;
    private static LocalInfo mLocalInfo = null;
    private String mVersionName = "";
    private int mVersionCode = 0;
    private String mHardwareCodeString = "";
    private String mHardwareNameString = "";
    private Location mMyLocation = null;
    private boolean mMyLocationChanged = false;
    private int mNavigationBarHeight = 0;
    private int mScreenHeight = 0;
    private int mScreenWidth = 0;
    private boolean mIsGCMRunning = false;
    private long mReportLastTime = 0L;
    private long mCheckVersionLastTime = 0L;
    private int ptzPromptCount = 0;
    private Map<String, String> mCameraIdMap = new HashMap<String, String>();
    private boolean isSoundOpen = true;
    private String mPackageName = "";
    private String mAppName = "";
    private String mPushAddr = "";
    public static final String EZACCESSTOKEN_OBJECT_JSON = "EZAccessToken_JSON";
    private String mAppKey;
    private EZAccessTokenInternal mEZAccesstoken;
    private String mAreaDomain;
    private String mAccessToken;

    public void saveEZAccesstoken() {
        if (this.mEditor != null) {
            this.setDataWithAes(EZACCESSTOKEN_OBJECT_JSON, this.mEZAccesstoken == null ? EMPTY_STRING : new Gson().toJson((Object)this.mEZAccesstoken));
        }
    }

    public void setEZAccessToken(EZAccessTokenInternal accessToken) {
        this.mEZAccesstoken = accessToken;
        this.saveEZAccesstoken();
    }

    public EZAccessTokenInternal getEZAccesstoken() {
        if (this.mEZAccesstoken == null) {
            this.mEZAccesstoken = new EZAccessTokenInternal();
        }
        return this.mEZAccesstoken;
    }

    public static void init(Application application, String appKey) {
        if (appKey == null) {
            LogUtil.e(TAG, "empty AppKey!");
            return;
        }
        if (mLocalInfo == null) {
            mLocalInfo = new LocalInfo(application, appKey);
        }
        if (!appKey.equals(mLocalInfo.getAppKey())) {
            mLocalInfo.setAppKey(appKey);
        }
    }

    public static LocalInfo getInstance() {
        return mLocalInfo;
    }

    public Context getContext() {
        return this.mContext;
    }

    private LocalInfo(Application application, String appKey) {
        this.mAppKey = appKey;
        this.mContext = application.getApplicationContext();
        this.mSharePreferences = this.mContext.getSharedPreferences("videoGo", 0);
        this.mEditor = this.mSharePreferences.edit();
        this.mCameraIdMap = new HashMap<String, String>();
        File cacheDir = this.mContext.getExternalCacheDir();
        this.mFilePath = cacheDir != null ? this.mContext.getExternalCacheDir().getAbsolutePath() + "/0_OpenSDK" : this.mContext.getCacheDir().getAbsolutePath() + "/0_OpenSDK";
        ApplicationInfo info = application.getApplicationInfo();
        Resources res = this.mContext.getResources();
        if (res != null && info.labelRes > 0) {
            this.mAppName = res.getString(info.labelRes);
        }
        this.mPackageName = info.packageName;
        if (TextUtils.isEmpty((CharSequence)this.mAppName)) {
            this.mAppName = "null";
        }
        if (TextUtils.isEmpty((CharSequence)this.mPackageName)) {
            this.mPackageName = "null";
        }
        this.getPreferences();
    }

    public boolean isSoundOpen() {
        return this.isSoundOpen;
    }

    public void setSoundOpen(boolean isSoundOpen) {
        this.isSoundOpen = isSoundOpen;
    }

    public void setScreenWidthHeight(int screenWidth, int screenHeight) {
        this.mScreenHeight = screenHeight;
        this.mScreenWidth = screenWidth;
    }

    public int getScreenWidth() {
        return this.mScreenWidth;
    }

    public int getScreenHeight() {
        return this.mScreenHeight;
    }

    public void setNavigationBarHeight(int navigationBarHeight) {
        this.mNavigationBarHeight = navigationBarHeight;
    }

    public int getNavigationBarHeight() {
        return this.mNavigationBarHeight;
    }

    public String getHardwareNameFromWare() {
        String nameString = null;
        nameString = Build.MODEL;
        if (nameString == null || nameString.equalsIgnoreCase(EMPTY_STRING)) {
            nameString = "unknow";
        }
        LogUtil.d("Utils", "getHardwareName:" + nameString);
        try {
            return Base64.encodeToString((byte[])nameString.getBytes("utf-8"), (int)2);
        }
        catch (UnsupportedEncodingException e) {
            LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
            return Base64.encodeToString((byte[])nameString.getBytes(), (int)2);
        }
    }

    public String getHardwareCode() {
        if (Config.bFakeHardwareCode) {
            return "78313dadecd92bd11623638d57aa5139";
        }
        if (TextUtils.isEmpty((CharSequence)this.mHardwareCodeString)) {
            this.mHardwareCodeString = this.getHardwareCodeFromware();
            this.setHardwareCode(this.mHardwareCodeString);
        }
        return this.mHardwareCodeString;
    }

    public void setHardwareCode(String hardwareCode) {
        this.mHardwareCodeString = hardwareCode;
        this.setDataWithAes(HARD_CODE, hardwareCode);
    }

    public String getHardwareName() {
        if (TextUtils.isEmpty((CharSequence)this.mHardwareNameString)) {
            this.mHardwareNameString = this.getHardwareNameFromWare();
            this.setHardwareName(this.mHardwareNameString);
        }
        return this.mHardwareNameString;
    }

    public void setHardwareName(String harewareName) {
        this.mHardwareNameString = harewareName;
        if (this.mEditor != null) {
            this.mEditor.putString(HARD_NAME, harewareName);
            this.mEditor.commit();
        }
    }

    public boolean isPublicServAddr() {
        String servAddr = this.getServAddr();
        if (TextUtils.isEmpty((CharSequence)servAddr)) {
            return false;
        }
        return servAddr.contains("open.ys7.com") || servAddr.contains("open.ezvizlife.com");
    }

    public String getServAddr() {
        if (!TextUtils.isEmpty((CharSequence)this.getEZAccesstoken().getAreaDomain())) {
            return this.getEZAccesstoken().getAreaDomain();
        }
        return CUSTOM_SERVER_URL;
    }

    public String getOAuthServAddr() {
        if (!TextUtils.isEmpty((CharSequence)this.getEZAccesstoken().getAreaAuthDomain())) {
            return this.getEZAccesstoken().getAreaAuthDomain();
        }
        return CUSTOM_AUTH_SERVER_URL;
    }

    public String getOriginalServAddr() {
        return CUSTOM_SERVER_URL;
    }

    public String getOriginalAuthServAddr() {
        return CUSTOM_AUTH_SERVER_URL;
    }

    public void setServAddr(String addr) {
        CUSTOM_SERVER_URL = addr;
    }

    public void setAuthServAddr(String addr) {
        CUSTOM_AUTH_SERVER_URL = addr;
    }

    public String getAreaDomain() {
        if (this.mEZAccesstoken != null) {
            return this.mEZAccesstoken.getAreaDomain();
        }
        return EMPTY_STRING;
    }

    public void setAccessToken(String accessToken) {
        this.getEZAccesstoken().setAccessToken(accessToken);
        this.saveEZAccesstoken();
    }

    public void setHttpToken(String httpToken) {
        this.getEZAccesstoken().setHttpToken(httpToken);
        this.saveEZAccesstoken();
    }

    public void setDeviceToken(String deviceSerial, String deviceToken) {
        this.getEZAccesstoken().setDeviceToken(deviceSerial, deviceToken);
    }

    public void setDeviceToken(String deviceSerial, int cameraNo, String deviceGlobalToken, String deviceVideoToken) {
        this.getEZAccesstoken().setDeviceToken(deviceSerial, cameraNo, deviceGlobalToken, deviceVideoToken);
    }

    public String getUserCode() {
        return this.mUserCode;
    }

    public void setUserCode(String userCode) {
        this.mUserCode = userCode;
        if (this.mEditor != null) {
            this.mEditor.putString(USER_CODE, userCode);
            this.mEditor.commit();
        }
    }

    public boolean getIsLogin() {
        String sessionID = this.getEZAccesstoken().getAccessToken();
        return !TextUtils.isEmpty((CharSequence)sessionID);
    }

    public String getFilePath() {
        return this.mFilePath;
    }

    public void setFilePath(String filePath) {
        this.mFilePath = filePath;
        if (this.mEditor != null) {
            this.mEditor.putString(FILE_PATH, filePath);
            this.mEditor.commit();
        }
    }

    public void setDate(String date) {
        this.mDate = date;
        if (this.mEditor != null) {
            this.mEditor.putString(DATE, date);
            this.mEditor.commit();
        }
    }

    public String getDate() {
        return this.mDate;
    }

    public int getLimitFlow() {
        return this.mLimitFlow;
    }

    public int getPtzPromptCount() {
        return this.ptzPromptCount;
    }

    public void setPtzPromptCount(int ptzPromptCount) {
        this.ptzPromptCount = ptzPromptCount;
        if (this.mEditor != null) {
            this.mEditor.putInt(PTZ_PROMPT_COUNT, ptzPromptCount);
            this.mEditor.commit();
        }
    }

    private void getPreferences() {
        if (null != this.mSharePreferences) {
            this.mOAuth = this.mSharePreferences.getString(OAUTH, EMPTY_STRING);
            this.mUserCode = this.mSharePreferences.getString(USER_CODE, EMPTY_STRING);
            this.mHardwareCodeString = this.getDataWithAesCompatibleToOldVersions(HARD_CODE);
            this.mHardwareNameString = this.mSharePreferences.getString(HARD_NAME, EMPTY_STRING);
            this.ptzPromptCount = this.mSharePreferences.getInt(PTZ_PROMPT_COUNT, 0);
            this.mPushAddr = this.mSharePreferences.getString(PUSH_ADDR, EMPTY_STRING);
            String json = this.getDataWithAesCompatibleToOldVersions(EZACCESSTOKEN_OBJECT_JSON);
            if (!TextUtils.isEmpty((CharSequence)json)) {
                this.mSharePreferences.edit().remove(ACCESS_TOKEN).commit();
                this.mSharePreferences.edit().remove(AREA_DOMAIN).commit();
                this.mEZAccesstoken = (EZAccessTokenInternal)new Gson().fromJson(json, EZAccessTokenInternal.class);
            } else {
                this.mEZAccesstoken = new EZAccessTokenInternal();
                this.mAccessToken = this.mSharePreferences.getString(ACCESS_TOKEN, EMPTY_STRING);
                this.mAreaDomain = this.mSharePreferences.getString(AREA_DOMAIN, EMPTY_STRING);
                this.mEZAccesstoken.setAccessToken(this.mAccessToken);
                this.mEZAccesstoken.setAreaDomain(this.mAreaDomain);
                this.mSharePreferences.edit().remove(ACCESS_TOKEN).commit();
                this.mSharePreferences.edit().remove(AREA_DOMAIN).commit();
            }
            if (this.mEZAccesstoken == null) {
                this.mEZAccesstoken = new EZAccessTokenInternal();
            }
            try {
                PackageManager packageManager = this.mContext.getPackageManager();
                PackageInfo packInfo = packageManager.getPackageInfo(this.mContext.getPackageName(), 0);
                this.mVersionName = packInfo.versionName;
                this.mVersionCode = packInfo.versionCode;
            }
            catch (PackageManager.NameNotFoundException e) {
                LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
            }
        }
    }

    public String getPushAddr() {
        if (this.mSharePreferences != null) {
            this.mPushAddr = this.mSharePreferences.getString(LocalInfo.getInstance().getServAddr(), EMPTY_STRING);
        }
        return this.mPushAddr;
    }

    public void setPushAddr(String pushAddr) {
        this.mPushAddr = pushAddr;
        if (this.mEditor != null) {
            this.mEditor.putString(LocalInfo.getInstance().getServAddr(), this.mPushAddr);
            this.mEditor.commit();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private String getHardwareCodeFromwareCacheUUID() {
        File file = null;
        file = new File(Environment.getDataDirectory(), ".com.ezviz.ezopensdk");
        File fileUUID = new File(file, "HardwareCode");
        String uuid = EMPTY_STRING;
        if (!file.isDirectory() && !file.exists()) {
            file.mkdir();
        }
        if (file.exists() && file.isDirectory()) {
            if (fileUUID.exists() && fileUUID.isFile()) {
                BufferedReader fileReader = null;
                try {
                    fileReader = new BufferedReader(new FileReader(fileUUID));
                    uuid = fileReader.readLine();
                }
                catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    if (fileReader != null) {
                        try {
                            fileReader.close();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                try {
                    fileUUID.createNewFile();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (TextUtils.isEmpty((CharSequence)uuid)) {
            uuid = UUID.randomUUID().toString();
            uuid = uuid.replaceAll("\\-", EMPTY_STRING);
            BufferedWriter bufferedWriter = null;
            try {
                bufferedWriter = new BufferedWriter(new FileWriter(fileUUID));
                bufferedWriter.write(uuid, 0, uuid.length());
                bufferedWriter.flush();
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if (bufferedWriter != null) {
                    try {
                        bufferedWriter.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        LogUtil.d(TAG, "getHardwareCodeFromwareCacheUUID = " + uuid);
        return uuid;
    }

    public String getHardwareCodeFromware() {
        String ret = this.getHardwareCodeFromwareCacheUUID();
        return ret;
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public String getAppName() {
        return this.mAppName;
    }

    public String getDeviceModel() {
        return Build.MODEL;
    }

    public boolean isLackOfSystemCert() {
        return this.mSharePreferences.getBoolean(LACK_OF_SYSTEMT_CERT, false);
    }

    public void setLackOfSystemCert(boolean mLackOfSystemCert) {
        this.mSharePreferences.edit().putBoolean(LACK_OF_SYSTEMT_CERT, mLackOfSystemCert).apply();
    }

    public String getAppKey() {
        return this.mAppKey;
    }

    private void setAppKey(String appKey) {
        this.mAppKey = appKey;
    }

    private void setStringToSp(SpKeyEnum keyEnum, String value) {
        this.setStringToSp(keyEnum.keyName(), value);
    }

    private String getStringFromSp(SpKeyEnum keyEnum) {
        return this.getStringFromSp(keyEnum.keyName());
    }

    private void setStringToSp(String key, String value) {
        if (this.mSharePreferences == null) {
            LogUtil.e(TAG, "failed to save " + key + "(" + value + ")");
            return;
        }
        this.mSharePreferences.edit().putString(key, value).apply();
    }

    private void removeStringFromSp(String key) {
        if (this.mSharePreferences == null) {
            LogUtil.e(TAG, "failed to remove " + key);
            return;
        }
        this.mSharePreferences.edit().remove(key).apply();
    }

    private String getStringFromSp(String key) {
        if (this.mSharePreferences == null) {
            LogUtil.e(TAG, "failed to get " + key);
            return null;
        }
        return this.mSharePreferences.getString(key, EMPTY_STRING);
    }

    private void setDataWithAes(String key, String value) {
        if (TextUtils.isEmpty((CharSequence)key)) {
            LogUtil.e(TAG, "setDataWithAes: invalid key!");
            return;
        }
        if (TextUtils.isEmpty((CharSequence)value)) {
            value = EMPTY_STRING;
        }
        String appKey = this.mAppKey;
        try {
            String encryptedKey = AESCBCCipher.encrypt(appKey, key);
            String encryptedValue = AESCBCCipher.encrypt(appKey, value);
            this.setStringToSp(encryptedKey, encryptedValue);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getDataWithAes(String key) {
        if (TextUtils.isEmpty((CharSequence)key)) {
            LogUtil.e(TAG, "getDataWithAes: invalid key!");
            return null;
        }
        String appKey = this.mAppKey;
        try {
            String encryptedKey = AESCBCCipher.encrypt(appKey, key);
            String encryptedValue = this.getStringFromSp(encryptedKey);
            if (!TextUtils.isEmpty((CharSequence)encryptedValue)) {
                return AESCBCCipher.decrypt(appKey, encryptedValue);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getDataWithAesCompatibleToOldVersions(String key) {
        LogUtil.i(TAG, "getDataWithAesCompatibleToOldVersions, key is: " + key);
        if (TextUtils.isEmpty((CharSequence)key)) {
            LogUtil.e(TAG, "getDataWithAesCompatibleToOldVersions: invalid key!");
            return null;
        }
        String value = null;
        try {
            value = this.getDataWithAes(key);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty((CharSequence)value)) {
            LogUtil.d(TAG, "found encrypted data, and decrypted it: " + value);
            return value;
        }
        value = this.getStringFromSp(key);
        if (!TextUtils.isEmpty((CharSequence)value)) {
            LogUtil.i(TAG, "create encryption version, key: " + key);
            this.setDataWithAes(key, value);
            LogUtil.i(TAG, "delete decryption version, key: " + key);
            this.removeStringFromSp(key);
        }
        return value;
    }

    private static enum SpKeyEnum {
        APP_KEY;


        String keyName() {
            return this.name();
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package com.videogo.openapi.bean;

import android.os.Parcel;
import android.os.Parcelable;
import com.videogo.constant.Config;
import com.videogo.util.LogUtil;
import java.util.HashMap;
import java.util.Map;

public class EZAccessTokenInternal
implements Parcelable {
    private static final String TAG = "EZAccessTokenInternal";
    private static final String GLOBAL = "_Global";
    private static final String VIDEO = "_Video";
    private String accessToken;
    private String httpToken;
    private Map<String, String> deviceTokens = new HashMap<String, String>();
    private long expire;
    private String scope;
    private String state;
    private String refresh_token;
    private String open_id;
    private String areaDomain;
    private String areaAuthDomain;
    public static final Parcelable.Creator<EZAccessTokenInternal> CREATOR = new Parcelable.Creator<EZAccessTokenInternal>(){

        public EZAccessTokenInternal createFromParcel(Parcel source) {
            return new EZAccessTokenInternal(source);
        }

        public EZAccessTokenInternal[] newArray(int size) {
            return new EZAccessTokenInternal[size];
        }
    };

    public String getAreaAuthDomain() {
        return this.areaAuthDomain;
    }

    public void setAreaAuthDomain(String areaAuthDomain) {
        this.areaAuthDomain = areaAuthDomain;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getHttpToken() {
        return this.httpToken;
    }

    public void setHttpToken(String httpToken) {
        this.httpToken = httpToken;
        LogUtil.i(TAG, String.format("[\u9884\u64cd\u4f5c]\u8bbe\u7f6ehttpToken(%s)", httpToken));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setDeviceToken(String deviceSerial, String deviceToken) {
        if (deviceSerial == null || deviceSerial.length() == 0 || deviceToken == null || deviceToken.length() == 0) {
            return;
        }
        Map<String, String> map = this.deviceTokens;
        synchronized (map) {
            this.deviceTokens.put(deviceSerial, deviceToken);
        }
        LogUtil.i(TAG, String.format("[\u9884\u64cd\u4f5c]\u8bbe\u7f6edeviceToken(%s, %s)", deviceSerial, deviceToken));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setDeviceToken(String deviceSerial, int cameraNo, String deviceGlobalToken, String deviceVideoToken) {
        if (deviceSerial == null || deviceSerial.length() == 0 || cameraNo < 0 || deviceGlobalToken == null || deviceGlobalToken.length() == 0 || deviceVideoToken == null || deviceVideoToken.length() == 0) {
            return;
        }
        String deviceKey = this.deviceKey(deviceSerial, cameraNo);
        Map<String, String> map = this.deviceTokens;
        synchronized (map) {
            this.deviceTokens.put(deviceKey + GLOBAL, deviceGlobalToken);
            this.deviceTokens.put(deviceKey + VIDEO, deviceVideoToken);
        }
        LogUtil.i(TAG, String.format("[\u9884\u64cd\u4f5c]\u8bbe\u7f6edeviceGlobalToken & deviceVideoToken(%s, %d, %s, %s)", deviceSerial, cameraNo, deviceGlobalToken, deviceVideoToken));
    }

    public String getAccessTokenOrHttpToken() {
        return Config.ENABLE_SDK_TKTOKEN ? this.getHttpToken() : this.getAccessToken();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public String getDeviceToken(String deviceSerial) {
        if (!Config.ENABLE_SDK_TKTOKEN) {
            return this.getAccessToken();
        }
        if (deviceSerial == null || deviceSerial.length() == 0) {
            return "";
        }
        String deviceToken = "";
        Map<String, String> map = this.deviceTokens;
        synchronized (map) {
            deviceToken = this.deviceTokens.get(deviceSerial);
        }
        return deviceToken != null ? deviceToken : "";
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public String getDeviceGlobalToken(String deviceSerial, int cameraNo) {
        if (!Config.ENABLE_SDK_TKTOKEN) {
            return this.getAccessToken();
        }
        if (deviceSerial == null || deviceSerial.length() == 0 || cameraNo < 0) {
            return "";
        }
        String deviceToken = "";
        String deviceKey = this.deviceKey(deviceSerial, cameraNo);
        Map<String, String> map = this.deviceTokens;
        synchronized (map) {
            deviceToken = this.deviceTokens.get(deviceKey + GLOBAL);
        }
        return deviceToken != null ? deviceToken : "";
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public String getDeviceVideoToken(String deviceSerial, int cameraNo) {
        if (!Config.ENABLE_SDK_TKTOKEN) {
            return this.getAccessToken();
        }
        if (deviceSerial == null || deviceSerial.length() == 0 || cameraNo < 0) {
            return "";
        }
        String deviceToken = "";
        String deviceKey = this.deviceKey(deviceSerial, cameraNo);
        Map<String, String> map = this.deviceTokens;
        synchronized (map) {
            deviceToken = this.deviceTokens.get(deviceKey + VIDEO);
        }
        return deviceToken != null ? deviceToken : "";
    }

    public long getExpire() {
        return this.expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public String getScope() {
        return this.scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRefresh_token() {
        return this.refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getOpen_id() {
        return this.open_id;
    }

    public void setOpen_id(String open_id) {
        this.open_id = open_id;
    }

    public int describeContents() {
        return 0;
    }

    public String getAreaDomain() {
        return this.areaDomain;
    }

    public void setAreaDomain(String areaDomain) {
        this.areaDomain = areaDomain;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.accessToken);
        dest.writeLong(this.expire);
        dest.writeString(this.scope);
        dest.writeString(this.state);
        dest.writeString(this.refresh_token);
        dest.writeString(this.open_id);
        dest.writeString(this.areaDomain);
        dest.writeString(this.areaAuthDomain);
    }

    public EZAccessTokenInternal() {
    }

    protected EZAccessTokenInternal(Parcel in) {
        this.accessToken = in.readString();
        this.expire = in.readLong();
        this.scope = in.readString();
        this.state = in.readString();
        this.refresh_token = in.readString();
        this.open_id = in.readString();
        this.areaDomain = in.readString();
        this.areaAuthDomain = in.readString();
    }

    private String deviceKey(String deviceSerial, int cameraNo) {
        return deviceSerial + "_" + cameraNo;
    }
}


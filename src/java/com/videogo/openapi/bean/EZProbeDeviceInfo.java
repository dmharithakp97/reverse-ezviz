/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.text.TextUtils
 *  com.google.gson.annotations.Expose
 *  org.json.JSONException
 *  org.json.JSONObject
 */
package com.videogo.openapi.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.google.gson.annotations.Expose;
import com.videogo.openapi.annotation.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class EZProbeDeviceInfo
implements Parcelable {
    @Serializable(name="displayName")
    private String displayName;
    @Serializable(name="subSerial")
    private String subSerial;
    @Serializable(name="fullSerial")
    private String fullSerial;
    @Serializable(name="status")
    private int status;
    @Serializable(name="defaultPicPath")
    private String defaultPicPath;
    @Serializable(name="supportWifi")
    private int supportWifi;
    @Serializable(name="releaseVersion")
    private String releaseVersion;
    @Serializable(name="availableChannelCount")
    private int availableChannelCount;
    @Serializable(name="relatedDeviceCount")
    private int relatedDeviceCount;
    @Serializable(name="supportExt")
    private String supportExt;
    @Expose
    private Map<String, String> supportExtMap;
    @Serializable(name="model")
    private String model;
    public static final Parcelable.Creator<EZProbeDeviceInfo> CREATOR = new Parcelable.Creator<EZProbeDeviceInfo>(){

        public EZProbeDeviceInfo createFromParcel(Parcel source) {
            return new EZProbeDeviceInfo(source);
        }

        public EZProbeDeviceInfo[] newArray(int size) {
            return new EZProbeDeviceInfo[size];
        }
    };

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getSubSerial() {
        return this.subSerial;
    }

    public void setSubSerial(String subSerial) {
        this.subSerial = subSerial;
    }

    public String getFullSerial() {
        return this.fullSerial;
    }

    public void setFullSerial(String fullSerial) {
        this.fullSerial = fullSerial;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDefaultPicPath() {
        return this.defaultPicPath;
    }

    public void setDefaultPicPath(String defaultPicPath) {
        this.defaultPicPath = defaultPicPath;
    }

    private void setSupportWifi(int supportWifi) {
        this.supportWifi = supportWifi;
    }

    public String getReleaseVersion() {
        return this.releaseVersion;
    }

    private void setReleaseVersion(String releaseVersion) {
        this.releaseVersion = releaseVersion;
    }

    public int getAvailableChannelCount() {
        return this.availableChannelCount;
    }

    private void setAvailableChannelCount(int availableChannelCount) {
        this.availableChannelCount = availableChannelCount;
    }

    public int getRelatedDeviceCount() {
        return this.relatedDeviceCount;
    }

    private void setRelatedDeviceCount(int relatedDeviceCount) {
        this.relatedDeviceCount = relatedDeviceCount;
    }

    public int getSupportWifi() {
        if (this.getSupportAP() == 2) {
            return 0;
        }
        return this.getSupport("support_wifi");
    }

    public boolean isSupport5GWiFi() {
        return this.getSupport("support_wifi_5G") == 1;
    }

    public int getSupportAP() {
        if (this.getSupportAPType() == 1) {
            return 0;
        }
        return this.getSupport("support_ap_mode");
    }

    public int getSupportAPType() {
        return this.getSupport("support_ap_protocol_type");
    }

    public int getSupportSoundWave() {
        if (this.getSupportAP() == 2) {
            return 0;
        }
        return this.getSupport("support_new_sound_wave");
    }

    public int getDeviceHotSpot() {
        return this.getSupport("support_custom_wifi_prefix");
    }

    private String getSupportExt() {
        return this.supportExt;
    }

    private void setSupportExt(String supportExt) {
        this.supportExt = supportExt;
    }

    private int getSupport(String key) {
        if (TextUtils.isEmpty((CharSequence)this.supportExt) || TextUtils.isEmpty((CharSequence)key)) {
            return 0;
        }
        try {
            JSONObject jsonObject = new JSONObject(this.supportExt);
            if (this.supportExtMap == null) {
                this.supportExtMap = new HashMap<String, String>();
                Iterator iter = jsonObject.keys();
                while (iter.hasNext()) {
                    String entry;
                    String keyentry = entry = (String)iter.next();
                    String valueentry = jsonObject.optString(keyentry);
                    this.supportExtMap.put(keyentry, valueentry);
                }
            }
            if (this.supportExtMap == null && this.supportExtMap.size() == 0) {
                return 0;
            }
            String value = this.supportExtMap.get(key);
            if (TextUtils.isEmpty((CharSequence)value)) {
                return 0;
            }
            return Integer.valueOf(value);
        }
        catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public String getModel() {
        return this.model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.displayName);
        dest.writeString(this.subSerial);
        dest.writeString(this.fullSerial);
        dest.writeInt(this.status);
        dest.writeString(this.defaultPicPath);
        dest.writeInt(this.supportWifi);
        dest.writeString(this.releaseVersion);
        dest.writeInt(this.availableChannelCount);
        dest.writeInt(this.relatedDeviceCount);
        dest.writeString(this.supportExt);
        dest.writeString(this.model);
    }

    public EZProbeDeviceInfo() {
    }

    protected EZProbeDeviceInfo(Parcel in) {
        this.displayName = in.readString();
        this.subSerial = in.readString();
        this.fullSerial = in.readString();
        this.status = in.readInt();
        this.defaultPicPath = in.readString();
        this.supportWifi = in.readInt();
        this.releaseVersion = in.readString();
        this.availableChannelCount = in.readInt();
        this.relatedDeviceCount = in.readInt();
        this.supportExt = in.readString();
        this.model = in.readString();
    }
}


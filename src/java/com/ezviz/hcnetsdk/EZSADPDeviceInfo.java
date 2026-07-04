/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  com.hikvision.sadp.SADP_DEVICE_INFO
 */
package com.ezviz.hcnetsdk;

import android.os.Parcel;
import android.os.Parcelable;
import com.ezviz.hcsadp.jna.HCSadpSDKByJNA;
import com.hikvision.sadp.SADP_DEVICE_INFO;

public class EZSADPDeviceInfo
implements Parcelable {
    private String deviceSerial;
    private String deviceMac;
    private boolean actived;
    private String localIp;
    private int localPort;
    private String localIpV6;
    private int httpPort;
    private int deviceType;
    private String deviceTypeDes;
    private String firmwareVersion;
    private int abilitySupport;
    private boolean DHCPOn;
    private boolean isEzvizDevice;
    public static final Parcelable.Creator<EZSADPDeviceInfo> CREATOR = new Parcelable.Creator<EZSADPDeviceInfo>(){

        public EZSADPDeviceInfo createFromParcel(Parcel source) {
            return new EZSADPDeviceInfo(source);
        }

        public EZSADPDeviceInfo[] newArray(int size) {
            return new EZSADPDeviceInfo[size];
        }
    };

    public EZSADPDeviceInfo() {
    }

    public EZSADPDeviceInfo(SADP_DEVICE_INFO device_info) {
        this.deviceSerial = new String(device_info.szSerialNO).trim();
        this.deviceMac = new String(device_info.szMAC).trim();
        this.actived = device_info.byActivated == 0;
        this.localIp = new String(device_info.szIPv4Address).trim();
        this.localPort = device_info.dwPort;
        this.localIpV6 = new String(device_info.szIPv4SubnetMask).trim();
        this.httpPort = device_info.wHttpPort;
        this.deviceType = device_info.dwDeviceType;
        this.deviceTypeDes = new String(device_info.szDevDesc).trim();
        this.firmwareVersion = new String(device_info.szDeviceSoftwareVersion).trim();
        this.abilitySupport = device_info.byDeviceAbility;
        this.DHCPOn = device_info.byDhcpEnabled == 1;
        boolean bl = this.isEzvizDevice = device_info.byEZVIZCode == 1;
        if (this.isEzvizDevice || this.deviceSerial.startsWith("CS-")) {
            this.setActived(true);
        }
    }

    public EZSADPDeviceInfo(HCSadpSDKByJNA.SADP_DEVICE_INFO device_info) {
        this.deviceSerial = new String(device_info.szSerialNO).trim();
        this.deviceMac = new String(device_info.szMAC).trim();
        this.actived = device_info.byActivated == 0;
        this.localIp = new String(device_info.szIPv4Address).trim();
        this.localPort = device_info.dwPort;
        this.localIpV6 = new String(device_info.szIPv4SubnetMask).trim();
        this.httpPort = device_info.wHttpPort;
        this.deviceType = device_info.dwDeviceType;
        this.deviceTypeDes = new String(device_info.szDevDesc).trim();
        this.firmwareVersion = new String(device_info.szDeviceSoftwareVersion).trim();
        this.abilitySupport = device_info.byDeviceAbility;
        this.DHCPOn = device_info.byDhcpEnabled == 1;
        boolean bl = this.isEzvizDevice = device_info.byEZVIZCode == 1;
        if (this.isEzvizDevice || this.deviceSerial.startsWith("CS-")) {
            this.setActived(true);
        }
    }

    public String getDeviceSerial() {
        return this.deviceSerial;
    }

    public void setDeviceSerial(String deviceSerial) {
        this.deviceSerial = deviceSerial;
    }

    public String getDeviceMac() {
        return this.deviceMac;
    }

    public void setDeviceMac(String deviceMac) {
        this.deviceMac = deviceMac;
    }

    public boolean isActived() {
        return this.actived;
    }

    public void setActived(boolean actived) {
        this.actived = actived;
    }

    public String getLocalIp() {
        return this.localIp;
    }

    public void setLocalIp(String localIp) {
        this.localIp = localIp;
    }

    public int getLocalPort() {
        return this.localPort;
    }

    public void setLocalPort(int localPort) {
        this.localPort = localPort;
    }

    public String getLocalIpV6() {
        return this.localIpV6;
    }

    public void setLocalIpV6(String localIpV6) {
        this.localIpV6 = localIpV6;
    }

    public int getHttpPort() {
        return this.httpPort;
    }

    public void setHttpPort(int httpPort) {
        this.httpPort = httpPort;
    }

    public int getDeviceType() {
        return this.deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceTypeDes() {
        return this.deviceTypeDes;
    }

    public void setDeviceTypeDes(String deviceTypeDes) {
        this.deviceTypeDes = deviceTypeDes;
    }

    public String getFirmwareVersion() {
        return this.firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    public int getAbilitySupport() {
        return this.abilitySupport;
    }

    public void setAbilitySupport(int abilitySupport) {
        this.abilitySupport = abilitySupport;
    }

    public boolean isDHCPOn() {
        return this.DHCPOn;
    }

    public void setDHCPOn(boolean DHCPOn) {
        this.DHCPOn = DHCPOn;
    }

    public boolean isEzvizDevice() {
        return this.isEzvizDevice;
    }

    public void setEzvizDevice(boolean ezvizDevice) {
        this.isEzvizDevice = ezvizDevice;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.deviceSerial);
        dest.writeString(this.deviceMac);
        dest.writeByte(this.actived ? (byte)1 : 0);
        dest.writeString(this.localIp);
        dest.writeInt(this.localPort);
        dest.writeString(this.localIpV6);
        dest.writeInt(this.httpPort);
        dest.writeInt(this.deviceType);
        dest.writeString(this.deviceTypeDes);
        dest.writeString(this.firmwareVersion);
        dest.writeInt(this.abilitySupport);
        dest.writeByte(this.DHCPOn ? (byte)1 : 0);
        dest.writeByte(this.isEzvizDevice ? (byte)1 : 0);
    }

    protected EZSADPDeviceInfo(Parcel in) {
        this.deviceSerial = in.readString();
        this.deviceMac = in.readString();
        this.actived = in.readByte() != 0;
        this.localIp = in.readString();
        this.localPort = in.readInt();
        this.localIpV6 = in.readString();
        this.httpPort = in.readInt();
        this.deviceType = in.readInt();
        this.deviceTypeDes = in.readString();
        this.firmwareVersion = in.readString();
        this.abilitySupport = in.readInt();
        this.DHCPOn = in.readByte() != 0;
        this.isEzvizDevice = in.readByte() != 0;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.openapi.bean.resp;

import com.videogo.openapi.annotation.Serializable;

public class EZDevicePlayInfo {
    @Serializable(name="deviceSerial")
    private String deviceSerial;
    @Serializable(name="deviceIP")
    private String deviceIP;
    @Serializable(name="devicePort")
    private int devicePort;
    @Serializable(name="cmdPort")
    private int cmdPort;
    @Serializable(name="httpPort")
    private int httpPort;
    @Serializable(name="streamPort")
    private int streamPort;
    @Serializable(name="localIp")
    private String localIp;
    @Serializable(name="localDevicePort")
    private int localDevicePort;
    @Serializable(name="localCmdPort")
    private int localCmdPort;
    @Serializable(name="localHttpPort")
    private int localHttpPort;
    @Serializable(name="localStreamPort")
    private int localStreamPort;
    @Serializable(name="netType")
    private int netType;
    @Serializable(name="ppvsAddr")
    private String ppvsAddr;
    @Serializable(name="ppvsPort")
    private int ppvsPort;
    @Serializable(name="casIp")
    private String casIp;
    @Serializable(name="casPort")
    private int casPort;
    @Serializable(name="maskIp")
    private String maskIp;
    @Serializable(name="upnp")
    private int upnp;
    @Serializable(name="cloudServiceStatus")
    private int cloudServiceStatus;
    @Serializable(name="releaseVersion")
    private String releaseVersion;
    @Serializable(name="belongSerial")
    private Object belongSerial;
    @Serializable(name="belongNo")
    private int belongNo;
    @Serializable(name="belongAdded")
    private int belongAdded;
    @Serializable(name="isEncrypt")
    private int isEncrypt;
    @Serializable(name="encryptPwd")
    private String encryptPwd;
    @Serializable(name="belongState")
    private int belongState;
    @Serializable(name="vtmIp")
    private String vtmIp;
    @Serializable(name="vtmPort")
    private int vtmPort;
    @Serializable(name="ttsIp")
    private String ttsIp;
    @Serializable(name="ttsPort")
    private int ttsPort;
    @Serializable(name="deviceStatus")
    private int deviceStatus;
    @Serializable(name="supportExt")
    private String supportExt;
    @Serializable(name="supportExtShort")
    private String supportExtShort;
    @Serializable(name="belongDevice")
    private Object belongDevice;
    @Serializable(name="publicKey")
    private String publicKey;
    @Serializable(name="version")
    private int publicKeyVersion;

    public void setDeviceSerial(String deviceSerial) {
        this.deviceSerial = deviceSerial;
    }

    public void setDeviceIP(String deviceIP) {
        this.deviceIP = deviceIP;
    }

    public void setDevicePort(int devicePort) {
        this.devicePort = devicePort;
    }

    public void setCmdPort(int cmdPort) {
        this.cmdPort = cmdPort;
    }

    public void setHttpPort(int httpPort) {
        this.httpPort = httpPort;
    }

    public void setStreamPort(int streamPort) {
        this.streamPort = streamPort;
    }

    public void setLocalIp(String localIp) {
        this.localIp = localIp;
    }

    public void setLocalDevicePort(int localDevicePort) {
        this.localDevicePort = localDevicePort;
    }

    public void setLocalCmdPort(int localCmdPort) {
        this.localCmdPort = localCmdPort;
    }

    public void setLocalHttpPort(int localHttpPort) {
        this.localHttpPort = localHttpPort;
    }

    public void setLocalStreamPort(int localStreamPort) {
        this.localStreamPort = localStreamPort;
    }

    public void setNetType(int netType) {
        this.netType = netType;
    }

    public void setPpvsAddr(String ppvsAddr) {
        this.ppvsAddr = ppvsAddr;
    }

    public void setPpvsPort(int ppvsPort) {
        this.ppvsPort = ppvsPort;
    }

    public void setCasIp(String casIp) {
        this.casIp = casIp;
    }

    public void setCasPort(int casPort) {
        this.casPort = casPort;
    }

    public void setMaskIp(String maskIp) {
        this.maskIp = maskIp;
    }

    public void setUpnp(int upnp) {
        this.upnp = upnp;
    }

    public void setCloudServiceStatus(int cloudServiceStatus) {
        this.cloudServiceStatus = cloudServiceStatus;
    }

    public void setReleaseVersion(String releaseVersion) {
        this.releaseVersion = releaseVersion;
    }

    public void setBelongSerial(Object belongSerial) {
        this.belongSerial = belongSerial;
    }

    public void setBelongNo(int belongNo) {
        this.belongNo = belongNo;
    }

    public void setBelongAdded(int belongAdded) {
        this.belongAdded = belongAdded;
    }

    public void setIsEncrypt(int isEncrypt) {
        this.isEncrypt = isEncrypt;
    }

    public void setEncryptPwd(String encryptPwd) {
        this.encryptPwd = encryptPwd;
    }

    public void setBelongState(int belongState) {
        this.belongState = belongState;
    }

    public void setVtmIp(String vtmIp) {
        this.vtmIp = vtmIp;
    }

    public void setVtmPort(int vtmPort) {
        this.vtmPort = vtmPort;
    }

    public void setTtsIp(String ttsIp) {
        this.ttsIp = ttsIp;
    }

    public void setTtsPort(int ttsPort) {
        this.ttsPort = ttsPort;
    }

    public void setDeviceStatus(int deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public void setSupportExt(String supportExt) {
        this.supportExt = supportExt;
    }

    public void setSupportExtShort(String supportExtShort) {
        this.supportExtShort = supportExtShort;
    }

    public void setBelongDevice(Object belongDevice) {
        this.belongDevice = belongDevice;
    }

    public String getDeviceSerial() {
        return this.deviceSerial;
    }

    public String getDeviceIP() {
        return this.deviceIP;
    }

    public int getDevicePort() {
        return this.devicePort;
    }

    public int getCmdPort() {
        return this.cmdPort;
    }

    public int getHttpPort() {
        return this.httpPort;
    }

    public int getStreamPort() {
        return this.streamPort;
    }

    public String getLocalIp() {
        return this.localIp;
    }

    public int getLocalDevicePort() {
        return this.localDevicePort;
    }

    public int getLocalCmdPort() {
        return this.localCmdPort;
    }

    public int getLocalHttpPort() {
        return this.localHttpPort;
    }

    public int getLocalStreamPort() {
        return this.localStreamPort;
    }

    public int getNetType() {
        return this.netType;
    }

    public String getPpvsAddr() {
        return this.ppvsAddr;
    }

    public int getPpvsPort() {
        return this.ppvsPort;
    }

    public String getCasIp() {
        return this.casIp;
    }

    public int getCasPort() {
        return this.casPort;
    }

    public String getMaskIp() {
        return this.maskIp;
    }

    public int getUpnp() {
        return this.upnp;
    }

    public int getCloudServiceStatus() {
        return this.cloudServiceStatus;
    }

    public String getReleaseVersion() {
        return this.releaseVersion;
    }

    public Object getBelongSerial() {
        return this.belongSerial;
    }

    public int getBelongNo() {
        return this.belongNo;
    }

    public int getBelongAdded() {
        return this.belongAdded;
    }

    public int getIsEncrypt() {
        return this.isEncrypt;
    }

    public String getEncryptPwd() {
        return this.encryptPwd;
    }

    public int getBelongState() {
        return this.belongState;
    }

    public String getVtmIp() {
        return this.vtmIp;
    }

    public int getVtmPort() {
        return this.vtmPort;
    }

    public String getTtsIp() {
        return this.ttsIp;
    }

    public int getTtsPort() {
        return this.ttsPort;
    }

    public int getDeviceStatus() {
        return this.deviceStatus;
    }

    public String getSupportExt() {
        return this.supportExt;
    }

    public String getSupportExtShort() {
        return this.supportExtShort;
    }

    public Object getBelongDevice() {
        return this.belongDevice;
    }

    public String getPublicKey() {
        return this.publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public int getPublicKeyVersion() {
        return this.publicKeyVersion;
    }

    public void setPublicKeyVersion(int publicKeyVersion) {
        this.publicKeyVersion = publicKeyVersion;
    }

    public String toString() {
        return "EZDevicePlayInfo{deviceSerial='" + this.deviceSerial + '\'' + ", deviceIP='" + this.deviceIP + '\'' + ", devicePort=" + this.devicePort + ", cmdPort=" + this.cmdPort + ", httpPort=" + this.httpPort + ", streamPort=" + this.streamPort + ", localIp='" + this.localIp + '\'' + ", localDevicePort=" + this.localDevicePort + ", localCmdPort=" + this.localCmdPort + ", localHttpPort=" + this.localHttpPort + ", localStreamPort=" + this.localStreamPort + ", netType=" + this.netType + ", ppvsAddr='" + this.ppvsAddr + '\'' + ", ppvsPort=" + this.ppvsPort + ", casIp='" + this.casIp + '\'' + ", casPort=" + this.casPort + ", maskIp='" + this.maskIp + '\'' + ", upnp=" + this.upnp + ", cloudServiceStatus=" + this.cloudServiceStatus + ", releaseVersion='" + this.releaseVersion + '\'' + ", belongSerial=" + this.belongSerial + ", belongNo=" + this.belongNo + ", belongAdded=" + this.belongAdded + ", isEncrypt=" + this.isEncrypt + ", encryptPwd='" + this.encryptPwd + '\'' + ", belongState=" + this.belongState + ", vtmIp='" + this.vtmIp + '\'' + ", vtmPort=" + this.vtmPort + ", ttsIp='" + this.ttsIp + '\'' + ", ttsPort=" + this.ttsPort + ", deviceStatus=" + this.deviceStatus + ", publicKey=" + this.publicKey + ", publicKeyVersion=" + this.publicKeyVersion + '}';
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.openapi.bean;

import com.videogo.openapi.annotation.Serializable;

public class EZServerInfo {
    private String stun1Addr;
    @Serializable(name="stun1Port")
    private int stun1Port;
    private String stun2Addr;
    private int stun2Port;
    private String vtmAddr;
    private int vtmPort;
    private boolean microCloudMode;
    private String ttsAddr;
    private int ttsPort;
    private String pushAddr;
    private int pushHttpPort;
    private int pushHttpsPort;
    private String authAddr;
    private String nodeJsAddr;
    private String nodeJsHttpPort;
    private String logAddr;
    private String oasLogAddr;
    private String aiMediaAddr;
    private int aiMediaPort;

    public String getStun1Addr() {
        return this.stun1Addr;
    }

    public void setStun1Addr(String stun1Addr) {
        this.stun1Addr = stun1Addr;
    }

    public int getStun1Port() {
        return this.stun1Port;
    }

    public void setStun1Port(int stun1Port) {
        this.stun1Port = stun1Port;
    }

    public String getStun2Addr() {
        return this.stun2Addr;
    }

    public void setStun2Addr(String stun2Addr) {
        this.stun2Addr = stun2Addr;
    }

    public int getStun2Port() {
        return this.stun2Port;
    }

    public void setStun2Port(int stun2Port) {
        this.stun2Port = stun2Port;
    }

    public String getVtmAddr() {
        return this.vtmAddr;
    }

    public void setVtmAddr(String vtmAddr) {
        this.vtmAddr = vtmAddr;
    }

    public int getVtmPort() {
        return this.vtmPort;
    }

    public void setVtmPort(int vtmPort) {
        this.vtmPort = vtmPort;
    }

    public boolean isMicroCloudMode() {
        return this.microCloudMode;
    }

    public void setMicroCloudMode(boolean microCloudMode) {
        this.microCloudMode = microCloudMode;
    }

    public String getTtsAddr() {
        return this.ttsAddr;
    }

    public void setTtsAddr(String ttsAddr) {
        this.ttsAddr = ttsAddr;
    }

    public int getTtsPort() {
        return this.ttsPort;
    }

    public void setTtsPort(int ttsPort) {
        this.ttsPort = ttsPort;
    }

    public String getPushAddr() {
        return this.pushAddr;
    }

    public void setPushAddr(String pushAddr) {
        this.pushAddr = pushAddr;
    }

    public int getPushHttpPort() {
        return this.pushHttpPort;
    }

    public void setPushHttpPort(int pushHttpPort) {
        this.pushHttpPort = pushHttpPort;
    }

    public int getPushHttpsPort() {
        return this.pushHttpsPort;
    }

    public void setPushHttpsPort(int pushHttpsPort) {
        this.pushHttpsPort = pushHttpsPort;
    }

    public String getAuthAddr() {
        return this.authAddr;
    }

    public void setAuthAddr(String authAddr) {
        this.authAddr = authAddr;
    }

    public String getNodeJsAddr() {
        return this.nodeJsAddr;
    }

    public void setNodeJsAddr(String nodeJsAddr) {
        this.nodeJsAddr = nodeJsAddr;
    }

    public String getNodeJsHttpPort() {
        return this.nodeJsHttpPort;
    }

    public void setNodeJsHttpPort(String nodeJsHttpPort) {
        this.nodeJsHttpPort = nodeJsHttpPort;
    }

    public String getLogAddr() {
        return this.logAddr;
    }

    public void setLogAddr(String logAddr) {
        this.logAddr = logAddr;
    }

    public String getOasLogAddr() {
        return this.oasLogAddr;
    }

    public void setOasLogAddr(String oasLogAddr) {
        this.oasLogAddr = oasLogAddr;
    }

    public String getAiMediaAddr() {
        return this.aiMediaAddr;
    }

    public void setAiMediaAddr(String aiMediaAddr) {
        this.aiMediaAddr = aiMediaAddr;
    }

    public int getAiMediaPort() {
        return this.aiMediaPort;
    }

    public void setAiMediaPort(int aiMediaPort) {
        this.aiMediaPort = aiMediaPort;
    }
}


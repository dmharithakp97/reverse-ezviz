/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.openapi.bean;

import com.videogo.openapi.annotation.Serializable;

public class EZDeviceVersion {
    @Serializable(name="currentVersion")
    private String currentVersion;
    @Serializable(name="newestVersion")
    private String newestVersion;
    @Serializable(name="isNeedUpgrade")
    private int isNeedUpgrade;
    @Serializable(name="isUpgrading")
    private int isUpgrading;
    @Serializable(name="url")
    private String downloadUrl;
    @Serializable(name="md5")
    private String md5;
    @Serializable(name="upgradeDesc")
    private String upgradeDesc;
    @Serializable(name="fullPackSize")
    private int fullPackSize;
    @Serializable(name="incrPackSize")
    private int incrPackSize;
    @Serializable(name="model")
    private String model;

    private void setCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion;
    }

    private void setNewestVersion(String newestVersion) {
        this.newestVersion = newestVersion;
    }

    private void setIsNeedUpgrade(int isNeedUpgrade) {
        this.isNeedUpgrade = isNeedUpgrade;
    }

    private void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    private void setMd5(String md5) {
        this.md5 = md5;
    }

    private void setUpgradeDesc(String upgradeDesc) {
        this.upgradeDesc = upgradeDesc;
    }

    private void setFullPackSize(int fullPackSize) {
        this.fullPackSize = fullPackSize;
    }

    private void setIncrPackSize(int incrPackSize) {
        this.incrPackSize = incrPackSize;
    }

    private void setModel(String model) {
        this.model = model;
    }

    public String getCurrentVersion() {
        return this.currentVersion;
    }

    public String getNewestVersion() {
        return this.newestVersion;
    }

    public int getIsNeedUpgrade() {
        return this.isNeedUpgrade;
    }

    public String getDownloadUrl() {
        return this.downloadUrl;
    }

    public String getMd5() {
        return this.md5;
    }

    public String getUpgradeDesc() {
        return this.upgradeDesc;
    }

    public int getFullPackSize() {
        return this.fullPackSize;
    }

    public int getIncrPackSize() {
        return this.incrPackSize;
    }

    public String getModel() {
        return this.model;
    }

    public int getIsUpgrading() {
        return this.isUpgrading;
    }

    public void setIsUpgrading(int isUpgrading) {
        this.isUpgrading = isUpgrading;
    }
}


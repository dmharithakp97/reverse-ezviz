/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.openapi.bean;

public class EZDevicePtzAngleInfo {
    private int horStartAng;
    private int horEndAng;
    private int horCurAng;
    private int verStartAng;
    private int verEndAng;
    private int verCurAng;
    private int orientation;
    private int ptzCfgVersion;

    public EZDevicePtzAngleInfo(int horStartAng, int horEndAng, int horCurAng, int verStartAng, int verEndAng, int verCurAng, int orientation, int ptzCfgVersion) {
        this.horStartAng = horStartAng;
        this.horEndAng = horEndAng;
        this.horCurAng = horCurAng;
        this.verStartAng = verStartAng;
        this.verEndAng = verEndAng;
        this.verCurAng = verCurAng;
        this.orientation = orientation;
        this.ptzCfgVersion = ptzCfgVersion;
    }

    public String toString() {
        return "EZDevicePtzAngleInfo{horStartAng=" + this.horStartAng + ", horEndAng=" + this.horEndAng + ", horCurAng=" + this.horCurAng + ", verStartAng=" + this.verStartAng + ", verEndAng=" + this.verEndAng + ", verCurAng=" + this.verCurAng + ", orientation=" + this.orientation + ", ptzCfgVersion=" + this.ptzCfgVersion + '}';
    }

    public int getHorStartAng() {
        return this.horStartAng;
    }

    public void setHorStartAng(int horStartAng) {
        this.horStartAng = horStartAng;
    }

    public int getHorEndAng() {
        return this.horEndAng;
    }

    public void setHorEndAng(int horEndAng) {
        this.horEndAng = horEndAng;
    }

    public int getHorCurAng() {
        return this.horCurAng;
    }

    public void setHorCurAng(int horCurAng) {
        this.horCurAng = horCurAng;
    }

    public int getVerStartAng() {
        return this.verStartAng;
    }

    public void setVerStartAng(int verStartAng) {
        this.verStartAng = verStartAng;
    }

    public int getVerEndAng() {
        return this.verEndAng;
    }

    public void setVerEndAng(int verEndAng) {
        this.verEndAng = verEndAng;
    }

    public int getVerCurAng() {
        return this.verCurAng;
    }

    public void setVerCurAng(int verCurAng) {
        this.verCurAng = verCurAng;
    }

    public int getOrientation() {
        return this.orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public int getPtzCfgVersion() {
        return this.ptzCfgVersion;
    }

    public void setPtzCfgVersion(int ptzCfgVersion) {
        this.ptzCfgVersion = ptzCfgVersion;
    }
}


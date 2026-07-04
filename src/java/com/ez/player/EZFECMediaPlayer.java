/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.SurfaceTexture
 *  android.util.Log
 *  android.view.Surface
 *  android.view.SurfaceHolder
 */
package com.ez.player;

import android.graphics.SurfaceTexture;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import com.ez.player.EZMediaPlayer;
import com.ez.stream.EZStreamClientManager;
import com.ez.stream.InitParam;
import com.ez.stream.LogUtil;
import com.ez.stream.NativeApi;
import java.util.ArrayList;
import java.util.Locale;

public class EZFECMediaPlayer
extends EZMediaPlayer {
    boolean m_bFEC = true;
    ArrayList<EZFECInfo> mFECInfos = new ArrayList();

    public EZFECMediaPlayer(EZStreamClientManager manager, InitParam initParam) {
        super(manager, initParam);
    }

    public EZFECMediaPlayer(EZStreamClientManager manager, String url, boolean forSqual) {
        super(manager, url, forSqual);
    }

    public EZFECMediaPlayer(EZStreamClientManager manager, String fileName) {
        super(manager, fileName);
    }

    public EZFECMediaPlayer(EZStreamClientManager manager, InitParam initParam, boolean ezlinkDevice) {
        super(manager, initParam, ezlinkDevice);
    }

    int createFECIn(int nPlaceType, int nCorrect) {
        if (this.m_bFEC && NativeApi.enableFEC(this.mhMedia) != 0) {
            this.m_bFEC = false;
        }
        if (this.mFECInfos.size() > 5) {
            return -1;
        }
        int nNewCorrectType = 256;
        switch (nCorrect) {
            case 0: {
                nNewCorrectType = 256;
                break;
            }
            case 257: {
                nNewCorrectType = 257;
                break;
            }
            case 1: {
                nNewCorrectType = 512;
                break;
            }
            case 2: {
                nNewCorrectType = 768;
                break;
            }
            case 3: {
                nNewCorrectType = 1024;
                break;
            }
            case 4: {
                nNewCorrectType = 1280;
                break;
            }
            case 5: {
                nNewCorrectType = 1536;
                break;
            }
            case 6: {
                nNewCorrectType = 1792;
                break;
            }
            case 7: {
                nNewCorrectType = 2048;
                break;
            }
            case 8: {
                nNewCorrectType = 2304;
                break;
            }
            case 9: {
                nNewCorrectType = 2560;
                break;
            }
            case 10: {
                nNewCorrectType = 2816;
                break;
            }
            case 11: {
                nNewCorrectType = 3072;
                break;
            }
            default: {
                nNewCorrectType = 256;
            }
        }
        int subPort = NativeApi.getFECPort(this.mhMedia, nPlaceType, nNewCorrectType);
        LogUtil.d("EZMediaPlayer", String.format(Locale.CHINA, "getFECPort %d nPlaceType[%d] nNewCorrectType[%d]", subPort, nPlaceType, nNewCorrectType));
        if (subPort < 0) {
            return -1;
        }
        this.mFECInfos.add(new EZFECInfo(subPort, nPlaceType, nNewCorrectType));
        return subPort;
    }

    public synchronized int createFEC(int nPlaceType, int nCorrect) {
        return this.createFECIn(nPlaceType, nCorrect);
    }

    public void setDisplay(int nSubPort, SurfaceHolder display) {
        Surface surface = null;
        if (null != display) {
            surface = display.getSurface();
            if (null == surface) {
                Log.e((String)"EZMediaPlayer", (String)"Surface null");
                return;
            }
            if (!surface.isValid()) {
                Log.e((String)"EZMediaPlayer", (String)"Surface Invalid");
                return;
            }
        }
        NativeApi.setFECWindow(this.mhMedia, nSubPort, surface);
    }

    public void setDisplayEx(int nSubPort, SurfaceTexture display) {
        if (this.mhMedia == 0L) {
            return;
        }
        Surface surface = display == null ? null : new Surface(display);
        NativeApi.setFECWindow(this.mhMedia, nSubPort, surface);
    }

    public void setSurface(int nSubPort, Surface surface) {
        if (this.mhMedia == 0L) {
            return;
        }
        NativeApi.setFECWindow(this.mhMedia, nSubPort, surface);
    }

    public synchronized void destroyFEC(int nSubPort) {
        NativeApi.deleteFECPort(this.mhMedia, nSubPort);
        for (int i = 0; i < this.mFECInfos.size(); ++i) {
            if (this.mFECInfos.get((int)i).nPlayPort != nSubPort) continue;
            this.mFECInfos.remove(i);
            break;
        }
    }

    public boolean setParamFEC(int nSubPort, int nUpdateType, float nZoom, float nWideScanOffset, float nPtzPositionX, float nPtzPositionY, float nRadiusLeft, float nRadiusTop, float nRadiusRight, float nRadiusBottom) {
        EZPTZParam ptzParam = new EZPTZParam();
        ptzParam.x = nPtzPositionX;
        ptzParam.y = nPtzPositionY;
        EZFECCYCLE_PARAM cycleParam = new EZFECCYCLE_PARAM();
        cycleParam.radiusLeft = nRadiusLeft;
        cycleParam.radiusRight = nRadiusRight;
        cycleParam.radiusTop = nRadiusTop;
        cycleParam.radiusBottom = nRadiusBottom;
        EZFISHEYE_PARAM stParam = new EZFISHEYE_PARAM();
        stParam.updateType = nUpdateType;
        stParam.zoom = nZoom;
        stParam.wideScanOffset = nWideScanOffset;
        stParam.ptzParam = ptzParam;
        stParam.cycleParam = cycleParam;
        return 0 == NativeApi.setFECFisheyeParam(this.mhMedia, nSubPort, stParam);
    }

    public boolean setParamFEC(int nSubPort, EZFISHEYE_PARAM stParam) {
        return 0 == NativeApi.setFECFisheyeParam(this.mhMedia, nSubPort, stParam);
    }

    public EZFISHEYE_PARAM getParamFEC(int nSubPort) {
        EZFISHEYE_PARAM stParam = new EZFISHEYE_PARAM();
        int ret = NativeApi.getFECFisheyeParam(this.mhMedia, nSubPort, stParam);
        return ret == 0 ? stParam : null;
    }

    public int getCurrentPTZPortFEC(int nPanorama, float fPositionX, float fPositionY) {
        return NativeApi.getFECCurrentPTZPort(this.mhMedia, nPanorama == 1, fPositionX, fPositionY);
    }

    public boolean setCurrentPTZPortFEC(int nSubPort) {
        return 0 == NativeApi.setFECCurrentPTZPort(this.mhMedia, nSubPort);
    }

    public boolean setPTZToWindow(int nSubPort, EZPTZParam stPTZRefOrigin, EZPTZParam stPTZRefWindow, EZPTZParam stPTZWindow, EZPTZParam stOutPTZ) {
        return 0 == NativeApi.ptzToWindow(this.mhMedia, nSubPort, stPTZRefOrigin, stPTZRefWindow, stPTZWindow, stOutPTZ);
    }

    public boolean setPTZoutLineShowMode(int mode) {
        return 0 == NativeApi.setFECPTZOutLineShowMode(this.mhMedia, mode);
    }

    public boolean setFish3DRotate(int nSubPort, EZFECTransformElement srTransParam) {
        return 0 == NativeApi.setFEC3DRotate(this.mhMedia, nSubPort, srTransParam);
    }

    public EZFECTransformElement getFish3DRotate(int nSubPort) {
        EZFECTransformElement stTransParam = new EZFECTransformElement();
        if (0 == NativeApi.getFEC3DRotate(this.mhMedia, nSubPort, stTransParam)) {
            return stTransParam;
        }
        return null;
    }

    public boolean setAnimation(int nSubPort, int nAnimationType, int nCurFrame, int nTotalFrames) {
        return 0 == NativeApi.setFECAnimation(this.mhMedia, nSubPort, nAnimationType, nCurFrame, nTotalFrames);
    }

    public boolean setFish3DRotateAbs(int nSubPort, EZFECTransformElement srTransParam) {
        return 0 == NativeApi.setFEC3DRotateABS(this.mhMedia, nSubPort, srTransParam);
    }

    public boolean setFECDisplayCB(int nSubPort, PlayerFECDisplayCB callback) {
        return 0 == NativeApi.setFECDisplayCallback(this.mhMedia, nSubPort, callback);
    }

    public boolean setFECPTZColor(int nSubPort, int alpha, int red, int green, int blue) {
        return 0 == NativeApi.setFECPTZColor(this.mhMedia, nSubPort, alpha, red, green, blue);
    }

    public boolean setEzvizSSLEffect(int nSubPort, boolean bOpen) {
        return 0 == NativeApi.setEzvizSSLEffect(this.mhMedia, nSubPort, bOpen);
    }

    public EZFECTransformElement getFish3DRotateSpecialView(int nSubPort) {
        EZFECTransformElement stTransParam = new EZFECTransformElement();
        if (0 == NativeApi.getFEC3DRotateSpecialViewInfo(this.mhMedia, nSubPort, 1, stTransParam)) {
            return stTransParam;
        }
        return null;
    }

    public int refreshFECPlay(int nSubPort) {
        return this.refreshFECPlay(nSubPort, 0);
    }

    public int refreshFECPlay(int nSubPort, int nStreamId) {
        return this.refreshFECPlay(nSubPort, 0, true);
    }

    public int refreshFECPlay(int nSubPort, int nStreamId, boolean checkRender) {
        return NativeApi.refreshFECPlay(this.mhMedia, nSubPort, nStreamId, checkRender);
    }

    static class EZFECInfo {
        int nPlayPort = -1;
        int nPlaceType = 0;
        int nCorrect = -1;

        public EZFECInfo(int nPlayPort, int nPlaceType, int nCorrect) {
            this.nPlayPort = nPlayPort;
            this.nPlaceType = nPlaceType;
            this.nCorrect = nCorrect;
        }
    }

    public static class EZPTZParam {
        public float x;
        public float y;
    }

    public static class EZFECCYCLE_PARAM {
        public float radiusLeft;
        public float radiusRight;
        public float radiusTop;
        public float radiusBottom;
    }

    public static class EZFISHEYE_PARAM {
        public int updateType;
        public int placeAndCorrect;
        public float zoom;
        public float wideScanOffset;
        public EZPTZParam ptzParam = new EZPTZParam();
        public EZFECCYCLE_PARAM cycleParam = new EZFECCYCLE_PARAM();
    }

    public static class EZFECTransformElement {
        public float fAxisX;
        public float fAxisY;
        public float fAxisZ;
        public float fValue;
    }

    public static interface PlayerFECDisplayCB {
        public void onFECDisplay(int var1, int var2);
    }
}


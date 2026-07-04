/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Color
 *  android.view.SurfaceHolder
 *  com.ez.player.EZFECMediaPlayer
 *  com.ez.player.EZFECMediaPlayer$EZFECTransformElement
 *  com.ez.player.EZFECMediaPlayer$EZFISHEYE_PARAM
 *  com.ez.player.EZFECMediaPlayer$EZPTZParam
 */
package com.videogo.stream;

import android.graphics.Color;
import android.view.SurfaceHolder;
import com.ez.player.EZFECMediaPlayer;
import com.videogo.openapi.ConfigLoader;
import com.videogo.openapi.EZConstants;
import com.videogo.stream.EZStreamBase;
import com.videogo.stream.EZStreamParamHelp;
import com.videogo.util.LogUtil;
import java.util.HashMap;

public abstract class EZFecStreamBase
extends EZStreamBase {
    protected final String TAG = "EZFecStreamBase";
    private static final int SUB_PORT_COUNT = 6;
    private static final float CENTER_POSITION = 0.5f;
    private static final float CENTER_POSITION_OFFSET = 0.4999f;
    private SurfaceHolder[] fecSurfaceHolders = new SurfaceHolder[6];
    private float[] fecFish3DRotateScales = new float[6];
    int selectedPtzIndex = -1;
    private int subPortCount;
    private int[] fecSubPorts = new int[6];
    private int[] fecPtzColors = new int[]{0, 0, 0, 0, 0, 0};
    public EZConstants.EZFecCorrectType fecCorrectType = EZConstants.EZFecCorrectType.EZ_FEC_CORRECT_NONE;
    public EZConstants.EZFecPlaceType fecPlaceType = EZConstants.EZFecPlaceType.EZ_FEC_PLACE_CEILING;
    private EZConstants.EZFecCorrectType inFecCorrectType = EZConstants.EZFecCorrectType.EZ_FEC_CORRECT_NONE;
    private EZConstants.EZFecPlaceType inFecPlaceType = EZConstants.EZFecPlaceType.EZ_FEC_PLACE_NONE;
    private HashMap<String, float[][]> fecMovePositionsMap = new HashMap();
    private float[] fecTouchScales = new float[]{1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f};
    private float[] fecTouchBaseScales = new float[]{1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f};
    private float[] fecTouchLastDistances = new float[]{1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f};
    private boolean[] fecTouchScalables = new boolean[6];
    private boolean[] fecTouchDraggables = new boolean[6];
    private boolean moveUp;
    private boolean moveLeft;
    private float xDistance = 0.0f;
    private float yDistance = 0.0f;
    private EZFECMediaPlayer.EZFISHEYE_PARAM fecParam;
    private EZFECMediaPlayer.EZPTZParam refPtzParam;
    private EZFECMediaPlayer.EZPTZParam currentPtzParam;
    private float currentOffSet = 0.0f;
    private int currentAnimatedIndex = -1;
    private Boolean[] animatedFlags = new Boolean[10];
    private boolean latFlag = false;
    private boolean scrollResultX = true;
    private boolean scrollResultY = true;
    boolean saveMovePosition = true;

    public EZFecStreamBase(EZStreamParamHelp streamParamHelp, ConfigLoader.PlayConfig playConfig) {
        super(streamParamHelp, playConfig);
    }

    public void setSurfaceHolds(SurfaceHolder[] shs) {
        if (shs == null || shs.length == 0) {
            LogUtil.d("EZFecStreamBase", "setSurfaceHolds shs's length must > 0");
            return;
        }
        int count = shs.length < 6 ? shs.length : 6;
        for (int i = 0; i < count; ++i) {
            this.fecSurfaceHolders[i] = shs[i];
        }
    }

    public void openFecCorrect(EZConstants.EZFecCorrectType fecCorrectType, EZConstants.EZFecPlaceType fecPlaceType, boolean reopen) {
        if (this.fecCorrectType != fecCorrectType || this.fecPlaceType != fecPlaceType || reopen) {
            this.fecCorrectType = fecCorrectType;
            this.fecPlaceType = fecPlaceType;
            this.setSubPortCount();
            for (int i = 0; i < 6; ++i) {
                this.fecTouchScalables[i] = this.isFecScalable();
                this.fecTouchDraggables[i] = this.isFecDraggable();
            }
            if (!reopen) {
                this.selectedPtzIndex = -1;
            }
            this.openFecCorrectInternal(fecCorrectType, fecPlaceType);
        }
    }

    private boolean openFecCorrectInternal(EZConstants.EZFecCorrectType fecCorrectType, EZConstants.EZFecPlaceType fecPlaceType) {
        if (this.mediaPlayer == null) {
            return false;
        }
        if (this.inFecCorrectType == fecCorrectType && this.inFecPlaceType == fecPlaceType) {
            return true;
        }
        this.inFecCorrectType = fecCorrectType;
        this.inFecPlaceType = fecPlaceType;
        if (fecCorrectType == EZConstants.EZFecCorrectType.EZ_FEC_CORRECT_NONE || fecPlaceType == EZConstants.EZFecPlaceType.EZ_FEC_PLACE_NONE) {
            this.closeFecCorrect(false);
            return true;
        }
        this.destroyFecSubPorts();
        return this.createFecSubPorts(fecCorrectType, fecPlaceType);
    }

    public void closeFecCorrect(boolean stopPlay) {
        this.inFecCorrectType = EZConstants.EZFecCorrectType.EZ_FEC_CORRECT_NONE;
        this.inFecPlaceType = EZConstants.EZFecPlaceType.EZ_FEC_PLACE_NONE;
        if (this.mediaPlayer == null) {
            return;
        }
        this.destroyFecSubPorts();
        if (stopPlay) {
            // empty if block
        }
    }

    private boolean createFecSubPorts(EZConstants.EZFecCorrectType fecCorrectType, EZConstants.EZFecPlaceType fecPlaceType) {
        if (this.mediaPlayer == null || this.fecSubPorts[0] != -1) {
            return true;
        }
        LogUtil.d("EZFecStreamBase", String.format("createFEC fecCorrectType=%d fecPlaceType=%d", fecCorrectType.fecCorrectType, fecPlaceType.fecPlaceType));
        EZFECMediaPlayer fecMediaPlayer = (EZFECMediaPlayer)this.mediaPlayer;
        int placeType = this.fecPlaceTypeTransfrom(fecPlaceType);
        int correctType = this.fecCorrectTypeTransFrom(fecCorrectType);
        boolean createResult = true;
        switch (fecCorrectType) {
            case EZ_FEC_CORRECT_4PTZ: {
                float[][] fecMovePositions = this.getFecMovePositions(fecCorrectType, fecPlaceType);
                for (int i = 0; i < 4; ++i) {
                    this.fecSubPorts[i] = fecMediaPlayer.createFEC(placeType, correctType);
                    if (this.fecSubPorts[i] != -1) {
                        boolean b = fecMediaPlayer.setParamFEC(this.fecSubPorts[i], 8, 0.0f, 0.0f, fecMovePositions[i][0], fecMovePositions[i][1], 0.0f, 0.0f, 0.0f, 0.0f);
                        if (!b) {
                            LogUtil.d("EZFecStreamBase", "setParamFEC " + i + " return fail");
                        }
                        this.setSurfaceHolder(i, this.fecSurfaceHolders[i], true);
                        continue;
                    }
                    createResult = false;
                }
                break;
            }
            case EZ_FEC_CORRECT_5PTZ: {
                float[][] fecMovePositions = this.getFecMovePositions(fecCorrectType, fecPlaceType);
                for (int i = 0; i < 5; ++i) {
                    this.fecSubPorts[i] = fecMediaPlayer.createFEC(placeType, correctType);
                    if (this.fecSubPorts[i] != -1) {
                        boolean b = fecMediaPlayer.setParamFEC(this.fecSubPorts[i], 8, 0.0f, 0.0f, fecMovePositions[i][0], fecMovePositions[i][1], 0.0f, 0.0f, 0.0f, 0.0f);
                        if (!b) {
                            LogUtil.d("EZFecStreamBase", "setParamFEC " + i + " return fail");
                        }
                        this.setSurfaceHolder(i, this.fecSurfaceHolders[i], true);
                        continue;
                    }
                    createResult = false;
                }
                break;
            }
            case EZ_FEC_CORRECT_FULL5PTZ: {
                float[][] fecMovePositions = this.getFecMovePositions(fecCorrectType, fecPlaceType);
                for (int i = 0; i < 6; ++i) {
                    this.fecSubPorts[i] = fecMediaPlayer.createFEC(placeType, i == 0 ? 11 : correctType);
                    if (this.fecSubPorts[i] != -1) {
                        boolean b;
                        if (i > 0 && !(b = fecMediaPlayer.setParamFEC(this.fecSubPorts[i], 8, 0.0f, 0.0f, fecMovePositions[i - 1][0], fecMovePositions[i - 1][1], 0.0f, 0.0f, 0.0f, 0.0f))) {
                            LogUtil.d("EZFecStreamBase", "setParamFEC " + i + " return fail");
                        }
                        this.setSurfaceHolder(i, this.fecSurfaceHolders[i], true);
                        if (i <= 0 || this.setFecPtzColor(i, this.fecPtzColors[i])) continue;
                        LogUtil.d("EZFecStreamBase", String.format("setFecPtzColor %d return fail", i));
                        continue;
                    }
                    createResult = false;
                }
                boolean b = fecMediaPlayer.setPTZoutLineShowMode(2);
                if (b) break;
                LogUtil.d("EZFecStreamBase", "setPTZLineShowMode return fail");
                break;
            }
            case EZ_FEC_CORRECT_CYC: {
                this.fecSubPorts[0] = fecMediaPlayer.createFEC(placeType, correctType);
                if (this.fecSubPorts[0] == -1) {
                    createResult = false;
                }
                this.resetFish3DRotate(0, false);
                this.setSurfaceHolder(0, this.fecSurfaceHolders[0], true);
                break;
            }
            case EZ_FEC_CORRECT_ARC_VER: {
                this.fecSubPorts[0] = fecMediaPlayer.createFEC(placeType, correctType);
                if (this.fecSubPorts[0] == -1) {
                    createResult = false;
                }
                this.setSurfaceHolder(0, this.fecSurfaceHolders[0], true);
                break;
            }
            case EZ_FEC_CORRECT_ARC_HOR: 
            case EZ_FEC_CORRECT_WIDEANGLE: {
                this.fecSubPorts[0] = fecMediaPlayer.createFEC(placeType, correctType);
                if (this.fecSubPorts[0] == -1) {
                    createResult = false;
                }
                this.resetFish3DRotate(0, correctType == 8);
                this.setSurfaceHolder(0, this.fecSurfaceHolders[0], true);
                break;
            }
            case EZ_FEC_CORRECT_LAT: {
                this.fecSubPorts[0] = fecMediaPlayer.createFEC(placeType, 3);
                if (this.fecSubPorts[0] == -1) {
                    createResult = false;
                }
                this.setSurfaceHolder(0, this.fecSurfaceHolders[0], true);
                break;
            }
            case EZ_FEC_CORRECT_180: 
            case EZ_FEC_CORRECT_360: {
                this.fecSubPorts[0] = fecMediaPlayer.createFEC(placeType, correctType);
                if (this.fecSubPorts[0] == -1) {
                    createResult = false;
                }
                this.setSurfaceHolder(0, this.fecSurfaceHolders[0], true);
            }
        }
        return createResult;
    }

    private void setSurfaceHolder(int index, SurfaceHolder sh, boolean force) {
        if (this.mediaPlayer != null && this.fecSubPorts[index] != -1 && (force || this.fecSurfaceHolders[index] != sh)) {
            if (this.fecSurfaceHolders[index] != null) {
                ((EZFECMediaPlayer)this.mediaPlayer).setDisplay(this.fecSubPorts[index], null);
            }
            if (sh != null) {
                ((EZFECMediaPlayer)this.mediaPlayer).setDisplay(this.fecSubPorts[index], sh);
            }
            if (this.checkAllFecSurfaceSet()) {
                this.mediaPlayer.refreshPlay();
            }
        }
    }

    private void destroyFecSubPorts() {
        if (this.fecSubPorts[0] != -1 && !((EZFECMediaPlayer)this.mediaPlayer).setPTZoutLineShowMode(0)) {
            LogUtil.d("EZFecStreamBase", "setPTZLineShowMode return fail");
        }
        for (int i = 0; i < this.fecSubPorts.length; ++i) {
            int value = this.fecSubPorts[i];
            if (value == -1) continue;
            LogUtil.d("EZFecStreamBase", "destroyFEC index = " + i);
            ((EZFECMediaPlayer)this.mediaPlayer).setDisplay(value, null);
            ((EZFECMediaPlayer)this.mediaPlayer).destroyFEC(value);
            this.fecSubPorts[i] = -1;
        }
    }

    private float[][] getFecMovePositions(EZConstants.EZFecCorrectType fecCorrectType, EZConstants.EZFecPlaceType fecPlaceType) {
        int ptzPortCount = 0;
        if (fecCorrectType == EZConstants.EZFecCorrectType.EZ_FEC_CORRECT_4PTZ) {
            ptzPortCount = 4;
        } else if (fecCorrectType == EZConstants.EZFecCorrectType.EZ_FEC_CORRECT_5PTZ || fecCorrectType == EZConstants.EZFecCorrectType.EZ_FEC_CORRECT_FULL5PTZ) {
            ptzPortCount = 5;
        }
        int ptzGroup = 0;
        if (fecCorrectType == EZConstants.EZFecCorrectType.EZ_FEC_CORRECT_4PTZ) {
            ptzGroup = 1;
        } else if (fecCorrectType == EZConstants.EZFecCorrectType.EZ_FEC_CORRECT_5PTZ || fecCorrectType == EZConstants.EZFecCorrectType.EZ_FEC_CORRECT_FULL5PTZ) {
            ptzGroup = 2;
        }
        String mapKey = this.streamParamHelp.deviceSerial + "-" + this.streamParamHelp.cameraNo + "-" + ptzGroup;
        float[][] fecMovePositions = this.fecMovePositionsMap.get(mapKey);
        if (fecMovePositions == null || fecMovePositions.length != ptzPortCount) {
            float startDegree = 0.0f;
            if (ptzPortCount == 4) {
                startDegree = 180.0f;
            } else if (ptzPortCount == 5) {
                startDegree = 90.0f;
            }
            float offsetPosition = 0.2f;
            float degree = 360.0f / (float)ptzPortCount;
            double radius = (double)offsetPosition / Math.cos(Math.toRadians(degree / 2.0f));
            fecMovePositions = new float[ptzPortCount][2];
            for (int i = 0; i < ptzPortCount; ++i) {
                double currentDegree = startDegree - degree / 2.0f - degree * (float)i;
                fecMovePositions[i] = new float[]{(float)(0.5 + radius * Math.cos(Math.toRadians(currentDegree))), (float)(0.5 - radius * Math.sin(Math.toRadians(currentDegree)))};
            }
            this.fecMovePositionsMap.put(mapKey, fecMovePositions);
        }
        return fecMovePositions;
    }

    public boolean isOpen() {
        return this.mediaPlayer != null && this.fecSubPorts[0] != -1;
    }

    public boolean isFecScalable() {
        return this.fecCorrectType == EZConstants.EZFecCorrectType.EZ_FEC_CORRECT_ARC_HOR || this.fecCorrectType == EZConstants.EZFecCorrectType.EZ_FEC_CORRECT_ARC_VER || this.fecCorrectType == EZConstants.EZFecCorrectType.EZ_FEC_CORRECT_4PTZ || this.fecCorrectType == EZConstants.EZFecCorrectType.EZ_FEC_CORRECT_CYC || this.fecCorrectType == EZConstants.EZFecCorrectType.EZ_FEC_CORRECT_WIDEANGLE;
    }

    public boolean isFecDraggable() {
        return this.fecCorrectType == EZConstants.EZFecCorrectType.EZ_FEC_CORRECT_ARC_HOR || this.fecCorrectType == EZConstants.EZFecCorrectType.EZ_FEC_CORRECT_ARC_VER || this.fecCorrectType == EZConstants.EZFecCorrectType.EZ_FEC_CORRECT_4PTZ || this.fecCorrectType == EZConstants.EZFecCorrectType.EZ_FEC_CORRECT_CYC || this.fecCorrectType == EZConstants.EZFecCorrectType.EZ_FEC_CORRECT_180 || this.fecCorrectType == EZConstants.EZFecCorrectType.EZ_FEC_CORRECT_360 || this.fecCorrectType == EZConstants.EZFecCorrectType.EZ_FEC_CORRECT_5PTZ || this.fecCorrectType == EZConstants.EZFecCorrectType.EZ_FEC_CORRECT_FULL5PTZ || this.fecCorrectType == EZConstants.EZFecCorrectType.EZ_FEC_CORRECT_WIDEANGLE;
    }

    public boolean isFecPtz(int index) {
        if (this.mediaPlayer == null || this.fecSubPorts[index] == -1) {
            return false;
        }
        switch (this.fecCorrectType) {
            case EZ_FEC_CORRECT_4PTZ: {
                if (index < 0 || index > 3) break;
                return true;
            }
            case EZ_FEC_CORRECT_5PTZ: {
                if (index < 0 || index > 4) break;
                return true;
            }
            case EZ_FEC_CORRECT_FULL5PTZ: {
                if (index < 1 || index > 5) break;
                return true;
            }
            default: {
                return false;
            }
        }
        return false;
    }

    public boolean isFecScalable(int index) {
        return this.fecTouchScalables[index];
    }

    public void setFecScalable(int index, boolean scalable) {
        this.fecTouchScalables[index] = scalable;
    }

    public boolean isFecDraggable(int index) {
        return this.fecTouchDraggables[index];
    }

    public void setFecDraggable(int index, boolean draggable) {
        this.fecTouchDraggables[index] = draggable;
    }

    private void clearAllPortColor() {
        if (this.mediaPlayer == null) {
            return;
        }
        EZFECMediaPlayer fecMediaPlayer = (EZFECMediaPlayer)this.mediaPlayer;
        for (int i = 0; i < this.fecSubPorts.length; ++i) {
            if (this.fecSubPorts[i] == -1) continue;
            fecMediaPlayer.setFECPTZColor(this.fecSubPorts[i], 0, 255, 255, 255);
        }
    }

    private void setSubPortCount() {
        switch (this.fecCorrectType) {
            case EZ_FEC_CORRECT_NONE: {
                this.subPortCount = 0;
                break;
            }
            case EZ_FEC_CORRECT_4PTZ: {
                this.subPortCount = 4;
                break;
            }
            case EZ_FEC_CORRECT_5PTZ: {
                this.subPortCount = 5;
                break;
            }
            case EZ_FEC_CORRECT_FULL5PTZ: {
                this.subPortCount = 6;
                break;
            }
            default: {
                this.subPortCount = 1;
            }
        }
    }

    private boolean checkAllFecSurfaceSet() {
        for (int i = 0; i < this.subPortCount; ++i) {
            if (this.fecSurfaceHolders != null) continue;
            return false;
        }
        return true;
    }

    public boolean onFecTouchDown(int index, float x, float y) {
        if (this.mediaPlayer == null || this.fecSubPorts[index] == -1) {
            return false;
        }
        LogUtil.d("EZFecStreamBase", String.format("onFecTouchDown index:%d,x=%f,y=%f", index, Float.valueOf(x), Float.valueOf(y)));
        this.scrollResultX = true;
        this.scrollResultY = true;
        this.xDistance = x;
        this.yDistance = y;
        block0 : switch (this.fecCorrectType) {
            case EZ_FEC_CORRECT_4PTZ: 
            case EZ_FEC_CORRECT_5PTZ: {
                this.refPtzParam = this.getPtzPosition(index, x, y);
                this.getFecParam(index);
                break;
            }
            case EZ_FEC_CORRECT_LAT: 
            case EZ_FEC_CORRECT_180: 
            case EZ_FEC_CORRECT_360: {
                this.getFecParam(index);
                break;
            }
            case EZ_FEC_CORRECT_FULL5PTZ: {
                if (index <= 0) break;
                this.refPtzParam = this.getPtzPosition(index, x, y);
                this.getFecParam(index);
                this.clearAllPortColor();
                EZFECMediaPlayer fecMediaPlayer = (EZFECMediaPlayer)this.mediaPlayer;
                if (index == this.selectedPtzIndex) {
                    this.selectedPtzIndex = -1;
                    return false;
                }
                this.selectedPtzIndex = index;
                int color = Color.rgb((int)255, (int)92, (int)92);
                fecMediaPlayer.setFECPTZColor(this.fecSubPorts[index], Color.alpha((int)color), Color.red((int)color), Color.green((int)color), Color.blue((int)color));
                break;
            }
            case EZ_FEC_CORRECT_CYC: 
            case EZ_FEC_CORRECT_ARC_HOR: 
            case EZ_FEC_CORRECT_WIDEANGLE: {
                break;
            }
            default: {
                int selectedPtzSubPort = ((EZFECMediaPlayer)this.mediaPlayer).getCurrentPTZPortFEC(0, x / (float)this.fecSurfaceHolders[index].getSurfaceFrame().width(), y / (float)this.fecSurfaceHolders[index].getSurfaceFrame().height());
                for (int i = 0; i < this.fecSubPorts.length; ++i) {
                    if (this.fecSubPorts[i] != selectedPtzSubPort) continue;
                    this.selectedPtzIndex = i;
                    break block0;
                }
            }
        }
        return true;
    }

    public void onFecTouchMove(int index, float x, float y) {
        if (this.mediaPlayer == null || this.fecSubPorts[index] == -1 || !this.isFecDraggable(index)) {
            return;
        }
        LogUtil.d("EZFecStreamBase", String.format("onFecTouchMove index:%d,x=%f,y=%f", index, Float.valueOf(x), Float.valueOf(y)));
        EZFECMediaPlayer fecMediaPlayer = (EZFECMediaPlayer)this.mediaPlayer;
        switch (this.fecCorrectType) {
            case EZ_FEC_CORRECT_LAT: 
            case EZ_FEC_CORRECT_180: 
            case EZ_FEC_CORRECT_360: {
                this.setFecMoveOffset(index, x, 360.0f);
                break;
            }
            case EZ_FEC_CORRECT_4PTZ: 
            case EZ_FEC_CORRECT_5PTZ: {
                this.setFecMovePosition(index, x, y);
                break;
            }
            case EZ_FEC_CORRECT_FULL5PTZ: {
                float ptzY;
                if (index != 0 || this.selectedPtzIndex <= 0) break;
                int selectedPtzSubPort = this.fecSubPorts[this.selectedPtzIndex];
                float ptzX = x / (float)this.fecSurfaceHolders[index].getSurfaceFrame().width();
                boolean b = fecMediaPlayer.setParamFEC(selectedPtzSubPort, 8, 0.0f, 0.0f, ptzX, ptzY = y / (float)this.fecSurfaceHolders[index].getSurfaceFrame().height(), 0.0f, 0.0f, 0.0f, 0.0f);
                if (b) {
                    int ptzIndex;
                    if (!this.saveMovePosition || (ptzIndex = this.selectedPtzIndex) <= -1) break;
                    float[][] fecMovePositions = this.getFecMovePositions(this.fecCorrectType, this.fecPlaceType);
                    fecMovePositions[ptzIndex - 1][0] = ptzX;
                    fecMovePositions[ptzIndex - 1][1] = ptzY;
                    break;
                }
                LogUtil.d("EZFecStreamBase", "onFecTouchMove setParamFEC return fail");
                break;
            }
            case EZ_FEC_CORRECT_ARC_VER: 
            case EZ_FEC_CORRECT_ARC_HOR: 
            case EZ_FEC_CORRECT_WIDEANGLE: {
                this.setFecTouchScroll(index, x, y, false);
                break;
            }
            case EZ_FEC_CORRECT_CYC: {
                this.setFecTouchScroll(index, x, y, true);
                break;
            }
            default: {
                int selectedPtzSubPort = this.fecSubPorts[this.selectedPtzIndex];
                float ptzX = x / (float)this.fecSurfaceHolders[index].getSurfaceFrame().width();
                float ptzY = y / (float)this.fecSurfaceHolders[index].getSurfaceFrame().height();
                boolean b = fecMediaPlayer.setParamFEC(selectedPtzSubPort, 8, 0.0f, 0.0f, ptzX, ptzY, 0.0f, 0.0f, 0.0f, 0.0f);
                if (b) {
                    fecMediaPlayer.refreshPlay(0);
                    break;
                }
                LogUtil.d("EZFecStreamBase", "onFecTouchMove setParamFEC return fail");
            }
        }
    }

    public void onFecTouchUp(int index) {
        if (this.mediaPlayer == null || this.fecSubPorts[index] == -1) {
            return;
        }
    }

    public void onFecTouchStartScale(int index, float distance) {
        if (this.mediaPlayer == null || this.fecSubPorts[index] == -1 || !this.isFecScalable(index)) {
            return;
        }
        this.fecTouchBaseScales[index] = this.fecTouchScales[index];
        this.fecTouchLastDistances[index] = distance;
    }

    public void onFecTouchScale(int index, float scale, float maxScale) {
        if (this.mediaPlayer == null || this.fecSubPorts[index] == -1 || !this.isFecScalable(index)) {
            return;
        }
        float preScale = this.fecTouchScales[index];
        float scaleFactor = scale * this.fecTouchBaseScales[index];
        if (preScale != scaleFactor) {
            this.fecTouchScales[index] = scaleFactor;
            if (this.fecCorrectType == EZConstants.EZFecCorrectType.EZ_FEC_CORRECT_CYC) {
                float fValue = (preScale / this.fecTouchBaseScales[index] - scale) * 3.0f;
                this.onFecTouchScaleInternal(index, fValue, maxScale);
            } else if (this.fecCorrectType == EZConstants.EZFecCorrectType.EZ_FEC_CORRECT_ARC_HOR || this.fecCorrectType == EZConstants.EZFecCorrectType.EZ_FEC_CORRECT_ARC_VER || this.fecCorrectType == EZConstants.EZFecCorrectType.EZ_FEC_CORRECT_WIDEANGLE) {
                this.onFecTouchScaleInternal(index, scaleFactor / preScale, maxScale);
            } else {
                this.onFecTouchScaleInternal(index, scaleFactor, maxScale);
            }
        }
    }

    private void onFecTouchScaleInternal(int index, float scaleFactor, float maxScale) {
        if (this.mediaPlayer == null || !this.isFecScalable(index) || this.fecSubPorts[index] == -1) {
            return;
        }
        EZFECMediaPlayer fecMediaPlayer = (EZFECMediaPlayer)this.mediaPlayer;
        switch (this.fecCorrectType) {
            case EZ_FEC_CORRECT_4PTZ: 
            case EZ_FEC_CORRECT_5PTZ: 
            case EZ_FEC_CORRECT_FULL5PTZ: {
                float scale = Math.min(scaleFactor, maxScale);
                scale = (scale - 1.0f) / maxScale;
                LogUtil.d("EZFecStreamBase", "setFecScale setParamFEC " + scaleFactor + " " + maxScale + " " + scale);
                boolean b = fecMediaPlayer.setParamFEC(this.fecSubPorts[index], 2, scale, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
                if (b) break;
                LogUtil.d("EZFecStreamBase", "setFecScale setParamFEC return fail");
                break;
            }
            case EZ_FEC_CORRECT_CYC: {
                EZFECMediaPlayer.EZFECTransformElement p = fecMediaPlayer.getFish3DRotate(this.fecSubPorts[index]);
                float fValue = 0.0f;
                fValue = scaleFactor > 0.0f ? Math.min(scaleFactor, 14.0f - p.fValue) : Math.max(scaleFactor, 2.5f - p.fValue);
                if (fValue == 0.0f) break;
                EZFECMediaPlayer.EZFECTransformElement srTransParam = new EZFECMediaPlayer.EZFECTransformElement();
                srTransParam.fValue = fValue;
                LogUtil.i("EZFecStreamBase", "onFecTouchScale sem cyc fValue = " + fValue);
                boolean b = fecMediaPlayer.setFish3DRotate(this.fecSubPorts[index], srTransParam);
                if (b) break;
                LogUtil.d("EZFecStreamBase", "onFECTouchScale setFish3DRotate return fail");
                break;
            }
            default: {
                LogUtil.d("EZFecStreamBase", String.format("onFecTouchScale index:%d,scaleFactor:%f", index, Float.valueOf(scaleFactor)));
                float factor = (1.0f - scaleFactor) * 2.0f;
                EZFECMediaPlayer.EZFECTransformElement p = fecMediaPlayer.getFish3DRotate(this.fecSubPorts[index]);
                if (p == null) {
                    return;
                }
                LogUtil.i("EZFecStreamBase", "onFecTouchScale srtransElement.fValue = " + p.fValue);
                if (p.fValue >= maxScale && factor > 0.0f) {
                    return;
                }
                EZFECMediaPlayer.EZFECTransformElement srTransParam = new EZFECMediaPlayer.EZFECTransformElement();
                if (p.fValue + factor > maxScale) {
                    factor = maxScale - p.fValue;
                }
                srTransParam.fValue = factor;
                if (fecMediaPlayer.setFish3DRotate(this.fecSubPorts[index], srTransParam)) break;
                LogUtil.d("EZFecStreamBase", "onFecTouchScale setFish3DRotate return fail");
            }
        }
    }

    private void setFecTouchScroll(int index, float x, float y, boolean reverse) {
        float dx = 0.0f;
        dx = reverse ? this.xDistance - x : x - this.xDistance;
        float dy = 0.0f;
        dy = reverse ? this.yDistance - y : y - this.yDistance;
        this.xDistance = x;
        this.yDistance = y;
        this.onFecTouchScroll(index, dx, this.fecCorrectType == EZConstants.EZFecCorrectType.EZ_FEC_CORRECT_CYC ? 0.0f : dy);
    }

    private void onFecTouchScroll(int index, float dx, float dy) {
        LogUtil.d("EZFecStreamBase", String.format("onFecTouchScroll index:%d,dx:%f,dy:%f", index, Float.valueOf(dx), Float.valueOf(dy)));
        EZFECMediaPlayer fecMediaPlayer = (EZFECMediaPlayer)this.mediaPlayer;
        EZFECMediaPlayer.EZFECTransformElement srTransParam = new EZFECMediaPlayer.EZFECTransformElement();
        srTransParam.fAxisX = dy / 400.0f;
        this.scrollResultX = fecMediaPlayer.setFish3DRotate(this.fecSubPorts[index], srTransParam);
        srTransParam.fAxisX = 0.0f;
        srTransParam.fAxisY = dx / 400.0f;
        this.scrollResultY = fecMediaPlayer.setFish3DRotate(this.fecSubPorts[index], srTransParam);
        this.moveUp = dy > 0.0f;
        boolean bl = this.moveLeft = dx > 0.0f;
        if (!this.scrollResultX || !this.scrollResultY) {
            LogUtil.d("EZFecStreamBase", "onFecTouchScroll setFish3DRotate return fail");
        }
    }

    private void setFecMoveOffset(int index, float x, float maxOffSet) {
        float offSet;
        if (this.fecParam == null) {
            this.getFecParam(index);
        }
        if (this.currentOffSet == (offSet = this.getFecOffset(index, x, maxOffSet))) {
            LogUtil.e("EZFecStreamBase", "setFecMoveOffset offset return");
            return;
        }
        if (this.fecParam != null && this.fecSubPorts[index] != -1) {
            this.currentOffSet = offSet;
            LogUtil.d("EZFecStreamBase", "setFecMoveOffset offset:" + offSet);
            EZFECMediaPlayer fecMediaPlayer = (EZFECMediaPlayer)this.mediaPlayer;
            boolean b = fecMediaPlayer.setParamFEC(this.fecSubPorts[index], 4, 0.0f, this.currentOffSet, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
            if (b) {
                fecMediaPlayer.refreshPlay();
            } else {
                LogUtil.d("EZFecStreamBase", "setFecMoveOffset setParamFEC return fail");
            }
        }
    }

    private void setFecMovePosition(int index, float x, float y) {
        if (this.fecSurfaceHolders[index].getSurfaceFrame().width() == 0 || this.fecSurfaceHolders[index].getSurfaceFrame().height() == 0 || this.refPtzParam == null || this.fecSubPorts[index] == -1) {
            return;
        }
        EZFECMediaPlayer.EZPTZParam ptzParam = this.getPtzPosition(index, x, y);
        if (this.currentPtzParam != null && Math.abs(this.currentPtzParam.x - ptzParam.x) < 0.1f && Math.abs(this.currentPtzParam.y - ptzParam.y) < 0.1f) {
            LogUtil.e("EZFecStreamBase", "setFecMovePosition position return. index = " + index);
            return;
        }
        this.currentPtzParam = ptzParam;
        EZFECMediaPlayer fecMediaPlayer = (EZFECMediaPlayer)this.mediaPlayer;
        if (!fecMediaPlayer.setCurrentPTZPortFEC(this.fecSubPorts[index])) {
            LogUtil.d("EZFecStreamBase", "setFecMovePosition setCurrentPTZPortFEC return fail");
        }
        if (this.fecParam == null) {
            this.getFecParam(index);
        }
        if (this.fecParam != null) {
            boolean b;
            LogUtil.d("EZFecStreamBase", index + " setFecMovePosition x:" + this.currentPtzParam.x + ", y:" + this.currentPtzParam.y);
            EZFECMediaPlayer.EZPTZParam outPtzParam = new EZFECMediaPlayer.EZPTZParam();
            if (!fecMediaPlayer.setPTZToWindow(this.fecSubPorts[index], this.fecParam.ptzParam, this.refPtzParam, this.currentPtzParam, outPtzParam)) {
                LogUtil.d("EZFecStreamBase", "setFecMovePosition setPTZToWindow return fail");
            }
            if (b = fecMediaPlayer.setParamFEC(this.fecSubPorts[index], 8, 0.0f, 0.0f, outPtzParam.x, outPtzParam.y, 0.0f, 0.0f, 0.0f, 0.0f)) {
                if (this.saveMovePosition) {
                    float[][] fecMovePositions = this.getFecMovePositions(this.fecCorrectType, this.fecPlaceType);
                    if (this.fecCorrectType == EZConstants.EZFecCorrectType.EZ_FEC_CORRECT_FULL5PTZ) {
                        fecMovePositions[index - 1][0] = outPtzParam.x;
                        fecMovePositions[index - 1][1] = outPtzParam.y;
                    } else {
                        fecMovePositions[index][0] = outPtzParam.x;
                        fecMovePositions[index][1] = outPtzParam.y;
                    }
                }
            } else {
                LogUtil.d("EZFecStreamBase", "setFecMovePosition setParamFEC return fail");
            }
        }
    }

    private float getFecOffset(int index, float x, float maxOffSet) {
        if (this.fecSurfaceHolders[index].getSurfaceFrame().width() == 0 || x == this.xDistance || this.fecParam == null) {
            return this.currentOffSet;
        }
        float d = x - this.xDistance;
        return ((this.fecParam != null ? this.fecParam.wideScanOffset : 0.0f) + maxOffSet / (float)this.fecSurfaceHolders[index].getSurfaceFrame().width() * d) % maxOffSet;
    }

    private EZFECMediaPlayer.EZPTZParam getPtzPosition(int index, float x, float y) {
        int width = this.fecSurfaceHolders[index].getSurfaceFrame().width();
        int height = this.fecSurfaceHolders[index].getSurfaceFrame().height();
        EZFECMediaPlayer.EZPTZParam ezptzParam = new EZFECMediaPlayer.EZPTZParam();
        if (width == 0 || height == 0) {
            return ezptzParam;
        }
        ezptzParam.x = x / (float)width;
        ezptzParam.y = y / (float)height;
        ezptzParam.x = Math.min(1.0f, Math.max(0.0f, ezptzParam.x));
        ezptzParam.y = Math.min(1.0f, Math.max(0.0f, ezptzParam.y));
        return ezptzParam;
    }

    private void getFecParam(int index) {
        if (this.mediaPlayer == null || this.fecSubPorts[index] == -1) {
            return;
        }
        EZFECMediaPlayer fecMediaPlayer = (EZFECMediaPlayer)this.mediaPlayer;
        this.fecParam = fecMediaPlayer.getParamFEC(this.fecSubPorts[index]);
        if ((this.fecCorrectType == EZConstants.EZFecCorrectType.EZ_FEC_CORRECT_4PTZ || this.fecCorrectType == EZConstants.EZFecCorrectType.EZ_FEC_CORRECT_5PTZ || this.fecCorrectType == EZConstants.EZFecCorrectType.EZ_FEC_CORRECT_FULL5PTZ) && this.fecParam != null && this.fecParam.ptzParam.x == 0.5f && this.fecParam.ptzParam.y == 0.5f) {
            LogUtil.e("EZFecStreamBase", "getFecParam setParamFEC RADIUS_4PTZ_OFFSET");
            boolean b = fecMediaPlayer.setParamFEC(this.fecSubPorts[index], 8, this.fecParam.zoom, this.fecParam.wideScanOffset, 0.4999f, 0.4999f, this.fecParam.cycleParam.radiusLeft, this.fecParam.cycleParam.radiusTop, this.fecParam.cycleParam.radiusRight, this.fecParam.cycleParam.radiusBottom);
            if (!b) {
                LogUtil.d("EZFecStreamBase", "getFecParam setParamFEC return fail");
            }
            this.fecParam = fecMediaPlayer.getParamFEC(this.fecSubPorts[index]);
        }
    }

    private boolean setFecPtzColor(int index, int color) {
        LogUtil.d("EZFecStreamBase", String.format("setFecPtzColor index=%d, color=%d", index, color));
        this.fecPtzColors[index] = color;
        if (this.mediaPlayer == null || this.fecSubPorts[index] == -1) {
            return false;
        }
        EZFECMediaPlayer fecMediaPlayer = (EZFECMediaPlayer)this.mediaPlayer;
        LogUtil.d("EZFecStreamBase", String.format("setFecPtzColor inner index=%d, color=%d", index, color));
        if (Color.alpha((int)color) == 0) {
            return fecMediaPlayer.setFECPTZColor(this.fecSubPorts[index], 0, 255, 255, 255);
        }
        fecMediaPlayer.setFECPTZColor(this.fecSubPorts[index], Color.alpha((int)color), Color.red((int)color), Color.green((int)color), Color.blue((int)color));
        return true;
    }

    private boolean clearFecPtzColor() {
        boolean result = true;
        for (int i = 0; i < this.fecPtzColors.length; ++i) {
            if (this.fecPtzColors[i] == 0) continue;
            result &= this.setFecPtzColor(i, 0);
        }
        return result;
    }

    private void resetFish3DRotate(int index, boolean animated) {
        EZFECMediaPlayer.EZFECTransformElement initFecParam;
        for (int i = 0; i < this.animatedFlags.length; ++i) {
            this.animatedFlags[i] = false;
        }
        if (this.fecCorrectType != EZConstants.EZFecCorrectType.EZ_FEC_CORRECT_CYC && this.fecCorrectType != EZConstants.EZFecCorrectType.EZ_FEC_CORRECT_ARC_HOR && this.fecCorrectType != EZConstants.EZFecCorrectType.EZ_FEC_CORRECT_WIDEANGLE) {
            this.resetFish3DRotateOld(index);
            return;
        }
        if (this.mediaPlayer == null || this.fecSubPorts[index] == -1) {
            return;
        }
        EZFECMediaPlayer fecMediaPlayer = (EZFECMediaPlayer)this.mediaPlayer;
        int width = this.fecSurfaceHolders[index].getSurfaceFrame().width();
        int height = this.fecSurfaceHolders[index].getSurfaceFrame().height();
        this.fecFish3DRotateScales[index] = width > 0 ? (float)height / (float)width : 0.0f;
        boolean isSquare = Math.abs(width - height) < 50;
        switch (this.fecCorrectType) {
            case EZ_FEC_CORRECT_CYC: {
                initFecParam = new EZFECMediaPlayer.EZFECTransformElement();
                initFecParam.fValue = 4.5f + (isSquare ? 0.5f : 0.0f);
                initFecParam.fAxisX = 0.0f;
                break;
            }
            case EZ_FEC_CORRECT_ARC_HOR: {
                initFecParam = new EZFECMediaPlayer.EZFECTransformElement();
                initFecParam.fValue = 1.8f + (isSquare ? 0.3f : 0.0f);
                initFecParam.fAxisX = 0.0f;
                break;
            }
            case EZ_FEC_CORRECT_WIDEANGLE: {
                initFecParam = new EZFECMediaPlayer.EZFECTransformElement();
                initFecParam.fValue = 0.7f + (isSquare ? 0.3f : 0.0f);
                initFecParam.fAxisX = 0.0f;
                break;
            }
            default: {
                initFecParam = new EZFECMediaPlayer.EZFECTransformElement();
            }
        }
        LogUtil.i("EZFecStreamBase", "resetFish3DRotate setFish3DRotateAbs");
        if (!fecMediaPlayer.setFish3DRotateAbs(this.fecSubPorts[index], initFecParam)) {
            LogUtil.d("EZFecStreamBase", "setFish3DRotateAbs return fail");
        }
        if (animated) {
            this.onFecTouchScaleInternal(index, 1.08f, 4.0f);
        }
    }

    private void resetFish3DRotateOld(int index) {
        if (this.mediaPlayer == null || this.fecSubPorts[index] == -1) {
            return;
        }
        EZFECMediaPlayer fecMediaPlayer = (EZFECMediaPlayer)this.mediaPlayer;
        EZFECMediaPlayer.EZFECTransformElement p = fecMediaPlayer.getFish3DRotate(this.fecSubPorts[index]);
        LogUtil.d("EZFecStreamBase", String.format("resetFish3DRotateOld %f %f %f %f", Float.valueOf(p.fValue), Float.valueOf(p.fAxisX), Float.valueOf(p.fAxisY), Float.valueOf(p.fAxisZ)));
        boolean isSquare = Math.abs(this.fecSurfaceHolders[index].getSurfaceFrame().width() - this.fecSurfaceHolders[index].getSurfaceFrame().height()) < 50;
        EZFECMediaPlayer.EZFECTransformElement srTransParam = new EZFECMediaPlayer.EZFECTransformElement();
        float f = srTransParam.fValue = isSquare ? 0.6f : 0.1f;
        if (!fecMediaPlayer.setFish3DRotate(this.fecSubPorts[index], srTransParam)) {
            LogUtil.d("EZFecStreamBase", "setFish3DRotate return fail");
        }
    }

    private int fecPlaceTypeTransfrom(EZConstants.EZFecPlaceType fecPlaceType) {
        switch (fecPlaceType) {
            case EZ_FEC_PLACE_CEILING: {
                return 3;
            }
            case EZ_FEC_PLACE_WALL: {
                return 1;
            }
            case EZ_FEC_PLACE_FLOOR: {
                return 1;
            }
        }
        return 0;
    }

    private int fecCorrectTypeTransFrom(EZConstants.EZFecCorrectType fecCorrectType) {
        switch (fecCorrectType) {
            case EZ_FEC_CORRECT_4PTZ: {
                return 0;
            }
            case EZ_FEC_CORRECT_5PTZ: 
            case EZ_FEC_CORRECT_FULL5PTZ: {
                return 257;
            }
            case EZ_FEC_CORRECT_CYC: {
                return 5;
            }
            case EZ_FEC_CORRECT_LAT: {
                return 8;
            }
            case EZ_FEC_CORRECT_ARC_HOR: 
            case EZ_FEC_CORRECT_WIDEANGLE: {
                return 8;
            }
            case EZ_FEC_CORRECT_ARC_VER: {
                return 9;
            }
            case EZ_FEC_CORRECT_180: {
                return 1;
            }
            case EZ_FEC_CORRECT_360: {
                return 2;
            }
        }
        return -1;
    }
}


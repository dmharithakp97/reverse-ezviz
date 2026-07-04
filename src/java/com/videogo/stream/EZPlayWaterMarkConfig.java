/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.ez.player.EZPlayWaterMarkInfo
 *  com.ez.player.EZPlayWaterMarkInfo$FecColor
 *  com.ez.player.EZPlayWaterMarkInfo$PtzParam
 *  com.ez.player.EZPlayWaterMarkInfo$WatermarkFontSize
 *  com.ez.player.EZPlayWaterMarkInfo$WatermarkNumber
 *  com.ez.player.EZPlayWaterMarkInfo$WatermarkRotateInfo
 *  com.ez.player.EZPlayWaterMarkInfo$WatermarkWindowAdapt
 */
package com.videogo.stream;

import com.ez.player.EZPlayWaterMarkInfo;

public class EZPlayWaterMarkConfig {
    public String[] fontArray = new String[0];
    public int fontWidth = 35;
    public int fontHeight = 35;
    public float fontSpace = 1.2f;
    public EZWaterMarkTextAlignment fontAlignment = EZWaterMarkTextAlignment.EZWaterMarkTextAlignmentLeft;
    public float fontRotateAngle = 0.0f;
    public boolean fontColorAdapt = false;
    public int red = 0;
    public int green = 0;
    public int blue = 0;
    public int alpha = 100;
    public float startPosX = 0.0f;
    public float startPosY = 0.0f;
    public EZWaterMarkWindowAdaptMode windowAdaptMode = EZWaterMarkWindowAdaptMode.EZWaterMarkWindowAdaptModeNone;
    public int windowAdaptRowSpace = 100;
    public int windowAdaptColumnSpace = 100;
    public int windowAdaptBaseWindowWidth;
    public int windowAdaptBaseWindowHeight;
    public boolean fillFullScreen = false;
    public int rowNumber = 2;
    public int columnNumber = 2;

    public EZPlayWaterMarkInfo convert2EZPlayWaterMarkInfo() {
        EZPlayWaterMarkInfo waterMarkInfo = new EZPlayWaterMarkInfo();
        waterMarkInfo.watermarkFontArray = this.fontArray;
        waterMarkInfo.fontSize = new EZPlayWaterMarkInfo.WatermarkFontSize();
        waterMarkInfo.fontSize.fontWidth = this.fontWidth;
        waterMarkInfo.fontSize.fontHeight = this.fontHeight;
        waterMarkInfo.fontSpace = this.fontSpace;
        waterMarkInfo.alignment = this.fontAlignment.fontAlignment;
        waterMarkInfo.colorAdapt = this.fontColorAdapt ? 1 : 0;
        waterMarkInfo.color = new EZPlayWaterMarkInfo.FecColor();
        waterMarkInfo.color.red = this.red;
        waterMarkInfo.color.green = this.green;
        waterMarkInfo.color.blue = this.blue;
        waterMarkInfo.color.alpha = this.alpha;
        waterMarkInfo.startPos = new EZPlayWaterMarkInfo.PtzParam();
        waterMarkInfo.startPos.x = this.startPosX;
        waterMarkInfo.startPos.y = this.startPosY;
        waterMarkInfo.rotateInfo = new EZPlayWaterMarkInfo.WatermarkRotateInfo();
        waterMarkInfo.rotateInfo.rotateAngle = this.fontRotateAngle;
        waterMarkInfo.rotateInfo.fillFullScreen = this.fillFullScreen ? 1 : 0;
        waterMarkInfo.number = new EZPlayWaterMarkInfo.WatermarkNumber();
        waterMarkInfo.number.rows = this.rowNumber;
        waterMarkInfo.number.columns = this.columnNumber;
        waterMarkInfo.windowAdapt = new EZPlayWaterMarkInfo.WatermarkWindowAdapt();
        waterMarkInfo.windowAdapt.windowAdaptMode = this.windowAdaptMode.windowAdaptMode;
        waterMarkInfo.windowAdapt.rowSpace = this.windowAdaptRowSpace;
        waterMarkInfo.windowAdapt.columnSpace = this.windowAdaptColumnSpace;
        waterMarkInfo.windowAdapt.baseWindowWidth = this.windowAdaptBaseWindowWidth;
        waterMarkInfo.windowAdapt.baseWindowHeight = this.windowAdaptBaseWindowHeight;
        return waterMarkInfo;
    }

    public static enum EZWaterMarkTextAlignment {
        EZWaterMarkTextAlignmentCenter(0),
        EZWaterMarkTextAlignmentLeft(1);

        public final int fontAlignment;

        private EZWaterMarkTextAlignment(int fontAlignment) {
            this.fontAlignment = fontAlignment;
        }
    }

    public static enum EZWaterMarkWindowAdaptMode {
        EZWaterMarkWindowAdaptModeNone(0),
        EZWaterMarkWindowAdaptModeFontRowColumn(1),
        EZWaterMarkWindowAdaptModeFontSize(2);

        public final int windowAdaptMode;

        private EZWaterMarkWindowAdaptMode(int windowAdaptMode) {
            this.windowAdaptMode = windowAdaptMode;
        }
    }
}


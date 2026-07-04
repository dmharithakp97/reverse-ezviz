/*
 * Decompiled with CFR 0.152.
 */
package com.ez.player;

public class EZPlayWaterMarkInfo {
    public String[] watermarkFontArray;
    public float fontSpace;
    public int colorAdapt;
    public int alignment;
    public PtzParam startPos;
    public FecColor color;
    public WatermarkFontSize fontSize;
    public WatermarkRotateInfo rotateInfo;
    public WatermarkNumber number;
    public WatermarkWindowAdapt windowAdapt;
    public final byte[] reserved = new byte[20];

    public static class WatermarkWindowAdapt {
        public int windowAdaptMode;
        public int rowSpace;
        public int columnSpace;
        public int baseWindowWidth;
        public int baseWindowHeight;
    }

    public static class WatermarkNumber {
        public int rows;
        public int columns;
    }

    public static class WatermarkFontSize {
        public int fontWidth;
        public int fontHeight;
    }

    public static class FecColor {
        public int red;
        public int green;
        public int blue;
        public int alpha;
    }

    public static class WatermarkRotateInfo {
        public float rotateAngle;
        public int fillFullScreen;
    }

    public static class PtzParam {
        public float x;
        public float y;
    }
}


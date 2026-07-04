/*
 * Decompiled with CFR 0.152.
 */
package com.ez.player;

public enum PlayEffectType {
    EFFECT_TYPE_NONE(0, 0.0f, 0.0f),
    EFFECT_TYPE_BRIGHTNESS(1, -1.0f, 1.0f),
    EFFECT_TYPE_HUE(2, 0.0f, 1.0f),
    EFFECT_TYPE_SATURATION(3, -1.0f, 1.0f),
    EFFECT_TYPE_CONTRAST(4, -1.0f, 1.0f),
    EFFECT_TYPE_SHARPNESS(5, 0.0f, 1.0f),
    EFFECT_TYPE_DILATE(6, 0.0f, 1.0f),
    EFFECT_TYPE_WHITEN(7, 0.0f, 1.0f),
    EFFECT_TYPE_RUDDY(8, 0.0f, 1.0f),
    EFFECT_TYPE_SMOOTH(9, 0.0f, 1.0f);

    private int mType;
    private float min;
    private float max;

    private PlayEffectType(int type, float min, float max) {
        this.mType = type;
        this.min = min;
        this.max = max;
    }

    public int getType() {
        return this.mType;
    }

    public float getMin() {
        return this.min;
    }

    public float getMax() {
        return this.max;
    }
}


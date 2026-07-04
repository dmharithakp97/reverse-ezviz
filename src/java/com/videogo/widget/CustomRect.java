/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.MediaPlayer.PlayM4.Player$MPRect
 */
package com.videogo.widget;

import org.MediaPlayer.PlayM4.Player;

public class CustomRect {
    private float mLeft = 0.0f;
    private float mTop = 0.0f;
    private float mRight = 0.0f;
    private float mBottom = 0.0f;

    public void setValue(float l, float t, float r, float b) {
        this.mLeft = l;
        this.mTop = t;
        this.mRight = r;
        this.mBottom = b;
    }

    public float getLeft() {
        return this.mLeft;
    }

    public float getTop() {
        return this.mTop;
    }

    public float getRight() {
        return this.mRight;
    }

    public float getBottom() {
        return this.mBottom;
    }

    public float getWidth() {
        return this.mRight - this.mLeft;
    }

    public float getHeight() {
        return this.mBottom - this.mTop;
    }

    public static void judgeRect(Player.MPRect orgRect, Player.MPRect curRect) {
        int oldW = orgRect.right - orgRect.left;
        int oldH = orgRect.bottom - orgRect.top;
        int newW = curRect.right - curRect.left;
        int newH = curRect.bottom - curRect.top;
        if (newW > oldW || newH > oldH) {
            curRect.left = orgRect.left;
            curRect.right = orgRect.right;
            curRect.top = orgRect.top;
            curRect.bottom = orgRect.bottom;
            return;
        }
        if (curRect.left < orgRect.left) {
            curRect.left = orgRect.left;
        }
        curRect.right = curRect.left + newW;
        if (curRect.top < orgRect.top) {
            curRect.top = orgRect.top;
        }
        curRect.bottom = curRect.top + newH;
        if (curRect.right > orgRect.right) {
            curRect.right = orgRect.right;
            curRect.left = curRect.right - newW;
        }
        if (curRect.bottom > orgRect.bottom) {
            curRect.bottom = orgRect.bottom;
            curRect.left = curRect.bottom - newH;
        }
    }
}


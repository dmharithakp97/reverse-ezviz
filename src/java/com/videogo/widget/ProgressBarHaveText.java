/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Canvas
 *  android.graphics.Paint
 *  android.graphics.Rect
 *  android.util.AttributeSet
 *  android.util.DisplayMetrics
 *  android.util.TypedValue
 *  android.widget.ProgressBar
 */
package com.videogo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.ProgressBar;

public class ProgressBarHaveText
extends ProgressBar {
    private String mText;
    private Paint mPaint;

    public ProgressBarHaveText(Context context) {
        super(context);
        this.initText();
    }

    public ProgressBarHaveText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.initText();
    }

    public ProgressBarHaveText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initText();
    }

    public synchronized void setProgress(int progress) {
        this.setText(progress);
        super.setProgress(progress);
    }

    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect rect = new Rect();
        this.mPaint.getTextBounds(this.mText, 0, this.mText.length(), rect);
        int x = this.getWidth() / 2 - rect.centerX();
        int y = this.getHeight() / 2 - rect.centerY();
        canvas.drawText(this.mText, (float)x, (float)y, this.mPaint);
    }

    private void initText() {
        this.mPaint = new Paint();
        this.mPaint.setColor(-5395027);
        this.mPaint.setTextSize(TypedValue.applyDimension((int)2, (float)12.0f, (DisplayMetrics)this.getContext().getResources().getDisplayMetrics()));
        this.mPaint.setFlags(1);
    }

    private void setText(int progress) {
        int i = progress * 100 / this.getMax();
        this.mText = String.valueOf(i) + "%";
    }
}


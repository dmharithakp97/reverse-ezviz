/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Canvas
 *  android.graphics.Paint
 *  android.graphics.Paint$Style
 *  android.util.AttributeSet
 *  android.view.View
 */
package com.videogo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import com.videogo.util.Utils;

public class RingView
extends View
implements Runnable {
    private final Paint mPaint;
    private final Context mContext;
    private float mMinRadius;
    private float mDistance;
    private float mCurrentRadius;

    public RingView(Context context) {
        this(context, null);
    }

    public RingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeWidth((float)Utils.dip2px(this.mContext, 1.0f));
    }

    protected void onDraw(Canvas canvas) {
        if (this.mCurrentRadius > 0.0f) {
            canvas.drawCircle((float)(this.getLeft() + this.getWidth() / 2), (float)(this.getTop() + this.getHeight() / 2), this.mCurrentRadius, this.mPaint);
        }
        super.onDraw(canvas);
    }

    public void setMinRadiusAndDistance(float minRadius, int distance) {
        this.mMinRadius = minRadius;
        this.mCurrentRadius = this.mMinRadius + this.mDistance;
        this.mDistance = distance;
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (this.mMinRadius > 0.0f && !Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(300L);
            }
            catch (Exception e) {
                Thread.currentThread().interrupt();
            }
            int maxRadius = this.getHeight() / 2;
            this.mCurrentRadius = this.mCurrentRadius + this.mDistance < (float)maxRadius ? (this.mCurrentRadius += this.mDistance) : this.mMinRadius + this.mDistance;
            int alpha = (int)(255.0f * ((float)maxRadius - this.mCurrentRadius + this.mDistance) / (float)maxRadius);
            this.mPaint.setARGB(alpha, 209, 216, 219);
            this.postInvalidate();
        }
    }
}


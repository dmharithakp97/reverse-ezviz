/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.PointF
 *  android.os.Handler
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$OnTouchListener
 *  android.view.ViewParent
 */
package com.videogo.widget;

import android.graphics.PointF;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import com.videogo.widget.CustomRect;

public abstract class CustomTouchListener
implements View.OnTouchListener {
    private static final String TAG = "CustomTouchListener";
    private PointF down = new PointF();
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = 0;
    private static final int MAX_DOUBLE_CLICK_INTERVAL = 300;
    private static final float MAX_DISTANCE_FOR_CLICK = 5.0f;
    private static final float MIN_DOUBLE_POINT_DISTANCE = 10.0f;
    public static final int DRAG_LEFT = 0;
    public static final int DRAG_RIGHT = 1;
    public static final int DRAG_UP = 2;
    public static final int DRAG_DOWN = 3;
    private static final float UNIT_SCALE_RATIO = 0.003f;
    private long lastClickTime = 0L;
    private Handler mHandler;
    private boolean mIsWaitDoubleClick = false;
    private float mMaxScale = 2.0f;
    private static final int INVALID_POINTER = -1;
    private float mRatioX = 1.0f;
    private float mRatioY = 1.0f;
    private int mActionPointerId = -1;
    private float mLastDis = 0.0f;
    private float mLastScale = 1.0f;
    private final CustomRect mOriginalRect = new CustomRect();
    private final CustomRect mVirtualRect = new CustomRect();
    private Runnable mTimerForSecondClick = new Runnable(){

        @Override
        public void run() {
            if (CustomTouchListener.this.mIsWaitDoubleClick) {
                CustomTouchListener.this.mIsWaitDoubleClick = false;
                CustomTouchListener.this.onSingleClick();
            }
        }
    };

    public CustomTouchListener() {
        this.mHandler = new Handler();
    }

    public void setSacaleRect(float maxScale, int l, int t, int r, int b) {
        this.mOriginalRect.setValue(l, t, r, b);
        this.mVirtualRect.setValue(l, t, r, b);
        this.mMaxScale = maxScale;
        this.down.x = 0.0f;
        this.down.y = 0.0f;
        this.mLastDis = 0.0f;
        this.mRatioX = 1.0f;
        this.mRatioY = 1.0f;
        this.mLastScale = 1.0f;
    }

    public boolean onTouch(View v, MotionEvent event) {
        ViewParent parent = v.getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(true);
        }
        this.touch(v, event);
        return true;
    }

    public void touch(View view, MotionEvent event) {
        int action = event.getAction() & 0xFF;
        switch (action) {
            case 0: {
                this.mode = 1;
                this.mActionPointerId = event.getPointerId(0);
                this.down.x = event.getX();
                this.down.y = event.getY();
                this.lastClickTime = event.getEventTime();
                break;
            }
            case 5: {
                this.mLastDis = CustomTouchListener.spacing(event);
                if (!(this.mLastDis > 10.0f)) break;
                this.mode = 2;
                this.midPoint(event);
                break;
            }
            case 2: {
                float distance;
                if (this.mode == 2) {
                    float newDist = CustomTouchListener.spacing(event);
                    float scale = newDist / this.mLastDis;
                    if (!((double)scale > 1.01) && !((double)scale < 0.99) || !this.canZoom(scale)) break;
                    this.onZoom(view, scale);
                    if (event.getPointerCount() != 2) break;
                    float newScale = this.mLastScale + (newDist - this.mLastDis) * 0.003f;
                    this.mLastDis = newDist;
                    if (newScale < 1.0f) {
                        newScale = 1.0f;
                    }
                    if (newScale > this.mMaxScale) {
                        newScale = this.mMaxScale;
                    }
                    this.scale(view, newScale);
                    this.midPoint(event);
                    break;
                }
                if (this.mode != 1 || !(5.0f < (distance = CustomTouchListener.distance(event, this.down)))) break;
                float disX = Math.abs(event.getX() - this.down.x);
                float disY = Math.abs(event.getY() - this.down.y);
                float k = disX == 0.0f ? 0.0f : disY / disX;
                double angle = Math.atan(k);
                double degree = Math.toDegrees(angle);
                float rate = distance / (float)(event.getEventTime() - this.lastClickTime);
                int index = this.mActionPointerId >= 0 ? event.findPointerIndex(this.mActionPointerId) : -1;
                int drag = -1;
                if (degree > 0.0 && degree < 60.0) {
                    if (event.getX() > this.down.x) {
                        if (this.canDrag(1)) {
                            this.onDrag(1, disX, rate);
                            drag = 1;
                        }
                    } else if (this.canDrag(0)) {
                        this.onDrag(0, disX, rate);
                        drag = 0;
                    }
                } else if (degree > 0.0) {
                    if (event.getY() > this.down.y) {
                        if (this.canDrag(3)) {
                            this.onDrag(3, disY, rate);
                            drag = 3;
                        }
                    } else if (this.canDrag(2)) {
                        this.onDrag(2, disY, rate);
                        drag = 2;
                    }
                }
                if (index >= 0) {
                    float x = event.getX(index);
                    float y = event.getY(index);
                    if (drag != -1) {
                        this.move(view, this.down.x, this.down.y, x, y);
                    }
                    this.down.x = x;
                    this.down.y = y;
                } else {
                    this.down.x = event.getX();
                    this.down.y = event.getY();
                }
                this.lastClickTime = event.getEventTime();
                break;
            }
            case 1: 
            case 3: {
                this.onEnd(view, this.mode);
                this.mode = 0;
                if (5.0f < CustomTouchListener.distance(event, this.down)) break;
                this.onClick(view, event);
                break;
            }
            case 6: {
                this.onSecondaryPointerUp(event);
                this.onEnd(view, this.mode);
                this.mode = 0;
                break;
            }
        }
    }

    public static float spacing(MotionEvent event) {
        float y;
        float x = event.getX(0) - event.getX(1);
        if (x < 0.0f) {
            x = -x;
        }
        if ((y = event.getY(0) - event.getY(1)) < 0.0f) {
            y = -y;
        }
        return (float)Math.sqrt(x * x + y * y);
    }

    public static float distance(MotionEvent point2, PointF point1) {
        float y;
        float x = point1.x - point2.getX();
        if (x < 0.0f) {
            x = -x;
        }
        if ((y = point1.y - point2.getY()) < 0.0f) {
            y = -y;
        }
        return (float)Math.sqrt(x * x + y * y);
    }

    private void onClick(View view, MotionEvent e) {
        if (this.mIsWaitDoubleClick) {
            this.midPointDoubleClick(e);
            if (this.mLastScale == this.mMaxScale) {
                this.scale(view, 1.0f);
            } else {
                this.scale(view, this.mMaxScale);
            }
            this.onDoubleClick(view, e);
            this.mIsWaitDoubleClick = false;
            this.mHandler.removeCallbacks(this.mTimerForSecondClick);
        } else {
            this.mIsWaitDoubleClick = true;
            this.mHandler.postDelayed(this.mTimerForSecondClick, 300L);
        }
    }

    public void scale(View view, float newScale) {
        float w = this.mOriginalRect.getWidth() * newScale;
        float h = this.mOriginalRect.getHeight() * newScale;
        float newL = this.mVirtualRect.getLeft() - this.mRatioX * (w - this.mVirtualRect.getWidth());
        float newT = this.mVirtualRect.getTop() - this.mRatioY * (h - this.mVirtualRect.getHeight());
        float newR = newL + w;
        float newB = newT + h;
        this.mVirtualRect.setValue(newL, newT, newR, newB);
        this.judge(this.mOriginalRect, this.mVirtualRect);
        this.mLastScale = newScale;
        this.onZoomChange(view, this.mLastScale, this.mOriginalRect, this.mVirtualRect);
    }

    private void move(View view, float lastX, float lastY, float curX, float curY) {
        float deltaX = curX - lastX;
        float deltaY = curY - lastY;
        float left = this.mVirtualRect.getLeft();
        float top = this.mVirtualRect.getTop();
        float right = this.mVirtualRect.getRight();
        float bottom = this.mVirtualRect.getBottom();
        float newL = left + deltaX;
        float newT = top + deltaY;
        float newR = right + deltaX;
        float newB = bottom + deltaY;
        this.mVirtualRect.setValue(newL, newT, newR, newB);
        this.judge(this.mOriginalRect, this.mVirtualRect);
        this.onZoomChange(view, this.mLastScale, this.mOriginalRect, this.mVirtualRect);
    }

    private void midPoint(MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        this.mRatioX = Math.abs(x / 2.0f - this.mVirtualRect.getLeft()) / this.mVirtualRect.getWidth();
        this.mRatioY = Math.abs(y / 2.0f - this.mVirtualRect.getTop()) / this.mVirtualRect.getHeight();
    }

    private void midPointDoubleClick(MotionEvent event) {
        float x = event.getX(0);
        float y = event.getY(0);
        this.mRatioX = Math.abs(x - this.mVirtualRect.getLeft()) / this.mVirtualRect.getWidth();
        this.mRatioY = Math.abs(y - this.mVirtualRect.getTop()) / this.mVirtualRect.getHeight();
    }

    private void judge(CustomRect oRect, CustomRect curRect) {
        float oL = oRect.getLeft();
        float oT = oRect.getTop();
        float oR = oRect.getRight();
        float oB = oRect.getBottom();
        float newL = curRect.getLeft();
        float newT = curRect.getTop();
        float newR = curRect.getRight();
        float newB = curRect.getBottom();
        float newW = curRect.getWidth();
        float newH = curRect.getHeight();
        if (newL > oL) {
            newL = oL;
        }
        newR = newL + newW;
        if (newT > oT) {
            newT = oT;
        }
        newB = newT + newH;
        if (newR < oR) {
            newR = oR;
            newL = oR - newW;
        }
        if (newB < oB) {
            newB = oB;
            newT = oB - newH;
        }
        curRect.setValue(newL, newT, newR, newB);
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        int pointerIndex = (ev.getAction() & 0xFF00) >> 8;
        int pointerId = ev.getPointerId(pointerIndex);
        int newPointerIndex = pointerIndex == 0 ? 1 : 0;
        this.down.x = ev.getX(newPointerIndex);
        this.down.y = ev.getY(newPointerIndex);
        if (pointerId == this.mActionPointerId) {
            this.mActionPointerId = ev.getPointerId(newPointerIndex);
        }
    }

    public abstract boolean canZoom(float var1);

    public abstract boolean canDrag(int var1);

    public abstract void onSingleClick();

    public abstract void onDoubleClick(View var1, MotionEvent var2);

    public abstract void onZoom(View var1, float var2);

    public abstract void onZoomChange(View var1, float var2, CustomRect var3, CustomRect var4);

    public abstract void onDrag(int var1, float var2, float var3);

    public abstract void onEnd(View var1, int var2);
}


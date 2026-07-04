/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Handler
 *  android.os.Message
 *  android.util.AttributeSet
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$OnTouchListener
 *  android.widget.HorizontalScrollView
 */
package com.videogo.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

public class TimeBarHorizontalScrollView
extends HorizontalScrollView
implements View.OnTouchListener {
    private boolean mIsEnable = true;
    private TimeScrollBarScrollListener mTimeScrollBarScrollListener;
    private Handler mHandler;
    private static final int CHECK_STOP_STATE = 272;
    private int mLastLeftX = 0;
    private boolean mIsScroll = false;

    public TimeBarHorizontalScrollView(Context context) {
        super(context);
        this.init();
    }

    public TimeBarHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public TimeBarHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.init();
    }

    private void init() {
        this.setOnTouchListener(this);
        this.mHandler = new Handler(){

            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 272: {
                        TimeBarHorizontalScrollView.this.checkScrollStop();
                        break;
                    }
                }
            }
        };
    }

    public void setEnable(boolean isEnable) {
        this.mIsEnable = isEnable;
    }

    public void setTimeScrollBarScrollListener(TimeScrollBarScrollListener listener) {
        this.mTimeScrollBarScrollListener = listener;
    }

    public boolean onTouch(View view, MotionEvent event) {
        if (!this.mIsEnable) {
            return true;
        }
        switch (event.getAction()) {
            case 0: {
                this.mIsScroll = false;
                break;
            }
            case 2: {
                if (this.mIsScroll) break;
                if (null != this.mTimeScrollBarScrollListener) {
                    this.mTimeScrollBarScrollListener.onScrollStart(this);
                }
                this.mIsScroll = true;
                break;
            }
            case 1: 
            case 3: {
                if (!this.mIsScroll) break;
                Message msg = this.mHandler.obtainMessage();
                msg.what = 272;
                this.mHandler.removeMessages(272);
                this.mHandler.sendMessage(msg);
                break;
            }
        }
        return false;
    }

    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if (this.mTimeScrollBarScrollListener != null) {
            this.mTimeScrollBarScrollListener.onScrollChanged(l, t, oldl, oldt, this);
        }
        super.onScrollChanged(l, t, oldl, oldt);
    }

    private void checkScrollStop() {
        int tempX = this.getScrollX();
        if (this.mLastLeftX == tempX) {
            if (null != this.mTimeScrollBarScrollListener) {
                this.mTimeScrollBarScrollListener.onScrollStop(this);
            }
        } else {
            this.mLastLeftX = tempX;
            this.mHandler.removeMessages(272);
            this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(272), 100L);
        }
    }

    public static interface TimeScrollBarScrollListener {
        public void onScrollChanged(int var1, int var2, int var3, int var4, HorizontalScrollView var5);

        public void onScrollStart(HorizontalScrollView var1);

        public void onScrollStop(HorizontalScrollView var1);
    }
}


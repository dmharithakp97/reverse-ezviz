/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Canvas
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.animation.AlphaAnimation
 *  android.view.animation.Animation
 *  android.view.animation.AnimationSet
 *  android.view.animation.LayoutAnimationController
 *  android.view.animation.TranslateAnimation
 *  android.widget.ListAdapter
 *  android.widget.ListView
 */
package com.videogo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.ListAdapter;
import android.widget.ListView;

public class PinnedHeaderListView
extends ListView {
    private static final int MAX_ALPHA = 255;
    private PinnedHeaderAdapter mAdapter;
    private View mHeaderView;
    private boolean mHeaderViewVisible;
    private int mHeaderViewWidth;
    private int mHeaderViewHeight;
    private LayoutAnimationController controller;

    public PinnedHeaderListView(Context context) {
        super(context);
        this.init();
    }

    public PinnedHeaderListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public PinnedHeaderListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.init();
    }

    private void init() {
        AnimationSet set = new AnimationSet(true);
        AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(50L);
        set.addAnimation((Animation)animation);
        animation = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 1.0f, 1, 0.0f);
        animation.setDuration(250L);
        set.addAnimation((Animation)animation);
        this.controller = new LayoutAnimationController((Animation)set, 0.5f);
    }

    public void startAnimation() {
        this.setLayoutAnimation(this.controller);
    }

    public void setPinnedHeaderView(View view) {
        this.mHeaderView = view;
        if (this.mHeaderView != null) {
            this.setFadingEdgeLength(0);
        }
        this.requestLayout();
    }

    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        this.mAdapter = (PinnedHeaderAdapter)adapter;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (this.mHeaderView != null) {
            this.measureChild(this.mHeaderView, widthMeasureSpec, heightMeasureSpec);
            this.mHeaderViewWidth = this.mHeaderView.getMeasuredWidth();
            this.mHeaderViewHeight = this.mHeaderView.getMeasuredHeight();
        }
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (this.mHeaderView != null) {
            this.mHeaderView.layout(0, 0, this.mHeaderViewWidth, this.mHeaderViewHeight);
            this.configureHeaderView(this.getFirstVisiblePosition());
        }
    }

    public void configureHeaderView(int position) {
        if (this.mHeaderView == null) {
            return;
        }
        int state = this.mAdapter.getPinnedHeaderState(position);
        switch (state) {
            case 0: {
                this.mHeaderViewVisible = false;
                break;
            }
            case 1: {
                this.mAdapter.configurePinnedHeader(this.mHeaderView, position, 255);
                if (this.mHeaderView.getTop() != 0) {
                    this.mHeaderView.layout(0, 0, this.mHeaderViewWidth, this.mHeaderViewHeight);
                }
                this.mHeaderViewVisible = true;
                break;
            }
            case 2: {
                int alpha;
                int y;
                int headerHeight;
                View firstView = this.getChildAt(0);
                if (firstView == null) break;
                int bottom = firstView.getBottom();
                if (bottom < (headerHeight = this.mHeaderView.getHeight())) {
                    y = bottom - headerHeight;
                    alpha = 255 * (headerHeight + y) / headerHeight;
                } else {
                    y = 0;
                    alpha = 255;
                }
                this.mAdapter.configurePinnedHeader(this.mHeaderView, position, alpha);
                if (this.mHeaderView.getTop() != y) {
                    this.mHeaderView.layout(0, y, this.mHeaderViewWidth, this.mHeaderViewHeight + y);
                }
                this.mHeaderViewVisible = true;
                break;
            }
        }
    }

    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (this.mHeaderViewVisible) {
            this.drawChild(canvas, this.mHeaderView, this.getDrawingTime());
        }
    }

    public static interface PinnedHeaderAdapter {
        public static final int PINNED_HEADER_GONE = 0;
        public static final int PINNED_HEADER_VISIBLE = 1;
        public static final int PINNED_HEADER_PUSHED_UP = 2;

        public int getPinnedHeaderState(int var1);

        public void configurePinnedHeader(View var1, int var2, int var3);
    }
}


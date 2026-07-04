/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.view.View
 *  android.view.animation.AccelerateInterpolator
 *  android.view.animation.Animation
 *  android.view.animation.Animation$AnimationListener
 *  android.view.animation.DecelerateInterpolator
 *  android.view.animation.Interpolator
 */
package com.videogo.util;

import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import com.videogo.widget.Rotate3dAnimation;

public class RotateViewUtil {
    private int mDuration = 300;
    private float mCenterX = 0.0f;
    private float mCenterY = 0.0f;
    private float mDepthZ = 300.0f;
    private View mContainer = null;
    private View mView1 = null;
    private View mView2 = null;

    public void applyRotation(View container, View view1, View view2, float startAngle, float toAngle) {
        this.mContainer = container;
        this.mView1 = view1;
        this.mView2 = view2;
        this.mCenterX = (float)this.mContainer.getWidth() / 2.0f;
        this.mCenterY = (float)this.mContainer.getHeight() / 2.0f;
        Rotate3dAnimation rotation = new Rotate3dAnimation(startAngle, toAngle, this.mCenterX, this.mCenterY, this.mDepthZ, true);
        rotation.setDuration(this.mDuration);
        rotation.setFillAfter(true);
        rotation.setInterpolator((Interpolator)new AccelerateInterpolator());
        rotation.setAnimationListener(new DisplayNextView());
        this.mView1.startAnimation((Animation)rotation);
    }

    private final class DisplayNextView
    implements Animation.AnimationListener {
        private DisplayNextView() {
        }

        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
            if (RotateViewUtil.this.mContainer != null) {
                RotateViewUtil.this.mContainer.post((Runnable)new SwapViews());
            }
        }

        public void onAnimationRepeat(Animation animation) {
        }
    }

    private final class DisplayEndView
    implements Animation.AnimationListener {
        private DisplayEndView() {
        }

        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
            if (RotateViewUtil.this.mView1 != null) {
                if (RotateViewUtil.this.mView1.getAnimation() != null) {
                    RotateViewUtil.this.mView1.getAnimation().reset();
                }
                RotateViewUtil.this.mView1.clearAnimation();
            }
            if (RotateViewUtil.this.mView2 != null) {
                if (RotateViewUtil.this.mView2.getAnimation() != null) {
                    RotateViewUtil.this.mView2.getAnimation().reset();
                }
                RotateViewUtil.this.mView2.clearAnimation();
            }
        }

        public void onAnimationRepeat(Animation animation) {
        }
    }

    private final class SwapViews
    implements Runnable {
        private SwapViews() {
        }

        @Override
        public void run() {
            RotateViewUtil.this.mView1.setVisibility(8);
            RotateViewUtil.this.mView2.setVisibility(0);
            RotateViewUtil.this.mView2.requestFocus();
            Rotate3dAnimation rotation = new Rotate3dAnimation(-90.0f, 0.0f, RotateViewUtil.this.mCenterX, RotateViewUtil.this.mCenterY, RotateViewUtil.this.mDepthZ, false);
            rotation.setDuration(RotateViewUtil.this.mDuration);
            rotation.setFillAfter(true);
            rotation.setInterpolator((Interpolator)new DecelerateInterpolator());
            rotation.setAnimationListener(new DisplayEndView());
            RotateViewUtil.this.mView2.startAnimation((Animation)rotation);
        }
    }
}


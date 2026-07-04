/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.content.Context
 *  android.util.AttributeSet
 *  android.view.accessibility.AccessibilityEvent
 *  android.view.accessibility.AccessibilityNodeInfo
 *  android.widget.CompoundButton
 */
package com.videogo.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.CompoundButton;

public class CheckTextButton
extends CompoundButton {
    private boolean mToggleEnable = true;

    public CheckTextButton(Context context) {
        this(context, null);
    }

    public CheckTextButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CheckTextButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @TargetApi(value=14)
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName((CharSequence)CheckTextButton.class.getName());
    }

    @TargetApi(value=14)
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName((CharSequence)CheckTextButton.class.getName());
    }

    public void setToggleEnable(boolean toggleEnable) {
        this.mToggleEnable = toggleEnable;
    }

    public void toggle() {
        if (this.mToggleEnable) {
            super.toggle();
        }
    }
}


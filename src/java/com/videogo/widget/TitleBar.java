/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Color
 *  android.graphics.Typeface
 *  android.graphics.drawable.Drawable
 *  android.text.TextUtils
 *  android.text.TextUtils$TruncateAt
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.widget.Button
 *  android.widget.CompoundButton
 *  android.widget.CompoundButton$OnCheckedChangeListener
 *  android.widget.FrameLayout
 *  android.widget.FrameLayout$LayoutParams
 *  android.widget.ImageView
 *  android.widget.LinearLayout
 *  android.widget.LinearLayout$LayoutParams
 *  android.widget.RelativeLayout
 *  android.widget.RelativeLayout$LayoutParams
 *  android.widget.TextView
 */
package com.videogo.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.videogo.util.Utils;
import com.videogo.widget.CheckTextButton;

public class TitleBar
extends FrameLayout {
    private Drawable mBackground;
    private Drawable mBackButton;
    private int mTextColor;
    private Context mContext = this.getContext();
    private ViewGroup mTitleLayout;
    private TextView mTextView;
    private View mTitleView;
    private LinearLayout mLeftLayout;
    private LinearLayout mRightLayout;
    private static final int ID_TITLELAYOUT = 1;
    private static final int ID_TEXTVIEW = 2;
    private static final int ID_LEFTLAYOUT = 3;
    private static final int ID_RIGHTLAYOUT = 4;

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mTextColor = Color.rgb((int)51, (int)51, (int)51);
        this.mBackground = Utils.getDrawableFromAssetsFile(this.mContext, "common_title.9.png");
        Drawable normal = Utils.getDrawableFromAssetsFile(this.mContext, "common_title_back.png");
        Drawable pressed = Utils.getDrawableFromAssetsFile(this.mContext, "common_title_back_sel.png");
        this.mBackButton = Utils.newSelector(this.mContext, normal, pressed, normal, normal);
        this.init();
    }

    private void init() {
        this.mTitleLayout = new RelativeLayout(this.mContext);
        this.mTitleLayout.setId(1);
        this.mTitleLayout.setBackgroundDrawable(this.mBackground);
        FrameLayout.LayoutParams titleLayoutLp = new FrameLayout.LayoutParams(-1, Utils.dip2px(this.mContext, 44.0f));
        this.addView((View)this.mTitleLayout, (ViewGroup.LayoutParams)titleLayoutLp);
        this.mLeftLayout = new LinearLayout(this.mContext);
        this.mLeftLayout.setId(3);
        RelativeLayout.LayoutParams leftLayoutLp = new RelativeLayout.LayoutParams(-2, -2);
        leftLayoutLp.addRule(9);
        leftLayoutLp.addRule(15);
        this.mLeftLayout.setOrientation(0);
        this.mLeftLayout.setGravity(16);
        this.mTitleLayout.addView((View)this.mLeftLayout, (ViewGroup.LayoutParams)leftLayoutLp);
        this.mTextView = new TextView(this.mContext);
        this.mTextView.setId(2);
        RelativeLayout.LayoutParams textViewLp = new RelativeLayout.LayoutParams(-2, -2);
        textViewLp.addRule(13);
        this.mTextView.setEllipsize(TextUtils.TruncateAt.END);
        this.mTextView.setMaxWidth(Utils.dip2px(this.mContext, 200.0f));
        this.mTextView.setSingleLine(true);
        this.mTextView.setTextColor(this.mTextColor);
        this.mTextView.setTextSize(2, 16.0f);
        this.mTextView.setTypeface(Typeface.create((Typeface)Typeface.DEFAULT, (int)1));
        this.mTitleLayout.addView((View)this.mTextView, (ViewGroup.LayoutParams)textViewLp);
        this.mRightLayout = new LinearLayout(this.mContext);
        this.mRightLayout.setId(4);
        RelativeLayout.LayoutParams rightLayoutLp = new RelativeLayout.LayoutParams(-2, -2);
        rightLayoutLp.addRule(11);
        rightLayoutLp.addRule(15);
        this.mRightLayout.setOrientation(0);
        this.mRightLayout.setGravity(16);
        this.mTitleLayout.addView((View)this.mRightLayout, (ViewGroup.LayoutParams)rightLayoutLp);
    }

    public void setStyle(int textColor, Drawable background, Drawable backButton) {
        this.mTextColor = textColor;
        this.mBackground = background;
        if (backButton != null) {
            this.mBackButton = backButton;
        }
        this.mTextView.setTextColor(this.mTextColor);
        this.mTitleLayout.setBackgroundDrawable(this.mBackground);
    }

    public TextView setTitle(int resId) {
        return this.setTitle(this.mContext.getText(resId));
    }

    public TextView setTitle(CharSequence text) {
        this.mTextView.setText(text);
        this.mTextView.setVisibility(TextUtils.isEmpty((CharSequence)text) || this.mTitleView != null ? 8 : 0);
        return this.mTextView;
    }

    public View setTitle(View view) {
        if (this.mTitleView != null) {
            this.mTitleLayout.removeView(this.mTitleView);
        }
        if (view != null) {
            RelativeLayout.LayoutParams layoutParams = null;
            layoutParams = view.getLayoutParams() == null ? new RelativeLayout.LayoutParams(-2, -2) : new RelativeLayout.LayoutParams(view.getLayoutParams());
            layoutParams.addRule(13);
            this.mTitleLayout.addView(view, (ViewGroup.LayoutParams)layoutParams);
            this.mTextView.setVisibility(8);
        } else {
            this.mTextView.setVisibility(0);
        }
        this.mTitleView = view;
        return view;
    }

    public void setBackgroundColor(int color) {
        this.mTitleLayout.setBackgroundColor(color);
    }

    public void setOnTitleClickListener(View.OnClickListener l) {
        this.mTextView.setOnClickListener(l);
    }

    public ImageView addTitleButton(int resId, View.OnClickListener l) {
        ImageView button = new ImageView(this.mContext);
        button.setImageResource(resId);
        button.setOnClickListener(l);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams.addRule(1, 2);
        layoutParams.addRule(13);
        this.mTitleLayout.addView((View)button, (ViewGroup.LayoutParams)layoutParams);
        return button;
    }

    public void addTitleView(View v) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams.addRule(1, 2);
        layoutParams.addRule(15);
        this.mTitleLayout.addView(v, (ViewGroup.LayoutParams)layoutParams);
    }

    public Button addLeftButton(int resId, View.OnClickListener l) {
        return this.addLeftButton(this.getResources().getDrawable(resId), l);
    }

    public Button addLeftButton(Drawable drawable, View.OnClickListener l) {
        Button button = new Button(this.mContext);
        button.setBackgroundDrawable(drawable);
        button.setOnClickListener(l);
        this.addLeftView((View)button);
        return button;
    }

    public void addLeftView(View v) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Utils.dip2px(this.mContext, 60.0f), Utils.dip2px(this.mContext, 44.0f));
        this.mLeftLayout.addView(v, 0, (ViewGroup.LayoutParams)layoutParams);
    }

    public void removeLeftView(View v) {
        this.mLeftLayout.removeView(v);
    }

    public void removeAllLeftView() {
        this.mLeftLayout.removeAllViews();
    }

    public Button addBackButton(View.OnClickListener l) {
        return this.addLeftButton(this.mBackButton, l);
    }

    public Button addRightButton(int resId, int width, int height) {
        Drawable drawable = this.getResources().getDrawable(resId);
        Button button = new Button(this.mContext);
        button.setBackgroundDrawable(drawable);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Utils.dip2px(this.mContext, width), Utils.dip2px(this.mContext, height));
        this.mRightLayout.addView((View)button, 0, (ViewGroup.LayoutParams)layoutParams);
        return button;
    }

    public Button addRightButton(int resId, View.OnClickListener l) {
        return this.addRightButton(this.getResources().getDrawable(resId), l);
    }

    public Button addRightButton(Drawable drawable, View.OnClickListener l) {
        Button button = new Button(this.mContext);
        button.setBackgroundDrawable(drawable);
        button.setOnClickListener(l);
        this.addRightView((View)button);
        return button;
    }

    public void addRightView(View v) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Utils.dip2px(this.mContext, 60.0f), Utils.dip2px(this.mContext, 44.0f));
        this.mRightLayout.addView(v, 0, (ViewGroup.LayoutParams)layoutParams);
    }

    public void removeRightView(View v) {
        this.mRightLayout.removeView(v);
    }

    public void removeAllRightView() {
        this.mRightLayout.removeAllViews();
    }

    public ImageView addRightProgress() {
        ImageView view = new ImageView(this.mContext);
        view.setImageBitmap(Utils.getImageFromAssetsFile(this.mContext, "common_title_refresh.png"));
        this.addRightView((View)view);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)view.getLayoutParams();
        layoutParams.rightMargin = Utils.dip2px(this.mContext, 13.0f);
        layoutParams.width = Utils.dip2px(this.mContext, 30.0f);
        layoutParams.height = Utils.dip2px(this.mContext, 30.0f);
        return view;
    }

    public CheckTextButton addRightCheckedText(final CharSequence text, final CharSequence checkedText, final CompoundButton.OnCheckedChangeListener l) {
        CheckTextButton view = new CheckTextButton(this.mContext);
        int padding = Utils.dip2px(this.mContext, 5.0f);
        view.setClickable(true);
        view.setPadding(padding, Utils.dip2px(this.mContext, 9.0f), padding, padding);
        view.setText(text);
        view.setTextColor(Color.rgb((int)51, (int)51, (int)51));
        view.setTextSize(2, 16.0f);
        view.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                buttonView.setText(isChecked ? checkedText : text);
                if (l != null) {
                    l.onCheckedChanged(buttonView, isChecked);
                }
            }
        });
        this.addRightView((View)view);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)view.getLayoutParams();
        layoutParams.rightMargin = Utils.dip2px(this.mContext, 15.0f) - padding;
        return view;
    }

    public void setBackButton(int resId) {
        this.mBackButton = this.getResources().getDrawable(resId);
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap
 *  android.os.Build$VERSION
 *  android.webkit.WebView
 *  android.webkit.WebViewClient
 */
package com.videogo.widget;

import android.graphics.Bitmap;
import android.os.Build;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CompatWebViewClient
extends WebViewClient {
    public final void onPageStarted(WebView view, String url, Bitmap favicon) {
        if (Build.VERSION.SDK_INT < 11 && this.shouldOverrideUrlLoading(view, url)) {
            view.stopLoading();
        } else {
            this.onPageStartedCompat(view, url, favicon);
        }
    }

    public void onPageStartedCompat(WebView view, String url, Bitmap favicon) {
    }
}


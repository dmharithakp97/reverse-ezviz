/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Intent
 *  android.os.Bundle
 */
package com.ezviz.opensdk.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.ezviz.opensdk.auth.EZAuthHelper;

public class EZAuthHandleActivity
extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EZAuthHelper ezAuthHelper = EZAuthHelper.getHelper();
        ezAuthHelper.setAuthHandleActivity(this);
        ezAuthHelper.analysisDate(this);
        this.finish();
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.setIntent(intent);
        EZAuthHelper ezAuthHelper = EZAuthHelper.getHelper();
        ezAuthHelper.analysisDate(this);
        this.finish();
    }

    protected void onAuthSuccess() {
    }

    protected void onAuthFail(int errorCode) {
    }
}


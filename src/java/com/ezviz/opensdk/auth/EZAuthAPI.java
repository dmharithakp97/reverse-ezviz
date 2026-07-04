/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.net.Uri$Builder
 */
package com.ezviz.opensdk.auth;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.ezviz.opensdk.auth.EZAuthHelper;
import java.util.List;

public class EZAuthAPI {
    public static void sendAuthReq(Context context, EZAuthPlatform ezAuthPatform) {
        EZAuthHelper.getHelper().sendAuthReq(context, ezAuthPatform);
    }

    public static void sendOpenPage(Context context, EZAuthSDKOpenPage page, EZAuthPlatform ezAuthPatform) {
        if (page == EZAuthSDKOpenPage.OpenPage_DeviceList) {
            EZAuthHelper.getHelper().sendOpenPageDeviceList(context, ezAuthPatform);
        } else if (page == EZAuthSDKOpenPage.OpenPage_AlarmList) {
            EZAuthHelper.getHelper().sendOpenPageAlarmList(context, ezAuthPatform);
        }
    }

    public static boolean isEzvizAppInstalledWithType(Context context, EZAuthPlatform ezAuthPatform) {
        String scheme = EZAuthHelper.getHelper().getScheme(ezAuthPatform);
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(scheme);
        Intent intent = new Intent("android.intent.action.VIEW", builder.build());
        List activities = context.getPackageManager().queryIntentActivities(intent, 0);
        boolean isValid = !activities.isEmpty();
        return isValid;
    }

    public static enum EZAuthPlatform {
        EZVIZ,
        EZVIZ_CHINA,
        HC_CONNECT;

    }

    public static enum EZAuthSDKOpenPage {
        OpenPage_DeviceList,
        OpenPage_AlarmList;

    }
}


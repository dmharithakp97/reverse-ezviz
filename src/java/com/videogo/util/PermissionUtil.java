/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Process
 */
package com.videogo.util;

import android.os.Process;
import com.videogo.openapi.BaseAPI;

public class PermissionUtil {
    public static boolean checkSelfPermission(String permission) {
        try {
            if (permission == null) {
                throw new IllegalArgumentException("permission is null");
            }
            int checkSelfPermission = BaseAPI.mApplication.getApplicationContext().checkPermission(permission, Process.myPid(), Process.myUid());
            if (checkSelfPermission == 0) {
                return true;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}


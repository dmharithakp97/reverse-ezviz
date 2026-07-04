/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Base64
 */
package com.videogo.openapi.model.req;

import android.util.Base64;
import com.videogo.openapi.BaseAPI;
import com.videogo.util.LocalInfo;
import com.videogo.util.LogUtil;

public class WebLoginReq {
    public static final String URL = "/oauth/authorize";
    private static String mVparam = null;

    public static String getReqData() {
        StringBuilder sb = new StringBuilder();
        String v_param = null != mVparam ? mVparam : "mobilezx";
        sb.append("?response_type=refresh_token&client_id=" + BaseAPI.getInstance().getAppKey());
        sb.append("&bundleId=").append(LocalInfo.getInstance().getPackageName());
        sb.append("&view_logo=").append(0);
        sb.append("&view_reg=").append(1);
        sb.append("&redirect_uri=default&scope=xx&state=xxx");
        sb.append("&v=").append(v_param);
        sb.append("&sign=" + LocalInfo.getInstance().getHardwareCode());
        sb.append("&cname=" + WebLoginReq.getBase64DeviceModel());
        sb.append("&client_type=13");
        LogUtil.i("Web", "getReqData: " + sb.toString());
        return sb.toString();
    }

    public static void setmVparam(String vParam) {
        mVparam = vParam;
    }

    private static String getBase64DeviceModel() {
        String deviceModel = LocalInfo.getInstance().getDeviceModel();
        return Base64.encodeToString((byte[])deviceModel.getBytes(), (int)0);
    }
}


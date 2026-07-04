/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.json.JSONException
 *  org.json.JSONObject
 */
package com.videogo.openapi.model.resp;

import com.videogo.camera.CameraInfoEx;
import com.videogo.device.DeviceInfoEx;
import com.videogo.exception.BaseException;
import com.videogo.openapi.model.BaseResponse;
import com.videogo.util.LogUtil;
import com.videogo.util.ReflectionUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class GetCameraDetailResp
extends BaseResponse {
    private static final String ADURL = "adUrl";
    private static final String DEVICE = "device";
    private static final String BELONGDEVICE = "belongDevice";
    private static final String CAMERA = "camera";
    private DeviceInfoEx deviceInfoEx;
    private CameraInfoEx cameraInfoEx;
    private DeviceInfoEx belongDeviceInfoEx;

    public GetCameraDetailResp(DeviceInfoEx deviceInfoEx, CameraInfoEx cameraInfoEx, DeviceInfoEx belongDeviceInfoEx) {
        this.deviceInfoEx = deviceInfoEx;
        this.cameraInfoEx = cameraInfoEx;
        this.belongDeviceInfoEx = belongDeviceInfoEx;
    }

    @Override
    public Object parse(String reponse) throws BaseException, JSONException {
        if (this.parseCode(reponse)) {
            JSONObject all = new JSONObject(reponse);
            this.cameraInfoEx.setAdUrl(all.optString(ADURL));
            GetCameraDetailResp.optCameraInfoEx(this.cameraInfoEx, all.getJSONObject(CAMERA));
            JSONObject objDevice = all.getJSONObject(DEVICE);
            GetCameraDetailResp.optDeviceInfoEx(this.deviceInfoEx, objDevice);
            this.deviceInfoEx.setDeviceSwitchInfoList(this.deviceInfoEx.getSwitches());
            try {
                GetCameraDetailResp.optDeviceInfoEx(this.belongDeviceInfoEx, objDevice.getJSONObject(BELONGDEVICE));
            }
            catch (JSONException e) {
                LogUtil.printErrStackTrace("GetCameraDetailResp", e.fillInStackTrace());
            }
        }
        return null;
    }

    public static void optDeviceInfoEx(DeviceInfoEx deviceInfoEx, JSONObject object) throws JSONException {
        if (deviceInfoEx != null && object != null) {
            ReflectionUtils.convJSONToObject(object, deviceInfoEx);
        }
    }

    public static void optCameraInfoEx(CameraInfoEx cameraInfoEx, JSONObject object) throws JSONException {
        if (cameraInfoEx != null && object != null) {
            ReflectionUtils.convJSONToObject(object, cameraInfoEx);
        }
    }
}


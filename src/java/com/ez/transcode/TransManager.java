/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 */
package com.ez.transcode;

import android.text.TextUtils;
import com.ez.transcode.ConvertParam;
import com.ez.transcode.MP4Converter;
import com.ez.transcode.TransCodeListener;

public class TransManager {
    public static MP4Converter initTransCode(final ConvertParam convertParam) {
        MP4Converter converter = new MP4Converter();
        if (!converter.init(convertParam)) {
            converter.release();
            return null;
        }
        converter.setConvertListener(new TransCodeListener(){

            @Override
            public void onDataOutput(byte[] data, int len, int type, long timeStamp) {
                TransManager.onDataCallback(convertParam.dataCB, type, 0, data, len, timeStamp, convertParam.pFCUser);
            }

            @Override
            public void onError(int errorCode, String error) {
                byte[] data = TextUtils.isEmpty((CharSequence)error) ? "unknown error".getBytes() : error.getBytes();
                TransManager.onDataCallback(convertParam.dataCB, 255, errorCode, data, data.length, 0L, convertParam.pFCUser);
            }
        });
        boolean ret = converter.start();
        if (!ret) {
            converter.release();
        }
        return ret ? converter : null;
    }

    public static void deInitTransCode(MP4Converter mp4Converter) {
        if (mp4Converter != null) {
            mp4Converter.release();
            mp4Converter = null;
        }
    }

    public static int transOneFrame(MP4Converter mp4Converter, byte[] data, int len, long timeStamp, long frameType) {
        int ret = -1;
        if (mp4Converter != null) {
            ret = mp4Converter.inputData(data, len, timeStamp, frameType);
        }
        return ret;
    }

    public static native void registerHWCallback(long var0);

    public static native int initSDK();

    public static native void uninitSDK();

    public static native void onDataCallback(long var0, int var2, int var3, byte[] var4, int var5, long var6, long var8);
}


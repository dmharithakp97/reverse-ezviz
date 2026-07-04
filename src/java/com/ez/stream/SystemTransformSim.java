/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 */
package com.ez.stream;

import android.text.TextUtils;
import com.ez.stream.EZGetPercentInfo;
import com.ez.stream.EZMediaTransferInfo;
import com.ez.stream.LogUtil;
import com.ez.stream.SystemTransform;
import com.ez.transcode.TransManager;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SystemTransformSim {
    String TAG = "SystemTransformSim";
    long mTrans = 0L;
    Object mTransSync = new Object();
    String mSrcFile = null;
    String mTgtFile = null;
    volatile boolean mIsStart = false;
    FileOutputStream mFileOut = null;
    String mTargetFile = "";
    String mSourceFile = "";
    SystemTransform.OutputDataCB mCallback = new SystemTransform.OutputDataCB(){

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void onOutputData(byte[] data, int len, int type, int flag) {
            if (SystemTransformSim.this.mFileOut != null) {
                try {
                    SystemTransformSim.this.mFileOut.write(data, 0, len);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (flag == 1) {
                Object object = SystemTransformSim.this.mTransSync;
                synchronized (object) {
                    SystemTransformSim.this.mTransSync.notify();
                }
            }
        }
    };

    public static SystemTransformSim create(byte[] header, int len, int type, String tgtFile) {
        SystemTransformSim systemTransformSim = null;
        if (header == null || tgtFile == null) {
            return systemTransformSim;
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(tgtFile);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            return systemTransformSim;
        }
        systemTransformSim = new SystemTransformSim(header, len, type, null, null, out);
        return systemTransformSim;
    }

    public static SystemTransformSim create(int type, String sourceFile, String tgtFile) {
        SystemTransformSim systemTransformSim = null;
        if (sourceFile == null || tgtFile == null) {
            return systemTransformSim;
        }
        try {
            FileInputStream input = new FileInputStream(sourceFile);
            byte[] header = new byte[40];
            int ret = input.read(header, 0, 40);
            if (ret != 40) {
                return systemTransformSim;
            }
            systemTransformSim = new SystemTransformSim(header, 40, type, sourceFile, tgtFile, null);
        }
        catch (FileNotFoundException e) {
            return systemTransformSim;
        }
        catch (IOException e) {
            return systemTransformSim;
        }
        return systemTransformSim;
    }

    public static SystemTransformSim createEx(int dstType, String sourceFile, String tgtFile) {
        if (sourceFile == null || tgtFile == null) {
            return null;
        }
        return new SystemTransformSim(dstType, sourceFile, tgtFile);
    }

    public EZMediaTransferInfo getFileInfo() {
        return this.getFileInfo(false);
    }

    public EZMediaTransferInfo getFileInfo(boolean enableTraversal) {
        if (this.mTrans != 0L) {
            return SystemTransform.getFileInfo(this.mTrans, enableTraversal);
        }
        return null;
    }

    public void setTargetInfo(EZMediaTransferInfo info) {
        if (this.mTrans != 0L) {
            SystemTransform.setTargetInfo(this.mTrans, info, this.mTargetFile);
        }
    }

    public void release() {
        if (this.mTrans != 0L) {
            SystemTransform.release(this.mTrans);
            this.mTrans = 0L;
        }
        if (this.mFileOut != null) {
            try {
                this.mFileOut.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            this.mFileOut = null;
        }
    }

    private SystemTransformSim(byte[] header, int len, int type, String sourceFile, String targetFile, FileOutputStream out) {
        this.mFileOut = out;
        this.mTargetFile = targetFile;
        this.mSourceFile = sourceFile;
        this.mTrans = SystemTransform.create(header, len, type, sourceFile, this.mCallback);
        if (this.mTrans != 0L) {
            TransManager.registerHWCallback(this.mTrans);
        }
    }

    private SystemTransformSim(int type, String sourceFile, String targetFile) {
        this.mTargetFile = targetFile;
        this.mSourceFile = sourceFile;
        this.mTrans = SystemTransform.createEx(type, sourceFile);
        if (this.mTrans != 0L) {
            TransManager.registerHWCallback(this.mTrans);
        }
    }

    public int start(String secretKey) {
        int ret = 0;
        this.mIsStart = false;
        if (this.mTrans != 0L) {
            this.setSecretKey(secretKey);
            ret = SystemTransform.start(this.mTrans, this.mSourceFile, this.mTargetFile);
            this.mIsStart = ret == 0;
        } else {
            ret = 1;
        }
        return ret;
    }

    public int setSecretKey(String secretKey) {
        int ret = 0;
        if (this.mTrans != 0L) {
            if (!TextUtils.isEmpty((CharSequence)secretKey)) {
                ret = SystemTransform.setEncryptKey(this.mTrans, 1, secretKey);
                return ret;
            }
        } else {
            ret = 1;
        }
        return ret;
    }

    public int setSegmentInterval(int interval) {
        int ret = 0;
        ret = this.mTrans != 0L ? SystemTransform.setSegmentInterval(this.mTrans, interval) : 1;
        return ret;
    }

    public int startEx() {
        int ret = 0;
        this.mIsStart = false;
        if (this.mTrans != 0L) {
            ret = SystemTransform.startEx(this.mTrans);
            this.mIsStart = ret == 0;
        } else {
            ret = 1;
        }
        return ret;
    }

    public int inputData(int dataType, byte[] data, int len) {
        int ret = 3;
        if (this.mTrans != 0L) {
            ret = SystemTransform.inputData(this.mTrans, dataType, data, len);
        }
        return ret;
    }

    public EZGetPercentInfo getPercent() {
        EZGetPercentInfo ret = null;
        if (this.mTrans != 0L) {
            ret = SystemTransform.getPercent(this.mTrans);
        }
        if (ret != null) {
            LogUtil.d("Parsley", "percent info. ret " + ret.ret + " percent " + ret.percent);
        }
        return ret;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void stop() {
        if (this.mTrans != 0L && this.mIsStart) {
            if (0 != SystemTransform.stop(this.mTrans)) {
                Object object = this.mTransSync;
                synchronized (object) {
                    try {
                        this.mTransSync.wait(500L);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            this.mIsStart = false;
        }
    }
}


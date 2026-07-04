/*
 * Decompiled with CFR 0.152.
 */
package com.ez.bwcheck;

public class BWCheckManager {
    private long mHandle = this.createCheckHandle();
    private OnMsgCallback mMsgCallback;

    public void setOnMsgCallback(OnMsgCallback msgCallback) {
        this.mMsgCallback = msgCallback;
    }

    public int startCheck2B(BWCheckReqInfo2B reqInfo, BWCheckType type) {
        if (this.mHandle == 0L) {
            return -1;
        }
        return this.startSpeedCheck2BNative(this.mHandle, reqInfo, type.value);
    }

    public int startCheck2C(BWCheckReqInfo2C reqInfo, BWCheckType type) {
        if (this.mHandle == 0L) {
            return -1;
        }
        return this.startSpeedCheck2CNative(this.mHandle, reqInfo, type.value);
    }

    public void onBWCheckResult(BWCheckResultInner resultInner) {
        if (this.mMsgCallback != null && resultInner != null) {
            BWCheckResult result = new BWCheckResult();
            result.checkType = resultInner.checkType == 1 ? BWCheckType.BWCheckType_TCP_UPLOAD : BWCheckType.BWCheckType_TCP_DOWNLOAD;
            result.downloadSpeed = resultInner.downloadSpeed;
            result.uploadSpeed = resultInner.uploadSpeed;
            result.latency = resultInner.latency;
            result.loss = resultInner.loss;
            result.result = resultInner.result;
            this.mMsgCallback.onBWCheckResult(result);
        }
    }

    public void release() {
        if (this.mHandle != 0L) {
            this.destroyCheckHandle(this.mHandle);
            this.mHandle = 0L;
        }
    }

    native long createCheckHandle();

    native void destroyCheckHandle(long var1);

    native int startSpeedCheck2BNative(long var1, BWCheckReqInfo2B var3, int var4);

    native int startSpeedCheck2CNative(long var1, BWCheckReqInfo2C var3, int var4);

    public static abstract class OnMsgCallback {
        public void onBWCheckResult(BWCheckResult result) {
        }
    }

    public static enum BWCheckType {
        BWCheckType_TCP_UPLOAD(1),
        BWCheckType_TCP_DOWNLOAD(2);

        private final int value;

        private BWCheckType(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    public static class BWCheckReqInfo2B {
        public String opid;
        public String url;
        public byte[] pbkey = new byte[128];
        public int keyver;
    }

    public static class BWCheckReqInfo2C {
        public String opid;
        public String serial;
        public int channel;
        public int authtype;
        public byte[] authtoken = new byte[256];
        public String svrip;
        public int svrport;
        public int checkTime = 20;
        public byte[] pbkey = new byte[128];
        public int keyver;
    }

    public static class BWCheckResult {
        public BWCheckType checkType;
        public float downloadSpeed;
        public float uploadSpeed;
        public int latency;
        public int loss;
        public int result;
    }

    public static class BWCheckResultInner {
        public int checkType;
        public float downloadSpeed;
        public float uploadSpeed;
        public int latency;
        public int loss;
        public int result;
    }

    public static interface OnLogCallback {
        public void onLog(String var1);
    }
}


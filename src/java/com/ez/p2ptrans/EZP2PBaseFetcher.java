/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.sun.jna.Callback
 *  com.sun.jna.Memory
 *  com.sun.jna.Pointer
 *  com.sun.jna.Structure
 *  com.sun.jna.Structure$ByReference
 *  com.sun.jna.Structure$ByValue
 */
package com.ez.p2ptrans;

import com.sun.jna.Callback;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public abstract class EZP2PBaseFetcher {

    public static interface ErrorCallback
    extends Callback {
        public void onError(int var1, Pointer var2);
    }

    public static interface MsgCallback
    extends Callback {
        public void onMsg(int var1, Pointer var2);
    }

    public static class EZP2PRelayBaseMsg
    extends Structure {
        public int logicChannel;
        public String reqId = "";
        public int type;
        public int cmd;
        public int seq;

        protected List<String> getFieldOrder() {
            return Arrays.asList("logicChannel", "reqId", "type", "cmd", "seq");
        }
    }

    public static class EZP2PTransParam {
        public String serial_;
        public int channel_;
        public String token_;
        public String relayAddr_;
        public int relayPort_;
        public int autoType_;
        public int relayPublicKeyVer_;
        public byte[] relayPublicKey_;
        public int timeout_;

        public EZP2PTransParamForAndroid.ByReference toJNA() {
            EZP2PTransParamForAndroid.ByReference param = new EZP2PTransParamForAndroid.ByReference();
            param.serial_ = this.serial_;
            param.channel_ = this.channel_;
            param.token_ = this.token_;
            param.relayAddr_ = this.relayAddr_;
            param.relayPort_ = this.relayPort_;
            param.autoType_ = this.autoType_;
            param.relayPublicKeyVer_ = this.relayPublicKeyVer_;
            if (this.relayPublicKey_ != null) {
                param.relayPublicKey_ = new Memory((long)this.relayPublicKey_.length);
                param.relayPublicKey_.write(0L, this.relayPublicKey_, 0, this.relayPublicKey_.length);
                param.relayPublicKeyLen_ = this.relayPublicKey_.length;
            }
            param.timeout_ = this.timeout_;
            return param;
        }
    }

    public static class EZP2PTransParamForAndroid
    extends Structure {
        public String serial_;
        public int channel_;
        public String token_;
        public String relayAddr_;
        public int relayPort_;
        public int autoType_;
        public int relayPublicKeyVer_;
        public Pointer relayPublicKey_;
        public int relayPublicKeyLen_;
        public int timeout_;

        protected List<String> getFieldOrder() {
            return Arrays.asList("serial_", "channel_", "token_", "relayAddr_", "relayPort_", "autoType_", "relayPublicKeyVer_", "relayPublicKey_", "relayPublicKeyLen_", "timeout_");
        }

        public static class ByValue
        extends EZP2PTransParamForAndroid
        implements Structure.ByValue {
        }

        public static class ByReference
        extends EZP2PTransParamForAndroid
        implements Structure.ByReference {
        }
    }

    public static interface BaseCallback {
        public void onMsg(int var1);

        public void onError(int var1);
    }
}


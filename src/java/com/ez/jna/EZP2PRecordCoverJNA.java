/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.sun.jna.Callback
 *  com.sun.jna.Pointer
 *  com.sun.jna.Structure$ByReference
 *  com.sun.jna.Structure$ByValue
 */
package com.ez.jna;

import com.ez.p2ptrans.EZP2PBaseFetcher;
import com.sun.jna.Callback;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class EZP2PRecordCoverJNA {

    public static interface RespCallback
    extends Callback {
        public void onResp(EZRecordResp var1, Pointer var2, int var3);
    }

    public static class EZRecordResp
    extends EZP2PBaseFetcher.EZP2PRelayBaseMsg {
        public int result;
        public String timeStamp;
        public String timeLapseTaskId;
        public int length;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("logicChannel", "reqId", "type", "cmd", "seq", "result", "timeStamp", "timeLapseTaskId", "length");
        }

        public static class ByValue
        extends EZRecordResp
        implements Structure.ByValue {
        }

        public static class ByReference
        extends EZRecordResp
        implements Structure.ByReference {
        }
    }

    public static class EZRecordReq
    extends EZP2PBaseFetcher.EZP2PRelayBaseMsg {
        public String serial;
        public int channel;
        public String startTime;
        public String stopTime;
        public int recordType;
        public String timeLapseTaskId;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("logicChannel", "reqId", "type", "cmd", "seq", "serial", "channel", "startTime", "stopTime", "recordType", "timeLapseTaskId");
        }

        public static class ByValue
        extends EZRecordReq
        implements Structure.ByValue {
        }

        public static class ByReference
        extends EZRecordReq
        implements Structure.ByReference {
        }
    }
}


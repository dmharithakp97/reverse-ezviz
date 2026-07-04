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

public class EZP2PDevFileJNA {

    public static interface RespCallback
    extends Callback {
        public void onResp(EZP2PDevFileResp.ByReference var1, Pointer var2);
    }

    public static class EZP2PDevFileResp
    extends EZP2PBaseFetcher.EZP2PRelayBaseMsg {
        public int channel;
        public int result;
        public int fileIndex;
        public int fileNum;
        public int curSize;
        public int fileSize;
        public int fileTotalSize;
        public String serial;
        public String fileName;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("logicChannel", "reqId", "type", "cmd", "seq", "channel", "result", "fileIndex", "fileNum", "curSize", "fileSize", "fileTotalSize", "serial", "fileName");
        }

        public static class ByValue
        extends EZP2PDevFileResp
        implements Structure.ByValue {
        }

        public static class ByReference
        extends EZP2PDevFileResp
        implements Structure.ByReference {
        }
    }

    public static class EZP2PDevFileReq
    extends EZP2PBaseFetcher.EZP2PRelayBaseMsg {
        public String serial;
        public int channel;
        public int fileType;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("logicChannel", "reqId", "type", "cmd", "seq", "serial", "channel", "fileType");
        }

        public static class ByValue
        extends EZP2PDevFileReq
        implements Structure.ByValue {
        }

        public static class ByReference
        extends EZP2PDevFileReq
        implements Structure.ByReference {
        }
    }
}


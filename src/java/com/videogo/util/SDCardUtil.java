/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Environment
 *  android.os.StatFs
 */
package com.videogo.util;

import android.os.Environment;
import android.os.StatFs;
import java.io.File;

public class SDCardUtil {
    public static final long PIC_MIN_MEM_SPACE = 0xA00000L;
    public static final long REC_MIN_MEM_SPACE = 0x1400000L;

    @Deprecated
    public static File getSDCardPath() {
        return Environment.getExternalStorageDirectory();
    }

    @Deprecated
    public static long getSDCardRemainSize() {
        StatFs statfs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long blockSize = statfs.getBlockSize();
        long availableBlocks = statfs.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    @Deprecated
    public static boolean isSDCardUseable() {
        return Environment.getExternalStorageState().equals("mounted");
    }
}


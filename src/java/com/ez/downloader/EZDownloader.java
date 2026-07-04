/*
 * Decompiled with CFR 0.152.
 */
package com.ez.downloader;

import com.ez.downloader.EZBaseDownloader;
import com.ez.stream.EZStreamClientManager;
import com.ez.stream.InitParam;
import com.ez.stream.NativeApi;

public class EZDownloader
extends EZBaseDownloader {
    public EZDownloader(EZStreamClientManager ezStreamClientManager, InitParam initParam, String dstFilePath) {
        super(ezStreamClientManager, initParam, dstFilePath);
    }

    @Override
    protected long createClient() {
        return NativeApi.createDownloadClient(this.mInitParam, this.dstFilePath);
    }
}


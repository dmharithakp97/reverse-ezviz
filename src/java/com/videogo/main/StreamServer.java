/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.main;

import com.videogo.main.IspInfo;
import java.util.List;

public class StreamServer {
    private int externalCmdPort;
    private int externalDataPort;
    private String index;
    private int internalCmdPort;
    private int internalDataPort;
    private int loading;
    private int type;
    private List<IspInfo> ispInfos;

    public String getIndex() {
        return this.index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public int getExternalCmdPort() {
        return this.externalCmdPort;
    }

    public void setExternalCmdPort(int externalCmdPort) {
        this.externalCmdPort = externalCmdPort;
    }

    public int getExternalDataPort() {
        return this.externalDataPort;
    }

    public void setExternalDataPort(int externalDataPort) {
        this.externalDataPort = externalDataPort;
    }

    public int getInternalCmdPort() {
        return this.internalCmdPort;
    }

    public void setInternalCmdPort(int internalCmdPort) {
        this.internalCmdPort = internalCmdPort;
    }

    public int getInternalDataPort() {
        return this.internalDataPort;
    }

    public void setInternalDataPort(int internalDataPort) {
        this.internalDataPort = internalDataPort;
    }

    public int getLoading() {
        return this.loading;
    }

    public void setLoading(int loading) {
        this.loading = loading;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<IspInfo> getIspInfos() {
        return this.ispInfos;
    }

    public void setIspInfos(List<IspInfo> ispInfos) {
        this.ispInfos = ispInfos;
    }
}


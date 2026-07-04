/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.stream;

public enum DevProtoEnum {
    GB28181("gb28181");

    private String protoName;

    private DevProtoEnum(String protoName) {
        this.protoName = protoName;
    }

    public String getProtoName() {
        return this.protoName;
    }
}


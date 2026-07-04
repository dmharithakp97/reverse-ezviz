/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.ui.setting.storage;

public class Storage {
    String index;
    String type;
    int capacity;
    String name;
    char status;
    int formatRate;

    public String getIndex() {
        return this.index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public char getStatus() {
        return this.status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public int getFormatRate() {
        return this.formatRate;
    }

    public void setFormatRate(int formatRate) {
        this.formatRate = formatRate;
    }
}


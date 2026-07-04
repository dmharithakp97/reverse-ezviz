/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.SerializedName
 */
package com.videogo.ptz;

import com.google.gson.annotations.SerializedName;

public class P2pPtzRequest {
    @SerializedName(value="commandId")
    public int commandId = 33281;
    @SerializedName(value="content")
    public P2pPtzContent content;

    public P2pPtzRequest(int channel, int cmd, int action, int speed, String uuid) {
        this.content = new P2pPtzContent(channel, cmd, action, speed, uuid);
    }

    private class P2pPtzContent {
        @SerializedName(value="channel")
        public int channel;
        @SerializedName(value="cmd")
        public int cmd;
        @SerializedName(value="action")
        public int action;
        @SerializedName(value="speed")
        public int speed;
        @SerializedName(value="uuid")
        public String uuid;
        @SerializedName(value="TimeStamp")
        public long timeStamp;

        public P2pPtzContent(int channel, int cmd, int action, int speed, String uuid) {
            this.channel = channel;
            this.cmd = cmd;
            this.action = action;
            this.speed = speed;
            this.uuid = uuid;
            this.timeStamp = System.currentTimeMillis();
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package com.ezviz.hcsadp.Interface;

import android.util.Log;
import com.ezviz.hcsadp.jna.HCSadpSDKByJNA;
import com.ezviz.hcsadp.jna.HCSadpSDKJNAInstance;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

public class MulticastReceive
extends Thread {
    private MulticastSocket m_multicastSocket;
    public final int MAX_PACKET_LENGTH = 20480;
    private boolean m_bExit = false;

    MulticastReceive(MulticastSocket ms) {
        this.m_multicastSocket = ms;
    }

    @Override
    public void run() {
        super.run();
        Log.i((String)"MulticastReceive", (String)"MulticastReceive run begin!");
        HCSadpSDKByJNA.BYTE_ARRAY data = new HCSadpSDKByJNA.BYTE_ARRAY(20481);
        try {
            while (!this.m_bExit) {
                byte[] buffer = new byte[20480];
                DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
                this.m_multicastSocket.receive(receivePacket);
                String s = new String(receivePacket.getData()).substring(0, receivePacket.getLength());
                System.arraycopy(receivePacket.getData(), 0, data.byValue, 0, receivePacket.getLength());
                data.write();
                HCSadpSDKJNAInstance.getInstance().SADP_ParseData(data.getPointer(), receivePacket.getLength());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        Log.i((String)"MulticastReceive", (String)"MulticastReceive run end!");
    }

    public void SetExit() {
        this.m_bExit = true;
    }
}


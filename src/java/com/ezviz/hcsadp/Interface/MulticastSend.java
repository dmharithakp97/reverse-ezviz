/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package com.ezviz.hcsadp.Interface;

import android.util.Log;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastSend
extends Thread {
    private MulticastSocket m_multicastSocket;
    private InetAddress m_inetAddress;
    public final int MULTICAST_PORT = 37020;
    private boolean m_bExit = false;
    private final String INQUIRY_PACKET = "<?xml version=\"1.0\" encoding=\"utf-8\"?><Probe><Uuid>B6F61EFB-2B77-4C54-9C32-8E0B4104922F</Uuid><Types>inquiry</Types></Probe>";

    MulticastSend(MulticastSocket multicastSocket, InetAddress inetAddress) {
        this.m_multicastSocket = multicastSocket;
        this.m_inetAddress = inetAddress;
    }

    @Override
    public void run() {
        super.run();
        Log.i((String)"MulticastSend", (String)"MulticastSend run begin!");
        DatagramPacket sendPacket = new DatagramPacket("<?xml version=\"1.0\" encoding=\"utf-8\"?><Probe><Uuid>B6F61EFB-2B77-4C54-9C32-8E0B4104922F</Uuid><Types>inquiry</Types></Probe>".getBytes(), "<?xml version=\"1.0\" encoding=\"utf-8\"?><Probe><Uuid>B6F61EFB-2B77-4C54-9C32-8E0B4104922F</Uuid><Types>inquiry</Types></Probe>".length(), this.m_inetAddress, 37020);
        try {
            while (!this.m_bExit) {
                this.m_multicastSocket.send(sendPacket);
                MulticastSend.sleep(5000L);
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        Log.i((String)"MulticastSend", (String)"MulticastSend run end!");
    }

    public void SetExit() {
        this.m_bExit = true;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.ezviz.hcsadp.Interface;

import com.ezviz.hcsadp.Interface.MulticastReceive;
import com.ezviz.hcsadp.Interface.MulticastSend;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class MulticastServer {
    public final String MULTICAST_IP = "239.255.255.250";
    public final int MULTICAST_PORT = 37020;
    private MulticastSocket m_multicastSocket;
    private InetAddress m_inetAddress;
    private MulticastReceive m_multicastReceive;
    private MulticastSend m_multicastSend;

    public boolean start() {
        try {
            this.m_inetAddress = InetAddress.getByName("239.255.255.250");
            this.m_multicastSocket = new MulticastSocket(37020);
            this.m_multicastSocket.setLoopbackMode(true);
            this.m_multicastSocket.setTimeToLive(32);
            this.m_multicastSocket.joinGroup(this.m_inetAddress);
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        }
        catch (SocketException e) {
            e.printStackTrace();
            return false;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        this.m_multicastSend = new MulticastSend(this.m_multicastSocket, this.m_inetAddress);
        this.m_multicastSend.start();
        this.m_multicastReceive = new MulticastReceive(this.m_multicastSocket);
        this.m_multicastReceive.start();
        return true;
    }

    public void stop() {
        this.m_multicastSend.SetExit();
        this.m_multicastReceive.SetExit();
        try {
            this.m_multicastSocket.leaveGroup(this.m_inetAddress);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.m_multicastSocket.close();
        try {
            this.m_multicastSend.join();
            this.m_multicastReceive.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


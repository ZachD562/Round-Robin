/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package node;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author igodz
 */
public class NodePCMThread extends Thread {
    
    private final String name;
    private final int port;
    private final int coordPort;
    private final String coordIP;
    
    
    //Constructor
    //**************************************************************************
    public NodePCMThread(String name, int port, String coordIP, int coordPort) {
        this.name = name;
        this.port = port;
        this.coordIP = coordIP;
        this.coordPort = coordPort;
    }
    
    
    // Thread to send TEST messages back to the coordinator
    //**************************************************************************
    @Override
    public void run() {
        try {
            InetAddress address = InetAddress.getByName(coordIP);
            String msg = "PCM, " + name;
            DatagramPacket sendingPacket = new DatagramPacket(msg.getBytes(), msg.getBytes().length, address, coordPort);
            DatagramSocket sendingSocket = new DatagramSocket(port + 3);
            sendingSocket.send(sendingPacket);
            sendingSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

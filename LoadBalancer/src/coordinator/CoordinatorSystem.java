/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinator;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.NoSuchElementException;
import node.Node;

/**
 *
 * @author igodz
 */

public class CoordinatorSystem {
    
    private final int coordinatorPort;
    
    private int lastNodeIndex = 0;
    public String lastNodeName = "";

    //Constructor
    //**************************************************************************
    public CoordinatorSystem(int port) {
        coordinatorPort = port;
    }
    
    
    //Method for retriving the coordinator port set from constructor
    //**************************************************************************
    public int getCoordinatorPort() {
        return coordinatorPort;
    }
    
    
    //Message functions
    //**************************************************************************
    public void SendJOBMessage(CoordinatorListener listener, int time) {
        try {
            Node nextNode;
            try {
                nextNode = listener.manager.iterator(lastNodeIndex + 1);
                if(!listener.manager.checkForNodeName(nextNode.getNodeName())) {
                    nextNode = listener.manager.iterator(lastNodeIndex + 1);    
                }
            } catch (NoSuchElementException e) {
                lastNodeIndex = 0;
                nextNode = listener.manager.iterator(lastNodeIndex);
            }
            lastNodeName = nextNode.getNodeName();
            lastNodeIndex = listener.manager.findNodeIndex(lastNodeName);
                        
            InetAddress address = InetAddress.getByName(nextNode.getNodeIP());
            String message = "JOB," + time;
            DatagramPacket packet = new DatagramPacket(message.getBytes(), message.getBytes().length, address, nextNode.getNodePort());
            DatagramSocket socket = new DatagramSocket(coordinatorPort + 2);
            socket.send(packet);
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void SendERRORMessage(String nodeIP, int nodePort, String error) {
        try {
            InetAddress address = InetAddress.getByName(nodeIP);
            String message = "ERROR, " + error;
            DatagramPacket packet = new DatagramPacket(message.getBytes(), message.getBytes().length, address, nodePort);
            DatagramSocket socket = new DatagramSocket(coordinatorPort + 6);
            socket.send(packet);
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }    
    


    
    
}

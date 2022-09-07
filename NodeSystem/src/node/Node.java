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
public class Node {
    private final int maxJobs;
    
    private String nodeName;
    private String nodeIP;
    private int nodePort;
    private int currentJobs;
    
    // Constructors
    //**************************************************************************
    public Node(String name, String ip, int port, int maxjobs) {
        nodeName = name;
        nodeIP = ip;
        nodePort = port;
        maxJobs = maxjobs;
    }
    
    
    // Get & Set functions 
    //************************************************************************** 
    public String getNodeIP() {
        return nodeIP;
    }
    public void setNodeIP(String nodeIP) {
        this.nodeIP = nodeIP;
    }
            
    
    public int getNodePort() {
        return nodePort;
    }   
    public void setNodePort(int nodePort) {
        this.nodePort = nodePort;
    }
      
    
    public String getNodeName() {
        return nodeName;
    }   
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }
    
    
    // Max and current job functions
    //************************************************************************** 
    public int getMaxJobs() {
        return maxJobs;
    }
    
    public int calculateWeight() {
        return maxJobs - currentJobs;
    }
    
    
    // Message Functions
    //**************************************************************************
    public void SendREGMessage(String coordIP, int coordPort) {
        try {
            InetAddress address = InetAddress.getByName(coordIP);
            String message = "REG, " + nodeName + ", " + nodeIP + ", " + nodePort + ", " + maxJobs;
            DatagramPacket packet = new DatagramPacket(message.getBytes(), message.getBytes().length, address, coordPort);
            DatagramSocket socket = new DatagramSocket(nodePort + 2);
            socket.send(packet);
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void SendERRORMessage(String coordIP, int coordPort, String error) {
        try {
            InetAddress address = InetAddress.getByName(coordIP);
            String message = "ERROR, " + nodeName + ", " + error;
            DatagramPacket packet = new DatagramPacket(message.getBytes(), message.getBytes().length, address, coordPort);
            DatagramSocket socket = new DatagramSocket(nodePort + 6);
            socket.send(packet);
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Debugging fuctions 
    //**************************************************************************  
    
    
    
    
}
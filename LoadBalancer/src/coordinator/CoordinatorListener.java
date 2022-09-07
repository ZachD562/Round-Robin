/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinator;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.LinkedList;
import node.Node;

/**
 *
 * @author igodz
 */
public class CoordinatorListener extends Thread {
    
    public NodeManager manager = new NodeManager();
    public LinkedList<String> nodesStillConnected = new LinkedList<String>();
    public LinkedList compJobData = new LinkedList();
    
    public int port;
    public String userIP;
    public int userPort;
    public int JTD = 0;
    public int JTS = 0;
    public int TIMS = 0;
     
    
    
    public boolean isInList(String name) {   
        for(int i=0; i < nodesStillConnected.size(); i++) {
            String nodeName = nodesStillConnected.get(i);
            if(nodeName != null) {
                if(nodeName.trim().equalsIgnoreCase(name.trim())) {
                    return true;
                }
            }
        }
        return false;
    }
   
    public int getJTD() {
        return JTD;
    }
    
    public String getUserIP() {
        return userIP;
    }
    
    public int getUserPort() {
        return userPort;
    }
    
    public void completeJob() {
        JTD -= 1;
    }
    
    //Constructor
    //**************************************************************************
    public CoordinatorListener(int port) {
        this.port = port;
    }
    
    //
    //**************************************************************************
    @Override
    public void run() { 
        try {            
            System.out.println("Running Coorninator.....");
            DatagramSocket socket = new DatagramSocket(port);
            socket.setSoTimeout(0);
            
            while(true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String message = new String(buffer);
                String[] info = message.split(",");
                switch(info[0].strip()) {
                    case"REG" -> {
                        String Name = info[1].strip();
                        String IP = info[2].strip();
                        String prt = info[3].strip();
                        String mj = info[4].strip();
                        Integer Port = Integer.valueOf(prt.trim());
                        Integer maxJobs = Integer.valueOf(mj.trim());
                        if (manager.checkForNodeName(Name.trim())) {
                            InetAddress address = InetAddress.getByName(IP);
                            String msg = "CON, 0";
                            DatagramPacket pckt = new DatagramPacket(msg.getBytes(), msg.getBytes().length, address, Port);
                            DatagramSocket sckt = new DatagramSocket(port + 2);
                            sckt.send(pckt);
                            sckt.close();                                
                        } else {
                            manager.registerNode(Name, IP, Port, maxJobs);
                            nodesStillConnected.add(Name);
                            InetAddress address = InetAddress.getByName(IP);
                            String msg = "CON, 1";
                            DatagramPacket pckt = new DatagramPacket(msg.getBytes(), msg.getBytes().length, address, Port);
                            DatagramSocket sckt = new DatagramSocket(port + 2);
                            sckt.send(pckt);
                            sckt.close(); 
                            System.out.println(Name + " Successfully registered");                               
                        }                     
                    }                    
                    case"PCM" -> {
                        String name = info[1].trim();
                        if(isInList(name) == false ) {
                            nodesStillConnected.add(name);
                        }                        
                    }
                    case"USERINFO" -> {
                        userIP = info[1].trim();
                        String port = info[2];
                        userPort = Integer.valueOf(port.trim());
                        System.out.println("Got User info:\n  IP " + userIP + "\n  Port " + userPort);
                    } 
                    case"NEWJOB" -> {
                        String num = info[1].strip();
                        Integer amount = Integer.valueOf(num.trim());
                        String secs = info[2].strip();
                        Integer time = Integer.valueOf(secs.trim());
                        JTD = amount;
                        TIMS = time;
                    }
                    case"COMJOB" -> {
                        String nodeName = info[1];
                        compJobData.add(nodeName);
                        JTS += 1; 
                    }
                    case"SHUTDOWN" -> {
                        System.out.println("Received shutdown message\n  Shutting down nodes ... ");
                        Node node;
                        for(int i=0; i<manager.getList().size(); i++) {
                        node = (Node)manager.getList().get(i);
                            if(node != null) {                                
                                InetAddress address = InetAddress.getByName(node.getNodeIP());
                                String msg = "SHUTDOWN, ";
                                DatagramPacket pckt = new DatagramPacket(msg.getBytes(), msg.getBytes().length, address, node.getNodePort());
                                DatagramSocket sckt = new DatagramSocket(port + 5);
                                sckt.send(pckt);
                                sckt.close(); 
                            }
                        }
                        System.out.println("Shutting down coordinator ... ");
                        System.exit(0);
                    }    
                    case"ERROR" -> {
                        String nodeName = info[1];
                        String errorType = info[2];
                        System.out.println(nodeName + " has incountered " + errorType + " error\n"
                                + "Please contact system admin");
                        Node node = manager.findNode(nodeName);
                        InetAddress address = InetAddress.getByName(node.getNodeIP());
                        String msg = "SHUTDOWN, ";
                        DatagramPacket pckt = new DatagramPacket(msg.getBytes(), msg.getBytes().length, address, node.getNodePort());
                        DatagramSocket sckt = new DatagramSocket(port + 6);
                        sckt.send(pckt);
                        sckt.close();
                        System.out.println("Removing " + nodeName);
                        int nodeIndex = manager.findNodeIndex(nodeName);
                        manager.getList().remove(nodeIndex);
                        
                    }
                }  
            }                    
        } catch(IOException | NumberFormatException e) {
            e.printStackTrace();
        }            
    }
}

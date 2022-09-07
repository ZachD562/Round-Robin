/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package node;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


/**
 *
 * @author igodz
 */


public class NodeSystem {
    
       
    public static void main(String[] args) {       
            NodeInfo getInfo = new NodeInfo();
            Node node;            
                       
            getInfo.getNodeInfo();           
            node = new Node(getInfo.getName(), getInfo.getIP(), getInfo.getPort(), getInfo.getMaxJobs());
            NodeListener listeningThread = new NodeListener(getInfo.getName(), getInfo.getIP(), getInfo.getPort(), getInfo.getMaxJobs(), getInfo.getCoordIP(), getInfo.getCoordPort());
            listeningThread.start();               
            long endTime = System.currentTimeMillis() + 500;
            while (System.currentTimeMillis() < endTime) {            
            } 
            
            if(listeningThread.threadRuning  == false) {
                boolean setPortLoop = false;
                while(setPortLoop == false) {
                    getInfo.setPort(getInfo.getPort() + 500);
                    System.out.println("Changing to port " + getInfo.getPort());
                    node.setNodePort(getInfo.getPort());
                    listeningThread = new NodeListener(getInfo.getName(), getInfo.getIP(), getInfo.getPort(), getInfo.getMaxJobs(), getInfo.getCoordIP(), getInfo.getCoordPort());
                    listeningThread.start(); 
                    endTime = System.currentTimeMillis() + 500;             
                    while (System.currentTimeMillis() < endTime) {            
                    }
                    setPortLoop = listeningThread.threadRuning;
                }
            }   
            
            node.SendREGMessage(getInfo.getCoordIP(), getInfo.getCoordPort());                
            endTime = System.currentTimeMillis() + 1000;             
            while (System.currentTimeMillis() < endTime) {            
            } 

            if(listeningThread.canConnect == false) {                
                boolean setNameLoop = false;
                while(setNameLoop == false) {
                    getInfo.setName();   
                    node.setNodeName(getInfo.getName());
                    listeningThread.setName(getInfo.getName());
                    node.SendREGMessage(getInfo.getCoordIP(), getInfo.getCoordPort());
                    endTime = System.currentTimeMillis() + 1000;             
                    while (System.currentTimeMillis() < endTime) {            
                    }
                    setNameLoop = listeningThread.canConnect;
                }      
            }
            System.out.println("Node successfully registered\nWaiting ...");
            
            try {
                while(true) {
                    endTime = System.currentTimeMillis() + 500;
                    while (System.currentTimeMillis() < endTime) {                             
                    }
                    if(NodeListener.JTS > 0) {
                        for(int i = NodeListener.JTS; i > 0; i--) {
                            try {
                                InetAddress address = InetAddress.getByName(getInfo.getCoordIP());
                                String msg = "COMJOB, " + getInfo.getName();
                                DatagramPacket pckt = new DatagramPacket(msg.getBytes(), msg.getBytes().length, address, getInfo.getCoordPort());
                                DatagramSocket sckt = new DatagramSocket(getInfo.getPort() + 5);
                                sckt.send(pckt);  
                                sckt.close();
                                NodeListener.JTS -= 1;
                            } catch (IOException e) {
                                listeningThread.SendERRORMessage("IOException");
                            }
                        }
                    }
                    
                    else if(listeningThread.storedJobs.size() > 0 && NodeListener.currentJobs <= 0) {
                        while(listeningThread.storedJobs.size() > 0) {
                            Object jobInfoObject = listeningThread.storedJobs.getFirst();
                            String jobInfo = String.valueOf(jobInfoObject);
                            String[] info = jobInfo.split(",");
                            String tskNm = info[0];
                            Integer taskNum = Integer.valueOf(tskNm.trim());
                            listeningThread.taskNumber += 1;
                            endTime = System.currentTimeMillis() + 300;
                            while (System.currentTimeMillis() < endTime) {         
                            }
                            NodeJOBMThread jobThread = new NodeJOBMThread(taskNum, info);
                            jobThread.start();
                            NodeListener.currentJobs += 1;
                            listeningThread.storedJobs.removeFirst();  
                        }
                        if(NodeListener.JTS > 0) {
                            for(int i = NodeListener.JTS; i > 0; i--) {
                                try {
                                    InetAddress address = InetAddress.getByName(getInfo.getCoordIP());
                                    String msg = "COMJOB, " + getInfo.getName();
                                    DatagramPacket pckt = new DatagramPacket(msg.getBytes(), msg.getBytes().length, address, getInfo.getCoordPort());
                                    DatagramSocket sckt = new DatagramSocket(getInfo.getPort() + 5);
                                    sckt.send(pckt);  
                                    sckt.close();
                                    NodeListener.JTS -= 1;
                                } catch (IOException e) {
                                    listeningThread.SendERRORMessage("IOException");
                                }
                            }
                        }
                        
                    }
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
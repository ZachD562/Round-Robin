/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinator;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import node.Node;



/**
 *
 * @author igodz
 */
public class Coordinator {
    
//Variable values being used from the Listening thread in the main class
//******************************************************************************
    static int JTD = 0;
    static int JTS = 0;
    static int TIMS = 0;
            
    
    public static void main(String[] args) {
        
    //Get the coordinator infomation and initalise all required threads 
    //**************************************************************************
        CoordinatorInfo coordInfo = new CoordinatorInfo();
        if (coordInfo.isPort()) {
            CoordinatorListener listeningThread = new CoordinatorListener(coordInfo.getCoordPort());
            CoordinatorPCMThread testThread = new CoordinatorPCMThread(listeningThread);
            CoordinatorSystem coordSys = new CoordinatorSystem(coordInfo.getCoordPort());
            listeningThread.start();
            testThread.start(); 
            
    
        //Main coordinator loop for doing the three main functions
        //**********************************************************************        
            while(true) {   
                JTD = listeningThread.JTD;
                JTS = listeningThread.JTS;
                TIMS = listeningThread.TIMS;
                
                
            //Sending of incomplete jobs from user client(s)
            //******************************************************************
                if(0 < JTD) {
                    for(int i = JTD; i > 0; i--) {
                        if(listeningThread.manager.listHasNext()) {
                            coordSys.SendJOBMessage(listeningThread, TIMS);
                            System.out.println("Sent Job to: " + coordSys.lastNodeName);
                            listeningThread.completeJob();
                        } else {
                            System.out.println("No Nodes ...");
                            long endTime = System.currentTimeMillis() + 1000;
                            while (System.currentTimeMillis() < endTime) {       
                            }     
                        }
                    }                   
                } 
                
            //Sending of completed jobs back to the user
            //******************************************************************
                else if (0 < JTS) {
                    for(int i = JTS; i > 0; i--) {
                        try {
                            Object getFirstNN = listeningThread.compJobData.getFirst();
                            String nodeName = String.valueOf(getFirstNN);
                            InetAddress address = InetAddress.getByName(listeningThread.getUserIP());
                            String msg = "COMJOB," + nodeName;
                            DatagramPacket pckt = new DatagramPacket(msg.getBytes(), msg.getBytes().length, address, listeningThread.getUserPort());
                            DatagramSocket sckt = new DatagramSocket(coordSys.getCoordinatorPort() + 5);
                            sckt.send(pckt);
                            sckt.close();
                            listeningThread.compJobData.removeFirst();
                            listeningThread.JTS -= 1;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }                    
                } else {
                    long endTime = System.currentTimeMillis() + 1000;
                    while (System.currentTimeMillis() < endTime) {       
                    } 
                }
                
            //Removing any inactive nodes from the coordination system
            //******************************************************************
                if(!listeningThread.manager.getList().isEmpty()){
                    listeningThread.nodesStillConnected.clear();
                    long endTime = System.currentTimeMillis() + 5000;
                    while (System.currentTimeMillis() < endTime) {       
                    }     
                    for(int i=0; i < listeningThread.manager.getList().size(); i++) {
                        Node node = null;
                        node = (Node)listeningThread.manager.getList().get(i);
                        boolean inList = false;
                        for(int k=0; k < listeningThread.nodesStillConnected.size(); k++) {
                            String connectedNode = listeningThread.nodesStillConnected.get(k);
                            if(node.getNodeName().equals(connectedNode)) {
                                inList = true;
                                break;
                            }
                        }
                        if(inList == false) {
                            System.out.println("Removing " + node.getNodeName());
                            listeningThread.manager.getList().remove(i);
                        }
                    }    
                }               
            } 
        } else {
            System.out.println("ERROR SETTING PORT");
        }        
    }        
     
}




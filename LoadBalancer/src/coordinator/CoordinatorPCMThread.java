/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinator;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.LinkedList;
import node.Node;

/**
 *
 * @author igodz
 */
public class CoordinatorPCMThread extends Thread {
    
    LinkedList listenerList;
    int port;
    
    
    //Constructor
    //**************************************************************************
    public CoordinatorPCMThread(CoordinatorListener listener) {
        listenerList = listener.manager.getList();
        port = listener.port;
    }
    
    
    //Thread for sending TEST messages to all registered nodes
    //**************************************************************************
    @Override
    public void run() {
        while(true){
            try {
                Node node;
                for (int i=0; i < listenerList.size(); i++) {
                    node = (Node)listenerList.get(i);
                    InetAddress address = InetAddress.getByName(node.getNodeIP());
                    String message = "PCM, Connection";
                    DatagramPacket packet = new DatagramPacket(message.getBytes(), message.getBytes().length, address, node.getNodePort());
                    DatagramSocket socket = new DatagramSocket(port + 1);
                    socket.send(packet);
                    socket.close();
                    long endTime = System.currentTimeMillis() + 200;
                    while (System.currentTimeMillis() < endTime) {       
                    }                     
                }  
                long endTime = System.currentTimeMillis() + 500;
                while (System.currentTimeMillis() < endTime) {       
                }                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }    
}
    


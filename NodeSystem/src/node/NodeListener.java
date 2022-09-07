/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package node;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.BindException;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.LinkedList;

/**
 *
 * @author igodz
 */

public class NodeListener extends Thread{
    
    LinkedList storedJobs = new LinkedList();
    
    public static int currentJobs = 0;
    public static int JTS = 0;
    
    
    public int taskNumber = 0;
    public int maxJobs;    
    
    private final int coordPort;
    private final String coordIP; 
    private String[] info;
    private int port;
    private String name;

    
    public boolean canConnect = false;
    public boolean threadRuning = false;
    public boolean Error = false;

    
    // Constructor
    //**************************************************************************
    public NodeListener(String name, String ip, int port, int maxjobs, String coordIP, int coordPort) {       
        this.port = port;
        this.maxJobs = maxjobs;
        this.name = name;
        this.coordIP = coordIP;
        this.coordPort = coordPort;
    }
    
    public void SendERRORMessage(String error) {
        try {
            InetAddress address = InetAddress.getByName(coordIP);
            String message = "ERROR, " + name + ", " + error;
            DatagramPacket packet = new DatagramPacket(message.getBytes(), message.getBytes().length, address, coordPort);
            DatagramSocket socket = new DatagramSocket(port + 6);
            socket.send(packet);
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    public boolean getCanConnect() {
        return canConnect;
    }
    
    public void setNodeName(String name) {
        this.name = name;
    }
    
    public void setNodePort(int port) {
        this.port = port;
    }
    
    // Listening switch case thread 
    //**************************************************************************
    @Override
    public void run() {
        try {
            
            DatagramSocket listeningSocket = new DatagramSocket(port);
            listeningSocket.setSoTimeout(0);
            threadRuning = true;
            
            while(Error == false) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                listeningSocket.receive(packet);
                String message = new String(buffer);
                info = message.split(",");
                switch(info[0].strip()) {
                    case"CON" -> {
                        String confm = info[1];
                        Integer confirm = Integer.valueOf(confm.trim());
                        if(confirm == 0){
                            canConnect = false;
                        } else if (confirm == 1) {
                            canConnect = true;
                        }
                    }                    
                    case"PCM" -> {
                        long endTime = System.currentTimeMillis() + 200;
                        while (System.currentTimeMillis() < endTime) {         
                        }
                        NodePCMThread pcmThread = new NodePCMThread(name, port, coordIP, coordPort);
                        pcmThread.start();
                    }
                    case"JOB" -> { 
                        if (currentJobs < maxJobs) {
                            taskNumber += 1;
                            long endTime = System.currentTimeMillis() + 300;
                            while (System.currentTimeMillis() < endTime) {         
                            }
                            NodeJOBMThread jobThread = new NodeJOBMThread(taskNumber, info);
                            jobThread.start();
                            currentJobs += 1;
                        } else {
                            taskNumber += 1;
                            System.out.println("Storing job " + taskNumber);
                            String tskNum = String.valueOf(taskNumber);
                            String time = info[1].strip();
                            String job = tskNum + ", " + time;
                            storedJobs.add(job);
                        }                                              
                    }
                    case"SHUTDOWN" -> {
                        System.out.println("Got system shutdown message\n  Shutting down node .... ");
                        System.exit(0);
                    }                    
                }
            }             
        } catch(BindException BX) {
            System.out.println("Port " + port + " already in use"); 
            Error = true;
        } catch (SocketException SX) {
            System.out.println("Socket Exception has occured");
            Error = true;
            SendERRORMessage("SocketException");
        } catch (IOException IOX) {
            System.out.println("IO Exception has occured");
            Error = true;
            SendERRORMessage("IOException");
        }

    }
    
}

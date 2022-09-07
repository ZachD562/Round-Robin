/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 *
 * @author igodz
 */
public class UserListener extends Thread {

    private final int port;
    
    public UserListener(int port) {
        this.port = port;
    }


    @Override
    public void run() { 
        try {            
            System.out.println("Running User.....");
            DatagramSocket socket = new DatagramSocket(port);
            socket.setSoTimeout(0);
            
            while(true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String message = new String(buffer);
                String[] info = message.split(",");
                switch(info[0].strip()) {
                    case"COMJOB" -> {
                        String nodeName = info[1];
                        System.out.println("Got job completed from " + nodeName);
                    }
                }            
            }            
        } catch (Exception e) {
            
        }
    }

}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

/**
 *
 * @author igodz
 */
public class User {
    
    public static void main(String[] args) throws IOException {
        
        boolean infoSent = false;
        
        Scanner scan = new Scanner(System.in);
        UserInfo getUserInfo = new UserInfo();
        UserListener listeningThread = new UserListener(getUserInfo.getUserPort());
        listeningThread.start();
        
        try {
            InetAddress address = InetAddress.getByName(getUserInfo.getCoordIP());
            String message = "USERINFO, " + getUserInfo.getUserIP() + ", " + getUserInfo.getUserPort();
            DatagramPacket packet = new DatagramPacket(message.getBytes(), message.getBytes().length, address, getUserInfo.getCoordPort());
            DatagramSocket socket = new DatagramSocket(getUserInfo.getUserPort() + 1);
            socket.send(packet);
            socket.close();
            infoSent = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        while(infoSent == true) {
            String choice = "";
            System.out.println("What would you like to do"
                    + "\n  (Job) -  send jobs"
                    + "\n  (Shutdown) -  shutdown all nodes and coordinator"
                    + "\n  (Exit) -  shutdown current user client"
                    + "\n***not case sensetive***\n");
            choice = scan.nextLine();
            if(choice.strip().equalsIgnoreCase("job")) {
                System.out.println("How many jobs do you want to send ");
                String num = scan.nextLine();
                System.out.println("How long do you want these jobs to be (In milliseconds - 1000 = 1sec)");
                String time = scan.nextLine();
                try {
                    InetAddress address = InetAddress.getByName(getUserInfo.getCoordIP());
                    String message = "NEWJOB, " + num + ", " + time;
                    DatagramPacket packet = new DatagramPacket(message.getBytes(), message.getBytes().length, address, getUserInfo.getCoordPort());
                    DatagramSocket socket = new DatagramSocket(getUserInfo.getUserPort() + 2);
                    socket.send(packet);
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (choice.strip().equalsIgnoreCase("shutdown")) {
                System.out.println("Are you sure you wan to shut down the whole system? (y/n)");
                choice = scan.nextLine();
                if (choice.strip().equalsIgnoreCase("y")) {
                    try {
                        InetAddress address = InetAddress.getByName(getUserInfo.getCoordIP());
                        String message = "SHUTDOWN,";
                        DatagramPacket packet = new DatagramPacket(message.getBytes(), message.getBytes().length, address, getUserInfo.getCoordPort());
                        DatagramSocket socket = new DatagramSocket(getUserInfo.getUserPort() + 3);
                        socket.send(packet);
                        socket.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }    
                } else if(choice.strip().equalsIgnoreCase("n")) {
                    System.out.println("Cancelled shutdown command");    
                }
            } else if (choice.strip().equalsIgnoreCase("exit")) {
                System.out.println("Are you sure you wan to shut down this user? (y/n)");
                choice = scan.nextLine();
                if (choice.strip().equalsIgnoreCase("y")) { 
                    System.out.println("Shutting down user ...");
                    System.exit(0);
                } else if (choice.strip().equalsIgnoreCase("n")) { 
                    System.out.println("Cancelled exit command");
                } else {
                    
                }
            } 
        }
    }    
}

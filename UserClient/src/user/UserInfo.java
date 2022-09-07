/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user;

import java.util.Scanner;

/**
 *
 * @author igodz
 */
public class UserInfo {
   boolean breakLoop = false;
   boolean IPLoop = false;
    boolean coordPortLoop = false;
    boolean coordIPLoop = false;
    boolean portLoop = false;
    String choice = "";
    int port;
    int coordPort;
    String ip;
    String coordIP;
    
    public UserInfo() {
        Scanner scan = new Scanner(System.in);
        while (breakLoop == false) {
            while (portLoop == false) {
                System.out.println("Enter a port number between 1000 - 2000");
                try {
                    String prt = scan.nextLine();
                    Integer PRT = Integer.valueOf(prt.trim());
                    port = PRT;
                    portLoop = true;
                } catch (NumberFormatException e) {
                    System.out.println("Incorrect Value ... ");
                }
            }                       
            while (IPLoop == false) {
                System.out.println("Enter User IP Address: \n Format - X.X.X.X");
                ip = scan.nextLine();
                if (ip.trim().equals("")) {
                    System.out.println("Please enter an IP");
                } else {
                    IPLoop = true;
                }
            }
            while (coordIPLoop == false) {
                System.out.println("If the Coordinator has the same IP as this machine put (y)\n Or set it manually (n)");
                choice = scan.nextLine();
                if (choice.equalsIgnoreCase("y")) {
                    System.out.println("Setting IP of " + ip + "\n");
                    coordIP = ip;
                    choice = "";
                    coordIPLoop = true;
                } else if (choice.equalsIgnoreCase("n")) {
                    System.out.println("Enter IP Address of Coordinator: ");
                    coordIP = scan.nextLine();
                    if (coordIP.trim().equals("")) {
                        System.out.println("Please enter an IP");
                    } else {
                        coordIPLoop = true;
                    }   
                }

            }
            while (coordPortLoop == false) {
                System.out.println("If the Coordinator is using the default port number put (y)\n Or set it manually (n)");
                choice = scan.nextLine();
                if (choice.equalsIgnoreCase("y")) {
                    System.out.println("Setting default value of 4000");
                    coordPort = 4000;
                    choice = "";
                    coordPortLoop = true;
                } else if (choice.equalsIgnoreCase("n")) {
                    System.out.println("Enter Port number of Coordinator: ");
                    try {
                        String crPrt = scan.nextLine();
                        Integer CRPRT = Integer.valueOf(crPrt.trim());
                        coordPort = CRPRT;
                        choice = "";
                        coordPortLoop = true;
                    } catch (NumberFormatException e) {
                        System.out.println("Incorrect Value ...");
                    }    
                }                
            }
            System.out.println( "\nAre you happy with the following details?\n  User IP Address: " + ip + 
                    "\n  User Port Number: " + port +
                    "\n  Coordinator IP address: " + coordIP + 
                    "\n  Coordinator Port number: " + coordPort);
            System.out.println("(y/n)");
            choice = scan.nextLine();
            if(choice.equalsIgnoreCase("y")) {
                break;
            } else if (choice.equalsIgnoreCase("n")) {
                System.out.println("Restarting.... \n");
                    breakLoop = false;
                    coordIPLoop = false;
                    portLoop = false;
            } else {
                
            }
        }
    }
    
    public int getCoordPort() {
        return coordPort;
    }
    
    public String getCoordIP(){
        return coordIP;
    }
    
    public int getUserPort() {
        return port;
    }
     public String getUserIP() {
         return ip;
     }
}

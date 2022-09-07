/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package node;

import java.util.Scanner;

/**
 *
 * @author igodz
 */



public class NodeInfo {
    
    
    Scanner scan = new Scanner(System.in);
    
    private String name;
    private String num;   
    private int Num;
    private String ip = "";   
    private String sp;
    private int Port;   
    private String mj;
    private int maxJobs;
    private String coordIP;
    private String crPrt;
    private int coordPort;
    
    private boolean breakLoop = false;
    private boolean nameLoop = false;
    private boolean ipLoop = false;
    private boolean portLoop = false;
    private boolean mjLoop = false;
    private boolean coordIPLoop = false;
    private boolean coordPortLoop = false;   
    
    private static String choice = "";
    
    //Getting Node information from the user
    //**************************************************************************
    
    public void getNodeInfo(){
        
        breakLoop = false;
        nameLoop = false;
        ipLoop = false;
        portLoop = false;
        mjLoop = false;
        coordIPLoop = false;
        coordPortLoop = false;
        
        while (breakLoop == false) {
            while (nameLoop == false) {
                System.out.println("Enter Node number: ");
                try {
                    num = scan.nextLine();
                    Num = Integer.valueOf(num.trim());
                    name = "Node-" + Num;
                    nameLoop = true;
                } catch (NumberFormatException e) {
                    System.out.println("Incorrect Value ...");
                }               
            }
            while (ipLoop == false) {
                System.out.println("Enter Node IP Address:\n  Format - X.X.X.X");
                ip = scan.nextLine();
                if (ip.trim().equals("")) {
                    System.out.println("Please enter an IP");
                } else {
                    ipLoop = true;
                }
            }
            while (portLoop == false) {
                System.out.println("Enter Node Port number in range (5000-10000) ending with 0:\n  For example - 5550 or 6450");
                try {
                    sp = scan.nextLine();
                    Integer SP = Integer.valueOf(sp.trim());
                    Port = SP;
                    portLoop = true;
                } catch (NumberFormatException e) {
                    System.out.println("Incorrect Value ...");
                }
                
            }
            while (mjLoop == false) {
                System.out.println("Enter Node Max Jobs: ");
                try {
                    mj = scan.nextLine();
                    Integer MJ = Integer.valueOf(mj.trim());
                    maxJobs = MJ;
                    mjLoop = true;
                } catch (NumberFormatException e) {
                    System.out.println("Incorrect Value ...");
                }                
            }
            while (coordIPLoop == false) {
                System.out.println("If the Coordinator has the same IP as this machine put (y)\n  Or set it manually (n)");
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
                        crPrt = scan.nextLine();
                        Integer CRPRT = Integer.valueOf(crPrt.trim());
                        coordPort = CRPRT;
                        choice = "";
                        coordPortLoop = true;
                    } catch (NumberFormatException e) {
                        System.out.println("Incorrect Value ...");
                    }    
                }                
            }
            System.out.println( "\nAre you happy with the following details?\n  Node name: " + name + 
                    "\n  IP address: " + ip + 
                    "\n  Port number: " + Port + 
                    "\n  Max Jobs: " + maxJobs + 
                    "\n  Coordinator IP: " + coordIP + 
                    "\n  Coordinator Port: " + coordPort);
            System.out.println("(y/n)");
            choice = scan.nextLine();
            if(choice.equalsIgnoreCase("y")) {
                break;
            } else if (choice.equalsIgnoreCase("n")) {
                System.out.println("Restarting.... \n");
                    breakLoop = false;
                    nameLoop = false;
                    ipLoop = false;
                    portLoop = false;
                    mjLoop = false;
                    coordIPLoop = false;
                    coordPortLoop = false; 
            } else {                
            }
        }
    }
    
    
    //Methods for retreiving the class information
    //**************************************************************************
    
    public String getName() {
        return name;
    }
    public void setName() {
        boolean newNameLoop = false;
        while (newNameLoop == false) {
            System.out.println("Name 'Node-" + Num + "' already in use");
            System.out.println("Enter Node number: ");
            try {
                String newNum = scan.nextLine();
                Integer Number = Integer.valueOf(newNum.trim());
                Num = Number;
                name = "Node-" + Number;
                newNameLoop = true;
            } catch (NumberFormatException e) {
                System.out.println("Incorrect Value ...");
            }               
        }
    }
 
    
    public String getIP() {
        return ip;
    }    

    
    public int getPort() {
        return Port;
    }
    public void setPort(int port) {
        Port = port;
    }
   
    
    public int getMaxJobs() {
        return maxJobs;
    }   
    
    
    public String getCoordIP() {
        return coordIP;
    }
    
    
    public int getCoordPort() {
        return coordPort;
    }
        
}

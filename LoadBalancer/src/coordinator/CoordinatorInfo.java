/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinator;

import java.util.Scanner;

/**
 *
 * @author igodz
 */
public class CoordinatorInfo {
    boolean breakLoop = false;
    boolean setPortLoop = false;
    String choice = "";
    int port;

    
//Constructor is used so that when called, it will ask for a port from the admin 
//they have the option of ether a preset or custom port number
//******************************************************************************
    public CoordinatorInfo(){
        Scanner scan = new Scanner(System.in);
        while (breakLoop == false) {
            System.out.println("Would you like to use default port number 4000? (y)\nOr use a custom port number? (n)");
            choice = scan.nextLine();
            if (choice.equalsIgnoreCase("n")) {
                while (setPortLoop == false) {
                    System.out.println("Enter port number: ");
                    try {
                        String num = scan.nextLine();
                        Integer prt = Integer.valueOf(num.trim());
                        port = prt;
                        setPortLoop = true;
                        breakLoop = true;
                    } catch (Exception e) {
                        System.out.println("Incorrect Value ...");
                    }     
                }   
            } else if (choice.equalsIgnoreCase("y")) {
                System.out.println("Using default port number of 4000");
                this.port = 4000;
                breakLoop = true;
            } else {
                System.out.println("Incorrent input");
            }
        }
    }

    
//Get method for giving access to the coordinator port in other classes   
//******************************************************************************
    public int getCoordPort() {
        return port;
    }


//Validation method for use in main method to make sure the port is not 0
//******************************************************************************    
    public boolean isPort() {
        return this.port > 0;
    }
    
}


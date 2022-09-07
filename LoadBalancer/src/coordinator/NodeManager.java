/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinator;

import java.util.ListIterator;
import java.util.LinkedList;
import node.Node;

/**
 *
 * @author igodz
 */
public class NodeManager {
    
    private LinkedList<Node> availableNodes = new LinkedList<Node>();;
        
    // Node handling
    //************************************************************************** 
    public void registerNode(String Name, String IP, int Port, int maxJobs) {
        Node node = new Node(Name, IP, Port, maxJobs);
        availableNodes.add(node);
    }
    
    public Node findNode(String name) {
        Node node = null;      
        for(int i=0; i<availableNodes.size(); i++) {
            node = (Node)availableNodes.get(i);
            if(node != null) {
                if(node.getNodeName().equals(name)) {
                    return node;
                }
            }
        }
        return node;
    }
    
    public int findNodeIndex (String name) {
        Node node = null;      
        for(int i=0; i<availableNodes.size(); i++) {
            node = (Node)availableNodes.get(i);
            if(node != null) {
                if(node.getNodeName().equals(name)) {
                    return i;
                }
            }
        }
        return 0;
    }
    
    public boolean checkForNodeName(String name) {
        Node node;      
        for(int i=0; i<availableNodes.size(); i++) {
            node = (Node)availableNodes.get(i);
            if(node != null) {
                if(node.getNodeName().equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }  
    
    public LinkedList getList() {
        return availableNodes;
    }
    
    public boolean listHasNext() {
        ListIterator<Node> it = availableNodes.listIterator();
        return it.hasNext();
    }
    
    public Node iterator(int x){
        Node node;
        ListIterator it = availableNodes.listIterator(x);
        node = (Node)it.next();
        return node;
    }
    
    // Debugging fuctions 
    //**************************************************************************    
}

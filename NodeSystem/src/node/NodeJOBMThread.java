/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package node;


/**
 *
 * @author igodz
 */
public class NodeJOBMThread extends Thread{

    private final int taskNumber;
    private final String[] info;


    
    public NodeJOBMThread(int taskNumber, String[] info) {
        this.taskNumber = taskNumber;
        this.info = info;
    }
    
    @Override
    public void run() {

        System.out.println("Doing job " + taskNumber);
        String time = info[1].strip();
        Integer Time = Integer.valueOf(time.trim());
        long endTime = System.currentTimeMillis() + Time;            
        while (System.currentTimeMillis() < endTime) {
        }
        NodeListener.JTS += 1;
        NodeListener.currentJobs -= 1;
        System.out.println("Job " + taskNumber + " complete");
    }
}

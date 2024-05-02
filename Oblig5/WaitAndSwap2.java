package Oblig5;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.lang.Math.*;

public class WaitAndSwap2 {


    static int N = 1;

    static boolean first = true;
    static int debuglevel = 9;  // 4: varispeed resumption; 3: varispeed delay time; 2: sems values; 1, : (not implemented) 
    static boolean variableSpeedThreads = true;
    static double variableSpeedRate = 0.0; // threads sleep for a random time between 0 and this rate in milliseconds
    static int extraSlowThreads = 0; // number of threads that sleep 10x variableSpeedRate
 

    static Semaphore s1 = new Semaphore(1);
    static Semaphore s2 = new Semaphore(0);

    static boolean isEven = false;
    static boolean isOdd = true;


 

    
    public static void variSpeed(int id, int iteration) { // let the calling thread sleep a random time
        long myWait = (long) (Math.random() * variableSpeedRate);
        if (variableSpeedRate == 0.0) return;
        if (id < extraSlowThreads) myWait = (long) (variableSpeedRate * 10.0); 
        // make the first <extraSlowThreads> always wait 10xvariableSpeedRate
        debugPrintln(id, iteration, 3, "         variSpeed delay: " + myWait + " ms");
        if (variableSpeedThreads) 
           try {
              TimeUnit.MILLISECONDS.sleep(myWait);
           } catch (Exception e) { return;}; 
        debugPrintln(id, iteration, 4, "         resuming after variSpeed delay");
    }
  
     public static void debugPrintln(int id, int iteration, int buglevel, String msg) {
        if (debuglevel >= buglevel) {  // then print the message
           System.out.println("Thread " + id + ", " + iteration + msg);  
        }     
    }
    

    public static void waitAndSwap(int id, int iteration){
        variSpeed(id, iteration);
        try {
            if(id% 2== 0 ){
                
                variSpeed(id, iteration);
                s1.acquire();
                isEven =true ;
                isOdd = false;
                
                variSpeed(id, iteration);
                System.out.println("Thread "+id + " finished");
                s2.release();
                
            }else {
                variSpeed(id, iteration);
                s2.acquire();
                System.out.println("Thread "+id + " finished");        
                isOdd = true;
                variSpeed(id, iteration);
                s1.release();
                
            }
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    
        variSpeed(id, iteration);
    }
    
    
    static class Worker extends Thread {
        int localId; 

        public Worker(int id){

            localId= id;

        }

        @Override
        public void run() {
            try {
                TimeUnit.MILLISECONDS.sleep((long) localId*1000); // let them start in order.
             } catch (Exception e) { return;}; 
                
            for (int i = 0; i < N; i++) {
                waitAndSwap(localId++, 0);
                
           
                
            }
        }

    }



    public static void main(String[] args) {
        

        int numThreads =8;
        for (int j = 0; j <4; j++) {
            System.out.println("-------------[Run "+ j+ "]-------------------");
            Worker[] workers = new Worker[numThreads];
            for (int i = 0; i < numThreads; i++) {
                
                
                workers[i] = new Worker(i+1);
                workers[i].start();
                
            }
            System.out.println("----------------------------------------------");
        }
    }
}

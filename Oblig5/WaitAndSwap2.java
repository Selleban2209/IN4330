package Oblig5;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.lang.Math.*;

public class WaitAndSwap2 {


    static int N = 1;

    static CyclicBarrier cb;
    static int debuglevel = 9;  // 4: varispeed resumption; 3: varispeed delay time; 2: sems values; 1, : (not implemented) 
    static boolean variableSpeedThreads = true;
    static double variableSpeedRate = 0.0; // threads sleep for a random time between 0 and this rate in milliseconds
    static int extraSlowThreads = 0; // number of threads that sleep 10x variableSpeedRate
 

    static Semaphore outer = new Semaphore(1, true);
    static Semaphore lastWait = new Semaphore(-1, true);
    static Semaphore flipBool = new Semaphore(1, true);
    static Semaphore firstWait = new Semaphore(0, true);

    static boolean first = true;
    static boolean odd= false;


 

    
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
    
        try {
            outer.acquire();
            
            if(first){
                first = false;
                outer.release();
            }
            flipBool.acquire();
            odd = !odd;

            if(odd){
                flipBool.release();

                lastWait.release();
                firstWait.acquire();

                lastWait.acquire();
            } else {
                flipBool.release();
                firstWait.release();
            }

            outer.release();

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    
        
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
                waitAndSwap(localId, 0);
                
            }
            System.out.println("Thread "+localId + " finished");    
        }

    }




    @SuppressWarnings("deprecation")
    public static void main(String[] args) {
        
        //default set to 8 if not specified
        int numThreads =8;
        int runs = 1;
        
        try {
            numThreads = Integer.parseInt(args[0]);
            runs = Integer.parseInt(args[1]);
        } catch (Exception e) {
            System.out.println("Usage: Oblig5.java <t> <n> ");
            System.exit(0);
        }
     //   cb = new CyclicBarrier(runs-1);

        System.out.println("Number of threads: " + numThreads);

        for (int j = 0; j <runs; j++) {
            System.out.println("\n-----------------[Run "+ (j+1)+ "]-----------------------");
            Worker[] workers = new Worker[numThreads];
            for (int i = 0; i < numThreads; i++) {
            
                
                workers[i] = new Worker(i+1);
                workers[i].start();
                
            }
            for (int x=0; x <numThreads-1; x++) {
                try {

                    if( x== numThreads-1 && numThreads-1 %2==1)workers[numThreads-1].stop();
                    workers[x].join();
                  //  System.out.println("HIHIHAHA");
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            }
        }
    }
}

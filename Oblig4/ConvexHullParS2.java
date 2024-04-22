import java.util.Arrays;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.*;
public class ConvexHullParS2 {

    int n, MAX_X, MAX_Y,MIN_X, MIN_Y;
    int x[], y[];
    int numThreads;
    CyclicBarrier cb;
    
    public ConvexHullParS2(int n, int seed, int numThreads){
        this.n = n;
        x = new int[n];
        y = new int[n];
        NPunkter17 p = new NPunkter17(n, seed);
        this.numThreads = numThreads; 
        p.fyllArrayer(x, y);      
        cb = new CyclicBarrier(numThreads); 
    }


    
    class Worker extends Thread{
        IntList localKoHyll;
        IntList workerSubset;
        int workerFurthest;
        int p1, p2;
        int start, end;
        int local_MAX_X, local_MAX_Y ;
        int local_MIN_X, local_MIN_Y ;
        int []localX ;
        int []localY ; 
        
        public Worker(int start , int end){ 
            this.start= start;
            this.end= end;
            this.localKoHyll = new IntList();
            localX=  Arrays.copyOfRange(x, start, end);
            localY = Arrays.copyOfRange(y, start, end);
          
        }
        
        @Override
        public void run() {
            
         
        }
    }
    

    
    

    
}

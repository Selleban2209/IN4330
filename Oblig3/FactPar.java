import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.ThreadFactory; 
public class FactPar {


    ExecutorService executor;
    long N; 
    long N2;
    int [] primesToN;
    Oblig3Precode ob3Pre;
    int threadsNum;
    int k;



    public FactPar(long n, int []primes, Oblig3Precode ob3Pre){
        N= n;
        N2 = n*n;
        primesToN= primes;
        this.ob3Pre = ob3Pre;
        k = primes.length;
    }


    public class Worker extends Thread  {  
    
        public Worker(){
           
        }

        @Override
        public void run() {

        }
    }
    


    public void factN2Par(){   
        for (long i = N2-100; i < N2; i++) {
            factNumberPar(i);      
        }
        ob3Pre.writeFactors();
    }

    public void factNumberPar(long number){ 

        
        long base = number; 
        AtomicLong baseNum=  new AtomicLong(base);
        threadsNum = Runtime.getRuntime().availableProcessors();
        executor= Executors.newFixedThreadPool(threadsNum-1);


      
        baseNum.set(number);
        for (int j = 0; j < threadsNum; j++) {
            final int start= (k/threadsNum)*j;
            int currEnd = (k/threadsNum) * (j+1);
            if(j==threadsNum -1 )currEnd = k;
            
            final int end = currEnd;

            executor.submit(() -> {
                String str = "";
                // Your code here
                for (int y=start;y<end; y++) {
                    Long prime = (long) primesToN[y];
                    str +=  prime.toString()+ "*";
                    if( baseNum.get() != 1 && baseNum.get()%prime ==0){
                        while( baseNum.get()  != 1 && baseNum.get()  % prime ==0){
                                synchronized(baseNum){       
                                ob3Pre.addFactor(baseNum.get(), prime);             
                                baseNum.set( baseNum.get()/ prime);
                            }   
                        }
                    }
                }
                System.out.println(str);
            });

        }
        executor.shutdown();
        while(!executor.isTerminated()){};
        if(number != 1)ob3Pre.addFactor(base, number);  
       
    }



    
}

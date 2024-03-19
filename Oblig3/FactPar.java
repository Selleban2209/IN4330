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
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
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



    public FactPar(long n, int []primes,int threadsNum, Oblig3Precode ob3Pre){
        N= n;
        N2 = n*n;
        primesToN= primes;
        this.ob3Pre = ob3Pre;
        this.threadsNum = threadsNum;
        k = primes.length;
    }

    

    public void factN2Par(){   
        for (long i = N2-100; i < N2; i++) {
            factNumberPar(i);      
        }
        ob3Pre.writeFactors();
    }



    public void factNumberPar(long number){ 

        ConcurrentLinkedQueue<Long> factors = new ConcurrentLinkedQueue<>();
        long base = number; 
        AtomicLong baseNum=  new AtomicLong(base);
         CountDownLatch latch = new CountDownLatch(threadsNum);
        
        executor= Executors.newFixedThreadPool(threadsNum);

      
       // baseNum.set(base);
        for (int j = 0; j < threadsNum; j++) {
            final int start= (k/threadsNum)*j;
            int currEnd = (k/threadsNum) * (j+1);
            if(j==threadsNum -1 )currEnd = k;
            
            final int end = currEnd;

            executor.submit(() -> {
                for (int y = start; y < end; y++) {
                    long prime = primesToN[y];
                    while (baseNum.get() != 1 && baseNum.get() % prime == 0) {
                        baseNum.set( baseNum.get()/ prime);
                        factors.add(prime);
                    }
                }
                latch.countDown(); 
            });
        }

    
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        executor.shutdown();
        while(!executor.isTerminated()){};
        if(baseNum.get() != 1)factors.add( baseNum.get());  
        
        for (Long factor : factors) {
            ob3Pre.addFactor(base, factor);
            
        }
       
    }



    
}

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit; 
import java.util.concurrent.ThreadFactory;   
public class SieveOfEratosthenesPar {

    ExecutorService executor;
    byte[] oddNumbers;
    int runningThreads=0;
    int threadsNum;
    int n, root, numOfPrimes;
    Queue<Integer> primes = new LinkedList<Integer>();
    static CyclicBarrier cb;
    Semaphore sf= new Semaphore(1);
    CyclicBarrier endingBarrier;

    public SieveOfEratosthenesPar(int n) {
        this.n = n;
        root = (int) Math.sqrt(n);
        oddNumbers = new byte[(n / 16) + 1];
    }



    
    public class Worker extends Thread  {

        int workId;
        public int start;
		public int end;

        public Worker(int start, int end,int id){
            workId= id;
            this.start= start;
			this.end = end;
        }



        @Override
        public void run() {
           // System.out.println("start "+ start + " end " + end);

            try {
                sf.acquire();
            } catch (InterruptedException e) {
                // System.out.println("thread " + id + " interrupted");
            }

            if (runningThreads >= threadsNum) {
                sf.release();
               

            }
            sf.release();
            traversePar(start,end );
      
            runningThreads--;

        }
    }
    
    public int[] collectPrimes() {
        int start = (root % 2 == 0) ? root + 1 : root + 2;

        for (int i = start; i <= n; i += 2)
            if (isPrime(i))
                numOfPrimes++;

        int[] primes = new int[numOfPrimes];

        primes[0] = 2;

        int j = 1;

        for (int i = 3; i <= n; i += 2)
            if (isPrime(i))
                primes[j++] = i;

        return primes;

    }

    /**
     * Performs the Sieve Of Eratosthenes
     */
    private void sievePar() {
        mark(1);
        numOfPrimes = 1;
        int prime = nextPrime(1);
		threadsNum = Runtime.getRuntime().availableProcessors();
        Worker[] workers = new Worker[threadsNum];
        ArrayList<Integer> markedPrimes = new ArrayList<>();
        cb = new CyclicBarrier(threadsNum+1);
        executor= Executors.newFixedThreadPool(threadsNum);
       
       
        while (prime != -1) {
            //int currentPrime = prime; 
            traversePar(prime, n);
                
       
            
            prime = nextPrime(prime);
            numOfPrimes++;
        }
        executor.shutdown();

        while(!executor.isTerminated()){};
    }

    public void traversePar(int prime , int end ) {
        CountDownLatch latch = new CountDownLatch(threadsNum);
        cb= new CyclicBarrier(threadsNum+1);
        int start = prime *prime;
        int step = prime *2;
        int total= ((n-(start))/step)+1;
        int chunk = total /threadsNum;
        int rest= total%threadsNum;
        
      //  System.out.println("chunk size"+ chunk);
        int currStart=0;
        for (int j = 0; j < threadsNum; j++) {
            currStart = start + j*chunk*step+ Math.min(j, rest)*step;
            
            int increment= (j <rest? chunk +1: chunk)*step;
       
            int currEnd =Math.min(currStart+increment,n+1 );
        
     
           
            final int threadStart= currStart;  
            final int threadEnd= currEnd;
           
            executor.execute(() -> {      
                for (int i = threadStart; i <= threadEnd; i += prime * 2)
                mark(i);

                latch.countDown(); 
            });
        } 
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Finds the next prime in the sequence. If there are no more left, it
     * simply returns -1.
     * 
     * @param prev The last prime that has been used to mark all non-primes.
     * @return The next prime or -1 if there are no more primes.
     */
    private int nextPrime(int prev  ) {
        for (int i = prev + 2; i <= root; i += 2)
            if (isPrime(i))
                return i;

        return -1;
    }



    public int [] getPrimesPar(){
        if (n <= 1)
        return new int[0];

        sievePar();

        return collectPrimes();
    }

 

    void mark(int num) {
        int bitIndex = (num % 16) / 2;
        int byteIndex = num / 16;
        oddNumbers[byteIndex] |= (1 << bitIndex);
    }

    boolean isPrime(int num) {
        int bitIndex = (num % 16) / 2;
        int byteIndex = num / 16;

        return (oddNumbers[byteIndex] & (1 << bitIndex)) == 0;
    }    

    public static void printPrimes(int[] primes) {
        for (int prime : primes)
            System.out.println(prime);
    }
    
    
}


/*
* 
int start = prime *prime;
int step = prime *2;
int total= ((n-(prime*prime))/step+1);
int chunk = total /threadsNum;
*/
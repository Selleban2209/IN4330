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

        ArrayList<Integer> collect = new ArrayList<>();
      

        collect.add(2);

        for (int i = 3; i <= n; i += 2)
            if (isPrime(i)) collect.add(i);

        return collect.stream().mapToInt(Integer::intValue).toArray();

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
            int currentPrime = prime;
            executor.execute(() -> {
                traversePar(currentPrime, n);
            });
         
            prime = nextPrime(prime);
            numOfPrimes++;
        }
        executor.shutdown();

        while(!executor.isTerminated()){};
    }

    public synchronized void traversePar(int prime , int end ) {
        
        for (int i = prime*prime; i <= end; i += prime * 2)
            mark(i);
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
     
		//threadsNum = Runtime.getRuntime().availableProcessors();
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
    
    
}


/*
* 
int start = prime *prime;
int step = prime *2;
int total= ((n-(prime*prime))/step+1);
int chunk = total /threadsNum;
*/
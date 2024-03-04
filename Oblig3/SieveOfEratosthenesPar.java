import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.CyclicBarrier;
public class SieveOfEratosthenesPar {
    
    byte[] oddNumbers;
    
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
       // while (threadsNum * root > n) threadsNum /= 2;
        while (prime != -1) {

            for (int j = 0; j < threadsNum; j++) {
                int start= (root/threadsNum)*j+prime;
               // if(j == 0) start = prime;
                int end = (root/threadsNum) * (j+1)+prime;
                if(j==threadsNum -1 )end =n; 

                workers[j] = new Worker(start, end,j);
                workers[j].start();
            } 
            prime = nextPrime(prime);
            numOfPrimes++;
            try {
                cb.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }           
        }
    }
    public void traversePar(int prime , int end ) {
        for (int i = prime * prime; i <= end; i += prime * 2)
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
            // TODO Auto-generated method stub
            traversePar(start,end );
            
            System.out.println("start "+ start + " end " + end);
            try {
                cb.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
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

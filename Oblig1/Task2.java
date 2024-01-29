import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;


public class Task2 {
   
    public static int n;
    public static int k;
    public static int[] numbers;
    static CyclicBarrier cb;
    
    static class Worker extends Thread {
        private int begin;
        public int end;
        int myCounter = 0;
        public int innerK;
        public Worker( int begin, int end) {
            this.begin = begin;
            this.end = end-1;
            this.innerK= begin+ k;
        }
        
    
        @Override
        public void run() {
            System.out.println("begin: "+ begin+ " end " + end);
            InsertionSort.insertSort(numbers, begin, innerK-1);
            InsertionSort.compare(numbers, innerK, end);

            try {
                cb.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                System.out.println("Failure...");
                System.exit(-1);
            }
       
            
        }
    
    }
    public static void main(String[] args) {
    
        
        try {
            n = Integer.parseInt(args[0]);
            k = Integer.parseInt(args[1]);
        } catch (Exception e) {
            System.out.println("Usage: Oblig1 <N> <K>");
            System.exit(0);
        }

        numbers = new int[n];
        Random random = new Random(7363);
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = random.nextInt(n);
        }
        
        System.out.println("before Sorted A2/w threads "+ Arrays.toString(numbers));

        //final int threads = 4; 
        int threadsNum = Runtime.getRuntime().availableProcessors();
        
        
        long timeStart = System.currentTimeMillis() ;
        while (threadsNum * k > n) threadsNum /= 2;
        System.out.println(threadsNum+ " how many threads i have");
        Thread[] workers = new Thread[threadsNum];
        
        int rest = n % threadsNum;
        cb = new CyclicBarrier(threadsNum+1); 
        for (int i = 0; i <threadsNum; i++) {
            int begin= n/threadsNum*i;
            int end = n/threadsNum * (i+1) ;
            workers[i] = new Worker(begin, end);
            workers[i].start();
        }
        workers[threadsNum-1] = new Worker(n-(n / threadsNum)-rest, n);
        workers[threadsNum-1].start();
    
        try {
            for (int j = 0; j < workers.length; j++) {
                workers[j].join();
                }
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
      
        long timefinish = System.currentTimeMillis();
        long elapsedTime = timefinish- timeStart;
        System.out.println("\n\nElapsed time: " + elapsedTime + " ms");
     //   System.out.println("Sorted A2/w threads "+ Arrays.toString(numbers));

    }
    
}



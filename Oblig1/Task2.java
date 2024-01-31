import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;


public class Task2 {
   
    public static int n;
    public static int k;
    public static int[] numbers;
    static CyclicBarrier cb;


    private static String presentChunk (int begin, int end ){
        StringBuilder sb = new StringBuilder("[");
        for (int i = begin; i < end; i++) {
            sb.append(numbers[i]);
            if (i < end -1) sb.append(" ");
        }

            return sb+"]";
    
    }
    
    static class Worker extends Thread {
        public int begin;
        public int end;
        int myCounter = 0;
        public int innerK;
        public Worker( int begin, int end) {
            this.begin = begin;
            this.end = end;
            this.innerK= begin+ k;
        }
        
        //System.out.println("begin: "+ begin+ " end " + end);
     
        
        @Override
        public void run()  {
            InsertionSort.insertSort(numbers, begin, innerK);
            InsertionSort.compare(numbers, innerK, end-1);
            
           // System.out.println(presentChunk(begin, end));
            
            try {
                cb.await();
                //System.out.println("begin "+ begin + " end "+ end+ " innerK "+ innerK);
            } catch (InterruptedException | BrokenBarrierException e) {
                System.out.println("Failure...");
                System.exit(-1);
            }
           
        }
    
    }
    public static void main(String[] args){
    
        
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
        
       // System.out.println("before Sorted A2/w threads "+ Arrays.toString(numbers));

        //final int threads = 4; 
        int threadsNum = Runtime.getRuntime().availableProcessors();
        
        
        System.out.println(threadsNum+ " how many threads i have");
        long timeStart = System.currentTimeMillis() ;
        while (threadsNum * k > n) threadsNum /= 2;
        Thread[] workers = new Thread[threadsNum];
        
        int rest = n % threadsNum;
        int[] ok = new int[threadsNum];
        cb = new CyclicBarrier(threadsNum+1); 
        for (int i = 0; i <threadsNum; i++) {
            
            int begin= n/threadsNum*i;
            int end = (n/threadsNum) * (i+1) ;
            if(i==threadsNum -1 ) end = n;     
            ok[i] = begin;    
            workers[i] = new Worker(begin, end);
            workers[i].start();
        }
        
        
        
        try {
            cb.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            System.out.println("Failure...");
            System.exit(-1);
        }
  
       // System.out.println("INdexeds "+ Arrays.toString(ok));
       // InsertionSort.compare(numbers, k-1, numbers.length-1);
        long timefinish = System.currentTimeMillis();
        long elapsedTime = timefinish- timeStart;
       //System.out.println("Sorted A2/w threads "+ Arrays.toString(numbers));
        System.out.println("\nElapsed time: " + elapsedTime + " ms");

    }
    
}



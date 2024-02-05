import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;


public class Task2 {
   
    public static int n;
    public static int k;
    public static int[] numbers;
    public static int[] A2SortPar;
    static CyclicBarrier cb;
    static Object lock = new Object();

    private static String presentChunk (int begin, int end ){
        StringBuilder sb = new StringBuilder("[");
        for (int i = begin; i < end; i++) {
            sb.append(numbers[i]);
            if (i < end -1) sb.append(" ");
        }
            return sb+"]";
    }
    private static void combineResults(int[]a, int indexes[]){
        int [] largestKs= new int[k];
        int currentK;
        int nextK;
        int largest;
        int partJ;
        for (int i = 0; i < k; i++) {
           partJ=0;
           largest = a[indexes[0]];
           for (int j = 0; j < indexes.length; j++) {
                if (a[indexes[j]]> largest){
                    partJ= j;
                    largest = a[indexes[j]];
                }                          
            }
            //System.out.println("currentK " + currentK+ " nextK " + nextK);
            largestKs[i] = indexes[partJ];
            indexes[partJ]++;
        }
         
        for (int i = 0; i < largestKs.length; i++) {
            if (largestKs[i] < k) {
                InsertionSort.swap(a,i+k, largestKs[i]);
            }
        }
        
        for (int i = 0; i < largestKs.length; i++) {           
            if(largestKs[i] >k){           
                InsertionSort.swap(a, i, largestKs[i] );
            }else {
                InsertionSort.swap(a, i , i+k);
            }            
        }
        //System.out.println("largest k "+Arrays.toString(largestKs));   
    }

    static class Worker extends Thread {
        public int begin;
        public int end;
        public int innerK;
        public Worker( int begin, int end) {
            this.begin = begin;
            this.end = end;
            this.innerK= begin+ k;
        }
        
        
        @Override
        public void run(){
            //System.out.println(presentChunk(begin, end));
            
            InsertionSort.insertSort(A2SortPar, begin, innerK-1);
            InsertionSort.compareRetEearly(A2SortPar, innerK-1, end-1);
            
        
            
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
        double[] timesA2 = new double[7];
        double[] timesA2Par = new double[7];
        double[] speedUpTimes = new double[7];
        
        
        //System.out.println(threadsNum+ " how many threads i have");

        for (int j = 0; j < 7; j++) {
            A2SortPar = numbers.clone();
            System.out.println("\n\nRun number: " + (j+1));
            
            while (threadsNum * k > n) threadsNum /= 2;
            Thread[] workers = new Thread[threadsNum];
            int[] BeginIndexes = new int[threadsNum];
            int[] EndIndexes = new int[threadsNum];

        //  cb = new CyclicBarrier(threadsNum); 
            double timeStart = System.nanoTime() ;
            for (int i = 0; i <threadsNum; i++) {       
                int begin= n/threadsNum*i;
                int end = (n/threadsNum) * (i+1) ;
                if(i==threadsNum -1 )end = n;  
            //  System.out.println("begin "+ begin + " end "+ end )   ;
                BeginIndexes[i] = begin;  
                EndIndexes [i] = end;
                workers[i] = new Worker(begin, end);
                workers[i].start();
            }

            try {
                for (Thread worker : workers) {
                    worker.join();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(-1);
            }
    
            combineResults(A2SortPar, BeginIndexes);
            double elapsedTimeA2Par =  (System.nanoTime() - timeStart) / 1e6;
            timesA2Par[j]= elapsedTimeA2Par;
            //System.out.println("Sorted A2/w threads "+ Arrays.toString(A2SortPar));
            System.out.println("\nElapsed time A2Par: " + elapsedTimeA2Par + " ms");

            int[] A2Sort = numbers.clone();
            double timeStartA2 = System.nanoTime();
            InsertionSort.insertSort(A2Sort, 0, k - 1);
            InsertionSort.compare(A2Sort, k-1, n - 1); 
            double elapsedTimeA2 =(System.nanoTime() - timeStartA2) / 1e6;
            timesA2[j]= elapsedTimeA2;
            System.out.println("\nElapsed time A2: " + elapsedTimeA2 + " ms");


            System.out.println("\nSpeed up: "+  elapsedTimeA2/elapsedTimeA2Par);
            speedUpTimes[j]=  elapsedTimeA2/elapsedTimeA2Par;


        }

        System.out.println("-----------------------------");

        System.out.println("\nTimes ran for K= " +k+ ", N ="+ n +": ");
        Arrays.sort(timesA2);
        System.out.println("\nMedian time A2  of 7 runs for K= " +k+ " N ="+ n +": " +timesA2[3]+ "ms");

        Arrays.sort(timesA2Par);
        System.out.println("\nMedian time A2 parallelized of 7 runs A2 for K= " +k+ " N ="+ n +": " +timesA2Par[3]+ "ms");
      
        Arrays.sort(speedUpTimes);
        System.out.println("\nMedian time speed up of A2/A2Par: " +speedUpTimes[3]);
    }
    
}



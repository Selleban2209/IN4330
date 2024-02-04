
import java.util.Timer;
import java.util.Arrays;
import java.util.Random;

public class Task1 {
    static final int N = 10000;
    static  int K = 20;
    static int[] numbers = new int[N];
   
    

    public static void main(String[] args) {
        int n = 0, k = 0;
        
        try {
            n = Integer.parseInt(args[0]);
            k = Integer.parseInt(args[1]);
        } catch (Exception e) {
            System.out.println("Usage: Task1 <N> <K>");
            System.exit(0);
        }

        int[] numbers = new int[n];
        Random random = new Random(7363);
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = random.nextInt(n);
        }        
        //testRun(numbers, K, Ns);
        double[] timesA1 = new double[7];
        double[] timesA2 = new double[7];
        for (int i = 0; i < 7; i++) {
            
            System.out.println("\n\nRun number: " + (i+1)+ "\n");
            InsertionSort sort = new InsertionSort();
            
            int[] regSort = numbers.clone();
           
            double timeStartA1 = System.nanoTime();;
            Arrays.sort(regSort);
            double elapsedTimeA1 =(System.nanoTime() - timeStartA1) / 1e6;;    
            //System.out.println("regular sort A1 " + Arrays.toString(regSort));
            timesA1[i]= elapsedTimeA1;
            System.out.println("Elapsed time A1: " + elapsedTimeA1 + " ms\n");
            
            int[] A2Sort = numbers.clone();
            double timeStartA2 = System.nanoTime();;
            InsertionSort.insertSort(A2Sort, 0, k - 1);
            InsertionSort.compare(A2Sort, k, n - 1);
            double elapsedTimeA2 =(System.nanoTime() - timeStartA2) / 1e6;
            timesA2[i]= elapsedTimeA2;
           //System.out.println("after A2: " + Arrays.toString(A2Sort));
            //System.out.println("after A2: " + Arrays.toString(numbers));

            System.out.println("Elapsed time A2: " + elapsedTimeA2 + " ms");

            System.out.println("\nTest done for K =" + k+ " N = " + n+"\n");

        }
        Arrays.sort(timesA1);
        System.out.println("Median time A1 for K= " +k+ " N ="+ n +": " +timesA1[3]+ "ms");
        Arrays.sort(timesA2);
        System.out.println("\nMedian time A2 for K= " +k+ " N ="+ n +": " +timesA2[3]+ "ms");
    }
    
}

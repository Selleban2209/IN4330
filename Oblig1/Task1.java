
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
            long timeStartA1 = System.currentTimeMillis();
            Arrays.sort(regSort);
            long timefinishA1 = System.currentTimeMillis();
            long elapsedTimeA1 = timefinishA1 - timeStartA1;    
            //System.out.println("regular sort A1 " + Arrays.toString(regSort));
            timesA1[i]= elapsedTimeA1;
            System.out.println("Elapsed time A1: " + elapsedTimeA1 + " ms\n");
            
            int[] A2Sort = numbers.clone();
            long timeStartA2 = System.currentTimeMillis();
            InsertionSort.insertSort(A2Sort, 0, k - 1);
            InsertionSort.compare(A2Sort, k-1, n - 1);
            long timefinishA2 = System.currentTimeMillis();
            long elapsedTimeA2 = timefinishA2 - timeStartA2;
            timesA2[i]= elapsedTimeA2;
           // System.out.println("after A2: " + Arrays.toString(A2Sort));
            System.out.println("Elapsed time A2: " + elapsedTimeA2 + " ms");

            System.out.println("\nTest done for K =" + k+ " N = " + n+"\n");

        }
        Arrays.sort(timesA1);
        System.out.println("Median time A1 for K= " +k+ " N ="+ n +": " +timesA1[3]+ "ms");
        Arrays.sort(timesA2);
        System.out.println("\nMedian time A2 for K= " +k+ " N ="+ n +": " +timesA2[3]+ "ms");
    }
    
}

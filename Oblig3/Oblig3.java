import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

public class Oblig3 {

      public static boolean compareTreeMaps(TreeMap<Long, LinkedList<Long>> map1, TreeMap<Long, LinkedList<Long>> map2) {
        // Check if the size of both TreeMap objects is the same
        if (map1.size() != map2.size())return false;
        

        // Iterate over entries in TreeMap1
        for (Map.Entry<Long, LinkedList<Long>> entry : map1.entrySet()) {
            Long key = entry.getKey();
            LinkedList<Long> list1 = entry.getValue();

            Collections.sort(entry.getValue());
            // Compare LinkedLists associated with the keys
            LinkedList<Long> list2 = map2.get(key);
            Collections.sort(list2);
            if (!list1.equals(list2)) {
                return false;
            }
        }

        return true;
    }
    
    public static void main(String[] args) {
        
        int n;
        int t;

        try {
            n = Integer.parseInt(args[0]);
           // t = Integer.parseInt(args[1]);
           t= 0;
            if (n <= 0)
                throw new Exception();

            if (args.length > 1) {
                int threadNum = Runtime.getRuntime().availableProcessors();
                if(Integer.parseInt(args[1])<=threadNum && Integer.parseInt(args[1])>0){
                    t = Integer.parseInt(args[1]);
                } else{
                    t = Runtime.getRuntime().availableProcessors();
                    System.out.println("Number of available threads exceeded, setting to max threads of " +t);
                } 
            } else {
                // If the second argument isn't provided, set t to the number of available threads
                t = Runtime.getRuntime().availableProcessors();
            }
        } catch (Exception e) {
           
            System.out.println("Correct use of program is: " +
                    "java Oblig3.java <n>  <t> where <n> is a positive integer, and t is number of threads");
            return;
        }

    
      
        
        SieveOfEratosthenesPar soePar = new SieveOfEratosthenesPar(n);
        
        int []primesPar = soePar.getPrimesPar();
        //SieveOfEratosthenes.printPrimes(primesPar);
        /**
         * Getting all the primes equal to and below 'n'
         */
        
        SieveOfEratosthenes soe = new SieveOfEratosthenes(n);
        int[] primes = soe.getPrimes();
        
        
        
        double[] timesRunSeqGetPrime = new double[7];
        for (int i = 0; i < 7 ; i++) {
            double timeStart = System.nanoTime();
            SieveOfEratosthenes soeTest = new SieveOfEratosthenes(n);
            int[] primesTest = soeTest.getPrimes();      
            double elapsedTime =(System.nanoTime() - timeStart) / 1e6; 
            timesRunSeqGetPrime[i]= elapsedTime;

        }
        Arrays.sort(timesRunSeqGetPrime);
        System.out.println("Sieve sequential times for a median of 7 test run: " + timesRunSeqGetPrime[3]+ "ms" + " (" +(timesRunSeqGetPrime[3]/1000)+ "s)");


        double[] timesRunParGetPrime = new double[7];
        for (int i = 0; i < 7 ; i++) {
            double timeStart = System.nanoTime();
            SieveOfEratosthenesPar soeParTest = new SieveOfEratosthenesPar(n);
            int []primesParTest = soeParTest.getPrimesPar();        
            double elapsedTime =(System.nanoTime() - timeStart) / 1e6; 
            timesRunParGetPrime[i]= elapsedTime;         
        }
        Arrays.sort(timesRunParGetPrime);
        System.out.println("Sieve parallel times for a median of 7 test run: " + timesRunParGetPrime[3]+ "ms" + " (" +(timesRunParGetPrime[3]/1000)+ "s)");
        
        System.out.println("Speed up of sieve for 7 runs: "+ String.format("%.3f",( (timesRunSeqGetPrime[3]/timesRunParGetPrime[3]))));
        /**
         * Printing the primes collected
         * 
         */
        
        //SieveOfEratosthenes.printPrimes(primes);
        
        
        double[] timesRunSeq = new double[7];
        Oblig3Precode ob3PreSeq = new Oblig3Precode(n);
        for (int i = 0; i < 7 ; i++) {
            ob3PreSeq = new Oblig3Precode(n);
            FactSeq seqFact = new FactSeq(n, primes, ob3PreSeq);
            double timeStart = System.nanoTime();
            seqFact.factN2();
            double elapsedTime =(System.nanoTime() - timeStart) / 1e6; 
            timesRunSeq[i]= elapsedTime;
        }
        Arrays.sort(timesRunSeq);
        System.out.println("Times for a median of 7 test run sequential factorization " + timesRunSeq[3]+ "ms" + " (" +(timesRunSeq[3]/1000)+ "s)");
        
        
        
        Oblig3Precode ob3PrePar = new Oblig3Precode(n);
        double[] timesRunPar = new double[7];
        for (int i = 0; i < 7 ; i++) {
            ob3PrePar = new Oblig3Precode(n);
            FactPar parFact = new FactPar(n, primes,t ,ob3PrePar);
            double timeStart = System.nanoTime();
            parFact.factN2Par();  
            double elapsedTime =(System.nanoTime() - timeStart) / 1e6; 
            timesRunPar[i]= elapsedTime;
        }
        Arrays.sort(timesRunPar);
        System.out.println("Times for a median of 7 test run Parallelized factorization, "+ "using " + t + " threads "+ timesRunPar[3]+ "ms" + " (" +(timesRunPar[3]/1000)+ "s)");
        
        System.out.println("Speed up of factorize for 7 runs: "+ String.format("%.3f",( (timesRunSeq[3]/timesRunPar[3]))));
        
    
       //ob3's factor treemap isnt accessible wihtout modifying the oblig3precode, which is needed to run this comparison
       // if(!compareTreeMaps(ob3PrePar.factors, ob3PreSeq.factors))System.out.println("Wrong prime numbers");
        
    }
}

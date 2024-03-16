import java.util.Arrays;

public class Oblig3 {
    
    public static void main(String[] args) {
        
        int n;

        try {
            n = Integer.parseInt(args[0]);
            if (n <= 0)
                throw new Exception();
        } catch (Exception e) {
            System.out.println("Correct use of program is: " +
                    "java SieveOfEratosthenes <n> where <n> is a positive integer.");
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
        for (int i = 0; i < 7 ; i++) {
            Oblig3Precode ob3Pre = new Oblig3Precode(n);
            FactSeq seqFact = new FactSeq(n, primes, ob3Pre);
            double timeStart = System.nanoTime();
            seqFact.factN2();
            double elapsedTime =(System.nanoTime() - timeStart) / 1e6; 
            timesRunSeq[i]= elapsedTime;
        }
        Arrays.sort(timesRunSeq);
        System.out.println("Times for a median of 7 test run sequential factorization " + timesRunSeq[3]+ "ms" + " (" +(timesRunSeq[3]/1000)+ "s)");
        
        
        
        double[] timesRunPar = new double[7];
        for (int i = 0; i < 7 ; i++) {
            Oblig3Precode ob3Pre = new Oblig3Precode(n);
            FactPar parFact = new FactPar(n, primes, ob3Pre);
            double timeStart = System.nanoTime();
            parFact.factN2Par();  
            double elapsedTime =(System.nanoTime() - timeStart) / 1e6; 
            timesRunPar[i]= elapsedTime;
        }
        Arrays.sort(timesRunPar);
        System.out.println("Times for a median of 7 test run Parallelized factorization " + timesRunPar[3]+ "ms" + " (" +(timesRunPar[3]/1000)+ "s)");
        
        System.out.println("Speed up of factorize for 7 runs: "+ String.format("%.3f",( (timesRunSeq[3]/timesRunPar[3]))));
        
        
        
    }
}

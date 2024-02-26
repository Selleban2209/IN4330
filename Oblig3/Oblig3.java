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

        SieveOfEratosthenes soe = new SieveOfEratosthenes(n);

        /**
         * Getting all the primes equal to and below 'n'
         */
        new SieveOfEratosthenes(n).getPrimes();
        int[] primes = soe.getPrimes();

        /**
         * Printing the primes collected
         */
        SieveOfEratosthenes.printPrimes(primes);

        Oblig3Precode ob3Pre = new Oblig3Precode(n);

        Factorize seqFact = new Factorize(n, primes, ob3Pre);

        seqFact.factN2();
    }
}

public class Factorize {
    long N; 
    long N2;
    int [] primesToN;
    Oblig3Precode ob3Pre;


    public Factorize(long n, int []primes, Oblig3Precode ob3Pre){
        N= n;
        N2 = n*n;
        primesToN= primes;
        this.ob3Pre = ob3Pre;
    }


    public void factN2 (){   
        for (long i = N2-100; i < N2; i++) {
            factNumber(i);      
        }
        ob3Pre.writeFactors();
    }

    public void factNumber(long number){ 
        long base = number; 
        for (int prime : primesToN) {
            while( number != 1 && number % prime ==0){
                number /=prime;
                ob3Pre.addFactor(base, prime);             
            }   
        }
       if(number != 1)ob3Pre.addFactor(base, number);       
    }
}

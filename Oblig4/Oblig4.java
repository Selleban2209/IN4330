import java.util.Arrays;

public class Oblig4 {
    public static void main(String[] args) {

        int n=0;
        int seed= 0;
        int numThreads =0;
        try {
            n = Integer.parseInt(args[0]);
            seed = Integer.parseInt(args[1]);
            numThreads = Integer.parseInt(args[2]);
            if(numThreads > Runtime.getRuntime().availableProcessors() || numThreads< 0)numThreads = Runtime.getRuntime().availableProcessors();
          
           } catch (Exception e) {
               System.out.println("Usage: Oblig2.java <n> <seed> <number of threads>");
               System.exit(0);
           }

        ConvexHull ch = new ConvexHull(n, seed);
        IntList koHyll = new IntList();
        double[] timesRunSeq = new double[7];
        for (int i = 0; i < 7; i++) {
            double timeStart = System.nanoTime();
            koHyll= ch.seqMethod();     
            double elapsedTime =(System.nanoTime() - timeStart) / 1e6; 
            timesRunSeq[i]= elapsedTime;
        }
        if(n < 1e5){
            Oblig4Precode ob4 = new Oblig4Precode(ch,koHyll );
            ob4.drawGraph();
        }
        Arrays.sort(timesRunSeq);
        
        ConvexHullPar chp = new ConvexHullPar(n, seed, numThreads);
        IntList koHyllPar  =  new IntList();
        double[] timesRunPar = new double[7];
        for (int i = 0; i < 7; i++) {
            double timeStart = System.nanoTime();
            koHyllPar = chp.parMethod();
            double elapsedTime =(System.nanoTime() - timeStart) / 1e6; 
            timesRunPar[i]= elapsedTime;
        }
        Arrays.sort(timesRunPar);
     
        if(n < 1e5){
            Oblig4Precode ob4par = new Oblig4Precode(ch, koHyllPar );
            ob4par.drawGraph();
        }
        System.out.println("---------------------------[RESULTS]----------------------------------");
        System.out.println("Median time of 7 runs for sequential Convex Hull: "+ timesRunSeq[3] + " ms" );
        System.out.println("Median time of 7 runs for Parallel Convex Hull: " +timesRunPar[3] +  "ms" );
        
        System.out.println("Speed up of sieve for 7 runs: "+ String.format("%.3f",( (timesRunSeq[3]/timesRunPar[3]))));
        //System.out.println("-----------------------------------------------------------------------");
    }
    
}

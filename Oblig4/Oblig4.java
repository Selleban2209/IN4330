import java.util.Arrays;

public class Oblig4 {
    public static void main(String[] args) {

        int n=0;
        int seed= 0;
 
        try {
            n = Integer.parseInt(args[0]);
            seed = Integer.parseInt(args[1]);
          
           } catch (Exception e) {
               System.out.println("Usage: Oblig2.java <n> <seed>  ");
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
        if(n < 1e6){
            Oblig4Precode ob4p = new Oblig4Precode(ch,koHyll );
            ob4p.drawGraph();
        }
            Arrays.sort(timesRunSeq);
		

        System.out.println("-------------------------------------------------------------");
        System.out.println("Median time of 7 runs for sequential Convex Hull: "+ timesRunSeq[3] + " ms" );
    }
    
}

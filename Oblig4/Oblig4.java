

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
       IntList koHyll= ch.seqMethod();
        Oblig4Precode ob4p = new Oblig4Precode(ch,koHyll );
        ob4p.drawGraph();
    }
    
}

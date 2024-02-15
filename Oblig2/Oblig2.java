public class Oblig2 {
    public static void main(String[] args) {

        int seed = 42;
        int n = 5;
        
        String op = "";
        Oblig2Precode.Mode operation = Oblig2Precode.Mode.SEQ_NOT_TRANSPOSED;
        
        try {
            seed = Integer.parseInt(args[0]);
            n = Integer.parseInt(args[1]);
            // operation as an argument
            //op = args[2];
           // operation = Oblig2Precode.Mode.valueOf(op.toUpperCase());
           } catch (Exception e) {
               System.out.println("Usage: Exampe.java <seed> <n> <Operation> ");
               System.exit(0);
           }


        double[] timesNotTransposed=  Seq.RunSeq(seed, n, Oblig2Precode.Mode.SEQ_NOT_TRANSPOSED );
        double[] timesBTransposed = Seq.RunSeq(seed, n, Oblig2Precode.Mode.SEQ_B_TRANSPOSED);
        double[] timesATransposed = Seq.RunSeq(seed, n, Oblig2Precode.Mode.SEQ_A_TRANSPOSED);

        double[] timesNotTransposedPar=  Par.RunPar(seed, n, Oblig2Precode.Mode.PARA_NOT_TRANSPOSED);
        double[] timesATransposedPar=  Par.RunPar(seed, n, Oblig2Precode.Mode.PARA_A_TRANSPOSED);
        double[] timesBTransposedPar=  Par.RunPar(seed, n, Oblig2Precode.Mode.PARA_B_TRANSPOSED);


        System.out.println("Median times of 7 runs for SEQ_NOT_TRANSPOSED sequential: "+timesNotTransposed[3]+"ms");  
        System.out.println("Median times of 7 runs for SEQ_A_TRANSPOSED: sequential: "+timesATransposed[3]+"ms");
        System.out.println("Median times of 7 runs for SEQ_B_TRANSPOSED sequential: "+timesBTransposed[3]+"ms");

        System.out.println("\nMedian times of 7 runs for PARA_NOT_TRANSPOSED: " +timesNotTransposedPar[3]+"ms");
        System.out.println("Median times of 7 runs for PARA_A_TRANSPOSED: " +timesATransposedPar[3]+"ms");
        System.out.println("Median times of 7 runs for PARA_B_TRANSPOSED: " +timesBTransposedPar[3]+"ms");


        System.out.println("\nMedian speed up of 7 runs for NOT_TRANSPOSED: " +String.format("%.3f", (timesNotTransposed[3]/timesNotTransposedPar[3])));
        System.out.println("Median speed up of 7 runs for A_TRANSPOSED: " +String.format("%.3f",(timesATransposed[3]/timesATransposedPar[3])));
        System.out.println("Median speed up of 7 runs for B_TRANSPOSED: " +String.format("%.3f",(timesBTransposed[3]/timesBTransposedPar[3])));
       


        
    }
}

public class Oblig2 {
    public static void main(String[] args) {

        int seed = 42;
        int n = 5;
        
        String op = "";
        Oblig2Precode.Mode operation = Oblig2Precode.Mode.SEQ_NOT_TRANSPOSED;
        
        try {
            seed = Integer.parseInt(args[0]);
            n = Integer.parseInt(args[1]);
            //op = args[2];
           // operation = Oblig2Precode.Mode.valueOf(op.toUpperCase());
           } catch (Exception e) {
               System.out.println("Usage: Exampe.java <seed> <n> <Operation> ");
               System.exit(0);
           }

        double[] timesNotTransposed=  Seq.RunSeq(seed, n, Oblig2Precode.Mode.SEQ_NOT_TRANSPOSED);
        double[] timesATransposed = Seq.RunSeq(seed, n, Oblig2Precode.Mode.SEQ_A_TRANSPOSED);
        double[] timesBTransposed = Seq.RunSeq(seed, n, Oblig2Precode.Mode.SEQ_B_TRANSPOSED);

        System.out.println("Median times of 7 runs for SEQ_NOT_TRANSPOSED: "+timesNotTransposed[3]+"ms");
        System.out.println("Median times of 7 runs for SEQ_A_TRANSPOSED: "+timesATransposed[3]+"ms");
        System.out.println("Median times of 7 runs for SEQ_B_TRANSPOSED: "+timesBTransposed[3]+"ms");
        
    }
}

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

        Seq.RunSeq(seed, n, Oblig2Precode.Mode.SEQ_NOT_TRANSPOSED);
        Seq.RunSeq(seed, n, Oblig2Precode.Mode.SEQ_A_TRANSPOSED);
        Seq.RunSeq(seed, n, Oblig2Precode.Mode.SEQ_B_TRANSPOSED);
        
    }
}

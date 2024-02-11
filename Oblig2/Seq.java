public class Seq {

    
	public static void RunSeq(int seed, int n, Oblig2Precode.Mode operation ) {

		// Process these from the command line
		/*
		 * 
		 int seed = 42;
		 int n = 5;
		 
		 String op = "";
		 Oblig2Precode.Mode operation = Oblig2Precode.Mode.SEQ_NOT_TRANSPOSED;
		 
		 try {
			 seed = Integer.parseInt(args[0]);
			 n = Integer.parseInt(args[1]);
			 op = args[2];
			 operation = Oblig2Precode.Mode.valueOf(op.toUpperCase());
			} catch (Exception e) {
				System.out.println("Usage: Exampe.java <seed> <n> <Operation> ");
				System.exit(0);
			}
			*/



		// Get the matrices
		double[][] a = Oblig2Precode.generateMatrixA(seed, n);
		double[][] b = Oblig2Precode.generateMatrixB(seed, n);
	

        
        switch (operation) {
            case SEQ_NOT_TRANSPOSED:   
            break;
            case SEQ_A_TRANSPOSED:
            a = Oblig2Precode.transposeMatrix(a, n);
            break;
            case  SEQ_B_TRANSPOSED:   
            b = Oblig2Precode.transposeMatrix(a, n);
            break;
            default:
                break;
        }
		// The result matrix
		double[][] c = Oblig2Precode.multMetrix(a, b, n, operation);

		// Do the multiplication
		// Clearly O(n^3)
		
		// Save the result

	
		Oblig2Precode.saveResult(seed, operation, c);
	}
    
}

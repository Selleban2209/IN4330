import java.util.Arrays;

public class Seq {

    
	public static double[] RunSeq(int seed, int n, Oblig2Precode.Mode operation ) {

		// Get the matrices
		double[][] a = Oblig2Precode.generateMatrixA(seed, n);
		double[][] b = Oblig2Precode.generateMatrixB(seed, n);
	
		double[] timesRunSeq = new double[7];
		double[][] c = new double[n][n];
        for (int i = 0; i < 7 ; i++) {
			double timeStart = System.nanoTime();;
			
			switch (operation) {
				case SEQ_NOT_TRANSPOSED:   
				break;
				case SEQ_A_TRANSPOSED:
				a = Oblig2Precode.transposeMatrix(a, n);
				break;
				case  SEQ_B_TRANSPOSED:   
				b = Oblig2Precode.transposeMatrix(b, n);
				break;
				default:
                break;
			}
			// The result matrix
			
			c = Oblig2Precode.multMetrix(a, b, n, operation);
			double elapsedTime =(System.nanoTime() - timeStart) / 1e6; 
			timesRunSeq[i]= elapsedTime;
		}

		Arrays.sort(timesRunSeq);

		// Save the result

		
		Oblig2Precode.saveResult(seed, operation, c);
		return timesRunSeq;
	}
    
}

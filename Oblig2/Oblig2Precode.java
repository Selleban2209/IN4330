import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;

/**
 * Pre-code for Oblig 2 (Matrix multiplication) IN3030 - Spring 2024
 *
 * @author Magnus Espeland <magnuesp@ifi.uio.no>
 * @date 2019.02.08
 *
 *       HOW TO USE THIS:
 *
 *       To get the matrices you are going to multiply:
 *       (Remember to read 'seed' and 'n' from the command line arguments)
 *
 *       double[][] A = Oblig2Precode.generateMatrixA(seed,n);
 *       double[][] B = Oblig2Precode.generateMatrixB(seed,n);
 *
 *       (Notice it's two different methods)
 *
 *       Then do your multiplications in the various ways:
 *       - Sequential with no transpose
 *       - Sequential with A transposed
 *       - Sequential with B transposed
 *       - Parallel with no transpose
 *       - Parallel with A transposed
 *       - Parallel with B transposed
 *
 *       When done, save all the results.
 *
 *       Example:
 *
 *       Oblig2Precode.saveResult(seed, Oblig2Precode.Mode.SEQ_NOT_TRANSPOSED,
 *       seqNTResult);
 *
 *       Beware that it'll only actually write files with max n=100.
 *
 *
 *       Example file output:
 *
 *       seed=42 mode=SEQ_NOT_TRANSPOSED n=5
 *
 *       1.35 2.27 1.73 1.07 1.69
 *       1.42 2.35 1.95 0.98 1.72
 *       1.19 2.14 1.87 1.09 1.47
 *       0.92 1.90 1.87 1.21 1.53
 *       1.22 2.17 1.61 0.97 1.65
 *
 *
 *
 *
 */

public class Oblig2Precode {

	/**
	 * The modes of operation your program should execute and measure.
	 *
	 */
	public enum Mode {
		SEQ_NOT_TRANSPOSED,
		SEQ_A_TRANSPOSED,
		SEQ_B_TRANSPOSED,
		PARA_NOT_TRANSPOSED,
		PARA_A_TRANSPOSED,
		PARA_B_TRANSPOSED
	}

	/**
	 * This method will generate the A matrix you are supposed to multiply with B.
	 *
	 * @param seed The RNG seed
	 * @param n    Size of matrix (NxN)
	 * @return Matrix A
	 */
	public static double[][] generateMatrixA(int seed, int n) {
		return generateMatrix(seed, n);
	}

	/**
	 * This method will generate the B matrix you are supposed to multiply with A.
	 *
	 * @param seed The RNG seed
	 * @param n    Size of matrix (NxN)
	 * @return Matrix B
	 */
	public static double[][] generateMatrixB(int seed, int n) {
		return generateMatrix(seed + 1, n);
	}

	/**
	 * For internal use, actually generating the matrix
	 */
	private static double[][] generateMatrix(int seed, int n) {
		double[][] m = new double[n][n];

		Random rnd = new Random(seed);

		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				m[i][j] = rnd.nextDouble();

		return m;
	}


	public static double[][] transposeMatrix(double matrix[][],int n){

		double[][] tempMx= matrix;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {	
				matrix[i][j] = tempMx[i][j];
			}		
		}
		return matrix;
	}

	
	public static double[][] multMetrix(double a[][], double b[][],double c[][],int s, int endr, int n ,Oblig2Precode.Mode op){
		// Do the multiplication
		// Clearly O(n^3)	
		double[][] tempC = c;
		switch (op) {
            case SEQ_NOT_TRANSPOSED : 
			case PARA_NOT_TRANSPOSED:
				for(int i=s;i<endr;i++)
					for(int j=0;j<n;j++)
						for(int k=0;k<n;k++)
						 	c[i][j] += a[i][k] * b[k][j];
					break;
            case SEQ_A_TRANSPOSED:
            case PARA_A_TRANSPOSED:
				for(int i=s;i<endr;i++)
					for(int j=0;j<n;j++)					
						for(int k=0;k<n;k++)
							c[i][j] += a[k][i] * b[k][j]; 				
            	break;
            case  SEQ_B_TRANSPOSED:  
            case  PARA_B_TRANSPOSED:  
				for(int i=s;i<endr;i++)
					for(int j=0;j<n;j++)
						for(int k=0;k<n;k++)
							c[i][j] += a[i][k] * b[j][k]; 
            	break;
            default:
                break;
        }
		//System.out.println(Arrays.deepToString(tempC));
		//System.out.println(Arrays.deepEquals(c, tempC));
		return tempC;

	}
	
	
	/**
	 * Method for saving your result to a file.
	 *
	 * Modes are:
	 * SEQ_NOT_TRANSPOSED
	 * SEQ_A_TRANSPOSED
	 * SEQ_B_TRANSPOSED
	 * PARA_NOT_TRANSPOSED
	 * PARA_A_TRANSPOSED
	 * PARA_B_TRANSPOSED
	 *
	 *
	 * @param seed The seed used in generateMatrix
	 * @param mode Which mode is this result from?
	 * @param m    The result of your matrix multiplication using this mode
	 */
	public static void saveResult(int seed, Mode mode, double[][] m) {

		if (m.length > 100)
			return;

		String filename = String.format("O2Result_%d_%s_%d.txt", seed, mode, m.length);

		try (PrintWriter writer = new PrintWriter(filename)) {
			writer.printf("seed=%d mode=%s n=%d\n\n", seed, mode, m.length);

			for (int i = 0; i < m.length; i++) {
				for (int j = 0; j < m[0].length; j++) {

					writer.printf("%.2f ", m[i][j]);
				}

				writer.println();
			}

			writer.flush();
			writer.close();

		} catch (Exception e) {
			System.out.printf("Got exception when trying to write file %s : %s", filename, e.getMessage());
		}

	}

}
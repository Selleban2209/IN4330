/**
 *
 * IN3030 - Oblig 2 Pre-code usage example
 *
 * @author Magnus Espeland <magnuesp@ifi.uio.no>
 * @date 2019.02.14
 */

 public class Example {

	public static void main(String[] args) {

		// Process these from the command line
		int seed = 42;
		int n = 5;

		String op = "";
		
		try {
            seed = Integer.parseInt(args[0]);
            n = Integer.parseInt(args[1]);
			op = args[2];
			
        } catch (Exception e) {
            System.out.println("Usage: Exampe.java <seed> <n> <Operation> ");
            System.exit(0);
        }

		Oblig2Precode.Mode operation = Oblig2Precode.Mode.valueOf(op.toUpperCase());
		// Get the matrices
		double[][] a = Oblig2Precode.generateMatrixA(seed, n);
		double[][] b = Oblig2Precode.generateMatrixB(seed, n);

		// The result matrix
		double[][] c = new double[n][n];

		// Do the multiplication
		// Clearly O(n^3)
		for(int i=0;i<n;i++)
			for(int j=0;j<n;j++)
				for(int k=0;k<n;k++)
					c[i][j] += a[i][k] * b[k][j];

		// Save the result

	
		Oblig2Precode.saveResult(seed, operation, c);
	}

}
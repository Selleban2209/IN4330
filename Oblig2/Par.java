import java.util.Arrays;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Par {
    

	public 	static double[][] c;
	public static int threadsNum;
	
	public static boolean deepCheckCompare( double [][] a, double [][] b,double [][] c, int n,  Oblig2Precode.Mode operation){

		//Prints an error 
		double [][] expected = new double[n][n];
		expected =  Oblig2Precode.multMetrix(a, b,expected, 0,n,n,operation);
		if(!Arrays.deepEquals(expected, c)){ 
			printDifferenceIndices(expected, c);
			return false;	
		}
		return true;
	}

	public static void printDifferenceIndices(double[][] arr1, double[][] arr2) {
        int rows = arr1.length;
        int cols = arr1[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (arr1[i][j] != arr2[i][j]) {
                    System.out.println("Difference at index [" + i + "][" + j + "]: " +
                            "Expected: " + arr1[i][j] + ", Actual: " + arr2[i][j]);
                }
            }
        }
    }
	
	static class Worker extends Thread{
		public int start;
		public int end;
		public double a[][];
		public double b[][];
		Oblig2Precode.Mode op;
		int localN;
		


		public Worker(int start, int end,int localN, double a[][], double b[][], Oblig2Precode.Mode  op){
			this.start= start;
			this.end = end;
			this.localN = localN;
			this.a= a;
			this.b = b;
			this.op = op;
			
		}

		@Override
		public void run() {		
			c = Oblig2Precode.multMetrix(a, b,c ,start, end, localN,op);	
		}
		
		
	}
	public static double[] RunPar(int seed, int n, Oblig2Precode.Mode operation ) {

		// Get the matrices
		double[][] a = Oblig2Precode.generateMatrixA(seed, n);
		double[][] b = Oblig2Precode.generateMatrixB(seed, n);
	
		double[] timesRunSeq = new double[7];
		threadsNum = Runtime.getRuntime().availableProcessors();
        for (int i = 0; i < 7 ; i++) {
			c = new double[n][n];
			double timeStart = System.nanoTime();
			switch (operation) {
				case PARA_NOT_TRANSPOSED:   
				break;
				case PARA_A_TRANSPOSED:
				a = Oblig2Precode.transposeMatrix(a, n);
				break;
				case  PARA_B_TRANSPOSED:   
				b = Oblig2Precode.transposeMatrix(b, n);
				break;
				default:
				break;
			}
			// The result matrix

			//System.out.println("number of threads:"+ threadsNum);
			Worker[] workers = new Worker[threadsNum];
			
			for (int j = 0; j < threadsNum; j++) {
				int start= (n/threadsNum)*j;
                int end = (n/threadsNum) * (j+1);
                if(j==threadsNum -1 )end = n; 

				//System.out.println("start and end " + start + " " + end );
				workers[j] = new Worker(start, end,n, a, b, operation);
                workers[j].start();
			}
		
			try {
                for (Worker worker : workers) {
                    //System.out.println("Thread finished");
                    worker.join();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(-1);
            }

			double elapsedTime =(System.nanoTime() - timeStart) / 1e6; 
			timesRunSeq[i]= elapsedTime;
		}
		
		Arrays.sort(timesRunSeq);
		

		//Compares sequential with paralel results, prints error if comparison fails
		 if(!deepCheckCompare(a, b, c, n, operation)) System.out.println("Wrong multplication");

		// Save the result	
		Oblig2Precode.saveResult(seed, operation, c);
		
		return timesRunSeq;

	}
    
}



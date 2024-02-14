import java.util.Arrays;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Par {
    

	public static int n;
	public 	static double[][] c;
	public static int threadsNum;
	
	public static void deepCheck( double a[][], double b[][],double c[][], int n,  Oblig2Precode.Mode operation){
		double [][] expected = new double[n][n];
		expected =  Oblig2Precode.multMetrix(a, b,expected, 0,n,n,operation);
		if(!Arrays.deepEquals(expected, c)){ 
			System.out.println("Wrong multplication");
		}else {
			//System.out.println("Correct multplication");
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
		c = new double[n][n];
		threadsNum = Runtime.getRuntime().availableProcessors();
        for (int i = 0; i < 7 ; i++) {
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
	

		deepCheck(a, b, c, n, operation);
		// Save the result	
		Oblig2Precode.saveResult(seed, operation, c);
		
		return timesRunSeq;

	}
    
}



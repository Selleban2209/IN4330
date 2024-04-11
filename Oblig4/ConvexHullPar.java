public class ConvexHullPar {
    int n, MAX_X, MAX_Y,MIN_X, MIN_Y;
    int x[], y[];
    
        //size cut off
    
        //thread cut off 
    
        //spawn only one, let the parent thread take over one of the two new tasks
    
    public ConvexHullPar(int n, int seed){
        this.n = n;
        x = new int[n];
        y = new int[n];
        NPunkter17 p = new NPunkter17(n, seed);
        p.fyllArrayer(x, y);


    }

    class Worker extends Thread{
        public Worker(){

        }

        @Override
        public void run() {
            



          
        }
    }


}

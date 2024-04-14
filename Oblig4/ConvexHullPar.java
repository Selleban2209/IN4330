public class ConvexHullPar {
    int n, MAX_X, MAX_Y,MIN_X, MIN_Y;
    int x[], y[];
    
    
    public ConvexHullPar(int n, int seed){
        this.n = n;
        x = new int[n];
        y = new int[n];
        NPunkter17 p = new NPunkter17(n, seed);
        p.fyllArrayer(x, y);
        
        
    }
    
    class Worker extends Thread{
        IntList koHyll;
        IntList workerSubset;
        int workerFurthest;
        int p1, p2;


        public Worker(int p1, int p2, int furthest ,IntList subset, IntList koHyll){
            this.p1 = p1;
            this.p2 = p2;
            this.workerFurthest = furthest;
            IntList workerSubset= subset;
            this.koHyll = koHyll;
            
        }
        
        @Override
        public void run() {
            
            
        }
    }
    
    //size cut off
    //thread cut off 
    //spawn only one, let the parent thread take over one of the two new tasks
    
    public IntList parMethod(){
        
        IntList koHyll = new IntList();


        IntList over_list = new IntList();
        IntList under_list = new IntList();

        MAX_X = findMax(x);
        MAX_Y = findMax(y);

        for (int i = 0; i < n; i++) {
            if(i == MIN_X || i== MAX_X)continue;
            double distance= findLargestDistance(MAX_X, MIN_X, i);
            
            if(distance >=0){
                over_list.add(i);
            } else if (distance <= 0)  under_list.add(i);
        }


        int furthestAbove= findFurthest(MAX_X, MIN_X, over_list);
        int furthestBelow= findFurthest(MIN_X, MAX_X, under_list);


        koHyll.add(MAX_X);

        Worker aboveWorker = new Worker(MAX_X, MIN_X, furthestAbove, over_list, koHyll);
        
        //seqReq(MAX_X, MIN_X, furthestAbove, over_list, koHyll);
        
        Worker belowWorker = new Worker(MIN_X, MAX_X, furthestBelow, under_list, koHyll);
        koHyll.add(MIN_X);
        //seqReq(MIN_X, MAX_X, furthestBelow, under_list, koHyll);


        return koHyll;

      
    }





    

    int findMax(int a[]){
        int max =0;
        for (int i = 0; i < a.length; i++) {
            if (a[i]>a[max]) max =i;
        }

        return max;
    }
    int findMin(int a[]){
        int min =0;
        for (int i = 0; i < a.length; i++) {
            if (a[i]<a[min]) min =i;
        }

        return min;
    }

    int findFurthest(int p1, int p2,IntList pointsList){
        double furthestPoint=0;
        int furthestPointIndex = pointsList.get(0);
        for (int i =0; i< pointsList.size(); i++) {
            int index = pointsList.get(i);
            if(index ==p1 || index == p2)continue;
            
            double distance= findLargestDistance(p1, p2, index);
            
            if (distance >= furthestPoint){
                furthestPoint = distance;
                furthestPointIndex= index;
            } 
        } 
        return furthestPointIndex;
    }

    double findLargestDistance(int min, int max, int i){
        int  a = y[min] - y[max];
        int b = x[max] - x[min];
        int  c = y[max]*x[min] - y[min]*x[max];
 
        double numerator = a*x[i]+ b*y[i]+ c;
        double denominator = Math.sqrt(a*a+b*b);

        return numerator/denominator;
     }

}

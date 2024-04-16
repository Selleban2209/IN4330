import java.util.Arrays;

public class ConvexHullPar {
    int n, MAX_X, MAX_Y,MIN_X, MIN_Y;
    int x[], y[];
    int numThreads;
    
    
    public ConvexHullPar(int n, int seed, int numThreads){
        this.n = n;
        x = new int[n];
        y = new int[n];
        NPunkter17 p = new NPunkter17(n, seed);
        this.numThreads = numThreads; 
        p.fyllArrayer(x, y);      
        
    }
    
  
    class Worker extends Thread{
        IntList localKoHyll;
        IntList workerSubset;
        int workerFurthest;
        int p1, p2;
        int start, end;
        int local_MAX_X, local_MAX_Y ;
        int local_MIN_X, local_MIN_Y ;
        int []localX ;
        int []localY ; 
        
        public Worker(int start , int end){ 
            this.start= start;
            this.end= end;
            this.localKoHyll = new IntList();
            localX=  Arrays.copyOfRange(x, start, end);
            localY = Arrays.copyOfRange(y, start, end);
            System.out.println("(" + start+ ", "+ (end)+ ")");
        }
        
        @Override
        public void run() {
            
            local_MAX_X = findMax(localX);
            local_MAX_Y = findMax(localY);
            local_MIN_X = findMin(localX);
            local_MIN_Y = findMin(localY);
                        
            //System.out.println(local_MAX_X+ ", "+ local_MAX_Y);
           // System.out.println(local_MIN_X+ ", "+ local_MIN_Y);

            IntList over_list = new IntList(end- start /2);
            IntList under_list = new IntList(end- start /2);
             
            for (int i = 0; i < n; i++) {
                if(i == local_MIN_X || i== local_MAX_X)continue;
                double distance= findLargestDistance(local_MAX_X, local_MIN_X, i);
                
                if(distance >=0){
                    over_list.add(i);
                } else if (distance <= 0)  under_list.add(i);
            }

            int furthestAbove= findFurthest(local_MAX_X, local_MIN_X, over_list);
            int furthestBelow= findFurthest(local_MIN_X, local_MAX_X, under_list);
           // System.out.println("jhalp prior" + furthestAbove + ", " + furthestBelow);
            localKoHyll.add(local_MAX_X);

            
            parReq(local_MAX_X, local_MIN_X, furthestAbove, over_list, localKoHyll);
            
        
            localKoHyll.add(local_MIN_X);
            parReq(local_MIN_X, local_MAX_X, furthestBelow, under_list, localKoHyll);
            
            
        }
    }
    


    public void parReq(int p1, int p2, int furthest, IntList subset, IntList koHyll){

        //put it to max threads for now
       // numThreads = Runtime.getRuntime().availableProcessors();
        IntList line1 = getPoints(p1, furthest, subset);
        IntList line2 = getPoints(furthest, p2, subset);


        int furthest1 = findFurthest(p1, furthest, line1);
        int furthest2 = findFurthest(furthest, p2, line2);


        //System.out.println("jhalp" + furthest1 + ", " + furthest2);
        if(furthest1!=-1 && !(koHyll.contains(furthest1))){     
            parReq(p1, furthest, furthest1, line1, koHyll);
        }

        if(!(koHyll.contains(furthest)) )koHyll.add(furthest);

        if (furthest2 != -1 && !(koHyll.contains(furthest2))){
            parReq(furthest, p2, furthest2, line2, koHyll);
        }
    }
    //size cut off
    //thread cut off 
    //spawn only one, let the parent thread take over one of the two new tasks
    
    public IntList parMethod(){
        
        IntList koHyll = new IntList();
        //IntList result = new IntList();

        
        Worker[] workers = new Worker[numThreads];
        for (int i = 0; i < numThreads; i++) {
            int start= (n/numThreads)*i;
            int end = (n/numThreads) * (i+1)-1;
            if(i==numThreads -1)end = n;  
            
            
            workers[i] = new Worker(start, end);
            workers[i].start();    
        }

        for (Worker worker : workers) {
            try {
                worker.join();
                for (int num : worker.localKoHyll.data) {
                    if(!koHyll.contains(num))koHyll.add(num);
                }  
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return koHyll;
  
    }


    public IntList getPoints(int start, int end, IntList subset){
        IntList pointsList= new IntList();

        for (int i = 0; i < subset.size(); i++) {
            int index = subset.get(i);

            if( index == start || index==end) continue;

            double distance = findLargestDistance(start, end, index);

            if( distance>= 0) pointsList.add(index);
            
        }
        return pointsList;
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

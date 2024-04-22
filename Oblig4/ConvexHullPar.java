import java.util.Arrays;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.*;
public class ConvexHullPar {
    int n, MAX_X, MAX_Y,MIN_X, MIN_Y;
    int x[], y[];
    int numThreads;
    CyclicBarrier cb;
    
    public ConvexHullPar(int n, int seed, int numThreads){
        this.n = n;
        x = new int[n];
        y = new int[n];
        NPunkter17 p = new NPunkter17(n, seed);
        this.numThreads = numThreads; 
        p.fyllArrayer(x, y);      
        cb = new CyclicBarrier(numThreads); 
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
          
        }
        
        @Override
        public void run() {
          
            local_MAX_X = findMax(localX);
            local_MAX_Y = findMax(localY);
            local_MIN_X = findMin(localX);
            local_MIN_Y = findMin(localY);
                        
            //System.out.println(local_MAX_X+ ", "+ local_MAX_Y);
           // System.out.println(local_MIN_X+ ", "+ local_MIN_Y);
       
            IntList over_list = new IntList();
            IntList under_list = new IntList();
             
            for (int i = start; i < n; i+=numThreads) {
                if(i == local_MIN_X || i== local_MAX_X)continue;
                double distance= findLargestDistance(local_MAX_X, local_MIN_X, i);
                if(distance >=0){
                    over_list.add(i);
                } else if (distance <= 0)  under_list.add(i);
            }
            
            int furthestAbove= findFurthest(local_MAX_X, local_MIN_X, over_list);
            int furthestBelow= findFurthest(local_MIN_X, local_MAX_X, under_list);
            
         

            localKoHyll.add(local_MAX_X);
           if(furthestAbove!=-1) parReq(local_MAX_X, local_MIN_X, furthestAbove, over_list, localKoHyll);
            
        
            localKoHyll.add(local_MIN_X);
           
            
            if(furthestBelow!=-1)parReq(local_MIN_X, local_MAX_X, furthestBelow, under_list, localKoHyll);
            
         
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
      
        if(furthest1!=-1 ){     
            parReq(p1, furthest, furthest1, line1, koHyll);
        }

        if( furthest!= -1  )koHyll.add(furthest);
        else return;

        if (furthest2 != -1){
            parReq(furthest, p2, furthest2, line2, koHyll);
        }
    }
    //size cut off
    //thread cut off 
    //spawn only one, let the parent thread take over one of the two new tasks
    
    public IntList parMethod(){
        
        IntList koHyll = new IntList();
        IntList result = new IntList();
        IntList over_list = new IntList();
        IntList under_list = new IntList();

        
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
                koHyll.append(worker.localKoHyll);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
      
        // int[] globalX=  Arrays.copyOfRange(x, 0, koHyll.size());
        //int[] globalY=  Arrays.copyOfRange(y, 0, koHyll.size());
        //System.out.println("ok"+Arrays.toString(globalX) + " " + koHyll.size());
        
        MIN_X = findMaxSize(x, koHyll.size(), koHyll);
        MAX_X = findMinSize(x, koHyll.size(), koHyll);
        //MIN_Y= findMaxSize(y, koHyll.size());
        //MAX_Y = findMinSize(y, koHyll.size());
       
        int curr =0; 
        //System.out.println("current MAX_x and min_x " + "("+ MIN_X+ ", " +MAX_X+ ")");
        for (int i = 0; i < koHyll.size(); i++) {

            curr = koHyll.get(i);
            if(curr == MIN_X || curr== MAX_X)continue;
            double distance= findLargestDistance(MAX_X, MIN_X, curr);  

            if(distance >=0)over_list.add(i);
            else if (distance <= 0)  under_list.add(i);
        }


        int furthestAbove= findFurthest(MAX_X, MIN_X, over_list);
        int furthestBelow= findFurthest(MIN_X, MAX_X, under_list);
      
        result.add(MAX_X);

        parReq(MAX_X, MIN_X, furthestAbove, over_list, result);

        result.add(MIN_X);
        parReq(MIN_X, MAX_X, furthestBelow, under_list, result);


        return result;
  
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


    int findMinSize(int a[], int size, IntList local){
        int min =0;
        int curr=0;
        for (int i = 0; i < size; i++) {
            curr= local.get(i);
            if (a[curr]<a[min]) min =curr;
        }
        return min;

    }
    int findMaxSize(int a[], int size, IntList local){
        int max =0;
        int curr =0;
        for (int i = 0; i < size; i++) {
            curr= local.get(i);
            if (a[curr]>a[max]) max =curr;
        }
        return max;
    }

    int findFurthest(int p1, int p2,IntList pointsList){
        double furthestPoint=0;
        int furthestPointIndex = pointsList.get(0);
        for (int i =0; i< pointsList.size(); i++) {
            int index = pointsList.get(i);
            if(index ==p1 || index == p2)continue;
            
            double distance= findLargestDistance(p1, p2, index);
            
            if (distance >= furthestPoint){
                if(distance == 0  && !checkBetween(p1,p2, index)) continue;
                furthestPoint = distance;
                furthestPointIndex= index;
            } 
        } 
        return furthestPointIndex;
    }


    public boolean checkBetween(int p1, int p2, int p3){
       
        double d1 = Math.sqrt( Math.pow((x[p2] - x[p1]), 2) + Math.pow((y[p2] - y[p1]), 2));
        double d2 = Math.sqrt( Math.pow((x[p3] - x[p1]), 2) + Math.pow((y[p3] - y[p1]), 2));
        double d3 = Math.sqrt( Math.pow((x[p3] - x[p2]), 2) + Math.pow((y[p3] - y[p2]), 2));
        if( d1> d2 && d1 > d3) return true;
        return false;
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

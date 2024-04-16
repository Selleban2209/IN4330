import java.util.Arrays;

/**
 * A basic class to allow the precode to compile. You will need to implement the
 * logic for finding what points make up the convex hull.
 * 
 */
public class ConvexHull {
    int n, MAX_X, MAX_Y,MIN_X, MIN_Y;
    int x[], y[];
    IntList over_list;
    IntList under_list;
    int a, b, c;
    IntList conveksPoints= new IntList();
    int numThreads;

    public ConvexHull(int n, int seed) {
        this.n = n;
        x = new int[n];
        y = new int[n];
        NPunkter17 p = new NPunkter17(n, seed);
        p.fyllArrayer(x, y);
        //IntList punkterList = p.lagIntList();
        //punkterList.print();

        //Oblig4Precode ob4p = new Oblig4Precode(this, p.lagIntList());
        //ob4p.drawGraph();
        MAX_X = findMax(x);
        MAX_Y = findMax(y);
        MIN_X = findMin(x);
        MIN_Y = findMin(y);

        //System.out.println("min ("+ x[MIN_X]+ ", " + y[MIN_X]+ ")");
       // System.out.println("max ("+ x[MAX_X]+ ", " + y[MAX_X]+ ")");
      
        a = y[MIN_X] - y[MAX_X];
        b = x[MAX_X] - x[MIN_X];
        c = y[MAX_X]*x[MIN_X] - y[MIN_X]*x[MAX_X];

       // System.out.println( "f(x)= "+ a+"x + "+ b + "y + ("+c+")");
        
     
     
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

    public void seqReq(int p1, int p2, int furthest ,IntList subset, IntList koHyll ){

        IntList line1 = getPoints(p1, furthest, subset);
        IntList line2 = getPoints(furthest, p2, subset);


        int furthest1 = findFurthest(p1, furthest, line1);
        int furthest2 = findFurthest(furthest, p2, line2);
        /*
         * 
         System.out.printf("line (%d -%d -%d)\n", p1, p2, furthest );
         System.out.println("\n over seq "); line1.print();
         System.out.println("\n under seq "); line2.print();
         
         System.out.printf("creating new line %d -%d\n", p1, furthest1 );
         System.out.printf("creating new line %d -%d\n", furthest1, p2 );
         */
  
        if(furthest1!=-1 && !(koHyll.contains(furthest1))){     
            seqReq(p1, furthest, furthest1, line1, koHyll);
        }

        if(!(koHyll.contains(furthest)))koHyll.add(furthest);

        if (furthest2 != -1 && !(koHyll.contains(furthest2))){
            seqReq(furthest, p2, furthest2, line2, koHyll);
        }
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

    public IntList seqMethod(){

        IntList koHyll = new IntList();


        IntList over_list = new IntList();
        IntList under_list = new IntList();

        MAX_X = findMax(x);
        MAX_Y = findMax(y);
        MIN_X = findMin(x);
        MIN_Y = findMin(y);
        for (int i = 0; i < n; i++) {
            int currX = x[i];
            int currY = y[i];
            if(i == MIN_X || i== MAX_X)continue;
            double distance= findLargestDistance(MAX_X, MIN_X, i);

          //  System.out.println("Point "+ "("+ currX+ ", "+ currY+ ") distance: "+ distance);
            

            if(distance >=0){
                over_list.add(i);
            } else if (distance <= 0)  under_list.add(i);
        }
        /*
        * 
        System.out.printf("line (%d -%d)\n", MAX_X, MIN_X );
        System.out.println(" over "); over_list.print();
        System.out.println(" under "); under_list.print();
        
        */

        int furthestAbove= findFurthest(MAX_X, MIN_X, over_list);
        int furthestBelow= findFurthest(MIN_X, MAX_X, under_list);
      
        koHyll.add(MAX_X);

        seqReq(MAX_X, MIN_X, furthestAbove, over_list, koHyll);

        koHyll.add(MIN_X);
        seqReq(MIN_X, MAX_X, furthestBelow, under_list, koHyll);


        return koHyll;


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
            System.err.println();
            local_MAX_X = findMax(localX);
            local_MAX_Y = findMax(localY);
            local_MIN_X = findMin(localX);
            local_MIN_Y = findMin(localY);
                        
            System.out.println(local_MAX_X+ ", "+ local_MAX_Y);
            System.out.println(local_MIN_X+ ", "+ local_MIN_Y);

            IntList over_list = new IntList();
            IntList under_list = new IntList();
             
            for (int i = 0; i < n; i++) {
                if(i == local_MIN_X || i== local_MAX_X)continue;
                double distance= findLargestDistance(local_MAX_X, local_MIN_X, i);
                
                if(distance >=0){
                    over_list.add(i);
                } else if (distance <= 0)  under_list.add(i);
            }

            int furthestAbove= findFurthest(local_MAX_X, local_MIN_X, over_list);
            int furthestBelow= findFurthest(local_MIN_X, local_MAX_X, under_list);
            System.out.println("jhalp prior" + furthestAbove + ", " + furthestBelow);
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


        System.out.println("jhalp" + furthest1 + ", " + furthest2);
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

        numThreads = Runtime.getRuntime().availableProcessors();
        numThreads = 1;
        Worker[] workers = new Worker[numThreads];
        for (int i = 0; i < numThreads; i++) {
            int start= (n/numThreads)*i;
            int end = (n/numThreads) * (i+1);
            if(i==numThreads -1)end = n;    
            workers[i] = new Worker(start, end-1);
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


    void findMinMaxLine(int a, int b ,int c ){
    
        a = y[MIN_X] - y[MAX_X];
        b = x[MAX_X] - x[MIN_X];
        c = y[MAX_X]*x[MIN_X] - y[MIN_X]*x[MAX_X];
        System.out.println( "f(x)= "+ a+"x + "+ b + "y "+c);
      
    }

    double findLargestDistance(int min, int max, int i){

       int  a = y[min] - y[max];
        int b = x[max] - x[min];
        int  c = y[max]*x[min] - y[min]*x[max];

        double numerator = a*x[i]+ b*y[i]+ c;
        double denominator = Math.sqrt(a*a+b*b);

        double distance = numerator/denominator;

        // System.out.println("Point "+ "("+ x+ ", "+ y+ ") distance: "+ distance);

        return distance;

    }



   
}

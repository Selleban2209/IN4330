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

    public ConvexHull(int n, int seed) {
        this.n = n;
        x = new int[n];
        y = new int[n];
        NPunkter17 p = new NPunkter17(n, seed);
        p.fyllArrayer(x, y);
        //IntList punkterList = p.lagIntList();
        //punkterList.print();

        Oblig4Precode ob4p = new Oblig4Precode(this, p.lagIntList());
        ob4p.drawGraph();
        MAX_X = findMax(x);
        MAX_Y = findMax(y);
        MIN_X = findMin(x);
        MIN_Y = findMin(y);

        System.out.println("min ("+ x[MIN_X]+ ", " + y[MIN_X]+ ")");
        System.out.println("max ("+ x[MAX_X]+ ", " + y[MAX_X]+ ")");
        findMinMaxLine(a, b, c);

        a = y[MIN_X] - y[MAX_X];
        b = x[MAX_X] - x[MIN_X];
        c = y[MAX_X]*x[MIN_X] - y[MIN_X]*x[MAX_X];

        System.out.println( "f(x)= "+ a+"x + "+ b + "y "+c);

        over_list = new IntList();
        under_list = new IntList();

        
        splitData();
        
   
        
      findFurthest(over_list, a,b,c);
      findFurthest(under_list, a,b,c);
      

      
       /* 
       System.out.println(Arrays.toString(x));
       System.out.println(Arrays.toString(y));
       */
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

    void splitData(){
        for (int i = 0; i < n; i++) {
            int currX = x[i];
            int currY = y[i];
            double distance= findLargestDistance(a, b, c, currX, currY);

            System.out.println("Point "+ "("+ currX+ ", "+ currY+ ") distance: "+ distance);
            

            if(distance >=0){
                over_list.add(i);
            } else  under_list.add(i);
        }
    }

    void findFurthest( IntList pointsList,int a, int b, int c){
        int furthestPoint=0;
        for (int l : pointsList.data) {
            int index = l;
            int currX = x[index];
            int currY = y[index];

            double distance= findLargestDistance(a, b, c, currX, currY);
            distance = Math.abs(distance);
            

            if (distance > furthestPoint) furthestPoint= index;
        }
        System.out.println("\nFurthest from over "+ "("+ x[furthestPoint]+ ", "+ y[furthestPoint]+ ")");
        conveksPoints.add(furthestPoint);

    }


    void findMinMaxLine(int a, int b ,int c ){
    
        a = y[MIN_X] - y[MAX_X];
        b = x[MAX_X] - x[MIN_X];
        c = y[MAX_X]*x[MIN_X] - y[MIN_X]*x[MAX_X];
        System.out.println( "f(x)= "+ a+"x + "+ b + "y "+c);
      
    }

    double findLargestDistance(int a, int b, int c,int x, int y){

            double numerator = a*x+ b*y+ c;
            double denominator = Math.sqrt(a*a+b*b);

            double distance = numerator/denominator;

           // System.out.println("Point "+ "("+ x+ ", "+ y+ ") distance: "+ distance);

           return distance;

    }



   
}

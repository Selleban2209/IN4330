import java.util.stream.IntStream;

public class InsertionSort {
    static Object lock = new Object();

    public static void insertSort (int [] a, int v, int h) {
        int i, t;
        for (int k = v; k < h; k++) {
            // invariant: a [v..k] is now sorted ascending (smallest first)
            t = a[k + 1];
            i = k;
            while (i>= v && a[i]<t) {
                a[i + 1] = a[i];
                i--;
            }
            a[i + 1] = t;
        } // than for k
    } //

    public static void compare(int []a, int k , int n){
        //if(n ==0)System.out.println("how the fuck did this happend");
        for (int i = k; i < n; i++) {         
           if(a[i]>a[k-1]){       
                //a[k-1] = a[i];     
                swap(a, k-1, i);    
                sortOne(a, k, k-1);
           } 
        }
    }
    public static void sortOne( int []a,int k, int h){          
        for (int j = k - 2; j >= 0 ; j--) {
            if( a[j] < a[j+1]){
                //a[j+1] = a[h];
                swap(a, j, j+1);
              
                //  System.out.println("swappe happened in sortOne between "+ a[j] + " and "+ a[j+1]);
            } else {
                return;
                
            }
        }
    }   
    
    public static void compare2(int []a, int k , int n){
        //if(n ==0)System.out.println("how the fuck did this happend");
        for (int i = k; i < n; i++) {         
           if(a[i]>=a[k-1]){       
               // swap(a, k-1, i);    
               swap(a, k-1, i);     
                sortOne2(a, k, k-1);
           } 
        }
    }

    public static void sortOne2( int []a,int k, int h){   
        int t =a[h];      
        for (int j = k - 2; j >= 0 ; j--) {
            if( a[j]<=t){
                //a[j+1] = a[h];
                swap(a, j, h);
                return;
                //  System.out.println("swappe happened in sortOne between "+ a[j] + " and "+ a[j+1]);
            }
        }
    }   
    public static void compareRetEearly(int []a, int k , int n){
        for (int i = k; i < n; i++) {         
           if(a[i+1]>=a[k]){       
                a[k] = a[i];
                sortOneRetEarly(a, k, i+1);
           } 
        }
    }

    public static void sortOneRetEarly( int []a,int k, int h){          
        int t = a[h];
        for (int j = 0; j < k; j++) {               
            if(a[j]<=t ){ 
               swap(a, j, h);  
                return;
                
            }                              
        }
    }   
    

    public static  void swap(int a[], int ind1, int ind2) {
        int temp = a[ind1];
        a[ind1] = a[ind2];
        a[ind2] = temp;
    }

    
}
    


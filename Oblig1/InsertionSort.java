public class InsertionSort {


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

    public static void compare(int []a, int v , int h){
        for (int i = v; i < h; i++) {         
           if(a[i+1]> a[v]){
              
                a[v] = a[i];
                sortOne(a, v, i+1);
           } 
        }
    }

    public static void sortOne( int []a,int k, int h){          
        for (int j = 0; j < k; j++) {               
            int t = a[j];
            if(a[j]<= a[h] ){
                a[j] = a[h];
                a[h] = t;
                
            }
            
                          
        }
    }
      
    

    
    
}
    


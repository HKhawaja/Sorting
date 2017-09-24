import java.lang.management.*;   //Time purposes
import java.util.Scanner;

public class Sort {

    public static Scanner keyboard = new Scanner (System.in);
    public static void main (String[] args) {

	ThreadMXBean bean = ManagementFactory.getThreadMXBean();   //Time purposes

	int N = Integer.parseInt(args[0]);

	System.out.println ("Generating " + N + " random numbers");
	int[] a = new int[N];
	for (int i=0; i<N; ++i) 
	    a[i] = (int)(Math.random()*(1<<30));

	// To test a particular sorting algorithm, I make a copy of the original
	// array, and then record the current "user time".  After running the
	// sort, I compute the elapsed time by taking the difference between the
	// time and the time from before the sort.

	
	int[] c = new int[N];
	for (int i=0; i<N; ++i) c[i] = a[i];

	System.out.println ("Enter 1 to run Insertion Sort, 2 to run non-randomized Quick Sort, 3 to run randomized Quick Sort, 4 to run recursive Merge Sort or 5 to run Iterative Merge Sort. ");
	int key = keyboard.nextInt();
	    
	if (key == 1) {
	    long t = bean.getCurrentThreadUserTime();   //Note Time  (t is a *long*)
	    insertionSort(c);
	    //print(c);
	    System.out.printf ("Insertion sort took %f seconds.\n",     //Note Time again
			       (bean.getCurrentThreadUserTime()-t) / 1e9);
	}

	else if (key == 2) {
	    long t = bean.getCurrentThreadUserTime();   // Note Time  (t is a *long*)
	    quickSort(c,0,c.length-1); //runs quicksort and notes the time taken for it to run
	    //print(c);
	    System.out.printf ("Quick sort took %f seconds.\n",     // Note Time again
			       (bean.getCurrentThreadUserTime()-t) / 1e9);
	}

	else if (key == 3) {
	    long t = bean.getCurrentThreadUserTime();   // Note Time  (t is a *long*)
	    randomizedQuickSort (c,0,c.length-1); //randomizedQuickSort uses a randomized pivot
	    //print(c);
	    System.out.printf ("Randomized Quick sort took %f seconds.\n",     //Note Time again
			       (bean.getCurrentThreadUserTime()-t) / 1e9);
	    
	}
	   
	else if (key == 4) {
	    long t = bean.getCurrentThreadUserTime();   // Note Time  (t is a *long*)
	    mergeSort (c,0, c.length-1);
	    //print(c);
	    System.out.printf ("Recursive Merge sort took %f seconds.\n",     // Note Time Again
			       (bean.getCurrentThreadUserTime()-t) / 1e9);
	} 

	else if (key == 5) {
	    long t = bean.getCurrentThreadUserTime();   // Note Time  (t is a *long*)
	    iterativeMergeSort(c); //iterativeMergeSort is not only iterative but also reuses an array
	    //print(c);
	    System.out.printf ("Iterative Merge sort took %f seconds.\n",     // Note Time again
			       (bean.getCurrentThreadUserTime()-t) / 1e9);
	    
	}	
	    
	    
	    
    }
    
    public static void mergeSort (int[]a, int p, int q) {
	    
	if (p<q) {
	    int r = (p+q)/2; //splits arrays into halves and calls mergeSort recursively on them
	    mergeSort (a, p,r);
	    mergeSort (a, r+1,q);
	    merge (a,p,r,q);
	}
    }
    
    public static void merge (int [] a, int p, int r, int q) {
	//creation of two arrays to sort from 
	
	int [] left = new int [r-p+1];
	for (int i = p; i<=r; i++)
	    left[i-p] = a[i]; 
	

	int [] right = new int [q-r];
	for (int i = r+1; i<=q; i++)
	    right [i-r-1]= a[i];
	
	
	int i = 0; //tracker
	int j = 0;//variables
	
	for (int k = p; k<=q; k++) {
	    if (left[i] <= right[j]) {
		a[k] = left[i];
		i++;
		if (i == left.length) { //copies all elements into the destination if the end of one sorter array is reached
		    for (int l = k+1; l<=q; l++) {
			a[l] = right[j];
			j++;
		    }
		    break;
		}
	    }
	    else {
		a[k] = right[j];
		j++;
		if (j == right.length) { 
		    for (int l = k+1; l<=q; l++) {
			a[l] = left[i];
			i++;
		    }
		    break;
		}
	    }
		
	}
	    
	
    }
    
    public static void iterativeMergeSort (int[] a) {
	int [] copyTracker = new int [a.length]; //creates one copyTracker array which is reused instead of two smaller arrays
	for (int i = 0; i<a.length; i++) 
	    copyTracker[i] = a[i];
	
	int blockSize = 2;
	
	while (blockSize < a.length*2) {
	    
	    for (int i = 0; i<a.length; i= i+blockSize) {
		int end = i+blockSize-1;
		int cutoff = (i+end)/2 +1;
		if (end >= a.length) { //reduces end and cutoff indexes to largest possible index if they are greater
		    end = a.length -1;
		}
		
		if (cutoff >= a.length) {
		    cutoff = a.length-1;
		}
		
		iterativeMerge (a,copyTracker, i, cutoff, end); //calls a slightly modified version of merge
	    }
	    blockSize *=2;
	}
	
    }
	
    public static void iterativeMerge (int [] a, int [] copyTracker, int start, int cutoff, int end) {
	
	int i = start; //copyTracker
	int j = cutoff;//variables
	
	for (int k = start; k<=end; k++) { //rest of code works similar to merge
	    if (copyTracker[i] <= copyTracker[j]) {
		a[k] = copyTracker[i];
		i++;
		if (i == cutoff) {
		    for (int l = k+1; l<=end; l++) {
			a[l] = copyTracker[j];
			j++;
		    }
		    break;
		}
	    }
	    else {
		a[k] = copyTracker[j];
		j++;
		if (j == end+1) {
		    for (int l = k+1; l<=end; l++) {
			a[l] = copyTracker[i];
			i++;
		    }
		    break;
		}
	    }
		
	}
	    
	if (!(start==0 && end==copyTracker.length-1)) { //copyTracker is updated after every call to iterativeMerge unless this is the last call
	    for (int k = start; k<=end; k++) 
		copyTracker[k] = a[k];
	}
    }
	    
    
    /////////////////////////////////////////////////////////////////////////
    // This method prints the contents of an array.  You might use it during
    // debugging.
    /////////////////////////////////////////////////////////////////////////
    
    public static void print(int[] a) {
	for (int i=0; i<a.length; ++i)
	    System.out.println (a[i]);
	System.out.println();
    }
    
    /////////////////////////////////////////////////////////////////////////
    // This method compares the contents of two arrays.  You might use it
    // during debugging to compare the results of two different algorithms.
    /////////////////////////////////////////////////////////////////////////
    
    public static void check(int[] a, int[] b) {
	for (int i=0; i<a.length; ++i)
	    if (a[i] != b[i]) 
		throw new RuntimeException ("Error in sorting results");
    }
    
    /////////////////////////////////////////////////////////////////////////
    // Here's the insertion sort.
    /////////////////////////////////////////////////////////////////////////
    
    public static void insertionSort(int[] a) {
	
	int n = a.length;
	
	for (int i=1; i<n; ++i) {
	    
	    int t = a[i];
	    int j = i-1;
	    while (j >= 0 && t < a[j]) {
		a[j+1] = a[j];
		j--;
	    }
	    a[j+1] = t;
	}
    }
    
    //quicksort without a random pivot
    public static void quickSort (int [] a, int p, int q) {
	if (p<q) {
	    int r = partition (a,p,q);
	    quickSort (a,p,r-1);
	    quickSort (a,r+1,q);
	}
	
    }
    
    //partition without a random pivot
    public static int  partition (int [] a, int p, int q) {
	
	int pivot = a[p]; //chooses the first element in the array to be sorted as the pivot
	int lastSmall = p; //keeps track of last smallest element
	
	for (int i = p+1; i<= q; i++) {
	    if (a[i] <= pivot) {
		lastSmall ++;
		int swapper = a[lastSmall];
		a[lastSmall] = a[i];
		a[i] = swapper;
	    }
	}
	
	a[p] = a[lastSmall]; //swaps pivot and lastSmall element and returns index of pivot
	a[lastSmall] = pivot;
	return lastSmall;
	
    }
	    
    public static void randomizedQuickSort (int [] a, int p, int q) { //quickSort with a random pivot
	if (p<q) {
	    int r = randomizedPartition (a,p,q);
	    randomizedQuickSort (a,p,r-1);
	    randomizedQuickSort (a,r+1,q);
	}

    }

    public static int randomizedPartition (int [] a, int p, int q) {
	
	int randomIndex = p+(int) Math.random()*(q-p+1); //finds a random index and replaces it with the first element of the subarray to be sorted
	int replacer = a[p];
	a[p] = a[randomIndex];
	a[randomIndex] = replacer;

	int pivot = a[p];//rest of randomizedPartition works similar to partition
	int lastSmall = p;

	for (int i = p+1; i<= q; i++) {
	    if (a[i] <= pivot) {
		lastSmall ++;
		int swapper = a[lastSmall];
		a[lastSmall] = a[i];
		a[i] = swapper;
	    }
	}

	a[p] = a[lastSmall];
	a[lastSmall] = pivot;
	return lastSmall;

    }

    


}




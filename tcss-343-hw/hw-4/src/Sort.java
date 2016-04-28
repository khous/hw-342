import java.util.*;


/**
 * A class that contains a group of sorting algorithms.
 * The input to the sorting algorithms is assumed to be
 * an array of integers.
 * 
 * @author Donald Chinn
 * @version April 22, 2016
 */
public class Sort {

    // Constructor for objects of class Sort
    public Sort() {
    }

    /**
     * Sort an array of integers in ascending order using insertion sort.
     *
     * @param data  an array of integers
     *
     */
    public static void insertionSort (int[] data) {
        for (int i = 1; i < data.length; i++) {
            int temp = data[i];
            int j = i-1;
            while ((j >= 0) && (data[j] > temp)) {
                data[j+1] = data[j];
                j--;
            }
            data[j+1] = temp;
        }
    }
    
    /**
     * Sort an array of integers in ascending order using mergesort.
     *
     * @param data  an array of integers
     *
     */
    public static void mergesort (int[] data) {
        mergesortRecursive (data, 0, data.length - 1);
    }

    /**
     * Sort the subarray of integers data[low .. high] in ascending
     * order using mergesort. This is meant to be a helper method for
     * mergesort
     *
     * @param data  an array of integers
     * @param low   low index
     * @param high  high index
     *
     */
    private static void mergesortRecursive (int[] data, int low, int high) {
        if (low < high) {
            mergesortRecursive (data,
                                low,
                                low + (high-low)/2 );
            mergesortRecursive (data,
                                low + (high-low)/2 + 1,
                                high);
            merge (data, low, high);
        }
    }
    

    /**
     * Given a subarray of integers data[low .. high], consisting of
     * sorted (ascending) elements from data[low .. low + (high-low)/2] and
     * sorted (ascending) elements from data[low + (high-low)/2 + 1 .. high],
     * merge the two sorted subarrays into one sorted (ascending) subarray
     * data[low .. high].
     *
     * @param data  an array of integers
     * @param low   low index
     * @param high  high index
     *
     */
    private static void merge (int[] data, int low, int high) {
        // create a new array for the sorted data
        int[] temp = new int[high - low + 1];
        
        int lowIndex = low;
        int highIndex = low + (high-low)/2 + 1;
        int midIndex = low + (high-low)/2;
        int tempIndex = 0;
        
        while ((lowIndex <= midIndex) &&
            (highIndex <= high)) {
            if (data[lowIndex] < data[highIndex]) {
                temp[tempIndex] = data[lowIndex];
                tempIndex++;
                lowIndex++;
            } else {
                temp[tempIndex] = data[highIndex];
                tempIndex++;
                highIndex++;
            }
        }
        
        if (lowIndex > midIndex) {
            // low subarray finished first
            while (highIndex <= high) {
                temp[tempIndex] = data[highIndex];
                tempIndex++;
                highIndex++;
            }
        
        } else {
            // high subarray finished first
            while (lowIndex <= midIndex) {
                temp[tempIndex] = data[lowIndex];
                tempIndex++;
                lowIndex++;
            }
        }
        
        // copy data back from temp to data
        int dataIndex;
        for (tempIndex = 0, dataIndex = low;
            dataIndex <= high;
            tempIndex++, dataIndex++) {
            
            data[dataIndex] = temp[tempIndex];
        }
    }
    
    

    /**
      * A USEFUL COMMENT GOES HERE.
      *
      */
    public static void heapsort (int[] data) {
        //Bundle up integers in their wrapper class in order to conform to make them Comparable
        Integer[] d = new Integer[data.length];
        for (int i = 0; i < data.length; i++)
            d[i] = data[i];

        BinaryHeap heapers = BinaryHeap.buildHeap(d);

        for (int i = 0; !heapers.isEmpty(); i++) {
            try {
                data[i] = ((Integer) heapers.deleteMin()).intValue();
            } catch (EmptyHeapException e) {
                System.err.println("Oops, empty heapers");
            }
        }
    }


    /**
     * Given an integer size, produce an array of size random integers.
     * The integers of the array are between 0 and size (inclusive) with
     * random uniform distribution.
     * @param size  the number of elements in the returned array
     * @return      an array of integers
     */
    public static int[] getRandomArrayOfIntegers(int size) {
        int[] data = new int[size];
        for (int i = 0; i < size; i++) {
            data[i] = (int) ((size + 1) * Math.random());
        }
        return data;
    }
    

    /**
     * Given an integer size, produce an array of size random integers.
     * The integers of the output array are between 0 and size-1 with
     * exactly one of each in the array.  Each permutation is generated
     * with random uniform distribution.
     * @param size  the number of elements in the returned array
     * @return      an array of integers
     */
    public static int[] getRandomPermutationOfIntegers(int size) {
        int[] data = new int[size];
        for (int i = 0; i < size; i++) {
            data[i] = i;
        }
        // shuffle the array
        for (int i = 0; i < size; i++) {
            int temp;
            int swap = i + (int) ((size - i) * Math.random());
            temp = data[i];
            data[i] = data[swap];
            data[swap] = temp;
        }
        return data;
    }


    /**
     * Perform checks to see if the algorithm has a bug.
     */
    private static void testCorrectness() {
        int[] data = getRandomPermutationOfIntegers(100);
        
        for (int i = 0; i < data.length; i++) {
            System.out.println("data[" + i + "] = " + data[i]);
        }
        
        int k = 100;
        heapsort(data);
        
        // verify that data[i] = i
        for (int i = 0; i < data.length; i++) {
            if (data[i] != i) {
                System.out.println ("Error!  data[" + i + "] = " + data[i] + ".");
            }
        }
    }
    
    
    /**
     * Perform timing experiments.
     */
    private static void testTiming () {
        // timer variables
        long totalTime = 0,
            startTime,
            finishTime;

        // start the timer
        int [] valuesOfN = {
                100000, 200000, 400000,
                800000, 1600000, 3200000
        };


        for (int n : valuesOfN) {
            int[] data = getRandomArrayOfIntegers(n);
            //Run the test for heap sort
            Date startDate = new Date();
            startTime = startDate.getTime();
            mergesort(data);
//            heapsort(data);
            Date finishDate = new Date();
            finishTime = finishDate.getTime();
            totalTime += (finishTime - startTime);
            System.out.println("mergesort," + n + "," + totalTime);
//            System.out.println("heapsort," + n + "," + totalTime);
        }
    }
    
    
    /**
     * code to test the sorting algorithms
     */
    public static void main (String[] argv) {
//        testCorrectness();
        testTiming();
        System.out.println("");
        System.out.println("Warm up over");
        System.out.println("");
        System.out.println("Algorithm,n,Time(ms)");
        testTiming();
        testTiming();
        testTiming();
    }
}

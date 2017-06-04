/**
 * A recursive implementation of BinarySearch.
 * Contains a testing method that validates the functionality
 * of this implementation through test values/arrays.
 *
 * Binary Search is a technique for looking through an array.
 * Input: sorted array, target element
 * Output: index of target element (-1 if DNE)
 *
 * Binary Search is logarithmic (Very fast) O(log sub n)
 *
 * @author: Sabirah Shuaybi
 */

public class BinarySearch {

    /**
     * Searches the sorted array for the test int.
     * Assumes the array is sorted in increasing order (smallest at index 0).
     * @param sorted: a sorted integer array (ascending order)
     * @param test: the value to search for in the int array
     * @return If test is found, returns its index; otherwise, returns -1.
     **/
    public static int binarySearch( int[] sorted, int test )
    {
        //Start the recursion between first and last indices
        return binarySearch(sorted, test, 0, sorted.length -1);
    }

    /**
     * A private helper method for binarySearch
     * Searches the sorted array for the test number between loIndex and hiIndex, inclusive.
     * Assumes the array is sorted in increasing order (smallest at index 0).
     * @param sorted: a sorted integer array (ascending order)
     * @param test: the value to search for in the int array
     * @return If test is found, returns its index; otherwise, returns -1.
     **/
    private static int binarySearch(int[] sorted, int test, int loIndex, int hiIndex) {

        //The middle index of the sorted array
        int middleIndex = (loIndex + hiIndex)/2;

        //Test value is not in array
        if(loIndex > hiIndex) {
            return -1;
        }
        //If test value is greater than the value at the middle of array,
                //narrow search to second half of array
        else if(test > sorted[middleIndex]) {
            return binarySearch(sorted, test, middleIndex+1, hiIndex);
        }
        //If test value is less than the value at the middle of array,
                //narrow search to first half of array
        else if(test < sorted[middleIndex]) {
            return binarySearch(sorted, test, loIndex, middleIndex-1);
        }
        //OR the test value IS the value at the middle index,
                //so just return the middle index
        else return middleIndex;
    }

    /**
     * A tester method for evaluating the binarySearch implementation
     * Searches the through oddArray for each value in testArray
     * Prints out the results of each search - line by line
     * @param oddArray: an array of the first 100 odd integers
     * @param testArray: a test array
     **/
    private static void oddTester(int[] oddArray, int[] testArray) {

        for(int i=0; i<testArray.length; i++) {

            //Each element in testArray serves as the test value against oddArray
            int result = binarySearch(oddArray, testArray[i]);

            //Print out value you're searching for and result (aka the index or -1 if not found)
            System.out.println("Searching for " + testArray[i] + ": " + result);
        }
    }

    /**
     * Creates and initializes an array of the first 100 odd integers
     * @return an integer array of the first 100 odd integers starting from 1
     **/
    private static int[] createOddArray() {

        int[] oddArray = new int[100];

        for(int i=0; i<oddArray.length; i++) {
            //This formula uses i itself to calculate the odd number value
            oddArray[i] = (i*2)+1;
        }
        return oddArray;
    }

    public static void main (String[] args) {

        //Initialize test array
        int[] testArray = {26, 78, 100, 186, 13, 99, 101, 177};
        BinarySearch.oddTester(createOddArray(), testArray);
    }
}

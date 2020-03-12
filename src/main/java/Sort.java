import java.util.InputMismatchException;

/**
 * This is the class which implements five most popular sorting algorithms.
 * It is generic, which means it can operate on any object type that can be compared.
 * The detailed description of each algorithm is given, including its time complexity and stability
 * (A sorting algorithm is said to be stable if two objects with equal keys appear in the same order
 * in sorted output as they were in the input array).
 */

public class Sort<T extends Comparable<T>> {

    private T[] arr;
    private int n;

    public Sort(T[] arr) { // a simple constructor
        this.arr = arr;
        n = arr.length;
    }

    /**
     * A method that starts the computation.
     * The number from 1 to 5 is passed to the method, and the correct sorting algorithm applies to the array.
     * If the received number is not supported, an exception is thrown and then caught in the calling method.
     */
    public double run(int choice) {
        double startTime = System.nanoTime(); // we save the time at the start of calculation
        switch (choice) {
            case 1:
                bubbleSort(arr);
                break;
            case 2:
                selectionSort(arr);
                break;
            case 3:
                insertionSort(arr);
                break;
            case 4:
                new MergeSort().start(arr);
                break;
            case 5:
                new QuickSort().start(arr);
                break;
            default:
                throw new InputMismatchException();
        }
        return (System.nanoTime() - startTime) / Math.pow(10, 9); // we return the time spent on calculation
    }

    public boolean isSorted() { // a public method that checks if the array is sorted
        for (int i = 1; i < n; i++)
            if (arr[i].compareTo(arr[i - 1]) < 0)
                return false;
        return true;
    }

    /**
     * We traverse through the array up to (n - 1) times, each time checking if the two neighbour elements
     * are in order. If they are not, we swap them. We repeat, reducing the inspected area by one element,
     * if there was at least one swap during the last loop.
     *
     * Worst-case time complexity is O(n^2), the algorithm is in-place (it does not require extra space)
     * and stable.
     */
    private void bubbleSort(T[] arr) {
        boolean swaps;
        for (int i = 0; i < n - 1; i++) { // since we always compare two elements, we will need to iterate up to (n - 1) times
            swaps = false;
            for (int j = 1; j < n - i; j++) // we reduce the inspected area by one element at each loop, because the largest element will already be pushed to the right
                if (arr[j].compareTo(arr[j - 1]) < 0) {
                    swap(arr, j, j - 1); // the swap method is implemented further
                    swaps = true;
                }
            if (!swaps) break; // we stop if there were no swaps during the last iteration
        }
    }

    /**
     * For each element in the array, we traverse through the part of the array on the right of this element
     * and swap it with the minimal element found. Thus, maximum number of swaps is n.
     *
     * Worst-case time complexity is O(n^2), the algorithm is in-place.
     * However, this default implementation is not stable.
     */
    private void selectionSort(T[] arr) {
        for (int i = 0; i < n; i++) {
            int min = i; // the index of the minimal element is set to i by default
            for (int j = i + 1; j < n; j++) {
                if (arr[j].compareTo(arr[min]) < 0)
                    min = j;
            }
            swap(arr, i, min);
        }
    }

    /**
     * For each element in the array, if it is less than its left neighbour, we swap the two elements and repeat
     * this step as many times as needed, thus shifting each element left to its place.
     *
     * Worst-case time complexity is O(n^2), the algorithm is in-place and stable.
     */
    private void insertionSort(T[] arr) {
        for (int i = 1; i < n; i++)
            for (int j = i; j > 0 && arr[j].compareTo(arr[j - 1]) < 0; j--) // we create the variable j to shift the element
                swap(arr, j, j - 1);
    }

    private void swap(T[] arr, int a, int b) { // a simple method that swaps tho elements in the array given their indices
        T temp = arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
    }

    /**
     * We recursively split the array to halves until the size of each part equals 1. Then, we copy the two
     * sorted parts and traverse through each part in the copy, comparing elements in them and rewriting
     * the original array.
     *
     * Worst-case time complexity is O(n log n), the algorithm is stable. However, to complete this algorithm,
     * we need to have a copy of the original array. As such, it is not in-place.
     */
    private class MergeSort {

        @SuppressWarnings("unchecked cast") // to be able to generify the whole Sort class and to create the copy of the array at the same time, we need to downcast which is secure in this case
        private void start(T[] arr) {
            T[] temp = (T[]) new Comparable[n]; // we create an empty array of type and size of the original array
            sort(arr, temp, 0, n - 1);
        }

        private void sort(T[] arr, T[] temp, int lo, int hi) { // lo and hi specify the size of the part to be sorted
            if (lo >= hi) return; // we return if the size of the part equals 1
            int mid = lo + (hi - lo) / 2;
            sort(arr, temp, lo, mid); // sorting the left part
            sort(arr, temp, mid + 1, hi); // sorting the right part
            merge(arr, temp, lo, hi, mid); // merging two parts
        }

        private void merge(T[] arr, T[] temp, int lo, int hi, int mid) {
            int i = lo, j = mid + 1; // defining pointers to iterate through two sorted parts
            System.arraycopy(arr, lo, temp, lo, hi + 1 - lo); // copying the two parts into our temp array

            for (int k = lo; k <= hi; k++) {
                if (i > mid) arr[k] = temp[j++]; // if the left part is exhausted, we take the element from the right
                else if (j > hi) arr[k] = temp[i++]; // if the right part is exhausted, we take the element from the left
                else if (temp[j].compareTo(temp[i]) < 0) arr[k] = temp[j++]; // taking the minimal element out of two sorted parts and replacing the element in the original array
                else arr[k] = temp[i++];
            }
        }
    }

    /**
     * Firstly, we shuffle the original array to improve performance. Then we take the first element of the
     * array and evaluate all other elements against it. Moving from both ends of the array, we place
     * lesser elements to the left part and greater elements to the right part. Finally, we put our pivot
     * element to its place and recursively repeat the same procedure to both left and right parts
     * (with regard to the index of the pivot element) till they are equal to 1.
     *
     * Worst-case time complexity is O(n^2). However, it is hardly possible when we shuffle the array.
     * The average-case performance should rather be considered for this algorithm, the time complexity
     * being O(n log n).
     * The algorithm is in-place which is helpful for sorting large arrays. This default implementation
     * is not stable though.
     */
    private class QuickSort {

        private void start(T[] arr) {
            shuffle(arr);
            sort(arr, 0, n - 1);
        }

        private void shuffle (T[] arr) {
            for (int i = 1; i < n; i++) {
                int random = (int) (Math.random() * (i + 1)); // for each element i in the array, we define a random element from [0, i]
                swap(arr, i, random); // we swap the two elements
            }
        }

        private void sort(T[] arr, int lo, int hi) { // lo and hi specify the size of the part to be sorted
            if (lo >= hi) return; // we return if the size of the part equals 1
            int pivot = partition(arr, lo, hi); // receiving the index of the pivot element
            sort(arr, lo, pivot - 1); // sorting the left part
            sort(arr, pivot + 1, hi); // sorting the right part
        }

        private int partition(T[] arr, int lo, int hi) {
            int i = lo, j = hi + 1; // defining two pointers on both ends of the inspected part
            while (true) {
                while (arr[++i].compareTo(arr[lo]) < 0) // shifting right as long as the element is less than the first element
                    if (i == hi) break; // breaking if we have reached the right end
                while (arr[lo].compareTo(arr[--j]) < 0) // shifting left as long as the element is greater than the first element
                    if (j == lo) break; // breaking if we have reached the left end
                if (i >= j) break; // breaking if the pointers meet
                swap(arr, i, j); // swapping the elements at two pointers
            }
            swap(arr, lo, j); // putting the first element in its place
            return j;
        }
    }
}
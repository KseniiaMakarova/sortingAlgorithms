import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * This is the starting point of the program, which creates an integer array of size n (specified by user)
 * and fills it with random numbers from 0 to n inclusively. The user then chooses the sorting algorithm
 * that will be applied to this array. The running time of the chosen algorithm will be shown to the user.
 */

public class Client {

    public static void main(String[] args) {
        int n;
        boolean correctInput;
        Integer[] arr = null;

        System.out.println("Hello! Type in the size of your array:");
        do { // we ask user to enter the array size again until we receive a correct input
            try {
                correctInput = true;
                n = new Scanner(System.in).nextInt();
                arr = new Integer[n];
                for (int i = 0; i < n; i++)
                    arr[i] = (int) (Math.random() * (n + 1)); // array is filled with random numbers [0, n]
            } catch (NoSuchElementException e) {
                System.out.println("This is not an integer number, please try again:");
                correctInput = false;
            } catch (NegativeArraySizeException e) {
                System.out.println("The number cannot be negative, try again please:");
                correctInput = false;
            }
        } while (!correctInput);

        System.out.println("Thanks! Select the type of sorting algorithm that you wish to choose:\n1 - bubble sort;\n2 - selection sort;\n3 - insertion sort;\n4 - merge sort;\n5 - quick sort;");
        Sort<Integer> mySort = new Sort<>(arr); // we create a Sort object and pass our array to the constructor

        do { // we ask user to choose the type of algorithm again until we receive a correct input
            try {
                correctInput = true;
                int choice = new Scanner(System.in).nextInt();
                double time = mySort.run(choice); // we start our sorting and receive the time needed to complete it
                System.out.printf("The algorithm is completed in %.7f seconds.", time);
                System.out.print("\nIs the array sorted? ");
                System.out.println(mySort.isSorted() ? "Yes!" : "No :("); // we check if our sorting was successful and display the result
            } catch (InputMismatchException e) { // the exception is thrown either in this method (wrong type is entered) or in mySort.run() (incorrect integer in entered)
                System.out.println("Please enter an integer from 1 to 5:");
                correctInput = false;
            }
        } while (!correctInput);
    }
}

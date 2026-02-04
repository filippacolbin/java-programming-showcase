import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

/**
 * This program handels random numbers
 *
 * @author Filippa Colbin
 * @version 1.0
 */

class Main {

    static final String USER_INPUT_PROMPT = "How many random numbers in the range 0 - 999 are desired?";
    static final String RANDOM_NUMBERS_LIST_MESSAGE = "Here are the random numbers:";
    static final String RANDOM_NUMBERS_SORTED_MESSAGE = "Here are the random numbers arranged:";
    static final String INVALID_INPUT_MESSAGE = "Invalid Input";
    static final int MAX_NUMBER_INPUT = 1000;

    public static void main(final String[] args) {
        Scanner userInput = new Scanner(System.in);
        Random random = new Random();

        int[] randomArray = null;
        int randomCount = 0;

        int[] evenArray = null;
        int evenCount = 0;

        int[] oddArray = null;
        int oddCount = 0;

        System.out.println(USER_INPUT_PROMPT);

        try {
            randomCount = userInput.nextInt();
            randomArray = new int[randomCount];
            evenArray = new int[randomCount];
            oddArray = new int[randomCount];

        } catch (InputMismatchException e) {
            System.out.println("Error, Please enter an integer");
            userInput.nextLine();
        } catch (OutOfMemoryError e) {
            System.out.println("Error, Cannot allocate so much memory");
        }

        if (randomCount <= 0) {
            System.out.println(INVALID_INPUT_MESSAGE);
            System.exit(0);
        }

        //Generate Random numbers
        //for loop, randomArray.length
        for (int i = 0; i < randomArray.length; i++) {
            randomArray[i] = random.nextInt(MAX_NUMBER_INPUT);
        }

        System.out.println();

        //Print random array
        System.out.println(RANDOM_NUMBERS_LIST_MESSAGE);
        for (int i = 0; i < randomArray.length; i++) {
            System.out.print(randomArray[i]);
            System.out.print(" ");
        }

        System.out.println();

        System.out.println(" ");
        System.out.println(RANDOM_NUMBERS_SORTED_MESSAGE);

        //Sort by even and odd
        for (int i = 0; i < randomCount; i++) {
            if (randomArray[i] % 2 == 0) {
                evenArray[evenCount] = randomArray[i];
                evenCount++;
            } else {
                oddArray[oddCount] = randomArray[i];
                oddCount++;
            }
        }

        //Sort the evenArray
        for (int i = 0; i < evenCount; i++) {
            for (int j = 0; j < evenCount; j++) {
                if (evenArray[j] > evenArray[j + 1]) {
                    int temp = evenArray[j];
                    evenArray[j] = evenArray[j + 1];
                    evenArray[j + 1] = temp;
                }
            }
        }

        //Sort the oddArray
        for (int i = 0; i < oddCount; i++) {
            for (int j = 0; j < oddCount; j++) {
                if (oddArray[j] > oddArray[j + 1]) {
                    int temp = oddArray[j];
                    oddArray[j] = oddArray[j + 1];
                    oddArray[j + 1] = temp;
                }
            }
        }

        //Print out sorted even and odd numbers in one line
        for (int i = 0; i < evenCount; i++) {
            System.out.print(evenArray[i] + " ");
        }
        if (evenCount > 0 && oddCount > 0) {
            System.out.print(" - ");
        }
        for (int i = 0; i < oddCount; i++) {
            System.out.print(oddArray[i] + " ");
        }

        System.out.println();

        //Counts
        System.out.println("\nOf the above " + randomCount + " numbers, " + evenCount + " were even and " + oddCount + " odd.");

        userInput.close();
    }
}

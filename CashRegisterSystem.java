import java.util.Scanner;
import java.util.Date;
/**
 * Cash Register System
 * With the system you can insert, remove, display and sell items.
 * The system also allows for maintaining sales history.
 *
 * @author Filippa Colbin
 */
public class Main {

    // Constants for the item array
    public static final int ITEM_ID = 0;
    public static final int ITEM_COUNT = 1;
    public static final int ITEM_PRICE = 2;
    public static final int ITEM_COLUMN_SIZE = 3;
    public static final int INITIAL_ITEM_SIZE = 10;

    // Constants for the sales array
    public static final int SALE_ITEM_ID = 0;
    public static final int SALE_ITEM_COUNT = 1;
    public static final int SALE_ITEM_PRICE = 2;
    public static final int SALE_COLUMN_SIZE = 3;
    public static final int MAX_SALES = 1000;

    // Menu constants
    public static final int MENU_ITEM_1 = 1;
    public static final int MENU_ITEM_2 = 2;
    public static final int MENU_ITEM_3 = 3;
    public static final int MENU_ITEM_4 = 4;
    public static final int MENU_ITEM_5 = 5;
    public static final int MENU_ITEM_6 = 6;
    public static final int MENU_ITEM_Q = -1;

    public static final int INITIAL_ITEM_NUMBER = 999;
    public static final int MIN_ITEM_COUNT = 1;
    public static final int MAX_ITEM_COUNT = 10;
    public static final int MIN_ITEM_PRICE = 100;
    public static final int MAX_ITEM_PRICE = 1000;

    private static Scanner userInputScanner = new Scanner(System.in);

    /**
     * Allows test injection of a custom scanner for unit testing.
     * @param inputScanner test scanner object
     */
    public static void injectInput(final Scanner inputScanner) {
        userInputScanner = inputScanner;
    }

    public static void main(final String[] args) {
        int[][] items = new int[INITIAL_ITEM_SIZE][ITEM_COLUMN_SIZE];
        int[][] sales = new int[MAX_SALES][SALE_COLUMN_SIZE];
        Date[] saleDates = new Date[MAX_SALES];
        int lastItemNumber = INITIAL_ITEM_NUMBER;

        while (true) {
            int userSelection = menu();
            switch (userSelection) {
                case MENU_ITEM_1:
                    System.out.println("How many items do you want to add?");
                    int noOfItems = input();
                    items = insertItems(items, lastItemNumber, noOfItems);
                    lastItemNumber += noOfItems;
                    System.out.println(noOfItems + " items added!");
                    break;

                case MENU_ITEM_2:
                    System.out.println("Specify an item ID to remove");
                    int itemIdToDelete = input();
                    int deletedStatus = removeItem(items, itemIdToDelete);
                    if (deletedStatus == 0) {
                        System.out.println("Successfully removed item " + itemIdToDelete);
                    } else {
                        System.out.println("Could not find item " + itemIdToDelete);
                    }
                    break;
                case MENU_ITEM_3:
                    printItems(items);
                    break;
                case MENU_ITEM_4:
                    System.out.println("Enter item ID and number of items to be sold");
                    int itemIdToSell = input();
                    int amountToSell = input();
                    int sold = sellItem(sales, saleDates, items, itemIdToSell, amountToSell);
                    if (sold == 0) {
                        System.out.println("Sold " + amountToSell + " units of " + itemIdToSell);
                    } else if (sold == -1) {
                        System.out.println("Could not find item " + itemIdToSell);
                    } else {
                        System.out.println("Failed to sell specified amount, only " + sold + " units available!");
                    }
                    break;
                case MENU_ITEM_5:
                    printSales(sales, saleDates);
                    break;
                case MENU_ITEM_6:
                    sortedTable(sales, saleDates);
                    break;
                case MENU_ITEM_Q:
                    System.out.println("Terminating...");
                    System.exit(0);
                    break;

                default:
                    System.out.println("That is an invalid entry");
                    break;
            }
        }
    }

    /**
     * Displays menu and returns user selection.
     * @return selected menu option as integer
     */
    public static int menu() {
        System.out.println("\n--- Cash Register Menu ---");
        System.out.println("1. Insert items");
        System.out.println("2. Remove an item");
        System.out.println("3. Display a list of items");
        System.out.println("4. Register a sale");
        System.out.println("5. Display sales history");
        System.out.println("6. Sort and display sales history table");
        System.out.println("q. Quit");
        System.out.print("Your Selection: ");
        return input();
    }

    /**
     * Gets valid numeric input from user.
     * @return integer input or -1 if 'q' is entered
     */
    public static int input() {
        while (true) {
            String userInput = userInputScanner.next().trim();
            if (userInput.equalsIgnoreCase("q")) {
                return -1; // Quit
            }
            try {
                return Integer.parseInt(userInput); // Only valid numbers will pass
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a valid number (or 'q' to quit): ");
            }
        }
    }


    /**
    * Inserts new items into the items array.
    *
    * @param items the array of existing items
    * @param lastItemId the last item ID used
    * @param noOfItems number of new items to insert
    * @return a new array with inserted items
    */
    public static int[][] insertItems(final int[][] items, final int lastItemId, final int noOfItems) {
        int itemId = lastItemId + 1;
        int[][] newItemsArray = items;

        if (checkFull(items, noOfItems)) {
            newItemsArray = extendArray(items, noOfItems);
        }

        for (int i = 0; i < noOfItems; i++) {
            int index = getIndex(newItemsArray, 0);
            newItemsArray[index][ITEM_ID] = itemId;
            newItemsArray[index][ITEM_COUNT] = getRandomNumberRange(MIN_ITEM_COUNT, MAX_ITEM_COUNT);
            newItemsArray[index][ITEM_PRICE] = getRandomNumberRange(MIN_ITEM_PRICE, MAX_ITEM_PRICE);
            itemId++;
        }
        return newItemsArray;
    }

    /**
    * Returns the index of an item with the specified item ID.
    *
    * @param items the array of items
    * @param itemID the item ID to search for
    * @return the index if found, otherwise -1
    */
    public static int getIndex(final int[][] items, final int itemID) {
        for (int i = 0; i < items.length; i++) {
            if (items[i][ITEM_ID] == itemID) {
                return i;
            }
        }
        return -1;
    }

    /**
    * Checks if the items array has fewer free slots than the number of items to insert.
    *
    * @param items the array of items
    * @param noOfItems the number of items to insert
    * @return true if extension is needed, false otherwise
    */
    public static boolean checkFull(final int[][] items, final int noOfItems) {
        int freeSlots = getFreeSlots(items);
        return freeSlots < noOfItems;
    }

    /**
    * Calculates the number of free slots in the array.
    *
    * @param items the array of items
    * @return number of free slots
    */
    public static int getFreeSlots(final int[][] items) {
        int freeSlots = 0;
        for (int i = 0; i < items.length; i++) {
            if (items[i][ITEM_ID] == 0) {
                freeSlots++;
            }
        }
        return freeSlots;
    }

    /**
    * Extends the items array to accommodate new items.
    *
    * @param items the original array
    * @param noOfItems number of items to insert
    * @return a new, larger array with copied data
    */
    public static int[][] extendArray(final int[][] items, final int noOfItems) {
        int additionalSize = noOfItems - getFreeSlots(items);
        int newSize = items.length + additionalSize;

        int[][] newArray = new int[newSize][ITEM_COLUMN_SIZE];
        for (int i = 0; i < items.length; i++) {
            System.arraycopy(items[i], 0, newArray[i], 0, ITEM_COLUMN_SIZE);
        }
        return newArray;
    }

    /**
    * Removes an item by setting its fields to 0.
    *
    * @param items the items array
    * @param itemId the item ID to remove
    * @return 0 if removed successfully, -1 if item not found
    */
    public static int removeItem(final int[][] items, final int itemId) {
        int index = getIndex(items, itemId);
        if (index != -1) {
            items[index][ITEM_ID] = 0;
            items[index][ITEM_COUNT] = 0;
            items[index][ITEM_PRICE] = 0;
            return 0;
        }
        return -1;
    }

    /**
    * Prints available items in the item array.
    *
    * @param items the array of items to be printed
    */
    public static void printItems(final int[][] items) {
        System.out.println("\n--- Available Items ---");
        for (int[] item : items) {
            if (item[ITEM_ID] != 0) {
                System.out.printf("Item ID: %d | Count: %d | Price: %d%n",
                        item[ITEM_ID], item[ITEM_COUNT], item[ITEM_PRICE]);
            }
        }
    }

    /**
    * Registers a sale of a specified item and quantity.
    * Updates item count, sales array and sale date.
    *
    * @param sales        is storing sale records
    * @param saleDates    is storing corresponding sale dates
    * @param items        the array of available items
    * @param itemIdToSell the ID of the item to sell
    * @param amountToSell the quantity to sell
    * @return 0 if the sale is successful,
    *  -1 if the item is not found,
    *   or the available quantity if not enough stock
    */
    public static int sellItem(final int[][] sales, final Date[] saleDates, final int[][] items, final int itemIdToSell, final int amountToSell) {
        int itemIndex = getIndex(items, itemIdToSell);
        int salesIndex = getIndex(sales, 0);

        if (itemIndex == -1) {
            return -1;
        }
        if (items[itemIndex][ITEM_COUNT] < amountToSell) {
            return items[itemIndex][ITEM_COUNT];
        }

        items[itemIndex][ITEM_COUNT] -= amountToSell;

        sales[salesIndex][SALE_ITEM_ID] = itemIdToSell;
        sales[salesIndex][SALE_ITEM_COUNT] = amountToSell;
        sales[salesIndex][SALE_ITEM_PRICE] = items[itemIndex][ITEM_PRICE] * amountToSell;
        saleDates[salesIndex] = new Date();

        return 0;
    }

    /**
    * Prints the entire sales history along with sale dates.
    * Only entries with SALE_ITEM_ID not equal to 0 are printed.
    *
    * @param sales      the array containing sales data
    * @param salesDate  the array containing corresponding sale dates
    */
    public static void printSales(final int[][] sales, final Date[] salesDate) {
        System.out.println("\n--- Sales History ---");
        for (int i = 0; i < sales.length; i++) {
            if (sales[i][SALE_ITEM_ID] != 0) {
                System.out.printf("Item ID: %d | Count: %d | Total Price: %d | Date: %s%n",
                        sales[i][SALE_ITEM_ID], sales[i][SALE_ITEM_COUNT],
                        sales[i][SALE_ITEM_PRICE], salesDate[i].toString());
            }
        }
    }

    /**
    * Sorts the sales array by SALE_ITEM_ID using bubble sort
    * and prints the sorted result along with corresponding sale dates.
    *
    * @param sales      the array of sales data to sort
    * @param salesDate  the corresponding dates of the sales
    */
    public static void sortedTable(final int[][]sales, final Date[] salesDate) {
        int[][] tempSales = new int[sales.length][SALE_COLUMN_SIZE];
        Date[] tempDates = new Date[sales.length];

        for (int i = 0; i < sales.length; i++) {
            System.arraycopy(sales[i], 0, tempSales[i], 0, SALE_COLUMN_SIZE);
            tempDates[i] = salesDate[i];
        }

        for (int i = 0; i < tempSales.length - 1; i++) {
            for (int j = 0; j < tempSales.length - 1 - i; j++) {
                if (tempSales[j][SALE_ITEM_ID] > tempSales[j + 1][SALE_ITEM_ID] && tempSales[j + 1][SALE_ITEM_ID] != 0) {
                    int[] temp = tempSales[j];
                    Date tempDate = tempDates[j];
                    tempSales[j] = tempSales[j + 1];
                    tempDates[j] = tempDates[j + 1];
                    tempSales[j + 1] = temp;
                    tempDates[j + 1] = tempDate;
                }
            }
        }
        printSales(tempSales, tempDates);
    }

   /**
   * Generates a random number between startRange and endRange (inclusive).
   *
   * @param startRange the lower bound of the range
   * @param endRange   the upper bound of the range
   * @return a random integer in the specified range
   */
    public static int getRandomNumberRange(final int startRange, final int endRange) {
        return (int) (Math.random() * (endRange - startRange + 1)) + startRange;
    }
}

import java.util.Scanner;
import java.util.Random;
/**
  * Simple library system for LTU
  * The program helps users to add, remove, loan and return books.
  * It validated ID of the books and dates
  * The format is YYYY-MM-DD (31 days/month)
  * Keeps track of loan history and counts late fees
  * @author Filippa Colbin
 **/

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String[] titles = new String[100];
        String[] isbns = new String[100];
        int[] ids = new int[100];
        String[] bookLoaners = new String[100];
        String[] loanDates = new String[100];
        String[] returnDates = new String[100];
        int[] loanCosts = new int[100];

        //Array for loan summary
        String[] loanersHistory = new String[100];
        String[] loanStartHistory = new String[100];

        int bookCount = 0;

        while (true) {
            System.out.println("----------------------------------");
            System.out.println("# LTU Library");
            System.out.println("----------------------------------");
            System.out.println("1. Add book");
            System.out.println("2. Remove book");
            System.out.println("3. Loan a book");
            System.out.println("4. Return a book");
            System.out.println("5. Print book list");
            System.out.println("6. Print lending summary");
            System.out.println("q. End program");
            System.out.print("> Enter your option: ");

            String option = sc.nextLine();

            switch (option) {
                case "1":
                    bookCount = addBook(titles, isbns, ids, bookCount, sc);
                    break;
                case "2":
                    bookCount = removeBook(titles, isbns, ids, bookLoaners, loanDates, bookCount, sc);
                    break;
                case "3":
                    loanBook(titles, ids, bookLoaners, loanDates, loanersHistory, loanStartHistory, bookCount, sc);
                    break;
                case "4":
                    returnBook(titles, isbns, ids, bookLoaners, loanDates, returnDates, loanCosts, bookCount, sc);
                    break;
                case "5":
                    printBookList(titles, isbns, ids, bookLoaners, bookCount);
                    break;
                case "6":
                    printLoanSummary(titles, ids, loanersHistory, loanStartHistory, returnDates, loanCosts, bookCount);
                    break;
                case "q":
                    System.out.println("Exiting...");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid menu item");
                    break;
            }
        }
    }

    //Method to add a book
    public static int addBook(String[] titles, String[] isbns, int[] ids, int bookCount, Scanner sc) {
        System.out.print("> Enter book title: ");
        String title = sc.nextLine();

        System.out.print("> Enter ISBN-10 code: ");
        String isbn = sc.nextLine();

        //Control for ISBN-format
        if (!isbn.matches("\\d{9}-\\d")) {
            System.out.println("Error: Invalid ISBN format.");
            return bookCount;
        }

        //Control if the IBSN already exists
        for (int i = 0; i < bookCount; i++) {
            if (isbns[i].equals(isbn)) {
                System.out.println("ISBN " + isbn + " already exists");
                return bookCount;
            }
        }

        //Genereate a new number between 1000-9999
        Random rand = new Random();
        int id;
        boolean exists;
        do {
            id = rand.nextInt(9000) + 1000;
            exists = false;

            for (int i = 0; i < bookCount; i++) {
                if (ids[i] == id) {
                    exists = true;
                }
            }

        } while (exists); //While-loop runs if the condition "exists" is true

        titles[bookCount] = title;
        isbns[bookCount] = isbn;
        ids[bookCount] = id;
        bookCount++;

        System.out.println("Book with title " + title + " was assigned ID " + id + " and added to the system.");
        return bookCount;
    }

    //Method to remove a book
    public static int removeBook(String[] titles, String[] isbns, int[] ids, String[] bookLoaners, String[] loanDates, int bookCount, Scanner sc) {
        System.out.print("> Enter book ID number: ");
        int idToRemove;

        try {
            idToRemove = sc.nextInt();
            sc.nextLine();
        } catch (Exception e) {
            System.out.println("Invalid ID format");
            sc.nextLine();
            return bookCount;
        }

        int index = -1;
        for (int i = 0; i < bookCount; i++) {
            if (ids[i] == idToRemove) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            System.out.println("Book with requested ID " + idToRemove + " does not exist.");
            return bookCount;
        }

        if (bookLoaners[index] != null) {
            System.out.println("Book with ID " + idToRemove + " is loaned out and cannot be removed.");
            return bookCount;
        }

        String removedTitle = titles[index];
        for (int i = index; i < bookCount - 1; i++) {
            titles[i] = titles[i + 1];
            isbns[i] = isbns[i + 1];
            ids[i] = ids[i + 1];
            bookLoaners[i] = bookLoaners[i + 1];
            loanDates[i] = loanDates[i + 1];
        }

        titles[bookCount - 1] = null;
        isbns[bookCount - 1] = null;
        ids[bookCount - 1] = 0;
        bookLoaners[bookCount - 1] = null;
        loanDates[bookCount - 1] = null;
        bookCount--;

        System.out.println("Book " + removedTitle + " was removed from the system.");
        return bookCount;
    }

    //Method to loan a book
    public static void loanBook(String[] titles, int[] ids, String[] bookLoaners, String[] loanDates,
                                String[] loanersHistory, String[] loanStartHistory, int bookCount, Scanner sc) {
        System.out.print("> Enter book ID number: ");
        int bookId;
        try {
            bookId = sc.nextInt();
            sc.nextLine();
        } catch (Exception e) {
            System.out.println("Invalid ID format");
            sc.nextLine();
            return;
        }

        //Find the book
        int index = -1;
        for (int i = 0; i < bookCount; i++) {
            if (ids[i] == bookId) {
                index = i;
                break;
            }
        }

        //Control if the book is not found
        if (index == -1) {
            System.out.println("Book with ID " + bookId + " not found.");
            return;
        }

        //Control if the book already is loaned by someone
        if (bookLoaners[index] != null) {
            System.out.println("Book " + titles[index] + " is already loaned by " + bookLoaners[index]);
            return;
        }

        System.out.print("> Enter lender's name: ");
        String lender = sc.nextLine();

        System.out.print("> Enter start date of the loan (YYYY-MM-DD): ");
        String loanDate = sc.nextLine();

        //Date-validatation: YYYY-MM-DD
        if (!isValidDate(loanDate)) {
            System.out.println("Invalid date");
            return;
        }

        //Save date and loaners name in variables
        bookLoaners[index] = lender;
        loanDates[index] = loanDate;

        // Save in arrays
        loanersHistory[index] = lender;
        loanStartHistory[index] = loanDate;

        System.out.println("Book " + titles[index] + " was loaned by " + lender + " on " + loanDate);
    }

    //Method to return a book
    public static void returnBook(String[] titles, String[] isbns, int[] ids, String[] bookLoaners,
                                  String[] loanDates, String[] returnDates, int[] loanCosts,
                                  int bookCount, Scanner sc) {

        System.out.print("> Enter book ID number: ");
        int bookId;
        try {
            bookId = sc.nextInt();
            sc.nextLine();
        } catch (Exception e) {
            System.out.println("Invalid ID format");
            sc.nextLine();
            return;
        }

        //For-loop to find the book
        int index = -1;
        for (int i = 0; i < bookCount; i++) {
            if (ids[i] == bookId) {
                index = i;
                break;
            }
        }

        //if-statement if the book is not found
        if (index == -1) {
            System.out.println("Book with ID " + bookId + " not found.");
            return;
        }

        //Get correct loaner
        String lender = bookLoaners[index];
        String loanDate = loanDates[index];

        System.out.print("> Enter return date: ");
        String returnDate = sc.nextLine();

        // Date-validation
        if (!isValidDate(returnDate)) {
            System.out.println("Invalid date");
            return;
        }

        // Count the number of days
        int start = startDay(loanDate);
        int end   = startDay(returnDate);
        int duration = end - start;

        //if-statement that controls if the days duration is longer than 10 days
        int cost = 0;
        if (duration > 10) { //if it is longer than 10 days
            cost = (duration - 10) * 15; //calculate number of days * 15 kr
        }

        System.out.println("RECEIPT LTU LIBRARY");
        System.out.println("Lender's name: " + lender);
        System.out.println("Book title: " + titles[index]);
        System.out.println("ISBN-10: " + isbns[index]);
        System.out.println("Period: " + loanDate + " - " + returnDate);
        System.out.println("Duration: " + duration + " days");
        System.out.println("Cost: " + cost + " kr");

        // Save date and cost for loan summary
        returnDates[index] = returnDate;
        loanCosts[index] = cost;

        //Shows that the book is available again
        bookLoaners[index] = null;
        loanDates[index] = null;
    }

    //Method that counts number of days with 31 days/month
    public static int startDay(String date) {
        String[] p = date.split("-");
        int y = Integer.parseInt(p[0]);
        int m = Integer.parseInt(p[1]);
        int d = Integer.parseInt(p[2]);
        return y * 372 + (m - 1) * 31 + (d - 1);
    }

    public static void printBookList(String[] titles, String[] isbns, int[] ids, String[] bookLoaners, int bookCount) {
        System.out.println("Book list LTU Library");
        System.out.printf("%-6s %-13s %-20s %s%n", "ID", "ISBN-10", "Title", "Status");

        for (int i = 0; i < bookCount; i++) {
            String status = (bookLoaners[i] != null) ? "Loaned" : "Available";
            System.out.printf("%-6d %-13s %-20s %s%n", ids[i], isbns[i], titles[i], status);
        }
    }

    public static void printLoanSummary(String[] titles, int[] ids, String[] loanersHistory, String[] loanStartHistory,
                                        String[] returnDates, int[] loanCosts, int bookCount) {

        System.out.println("Loan summary LTU Library");
        System.out.printf("%-6s %-20s %-12s %-12s %-6s%n", "ID", "Lender", "Start Date", "End Date", "Cost");

        int totalLoans = 0;
        int totalRevenue = 0;

        // For-loop through all books
        for (int i = 0; i < bookCount; i++) {
            //Check if book has ever been loaned
            if (loanStartHistory[i] != null) { //if-statement for existing or past loans
                String startDate = loanStartHistory[i];
                String endDate = (returnDates[i] != null) ? returnDates[i] : "N/A";
                int cost = loanCosts[i];

                //Print single loan information
                System.out.printf("%-6d %-20s %-12s %-12s %-6d%n", ids[i],
                        loanersHistory[i] != null ? loanersHistory[i] : "-",
                        startDate, endDate, cost);

                totalLoans++;
                totalRevenue += cost;
            }
        }
        System.out.println("Number of loans: " + totalLoans);
        System.out.println("Total revenue: " + totalRevenue + " kr");
    }

    //Method to validate date
    public static boolean isValidDate(String date) {
        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {

            return false;
        }

        String[] parts = date.split("-");
        int y = Integer.parseInt(parts[0]);
        int m = Integer.parseInt(parts[1]);
        int d = Integer.parseInt(parts[2]);

        if (m < 1 || m > 12) {
            return false;
        }

        if (d < 1 || d > 31) { //31 days in every month
            return false;
        }
        return true;
    }
}

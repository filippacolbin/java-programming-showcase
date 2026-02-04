import java.util.Scanner;

/**
 * Main class containing methods to calculate area, volume, and simplify fractions.
 * @author Filippa Colbin
 *
 */
public class Main {
    //Creation of scanner object.
    private static Scanner userInputScanner = new Scanner(System.in);

    //Constants
    static final int QUIT = -1;

    public static void injectInput(final Scanner inputScanner) {
        userInputScanner = inputScanner;
    }
    public static void main(final String[] args) {
        int radius = 0;
        int height = 0;
        int numerator = 0;
        int denominator = 0;

        //Print the header of the program for area and volume.
        System.out.println("----------------------------------");
        System.out.println("# Test of area and volume methods");
        System.out.println("----------------------------------");

        // While loop that runs until user enters "q" for area and volume.
        while (true) {

            radius = input();
            if (radius == QUIT) {
                break;
            }

            height = input();
            if (height == QUIT) {
                break;
            }

            System.out.println("r = " + radius + ", h = " + height);
            System.out.printf("Circle area: %.2f %n", area(radius));
            System.out.printf("Cone area: %.2f %n", area(radius, height));
            System.out.printf("Cone volume: %.2f %n", volume(radius, height));
        }

        //Print the header of the program for area and volume.
        System.out.println("----------------------------------");
        System.out.println("# Test of the fractional methods");
        System.out.println("----------------------------------");

        // While loop that runs until user enters "q" for the fraction part
        while (true) {
            numerator = input();
            if (numerator == QUIT) {
                break;
            }
            denominator = input();
            if (denominator == QUIT) {
                break;
            }
            System.out.printf("%d/%d = ", numerator, denominator);
            printFraction(fraction(numerator, denominator));
        }
    }

    /**
    * Method that calculates the area of a circle.
    * @param radius the radius of the circle
    * @return the area of the circle
    */
    public static double area(final int radius) {
        return Math.PI * Math.pow(radius, 2);
    }

    /**
    * Method that calculates the lateral surface area of a cone.
    * @param radius the radius of the base
    * @param height the height of the cone
    * @return the lateral surface area
    */
    public static double area(final int radius, final int height) {
        double slantHeight = pythagoras(radius, height);
        return Math.PI * radius * slantHeight;
    }

    /**
    * Method that calculates the hypotenuse using Pythagoras' theorem.
    * @param sideA one leg of the right triangle
    * @param sideB the other leg of the right triangle
    * @return the length of the hypotenuse
    */
    public static double pythagoras(final int sideA, final int sideB) {
        return Math.sqrt(Math.pow(sideA, 2) + Math.pow(sideB, 2));
    }


    /**
    * Method that calculates the volume of a cone.
    * @param radius the radius of the base
    * @param height the height of the cone
    * @return the volume of the cone
    */
    public static double volume(final int radius, final int height) {
        return (Math.PI * Math.pow(radius, 2) * height) / 3;
    }

    /**
    * Method that simplifies a fraction and returns the result as an array.
    * @param numerator the numerator
    * @param denominator the denominator
    * @return an array containing [whole number, numerator, denominator] or null if invalid
    */
    public static int[] fraction(final int numerator, final int denominator) {
        if (denominator == 0) {
            return null;
        }
        if (numerator == 0) {
            return new int[]{0, 0, 0};
        }
        int integerPart = numerator / denominator;
        int fractionNumerator = numerator % denominator;
        int fractionDenominator = denominator;

        if (fractionNumerator != 0) {
            int gcdValue = gcd(fractionNumerator, fractionDenominator);
            fractionNumerator /= gcdValue;
            fractionDenominator /= gcdValue;
        }

        return new int[]{integerPart, fractionNumerator, fractionDenominator};
    }

    /**
    * Method that calculates GCD using the Euclidean algorithm.
    * @param a the first number
    * @param b the second number
    * @return the GCD of the two numbers
    */
    public static int gcd(final int a, final int b) {
        int x = Math.abs(a);
        int y = Math.abs(b);
        while (y != 0) {
            int temp = x % y;
            x = y;
            y = temp;
        }
        return x;
    }

    /**
    * Method that prints a fraction in a readable format.
    * @param parts an array representing the fraction in the format [whole number, numerator, denominator]
    */
    public static void printFraction(final int[] parts) {
        if (parts == null) {
            System.out.println("Error");
        } else if (parts[0] == 0 && parts[1] == 0) {
            System.out.println("0");
        } else if (parts[1] == 0) {
            System.out.println(parts[0]);
        } else if (parts[0] == 0) {
            System.out.println(parts[1] + "/" + parts[2]);
        } else {
            System.out.println(parts[0] + " " + parts[1] + "/" + parts[2]);
        }
    }

    /**
    * Method that handles users input.
    * @return the absolute value of an entered integer, or -1 if the user enters "q" to quit
    */
    public static int input() {
        while (true) {
            if (userInputScanner.hasNextInt()) {
                int value = userInputScanner.nextInt();
                return Math.abs(value);
            } else if (userInputScanner.hasNext("q")) {
                userInputScanner.next();
                return QUIT;
            } else {
                userInputScanner.next(); // Ignore invalid input
            }
        }
    }
}

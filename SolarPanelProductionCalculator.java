/**
*
* 1. Declare constants
* 2. Declare variables
* 3. Ask user for month and date and validate
* 4. Ask user for sunrise and validate
* 5. Ask user for sunset and validate
* 6. Calculate the combined hours for both sunrise and sunset and validate
* 7. Calculate production
* 8. Calculate earning
* 9. Print it
*
* @author Filippa Colbin
*/
public class Main {
  /* The two main math equations are
  1) production (in kWh) = SOLAR_RADIATION * EFFICIENCY * PANEL_AREA * hours * NUM_OF_PANELS / 1000; //Note the equation in instruction is in Wh, so we divide by 1000 to get kWh as price is per kWh
  2) value = production * ELECTRIC_PRICE
  where,
  NUM_OF_PANELS = 26;
  PANEL_HEIGHT = 1; //Units is meters, so no conversion is needed as SOLAR_RADIATION is per m^2
  SOLAR_RADIATION = 166; //this is in Wh units, hence divide by 1000 in eq 1 above to convert to kWh
  PANEL_WIDTH = 1.7; //Units is meters, so no conversion is needed as SOLAR_RADIATION is per m^2
  PANEL_AREA = PANEL_WIDTH * PANEL_HEIGHT;
  EFFICIENCY = 0.2; //(instruction says 20 percent efficiency, so, 20/100 = 0.2)
  ELECTRIC_PRICE = 0.9; //this is per kWh, so we calculate production in kWh in eq 1
  DAYS_IN_JUNE_MAX = 30;
  */

  //step 1: Declare Constants
    static final int NUM_OF_PANELS = 26;
    static final int PANEL_HEIGHT = 1;
    static final double PANEL_WIDTH = 1.7;
    static final int SOLAR_RADIATION = 166;
    static final double ELECTRIC_PRICE = 0.9;
    static final double EFFICIENCY = 0.2;

    static final int JUNE = 6;
    static final int JULY = 7;
    static final int DAYS_IN_JUNE_MAX = 30;
    static final int DAYS_IN_JULY_MAX = 31;
    static final int MAX_HOURS = 23;
    static final int MIN_HOURS = 0;
    static final int MAX_MINUTES = 59;
    static final int MIN_MINUTES = 0;

    static final int CONVERT_TO_KW = 1000;
    static final double CONVERT_TO_HOURS = 60.0;
    static final double PANEL_AREA = PANEL_WIDTH * PANEL_HEIGHT;

    static final String INVALID_DATE_FORMAT = "Error! Invalid date format.";
    static final String INVALID_MONTH = "Error! Invalid month. Month must be 06 or 07.";
    static final String INVALID_DAY = "Error! Invalid day.";

    static final String INVALID_TIME_FORMAT = "Error! Invalid time format.";
    static final String INVALID_HOURS = "Error! Invalid time. Hours must be between 0 and 23.";
    static final String INVALID_MINUTES = "Error! Invalid time. Minutes must be between 0 and 59.";

    static final String SURISE_GT_SUNSET = "Error! Sunrise must be before sunset.";

    public static void main(final String[] args) {

  //Step 2: Variables
  //hour, minutes, month, day

        int month = 0;
        int day = 0;

        int hours = 0;
        int minutes = 0;

        double finalSunriseHours = 0.0;
        double finalSunsetHours = 0.0;
        double totalSolarHours = 0.0;

        double electricProduction = 0.0;
        double earning = 0.0;

        Scanner userInput = new Scanner(System.in);

  //Step 3: Ask user for month and date
  //Use the correct delimiter
        userInput.useDelimiter("[-|\\s]+");
        System.out.print("Enter todays's date [mm-dd]>");

        if (userInput.hasNextInt()) {
            month = userInput.nextInt();
        } else {
            System.out.println(INVALID_DATE_FORMAT);
            System.exit(0);
        }

        if (userInput.hasNextInt()) {
            day = userInput.nextInt();
        } else {
            System.out.println(INVALID_DATE_FORMAT);
            System.exit(0);
        }

    //Validate (month and day)
    //Validate if month is june or july
    //Validate if the month is June, then day myst be between 1 and 30 (both included)
    //Validate if the month is Juny, then day must be between 1 and 31 (both included)

        if (month == JUNE) {
            if (day > DAYS_IN_JUNE_MAX) {
                System.out.println(INVALID_DAY + "Day in june must be between 1 and 30");
                System.exit(0);
            }
        } else if (month == JULY) {
            if (day > DAYS_IN_JULY_MAX) {
                System.out.println(INVALID_DAY + "Day in July must be between 1 and 31");
                System.exit(0);
            }
        } else {
            System.out.println(INVALID_MONTH);
            System.exit(0);
        }

        if (day < 1) {
            System.out.println(INVALID_DAY + "Day cannot be less than 1");
            System.exit(0);
        }

      //Step 4: Ask user for sunrise
      //Use the correct delimiter
      //Validate hours and miniutes
      //Validate that hours are between 00 and 23 (both included)
      //Validate that mintues are between 00 and 59 (both included)
        userInput.useDelimiter("[:|\\s]+");
        System.out.print("Enter the time of sunrise [hh:mm]>");

        if (userInput.hasNextInt()) {
            hours = userInput.nextInt();
        } else {
            System.out.println(INVALID_TIME_FORMAT);
            System.exit(0);
        }

        if (userInput.hasNextInt()) {
            minutes = userInput.nextInt();
        } else {
            System.out.println(INVALID_TIME_FORMAT);
            System.exit(0);
        }

        if (hours < 0) {
            System.out.println(INVALID_HOURS);
            System.exit(0);
        } else if (hours > MAX_HOURS) {
            System.out.println(INVALID_HOURS);
            System.exit(0);
        }

        if (minutes < 0) {
            System.out.println(INVALID_MINUTES);
            System.exit(0);
        } else if (minutes > MAX_MINUTES) {
            System.out.println(INVALID_MINUTES);
            System.exit(0);
        }

        finalSunriseHours = hours + (minutes / (double) CONVERT_TO_HOURS);

      //Step 5: same as step 4, but for sunset
        System.out.print("Enter the time of sunset of [hh:mm]>");

        if (userInput.hasNextInt()) {
            hours = userInput.nextInt();
        } else {
            System.out.println(INVALID_TIME_FORMAT);
            System.exit(0);
        }

        if (userInput.hasNextInt()) {
            minutes = userInput.nextInt();
        } else {
            System.out.println(INVALID_TIME_FORMAT);
            System.exit(0);
        }

        if (hours < 0) {
            System.out.println(INVALID_HOURS);
            System.exit(0);
        } else if (hours > MAX_HOURS) {
            System.out.println(INVALID_HOURS);
            System.exit(0);
        }

        if (minutes < 0) {
            System.out.println(INVALID_MINUTES);
            System.exit(0);
        } else if (hours > MAX_MINUTES) {
            System.out.println(INVALID_MINUTES);
            System.exit(0);
        }
        finalSunsetHours = hours + (minutes / (double) CONVERT_TO_HOURS);

      //Step 6:
      //Validate that sunset is after sunrise
      //Calculate the combined hours for both sunrise and sunset
      //Validate
        if (finalSunriseHours > finalSunsetHours) {
            System.out.println(SURISE_GT_SUNSET);
            System.exit(0);
        }

        totalSolarHours = finalSunsetHours - finalSunriseHours;

      //Step 7: Calcualte production, eq 1 above
        electricProduction = (SOLAR_RADIATION * EFFICIENCY * PANEL_AREA * totalSolarHours * NUM_OF_PANELS) / CONVERT_TO_KW;

      //Step 8: Calculate money, eq 2 above
        earning = electricProduction * ELECTRIC_PRICE;

      //Step 9: Print it
        System.out.println("============================================================");
        System.out.printf("Sun hours: %.2f hours\n", totalSolarHours);
        System.out.printf("The production on " + day + "/" + month + " is: %.2f kWh to a value of: SEK %.2f ", electricProduction, earning);
        userInput.close();
    }
}

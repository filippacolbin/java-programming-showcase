/**
* Electric Car Charging Time Calculator
* Calculates the charging time for an electric car with a 35.8 kWh battery
*
* Declare constants and varibles
* Calculate charging power 5 times
* Round the charging power to two decimals 5 times
* Calculate charging time 5 times
* Round the charging time to two decimals 5 times
* Print table
*
* @author Filippa Colbin
* @version 1.0
*/
public class Main {

    static final double BATTERY_CAPACITY = 35.8;
    static final double W_TO_KW = 1000.0;
    static final double AMPERE_10 = 10.0;
    static final double AMPERE_16 = 16.0;
    static final double AMPERE_32 = 32.0;
    static final double VOLTAGE_230 = 230.0;
    static final double VOLTAGE_400 = 400.0;
    static final int SCALE_FACTOR = 2;
    static final int BASE_10 = 10;

    public static void main(final String[] args) {
        final int base10 = 10;
        double chargingPower = 0;
        double chargingTime = 0;

        //3.142356 -> 3.14
        int noOfDecimals = SCALE_FACTOR;
        double scale = 0;
        scale = Math.pow(BASE_10, noOfDecimals);

        System.out.printf("Battery: %.2f (kwh)%n", BATTERY_CAPACITY);
        System.out.printf("%-15s %-15s %-19s %-15s%n", "Current(A)", "Voltage(V)", "Charging Power(kW)", "Charging Time(h)");

        chargingPower = (AMPERE_10 * VOLTAGE_230) / W_TO_KW;
        chargingPower = Math.round(chargingPower * scale) / scale;
        chargingTime = BATTERY_CAPACITY / chargingPower;
        chargingTime = Math.round(chargingTime * scale) / scale;
        System.out.printf("%-15.2f %-15.2f %-19.1f %-15.2f %n", AMPERE_10, VOLTAGE_230, chargingPower, chargingTime);

        chargingPower = (AMPERE_16 * VOLTAGE_230) / W_TO_KW;
        chargingPower = Math.round(chargingPower * scale) / scale;
        chargingTime = BATTERY_CAPACITY / chargingPower;
        chargingTime = Math.round(chargingTime * scale) / scale;
        System.out.printf("%-15.2f %-15.2f %-19.2f %-15.2f %n", AMPERE_16, VOLTAGE_230, chargingPower, chargingTime);

        chargingPower = (AMPERE_10 * VOLTAGE_400 * Math.sqrt(3)) / W_TO_KW;
        chargingPower = Math.round(chargingPower * scale) / scale;
        chargingTime = BATTERY_CAPACITY / chargingPower;
        chargingTime = Math.round(chargingTime * scale) / scale;
        System.out.printf("%-15.2f %-15.2f %-19.2f %-15.2f %n", AMPERE_10, VOLTAGE_400, chargingPower, chargingTime);

        chargingPower = (AMPERE_16 * VOLTAGE_400 * Math.sqrt(3)) / W_TO_KW;
        chargingPower = Math.round(chargingPower * scale) / scale;
        chargingTime = BATTERY_CAPACITY / chargingPower;
        chargingTime = Math.round(chargingTime * scale) / scale;
        System.out.printf("%-15.2f %-15.2f %-19.2f %-15.2f %n", AMPERE_16, VOLTAGE_400, chargingPower, chargingTime);

        chargingPower = (AMPERE_32 * VOLTAGE_400 * Math.sqrt(3)) / W_TO_KW;
        chargingPower = Math.round(chargingPower * scale) / scale;
        chargingTime = BATTERY_CAPACITY / chargingPower;
        chargingTime = Math.round(chargingTime * scale) / scale;
        System.out.printf("%-15.2f %-15.2f %-19.2f %-15.2f %n", AMPERE_32, VOLTAGE_400, chargingPower, chargingTime);
    }
}


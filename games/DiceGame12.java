import java.util.Scanner;
import java.util.Random;

/**
 * The program is a game where the player will roll 3 dice to get a total sum of 12 in order to win.
 * @author Filippa Colbin
 */
class Main {
    static final String GAME_START = "Welcome to dice game 12. You must roll 1-3 dice and try to get the sum of 12 ...\n";
    static final String CHOOSE_DICE = "Enter which dice you want to roll [1,2,3] (exit with q):";
    static final String ROUND_WON = "You won!!";
    static final String ROUND_LOST = "You lost!!";
    static final String ROUND_TIE = "You neither won nor lost the round.";
    static final String NEXT_ROUND = "Next round!";
    static final String GAME_OVER = "Game Over!";
    static final String ALREADY_SELECTED_DICE = "Sorry, you have already rolled that dice. Try again";
    static final String INVALID_ENTRY = "Sorry, that is an invalid entry. Try again. Valid entries are 1, 2, 3, and q\n";
    static final String AMOUNT_WIN_STRING = "#win: ";
    static final String AMOUNT_LOST_STRING = " #loss: ";
    static final String SUM_STRING = " sum: ";
    static final int MAX_DICE_VALUE = 6;
    static final int MIN_DICE_VALUE = 1;
    static final int DICE_SUM_TARGET_VALUE = 12;

    public static void main(final String[] args) {

        int dice1Value = 0;
        boolean dice1Rolled = false; // Is dice 1 rolled?

        int dice2Value = 0;
        boolean dice2Rolled = false; // Is dice 2 rolled?

        int dice3Value = 0;
        boolean dice3Rolled = false; // Is dice 3 rolled?

        int sum = 0;
        int winCount = 0;
        int lossCount = 0;

        int currentDiceRolled = 0;
        int noOfDiceRolls = 0;

        boolean isGameInPlay = true; // Whether the game is being played or not

        Scanner userInput = new Scanner(System.in);
        Random rand = new Random();
        // int randomNumber = rand.nextInt(6) + 1; // 1-6

        System.out.println(GAME_START);

        while (isGameInPlay) {
            System.out.println(CHOOSE_DICE);

            if (userInput.hasNextInt()) {
                currentDiceRolled = userInput.nextInt();

                if (currentDiceRolled < 1 || currentDiceRolled > 3) {
                    System.out.println(INVALID_ENTRY);
                    continue;
                }

            } else {
                String userString = userInput.next();

                if (userString.equalsIgnoreCase("q")) {
                    System.out.printf("%d %d %d sum: %d #win: %d #loss: %d%n", dice1Value, dice2Value, dice3Value, sum, winCount, lossCount);
                    System.out.println(GAME_OVER);
                    isGameInPlay = false;
                    break;
                } else {
                    System.out.println(INVALID_ENTRY);
                    continue;
                }
            }

            System.out.printf("The user rolled %d%n", currentDiceRolled);

            if (currentDiceRolled == 1) {
                if (dice1Rolled) {
                    System.out.println(ALREADY_SELECTED_DICE);
                } else {
                    dice1Value = rand.nextInt(MAX_DICE_VALUE) + 1;
                    dice1Rolled = true;
                    noOfDiceRolls++;
                    sum = sum + dice1Value;
                }
            } else if (currentDiceRolled == 2) {
                if (dice2Rolled) {
                    System.out.println(ALREADY_SELECTED_DICE);
                } else {
                    dice2Value = rand.nextInt(MAX_DICE_VALUE) + 1;
                    dice2Rolled = true;
                    noOfDiceRolls++;
                    sum = sum + dice2Value;
                }
            } else if (currentDiceRolled == 3) {
                if (dice3Rolled) {
                    System.out.println(ALREADY_SELECTED_DICE);
                } else {
                    dice3Value = rand.nextInt(MAX_DICE_VALUE) + 1;
                    dice3Rolled = true;
                    noOfDiceRolls++;
                    sum = sum + dice3Value;
                }
            }
            System.out.printf("%d %d %d sum: %d #win: %d #loss: %d%n", dice1Value, dice2Value, dice3Value, sum, winCount, lossCount);

            if (noOfDiceRolls == 3) {
                if (sum == DICE_SUM_TARGET_VALUE) {
                    System.out.println(ROUND_WON);
                    winCount++;
                } else if (sum > DICE_SUM_TARGET_VALUE) {
                    System.out.println(ROUND_LOST);
                    lossCount++;
                } else {
                    System.out.println(ROUND_TIE);
                }

                sum = 0;
                noOfDiceRolls = 0;
                dice1Rolled = false;
                dice2Rolled = false;
                dice3Rolled = false;
                System.out.println(NEXT_ROUND);
            }
        }
        userInput.close();
    }
}

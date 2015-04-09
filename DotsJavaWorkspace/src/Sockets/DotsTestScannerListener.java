package Sockets;

import Constants.DotsConstants;
import Dots.DotsPoint;
import Model.Interaction.DotsInteraction;
import Model.Interaction.DotsInteractionStates;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Thread to listen for input from the scanner
 * Scanner is used for testing, start this thread
 *
 * Created by JiaHao on 1/4/15.
 */
public class DotsTestScannerListener implements Runnable {

    private final DotsServerClientParent dotsServerClientParent;
    private final int playerNo;

    // Set manualtest to false to trigger automated testing
    private final boolean manualTest;


    public DotsTestScannerListener(DotsServerClientParent dotsServerClientParent, int playerNo, boolean manualTest) {
        this.dotsServerClientParent = dotsServerClientParent;
        this.playerNo = playerNo;
        this.manualTest = manualTest;
    }

    @Override
    public void run() {

        Scanner scanner = new Scanner(System.in);

        boolean gameIsRunning = true;
        int counter = 0;

        Random random = new Random();
        while (gameIsRunning) {

            // Whenever we do an interaction on the screen, we send a message to the server and get a response
            // of the move's validity
            DotsInteraction dotsInteraction;

            // TODO refactor this nicely to separate functions for automated and manual tests
            if (this.manualTest) {
                dotsInteraction = getInteractionFromScanner(this.playerNo, scanner);

            } else {

                // Automated testing basically randomly sleeps and randomly creates a dotsInteraction
                try {
                    Thread.sleep(random.nextInt(100));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                DotsPoint selectedDotsPoint = new DotsPoint(random.nextInt(DotsConstants.BOARD_SIZE), random.nextInt(DotsConstants.BOARD_SIZE));
                dotsInteraction = new DotsInteraction(this.playerNo, DotsInteractionStates.randomState(), selectedDotsPoint);

            }

            try {
                this.dotsServerClientParent.doInteraction(dotsInteraction);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            counter++;
            if (!this.manualTest) {
                if (counter == 100) {
                    break;
                }
            }

        }

        try {
            // if its the client, this method will do nothing, as client does not override this message
            this.dotsServerClientParent.updateBoard();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Problem here: if this thread for the scanner ends before the same thread on the server, the server will block
        // and will be unable reach the final part of the code where it sends the final board message to the client
        // NO IDEA WHY
        scanner.close();
        System.out.println("Scanner has been stopped");
    }


    /**
     * Nice method to prompt user entry through a scanner and returns a dotsInteraction
     * @param player player number
     * @param scanner scanner object
     * @return parsed result
     */
    public static DotsInteraction getInteractionFromScanner(int player, Scanner scanner) {

        // change largest number here to match size of board - 1
        final String REG_EX = "[0-5][0-5][0-2]";

        String playerPointString = "";
        boolean correctInput = false;
        while (!correctInput) {

            System.out.println("Enter move x, y, state without spaces:");

            playerPointString = scanner.nextLine();

            if (!(correctInput = Pattern.matches(REG_EX, playerPointString))) {
                System.out.println("Invalid entry.");
            }
        }

        return parseStringToInteraction(player, playerPointString);


    }

    /**
     * This parses a string into a DotsInteraction
     */
    private static DotsInteraction parseStringToInteraction(int player, String inp) {

        final int NO_OF_PARAMETERS = 3;
        int[] pointParameters = new int[NO_OF_PARAMETERS];

        for (int i = 0; i < 3; i++) {
            char currentChar = inp.charAt(i);
            pointParameters[i] = Character.getNumericValue(currentChar);
        }

        DotsPoint dotsPoint = new DotsPoint(pointParameters[0], pointParameters[1]);

        DotsInteraction interaction = new DotsInteraction(player, DotsInteractionStates.values()[pointParameters[2]], dotsPoint);

        return interaction;

    }

    // testing code for interaction scanner parsing
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);


        while (true) {

            DotsInteraction interaction = getInteractionFromScanner(0, scanner);

            System.out.println(interaction);

        }

    }

}

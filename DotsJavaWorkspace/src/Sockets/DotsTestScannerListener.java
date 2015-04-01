package Sockets;

import Model.DotsInteraction;
import Model.DotsLocks;

import java.io.IOException;
import java.util.Scanner;

/**
 * Thread to listen for input from the scanner
 * Scanner is used for testing, start this thread
 */
/**
 * Created by JiaHao on 1/4/15.
 */
public class DotsTestScannerListener implements Runnable {

    private final DotsServerClientParent dotsServerClientParent;

    public DotsTestScannerListener(DotsServerClientParent dotsServerClientParent) {
        this.dotsServerClientParent = dotsServerClientParent;
    }

    @Override
    public void run() {

        Scanner scanner = new Scanner(System.in);

        boolean gameIsRunning = true;

        while (gameIsRunning) {

            // Whenever we do an interaction on the screen, we send a message to the server and get a response
            // of the move's validity

            final int PLAYER_NO = 1;

            DotsInteraction dotsInteraction = DotsSocketHelper.getInteractionFromScanner(PLAYER_NO, scanner);

            try {
                this.dotsServerClientParent.doInteraction(dotsInteraction);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        scanner.close();
        System.out.println("Scanner has been stopped");
    }
}

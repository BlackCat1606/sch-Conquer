package Sockets;

import AwesomeSockets.AwesomeServerSocket;
import Dots.*;
import Model.*;

import java.io.IOException;
import java.util.Scanner;

/**
* Created by JiaHao on 25/2/15.
*/
public class DotsServer {

    // static variable for threads to keep track of whether the board has changed

//    public static boolean boardChanged = true;
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        final int PORT = 4321;

        // initialize server
        AwesomeServerSocket server = new AwesomeServerSocket(PORT);
//        server.debugMode = false;
        server.acceptClient();

        // initialize game object
        DotsGame dotsGame = new DotsGame();
        DotsLocks dotsLocks = dotsGame.getGameLocks();

//        // initial printing of board to client
//        DotsSocketHelper.sendBoardToClient(server, dotsGame.getBoardArray());



        // init listener thread
        DotsServerClientListener dotsClientListener = new DotsServerClientListener(server, dotsGame);
        Thread listenerThread = new Thread(dotsClientListener);



        // init scanner thread
        Scanner scanner = new Scanner(System.in);
        DotsServerScannerListener dotsScannerListener = new DotsServerScannerListener(dotsGame, scanner);
        Thread scannerThread = new Thread(dotsScannerListener);

        // starts threads
        listenerThread.start();
        scannerThread.start();




        // block for game sequence
        while (dotsGame.isGameRunning()) {

            // we acquire a lock and wait upon the game locks here.
            synchronized (dotsLocks) {

                while (!dotsLocks.isBoardChanged()) {

                    try {
                        dotsLocks.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


                // when we are notified, we will do board updating,
                doBoardUpdating(server, dotsGame);

                // and reset the boardchanged variable in dotsLocks to false.
                dotsLocks.setBoardChanged(false);

            }

        }

        // closes server and clients when game over
        server.closeServer();

        System.out.println("Game over");
    }


    private static void doBoardUpdating(AwesomeServerSocket serverSocket, DotsGame dotsGame) throws IOException {

        // update the board on the current device
        // TODO change this to a method to update the screen when on android
        dotsGame.getDotsBoard().printWithIndex();


        // update the board on the remote device
        sendBoardToClient(serverSocket, dotsGame);

    }

    private static void sendBoardToClient(AwesomeServerSocket serverSocket, DotsGame dotsGame) throws IOException {

        DotsMessageBoard messageBoard = new DotsMessageBoard(dotsGame.getDotsBoard());
        DotsSocketHelper.sendMessageToClient(serverSocket, messageBoard);

    }

}


class DotsServerScannerListener implements Runnable {

    private final Scanner scanner;
    private final DotsGame dotsGame;

    public DotsServerScannerListener(DotsGame dotsGame, Scanner scanner) {
        this.scanner = scanner;
        this.dotsGame = dotsGame;
    }

    @Override
    public void run() {

        while (this.dotsGame.isGameRunning()) {

//            Point inputPoint = DotsSocketHelper.getPointFromScanner(this.scanner);


            DotsInteraction dotsInteraction = DotsSocketHelper.getInteractionFromScanner(0, this.scanner);

            boolean result = this.dotsGame.doMove(dotsInteraction);


            if (result) {

                // update the android screen with the corresponding method

                /**
                 * There are two types of game screen updates:
                 * 1. Update to show user touches when touch down and dragged
                 * 2. Update the entire board when elements are cleared
                 *
                 * In this function, we only update the first case, which is to show valid touches
                 * on user interaction
                 *
                 * The second case is dealt directly in the dotsGame object itself, which will notify the locks
                 * when the board needs to be updated, and will hence trigger the main thread where doBoardUpdating()
                 * will be triggered.
                 *
                 */



            }




        }

        System.out.println("Game over!");
    }
}

class DotsServerClientListener implements Runnable {

    private final AwesomeServerSocket serverSocket;

    private final DotsGame dotsGame;

    public DotsServerClientListener(AwesomeServerSocket serverSocket, DotsGame dotsGame) {
        this.serverSocket = serverSocket;
        this.dotsGame = dotsGame;

    }

    @Override
    public void run() {

        if (this.dotsGame == null) {
            throw new NullPointerException();
        }

        try {

            while (this.dotsGame.isGameRunning()) {


                DotsMessage message = DotsSocketHelper.readMessageFromClient(serverSocket);

                this.dealWithMessage(message);


            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("Game over!");
    }


    /**
     * This method deals with messages sent from the client.
     * @param message
     * @throws IOException
     */
    private void dealWithMessage(DotsMessage message) throws IOException {


        // Right now, only one case of the message, which is an interaction.
        // The response here would be to respond with a boolean indicating the validity of the interaction
        if (message instanceof DotsMessageInteraction) {

            // process the interaction
            DotsInteraction receivedInteraction = ((DotsMessageInteraction) message).getDotsInteraction();
            boolean response = this.dotsGame.doMove(receivedInteraction);

            // Package and send a response to the client
            DotsMessageResponse dotsMessageResponse = new DotsMessageResponse(response);
            DotsSocketHelper.sendMessageToClient(this.serverSocket, dotsMessageResponse);

        } else {

            System.err.println("Invalid message type");

        }



    }


}

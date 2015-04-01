package Sockets;

import AwesomeSockets.AwesomeServerSocket;
import Constants.DotsConstants;
import Dots.*;
import AndroidCallback.DotsAndroidCallback;
import Model.*;

import java.io.IOException;

/**
 *
 * Primary object to be run by the server for the game
 * Created by JiaHao on 25/2/15.
 */
public class DotsServer extends DotsServerClientParent{

    private final int port;

    private DotsGame dotsGame;
    private DotsLocks dotsLocks;
    private AwesomeServerSocket serverSocket;

    public DotsServer(int port) {
        this.port = port;
    }

    public void start() throws IOException, InstantiationException, InterruptedException {

        super.start();
        // initialize server
        this.serverSocket = new AwesomeServerSocket(port);
        this.serverSocket.acceptClient();

        // initialize game object
        this.dotsGame = new DotsGame();
        this.dotsLocks = dotsGame.getGameLocks();

        // init listener thread
        DotsServerClientListener dotsClientListener = new DotsServerClientListener(this.serverSocket, this.dotsGame);
        Thread listenerThread = new Thread(dotsClientListener);

        // starts thread to listen for messages
        listenerThread.start();

        // First time run to display the board on the client and server
        this.updateBoard();

        // todo closes server and clients when game over


        System.out.println("Waiting for interactions");
    }

    @Override
    public void doInteraction(DotsInteraction dotsInteraction) throws IOException, InterruptedException {

        boolean result = this.dotsGame.doMove(dotsInteraction);

        // only proceed if its a valid move
        if (result) {

            /**
             * There are two types of game screen updates:
             * 1. Update to show user touches when touch down and dragged
             * 2. Update the entire board when elements are cleared
             */

            // First we deal with the first case where we show interactions on the screen

            updateScreenForTouchInteractions(dotsInteraction);

            // Sends the valid interaction to the client player so that the client can see the move made by the other player
            DotsMessageInteraction interactionMessage = new DotsMessageInteraction(dotsInteraction);
            DotsSocketHelper.sendMessageToClient(this.serverSocket, interactionMessage);


            // finally, we check if the board needs to be updated
            // dotsGame will automatically update this variable if the board is changed
            if (this.dotsLocks.isBoardChanged()) {

                this.updateBoard();

            }
        }
    }

    /**
     * Updates the board, by doing the callback, and sending the board to the client
     * @throws IOException
     */
    private void updateBoard() throws IOException {

        // update the board on the current device
        DotsBoard updatedBoard = dotsGame.getDotsBoard();
        updatedBoard.printWithIndex();

        this.getAndroidCallback().onBoardChanged(updatedBoard);

        // update the board on the remote device
        DotsMessageBoard messageBoard = new DotsMessageBoard(dotsGame.getDotsBoard());
        DotsSocketHelper.sendMessageToClient(this.serverSocket, messageBoard);

        this.dotsLocks.setBoardChanged(false);

    }

    /**
     * Method here to update the screen for touch interaction, i.e. reflect touches
     * @param dotsInteraction interaction object
     */
    private void updateScreenForTouchInteractions(DotsInteraction dotsInteraction) {

        // debug method to print valid interaction
        System.out.println("DRAW on screen touch interaction: " + dotsInteraction.toString());

        this.getAndroidCallback().onValidPlayerInteraction(dotsInteraction);
    }

//    /**
//     * This function is called when the main thread is notified and awakened, indicating that
//     * the board has been changed and the screen needs updating
//     * @param serverSocket server to send the board to
//     * @param dotsGame the game object
//     * @throws IOException if sending message through the socket fails
//     */
//    private void doBoardUpdating(AwesomeServerSocket serverSocket, DotsGame dotsGame) throws IOException {
//
//        // update the board on the current device
//        DotsBoard updatedBoard = dotsGame.getDotsBoard();
//
//        updatedBoard.printWithIndex();
//
//        this.getBoardViewListener().onBoardUpdate(updatedBoard);
//
//        // update the board on the remote device
//        DotsMessageBoard messageBoard = new DotsMessageBoard(dotsGame.getDotsBoard());
//        DotsSocketHelper.sendMessageToClient(serverSocket, messageBoard);
//
//    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException, InstantiationException {

        // Initialise the server
        DotsServer dotsServer = new DotsServer(DotsConstants.CLIENT_PORT);

        // Compulsory to set listeners
        dotsServer.setAndroidCallback(new DotsAndroidCallback() {
            @Override
            public void onValidPlayerInteraction(DotsInteraction interaction) {

            }

            @Override
            public void onBoardChanged(DotsBoard board) {

            }

            @Override
            public void onGameOver() {

            }
        });


        // Testing scanner thread
        Thread scannerThread = new Thread(new DotsTestScannerListener(dotsServer));

        // Starts the server
        dotsServer.start();
        scannerThread.start();


        System.out.println("Main thread has reached the end");

    }
}

/**
 * Thread to listen and deal with messages from the client
 */
class DotsServerClientListener implements Runnable {

    private final AwesomeServerSocket serverSocket;
    private final DotsGame dotsGame;

    public DotsServerClientListener(AwesomeServerSocket serverSocket, DotsGame dotsGame) {
        this.serverSocket = serverSocket;
        this.dotsGame = dotsGame;

    }

    @Override
    public void run() {

        try {

            while (this.dotsGame.isGameRunning()) {

                // read and deal with messages from the client
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

            // doMove will automatically obtain a lock and notify the main thread in the dotsGame thread
            boolean response = this.dotsGame.doMove(receivedInteraction);

            // Package and send a response to the client
            DotsMessageResponse dotsMessageResponse = new DotsMessageResponse(response);
            DotsSocketHelper.sendMessageToClient(this.serverSocket, dotsMessageResponse);

        } else {

            System.err.println("Invalid message type");

        }
    }
}

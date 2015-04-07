package Sockets;

import AwesomeSockets.AwesomeServerSocket;
import Constants.DotsConstants;
import Dots.*;
import AndroidCallback.DotsAndroidCallback;
import Latency.RuntimeStopwatch;
import Model.*;

import java.io.IOException;

/**
 *
 * Primary object to be run by the server for the game
 * Created by JiaHao on 25/2/15.
 */
public class DotsServer extends DotsServerClientParent{

    private final DotsGame dotsGame;
    private final DotsLocks dotsLocks;
    private final AwesomeServerSocket serverSocket;
    private final Thread listenerThread;

    private final RuntimeStopwatch runtimeStopwatch;

    public DotsServer(int port) throws IOException {

        // initialize server
        this.serverSocket = new AwesomeServerSocket(port);

        // initialize game object
        this.dotsGame = new DotsGame();
        this.dotsLocks = dotsGame.getGameLocks();
        this.runtimeStopwatch = new RuntimeStopwatch();

        // init listener thread
        DotsServerClientListener dotsClientListener = new DotsServerClientListener(this.serverSocket, this.dotsGame, this);
        listenerThread = new Thread(dotsClientListener);
    }

    public void start() throws IOException, InstantiationException, InterruptedException {
        // Super checks if callbacks have been initialized
        super.start();

        // Accepts the client (BLOCKS)
        this.serverSocket.acceptClient();

        // starts thread to listen for messages
        listenerThread.start();

        // First time run to display the board on the client and server
        this.updateBoard();

        System.out.println("Waiting for interactions");

        super.setGameStarted(true);

        // todo closes server and clients when game over

    }

    @Override
    public void doInteraction(DotsInteraction dotsInteraction) throws IOException, InterruptedException {
        System.out.println("Doing Interaction: " + dotsInteraction);

        this.runtimeStopwatch.startMeasurement();

        boolean result = this.dotsGame.doMove(dotsInteraction);

        // if the interaction came from the client, we have to send a response to the client
        // that contains the validity of the move
        if (dotsInteraction.getPlayerId() == 1) {

            DotsMessageResponse dotsMessageResponse = new DotsMessageResponse(result);
            DotsSocketHelper.sendMessageToClient(this.serverSocket, dotsMessageResponse);
        }


        // only proceed if its a valid move
        if (result) {

            /**
             * There are two types of game screen updates:
             * 1. Update to show user touches when touch down and dragged
             * 2. Update the entire board when elements are cleared
             */

            // First we deal with the first case where we show interactions on the screen

            updateScreenForTouchInteractions(dotsInteraction);

            // Here, we only send screen interactions to the client if the interaction is made by the server player.
            // The client will draw its onw client player interactions based on the response it receives
            if (dotsInteraction.getPlayerId() == 0) {

                // Sends the valid interaction to the client player so that the client can see the move made by the other player
                DotsMessageInteraction interactionMessage = new DotsMessageInteraction(dotsInteraction);
                DotsSocketHelper.sendMessageToClient(this.serverSocket, interactionMessage);

            }


            // finally, we check if the board needs to be updated
            // dotsGame will automatically update this variable if the board is changed
            if (this.dotsLocks.isBoardChanged()) {

                this.updateBoard();

            }
        }

        // perform a check for game over
        if (!dotsLocks.isGameRunning()) {

            // Send the game over message to the client
            DotsMessageGameOver gameOverMessage = new DotsMessageGameOver();
            DotsSocketHelper.sendMessageToClient(this.serverSocket, gameOverMessage);

            // triggers the game over callback
            this.gameOver();
        }

        this.runtimeStopwatch.stopMeasurement();
        System.out.println("Latency: " + this.runtimeStopwatch.getAverageRuntime());

    }

    /**
     * Updates the board, by doing the callback, and sending the board to the client
     * @throws IOException
     */
    public void updateBoard() throws IOException {

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

    /**
     * Triggers the gameover callback
     */
    private void gameOver() {
        System.out.println("GAME OVER");
        this.getAndroidCallback().onGameOver();
    }


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
        Thread scannerThread = new Thread(new DotsTestScannerListener(dotsServer, 0, true));

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
    private final DotsServerClientParent dotsServerClientParent;

    public DotsServerClientListener(AwesomeServerSocket serverSocket, DotsGame dotsGame, DotsServerClientParent dotsServerClientParent) {
        this.serverSocket = serverSocket;
        this.dotsGame = dotsGame;
        this.dotsServerClientParent = dotsServerClientParent;
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
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Game over!");

    }

    /**
     * This method deals with messages sent from the client.
     * @param message
     * @throws IOException
     */
    private void dealWithMessage(DotsMessage message) throws IOException, InterruptedException {

        // Right now, only one case of the message, which is an interaction.
        // The response here would be to respond with a boolean indicating the validity of the interaction
        if (message instanceof DotsMessageInteraction) {

            // process the interaction
            DotsInteraction receivedInteraction = ((DotsMessageInteraction) message).getDotsInteraction();

            // doMove will automatically obtain a lock and notify the main thread in the dotsGame thread
//            boolean response = this.dotsGame.doMove(receivedInteraction);

//
//
//            // Package and send a response to the client
//            DotsMessageResponse dotsMessageResponse = new DotsMessageResponse(response);
//            DotsSocketHelper.sendMessageToClient(this.serverSocket, dotsMessageResponse);

            this.dotsServerClientParent.doInteraction(receivedInteraction);


        } else {

            System.err.println("Invalid message type");

        }
    }
}

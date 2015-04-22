package Sockets;

import AwesomeSockets.AwesomeServerSocket;
import Constants.DotsConstants;
import Dots.*;
import AndroidCallback.DotsAndroidCallback;
import Latency.RuntimeStopwatch;
import Model.Interaction.DotsInteraction;
import Model.Interaction.DotsInteractionStates;
import Model.Locks.DotsLocks;
import Model.Messages.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

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

    private final boolean isSinglePlayer;



    public DotsServer(int port) throws IOException {


        // initialize game object
        this.dotsGame = new DotsGame();
        this.dotsLocks = dotsGame.getGameLocks();
        this.runtimeStopwatch = new RuntimeStopwatch();


        if (port == DotsConstants.SINGLE_PLAYER_PORT) {
            this.isSinglePlayer = true;
            this.serverSocket = null;
            this.listenerThread = null;

            DotsSocketHelper.enabled = false;

        } else {
            this.serverSocket = new AwesomeServerSocket(port);
            this.isSinglePlayer = false;
            // init listener thread
            DotsServerClientListener dotsClientListener = new DotsServerClientListener(this.serverSocket, this.dotsGame, this);
            this.listenerThread = new Thread(dotsClientListener);
        }
    }

    public void start() throws IOException, InstantiationException, InterruptedException {
        // Super checks if callbacks have been initialized
        super.start();

        if (!this.isSinglePlayer) {

            // Accepts the client (BLOCKS)
            this.serverSocket.acceptClient();
            this.getAndroidCallback().onSocketConnected();

            // starts thread to listen for messages
            this.listenerThread.start();

        }


        // First time run to display the board on the client and server
        this.updateBoard();

        System.out.println("Waiting for interactions");

        super.setGameStarted(true);

        // todo closes server and clients when game over

    }

    @Override
    public void doInteraction(DotsInteraction dotsInteraction) throws IOException, InterruptedException {

        if (this.dotsLocks.isGameRunning()) {

            this.runtimeStopwatch.startMeasurement();

            int playerId = dotsInteraction.getPlayerId();

            boolean interactionIsValid = this.dotsGame.doMove(dotsInteraction);

            // if the client made the move and it is an invalid move, we need to cancel the displayed touch on the client
            if (playerId == 1 && !interactionIsValid) {

                DotsInteraction cancelCurrentInteraction = DotsInteraction.getInvalidResponseInteractionInstance(dotsInteraction);
                DotsMessageInteraction cancelCurrentInteractionMessage = new DotsMessageInteraction(cancelCurrentInteraction);
                DotsSocketHelper.sendMessageToClient(this.serverSocket, cancelCurrentInteractionMessage);

            }


            // only proceed if its a valid move
            if (interactionIsValid) {

                /**
                 * There are two types of game screen updates:
                 * 1. Update to show user touches when touch down and dragged
                 * 2. Update the entire board when elements are cleared
                 */

                DotsInteraction interactionToUpdate;
                boolean shouldSendInteractionToClient = false;

                // if the board needs updating
                if (this.dotsLocks.getChangedDots() != null) {

                    // update the board state, case (2)
                    this.updateBoard();

                    // package the interaction with the correct animation and clearAll parameters
                    interactionToUpdate = DotsInteraction.getUpdateBoardInteraction(dotsInteraction);
                    shouldSendInteractionToClient = true;

                } else {

                    // if board does not need updating

                    if (playerId == 0) {
                        // send valid moves made by the server to client
                        shouldSendInteractionToClient = true;
                    } else {

                        // if the move was made by the client, we want to send a touch_up response to the client
                        if (dotsInteraction.getState() == DotsInteractionStates.TOUCH_UP) {
                            shouldSendInteractionToClient = true;
                        }

                    }

                    // no need to update board, the valid interaction remains the same as the input parameter
                    interactionToUpdate = dotsInteraction;

                }

                // case(1) update the touch path

                if (shouldSendInteractionToClient) {
                    // send the interation to the client
                    DotsMessageInteraction interactionMessage = new DotsMessageInteraction(interactionToUpdate);
                    DotsSocketHelper.sendMessageToClient(this.serverSocket, interactionMessage);
                }

                // update the screen
                this.updateScreenForTouchInteractions(interactionToUpdate);

                if (this.dotsLocks.powerUpNeedsUpdate()) {

                    dealWithPowerUp(playerId);

                }


                // if getPlayerAffected is not -1, a player has been affected
                int playerAffected = this.dotsLocks.getPlayerAffected();
                if (playerAffected != -1) {

                    // Create an arbitrary touch up interaction to clear all dots
                    DotsInteraction clearDisplayedInteraction = DotsInteraction.getConflictInteractionInstance(playerAffected);

                    // Clear touches on the screen for the player affected
                    this.updateScreenForTouchInteractions(clearDisplayedInteraction);

                    // send the message to the client
                    DotsMessageInteraction interactionMessage = new DotsMessageInteraction(clearDisplayedInteraction);
                    DotsSocketHelper.sendMessageToClient(this.serverSocket, interactionMessage);

                    // Reset the playerAffected variable
                    this.dotsLocks.setPlayerAffected(-1);

                }
            }

            if (this.dotsLocks.isScoreNeedingUpdate()) {

                int[] score = this.dotsGame.getScores();

                DotsMessageScore scoreMessage = new DotsMessageScore(score);
                DotsSocketHelper.sendMessageToClient(this.serverSocket, scoreMessage);

                this.getAndroidCallback().onScoreUpdated(score);

                this.dotsLocks.setScoreNeedingUpdate(false);

            }

            // perform a check for game over
            if (!this.dotsLocks.isGameRunning()) {

                // Send the game over message to the client

                int[] score = this.dotsGame.getScores();

                DotsMessageGameOver gameOverMessage = new DotsMessageGameOver(score);
                DotsSocketHelper.sendMessageToClient(this.serverSocket, gameOverMessage);

                // triggers the game over callback
                this.gameOver(score);
            }

            this.getAndroidCallback().latencyChanged(this.runtimeStopwatch.stopMeasurement());

        } else {
            System.err.println("Interaction not complete, game is not running!");
        }
    }

    private void dealWithPowerUp(int currentPlayer) throws IOException {


        int[] powerUpCount = this.dotsLocks.getPowerUpCount();

        // iterate through all possible powerup instances
        for (int i = 0; i < powerUpCount.length; i++) {

            int current = powerUpCount[i];

            if (current > 0) {

                DotsPowerUpType powerUpType;

                // selects the powerUp type by int
                if (i == 0) {
                    powerUpType = DotsPowerUpType.BOMB;
                } else {
                    powerUpType = DotsPowerUpType.FREEZE;
                }

                // creates a message for the power up with the correct duration
                DotsMessagePowerUp messagePowerUp = new DotsMessagePowerUp(powerUpType, current*DotsConstants.POWER_UP_DURATION);

                if (currentPlayer == 0) {
                    // if interaction came from the server, send it to the client
                    DotsSocketHelper.sendMessageToClient(this.serverSocket, messagePowerUp);

                } else {
                    // if the interaction came from the client, update the server callback

                    this.updateLocalPowerUpCallback(messagePowerUp);
                }

            }
        }

        this.dotsLocks.resetPowerUpCount();

    }



    /**
     * Updates the board, by doing the callback, and sending the board to the client
     * @throws IOException
     */
    public void updateBoard() throws IOException {

        // update the board on the current device

        ArrayList<DotsPoint> changedDots = this.dotsLocks.getChangedDots();
//
        this.dotsGame.getDotsBoard().printWithIndex();

        this.getAndroidCallback().onBoardChanged(changedDots);


        // update the board on the remote device
        DotsMessageBoard messageBoard = new DotsMessageBoard(changedDots);
        DotsSocketHelper.sendMessageToClient(this.serverSocket, messageBoard);

        this.dotsLocks.setChangedDots(null);
//        this.dotsLocks.setBoardChanged(false);

    }

    /**
     * Method here to update the screen for touch interaction, i.e. reflect touches
     * @param dotsInteraction interaction object
     */
    private void updateScreenForTouchInteractions(DotsInteraction dotsInteraction) {
        this.getAndroidCallback().onValidPlayerInteraction(dotsInteraction);
    }

    /**
     * Triggers the game over callback
     */
    private void gameOver(int[] finalScore) {
        System.out.println("GAME OVER");

        int winningPlayerId = DotsServerClientParent.getWinner(finalScore);

        this.getAndroidCallback().onGameOver(winningPlayerId, finalScore);
    }


    @Override
    public void stopGame() {

        // By right, if game over, dotsLocks.isGameRunning should automatically be set to false
        // just do it again to be sure
        // this will terminate the while loop in DotsServerClientListener and stop the game
        this.dotsLocks.setGameRunning(false);

        // close the socket
        if (!this.isSinglePlayer) {
            try {
                this.serverSocket.closeServer();
            } catch (IOException e) {
                System.err.println("IO Exception with closing server: " + e);
            }
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException, InstantiationException {

        // Initialise the server
        DotsServer dotsServer = new DotsServer(DotsConstants.CLIENT_PORT);
//        DotsServer dotsServer = new DotsServer(DotsConstants.SINGLE_PLAYER_PORT);
        // Compulsory to set listeners

        dotsServer.setAndroidCallback(new DotsAndroidCallback() {
            @Override
            public void onSocketConnected() {

            }

            @Override
            public void onValidPlayerInteraction(DotsInteraction interaction) {


            }

            @Override
            public void onBoardChanged(ArrayList<DotsPoint> changedPoints) {
                System.out.println("Changed points: " + changedPoints.toString());
            }

            @Override
            public void onGameOver(int winningPlayerId, int[] finalScore) {

            }


            @Override
            public void onScoreUpdated(int[] score) {
                System.out.println("Score : " + Arrays.toString(score));
            }

            @Override
            public void latencyChanged(long latency) {
                System.out.println("Current Latency: " + latency);
            }

            @Override
            public void onPowerUpReceived(DotsPowerUp powerUp) {
                System.out.println("Power Up received: " + powerUp);
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

        while (this.dotsGame.isGameRunning()) {
            try {

                // read and deal with messages from the client
                DotsMessage message = DotsSocketHelper.readMessageFromClient(serverSocket);
                this.dealWithMessage(message);


            } catch(IOException e){
                System.err.println("DotsServerClientListener IO Exception: " + e);
                // this happens when the connection is cut on the client side
                break;
            } catch(ClassNotFoundException e){
                System.err.println("DotsServerClientListener ClassNotFoundException: " + e);
            } catch(InterruptedException e){
                System.err.println("DotsServerClientListener interrupted: " + e);
            }
        }

        System.out.println("Server listener thread reached end");


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
            this.dotsServerClientParent.doInteraction(receivedInteraction);


        } else {

            System.err.println("Invalid message type");

        }
    }
}

package Sockets;

import AwesomeSockets.AwesomeClientSocket;
import Constants.DotsConstants;

import AndroidCallback.DotsAndroidCallback;
import Dots.DotsPoint;
import Dots.DotsPowerUp;
import Dots.DotsPowerUpState;
import Dots.DotsPowerUpType;
import Latency.RuntimeStopwatch;
import Model.Interaction.DotsInteraction;
import Model.Interaction.DotsInteractionStates;
import Model.Locks.DotsLocks;
import Model.Messages.*;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Primary object to be run by the client for the game
 *
 * Created by JiaHao on 25/2/15.
 */
public class DotsClient extends DotsServerClientParent {

    private final String serverAddress;
    private final int port;

    // States
    private final DotsLocks dotsLocks;
    private final RuntimeStopwatch runtimeStopwatch;

    private AwesomeClientSocket clientSocket;


    public DotsClient(String serverAddress, int port) throws IOException {
        this.serverAddress = serverAddress;
        this.port = port;

        // Initialise Model
        this.dotsLocks = new DotsLocks();
        this.runtimeStopwatch = new RuntimeStopwatch();


    }

    public void start() throws IOException, InterruptedException, InstantiationException {
        super.start();

        // Initialize client socket

        this.clientSocket = new AwesomeClientSocket(this.serverAddress, this.port);

        this.getAndroidCallback().onSocketConnected();

        // Init server listener thread
        DotsClientServerListener dotsServerListener = new DotsClientServerListener(this);
        Thread listenerThread = new Thread(dotsServerListener);

        // start thread to deal with messages from server
        listenerThread.start();
        super.setGameStarted(true);
        System.out.println("Waiting for interactions");

    }

    /**
     * Method to call with an interaction on the screen
     * @param dotsInteraction touch interactions on the screen
     */
    @Override
    public void doInteraction(final DotsInteraction dotsInteraction) throws IOException, InterruptedException {

        this.runtimeStopwatch.startMeasurement();

        // Package the interaction in to a message
        DotsMessageInteraction interactionMessage = new DotsMessageInteraction(dotsInteraction);

        // and send it to the server
        DotsSocketHelper.sendMessageToServer(this.clientSocket, interactionMessage);

        // Temporarily draw interactions first on screen before validating it with the server
        // to make the client seem more responsive
        updateScreenForTouchInteractions(dotsInteraction);
    }

    /**
     * Method here to update the screen for touch interaction, i.e. reflect touches
     * @param dotsInteraction interaction object
     */
    private void updateScreenForTouchInteractions(DotsInteraction dotsInteraction) {

        // as the method is called from the same device, we ignore touch_Up interactions. Touch up interactions are
        // processed in the server and sent to the client, as the server has to package the interactions to indicate
        // if need to clearAll and animate
        if (dotsInteraction.getState() != DotsInteractionStates.TOUCH_UP) {
            this.getAndroidCallback().onValidPlayerInteraction(dotsInteraction);

        }


    }

    @Override
    public void stopGame() {

        // this will terminate the while loop in DotsClientServerListener and stop the game
        this.dotsLocks.setGameRunning(false);

        try {
            this.clientSocket.closeClient();
        } catch (IOException e) {
            System.err.println("Close client IO Exception: " + e);
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException, InstantiationException {

//        final String SERVER_ADDRESS = "10.12.17.172";
        final String SERVER_ADDRESS = "127.0.0.1";

        // Initialise the client
        DotsClient dotsClient = new DotsClient(SERVER_ADDRESS, DotsConstants.CLIENT_PORT);

        // Compulsory to add listeners for changes
        dotsClient.setAndroidCallback(new DotsAndroidCallback() {
            @Override
            public void onSocketConnected() {

            }

            @Override
            public void onValidPlayerInteraction(DotsInteraction interaction) {

                // skeleton code for android
                boolean animate = interaction.isAnimate();
                boolean clearAll = interaction.isClearAll();
                boolean makeVisible;

                if (interaction.getState() != DotsInteractionStates.TOUCH_UP) {
                    makeVisible = true;
                } else {
                    makeVisible = false;
                }

                // base on the booleans set the touch path appropriately

            }

            @Override
            public void onBoardChanged(ArrayList<DotsPoint> changedPoints) {

                System.out.println("Changed points: " + changedPoints.toString());
            }

            @Override
            public void onGameOver(int winningPlayerId, int[] finalScore) {
                System.out.println("GAME OVER, WINNER: " + winningPlayerId + " FINAL SCORE: " + Arrays.toString(finalScore));
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
        Thread scannerThread = new Thread(new DotsTestScannerListener(dotsClient, 1, true));

        // Starts the client
        dotsClient.start();
        scannerThread.start();

    }

    public DotsLocks getDotsLocks() {
        return dotsLocks;
    }

    public AwesomeClientSocket getClientSocket() {
        return clientSocket;
    }

    public RuntimeStopwatch getRuntimeStopwatch() {
        return runtimeStopwatch;
    }
}




/**
 * Thread to listen for messages from the server and deal with them
 */
class DotsClientServerListener implements Runnable {

    private final AwesomeClientSocket clientSocket;
    private final DotsLocks locks;
    private final DotsAndroidCallback dotsAndroidCallback;
    private final RuntimeStopwatch runtimeStopwatch;

    private final DotsClient dotsClient;

    public DotsClientServerListener(DotsClient dotsClient) {
        this.clientSocket = dotsClient.getClientSocket();
        this.locks = dotsClient.getDotsLocks();
        this.dotsAndroidCallback = dotsClient.getAndroidCallback();
        this.runtimeStopwatch = dotsClient.getRuntimeStopwatch();

        this.dotsClient = dotsClient;
    }

    @Override
    public void run() {

        while (this.locks.isGameRunning()) {

            try {

                // Read message from server
                DotsMessage message = DotsSocketHelper.readMessageFromServer(clientSocket);
                System.out.println(message);
                this.dealWithMessage(message);

            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Server is closed!");

                break;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                System.err.println("Message received cannot be resolved to a class");
                break;
            } catch (InterruptedException e) {
                System.err.println("Adding to response queue has been interrupted");
                e.printStackTrace();
            }
        }
    }

    /**
     * Primary method to deal with received messages appropriately
     * @param message a message that belongs to the parent DotsMessage
     * @throws InterruptedException if placing a response in the responseQueue is interrupted
     */
    private void dealWithMessage(DotsMessage message) throws InterruptedException {

        if (message instanceof DotsMessageBoard) {

            DotsMessageBoard receivedBoardMessage = (DotsMessageBoard) message;
            ArrayList<DotsPoint> receivedPoints = receivedBoardMessage.getChangedPoints();
            this.updateScreenWithNewPoints(receivedPoints);

        } else if (message instanceof DotsMessageInteraction) {
            // deal with interactions from the other player (server player)
            DotsMessageInteraction receivedInteractionMessage = (DotsMessageInteraction) message;
            DotsInteraction receivedInteraction = receivedInteractionMessage.getDotsInteraction();
            this.updateScreenWithInteraction(receivedInteraction);


            if (receivedInteraction.getPlayerId() == 1) {
                this.dotsAndroidCallback.latencyChanged(this.runtimeStopwatch.stopMeasurement());
            }


        } else if (message instanceof DotsMessageGameOver) {

            System.out.println("GAME OVER RECEIVED");
            // Trigger the game over callback

            int[] finalScore = ((DotsMessageGameOver) message).getScore();
            int winningPlayerId = DotsServerClientParent.getWinner(finalScore);

            this.dotsAndroidCallback.onGameOver(winningPlayerId, finalScore);

        } else if (message instanceof DotsMessageScore) {

            int[] score = ((DotsMessageScore) message).getScore();
            this.dotsAndroidCallback.onScoreUpdated(score);

        } else if (message instanceof DotsMessagePowerUp) {

            // start powerup
            this.dotsClient.updateLocalPowerUpCallback((DotsMessagePowerUp) message);


        } else {
            System.err.println("Unknown message type: ");
            System.err.println(message.toString());
        }
    }

    /**
     * Method to update the screen with the board using a callback
     * @param updatedPoints board
     */
    private void updateScreenWithNewPoints(ArrayList<DotsPoint> updatedPoints) {

        // update the board on the current device
        this.dotsAndroidCallback.onBoardChanged(updatedPoints);

    }

    /**
     * Method to update the screen with the moves of the other player
     * @param interaction Interactions here should be all valid moves from the other player. This is checked in dealWithMessage()
     */
    private void updateScreenWithInteraction(DotsInteraction interaction) {

        System.out.println("Interaction received from server: ");
        System.out.println(interaction);
        this.dotsAndroidCallback.onValidPlayerInteraction(interaction);

    }
}


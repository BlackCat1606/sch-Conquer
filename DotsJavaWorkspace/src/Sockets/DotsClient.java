package Sockets;

import AwesomeSockets.AwesomeClientSocket;
import Constants.DotsConstants;
import Dots.DotsBoard;
import ListenerInterface.DotsBoardViewListener;
import ListenerInterface.DotsPlayerMovesListener;
import Model.*;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Primary object to be run by the client for the game
 *
 * Created by JiaHao on 25/2/15.
 */
public class DotsClient extends DotsServerClientParent {

    private final String serverAddress;
    private final int port;

    // States
    private AwesomeClientSocket clientSocket;
    private DotsLocks dotsLocks;
    private LinkedBlockingQueue<Boolean> responseQueue;


    public DotsClient(String serverAddress, int port) {
        this.serverAddress = serverAddress;
        this.port = port;
    }

    public void start() throws IOException, InterruptedException, InstantiationException {
        super.start();

        // Initialise Model
        this.dotsLocks = new DotsLocks();
        this.responseQueue = new LinkedBlockingQueue<Boolean>();

        // Initialize client socket
        this.clientSocket = new AwesomeClientSocket(this.serverAddress, this.port);


        // Init server listener thread
        DotsClientServerListener dotsServerListener = new DotsClientServerListener(this.clientSocket, dotsLocks, responseQueue, this.getBoardViewListener(), this.getPlayerMovesListener());
        Thread listenerThread = new Thread(dotsServerListener);

        // start thread to deal with messages from server
        listenerThread.start();
        System.out.println("Waiting for interactions");

    }

    /**
     * Method to call with an interaction on the screen
     * @param dotsInteraction touch interactions on the screen
     */
    @Override
    public void doInteraction(DotsInteraction dotsInteraction) throws IOException, InterruptedException {

        // Package the interaction in to a message
        DotsMessageInteraction interactionMessage = new DotsMessageInteraction(dotsInteraction);

        // and send it to the server
        DotsSocketHelper.sendMessageToServer(this.clientSocket, interactionMessage);


        // Read the response from the server for the interaction
        // Here, we use a queue which is populated by the other serverListener thread with appropriate
        // responses
        boolean response = this.responseQueue.take();

        System.out.println("Received response: " + response);

        // if the response is valid, it means that the move we have made is valid
        // Therefore, we update the screen based on our touches
        if (response) {
            updateScreenForTouchInteractions(dotsInteraction);
        }

    }

    /**
     * Method here to update the screen for touch interaction, i.e. reflect touches
     * @param dotsInteraction interaction object
     */
    private void updateScreenForTouchInteractions(DotsInteraction dotsInteraction) {

        // debug method to print valid interaction
        System.out.println("DRAW on screen touch interaction: " + dotsInteraction.toString());
        this.getPlayerMovesListener().onValidInteraction(dotsInteraction);

    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException, InstantiationException {

        // Initialise the client
        DotsClient dotsClient = new DotsClient(DotsConstants.SERVER_ADDRESS, DotsConstants.CLIENT_PORT);

        // Compulsory to add listeners for changes
        dotsClient.setBoardViewListener(new DotsBoardViewListener() {
            @Override
            public void onBoardUpdate(DotsBoard board) {

            }
        });

        dotsClient.setPlayerMovesListener(new DotsPlayerMovesListener() {
            @Override
            public void onValidInteraction(DotsInteraction interaction) {

            }
        });

        // Testing scanner thread
        Thread scannerThread = new Thread(new DotsTestScannerListener(dotsClient));

        // Starts the client
        dotsClient.start();
        scannerThread.start();

    }

    public DotsLocks getDotsLocks() {
        return dotsLocks;
    }
}




/**
 * Thread to listen for messages from the server and deal with them
 */
class DotsClientServerListener implements Runnable {

    private final AwesomeClientSocket clientSocket;
    private final DotsLocks locks;
    private final LinkedBlockingQueue<Boolean> responseQueue;
    private final DotsBoardViewListener boardViewListener;
    private final DotsPlayerMovesListener playerMovesListener;

    public DotsClientServerListener(
            AwesomeClientSocket clientSocket,
            DotsLocks locks,
            LinkedBlockingQueue<Boolean> responseQueue,
            DotsBoardViewListener boardViewListener,
            DotsPlayerMovesListener playerMovesListener) {

        this.clientSocket = clientSocket;
        this.locks = locks;
        this.responseQueue = responseQueue;
        this.boardViewListener = boardViewListener;
        this.playerMovesListener = playerMovesListener;

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
                break;
            } catch (InterruptedException e) {
                System.err.println("Adding to response queue has been interrupted");
                e.printStackTrace();
            }

        }

    }

    private void dealWithMessage(DotsMessage message) throws InterruptedException {

        // TODO Check and deal with game over

        if (message instanceof DotsMessageBoard) {

            DotsMessageBoard receivedBoardMessage = (DotsMessageBoard) message;
            DotsBoard receivedBoard = receivedBoardMessage.getDotsBoard();

            this.updateScreenWithNewBoard(receivedBoard);

        } else if (message instanceof DotsMessageResponse) {

            DotsMessageResponse receivedResponse = (DotsMessageResponse) message;
            boolean response = receivedResponse.getResponse();
            this.responseQueue.put(response);

        } else if (message instanceof DotsMessageInteraction) {
            // deal with interactions from the other player (server player)
            DotsMessageInteraction receivedInteractionMessage = (DotsMessageInteraction) message;
            DotsInteraction receivedInteraction = receivedInteractionMessage.getDotsInteraction();
            this.updateScreenWithInteraction(receivedInteraction);

        } else {
            System.err.println("Unknown message type: ");
            System.err.println(message.toString());
        }


    }

    /**
     * Method to update the screen with the board using a callback
     * @param dotsBoard board
     */
    private void updateScreenWithNewBoard(DotsBoard dotsBoard) {


        // Debug method to print the board to the console
        dotsBoard.printWithIndex();

        // update the board on the current device
        this.boardViewListener.onBoardUpdate(dotsBoard);

    }

    /**
     * Method to update the screen with the moves of the other player
     * @param interaction Interactions here should be all valid moves from the other player. This is checked in dealWithMessage()
     */
    private void updateScreenWithInteraction(DotsInteraction interaction) {

        System.out.println("Interaction received from server: ");
        System.out.println(interaction);
        this.playerMovesListener.onValidInteraction(interaction);

    }
}


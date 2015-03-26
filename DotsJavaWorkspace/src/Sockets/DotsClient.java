package Sockets;

import AwesomeSockets.AwesomeClientSocket;
import Constants.DotsConstants;
import Dots.DotsBoard;
import Model.*;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Primary object to be run by the client for the game
 *
 * Created by JiaHao on 25/2/15.
 */
public class DotsClient {

    private final String serverAddress;
    private final int port;

    public DotsClient(String serverAddress, int port) {
        this.serverAddress = serverAddress;
        this.port = port;
    }

    public void start() throws IOException, InterruptedException {

        // Initialise Model
        DotsLocks dotsLocks = new DotsLocks();
        LinkedBlockingQueue<Boolean> responseQueue = new LinkedBlockingQueue<Boolean>();

        // Initialize client socket
        AwesomeClientSocket clientSocket = new AwesomeClientSocket(this.serverAddress, this.port);


        // Init server listener thread
        DotsClientServerListener dotsServerListener = new DotsClientServerListener(clientSocket, dotsLocks, responseQueue);
        Thread listenerThread = new Thread(dotsServerListener);

        // Init scanner thread
        Scanner scanner = new Scanner(System.in);
        DotsClientScannerListener dotsClientScannerListener = new DotsClientScannerListener(scanner, clientSocket, dotsLocks, responseQueue);
        Thread scannerThread = new Thread(dotsClientScannerListener);

        // start threads
        listenerThread.start();
        scannerThread.start();

        // Temporary debug
        // create an object here to block on while the game is running
        Object gameRunningLock = new Object();

        synchronized (gameRunningLock) {

            while (dotsLocks.isGameRunning()) {

                gameRunningLock.wait();
            }
        }

        clientSocket.closeClient();

    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {


        DotsClient dotsClient = new DotsClient(DotsConstants.SERVER_ADDRESS, DotsConstants.CLIENT_PORT);
        dotsClient.start();
    }
}

/**
 * Thread to listen for input from the scanner
 * TODO translate the scanner to system.in or something to parse touches on android to maintain modularity?
 */
class DotsClientScannerListener implements Runnable {

    private final Scanner scanner;
    private final AwesomeClientSocket clientSocket;
    private final DotsLocks locks;
    private final LinkedBlockingQueue<Boolean> responseQueue;

    public DotsClientScannerListener(Scanner scanner, AwesomeClientSocket clientSocket, DotsLocks locks, LinkedBlockingQueue<Boolean> responseQueue) {
        this.scanner = scanner;
        this.clientSocket = clientSocket;
        this.locks = locks;
        this.responseQueue = responseQueue;
    }

    @Override
    public void run() {

        while (this.locks.isGameRunning()) {

            // Whenever we do an interaction on the screen, we send a message to the server and get a response
            // of the move's validity
            DotsInteraction dotsInteraction = DotsSocketHelper.getInteractionFromScanner(1, this.scanner);

            try {

                DotsMessageInteraction interactionMessage = new DotsMessageInteraction(dotsInteraction);
                DotsSocketHelper.sendMessageToServer(this.clientSocket, interactionMessage);

                // Here, we use a queue which is populated by the other serverListener thread with appropriate
                // responses

                boolean response = this.responseQueue.take();

                System.out.println("Received response: " + response);

                // if the response is valid, it means that the move we have made is valid
                // Therefore, we update the screen based on our touches
                if (response) {

                    updateScreenForTouchInteractions(dotsInteraction);

                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                System.err.println("Taking from response queue is interrupted.");
                e.printStackTrace();
            }

        }

        System.out.println("Game over!");
    }

    /**
     * Method here to update the screen for touch interaction, i.e. reflect touches
     * @param dotsInteraction interaction object
     */
    private void updateScreenForTouchInteractions(DotsInteraction dotsInteraction) {

        // TODO add android to update screen for the touch interactions
        // TODO use a interface callback here to make it more modular
        // debug method to print valid interaction
        System.out.println("DRAW on screen touch interaction: " + dotsInteraction.toString());

    }
}

/**
 * Thread to listen for messages from the server and deal with them
 */
class DotsClientServerListener implements Runnable {

    private final AwesomeClientSocket clientSocket;
    private final DotsLocks locks;
    private final LinkedBlockingQueue<Boolean> responseQueue;

    public DotsClientServerListener(AwesomeClientSocket clientSocket, DotsLocks locks, LinkedBlockingQueue<Boolean> responseQueue) {
        this.clientSocket = clientSocket;
        this.locks = locks;
        this.responseQueue = responseQueue;
    }

    @Override
    public void run() {

        while (this.locks.isGameRunning()) {

            try {

                // Read message from server
                DotsMessage message = DotsSocketHelper.readMessageFromServer(clientSocket);
                this.dealWithMessage(message);

            } catch (IOException e) {
//                e.printStackTrace();
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
        }

        // TODO deal with server player interactions (draw them on the screen)

    }

    /**
     * Android method to update the screen with the board
     * @param dotsBoard board
     */
    private void updateScreenWithNewBoard(DotsBoard dotsBoard) {

        // update the board on the current device
        // TODO change this to a method to update the screen when on android
        // TODO use a interface callback here to make it more modular

        // Debug method to print the board to the console
        dotsBoard.printWithIndex();


    }
}


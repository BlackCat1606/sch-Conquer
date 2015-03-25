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
    // init as true so that the board will print it in the first time it listens for change
    public static boolean boardChanged = true;
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        final int PORT = 4321;

        // initialize server
        AwesomeServerSocket server = new AwesomeServerSocket(PORT);
//        server.debugMode = false;
        server.acceptClient();

        // initialize game object
        DotsGame dotsGame = new DotsGame();

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

        final Object boardChangedLock = new Object();



        // block for game sequence
        while (dotsGame.isGameRunning()) {


            // gets a lock for waiting
            synchronized (boardChangedLock) {

                while (!boardChanged) {

                    try {
                        boardChangedLock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


                doBoardUpdating(server, dotsGame);


//                DotsMessageBoard messageBoard = new DotsMessageBoard(dotsGame.getDotsBoard());
//                DotsSocketHelper.sendMessageToClient(server, messageBoard);

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

        boardChanged = false;

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

            // we assume that the board is changed whenever a move is made now
            DotsServer.boardChanged = true;

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

                // we assume that the board is changed whenever a move is made now
                DotsServer.boardChanged = true;

            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("Game over!");
    }


    private void dealWithMessage(DotsMessage message) throws IOException {


        if (message instanceof DotsMessageInteraction) {


            // process the interaction
            DotsInteraction receivedInteraction = ((DotsMessageInteraction) message).getDotsInteraction();
            boolean response = this.dotsGame.doMove(receivedInteraction);

            // send a response to the client
            DotsMessageResponse dotsMessageResponse = new DotsMessageResponse(response);

            DotsSocketHelper.sendMessageToClient(serverSocket, dotsMessageResponse);


        } else {

            System.err.println("Invalid message type");

        }



    }


}

package Sockets;

import AwesomeSockets.AwesomeClientSocket;
import Dots.Dot;
import Dots.DotsBoard;
import Dots.Point;

import java.io.IOException;
import java.util.Scanner;

/**
 * Created by JiaHao on 25/2/15.
 */
public class DotsClient {
    // start as false here, as
    public static boolean boardChanged = false;
    public static boolean gameIsRunning = true;

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        final int PORT = 4321;
        final String SERVER_ADDRESS = "localhost";

        // Initialize client socket
        AwesomeClientSocket clientSocket = new AwesomeClientSocket(SERVER_ADDRESS, PORT);


        // Init server listener thread
        DotsClientServerListener dotsServerListener = new DotsClientServerListener(clientSocket);
        Thread listenerThread = new Thread(dotsServerListener);

        // Init scanner thread
        Scanner scanner = new Scanner(System.in);


        // start threads
        listenerThread.start();
//



        try {

            while (gameIsRunning) {
                // sleep first so client has time to get board from server
                // also sleep here to avoid the process consuming the cpu
                Thread.sleep(100);

                if (boardChanged) {
                    DotsBoard.printBoardWithIndex(dotsServerListener.getBoardArray());
                    boardChanged = false;
                }



            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        clientSocket.closeClient();

    }
}

class DotsClientScannerListener implements Runnable {

    private final Scanner scanner;
    private final AwesomeClientSocket clientSocket;

    public DotsClientScannerListener(AwesomeClientSocket clientSocket, Scanner scanner) {
        this.scanner = scanner;
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {

        while (DotsClient.gameIsRunning) {

            Point inputPoint = DotsSocketHelper.getPointFromScanner(this.scanner);

//
            try {
                DotsSocketHelper.sendMoveToServer(this.clientSocket, inputPoint);

                // TODO get response from server

                // we assume that the board is changed whenever a move is made now
                DotsServer.boardChanged = true;


            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        System.out.println("Game over!");
    }
}

class DotsClientServerListener implements Runnable {

    private Dot[][] boardArray;
    private final AwesomeClientSocket clientSocket;


    public DotsClientServerListener(AwesomeClientSocket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public Dot[][] getBoardArray() {
        return this.boardArray;
    }


    @Override
    public void run() {

        while (DotsClient.gameIsRunning) {

            try {
                // TODO protocol to read messages from server

                // check for game over
//                if (message == game over) {
//                    DotsClient.gameIsRunning = false;
//                }

                // check for board
                System.out.println("waiting for board");
                this.boardArray = DotsSocketHelper.readBoardFromServer(this.clientSocket);
                DotsClient.boardChanged = true;
                System.out.println("board received");



            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


        }

    }
}


package Sockets;

import AwesomeSockets.AwesomeServerSocket;
import Dots.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by JiaHao on 25/2/15.
 */
public class DotsServer {

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        final int PORT = 4321;

        AwesomeServerSocket server = new AwesomeServerSocket(PORT);
//        server.debugMode = false;

        server.acceptClient();

        DotsGame dotsGame = new DotsGame();

        DotsSocketHelper.sendBoardToClient(server, dotsGame.getBoardArray());
        DotsBoard.printBoardWithIndex(dotsGame.getBoardArray());

        DotsMessageListener dotsMessageListener = new DotsMessageListener(server, 0);
        dotsMessageListener.setDotsGame(dotsGame);

        Thread listenerThread = new Thread(dotsMessageListener);
        listenerThread.start();

        Scanner scanner = new Scanner(System.in);


        while (dotsGame.isGameRunning()) {


            Point inputPoint = DotsSocketHelper.getPointFromScanner(scanner);


            boolean result = dotsGame.doMove(0, inputPoint);

            System.out.println(result);

            DotsBoard.printBoardWithIndex(dotsGame.getBoardArray());
        }

        server.closeServer();

    }



}

class DotsMessageListener implements Runnable {

    private final AwesomeServerSocket serverSocket;
    private final ArrayList<String> messages;
    private final int clientNumber;

    private DotsGame dotsGame = null;

    public DotsMessageListener(AwesomeServerSocket serverSocket, int clientNumber) {
        this.serverSocket = serverSocket;
        this.messages = new ArrayList<String>();
        this.clientNumber = clientNumber;
    }


    public void setDotsGame(DotsGame dotsGame) {
        this.dotsGame = dotsGame;
    }

    @Override
    public void run() {

        if (this.dotsGame == null) {
            throw new NullPointerException();
        }

        try {

            while (this.dotsGame.isGameRunning()) {
//                String message = this.serverSocket.readMessageLineForClient(this.clientNumber);


                Point receivedPoint = DotsSocketHelper.readMoveFromClient(serverSocket);
                System.out.println("Received point: " + receivedPoint);

                this.dotsGame.doMove(1, receivedPoint);

                Dot[][] boardArray = this.dotsGame.getBoardArray();
                DotsSocketHelper.sendBoardToClient(serverSocket, boardArray);

            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        //... close socket, etc.



    }
}

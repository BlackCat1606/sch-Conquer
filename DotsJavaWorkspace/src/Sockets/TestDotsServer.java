package Sockets;

import AwesomeSockets.AwesomeServerSocket;
import Dots.Dot;
import Dots.DotsBoard;
import Dots.DotsLogic;
import Dots.Point;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by JiaHao on 25/2/15.
 */
public class TestDotsServer {

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        final int PORT = 4321;

        AwesomeServerSocket server = new AwesomeServerSocket(PORT);
//        server.debugMode = false;

        server.acceptClient();


        DotsBoard board = new DotsBoard(5);
//        board.printWithIndex();

        DotsLogic logic = new DotsLogic(board);


        // sends the board to the player

//        Scanner scanner = new Scanner(System.in);

        boolean gameRunning = true;

//        while (gameRunning) {
//
////            scanner.nextLine();

//
//        }


        Dot[][] boardArray = board.getBoardArray();
        DotsSocketHelper.sendBoardToClient(server, boardArray);


        Point receivedPoint = DotsSocketHelper.readMoveFromClient(server);
        System.out.println("Received point:");

        System.out.println(receivedPoint);


        server.closeServer();

    }




}

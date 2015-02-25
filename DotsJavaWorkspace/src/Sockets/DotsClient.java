package Sockets;

import AwesomeSockets.AwesomeClientSocket;
import Dots.Dot;
import Dots.DotsBoard;
import Dots.Point;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

/**
 * Created by JiaHao on 25/2/15.
 */
public class DotsClient {

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        final int PORT = 4321;
        final String SERVER_ADDRESS = "localhost";

        AwesomeClientSocket clientSocket = new AwesomeClientSocket(SERVER_ADDRESS, PORT);



        boolean gameRunning = true;

        Scanner scanner = new Scanner(System.in);

        while (gameRunning) {


            Dot[][] receivedBoard = DotsSocketHelper.readBoardFromServer(clientSocket);
//            System.out.println(Arrays.deepToString(receivedBoard));

            DotsBoard.printBoardWithIndex(receivedBoard);

            Point inputPoint = DotsSocketHelper.getPointFromScanner(scanner);

            DotsSocketHelper.sendMoveToServer(clientSocket, inputPoint);


        }



        clientSocket.closeClient();




    }
}

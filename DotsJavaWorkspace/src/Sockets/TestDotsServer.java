//package Sockets;
//
//import AwesomeSockets.AwesomeServerSocket;
//import Dots.*;
//import Model.Messages.DotsMessage;
//import Model.Messages.DotsMessageBoard;
//import Model.Messages.DotsMessageResponse;
//
//import java.io.IOException;
//
///**
// * Created by JiaHao on 25/2/15.
// */
//public class TestDotsServer {
//
//    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
//
//        final int PORT = 4321;
//
//        AwesomeServerSocket server = new AwesomeServerSocket(PORT);
////        server.debugMode = false;
//
//        server.acceptClient();
//
//
//        DotsGame dotsGame = new DotsGame();
//
//        DotsBoard board = dotsGame.getDotsBoard();
//
//
//
//
//        // Send to client
//        DotsMessageBoard messageBoard = new DotsMessageBoard(board);
//        DotsSocketHelper.sendMessageToClient(server, messageBoard);
//
//        DotsMessageResponse messageResponse = new DotsMessageResponse(true);
//        DotsSocketHelper.sendMessageToClient(server, messageResponse);
//
//
//
//
//        Thread.sleep(2000);
//        // Read from client
//
//        // read from server
//        for (int i = 0; i < 2; i++) {
//
//            DotsMessage message = DotsSocketHelper.readMessageFromClient(server);
//
//            if (message instanceof DotsMessageBoard) {
//                DotsBoard receivedBoard = ((DotsMessageBoard) message).getDotsBoard();
//                receivedBoard.printWithIndex();
//
//            } else if (message instanceof DotsMessageResponse) {
//                boolean response = ((DotsMessageResponse) message).getResponse();
//                System.out.println("Received Response: ");
//                System.out.println(response);
//
//            } else {
//
//                System.err.println("Unknown message type");
//            }
//        }
//
//
//        Thread.sleep(10000);
//
//        server.closeServer();
//
//    }
//
//
//
//
//}

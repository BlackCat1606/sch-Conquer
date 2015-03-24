//package Sockets;
//
//import AwesomeSockets.AwesomeServerSocket;
//import Dots.*;
//
//import java.io.IOException;
//import java.util.Scanner;
//
///**
// * Created by JiaHao on 25/2/15.
// */
//public class DotsServer {
//
//    // static variable for threads to keep track of whether the board has changed
//    // init as true so that the board will print it in the first time it listens for change
//    public static boolean boardChanged = true;
//    public static void main(String[] args) throws IOException, ClassNotFoundException {
//
//        final int PORT = 4321;
//
//        // initialize server
//        AwesomeServerSocket server = new AwesomeServerSocket(PORT);
////        server.debugMode = false;
//        server.acceptClient();
//
//        // initialize game object
//        DotsGame dotsGame = new DotsGame();
//
////        // initial printing of board to client
////        DotsSocketHelper.sendBoardToClient(server, dotsGame.getBoardArray());
//
//
//
//        // init listener thread
//        DotsServerClientListener dotsClientListener = new DotsServerClientListener(server, dotsGame);
//        Thread listenerThread = new Thread(dotsClientListener);
//
//
//
//        // init scanner thread
//        Scanner scanner = new Scanner(System.in);
//        DotsServerScannerListener dotsScannerListener = new DotsServerScannerListener(dotsGame, scanner);
//        Thread scannerThread = new Thread(dotsScannerListener);
//
//        // starts threads
//        listenerThread.start();
//        scannerThread.start();
//
//        // block while game is running and listens for messages
//        // TODO is there a better way to implement a listener for a changed board?
//        try {
//
//            while (dotsGame.isGameRunning()) {
//
//                if (boardChanged) {
//                    DotsBoard.printBoardWithIndex(dotsGame.getBoardArray());
//                    DotsSocketHelper.sendBoardToClient(server, dotsGame.getBoardArray());
//                    boardChanged = false;
//                }
//
//
//                // Sleep here to avoid the process consuming the cpu
//                Thread.sleep(100);
//
//            }
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        // closes server and clients when game over
//        server.closeServer();
//
//        System.out.println("Game over");
//    }
//
//}
//
//
//class DotsServerScannerListener implements Runnable {
//
//    private final Scanner scanner;
//    private final DotsGame dotsGame;
//
//    public DotsServerScannerListener(DotsGame dotsGame, Scanner scanner) {
//        this.scanner = scanner;
//        this.dotsGame = dotsGame;
//    }
//
//    @Override
//    public void run() {
//
//        while (this.dotsGame.isGameRunning()) {
//
//            Point inputPoint = DotsSocketHelper.getPointFromScanner(this.scanner);
//
//
//            boolean result = this.dotsGame.doMove(0, inputPoint);
//
//            // we assume that the board is changed whenever a move is made now
//
//
//
//            DotsServer.boardChanged = true;
//
//            System.out.println(result);
//
//        }
//
//        System.out.println("Game over!");
//    }
//}
//
//class DotsServerClientListener implements Runnable {
//
//    private final AwesomeServerSocket serverSocket;
//
//    private final DotsGame dotsGame;
//
//    public DotsServerClientListener(AwesomeServerSocket serverSocket, DotsGame dotsGame) {
//        this.serverSocket = serverSocket;
//        this.dotsGame = dotsGame;
//
//    }
//
//
//    @Override
//    public void run() {
//
//        if (this.dotsGame == null) {
//            throw new NullPointerException();
//        }
//
//        try {
//
//            while (this.dotsGame.isGameRunning()) {
////                String message = this.serverSocket.readMessageLineForClient(this.clientNumber);
//
//                Point receivedPoint = DotsSocketHelper.readMoveFromClient(serverSocket);
//                System.out.println("Received point: " + receivedPoint);
//
//                this.dotsGame.doMove(1, receivedPoint);
//
//                Dot[][] boardArray = this.dotsGame.getBoardArray();
////                DotsSocketHelper.sendBoardToClient(serverSocket, boardArray);
//
//
//                // we assume that the board is changed whenever a move is made now
//                DotsServer.boardChanged = true;
//            }
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("Game over!");
//    }
//}

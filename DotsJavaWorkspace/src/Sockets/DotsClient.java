//package Sockets;
//
//import AwesomeSockets.AwesomeClientSocket;
//import Dots.Dot;
//import Dots.DotsBoard;
//import Dots.Point;
//import Model.DotsLocks;
//
//import java.io.IOException;
//import java.util.Scanner;
//
///**
// * Created by JiaHao on 25/2/15.
// */
//public class w3DotsClient {
//    // start as false here, as
//
//
//    public static void main(String[] args) throws IOException, ClassNotFoundException {
//
//        final int PORT = 4321;
//        final String SERVER_ADDRESS = "localhost";
//
//        // Initialise Locks
//
//        DotsLocks dotsLocks = new DotsLocks();
//
//
//
//        // Initialize client socket
//        AwesomeClientSocket clientSocket = new AwesomeClientSocket(SERVER_ADDRESS, PORT);
//
//
//        // Init server listener thread
//        DotsClientServerListener dotsServerListener = new DotsClientServerListener(clientSocket, dotsLocks);
//        Thread listenerThread = new Thread(dotsServerListener);
//
//        // Init scanner thread
//        Scanner scanner = new Scanner(System.in);
//        DotsClientScannerListener dotsClientScannerListener = new DotsClientScannerListener(scanner, clientSocket, dotsLocks);
//        Thread scannerThread = new Thread(dotsClientScannerListener);
//
//
//        // start threads
//        listenerThread.start();
//        scannerThread.start();
////
//
//        // sleep first so client has time to get board from server
//        try {
//            Thread.sleep(100);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        while (dotsLocks.isGameRunning()) {
//
//            synchronized (dotsLocks) {
//                System.out.println(dotsLocks.isBoardChanged());
//                while (!dotsLocks.isBoardChanged()) {
//
//                    try {
//                        System.out.println("Waiting");
//                        dotsLocks.wait();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//
//
//                }
//
//                DotsBoard.printBoardWithIndex(dotsServerListener.getBoardArray());
//                dotsLocks.setBoardChanged(false);
//                System.out.println("HELLO");
////                    boardChanged = false;
//            }
//        }
//
//        clientSocket.closeClient();
//
//    }
//}
//
//class DotsClientScannerListener implements Runnable {
//
//    private final Scanner scanner;
//    private final AwesomeClientSocket clientSocket;
//    private final DotsLocks locks;
//
//    public DotsClientScannerListener(Scanner scanner, AwesomeClientSocket clientSocket, DotsLocks locks) {
//        this.scanner = scanner;
//        this.clientSocket = clientSocket;
//        this.locks = locks;
//    }
//
//    @Override
//    public void run() {
//
//        while (this.locks.isGameRunning()) {
//
//            Point inputPoint = DotsSocketHelper.getPointFromScanner(this.scanner);
//
//            try {
//                DotsSocketHelper.sendMoveToServer(this.clientSocket, inputPoint);
//
//                // TODO get response from server
//
////                System.out.println(this.clientSocket.readMessageLine());
//
//                // we assume that the board is changed whenever a move is made now
////                this.locks.setBoardChanged(true);
//
//
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
//
//        System.out.println("Game over!");
//    }
//}
//
//class DotsClientServerListener implements Runnable {
//
//    private Dot[][] boardArray;
//    private final AwesomeClientSocket clientSocket;
//    private final DotsLocks locks;
//
//
//    public DotsClientServerListener(AwesomeClientSocket clientSocket, DotsLocks locks) {
//        this.clientSocket = clientSocket;
//        this.locks = locks;
//    }
//
//    public Dot[][] getBoardArray() {
//        return this.boardArray;
//    }
//
//
//    @Override
//    public void run() {
//
//        while (this.locks.isGameRunning()) {
//
//            try {
//                // TODO protocol to read messages from server
//
//                // check for game over
////                if (message == game over) {
////                    DotsClient.gameIsRunning = false;
////                }
//
//                // check for board
//                this.boardArray = DotsSocketHelper.readBoardFromServer(this.clientSocket);
//                this.locks.setBoardChanged(true);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//                break;
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//                break;
//            }
//
//
//        }
//
//    }
//}
//

package Dots;

import Constants.DotsConstants;
import Model.DotsInteraction;
import Model.DotsInteractionStates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * For single player of how the game might work
 * Created by JiaHao on 10/2/15.
 */
public class TestDotsGame {
    public static void main(String[] args) {


        // Initialize

//        DotsBoard board = new DotsBoard(DotsConstants.BOARD_SIZE);
//        System.out.println(board);
//        board.printWithIndex();
//        DotsLogic logic = new DotsLogic(board);

//        Scanner scanner = new Scanner(System.in);
//        while (true) {
//            System.out.println("Enter x");
//            String xString = scanner.nextLine();
//            System.out.println("Enter y");
//            String yString = scanner.nextLine();
//
//            Point startPoint = new Point(Integer.parseInt(xString), Integer.parseInt(yString));
//
//            System.out.println("Enter x");
//            xString = scanner.nextLine();
//            System.out.println("Enter y");
//            yString = scanner.nextLine();
//
//            Point endPoint = new Point(Integer.parseInt(xString), Integer.parseInt(yString));
//
//            System.out.println(logic.checkAdjacency(startPoint, endPoint));
//
//        }


        // Initialise the game
        DotsGame dotsGame = new DotsGame();

        // first print the generated board
        dotsGame.getDotsBoard().printWithIndex();

        // testing points that are adjacent and have the same color
        Point point0 = new Point(0, 1);
        Point point1 = new Point(1,1);
        Point point2 = new Point(1,2);
        Point point3 = new Point(1,3);
        Point point4 = new Point(0,3);

        int PLAYER_0 = 0;


        // Simulation of interactions
        DotsInteraction interaction0 = new DotsInteraction(PLAYER_0, DotsInteractionStates.TOUCH_DOWN, point0);
//        dotsGame.getDotsBoard().printWithIndex();
        System.out.println(dotsGame.doMove(interaction0));

        DotsInteraction interaction1 = new DotsInteraction(PLAYER_0, DotsInteractionStates.TOUCH_MOVE, point1);
        System.out.println(dotsGame.doMove(interaction1));

        DotsInteraction interaction2 = new DotsInteraction(PLAYER_0, DotsInteractionStates.TOUCH_MOVE, point2);
        System.out.println(dotsGame.doMove(interaction2));

        DotsInteraction interaction3 = new DotsInteraction(PLAYER_0, DotsInteractionStates.TOUCH_MOVE, point3);
        System.out.println(dotsGame.doMove(interaction3));

        DotsInteraction interaction4 = new DotsInteraction(PLAYER_0, DotsInteractionStates.TOUCH_UP, point4);
        System.out.println(dotsGame.doMove(interaction4));

        // print the final board after the interactions are complete
        dotsGame.getDotsBoard().printWithIndex();


    }
}

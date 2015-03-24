package Dots;

import Constants.DotsConstants;
import Model.DotsInteraction;
import Model.DotsInteractionStates;

import java.util.ArrayList;

/**
 * Created by JiaHao on 25/2/15.
 */
public class DotsGame {


    private final DotsBoard dotsBoard;
    private final DotsLogic dotsLogic;


    private ArrayList<Point>[] playerMoves;

    private boolean gameRunning;

    public DotsGame() {
        this.dotsBoard = new DotsBoard(DotsConstants.BOARD_SIZE);
        this.dotsLogic = new DotsLogic(this.dotsBoard);


        this.gameRunning = true;

        this.playerMoves = new ArrayList[DotsConstants.NO_OF_PLAYERS];

        for (int i = 0; i < DotsConstants.NO_OF_PLAYERS; i++) {
            this.playerMoves[i] = new ArrayList<Point>();
        }

    }


    public boolean isGameRunning() {
        return gameRunning;
    }

//    public synchronized Dot[][] getBoardArray() {
//        return this.dotsBoard.getBoardArray();
//    }

    public synchronized DotsBoard getDotsBoard() {
        return dotsBoard;
    }


//    public synchronized boolean doMove(int player, Point point) {
//        ArrayList<Point> playerPoints;
//
//        if (player == 0) {
//            playerPoints = player0Moves;
//        } else if (player == 1) {
//            playerPoints = player1Moves;
//        } else {
//            System.err.println("No such player");
//            return false;
//        }
//
//        boolean moveResult = this.dotsLogic.checkMove(playerPoints);
//
//        if (moveResult) {
//
//            playerPoints.add(point);
//        }
//
//        return moveResult;
//
//    }


    public synchronized boolean doMove(DotsInteraction interaction) {

        // gets details from the interaction
        int player = interaction.getPlayerId();
        Point point = interaction.getPoint();


        // if its a new touch down, recreate the array list
        if (interaction.getState() == DotsInteractionStates.TOUCH_DOWN) {

            this.playerMoves[player] = new ArrayList<Point>();
        }


        // we temporarily add the new point to the current list of points and perform a check for adjacency
        this.playerMoves[player].add(point);
        boolean moveResult = this.dotsLogic.checkMove(this.playerMoves[player]);


        // remove the point if it is invalid
        if (!moveResult) {

            this.playerMoves[player].remove(this.playerMoves[player].size()-1);
            return false;
        }

        if (interaction.getState() == DotsInteractionStates.TOUCH_UP) {

            this.dotsLogic.moveCompleted(this.playerMoves[player]);

        }



        return moveResult;
    }



}

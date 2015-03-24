package Dots;

import java.util.ArrayList;

/**
 * Created by JiaHao on 25/2/15.
 */
public class DotsGame {


    private final int BOARD_SIZE = 5;
    private final DotsBoard dotsBoard;
    private final DotsLogic dotsLogic;

    private ArrayList<Point> player0Moves;
    private ArrayList<Point> player1Moves;

    private boolean gameRunning;

    public DotsGame() {
        this.dotsBoard = new DotsBoard(BOARD_SIZE);
        this.dotsLogic = new DotsLogic(this.dotsBoard);

        this.player0Moves = new ArrayList<Point>();
        this.player1Moves = new ArrayList<Point>();

        this.gameRunning = true;

    }


    public boolean isGameRunning() {
        return gameRunning;
    }

    public synchronized Dot[][] getBoardArray() {
        return this.dotsBoard.getBoardArray();
    }

    public synchronized boolean doMove(int player, Point point) {
        ArrayList<Point> playerPoints;

        if (player == 0) {
            playerPoints = player0Moves;
        } else if (player == 1) {
            playerPoints = player1Moves;
        } else {
            System.err.println("No such player");
            return false;
        }

        boolean moveResult = this.dotsLogic.checkMove(playerPoints);

        if (moveResult) {

            playerPoints.add(point);
        }

        return moveResult;

    }


}

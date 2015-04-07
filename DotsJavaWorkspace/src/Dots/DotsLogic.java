package Dots;

import Constants.DotsConstants;
import Model.Locks.DotsLocks;

import java.util.ArrayList;

/**
 * A logical checker to perform operations on the board
 *
 * Created by JiaHao on 10/2/15.
 */
public class DotsLogic {

    private final DotsBoard board;
    private final DotsLocks dotsLocks;

    public DotsLogic(DotsBoard board, DotsLocks dotsLocks) {
        this.board = board;
        this.dotsLocks = dotsLocks;
    }

    /**
     * Takes an input move sequence and clear the dots if it is a valid move
     * @param inputMoves chronologically ordered arrayList of points
     * @return 0 if board does not need to be updated, or the number of points cleared
     */
    public int moveCompleted(ArrayList<DotsPoint> inputMoves) {

        int dotsCleared = 0;

        if (inputMoves.size() <= 1) {
            return 0;
        }


        boolean needToUpdateBoard = checkMove(inputMoves);


        if (needToUpdateBoard) {

            board.clearDots(inputMoves);

            dotsCleared = inputMoves.size();

            // Every time we update the board, perform a check for a remaining legal move
            // and update the locks
            dotsLocks.setGameRunning(this.legalMovePresent());
        }

        return dotsCleared;
    }


    /**
     * For checking horizontally and vertically adjacent dots only
     *
     * @param start
     * @param end
     * @return validity of adjacency only
     */
    private boolean checkAdjacency(DotsPoint start, DotsPoint end) {

        // If the points are the same, we return true right away
        if (start.compareWith(end)) {
            return true;
        }

        double x1 = start.x;
        double y1 = start.y;
        double x2 = end.x;
        double y2 = end.y;
        if (Math.sqrt(Math.pow(y1 - y2, 2) + Math.pow(x1 - x2, 2)) != 1) {
//            throw new UnsupportedOperationException("dots must be vertically or horizontally adjacent");
            System.err.println("Points not vertically or horizontally adjacent");
            return false;
        }

        Dot startDot = board.getElement(start);
        Dot endDot = board.getElement(end);

        return startDot.color == endDot.color;

    }

    /**
     * Checks if the point parameter is within the bounds of the board
     * @param point
     * @return true if point is inside bounds, false otherwise
     */
    private boolean inBoard(DotsPoint point) {

        boolean leftCheck = point.x >= 0;
        boolean rightCheck = point.x < DotsConstants.BOARD_SIZE;

        boolean topCheck = point.y >= 0;
        boolean bottomCheck = point.y < DotsConstants.BOARD_SIZE;

        return leftCheck && rightCheck && topCheck && bottomCheck;

    }

    /**
     * Takes an arrayList of points and checks for adjacency in colors for all points
     *
     * @param inputMoves chronologically ordered arrayList of points
     * @return validity of move
     */
    public boolean checkMove(ArrayList<DotsPoint> inputMoves) {

        System.out.println("DotsLogic/Checking moves: " + inputMoves);
        for (int i = 0; i < inputMoves.size() - 1; i++) {

            DotsPoint startDotsPoint = inputMoves.get(i);
            DotsPoint endDotsPoint = inputMoves.get(i + 1);

            if (!checkAdjacency(startDotsPoint, endDotsPoint)) {
                return false;
            }
        }

        return true;
    }


    /**
     * Does a check for a legal move
     * @return true if legal move is left, false otherwise
     */
    public boolean legalMovePresent() {

        for (int j = 0; j < DotsConstants.BOARD_SIZE; j++) {

            for (int i = 0; i < DotsConstants.BOARD_SIZE; i++) {

                DotsPoint firstPoint = new DotsPoint(i, j);
                DotsPoint secondPoint = new DotsPoint(i+1, j);

                // If either of the points are not in the board, ignore this
                if (!this.inBoard(firstPoint) || !this.inBoard(secondPoint)) {
                    continue;
                }

                if (checkAdjacency(firstPoint, secondPoint)) {
                    return true;
                }


            }
        }

        System.out.println("NO MOVES LEFT");

        return false;
    }

}

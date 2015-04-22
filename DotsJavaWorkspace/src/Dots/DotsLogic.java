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

    private ArrayList<DotsPoint> additionalPointsAffected;

    public DotsLogic(DotsBoard board, DotsLocks dotsLocks) {
        this.board = board;
        this.dotsLocks = dotsLocks;
        this.additionalPointsAffected = new ArrayList<DotsPoint>();
    }

    /**
     * Takes an input move sequence and clear the dots if it is a valid move
     * @param inputMoves chronologically ordered arrayList of points
     * @return 0 if board does not need to be updated, or the number of points cleared
     */
    public int moveCompleted(ArrayList<DotsPoint> inputMoves) {

        // refresh the additional points
        this.additionalPointsAffected = new ArrayList<DotsPoint>();

        int dotsCleared = 0;

        if (inputMoves.size() <= 1) {
            return 0;
        }


        boolean needToUpdateBoard = checkMove(inputMoves);

        if (needToUpdateBoard) {


            // copy the points to clear
            ArrayList<DotsPoint> pointsToClear = new ArrayList<DotsPoint>(inputMoves);

            // check for powerups
//            ArrayList<DotsPoint> bombPointsToClear = new ArrayList<DotsPoint>();

            int[] powerUpCount = new int[DotsPowerUpType.NO_OF_POWER_UPS];


            for (DotsPoint point : inputMoves) {
                Dot correspondingDot = this.board.getElement(point);

                if (correspondingDot.powerUp == DotsPowerUpType.BOMB) {
//                    bombPointsToClear.add(point);
                    powerUpCount[0]++;
                } else if (correspondingDot.powerUp == DotsPowerUpType.FREEZE) {
                    powerUpCount[1]++;
                }
            }

            // sets the powerup to the lock
            this.dotsLocks.setPowerUpCount(powerUpCount);

            // Commented out to disable bombs
//            ArrayList<DotsPoint> affectedPointsForBomb = getAffectedPointsForBombs(bombPointsToClear);
//            this.additionalPointsAffected.addAll(affectedPointsForBomb);
//            pointsToClear.addAll(affectedPointsForBomb);

            board.clearDots(pointsToClear);
            dotsCleared = pointsToClear.size();

            // Every time we update the board, perform a check for a remaining legal move
            // and update the locks
            dotsLocks.setGameRunning(this.legalMovePresent());
        }

        return dotsCleared;
    }

    /**
     *
     * @param bombPoints
     * @return does not include the bomb points, only extras
     */
    private ArrayList<DotsPoint> getAffectedPointsForBombs(ArrayList<DotsPoint> bombPoints) {


        ArrayList<DotsPoint> affectedPoints = new ArrayList<DotsPoint>();

        int[][] directions = new int[][]{
                new int[]{-1,-1}, new int[]{0,-1}, new int[]{1, -1},
                new int[]{-1, 0}, new int[]{1, 0},
                new int[]{-1, 1}, new int[]{0,1}, new int[]{1,1}
        };


        // generate up down left right and put it into tempstore
        ArrayList<DotsPoint> temporaryStore = new ArrayList<DotsPoint>();
        for (DotsPoint bombPoint: bombPoints) {

            for (int[] dir: directions) {

                DotsPoint transformedPoint = bombPoint.transform(dir);
                temporaryStore.add(transformedPoint);

            }
        }

        // check if points in temporaryStore are in the board, and if so add it to affected points
        for (DotsPoint point: temporaryStore) {
            if (checkPointInBoard(point)) {
                affectedPoints.add(point);
            }
        }

        return affectedPoints;
    }


    /**
     * Check if point is within the bounds of the board
     * @param point
     * @return
     */
    private boolean checkPointInBoard(DotsPoint point) {

        int boardSize = this.board.getBoardArray().length;


        if (point.x < 0) {
            // left
            return false;
        } else if (point.y < 0) {
            // top
            return false;
        } else if (point.x >= boardSize) {
            // right
            return false;
        } else if (point.y >= boardSize) {
            // bottom
            return false;
        }

        return true;
    }


    public ArrayList<DotsPoint> getAdditionalPointsAffected() {
        return additionalPointsAffected;
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

                // Horizontal check
                DotsPoint firstPoint = new DotsPoint(i, j);
                DotsPoint secondPoint = new DotsPoint(i+1, j);

                // Vertical check
                DotsPoint thirdPoint = new DotsPoint(i, j + 1);


                // If either of the points are not in the board, ignore this

                boolean horiTest = false;
                boolean vertTest = false;
                if (this.inBoard(secondPoint)) {

                    horiTest = this.checkAdjacency(firstPoint, secondPoint);

                }

                if (this.inBoard(thirdPoint)) {
                    vertTest= this.checkAdjacency(firstPoint, thirdPoint);
                }

                if (horiTest || vertTest) {
                    return true;
                }



            }
        }

        System.out.println("NO MOVES LEFT");
        this.board.printWithIndex();

        return false;
    }

}

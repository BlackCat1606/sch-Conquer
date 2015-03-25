package Dots;

import java.util.ArrayList;

/**
 * Created by JiaHao on 10/2/15.
 */
public class DotsLogic {

    private final DotsBoard board;

    private ArrayList<Point> currentTouches;

    public DotsLogic(DotsBoard board) {
        this.board = board;

    }

    /**
     * Takes an input move sequence and clear the dots if it is a valid move
     * @param inputMoves chronologically ordered arrayList of points
     * @return true if board needs to be updated
     */
    public boolean moveCompleted(ArrayList<Point> inputMoves) {

        if (inputMoves.size() <= 1) {
            return false;
        }

        boolean needToUpdateBoard = checkMove(inputMoves);


        if (needToUpdateBoard) {
            board.clearDots(inputMoves);
        }

        return needToUpdateBoard;
    }


    /**
     * For checking horizontally and vertically adjacent dots only
     *
     * @param start
     * @param end
     * @return validity of adjacency only
     */
    private boolean checkAdjacency(Point start, Point end) {

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
     * Takes an arrayList of points and checks for adjacency in colors for all points
     *
     * @param inputMoves chronologically ordered arrayList of points
     * @return validity of move
     */
    public boolean checkMove(ArrayList<Point> inputMoves) {

        System.out.println(inputMoves);
        for (int i = 0; i < inputMoves.size() - 1; i++) {

            Point startPoint = inputMoves.get(i);
            Point endPoint = inputMoves.get(i + 1);

            if (!checkAdjacency(startPoint, endPoint)) {
                return false;
            }
        }

        return true;
    }

}

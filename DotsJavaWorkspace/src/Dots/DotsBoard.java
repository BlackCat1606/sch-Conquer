package Dots;

import Model.Locks.DotsLocks;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A wrapper for a 2D array representing the board, with some helper functions
 *
 * Created by JiaHao on 10/2/15.
 */
public class DotsBoard implements Serializable {

    private Dot[][] boardArray;
//    private Dot[][] boardArray = {
//            {new Dot(DotColor.RED), new Dot(DotColor.YELLOW), new Dot(DotColor.BLUE), new Dot(DotColor.RED), new Dot(DotColor.BLUE), new Dot(DotColor.RED)},
//            {new Dot(DotColor.BLUE), new Dot(DotColor.BLUE), new Dot(DotColor.RED), new Dot(DotColor.GREEN), new Dot(DotColor.GREEN), new Dot(DotColor.YELLOW)},
//            {new Dot(DotColor.GREEN), new Dot(DotColor.BLUE), new Dot(DotColor.BLUE), new Dot(DotColor.RED), new Dot(DotColor.GREEN), new Dot(DotColor.RED)},
//            {new Dot(DotColor.BLUE), new Dot(DotColor.BLUE), new Dot(DotColor.GREEN), new Dot(DotColor.BLUE), new Dot(DotColor.RED), new Dot(DotColor.RED)},
//            {new Dot(DotColor.GREEN), new Dot(DotColor.BLUE), new Dot(DotColor.BLUE), new Dot(DotColor.BLUE), new Dot(DotColor.GREEN), new Dot(DotColor.BLUE)},
//            {new Dot(DotColor.BLUE), new Dot(DotColor.BLUE), new Dot(DotColor.GREEN), new Dot(DotColor.RED), new Dot(DotColor.RED), new Dot(DotColor.GREEN)},
//    };

    // Dotslocks is used for setChangedDots, to update the arrayList if dots have been cleared
    private final DotsLocks dotsLocks;

    /**
     * Designated initialize that sets up a n x n board
     *
     * @param n rows and columns to have
     */
    public DotsBoard(int n, DotsLocks dotsLocks) {
        // Uncomment this to initialize board properly
        this.dotsLocks = dotsLocks;
        initializeBoard(n);


    }


    /**
     * Randomly assigns different colored dots to board
     *
     * @param n number of rows and columns to have
     */
    private void initializeBoard(int n) {
        ArrayList<DotsPoint> initialPoints = new ArrayList<DotsPoint>();

        boardArray = new Dot[n][n];

        for (int i = 0; i < boardArray.length; i++) {
            Dot[] currentRow = boardArray[i];

            for (int j = 0; j < currentRow.length; j++) {
                Dot createdDot = new Dot();
                currentRow[j] = createdDot;

                // add the point to the lock
                DotsPoint currentPoint = new DotsPoint(j, i, createdDot.color, createdDot.powerUp);
                initialPoints.add(currentPoint);


            }
        }

        // initialise the lock
        this.dotsLocks.setChangedDots(initialPoints);
    }

    /**
     * Gets a dot at a point
     *
     * @param dotsPoint integer array [x, y]
     * @return
     */
    public Dot getElement(DotsPoint dotsPoint) {

        return boardArray[dotsPoint.y][dotsPoint.x];
    }


    private Dot[][] getBoard() {
        return boardArray;
    }

    /**
     * Method to clear and cascade new dots down when an arrayList of points is entered
     * <p/>
     * Clearing of dots will trigger an update of the dotsLocks for the dots which need updating
     *
     * @param dotsPointList points to be removed from the boardArray. Does not check for validity of move, should have
     *                      already been in DotsLogic before calling this
     */
    public void clearDots(ArrayList<DotsPoint> dotsPointList) {
        // Mark points for deletion
        markDotsForDeletion(dotsPointList);

        for (int columnIndex = 0; columnIndex < boardArray.length; columnIndex++) {

            // Might want to do this in-place but its just more tedious

            // Create a temporary array of the current column
            Dot[] currentColumn = new Dot[boardArray.length];
            int counter = 0;
            for (int i = boardArray.length - 1; i >= 0; i--) {

                currentColumn[counter] = boardArray[i][columnIndex];
                counter++;
            }

            // Get array without nulls
            Dot[] currentColumnNullsRemoved = new Dot[boardArray.length];
            counter = 0;
            for (Dot dot : currentColumn) {

                if (dot != null) {
                    currentColumnNullsRemoved[counter] = dot;
                    counter++;
                }
            }


            // fill in nulls with new randomised dots
            for (int i = 0; i < currentColumnNullsRemoved.length; i++) {
                if (currentColumnNullsRemoved[i] == null) {
                    currentColumnNullsRemoved[i] = new Dot();
                }
            }

            // Put in new column back into original board
            for (int i = 0; i < currentColumnNullsRemoved.length; i++) {
                boardArray[boardArray.length - 1 - i][columnIndex] = currentColumnNullsRemoved[i];
            }
        }

        // updates dotLocks with the corresponding changed dots
        this.updateChangedPoints(dotsPointList);

    }

    /**
     * Mark dots for deletion with null
     *
     * @param dotsPointList list of points to mark for deletion
     */
    private void markDotsForDeletion(ArrayList<DotsPoint> dotsPointList) {
        for (DotsPoint dotsPoint : dotsPointList) {
            setElement(dotsPoint, null);
        }
    }

    /**
     * Set elements at a point to a dot. For use with marking for deletion
     *
     * @param dotsPoint
     * @param dot
     */
    private void setElement(DotsPoint dotsPoint, Dot dot) {
        boardArray[dotsPoint.y][dotsPoint.x] = dot;
    }

    /**
     * Based on the input dots which are cleared, this method creates and populates an ArrayList of affected dots,
     * namely dots which are cleared and those which are above them, and puts it in the dotsLocks container
     *
     * @param clearedPoints points which are cleared from interaction or whatnot
     */
    private void updateChangedPoints(ArrayList<DotsPoint> clearedPoints) {

        HashMap<Integer, Integer> affectedMap = new HashMap<Integer, Integer>();

        // find affected columns

        // This loop iterates through all cleared points and finds the position of the dot which is the closest
        // to the bottom of the board for each column
        for (DotsPoint point : clearedPoints) {

            if (affectedMap.get(point.x) == null) {
                affectedMap.put(point.x, point.y);
            } else {

                if (affectedMap.get(point.x) < point.y) {
                    affectedMap.put(point.x, point.y);
                }
            }
        }

        ArrayList<DotsPoint> pointsNeedingUpdate = new ArrayList<DotsPoint>();

        // for each column in map
        for (Map.Entry<Integer, Integer> entry : affectedMap.entrySet()) {
            // the column
            int xIndex = entry.getKey();

            // the largest y value (closest to bottom of the board) of the element in that column
            int yMax = entry.getValue();

            // iterate the rows from top to bottom
            for (int i = 0; i < yMax + 1; i++) {

                DotColor color = this.getBoard()[i][xIndex].color;
                DotPowerUpState powerUp = this.getBoard()[i][xIndex].powerUp;

                System.out.println(powerUp);
                DotsPoint pointToAdd = new DotsPoint(xIndex, i, color, powerUp);
                pointsNeedingUpdate.add(pointToAdd);

            }
        }

        // updates the dotsLocks with the points
        this.dotsLocks.setChangedDots(pointsNeedingUpdate);
    }


    /**
     * Nice method to return the board as a string
     *
     * @return
     */
    @Override
    public String toString() {
//        return Arrays.deepToString(boardArray);
        String result = "";
        for (Dot[] currentRow : boardArray) {
            String row = "";
            for (Dot dot : currentRow) {
                if (dot == null) {
                    row += 0 + " ";
                } else {
                    row += dot + " ";
                }
            }

            result += row + "\n";
        }
        return result;

    }


    /**
     * Static Method to print board nicely from other classes
     *
     * @param inputBoard Dot[][] array
     */
    public static void printBoardWithIndex(Dot[][] inputBoard) {

        String[][] printArray = new String[inputBoard.length + 1][inputBoard.length + 1];

        for (int i = 0; i < printArray.length; i++) {
            int newI = i;
            if (i != 0) {
                newI--;
            }
            printArray[0][i] = Integer.toString(newI);
        }

        for (int i = 0; i < printArray.length; i++) {
            int newI = i;
            if (i != 0) {
                newI--;
            }
            printArray[i][0] = Integer.toString(newI);
        }

        for (int i = 1; i < printArray.length; i++) {
            for (int j = 1; j < printArray.length; j++) {
                printArray[i][j] = inputBoard[i - 1][j - 1].toString();
            }
        }

        // Duplicated method from toString
        String result = "";
        for (String[] currentRow : printArray) {
            String row = "";
            for (String string : currentRow) {
                if (string == null) {
                    row += 0 + " ";
                } else {
                    row += string + " ";
                }
            }

            result += row + "\n";
        }

        System.out.println(result);
    }

    /**
     * Even nicer method to print the board with index numbers
     */
    public void printWithIndex() {
        printBoardWithIndex(this.boardArray);
    }

    /**
     * Makes a copy of the current board and returns it
     *
     * @return
     */
    public Dot[][] getBoardArray() {

        return this.copy2dArray(this.boardArray);
    }


    private Dot[][] copy2dArray(Dot[][] input) {

        Dot[][] copy = new Dot[input.length][input[0].length];

        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[0].length; j++) {
                copy[i][j] = input[i][j];
            }
        }

        return copy;
    }
}
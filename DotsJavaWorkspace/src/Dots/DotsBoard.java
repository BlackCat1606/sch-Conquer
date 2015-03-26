package Dots;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A wrapper for a 2D array representing the board, with some helper functions
 *
 * Created by JiaHao on 10/2/15.
 */
public class DotsBoard implements Serializable {

    // TODO swap comments here to use default randomised board
    // private Dot[][] boardArray;
    private Dot[][] boardArray = {
            {new Dot(DotColor.RED), new Dot(DotColor.YELLOW), new Dot(DotColor.BLUE), new Dot(DotColor.RED), new Dot(DotColor.BLUE), new Dot(DotColor.RED)},
            {new Dot(DotColor.BLUE), new Dot(DotColor.BLUE), new Dot(DotColor.RED), new Dot(DotColor.GREEN), new Dot(DotColor.GREEN), new Dot(DotColor.YELLOW)},
            {new Dot(DotColor.GREEN), new Dot(DotColor.BLUE), new Dot(DotColor.BLUE), new Dot(DotColor.RED), new Dot(DotColor.GREEN), new Dot(DotColor.RED)},
            {new Dot(DotColor.BLUE), new Dot(DotColor.BLUE), new Dot(DotColor.GREEN), new Dot(DotColor.BLUE), new Dot(DotColor.RED), new Dot(DotColor.RED)},
            {new Dot(DotColor.GREEN), new Dot(DotColor.BLUE), new Dot(DotColor.BLUE), new Dot(DotColor.BLUE), new Dot(DotColor.GREEN), new Dot(DotColor.BLUE)},
            {new Dot(DotColor.BLUE), new Dot(DotColor.BLUE), new Dot(DotColor.GREEN), new Dot(DotColor.RED), new Dot(DotColor.RED), new Dot(DotColor.GREEN)},
    };

    /**
     * Designated initialize that sets up a n x n board
     *
     * @param n rows and columns to have
     */
    public DotsBoard(int n) {
        // Uncomment this to initialize board properly
//        initializeBoard(n);
    }

    /**
     * Randomly assigns different colored dots to board
     *
     * @param n number of rows and columns to have
     */
    private void initializeBoard(int n) {
        boardArray = new Dot[n][n];
        for (Dot[] currentRow : boardArray) {
            for (int j = 0; j < currentRow.length; j++) {
                currentRow[j] = new Dot();
            }

        }

    }

    /**
     * Gets a dot at a point
     *
     * @param point integer array [x, y]
     * @return
     */
    public Dot getElement(Point point) {

        return boardArray[point.y][point.x];
    }


    private Dot[][] getBoard() {
        return boardArray;
    }

    /**
     * Method to clear and cascade new dots down when an arrayList of points is entered
     *
     * @param pointList points to be removed from the boardArray. Does not check for validity of move, should have
     *                  already been in DotsLogic before calling this
     */
    public void clearDots(ArrayList<Point> pointList) {
        // Mark points for deletion
        markDotsForDeletion(pointList);

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

    }

    /**
     * Mark dots for deletion with null
     *
     * @param pointList list of points to mark for deletion
     */
    private void markDotsForDeletion(ArrayList<Point> pointList) {
        for (Point point : pointList) {
            setElement(point, null);
        }

        System.out.println("Points marked for deletion");
        System.out.println(this.toString());
    }

    /**
     * Set elements at a point to a dot. For use with marking for deletion
     *
     * @param point
     * @param dot
     */
    private void setElement(Point point, Dot dot) {
        boardArray[point.y][point.x] = dot;
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

    // TODO find out how to generalise this
//    private String formattedForPrinting(Class[][] inpArray, Class type) {
//        String result = "";
//        for (type[] currentRow : boardArray) {
//            String row = "";
//            for (Dot dot : currentRow) {
//                if (dot == null) {
//                    row += 0 + " ";
//                } else {
//                    row += dot + " ";
//                }
//            }
//
//            result += row + "\n";
//        }
//        return result;
//    }
}

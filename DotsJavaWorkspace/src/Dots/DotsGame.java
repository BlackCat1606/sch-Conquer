package Dots;

import Constants.DotsConstants;
import Model.Interaction.DotsInteraction;
import Model.Interaction.DotsInteractionStates;
import Model.Locks.DotsLocks;

import java.util.ArrayList;

/**
 * The general dots game, that wraps the board, logic and locks together
 *
 * Stores information about the points that are held by each player
 *
 * Created by JiaHao on 25/2/15.
 */
public class DotsGame {


    private final DotsBoard dotsBoard;
    private final DotsLogic dotsLogic;
    private final DotsLocks dotsLocks;



    private final int[] score;

    private ArrayList<DotsPoint>[] playerMoves;



    public DotsGame() {
        this.dotsBoard = new DotsBoard(DotsConstants.BOARD_SIZE);



        this.dotsLocks = new DotsLocks();
        this.dotsLogic = new DotsLogic(this.dotsBoard, this.dotsLocks);

        this.score = new int[DotsConstants.NO_OF_PLAYERS];
        this.playerMoves = new ArrayList[DotsConstants.NO_OF_PLAYERS];

        for (int i = 0; i < DotsConstants.NO_OF_PLAYERS; i++) {
            this.playerMoves[i] = new ArrayList<DotsPoint>();
        }

    }

    public boolean isGameRunning() {
        return this.dotsLocks.isGameRunning();
    }

    public synchronized DotsBoard getDotsBoard() {
        return dotsBoard;
    }

    /**
     * Returns the lock that is used for tracking the game state
     * @return
     */
    public DotsLocks getGameLocks() {
        return dotsLocks;
    }



    /**
     * The primary method that we will call on our game, to update the game based on
     * an input DotsInteraction
     *
     * In this method, we will also check for a change for the board, when the interaction is touchUp
     * and certain dots on the screen need to be cleared.
     *
     * If so, this method will notify the dotsLock, which will in turn awaken the main thread to update
     * the screen
     *
     * @param interaction touch interaction
     * @return true if valid, false if invalid
     */
    public synchronized boolean doMove(DotsInteraction interaction) {

        // Todo fix touch below reserved dot as well
        // if conflict detected

        boolean conflictDetected = conflictIsDetected(interaction);

        // gets details from the interaction
        int player = interaction.getPlayerId();
        DotsPoint dotsPoint = interaction.getDotsPoint();

        // if its a new touch down, recreate the array list
//        if (interaction.getState() == DotsInteractionStates.TOUCH_DOWN) {
//
//            this.playerMoves[player] = new ArrayList<DotsPoint>();
//        }

        boolean moveResult = false;

        if (!conflictDetected) {
            // Check first to determine if the point has already been recorded
            boolean pointAlreadyRecorded = false;
            for (DotsPoint storedPoints : this.playerMoves[player]) {

                if (storedPoints.compareWith(dotsPoint)) {
                    pointAlreadyRecorded = true;
                }
            }

            // If the point has not been added previously, we add it into the list of points
            if (!pointAlreadyRecorded) {
                this.playerMoves[player].add(dotsPoint);
            }


            // perform a check for adjacency with the new points
            moveResult = this.dotsLogic.checkMove(this.playerMoves[player]);
            System.out.println("MoveResult: " + moveResult);
            // remove the point if it is invalid, so this.playerMoves[player] always contains valid moves
            if (!moveResult) {

                this.playerMoves[player].remove(this.playerMoves[player].size()-1);
            }
        }



        boolean needToUpdateBoard = false;

        if (interaction.getState() == DotsInteractionStates.TOUCH_UP) {

            int noOfDotsCleared = this.dotsLogic.moveCompleted(this.playerMoves[player]);
            this.scoreChanged(player, noOfDotsCleared);

            System.out.println("Dots cleared: " + noOfDotsCleared);
            if (noOfDotsCleared != 0) {
                needToUpdateBoard = true;
//            needToUpdateBoard = this.dotsLogic.moveCompleted(this.playerMoves[player]);
            }

            /**
             * A touchUp is always a valid move, so even if there is no need to update the board, this interaction
             * will have to be true.
             *
             * This prevents problems when you select one dot and touch up on a adjacent dot of a different color
             * that is next to it.
             */
            moveResult = true;

            // if the board is changed, notify lock
            if (needToUpdateBoard) {

                // TODO We no longer use wait/notify, might want to remove the synchronized
                synchronized (this.dotsLocks) {
                    this.dotsLocks.setBoardChanged(true);
                    this.dotsLocks.notifyAll();
                }

            }

            // After a touch up, clear the stored moves
            this.playerMoves[player] = new ArrayList<DotsPoint>();

        }

        /**
         * Move result tells us the validity of the touched move
         * Case 1: valid touch adjacent to previous points, board no need to update: return moveResult = true
         * Case 2: valid touch adjacent to previous points, board needs to update: return moveResult || needToUpdateBoard = true
         * Case 3: invalid touch not adjacent to previous points, board no need to update: return moveResult = false
         * Case 4: invalid touch not adjacent to previous points, but touch up and board needs to update with previous points: return moveResult || needToUpdateBoard = true
         */
        return moveResult || needToUpdateBoard;
    }

    /**
     * General method to check for conflicts in the same point selected
     * and when points are selected above points that can be cleared
     * @param dotsInteraction
     * @return
     */
    private boolean conflictIsDetected(DotsInteraction dotsInteraction) {

        boolean samePointConflict = this.samePointConflict(dotsInteraction);
        boolean cascadingConflict = this.cascadingConflict(dotsInteraction);

        return samePointConflict || cascadingConflict;

    }

    /**
     * Helper method to check for an interaction that selects points
     * which are above the points another player has selected
     * @param dotsInteraction interaction of a particular player
     * @return false if no conflict, true if there is a conflict
     */
    private boolean cascadingConflict(DotsInteraction dotsInteraction) {

        DotsPoint interactionDotsPoint = dotsInteraction.getDotsPoint();

        ArrayList<DotsPoint> otherPlayerDotsPoints = this.getOtherPlayerPoints(dotsInteraction.getPlayerId());

        for (DotsPoint otherPlayerDotsPoint : otherPlayerDotsPoints) {

            // same column
            if (otherPlayerDotsPoint.x == interactionDotsPoint.x) {

                // interaction point is above the other player point
                if (otherPlayerDotsPoint.y > interactionDotsPoint.y) {
                    System.out.println("DotsGame: Cascading conflict detected");
                    return true;
                }
            }
        }

        return false;

    }

    /**
     * This method will check for any conflicts in touches between players.
     * I.e. if player0 is holding on to a point, player1 will not be allowed to select that point
     * @param dotsInteraction interaction of a particular player
     * @return false if no conflict, true if there is a conflict
     */
    private boolean samePointConflict(DotsInteraction dotsInteraction) {

        ArrayList<DotsPoint> otherPlayerDotsPoints = this.getOtherPlayerPoints(dotsInteraction.getPlayerId());

        // null if no such player
        if (!(otherPlayerDotsPoints == null)) {

            for (DotsPoint otherPlayerDotsPoint : otherPlayerDotsPoints) {

                if (otherPlayerDotsPoint.compareWith(dotsInteraction.getDotsPoint())) {
                    System.out.println("DotsGame: Same point conflict detected");
                    return true;
                }

            }

        }


        return false;
    }

    /**
     * Returns an arrayList of points held by the other player
     * @param player current player
     * @return
     */
    private ArrayList<DotsPoint> getOtherPlayerPoints(int player) {
        ArrayList<DotsPoint> otherPlayerDotsPoints;

        if (player == 0) {

            otherPlayerDotsPoints = this.playerMoves[1];
        } else if (player == 1) {
            otherPlayerDotsPoints = this.playerMoves[0];
        } else {
            System.err.println("No such player");
            return null;
        }
        return otherPlayerDotsPoints;

    }


    //TODO Game over check
    // Time limit, score, no more valid moves
    private void scoreChanged(int playerId, int noOfDotsCleared) {

        if (noOfDotsCleared != 0) {

            this.dotsLocks.setScoreNeedingUpdate(true);
            this.score[playerId] += scoreFromNoOfDotsCleared(noOfDotsCleared);
        }

    }

    /**
     * Maps the number of dots cleared to a score
     * @return
     */
    private int scoreFromNoOfDotsCleared(int noOfDotsCleared) {



        // Sum of natural numbers
        // 1 + 2 + ... noOfDotsCleared
        return noOfDotsCleared * (noOfDotsCleared + 1) / 2;

    }

    /**
     * Escape to get a copy of the score
     * @return
     */
    public int[] getScore() {
        int[] copiedArray = new int[this.score.length];
        System.arraycopy(this.score, 0, copiedArray, 0, this.score.length);
        return copiedArray;
    }


}

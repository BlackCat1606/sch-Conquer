package Dots;

import Constants.DotsConstants;
import Model.DotsInteraction;
import Model.DotsInteractionStates;
import Model.DotsLocks;

import java.util.ArrayList;

/**
 * Created by JiaHao on 25/2/15.
 */
public class DotsGame {


    private final DotsBoard dotsBoard;
    private final DotsLogic dotsLogic;

    private final DotsLocks dotsLocks;



    private ArrayList<Point>[] playerMoves;


    public DotsGame() {
        this.dotsBoard = new DotsBoard(DotsConstants.BOARD_SIZE);
        this.dotsLogic = new DotsLogic(this.dotsBoard);


        this.dotsLocks = new DotsLocks();


        this.playerMoves = new ArrayList[DotsConstants.NO_OF_PLAYERS];

        for (int i = 0; i < DotsConstants.NO_OF_PLAYERS; i++) {
            this.playerMoves[i] = new ArrayList<Point>();
        }

    }


    public boolean isGameRunning() {
        return this.dotsLocks.isGameRunning();
    }

//    public synchronized Dot[][] getBoardArray() {
//        return this.dotsBoard.getBoardArray();
//    }

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
     * The main method that we will call on our game, to update the game based on
     * an input DotsInteraction
     *
     * In this method, we will also check for a change for the board, when the interaction is touchUp
     * and certain dots on the screen need to be cleared.
     *
     * If so, this method will notify the dotsLock, which will in turn awaken the main thread to update
     * the screen
     *
     * @param interaction
     * @return
     */
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

            boolean needToUpdateBoard = this.dotsLogic.moveCompleted(this.playerMoves[player]);


            // if the board is changed, notify lock
            if (needToUpdateBoard) {

                synchronized (this.dotsLocks) {

                    this.dotsLocks.setBoardChanged(true);
                    this.dotsLocks.notifyAll();
                }

            }


        }

        return moveResult;
    }



}

package Sockets;

import ListenerInterface.DotsBoardViewListener;
import ListenerInterface.DotsPlayerMovesListener;

import java.io.IOException;

/**
 * Parent class of Dots Server and Client, so that code duplication is eliminated.
 *
 * Primarily for instantiating of the server and client, and to throw an exception if no listeners are set for the server
 * and client
 *
 * Created by JiaHao on 31/3/15.
 */
public class DotsServerClientParent {


    private DotsPlayerMovesListener playerMovesListener = null;
    private DotsBoardViewListener boardViewListener = null;

    /**
     * Compulsory method to set a listener to display player interactions on the screen
     */
    public void setPlayerMovesListener(DotsPlayerMovesListener playerMovesListener) {
        this.playerMovesListener = playerMovesListener;
    }


    /**
     * Compulsory method to set a listener to update the board when it is changed
     */
    public void setBoardViewListener(DotsBoardViewListener boardViewListener) {
        this.boardViewListener = boardViewListener;
    }


    // Getters for subclass

    protected DotsPlayerMovesListener getPlayerMovesListener() {
        return playerMovesListener;
    }

    protected DotsBoardViewListener getBoardViewListener() {
        return boardViewListener;
    }

    /**
     * Helper method to check if both listeners are instantiated
     */
    private boolean listenerPresent() {

        if (this.getPlayerMovesListener() == null || this.getBoardViewListener() == null) {
            return false;
        } else {

            return true;
        }

    }

    public void start() throws IOException, InterruptedException, InstantiationException {

        if (!this.listenerPresent()) {
            System.err.println("Listeners not set up, exiting");
            throw new InstantiationException();
        }
    }


}

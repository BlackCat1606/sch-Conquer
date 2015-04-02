package Sockets;

import AndroidCallback.DotsAndroidCallback;
import Model.DotsInteraction;

import java.io.IOException;

/**
 * Parent class of Dots Server and Client, so that code duplication is eliminated.
 *
 * Primarily for instantiating of the server and client, and to throw an exception if no listeners are set for the server
 * and client
 *
 * Created by JiaHao on 31/3/15.
 */
public abstract class DotsServerClientParent {

    private DotsAndroidCallback androidCallback;

    /**
     * Compulsory method to set a listener to update the screen
     */
    public void setAndroidCallback(DotsAndroidCallback androidCallback) {
        this.androidCallback = androidCallback;
    }

    public DotsAndroidCallback getAndroidCallback() {
        return androidCallback;
    }

    /**
     * Call start to start the game and start threads to listen for messages
     */
    public void start() throws IOException, InterruptedException, InstantiationException {

        if (this.androidCallback == null) {
            System.err.println("Listener not set up, exiting");
            throw new InstantiationException();
        }
    }

    /**
     * Override this in the subclass
     * @param interaction
     * @throws IOException
     * @throws InterruptedException
     */
    public void doInteraction (DotsInteraction interaction) throws IOException, InterruptedException {
        System.err.println("Do interaction is not overwrote in parent class!");
    }

    /**
     * Manual method to trigger a screen update
     */
    public void updateBoard() throws IOException {

    }

}

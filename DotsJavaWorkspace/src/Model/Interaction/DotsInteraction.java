package Model.Interaction;

import Dots.DotsPoint;

import java.io.Serializable;

/**
 * A object to represent touches on the screen
 * When implementing in android, we will simply have to make a function that converts the detected touches
 * into this object, before dealing with it in the client/server
 *
 *
 * Created by JiaHao on 24/3/15.
 */
public class DotsInteraction implements Serializable {


    private final int playerId;
    private final DotsInteractionStates state;
    private final DotsPoint dotsPoint;

    /*
        On touchUp ,following variables are
        used for callback, onValidPlayerInteraction to distinguish between touches cancelled due to conflict and
        response for client.

        Cancelled touch path due to conflict:
        clearAll = true, animate = false

        Response for client
        clearAll = false, animate = false

        Default interactions
        clearAll = true, animate = true
     */


    private final boolean clearAll;
    private final boolean animate;

    /**
     * Default constructor used when sending unprocessed touches from the client to server,
     * or for the server to the DotsGame object
     * @param playerId
     * @param state
     * @param dotsPoint
     */
    public DotsInteraction(int playerId, DotsInteractionStates state, DotsPoint dotsPoint) {
        this(playerId, state, dotsPoint, false, true);
    }


    private DotsInteraction(int playerId, DotsInteractionStates state, DotsPoint dotsPoint, boolean clearAll, boolean animate) {
        this.playerId = playerId;
        this.state = state;
        this.dotsPoint = dotsPoint;
        this.clearAll = clearAll;
        this.animate = animate;
    }

    /**
     * Packages the player id and creates an interaction that indicates cancellation of touch path without animation
     *
     * To be used when a conflict is detected for a particular player, and there is a need to make touch paths of this
     * player invisible
     *
     * @param playerId
     * @return
     */
    public static DotsInteraction getConflictInteractionInstance(int playerId) {

        // clearAll = true
        // animate = false
        DotsPoint arbitraryPoint = DotsPoint.getArbitraryPoint();
        return new DotsInteraction(playerId, DotsInteractionStates.TOUCH_UP, arbitraryPoint, true, false);

    }


    /**
     * Packages an interaction into a response if it is an invalid move
     *
     * To be used when the server sends a response to the client, and the server has determined that that move is invalid,
     * so we send this interaction to cancel touch path already displayed on the client
     * @param interaction original interaction
     * @return
     */
    public static DotsInteraction getInvalidResponseInteractionInstance(DotsInteraction interaction) {

        // clearAll = false
        // animate = false
        // state = touchUp

        return new DotsInteraction(interaction.getPlayerId(), DotsInteractionStates.TOUCH_UP, interaction.getDotsPoint(), false, false);

    }

    public DotsInteractionStates getState() {
        return state;
    }

    public DotsPoint getDotsPoint() {
        return dotsPoint;
    }

    public int getPlayerId() {
        return playerId;
    }

    public boolean isClearAll() {
        return clearAll;
    }

    public boolean isAnimate() {
        return animate;
    }

    @Override
    public String toString() {
        return "Player: " + this.getPlayerId() + " State: " + this.getState() + " Point: " + dotsPoint.toString();
    }

    /**
     * Compares this with another dotsInteraction
     * @param otherInteraction
     * @return true if their fields are the same
     */
    public boolean compareWith(DotsInteraction otherInteraction) {

        // compare point
        if (!this.getDotsPoint().compareWith(otherInteraction.getDotsPoint())) {
            return false;
        }

        // compare state
        if (this.state != otherInteraction.getState()) {
            return false;
        }

        // compare player Id
        if (this.playerId != otherInteraction.getPlayerId()) {
            return false;
        }

        return true;
    }
}



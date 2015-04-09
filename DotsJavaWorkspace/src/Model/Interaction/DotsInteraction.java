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


    private final DotsInteractionStates state;

    private final DotsPoint dotsPoint;
    private final int playerId;

    public DotsInteraction(int playerId, DotsInteractionStates state, DotsPoint dotsPoint) {
        this.state = state;
        this.dotsPoint = dotsPoint;
        this.playerId = playerId;
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



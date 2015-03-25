package Model;

import Dots.Point;

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

    private final Point point;
    private final int playerId;

    public DotsInteraction(int playerId, DotsInteractionStates state, Point point) {
        this.state = state;
        this.point = point;
        this.playerId = playerId;
    }



    public DotsInteractionStates getState() {
        return state;
    }

    public Point getPoint() {
        return point;
    }

    public int getPlayerId() {
        return playerId;
    }

    @Override
    public String toString() {
        return "Player: " + this.getPlayerId() + " State: " + this.getState() + " Point: " + point.toString();
    }
}



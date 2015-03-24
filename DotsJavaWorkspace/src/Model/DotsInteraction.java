package Model;

import Dots.Point;

/**
 * Created by JiaHao on 24/3/15.
 */
public class DotsInteraction {


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
}



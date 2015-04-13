package Model.Messages;

import Dots.DotsBoard;
import Dots.DotsPoint;

import java.util.ArrayList;

/**
 * Container for a message that holds a DotsBoard
 * Created by JiaHao on 24/3/15.
 */
public class DotsMessageBoard implements DotsMessage {

//    private final DotsBoard dotsBoard;
//
//    public DotsMessageBoard(DotsBoard dotsBoard) {
//        this.dotsBoard = dotsBoard;
//    }
//
//    public DotsBoard getDotsBoard() {
//        return dotsBoard;
//    }

    private final ArrayList<DotsPoint> changedPoints;

    public DotsMessageBoard(ArrayList<DotsPoint> changedPoints) {
        this.changedPoints = changedPoints;
    }

    public ArrayList<DotsPoint> getChangedPoints() {
        return changedPoints;
    }
}

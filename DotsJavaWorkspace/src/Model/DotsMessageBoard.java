package Model;

import Dots.Dot;
import Dots.DotsBoard;

/**
 * Created by JiaHao on 24/3/15.
 */
public class DotsMessageBoard implements DotsMessage {


    private final DotsBoard dotsBoard;


    public DotsMessageBoard(DotsBoard dotsBoard) {
        this.dotsBoard = dotsBoard;
    }

    public DotsBoard getDotsBoard() {
        return dotsBoard;
    }

}

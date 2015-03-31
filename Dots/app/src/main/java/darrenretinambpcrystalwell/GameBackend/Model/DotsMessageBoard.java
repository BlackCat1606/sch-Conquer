package darrenretinambpcrystalwell.GameBackend.Model;

import darrenretinambpcrystalwell.GameBackend.Dots.DotsBoard;

/**
 * Container for a message that holds a DotsBoard
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

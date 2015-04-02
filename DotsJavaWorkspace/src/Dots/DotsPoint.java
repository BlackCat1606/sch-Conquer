package Dots;

import java.io.Serializable;

/**
 *
 * Object to track the index in a 2D array where a dot is positioned, NOT in PIXELS
 *
 * Created by JiaHao on 10/2/15.
 */
public class DotsPoint implements Serializable {

    public final int x;
    public final int y;

    /**
     *
     * @param x,y Units are not in pixels, but rather index in a 2D array
     */
    public DotsPoint(int x, int y) {
        this.x = x;
        this.y = y;

    }

    @Override
    public String toString() {

        return "(" + this.x + ", " + this.y + ")";
    }


    /**
     * Method to compare points
     * @param dotsPoint other point
     * @return true if the same coordinates, false otherwise
     */
    public boolean compareWith(DotsPoint dotsPoint) {

        if (this.x == dotsPoint.x && this.y == dotsPoint.y) {
            return true;
        } else {
            return false;
        }
    }
}

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

    private DotColor color;

    /**
     *
     * @param x,y Units are not in pixels, but rather index in a 2D array
     */
    public DotsPoint(int x, int y) {
        this.x = x;
        this.y = y;

    }

    public DotsPoint(int x, int y, DotColor color) {
        this.x = x;
        this.y = y;
        this.color = color;
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

    public DotColor getColor() {
        return color;
    }

    public void setColor(DotColor color) {
        this.color = color;
    }


    public static DotsPoint getArbitraryPoint() {

        int ARBITRARY_INDEX = 17;

        return new DotsPoint(ARBITRARY_INDEX, ARBITRARY_INDEX);
    }
}

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
    private DotPowerUpState powerUp;

    /**
     *
     * @param x,y Units are not in pixels, but rather index in a 2D array
     */
    public DotsPoint(int x, int y) {
        this.x = x;
        this.y = y;


    }

    // constructor with color only for sending to client
    public DotsPoint(int x, int y, DotColor color, DotPowerUpState powerUp) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.powerUp = powerUp;
    }

//    // copies the point
//    public DotsPoint(DotsPoint dotsPoint) {
//        this.x = dotsPoint.x;
//        this.y = dotsPoint.y;
//        this.color = dotsPoint.color;
//    }

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

    public DotPowerUpState getPowerUp() {
        return powerUp;
    }

    public void setColor(DotColor color) {
        this.color = color;
    }

    public DotsPoint transform(int[] transform) {

        return new DotsPoint(this.x + transform[0], this.y+ transform[1], this.color, this.powerUp);
    }


    public static DotsPoint getArbitraryPoint() {

        // Cannot set arbitrary index to be > board size, will throw null pointer exception
        // due to the way dotViews are accessed with index
        int ARBITRARY_INDEX = 0;

        return new DotsPoint(ARBITRARY_INDEX, ARBITRARY_INDEX);
    }
}

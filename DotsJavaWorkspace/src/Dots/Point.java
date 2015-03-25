package Dots;

import java.io.Serializable;

/**
 * Object to store a point nicely
 * Created by JiaHao on 10/2/15.
 */
public class Point implements Serializable {

    public final int x;
    public final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;

    }

    @Override
    public String toString() {

        return "(" + this.x + ", " + this.y + ")";
    }


    /**
     * Method to compare points
     * @param point
     * @return
     */
    public boolean compareWith(Point point) {

        if (this.x == point.x && this.y == point.y) {
            return true;
        } else {
            return false;
        }

    }
}

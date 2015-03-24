package Game;

import java.io.Serializable;

/**
 * Object to store a point nicely
 * Created by JiaHao on 10/2/15.
 */
public class Point implements Serializable {

    public final int x;
    public final int y;


    // 0 if normal move, 1 if endMove
    public final int state;

    public Point(int x, int y) {
        this(x, y, 0);
    }



    public Point(int x, int y, int state) {
        this.x = x;
        this.y = y;

        if (state > 1 || state < 0) {
            System.err.println("State needs to be 1 or 0");
            throw new NumberFormatException();
        }
        this.state = state;
    }



    @Override
    public String toString() {

        return "(" + this.x + ", " + this.y + ") state: " + this.state;
    }
}

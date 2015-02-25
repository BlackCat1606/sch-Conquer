package Dots;

import java.io.Serializable;

/**
 * Created by JiaHao on 10/2/15.
 */
public class Dot implements Serializable {

    public final DotColor color;


    /**
     * Randomly assigns a color to the dot
     */
    public Dot() {
        this.color = DotColor.randomColor();
    }

    /**
     * Creates a dot of a certain color
     * @param color DotColor Enum
     */
    public Dot(DotColor color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return color.toString();
    }
}

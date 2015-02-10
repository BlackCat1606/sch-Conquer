package Dots;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by JiaHao on 10/2/15.
 */
public enum DotColor {

    RED, BLUE, YELLOW, GREEN;

    /**
     * Override to allow nice single character printing
     * @return
     */
    @Override
    public String toString() {
        switch(this) {
            case RED: return "R";
            case BLUE: return "B";
            case YELLOW: return "Y";
            case GREEN: return "G";
            default: throw new IllegalArgumentException();
        }
    }



    private static final List<DotColor> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    /**
     * Randomly generates a color
     * @return
     */
    public static DotColor randomColor()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}

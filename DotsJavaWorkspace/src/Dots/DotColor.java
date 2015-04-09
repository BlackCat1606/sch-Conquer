package Dots;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Possible colors that can be used for the dots
 *
 * Created by JiaHao on 10/2/15.
 */
public enum DotColor {

    RED, BLUE, YELLOW, GREEN, PURPLE, PLAYER_0, PLAYER_1;

    /**
     * Override to allow nice single character printing
     * @return
     */
    @Override
    public String toString() {
        switch(this) {
            case RED:
                return "R";
            case BLUE:
                return "B";
            case YELLOW:
                return "Y";
            case GREEN:
                return "G";
            case PURPLE:
                return "P";
            case PLAYER_0:
                return "0";
            case PLAYER_1:
                return "1";
            default:
                throw new IllegalArgumentException();
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
        // we want to exclude player one and player 2 from being randomized
        return VALUES.get(RANDOM.nextInt(SIZE-2));
    }


    /**
     * Testing code for random color
     */
    public static void main(String[] args) {

        for (int i = 0; i < 100; i++) {

            DotColor color = DotColor.randomColor();
            System.out.println(color);

        }

        System.out.println(DotColor.PLAYER_0);

    }
}

package Dots;

import java.util.Random;

/**
 * Enum to keep power up types
 *
 * Created by JiaHao on 21/4/15.
 */
public enum DotsPowerUpType {
    NONE, BOMB, FREEZE;


    private static final Random RANDOM = new Random();

    public static DotsPowerUpType randomPowerUp() {

        // NONE, BOMB, FREEZE
        double[] probability = new double[] {0.9, 0.06, 0.04};

        double[] cumulative = new double[probability.length];

        double previous = 0;
        for (int i = 0; i < probability.length; i++) {
            double x = probability[i];

            cumulative[i] = x + previous;
            previous = cumulative[i];

            if (i == probability.length -1) {
                if (cumulative[i] != 1) {
                    System.err.println("Total power up probability not 1");
                }
            }
        }


        double randomized = RANDOM.nextDouble();

        int index = 0;
        for (int i = 0; i < cumulative.length; i++) {

            double currentLargest = cumulative[i];

            if (randomized < currentLargest) {
                index = i;
                break;
            }
        }

        if (index == 1) {
            return BOMB;
        } else if (index == 2) {
            return FREEZE;
        } else {
            return NONE;
        }


    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            System.out.println(randomPowerUp());
        }
    }

    // subtract one as we do not count the NONE type
    public static final int NO_OF_POWER_UPS = DotsPowerUpType.values().length - 1;
}

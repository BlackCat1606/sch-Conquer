package Dots;

import java.io.Serializable;

/**
 * A container for a colored dot on a board
 *
 * Created by JiaHao on 10/2/15.
 */
public class Dot implements Serializable {

    public final DotColor color;
    public final DotsPowerUpType powerUp;


    /**
     * Randomly assigns a color to the dot
     */
    public Dot() {
        this.color = DotColor.randomColor();
        this.powerUp = DotsPowerUpType.randomPowerUp();
    }
//
//    /**
//     * Creates a dot of a certain color
//     * @param color DotColor Enum
//     */
//    public Dot(DotColor color) {
//        this.color = color;
//    }
//


    @Override
    public String toString() {
        // todo remove debug

        String powerUp = "";
        if (this.powerUp == DotsPowerUpType.BOMB) {
            powerUp = ".";
        }

        return color.toString() + powerUp;
    }
}

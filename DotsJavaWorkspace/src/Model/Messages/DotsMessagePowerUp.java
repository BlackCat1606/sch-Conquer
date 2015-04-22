package Model.Messages;

import Dots.DotsPowerUp;
import Dots.DotsPowerUpType;

/**
 * Created by JiaHao on 22/4/15.
 */
public class DotsMessagePowerUp implements DotsMessage{

    private final DotsPowerUpType powerUp;
    private final long duration;

    public DotsMessagePowerUp(DotsPowerUpType powerUp, long duration) {
        this.powerUp = powerUp;
        this.duration = duration;
    }

    public DotsPowerUpType getPowerUp() {
        return powerUp;
    }

    public long getDuration() {
        return duration;
    }
}

package Sockets;

import AndroidCallback.DotsAndroidCallback;
import Dots.DotsPowerUp;
import Dots.DotsPowerUpState;
import Dots.DotsPowerUpType;
import Model.Messages.DotsMessagePowerUp;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by JiaHao on 22/4/15.
 */
public class DotsPowerUpTimedTask {

    private final TimerTask task;
    private final long duration;
    public DotsPowerUpTimedTask(final DotsPowerUpType powerUpType, long duration, final DotsAndroidCallback dotsAndroidCallback) {
        // create timed notification for callback

        this.task = new TimerTask() {
            @Override
            public void run() {
                try {
                    DotsPowerUp powerUpEnd = new DotsPowerUp(powerUpType, DotsPowerUpState.ENDED);
                    dotsAndroidCallback.onPowerUpReceived(powerUpEnd);

                } catch (NullPointerException e) {
                    // game ended before timer task could notify
                }
            }
        };

        this.duration = duration;

    }

    public void start() {
        Timer delayedTask = new Timer();
        delayedTask.schedule(this.task, this.duration);
    }
}

package Model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Enum of possible states for an interaction
 *
 * Created by JiaHao on 24/3/15.
 */
public enum DotsInteractionStates {

    TOUCH_DOWN, TOUCH_MOVE, TOUCH_UP;


    // Fields to randomly generate a state
    private static final List<DotsInteractionStates> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    /**
     * Randomly generates a state
     * @return
     */
    public static DotsInteractionStates randomState()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }

}


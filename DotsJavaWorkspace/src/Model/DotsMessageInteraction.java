package Model;

import Dots.DotsBoard;

/**
 * Created by JiaHao on 25/3/15.
 */
public class DotsMessageInteraction implements DotsMessage {



    private final DotsInteraction dotsInteraction;


    public DotsMessageInteraction(DotsInteraction dotsInteraction) {
        this.dotsInteraction = dotsInteraction;
    }

    public DotsInteraction getDotsInteraction() {
        return dotsInteraction;
    }
}

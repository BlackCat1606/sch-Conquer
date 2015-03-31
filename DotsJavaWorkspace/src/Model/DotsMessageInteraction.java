package Model;

/**
 * Container for a message that holds a DotsInteraction
 *
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

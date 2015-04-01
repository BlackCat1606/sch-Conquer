package ListenerInterface;

import Model.DotsInteraction;

/**
 * Interface to act as a listener to draw player moves on the screen
 *
 * Created by JiaHao on 31/3/15.
 */
public interface DotsPlayerMovesListener {

    public void onValidInteraction(DotsInteraction interaction);

}

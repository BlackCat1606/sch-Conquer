package AndroidCallback;

import Dots.DotsBoard;
import Model.DotsInteraction;

/**
 * Callback interface, to link android front end with the back end
 * Created by JiaHao on 1/4/15.
 *
 */
public interface DotsAndroidCallback {

    public void onValidPlayerInteraction(DotsInteraction interaction);

    public void onBoardChanged(DotsBoard board);

    public void onGameOver();


}

package AndroidCallback;

import Dots.DotsBoard;
import Model.Interaction.DotsInteraction;

/**
 * Callback interface, to link android front end with the back end
 * Created by JiaHao on 1/4/15.
 *
 */
public interface DotsAndroidCallback {

    void onValidPlayerInteraction(DotsInteraction interaction);

    void onBoardChanged(DotsBoard board);

    void onGameOver(int winningPlayerId, int[] finalScore);

    void onScoreUpdated(int[] score);

    void latencyChanged(long latency);

}

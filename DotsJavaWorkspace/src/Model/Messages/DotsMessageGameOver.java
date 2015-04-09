package Model.Messages;

/**
 *
 * Container for a message to tell client that the game is over
 * Created by JiaHao on 7/4/15.
 */
public class DotsMessageGameOver implements DotsMessage{

    private final int[] score;

    public DotsMessageGameOver(int[] score) {
        this.score = score;
    }

    public int[] getScore() {
        int[] copiedArray = new int[this.score.length];
        System.arraycopy(this.score, 0, copiedArray, 0, this.score.length);
        return copiedArray;
    }


}

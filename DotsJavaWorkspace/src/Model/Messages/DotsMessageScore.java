package Model.Messages;

/**
 * Message for the scores of both players
 * Created by JiaHao on 7/4/15.
 */
public class DotsMessageScore implements DotsMessage{

    private final int[] score;

    public DotsMessageScore(int[] score) {
        this.score = score;
    }

    public int[] getScore() {
        int[] copiedArray = new int[this.score.length];
        System.arraycopy(this.score, 0, copiedArray, 0, this.score.length);
        return copiedArray;
    }
}

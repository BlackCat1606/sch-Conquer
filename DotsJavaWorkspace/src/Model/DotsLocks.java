package Model;

/**
 * Created by JiaHao on 15/3/15.
 */
public class DotsLocks {

    private boolean boardChanged;
    private boolean gameRunning;

    public DotsLocks() {
        this.boardChanged = false;
        this.gameRunning = true;
    }

    public synchronized void setBoardChanged(boolean boardChanged) {

        this.boardChanged = boardChanged;
        this.notifyAll();

    }

    public synchronized boolean isGameRunning() {
        return gameRunning;
    }

    public synchronized void setGameRunning(boolean gameRunning) {
        this.gameRunning = gameRunning;
    }

    public synchronized boolean isBoardChanged() {

        return this.boardChanged;
    }




}

package Model.Locks;

/**
 * A thread-safe(?) object that holds locks, to track the state of the game
 *
 * Created by JiaHao on 15/3/15.
 */
public class DotsLocks {

    // TODO is this class redundant? can we just put it into the DotsGame?
    // Todo not sure why i synchronise methods as well
    private boolean boardChanged;
    private boolean gameRunning;

    private boolean scoreNeedingUpdate;

    public DotsLocks() {
        // init as true so that the board will print it in the first time it listens for change
        this.boardChanged = true;
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


    public synchronized boolean isScoreNeedingUpdate() {
        return scoreNeedingUpdate;
    }

    public synchronized void setScoreNeedingUpdate(boolean scoreNeedingUpdate) {
        this.scoreNeedingUpdate = scoreNeedingUpdate;
    }
}

package Latency;


/**
 * A stopwatch object used to make timings for latency
 *
 * By right, method calls should be synchronized, as DotsClient calls these methods across multiple threads. However, this is
 * merely a debugging object, and we do not want synchronized calls to slow down any updates in the main thread. Hence,
 * we leave the methods unsynchronized.
 *
 * Created by JiaHao on 5/4/15.
 */
public class RuntimeStopwatch {


    private long previousAverage;
    private long noOfReadingsTaken;

    private long startTime;

    public RuntimeStopwatch() {
    }

    /**
     * Starts the internal stopwatch
     * If it is called again before the stopMeasurement(), the previous start time is refreshed.
     */
    public void startMeasurement() {


        this.startTime = System.currentTimeMillis();

    }

    /**
     * @return the timing of the current measurement
     */
    public long stopMeasurement() {

        if (this.startTime != 0) {
            long timing = System.currentTimeMillis() - this.startTime;
            this.startTime = 0;

            this.previousAverage = (this.previousAverage * this.noOfReadingsTaken + timing)/(this.noOfReadingsTaken + 1);
            this.noOfReadingsTaken++;

            return timing;
        }

        return 0;
    }

    /**
     * Method to get the averaged run time.
     * @return Average runtime
     */
    public long getAverageRuntime() {
        return this.previousAverage;
    }

}

package Latency;

import java.util.ArrayList;

/**
 * Created by JiaHao on 5/4/15.
 */
public class RuntimeStopwatch {


    private double previousAverage;
    private double noOfReadingsTaken;

    private double startTime;

    public RuntimeStopwatch() {
    }

    public void startMeasurement() {

        if (this.startTime == 0) {
            this.startTime = System.currentTimeMillis();
        }
    }

    /**
     * @return the timing of the current measurement
     */
    public double stopMeasurement() {

        if (this.startTime != 0) {
            double timing = System.currentTimeMillis() - this.startTime;
            this.startTime = 0;

            this.previousAverage = (this.previousAverage * this.noOfReadingsTaken + timing)/(this.noOfReadingsTaken + 1);
            this.noOfReadingsTaken++;

            return timing;
        }

        return 0;
    }

    public double getAverageRuntime() {
        return this.previousAverage;
    }

}

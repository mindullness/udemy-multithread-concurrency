package race.condition;

public class MinMaxMetricsExercise {
    // udemy solution
    private volatile long min;
    private volatile long max;

    public MinMaxMetricsExercise() {
        this.min = Long.MIN_VALUE;
        this.max = Long.MAX_VALUE;
    }
    public void addSample(long newSample) {
        synchronized (this) {
            this.min = Math.min(newSample, min);
            this.max = Math.max(newSample, max);
        }
    }

    public long getMin() {
        return min;
    }

    public long getMax() {
        return max;
    }
}

package section9.atomic.quiz;

import java.util.concurrent.atomic.AtomicReference;

public class Metric {
    private static class InternalMetric {
        public long count = 0;
        public long sum = 0;
    }
    private final AtomicReference<InternalMetric> internalMetric = new AtomicReference<>(new InternalMetric());

    public void addSample(long sample) {
        InternalMetric currentState;
        InternalMetric newState;
        long sum;
        do {
            currentState = internalMetric.get();
            newState = new InternalMetric();
            sum = currentState.sum + sample;
            newState.sum = sum;
            newState.count = currentState.count + 1;
        } while (!internalMetric.compareAndSet(currentState, newState));
        System.out.println("Added:: " + sample+ ", Sum:: " + sum);
    }

    public double getAverage() {
        InternalMetric currentState;
        InternalMetric newState = new InternalMetric();
        ;
        double avg;
        do {
            currentState = internalMetric.get();
            avg = (double) currentState.sum / (currentState.count == 0 ? 1 : currentState.count);
        } while (!internalMetric.compareAndSet(currentState, newState));
        return avg;
    }
}

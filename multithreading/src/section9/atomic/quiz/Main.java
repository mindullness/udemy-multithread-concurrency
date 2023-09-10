package section9.atomic.quiz;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        final int NUM_OF_THREADS = 5;
        Random random = new Random();
        List<Thread> threads = new ArrayList<>();
        Metric metric = new Metric();

        for (int i = 0; i < NUM_OF_THREADS; i++) {
            EachThread thread = new EachThread(metric, random);
            thread.setDaemon(true);
            threads.add(thread);
            Emitter emitter = new Emitter(metric);
            emitter.setDaemon(true);
            threads.add(emitter);
        }
        threads.forEach(Thread::start);
        Thread.sleep(5000);

    }

    private static class Emitter extends Thread {
        private Metric metric;

        public Emitter(Metric metric) {
            this.metric = metric;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    sleep(500);
                    double result = metric.getAverage();
                    if (result != 0) {
                        System.out.println("=> Average:: " + String.format("%.2f", result));
                    }
                } catch (InterruptedException e) {

                }
            }
        }
    }

    private static class EachThread extends Thread {
        private Metric metric;
        private Random random;
        private final int MAX_SLEEP = 1000;

        public EachThread(Metric metric, Random random) {
            this.metric = metric;
            this.random = random;
        }

        @Override
        public void run() {
            while (true) {
                long start = System.currentTimeMillis();
                try {
                    sleep(random.nextInt(MAX_SLEEP));
                } catch (InterruptedException e) {

                }
                long end = System.currentTimeMillis();
                metric.addSample(end - start);
            }
        }
    }
}

package race.condition;

public class SharedClass {
    public static void main(String[] args) {
        Shared instance = new Shared();
        Thread thread1 = new Thread(() -> {
            while (true) {
                instance.increment();
                if (Thread.interrupted()) break;
                if (Thread.currentThread().isDaemon()) break;
            }
        });
        Thread thread2 = new Thread(() -> {
            while (true) {
                instance.increment();
                if (Thread.interrupted()) break;
                if (Thread.currentThread().isDaemon()) break;
            }
        });
        thread1.start();
        thread2.start();
        try {
            thread1.join(50);
            System.out.println("Counter:: " + instance.getCounter());
            thread2.join(10);
            System.out.println("Counter:: " + instance.getCounter());
            thread1.interrupt();
            thread2.interrupt();
        } catch (InterruptedException | IllegalThreadStateException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Counter:: " + instance.getCounter());
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> System.out.println("A critical error happened in thread:: " + t.getName()
                + ", the error is:: " + e.getMessage()));
    }

    public static class Shared {
        private int counter = 0;

        synchronized void increment() {
            counter++;
        }

        public int getCounter() {
            return counter;
        }
    }
}

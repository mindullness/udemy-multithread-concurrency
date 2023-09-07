package thread.creation.example.one;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(new TestThreadInterface());
        thread1.start();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // Code that will run in a new thread, scheduled by the OS
                System.out.println("We are now in thread " + Thread.currentThread().getName());
                System.out.println("Current thread priority is: " + Thread.currentThread().getPriority());
                throw new RuntimeException("Intentional Exception");
            }
        });

        thread.setName("New Worker Thread");
        thread.setPriority(Thread.MAX_PRIORITY);
        System.out.println("We are in thread: " + Thread.currentThread().getName() + " before starting a new thread");
        // instruct the JVM to create new thread and pass it to the OS
        // New Thread is created and passed to the OS by the JVM
        thread.start();
        // Thread.currentThread: get Thread Object of the current Thread
        System.out.println("We are in thread: " + Thread.currentThread().getName() + " after starting a new thread");
        /* sleep(): instruct the OS to NOT Schedule the current thread until that time passes.
            During that time, this thread is not consuming any CPU.
        */
        thread.setName("Misbehaving thread");
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println("A critical error happened in thread:: " + t.getName()
                + ", the error is:: " + e.getMessage());
            }
        });
        Thread.sleep(1000);
        System.out.println("AFTER EXCEPTION");
    }
}

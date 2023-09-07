package thread.interrupt.example;

import java.io.IOException;

public class Quiz3 {
    public static void main(String[] args) {
        Thread thread = new Thread(new WaitingForUserInput());
        Thread.currentThread().setName("InputWaitingThread");
        thread.start();
        thread.interrupt();
//        thread.setDaemon(true);
//        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
//            System.out.println("An exception was caught 2 " + e.getMessage());
//        });
//        Thread sleepThread = new Thread(new SleepingThread());
//        sleepThread.start();
//        sleepThread.interrupt();
    }
    private static class SleepingThread implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000000);
                } catch (InterruptedException e) {
    //                System.out.println("Interrupted Exception thrown at run()");
                    return;
                }
            }
        }
    }
    private static class WaitingForUserInput implements Runnable {

        @Override
        public void run() {
            try {
                while (true) {
                    char input = (char) System.in.read();
                    if (input == 'q') {
                        return;
                    }
                }
            } catch (IOException e) {
                System.out.println("An exception was caught " + e.getMessage());
            }
        }
    }
}

package thread.creation.example.two;

public class Quiz2 {
    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> System.out.println("I'm going for a walk"));
        thread1.setPriority(Thread.MAX_PRIORITY);
        Thread thread2 = new Thread(() -> System.out.println("I'm going to swim"));
        System.out.println(Thread.currentThread().getName());
        System.out.println("I'm going home");
        Thread taskThread = new Thread(new TaskThread());
        taskThread.start();
        thread1.start();
        thread2.start();
    }

    private static class TaskThread implements Runnable{

        @Override
        public void run() {
            System.out.println("This is TaskThread implement Runnable interface!");
        }
    }

}

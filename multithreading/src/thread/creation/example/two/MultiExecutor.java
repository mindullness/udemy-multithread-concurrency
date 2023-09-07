package thread.creation.example.two;

import java.util.List;

public class MultiExecutor {
    private List<Runnable> tasks;

    public MultiExecutor(List<Runnable> tasks) {
        this.tasks = tasks;
    }

    public void executeAll() {
//        for (Runnable thread : threads) {
//            Thread newThread = new Thread(thread);
//            newThread.start();
//        }
        this.tasks.forEach(t -> new Thread(t).start());
    }
}

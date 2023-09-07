package race.condition.deadlock;

public class Intersection {
    Object roadA = new Object();
    Object roadB = new Object();

    public void takeRoadA() {
        synchronized (roadA) {
            System.out.println("Road A is locked by thread:: " + Thread.currentThread().getName());
            synchronized (roadB) {
                System.out.println("Train is passing through road A");
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    System.out.println("Interrupted Exception thrown here");
                }
            }
        }
    }

    public void takeRoadB() {
        synchronized (roadA) {
            System.out.println("Road A is locked by thread:: " + Thread.currentThread().getName());
            synchronized (roadB) {
                System.out.println("Train is passing through road B");
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    System.out.println("Interrupted Exception thrown here");
                }
            }
        }
    }
}

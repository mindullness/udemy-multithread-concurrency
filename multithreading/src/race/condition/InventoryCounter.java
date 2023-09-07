package race.condition;

public class InventoryCounter {
    private int items = 0;
    private boolean isIncrementing;

    /*
        Second solution: Lock - create an object to synchronize on
            In this solution:
            If Thread A access method increment(),
            still another Thread like Thread B, at the same time is able
            to access method decrement()
         */
    public void increment() {
        synchronized (this) {
            isIncrementing = true;
            items++;
        }
    }

    public boolean isIncrementing() {
        synchronized (this) {
            return isIncrementing;
        }
    }

    public void decrement() {
        synchronized (this) {
            isIncrementing = false;
            items--;
        }
    }

    public int getItems() {
        synchronized (this) {
            return items;
        }
    }

        /*
    First solution: Monitor - Synchronized :: make the entire methods "synchronized"
     */
//    public synchronized void increment() {
//        items++;
//    }
//    public synchronized void decrement() {
//        items--;
//    }
//    public synchronized int getItems() {
//        return items;
//    }
}

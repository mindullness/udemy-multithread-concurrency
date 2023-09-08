package locks.semaphore;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DHLWareHouse {
    private Queue<Bottle> bottles;
    private Lock lock;

    public DHLWareHouse(int maxCapacity) {
        bottles = new ArrayDeque<>(maxCapacity);
        this.lock = new ReentrantLock(true);
    }

    public boolean addBottle(Bottle bottle) throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            if (lock.tryLock()) {
                try {
                    return this.bottles.add(bottle);
                } catch (IllegalStateException ex) {
                    System.out.println("Full capacity");
                    return false;
                } finally {
                    lock.unlock();
                }
            }
        }
        System.out.println("Cannot add now");
        return false;
    }

    public Bottle getBottle() {
        synchronized (this) {
            return this.bottles.poll();
        }
    }
}

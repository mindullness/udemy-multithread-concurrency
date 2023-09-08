package locks.semaphore;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class WareHouse {
    private final Queue<Bottle> bottles;
    private final ReentrantReadWriteLock lockObj;
    private static int max = 0;

    public WareHouse(int maxCapacity) {
        bottles = new ArrayDeque<>(maxCapacity);
        this.lockObj = new ReentrantReadWriteLock();
    }

    public boolean addBottle(Bottle bottle) throws InterruptedException {
        Lock lock = getWriteLock();
        for (int i = 0; i < 5; i++) {
            if (lock.tryLock()) {
                try {
                    max = Math.max(bottles.size() + 1, max);
                    return this.bottles.offer(bottle);
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

    private Lock getWriteLock() {
        return lockObj.writeLock();
    }

    private Lock getReadLock() {
        return lockObj.readLock();
    }

    public int getQuantity() {
        Lock lock = getReadLock();
        try {
            lock.lock();
            return this.bottles.size();
        } finally {
            lock.unlock();
        }
    }
    public int getMax() {
        Lock lock = getReadLock();
        try {
            lock.lock();
            return max;
        } finally {
            lock.unlock();
        }
    }
    public Bottle getBottle() {
        Lock lock = getWriteLock();
        try {
            lock.lock();
            return this.bottles.poll();
        } finally {
            lock.unlock();
        }
    }
}

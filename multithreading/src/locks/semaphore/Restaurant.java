package locks.semaphore;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Restaurant extends Thread {
    private Semaphore full;
    private Semaphore empty;
    private Lock lock = new ReentrantLock();
    private DHLWareHouse wareHouse;
    private int sold = 0;
    private volatile double revenue = 0;
    public Restaurant(Semaphore full, Semaphore empty, DHLWareHouse wareHouse) {
        this.full = full;
        this.empty = empty;
        this.wareHouse = wareHouse;
    }

    @Override
    public void run() {
        while (true) {
            try {
                full.acquire();
                this.selling();
                empty.release();
            } catch (InterruptedException e) {
                System.out.printf("\"%s\", has sold %d bottles of beer. Earned $%.2fUSD of revenue\n", this.getName(), this.getSold(), this.revenue);
                break;
            }
            if (Thread.currentThread().isInterrupted()) {
                break;
            }
        }
    }

    public void selling() throws InterruptedException {
        int timesToTry = 5;
//        Thread.sleep(1500);
        while (timesToTry >= 0) {
            if (lock.tryLock()) {
                try {
                    Bottle bottle = wareHouse.getBottle();
                    if (bottle == null) {

                        if (timesToTry == 0) {
                            System.out.println("Getting more beer, wait...");
                        }
                        timesToTry--;
                        continue;
                    }
                    System.out.println("Selling " + bottle);
                    increment(bottle);
                    System.out.println(this.getName() + " sold:: " + getSold());
                    break;
                } finally {
                    lock.unlock();
                }
            }
        }

    }

    public int getSold() {
        return sold;
    }

    private void increment(Bottle bottle) {
        Lock l = getLock();
        l.lock();
        this.sold += 1;
        this.revenue += bottle.getPrice();
        l.unlock();
    }

    private Lock getLock() {
        return this.lock;
    }
}

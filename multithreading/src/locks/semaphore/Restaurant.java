package locks.semaphore;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Restaurant extends Thread {
    private final Semaphore full;
    private final Semaphore empty;
    private final Lock lock = new ReentrantLock(true);
    private final WareHouse wareHouse;
    private int sold = 0;
    private volatile double revenue = 0;
    public Restaurant(Semaphore full, Semaphore empty, WareHouse wareHouse) {
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
                System.out.printf("\"%s\", has sold %d bottles of beer. Earned $%.2fUSD of revenue\n",
                        this.getName(), this.getSold(), this.getRevenue());
                break;
            }
            if (this.isInterrupted()) {
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
                        sleep(100);
                        continue;
                    }
                    System.out.println("Selling " + bottle);
                    increment(bottle);
                    System.out.println(this.getName() + " sold:: " + getSold());
                    sleep(200);
                    break;
                } finally {
                    lock.unlock();
                }
            }
            if (timesToTry == 0) {
                System.out.println("Getting more beer, wait...");
            }
            timesToTry--;
        }
    }

    public int getSold() {
        return sold;
    }

    private void increment(Bottle bottle) {
        double price = bottle.getPrice();
        Lock l = getLock();
        l.lock();
        this.sold += 1;
        revenue += price;
        l.unlock();
    }
    private double getRevenue(){
        return this.revenue;
    }
    private Lock getLock() {
        return this.lock;
    }
}

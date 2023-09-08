package locks.semaphore;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Brewery extends Thread {
    private Semaphore full;
    private Semaphore empty;
    private DHLWareHouse wareHouse;
    private Lock breweryLock = new ReentrantLock(true);

    public Brewery(Semaphore full, Semaphore empty, DHLWareHouse wareHouse) {
        this.full = full;
        this.empty = empty;
        this.wareHouse = wareHouse;
    }

    @Override
    public void run() {
        while (true) {
            Bottle bottle = null;
            try {
                bottle = produce();
                empty.acquire();
            } catch (InterruptedException e) {
                System.out.println("Producing interrupted");
                break;
            }
            breweryLock.lock();
            try {
                System.out.println(this.getName() + " is making beer ... 🍺");
                if (wareHouse.addBottle(bottle)) {
                    sleep(500);
                    System.out.println("Added more beer");
                } else {
                    System.out.println("Failed to add more beer");
                }
            } catch (InterruptedException e) {
                break;
            }
            breweryLock.unlock();
            full.release();
            if (Thread.currentThread().isInterrupted()) {
                break;
            }
        }
    }

    private Bottle produce() throws InterruptedException {
        synchronized (this) {
            Random random = new Random();
            int min = 10;
            int max = 50;
            int choice = random.nextInt(BeerType.values().length);
            BeerType t = BeerType.values()[choice];

            Bottle bottle = new Bottle();
            bottle.setName(t.getType());
            bottle.setIpa((random.nextInt(8)) + random.nextFloat());
            bottle.setPrice((Math.floor(Math.random() * (max - min + 1) + min)) + random.nextDouble());
            bottle.setSize(Arrays.asList(9, 12, 14).get(random.nextInt(3)));
            return bottle;
        }
    }
//    public List<Worker> getWorkers() {
//        return workers;
//    }
//
//    public void addWorker(Worker worker) {
//        Optional<Worker> workerObt = workers.stream().filter(w -> w.getId() == worker.getId()).findFirst();
//        if(workerObt.isPresent()) return;
//        this.workers.add(worker);
//    }
//    public boolean removeWorker(int id){
//        Optional<Worker> workerObt = workers.stream().filter(w -> w.getId() == id).findFirst();
//        if(workerObt.isEmpty()){
//            return false;
//        }
//        Worker worker = workerObt.get();
//        if (worker.isWorking()) {
//            return false;
//        }
//        return workers.remove(worker);
//    }
}

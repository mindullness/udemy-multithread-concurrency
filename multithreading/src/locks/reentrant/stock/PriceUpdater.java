package locks.reentrant.stock;

import java.util.Random;

public class PriceUpdater extends Thread {
    private PricesContainer pricesContainer;
    private Random random = new Random();
    public PriceUpdater(PricesContainer pricesContainer) {
        this.pricesContainer = pricesContainer;
    }

    @Override
    public void run() {
        while(true) {
            pricesContainer.getLockObject().lock();

            try {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) { }
                pricesContainer.setBitcoin(random.nextInt(20000));
                pricesContainer.setEther(random.nextInt(2000));
                pricesContainer.setLiteCoin(random.nextInt(500));
                pricesContainer.setBitcoinCash(random.nextInt(5000));
                pricesContainer.setRipple(random.nextDouble());
            } finally {
                pricesContainer.getLockObject().unlock();
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) { }
        }
    }
}

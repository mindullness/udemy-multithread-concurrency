package locks.rwreentrant;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    private static final int HIGHEST_PRICE = 1000;
    public static void main(String[] args) throws InterruptedException {
        InventoryDatabase inventoryDatabase = new InventoryDatabase();
        Random random = new Random();
        for (int i = 0; i < 100_000; i++) {
            inventoryDatabase.addItems(random.nextInt(HIGHEST_PRICE));
        }
        Thread writer = new Thread(() -> {
            while(true) {
                inventoryDatabase.addItems(random.nextInt(HIGHEST_PRICE));
                inventoryDatabase.removeItem(random.nextInt(HIGHEST_PRICE));

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) { }
            }
        });
        writer.setDaemon(true);
        writer.start();

        int numberOfReaderThreads = 7;
        List<Thread> readers = new ArrayList<>();
        for (int readerIndex = 0; readerIndex < numberOfReaderThreads; readerIndex++) {
            Thread reader = new Thread(() -> {
                for (int i = 0; i < 100_000; i++) {
                    int upperBoundPrice = random.nextInt(HIGHEST_PRICE);
                    int lowerBoundPrice = upperBoundPrice > 0 ? random.nextInt(upperBoundPrice) : 0;
                    inventoryDatabase.getNumberOfItemsPriceRange(lowerBoundPrice, upperBoundPrice);
                }
            });
            reader.setDaemon(true);
            readers.add(reader);
        }
        long start = System.currentTimeMillis();
        readers.forEach(Thread::start);
        for (Thread reader : readers) {
            reader.join();
        }
        long end = System.currentTimeMillis();
        System.out.printf("Reading took %d ms%n", end - start);
    }
}

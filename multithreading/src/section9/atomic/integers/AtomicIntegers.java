package section9.atomic.integers;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegers {
    public static void main(String[] args) throws InterruptedException {
        Inventory inventory = new Inventory();
        Provider provider = new Provider(inventory);
        Consumer consumer = new Consumer(inventory);
        provider.start();
        consumer.start();
        provider.join();
        consumer.join();

        System.out.printf("Inventory currently have %d items", inventory.getItems());
    }
    public static class Provider extends Thread {
        private Inventory inventory;
        public Provider(Inventory inventory) {
            this.inventory = inventory;
        }
        @Override
        public void run() {
            for (int i = 0; i < 10_002; i++) {
                inventory.increment();
            }
        }
    }
    public static class Consumer extends Thread {
        private Inventory inventory;
        public Consumer(Inventory inventory) {
            this.inventory = inventory;
        }
        @Override
        public void run() {
            for (int i = 0; i < 10_000; i++) {
                inventory.decrement();
            }
        }
    }
    private static class Inventory {
        private AtomicInteger items;

        public Inventory() {
            this.items = new AtomicInteger(0);
        }
        public void increment() {
            items.incrementAndGet();
        }
        public void decrement() {
            items.decrementAndGet();
        }
        public int getItems() {
            return items.get();
        }
    }
}

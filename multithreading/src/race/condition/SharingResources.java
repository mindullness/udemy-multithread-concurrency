package race.condition;

public class SharingResources {
    public static void main(String[] args) throws InterruptedException {
        InventoryCounter inventoryCounter = new InventoryCounter();
        IncrementingThread incrementingThread = new IncrementingThread(inventoryCounter);
        DecrementingThread decrementingThread = new DecrementingThread(inventoryCounter);
        long memB4 = Runtime.getRuntime().freeMemory();
        System.out.println("Free memory before:: " + Runtime.getRuntime().freeMemory());
        incrementingThread.start();
        decrementingThread.start();
//        incrementingThread.join();
//        decrementingThread.join();
        long memAfter = Runtime.getRuntime().freeMemory();
        System.out.println("Free memory after:: " + memAfter);
        System.out.println("Difference:: " + (memAfter - memB4) / 1024);
        System.out.println("We currently have " + inventoryCounter.getItems() + " items");
    }

    public static class IncrementingThread extends Thread {
        private InventoryCounter inventoryCounter;
        public IncrementingThread(InventoryCounter inventoryCounter) {
            this.inventoryCounter = inventoryCounter;
        }

        @Override
        public void run() {
            for (int i = 0; i < 500; i++) {
                inventoryCounter.increment();
            }
        }
    }

    public static class DecrementingThread extends Thread {
        private InventoryCounter inventoryCounter;

        public DecrementingThread(InventoryCounter inventoryCounter) {
            this.inventoryCounter = inventoryCounter;
        }

        @Override
        public void run() {
            for (int i = 0; i < 500; i++) {
                inventoryCounter.decrement();
            }
        }
    }
}

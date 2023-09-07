package race.condition;

public class DataRace {
    public static void main(String[] args) {
        InnerSharedClass innerSharedClass = new InnerSharedClass();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                innerSharedClass.increment();
            }
        });
        Thread t2 = new Thread(() -> {
            int count = 0;
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                count = innerSharedClass.checkDataRace() ? count + 1 : count;
            }
            System.out.println("Data Race for " + count + " times");
        });
        t1.start();
        t2.start();
    }

    public static class InnerSharedClass {
        private volatile int x = 0;
        private volatile int y = 0;

        public void increment() {
            x++;
            y++;
        }

        public boolean checkDataRace() {
            if (y > x) {
                System.out.println("y > x - Data Race is detected");
                return true;
            }
            return false;
        }
    }
}

package thread.interrupt.example;
//
//import java.math.BigInteger;
//
public class ThreadTermination {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new BlockingTask());
        thread.start();
        try {
            thread.setDaemon(true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println(thread.getName());
//        Thread.currentThread().join(2000);
//        thread.interrupt();
//        Thread thread1 = new Thread(new LongComputationTask(new BigInteger("2"), new BigInteger("10")));
//        thread1.start();
//        System.out.println("Start:: " + Thread.currentThread().getName());
    }
//
//    private static class LongComputationTask implements Runnable {
//        private BigInteger base;
//        private BigInteger power;
//
//        public LongComputationTask(BigInteger base, BigInteger power) {
//            this.base = base;
//            this.power = power;
//        }
//
//        @Override
//        public void run() {
//            System.out.println(base + "^" + power + " = " + pow(base, power));
//        }
//
//        private BigInteger pow(BigInteger base, BigInteger power) {
//            BigInteger result = BigInteger.ONE;
//            for (BigInteger i = BigInteger.ZERO; i.compareTo(power) != 0; i.add(BigInteger.ONE)) {
//                result = result.multiply(base);
//            }
//            return result;
//        }
//    }
//
    private static class BlockingTask implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(5000000);
            } catch (InterruptedException e) {
                System.out.println("Exiting blocking thread");
            }
        }
    }
}

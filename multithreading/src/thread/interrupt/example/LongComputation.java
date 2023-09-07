package thread.interrupt.example;

import java.math.BigInteger;

public class LongComputation {
    public static void main(String[] args) throws InterruptedException {
        Thread compute = new Thread(new LongComputationTask(new BigInteger("2000000"), new BigInteger("10000000")));
        compute.setDaemon(true);
        compute.start();
//        Thread.sleep(50);
        compute.interrupt();
    }

    private static class LongComputationTask implements Runnable {
        private final BigInteger base;
        private final BigInteger power;

        public LongComputationTask(BigInteger base, BigInteger power) {
//            System.out.println("Constructor");
            this.base = base;
            this.power = power;
        }

        @Override
        public void run() {
            System.out.println(base + "^" + power + " = " + this.pow(base, power));
        }

        private BigInteger pow(BigInteger base, BigInteger power) {
            BigInteger result = BigInteger.ONE;
            for (BigInteger i = BigInteger.ZERO; i.compareTo(power) != 0; i = i.add(BigInteger.ONE)) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Prematurely interrupted computation");
                    return BigInteger.ZERO;
                }
                result = result.multiply(base);
            }
            System.out.println("Result:: " + result);
            return result;
        }
    }

}

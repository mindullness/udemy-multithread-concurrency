package thread.interrupt.example;

import java.math.BigInteger;
import java.util.*;

public class ComplexCalculation {
    public static void main(String[] args) {
        BigInteger base1 = new BigInteger("25");
        BigInteger power1 = new BigInteger("10");
        BigInteger base2 = new BigInteger("33");
        BigInteger power2 = new BigInteger("2");
        System.out.printf("%d^%d + %d^%d = %d", base1, power1, base2, power2, calculateResult(base1, power1, base2, power2));
    }

    private static BigInteger calculateResult(BigInteger base1, BigInteger power1, BigInteger base2, BigInteger power2) {
        BigInteger result = BigInteger.ZERO;
        /*
            Calculate result = ( base1 ^ power1 ) + (base2 ^ power2).
            Where each calculation in (..) is calculated on a different thread
        */
        List<PowerCalculatingThread> threads = new ArrayList<>();
        threads.add(new PowerCalculatingThread(base1, power1));
        threads.add(new PowerCalculatingThread(base2, power2));
        threads.forEach(Thread::start);
        try {
            for (Thread t : threads) {
                t.join();
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }
        for (PowerCalculatingThread t : threads) {
            result = t.getResult().add(result);
        }
        return result;
    }

    private static class PowerCalculatingThread extends Thread {
        private BigInteger result;
        private BigInteger base;
        private BigInteger power;

        public PowerCalculatingThread(BigInteger base, BigInteger power) {
            this.setName("Thread-" + base + "^" + power + "::");
            this.base = base;
            this.power = power;
        }

        @Override
        public void run() {
           /*
           Implement the calculation of result = base ^ power
           */
            System.out.println(Thread.currentThread().getName() + " is running");
            BigInteger temp = BigInteger.ONE;
            for (BigInteger i = BigInteger.ZERO; i.compareTo(power) != 0; i = i.add(BigInteger.ONE)) {
                // if (Thread.currentThread().isInterrupted()) {
                //     break;
                // }
                temp = temp.multiply(base);
            }
            result = temp;
        }

        public BigInteger getResult() {
            return result;
        }
    }
}
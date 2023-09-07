package thread.interrupt.example;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JoiningThreads {
    public static void main(String[] args) throws InterruptedException {

        // we want to calculate !0, !3435, !2324L, !4556, !23, !2435, !5566;
        List<Long> numbers = Arrays.asList(1320L, 43L, 3333L, 34L, 53L, 25L, 362323232L);
        // 7 numbers
        // start 7 threads
        // 7 thread running in parallel => variables(resources) of the individual object
        // main thread.

        List<FactorialThread> threads = new ArrayList<>();
        numbers.forEach(n -> {
            threads.add(new FactorialThread(n));
        });
        //            t.setDaemon(true);
        //            t.setDaemon(true);
        threads.forEach(Thread::start);

        long start = System.currentTimeMillis();
        for (Thread thread : threads) {
            thread.join(2000);
        }
        for (int i = 0; i < numbers.size(); i++) {
            FactorialThread factorialThread = threads.get(i);
            if (factorialThread.isFinished()) { // Race Condition happens here
                System.out.println("Factorial of " + numbers.get(i) + " is:: " + factorialThread.getResult());
            } else {
                factorialThread.interrupt();
                System.out.println("The calculation for " + numbers.get(i) + " is still in progress");
            }
        }
        System.out.println("Program runs in " + (System.currentTimeMillis() - start) + " ms");
    }

    private static class FactorialThread extends Thread {
        private final long inputNumber;
        private BigInteger result = BigInteger.ZERO;
        private boolean isFinished = false;

        public FactorialThread(long inputNumber) {
            this.inputNumber = inputNumber;
            this.setName("Thread:: " + this.inputNumber);
        }

        @Override
        public void run() {
            this.result = factorial(inputNumber);
            this.isFinished = true;
        }

        //        private BigInteger factorial(long n) {
//            BigInteger temp = new BigInteger(Long.toString(n));
//            if (n == 0) return BigInteger.ONE;
//            return temp.multiply(factorial(n - 1));
//        }
        private BigInteger factorial(long n) {
            BigInteger temp = BigInteger.ONE;
            for (long i = n; i > 0; i--) {
                if (this.isInterrupted()) {
                    System.out.println("Still calculating but result is:: ... a lot of digits");
                    break;
                }
                // * + - /
                temp = temp.multiply(new BigInteger(Long.toString(n)));
            }
            return temp;
        }

        public BigInteger getResult() {
            return result;
        }

        public boolean isFinished() {
            if (!isFinished) {
                System.out.println(this.getName() + " is running");
            }
            return isFinished;
        }
    }
}

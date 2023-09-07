package thread.creation.example.two;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class PreventHacking {
    private static final int MAX_PASS = 10000;

    public static void main(String[] args) {
        Vault vault = new Vault(new Random().nextInt(MAX_PASS));
        List<Thread> threads = new ArrayList<>();
        threads.add(new AscendingHacker(vault));
        threads.add(new DescendingHacker(vault));
        threads.add(new Police());
        threads.forEach(Thread::start);
    }

    private static class Vault {
        private int pass;

        public Vault(int pass) {
            System.out.println("Password:: " + pass);
            this.pass = pass;
        }

        public boolean equals(int guess) {
            try {
                Thread.sleep(5);
                return pass == guess;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static abstract class HackerThread extends Thread {
        protected Vault vault;

        public HackerThread(Vault vault) {
            this.vault = vault;
            this.setName(this.getClass().getCanonicalName());
            this.setPriority(MAX_PRIORITY);
        }

        @Override
        public synchronized void start() {
            System.out.println("Starting thread:: " + this.getName() + ", priority:: " + this.getPriority());
            super.start();
        }
    }

    private static class AscendingHacker extends HackerThread {
        public AscendingHacker(Vault vault) {
            super(vault);
        }

        @Override
        public void run() {
            for (int guess = 0; guess < MAX_PASS; guess++) {
                if (this.vault.equals(guess)) {
                    System.out.println("Ascending hacker guessed the password!");
                    System.exit(0);
                }
            }
        }
    }

    private static class DescendingHacker extends HackerThread {
        public DescendingHacker(Vault vault) {
            super(vault);
        }

        @Override
        public void run() {
            for (int guess = MAX_PASS - 1; guess >= 0; guess--) {
                if (this.vault.equals(guess)) {
                    System.out.println("Descending hacker guessed the password!");
                    System.exit(0);
                }
            }
        }
    }

    private static class Police extends Thread {
        @Override
        public void run() {
            for (int i = 10; i >= 0; i--) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(i);
            }
            System.out.println("Game over!!! Police won!!!");
            System.exit(0);
        }
    }
}

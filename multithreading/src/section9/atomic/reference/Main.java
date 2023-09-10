package section9.atomic.reference;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

public class Main {
    public static void main(String[] args) throws InterruptedException {
//        StandardStack<Integer> stack = new StandardStack<>();
        LockFreeStack<Integer> stack = new LockFreeStack<>();
        Random random = new Random();
        for (int i = 0; i < 100_000; i++) {
            stack.push(random.nextInt());
        }
        List<Thread> threads = new ArrayList<>();
        int pushingThreads = 2;
        int poppingThreads = 2;

        for (int i = 0; i < pushingThreads; i++) {
            Thread thread = new Thread(() -> {
                while (true) {
                    stack.push(random.nextInt());
                }
            });
            thread.setDaemon(true);
            threads.add(thread);
        }
        for (int i = 0; i < poppingThreads; i++) {
            Thread thread = new Thread(() -> {
                while (true) {
                    stack.pop();
                }
            });
            thread.setDaemon(true);
            threads.add(thread);
        }
        threads.forEach(Thread::start);
        Thread.sleep(10000);
        System.out.printf("%,d operations were performed in 10 seconds", stack.getCounter());
    }

    private static class LockFreeStack<T> {
        private AtomicReference<StackNode<T>> head = new AtomicReference<>();
        private AtomicInteger counter = new AtomicInteger(0);

        public void push(T value) {
            StackNode<T> currentHead;
            StackNode<T> newHeadNode = new StackNode<>(value);
//            long start = System.nanoTime();
            while (true) {
                currentHead = head.get();
                if(head.compareAndSet(currentHead, newHeadNode)) {
                    break;
                } else {
                    LockSupport.parkNanos(1);
                }
            }
//            do {
//                LockSupport.parkNanos(1);
//                currentHead = head.get();
//            } while (!head.compareAndSet(currentHead, newHeadNode));
//            long end = System.nanoTime();
//            System.out.println("Push operates in " + (end - start) + " ns");
            counter.incrementAndGet();
        }

        public T pop() {
            StackNode<T> currentNode = head.get();
            StackNode<T> newHeadNode;
            while (currentNode != null) {
                newHeadNode = currentNode.next;
                if (head.compareAndSet(currentNode, newHeadNode)) {
                    break;
                } else {
                    LockSupport.parkNanos(1);
                    currentNode = head.get();
                }
            }
            counter.incrementAndGet();
            return currentNode != null ? currentNode.value : null;
        }

        public int getCounter() {
            return counter.get();
        }
    }

    private static class StandardStack<T> {
        private StackNode<T> head;
        private int counter = 0;

        public synchronized void push(T value) {
            StackNode<T> newHead = new StackNode<>(value);
            newHead.next = head;
            head = newHead;
            counter++;
        }

        public synchronized T pop() {
            if (head == null) {
                counter++;
                return null;
            }
            T value = head.value;
            head = head.next;
            counter++;
            return value;
        }

        public int getCounter() {
            return counter;
        }
    }

    private static class StackNode<T> {
        public T value;
        public StackNode<T> next;

        public StackNode(T value) {
            this.value = value;
        }
    }
}

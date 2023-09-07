package locks.rwreentrant;

import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class InventoryDatabase {
    private TreeMap<Integer, Integer> priceToCountMap = new TreeMap<>();
//    private ReentrantLock lock = new ReentrantLock();
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private Lock readLock = lock.readLock();
    private Lock writeLock = lock.writeLock();
    public int getNumberOfItemsPriceRange(int lowerBound, int upperBound) {
//        lock.lock();
        readLock.lock();
        try {
            Integer fromKey = priceToCountMap.ceilingKey(lowerBound);
            Integer toKey = priceToCountMap.floorKey(upperBound);
            if (fromKey == null || toKey == null) {
                return 0;
            }
            NavigableMap<Integer, Integer> rangeOfPrices = priceToCountMap.subMap(fromKey, true, toKey, true);
            return rangeOfPrices.values().stream().reduce(0, Integer::sum);
        } finally {
//            lock.unlock();
            readLock.unlock();
        }
    }

    public void addItems(int price) {
//        lock.lock();
        writeLock.lock();
        try {
            Integer numberOfItemsForPrice = priceToCountMap.get(price);
            priceToCountMap.put(price, numberOfItemsForPrice == null ? 1 : numberOfItemsForPrice + 1);
        } finally {
//            lock.unlock();
            writeLock.unlock();
        }
    }
    public void removeItem(int price) {
//        lock.lock();
        writeLock.lock();
        try {
            Integer numberOfItemsForPrice = priceToCountMap.get(price);
            if (numberOfItemsForPrice == null || numberOfItemsForPrice == 1) {
                priceToCountMap.remove(price);
            } else {
                priceToCountMap.put(price, numberOfItemsForPrice - 1);
            }
        } finally {
//            lock.unlock();
            writeLock.unlock();
        }
    }
}

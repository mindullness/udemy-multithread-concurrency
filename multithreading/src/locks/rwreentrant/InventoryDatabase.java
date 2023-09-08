package locks.rwreentrant;

import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class InventoryDatabase {
    private final TreeMap<Integer, Integer> priceToCountMap = new TreeMap<>();
    private final ReentrantReadWriteLock lockObj;
    public InventoryDatabase() {
        this.lockObj = new ReentrantReadWriteLock();
    }
    public int getNumberOfItemsPriceRange(int lowerBound, int upperBound) {
        Lock lock = getReadLock();
        lock.lock();
        try {
            Integer fromKey = priceToCountMap.ceilingKey(lowerBound);
            Integer toKey = priceToCountMap.floorKey(upperBound);
            if (fromKey == null || toKey == null) {
                return 0;
            }
            NavigableMap<Integer, Integer> rangeOfPrices = priceToCountMap.subMap(fromKey, true, toKey, true);
            return rangeOfPrices.values().stream().reduce(0, Integer::sum);
        } finally {
            lock.unlock();
        }
    }

    public void addItems(int price) {
        Lock lock = getWriteLock();
        lock.lock();
        try {
            Integer numberOfItemsForPrice = priceToCountMap.get(price);
            priceToCountMap.put(price, numberOfItemsForPrice == null ? 1 : numberOfItemsForPrice + 1);
        } finally {
            lock.unlock();
        }
    }
    public void removeItem(int price) {
        Lock lock = getWriteLock();
        lock.lock();
        try {
            Integer numberOfItemsForPrice = priceToCountMap.get(price);
            if (numberOfItemsForPrice == null || numberOfItemsForPrice == 1) {
                priceToCountMap.remove(price);
            } else {
                priceToCountMap.put(price, numberOfItemsForPrice - 1);
            }
        } finally {
            lock.unlock();
        }
    }
    private Lock getReadLock(){
        return this.lockObj.readLock();
    }
    private Lock getWriteLock() {
        return this.lockObj.writeLock();
    }
}

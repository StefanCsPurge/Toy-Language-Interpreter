package Model.ADT;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LatchTable implements ILatchTable{
    private final ConcurrentHashMap<Integer, Integer> map;
    private int newFreeLocation;

    public LatchTable() {
        this.map = new ConcurrentHashMap<>();
        this.newFreeLocation = 0;
    }

    @Override
    public void put(int address, int count) {
        this.map.put(address, count);
    }

    @Override
    public Map<Integer, Integer> getContent() {
        return map;
    }

    @Override
    public Integer get(int address) {
        ReadWriteLock lock = new ReentrantReadWriteLock();
        lock.readLock().lock();
        int count = map.get(address);
        lock.readLock().unlock();
        return count;
    }

    @Override
    public boolean isDefined(int address) {
        return this.map.containsKey(address);
    }

    @Override
    public void remove(int address) {
        this.map.remove(address);
    }

    @Override
    public int getNewFreeLocation() {
        Lock lock = new ReentrantLock();
        lock.lock();
        this.newFreeLocation++;
        lock.unlock();
        return this.newFreeLocation;
    }

    @Override
    public String toString() {
        StringBuilder s= new StringBuilder("\n");
        for (Integer key : map.keySet()) {
            s.append(key.toString());
            String valueStr = map.get(key).toString();
            s.append(" --> ").append(valueStr).append("\n");
        }
        return s.toString();
    }
}

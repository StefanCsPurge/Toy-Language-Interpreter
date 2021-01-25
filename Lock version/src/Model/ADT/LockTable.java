package Model.ADT;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LockTable implements ILockTable{
    private final ConcurrentHashMap<Integer, Integer> map;
    private int newFreeLocation;

    public LockTable() {
        this.map = new ConcurrentHashMap<>();
        this.newFreeLocation = 0;
    }

    @Override
    public void put(int address, int state) {
        this.map.put(address, state);
    }

    @Override
    public Map<Integer, Integer> getContent() {
        return map;
    }

    @Override
    public Integer get(int address) {
        ReadWriteLock lock = new ReentrantReadWriteLock();
        lock.readLock().lock();
        int state = map.get(address);
        lock.readLock().unlock();
        return state;
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
        int localNextFree;
        lock.lock();
        this.newFreeLocation++;
        localNextFree = newFreeLocation;
        lock.unlock();
        return localNextFree;
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

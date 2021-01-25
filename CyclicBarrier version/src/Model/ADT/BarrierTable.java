package Model.ADT;

import javafx.util.Pair;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BarrierTable implements IBarrierTable{
    private final ConcurrentHashMap<Integer, Pair<Integer, List<Integer>>> map;
    private int newFreeLocation;

    public BarrierTable() {
        this.map =  new ConcurrentHashMap<>();
        this.newFreeLocation = 0;
    }

    @Override
    public void put(int address, int BarrierN, List<Integer> threadIDs) {
        this.map.put(address, new Pair<>(BarrierN, threadIDs));
    }

    @Override
    public void remove(int address) {
        this.map.remove(address);
    }

    @Override
    public boolean isDefined(int address) {
        return this.map.containsKey(address);
    }

    @Override
    public Pair<Integer, List<Integer>> get(int address) {
        return this.map.get(address);
    }

    @Override
    public Map<Integer, Pair<Integer, List<Integer>>> getContent() {
        return map;
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
            var pair = map.get(key);
            String valueStr = pair.getKey().toString() + " w/ threads: " + pair.getValue().toString();
            s.append(" --> ").append(valueStr).append("\n");
        }
        return s.toString();
    }
}

package Model.ADT;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SemaphoreTable implements ISemaphoreTable {
    private final ConcurrentHashMap<Integer, Pair<Integer, ArrayList<Integer>>> map;
    private int newFreeLocation;

    public SemaphoreTable(){
        this.map = new ConcurrentHashMap<>();
        this.newFreeLocation = 0;
    }

    public int getNewFreeLocation(){
        Lock lock = new ReentrantLock();
        lock.lock();
        this.newFreeLocation++;
        lock.unlock();
        return this.newFreeLocation;
    }

    @Override
    public void put(int address, int SemaphoreN, ArrayList<Integer> threadIDs) {
       this.map.put(address, new Pair<>(SemaphoreN, threadIDs));
    }

    @Override
    public void remove(int addr) {
        this.map.remove(addr);
    }

    @Override
    public boolean isDefined(int address) {
        return this.map.containsKey(address);
    }

    @Override
    public Pair<Integer, ArrayList<Integer>> get(int address) {
        ReadWriteLock lock = new ReentrantReadWriteLock();
        lock.readLock().lock();
        var pair = map.get(address);
        lock.readLock().unlock();
        return pair;
    }

    @Override
    public Map<Integer, Pair<Integer, ArrayList<Integer>>> getContent() {
        return map;
    }

    /*public void setContent(ConcurrentHashMap<Integer, Pair<Integer, ArrayList<Integer>>> content){
        this.map = content;
    }*/


    @Override
    public String toString() {
        StringBuilder s= new StringBuilder("\n");
        for (Integer key : map.keySet()) {
            s.append(key.toString());
            var pair = map.get(key);
            String valueStr = pair.getKey().toString() + " Threads: " + pair.getValue().toString();
            s.append(" --> ").append(valueStr).append("\n");
        }
        return s.toString();
    }
}

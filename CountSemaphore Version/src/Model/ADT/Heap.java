package Model.ADT;
import java.util.HashMap;
import java.util.Map;

public class Heap<T> implements IHeap<T> {
    private HashMap<Integer, T> map;
    private int memory;

    public Heap() {
        this.map = new HashMap<>();
        this.memory = 0;
    }

    @Override
    public synchronized int allocate(T val) {
        this.memory++;
        this.map.put(this.memory, val);
        return this.memory;
    }

    @Override
    public T get(int addr){
        return this.map.get(addr);
    }

    @Override
    public synchronized void put(int addr, T val){
        this.map.put(addr, val);
    }

    @Override
    public synchronized void deallocate(int addr) {
        this.map.remove(addr);
    }

    @Override
    public boolean isDefined(int addr){
        return this.map.containsKey(addr) && this.map.get(addr) != null;
    }

    @Override
    public Map<Integer, T> getContent() {
        return map;
    }

    @Override
    public void setContent(HashMap<Integer, T> content){
        this.map = content;
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


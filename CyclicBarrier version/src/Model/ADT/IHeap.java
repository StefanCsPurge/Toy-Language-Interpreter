package Model.ADT;

import java.util.HashMap;
import java.util.Map;

public interface IHeap<T> {
    int allocate(T value);

    void put(int address, T value);

    T get(int address);

    void deallocate(int address);

    boolean isDefined(int address);

    Map<Integer, T> getContent();

    void setContent(HashMap<Integer, T> content);
}

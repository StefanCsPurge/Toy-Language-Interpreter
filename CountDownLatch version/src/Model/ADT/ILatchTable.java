package Model.ADT;

import java.util.Map;

public interface ILatchTable {

    void put(int address, int count);

    Map<Integer, Integer> getContent();

    Integer get(int address);

    boolean isDefined(int address);

    void remove(int address);

    int getNewFreeLocation();
}

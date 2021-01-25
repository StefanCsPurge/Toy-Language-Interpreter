package Model.ADT;

import java.util.Map;

public interface ILockTable {

    void put(int address, int state);

    Map<Integer, Integer> getContent();

    Integer get(int address);

    boolean isDefined(int address);

    void remove(int address);

    int getNewFreeLocation();
}

package Model.ADT;

import javafx.util.Pair;

import java.util.List;
import java.util.Map;

public interface IBarrierTable {
    void put(int address, int BarrierN, List<Integer> threadIDs);

    void remove(int address);

    boolean isDefined(int address);

    Pair<Integer, List<Integer>> get(int address);

    Map<Integer, Pair<Integer, List<Integer>>> getContent();

    int getNewFreeLocation();
}

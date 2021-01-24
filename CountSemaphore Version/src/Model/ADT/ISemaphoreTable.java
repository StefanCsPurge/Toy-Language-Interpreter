package Model.ADT;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Map;

public interface ISemaphoreTable {
    void put(int address, int SemaphoreN, ArrayList<Integer> threadIDs);

    void remove(int address);

    boolean isDefined(int address);

    Pair<Integer, ArrayList<Integer>> get(int address);

    Map<Integer, Pair<Integer, ArrayList<Integer>>> getContent();

    int getNewFreeLocation();
}

package Model.ADT;

import Model.statement.Statement;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProcTable implements IProcTable{
    private final ConcurrentHashMap<String, Pair<List<String>, Statement>> map;

    public ProcTable() {
        this.map = new ConcurrentHashMap<>();
    }

    @Override
    public Map<String, Pair<List<String>, Statement>> getContent() {
        return map;
    }

    @Override
    public void put(String procName, List<String> parameters, Statement procBody) {
        this.map.put(procName, new Pair<>(parameters, procBody));
    }

    @Override
    public void remove(String procName) {
        this.map.remove(procName);
    }

    @Override
    public boolean isDefined(String procName) {
        return this.map.containsKey(procName);
    }

    @Override
    public Pair<List<String>, Statement> get(String procName) {
        return this.map.get(procName);
    }

    @Override
    public String toString() {
        StringBuilder s= new StringBuilder("\n");
        for (String key : map.keySet()) {
            s.append(key);
            var pair = map.get(key);
            String valueStr = pair.getKey().toString() + " body: " + pair.getValue().toString();
            s.append(" --> ").append(valueStr).append("\n");
        }
        return s.toString();
    }
}

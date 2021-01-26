package Model.ADT;

import Model.statement.Statement;
import javafx.util.Pair;

import java.util.Map;
import java.util.List;

public interface IProcTable {
    Map<String, Pair<List<String>, Statement>> getContent();

    void put (String procName, List<String> parameters, Statement procBody);

    void remove(String procName);

    boolean isDefined(String procName);

    Pair<List<String>,Statement> get(String procName);

}

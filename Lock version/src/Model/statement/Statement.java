package Model.statement;

import Model.ADT.IDictionary;
import Model.ProgramState;
import Model.type.Type;

public interface Statement {
    ProgramState execute(ProgramState state) throws Exception;
    //which is the execution method for a statement.

    IDictionary<String, Type> typeCheck(IDictionary<String,Type> typeEnv) throws Exception;
}

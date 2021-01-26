package Model.statement.Procedure;

import Model.ADT.IDictionary;
import Model.ProgramState;
import Model.statement.Statement;
import Model.type.Type;

public class ReturnStmt implements Statement {
    @Override
    public ProgramState execute(ProgramState state) throws Exception {
        state.getSymbolTableStack().pop();
        return null;
    }

    @Override
    public IDictionary<String, Type> typeCheck(IDictionary<String, Type> typeEnv) throws Exception {
        return typeEnv;
    }

    @Override
    public String toString() {
        return "return";
    }
}

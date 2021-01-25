package Model.statement.CountDownLatch;

import Exceptions.TypeMismatchException;
import Exceptions.VariableNotDeclaredException;
import Model.ADT.IDictionary;
import Model.ProgramState;
import Model.statement.Statement;
import Model.type.IntType;
import Model.type.Type;
import Model.value.IntValue;
import Model.value.Value;

public class AwaitStmt implements Statement {
    private final String var;

    public AwaitStmt(String var) {
        this.var = var;
    }

    @Override
    public ProgramState execute(ProgramState state) throws Exception {
        if (!state.getSymbolTable().containsKey(var))
            throw new VariableNotDeclaredException("Await variable not declared!");
        Value varVal = state.getSymbolTable().get(var);
        if (!varVal.getType().equals(new IntType()))
            throw new TypeMismatchException("Await variable is not of type int!");

        int foundIndex = ((IntValue) varVal).getValue();
        if (!state.getLatchTable().isDefined(foundIndex))
            throw new Exception("(Await) Found index in not an index in the LatchTable!");

        if(state.getLatchTable().get(foundIndex) != 0)
            state.getExecutionStack().push(this);
        return null;
    }

    @Override
    public IDictionary<String, Type> typeCheck(IDictionary<String, Type> typeEnv) throws Exception {
        Type type = typeEnv.get(var);
        if (!type.equals(new IntType()))
            throw new TypeMismatchException("Await variable is not of type int!");
        return typeEnv;
    }

    @Override
    public String toString() {
        return "await(" + var +  ")";
    }
}

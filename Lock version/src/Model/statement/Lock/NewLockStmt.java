package Model.statement.Lock;

import Exceptions.TypeMismatchException;
import Exceptions.VariableNotDeclaredException;
import Model.ADT.IDictionary;
import Model.ProgramState;
import Model.statement.Statement;
import Model.type.IntType;
import Model.type.Type;
import Model.value.IntValue;
import Model.value.Value;

public class NewLockStmt implements Statement {
    private final String var;

    public NewLockStmt(String var) {
        this.var = var;
    }

    @Override
    public ProgramState execute(ProgramState state) throws Exception {
        if(!state.getSymbolTable().containsKey(var))
            throw new VariableNotDeclaredException("NewLock variable not declared!");
        Value v = state.getSymbolTable().get(var);
        if(! v.getType().equals(new IntType()))
            throw new TypeMismatchException("NewLock key is not of type int!");

        int newFreeLocation = state.getLockTable().getNewFreeLocation();
        state.getLockTable().put(newFreeLocation,-1);
        state.getSymbolTable().set(var,new IntValue(newFreeLocation));

        return null;
    }

    @Override
    public IDictionary<String, Type> typeCheck(IDictionary<String, Type> typeEnv) throws Exception {
        Type varType = typeEnv.get(var);
        if(! varType.equals(new IntType()))
            throw new TypeMismatchException("NewLock variable is not of type int!");
        return typeEnv;
    }

    @Override
    public String toString() {
        return "newLock(" + var + ")";
    }
}

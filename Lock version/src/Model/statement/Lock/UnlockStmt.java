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

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UnlockStmt implements Statement {
    private final String var;

    public UnlockStmt(String var) {
        this.var = var;
    }

    @Override
    public ProgramState execute(ProgramState state) throws Exception {
        Lock lock = new ReentrantLock();
        lock.lock();
        if(!state.getSymbolTable().containsKey(var))
            throw new VariableNotDeclaredException("Unlock variable not declared!");
        Value v = state.getSymbolTable().get(var);
        if(! v.getType().equals(new IntType()))
            throw new TypeMismatchException("Unlock variable is not of type int!");

        int foundIndex = ((IntValue)state.getSymbolTable().get(var)).getValue();
        if (!state.getLockTable().isDefined(foundIndex))
            throw new Exception("(Unlock) Found index in not an index in the LockTable!");

        if(state.getLockTable().get(foundIndex) == state.getID())
            state.getLockTable().put(foundIndex,-1);
        lock.unlock();
        return null;
    }

    @Override
    public IDictionary<String, Type> typeCheck(IDictionary<String, Type> typeEnv) throws Exception {
        Type type = typeEnv.get(var);
        if (!type.equals(new IntType()))
            throw new TypeMismatchException("Lock variable is not of type int!");
        return typeEnv;
    }

    @Override
    public String toString() {
        return "unlock(" + var + ")";
    }
}

package Model.statement.Semaphore;

import Exceptions.TypeMismatchException;
import Exceptions.VariableNotDeclaredException;
import Model.ADT.IDictionary;
import Model.ProgramState;
import Model.expression.Expression;
import Model.statement.Statement;
import Model.type.IntType;
import Model.type.Type;
import Model.value.IntValue;
import Model.value.Value;

public class ReleaseStmt implements Statement {
    private final Expression var;

    public ReleaseStmt(Expression var) {
        this.var = var;
    }


    @Override
    public ProgramState execute(ProgramState state) throws Exception {
        String varName = var.toString();
        if (!state.getSymbolTable().containsKey(varName))
            throw new VariableNotDeclaredException("Release variable not declared!");
        Value resVal = var.evaluate(state.getSymbolTable(), state.getHeap());
        if (!resVal.getType().equals(new IntType()))
            throw new TypeMismatchException("Release variable is not of type int!");

        int foundIndex = ((IntValue) resVal).getValue();
        if (!state.getSemaphoreTable().isDefined(foundIndex))
            throw new Exception("Found index in not an index in the SemaphoreTable!");

        var pair = state.getSemaphoreTable().get(foundIndex);
        var list1 = pair.getValue();
        int stateID = state.getID();
        if(list1.contains(stateID))
            list1.remove((Integer) stateID);
        return null;
    }

    @Override
    public IDictionary<String, Type> typeCheck(IDictionary<String, Type> typeEnv) throws Exception {
        Type type = var.typeCheck(typeEnv);
        if (!type.equals(new IntType()))
            throw new TypeMismatchException("Release variable is not of type int!");
        return typeEnv;
    }

    @Override
    public String toString() {
        return "release(" + var.toString() + ")";
    }
}

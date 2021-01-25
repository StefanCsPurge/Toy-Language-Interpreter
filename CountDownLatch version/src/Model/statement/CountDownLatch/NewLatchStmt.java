package Model.statement.CountDownLatch;

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

public class NewLatchStmt implements Statement {
    private final String var;
    private final Expression exp;

    public NewLatchStmt(String var, Expression exp) {
        this.var = var;
        this.exp = exp;
    }

    @Override
    public ProgramState execute(ProgramState state) throws Exception {
        Value resVal = exp.evaluate(state.getSymbolTable(), state.getHeap());
        if(! resVal.getType().equals(new IntType()))
            throw new TypeMismatchException("NewLatch expression result is not of type int!");

        int num1 = ((IntValue)resVal).getValue();
        var latchTable = state.getLatchTable();
        int newFreeLocation = latchTable.getNewFreeLocation();
        latchTable.put(newFreeLocation,num1);

        if(!state.getSymbolTable().containsKey(var))
            throw new VariableNotDeclaredException("NewLatch variable not declared!");
        Value v = state.getSymbolTable().get(var);
        if(! v.getType().equals(new IntType()))
            throw new TypeMismatchException("NewLatch key is not of type int!");

        state.getSymbolTable().set(var,new IntValue(newFreeLocation));

        return null;
    }

    @Override
    public IDictionary<String, Type> typeCheck(IDictionary<String, Type> typeEnv) throws Exception {
        Type varType = typeEnv.get(var);
        if(! varType.equals(new IntType()))
            throw new TypeMismatchException("NewLatch key is not of type int!");
        Type type = exp.typeCheck(typeEnv);
        if(! type.equals(new IntType()))
            throw new TypeMismatchException("NewLatch expression result is not of type int!");
        return typeEnv;
    }

    @Override
    public String toString() {
        return "newLatch(" + var + ","  + exp.toString() + ")";
    }
}

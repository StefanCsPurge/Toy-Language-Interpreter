package Model.statement.CyclicBarrier;

import Exceptions.TypeMismatchException;
import Model.ADT.IDictionary;
import Model.ProgramState;
import Model.expression.Expression;
import Model.statement.Statement;
import Model.type.IntType;
import Model.type.Type;
import Model.value.IntValue;
import Model.value.Value;

import java.util.ArrayList;

public class NewBarrierStmt implements Statement {
    private final String var;
    private final Expression exp;

    public NewBarrierStmt(String var, Expression exp) {
        this.var = var;
        this.exp = exp;
    }

    @Override
    public ProgramState execute(ProgramState state) throws Exception {
        Value resVal = exp.evaluate(state.getSymbolTable(), state.getHeap());
        if(! resVal.getType().equals(new IntType()))
            throw new TypeMismatchException("New Barrier expression result is not of type int!");

        int number = ((IntValue)resVal).getValue();
        var barrierTable = state.getBarrierTable();
        int newFreeLocation = barrierTable.getNewFreeLocation();
        barrierTable.put(newFreeLocation, number , new ArrayList<>());

        state.getSymbolTable().set(var,new IntValue(newFreeLocation));
        return null;
    }

    @Override
    public IDictionary<String, Type> typeCheck(IDictionary<String, Type> typeEnv) throws Exception {
        Type type = exp.typeCheck(typeEnv);
        if(! type.equals(new IntType()))
            throw new TypeMismatchException("New Barrier expression result is not of type int!");
        return typeEnv;
    }

    @Override
    public String toString() {
        return "newBarrier(" + var + ","  + exp.toString() + ")";
    }
}

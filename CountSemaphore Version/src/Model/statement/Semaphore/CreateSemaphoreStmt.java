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

import java.util.ArrayList;

public class CreateSemaphoreStmt implements Statement {
    private final Expression var;
    private final Expression exp;

    public CreateSemaphoreStmt(Expression var, Expression exp) {
        this.var = var;
        this.exp = exp;
    }

    @Override
    public ProgramState execute(ProgramState state) throws Exception {
        Value resVal = exp.evaluate(state.getSymbolTable(), state.getHeap());
        if(! resVal.getType().equals(new IntType()))
            throw new TypeMismatchException("Create semaphore expression result is not of type int!");

        int number1 = ((IntValue)resVal).getValue();
        var semaphoreTable = state.getSemaphoreTable();
        int newFreeLocation = semaphoreTable.getNewFreeLocation();
        var emptyList = new ArrayList<Integer>();
        semaphoreTable.put(newFreeLocation, number1, emptyList);

        if(state.getSymbolTable().containsKey(var.toString())){
            Value v = var.evaluate(state.getSymbolTable(), state.getHeap());
            if(! v.getType().equals(new IntType()))
                throw new TypeMismatchException("Create semaphore key is not of type int!");
            state.getSymbolTable().set(var.toString(),new IntValue(newFreeLocation));
        }
        else
            throw new VariableNotDeclaredException("Create semaphore variable not declared!");
        return null;
    }

    @Override
    public IDictionary<String, Type> typeCheck(IDictionary<String, Type> typeEnv) throws Exception {
        Type varType = var.typeCheck(typeEnv);
        if(! varType.equals(new IntType()))
            throw new TypeMismatchException("Create semaphore key is not of type int!");
        Type type = exp.typeCheck(typeEnv);
        if(! type.equals(new IntType()))
            throw new TypeMismatchException("Create semaphore expression result is not of type int!");
        return typeEnv;
    }

    @Override
    public String toString() {
        return "createSemaphore(" + var.toString() + ","  + exp.toString() + ")";
    }
}

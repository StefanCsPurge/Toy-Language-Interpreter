package Model.statement;

import Exceptions.TypeMismatchException;
import Model.ADT.IDictionary;
import Model.ProgramState;
import Model.expression.Expression;
import Model.expression.ValueExpression;
import Model.type.IntType;
import Model.type.Type;
import Model.value.IntValue;

public class SleepStmt implements Statement{
    private final Expression exp;

    public SleepStmt(Expression exp) {
        this.exp = exp;
    }

    @Override
    public ProgramState execute(ProgramState state) throws Exception {
        int number = ((IntValue)exp.evaluate(state.getSymbolTable(), state.getHeap())).getValue();
        if(number > 0)
            state.getExecutionStack().push(new SleepStmt(new ValueExpression(new IntValue(number-1))));
        return null;
    }

    @Override
    public IDictionary<String, Type> typeCheck(IDictionary<String, Type> typeEnv) throws Exception {
        Type type = exp.typeCheck(typeEnv);
        if(! type.equals(new IntType()))
            throw new TypeMismatchException("Sleep parameter is not of type int!");
        return typeEnv;
    }

    @Override
    public String toString(){
        return "sleep(" + exp.toString() + ")";
    }
}

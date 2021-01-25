package Model.statement;

import Exceptions.TypeMismatchException;
import Model.ADT.IDictionary;
import Model.ProgramState;
import Model.expression.Expression;
import Model.expression.ValueExpression;
import Model.type.IntType;
import Model.type.Type;
import Model.value.IntValue;

public class WaitStmt implements Statement{
    private final Expression expr;

    public WaitStmt(Expression expr) {
        this.expr = expr;
    }

    @Override
    public ProgramState execute(ProgramState state) throws Exception {
        int number = ((IntValue)expr.evaluate(state.getSymbolTable(), state.getHeap())).getValue();
        if (number > 0){
            Statement equiv = new CompStmt(new PrintStmt(new ValueExpression(new IntValue(number))), new WaitStmt(new ValueExpression(new IntValue(number-1))));
            state.getExecutionStack().push(equiv);
        }
        return null;
    }

    @Override
    public IDictionary<String, Type> typeCheck(IDictionary<String, Type> typeEnv) throws Exception {
        Type type = expr.typeCheck(typeEnv);
        if(!type.equals(new IntType()))
            throw new TypeMismatchException("Wait parameter is not of type int!");
        return typeEnv;
    }

    @Override
    public String toString() { return "wait(" + expr.toString() + ")"; }
}

package Model.statement;

import Model.ADT.IDictionary;
import Model.ProgramState;
import Model.expression.Expression;
import Model.type.Type;
import Model.value.Value;

public class PrintStmt implements Statement{
    private final Expression expression;

    public PrintStmt(Expression expression) {
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws Exception {
        Value value = expression.evaluate(state.getSymbolTable(), state.getHeap());
        state.getOutput().append(value);
        return null;
    }

    @Override
    public IDictionary<String, Type> typeCheck(IDictionary<String,Type> typeEnv) throws Exception{
        expression.typeCheck(typeEnv);
        return typeEnv;
    }

    @Override
    public String toString() {
        return "print(" + expression.toString() + ")";
    }
}

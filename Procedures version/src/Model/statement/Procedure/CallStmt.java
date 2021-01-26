package Model.statement.Procedure;

import Model.ADT.Dictionary;
import Model.ADT.IDictionary;
import Model.ProgramState;
import Model.expression.Expression;
import Model.statement.Statement;
import Model.type.Type;
import Model.value.Value;

import java.util.ArrayList;
import java.util.Arrays;

public class CallStmt implements Statement {
    private final String procName;
    private final ArrayList<Expression> paramValues;

    public CallStmt(String procName, Expression... values) {
        this.procName = procName;
        this.paramValues = new ArrayList<>(Arrays.asList(values));
    }

    @Override
    public ProgramState execute(ProgramState state) throws Exception {
        if(! state.getProcTable().isDefined(procName))
            throw new Exception("Procedure " + procName + " was not defined!");

        var pair = state.getProcTable().get(procName);
        var parameters = pair.getKey();
        if(parameters.size() != paramValues.size())
            throw new Exception("Incorrect number of parameters given to procedure " + procName + " !");
        var procBody = pair.getValue();

        IDictionary<String,Value> newSymbolTable = new Dictionary<>();

        for (int i=0; i<paramValues.size(); i++){
            Value evalResult = paramValues.get(i).evaluate(state.getSymbolTable(), state.getHeap());
            newSymbolTable.set(parameters.get(i),evalResult);
        }
        state.getSymbolTableStack().push(newSymbolTable);
        state.getExecutionStack().push(new ReturnStmt());
        state.getExecutionStack().push(procBody);
        return null;
    }

    @Override
    public IDictionary<String, Type> typeCheck(IDictionary<String, Type> typeEnv) throws Exception {
        return typeEnv;
    }

    @Override
    public String toString() {
        return "call " + procName + "" + paramValues.toString() + "" ;
    }
}

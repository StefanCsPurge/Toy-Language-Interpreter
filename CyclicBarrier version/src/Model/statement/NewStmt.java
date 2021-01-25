package Model.statement;

import Exceptions.TypeMismatchException;
import Exceptions.VariableNotDeclaredException;
import Model.ADT.IDictionary;
import Model.ProgramState;
import Model.expression.Expression;
import Model.type.RefType;
import Model.type.Type;
import Model.value.RefValue;
import Model.value.Value;

public class NewStmt implements Statement {
    private final String varName;
    private final Expression expr;

    public NewStmt(String varName, Expression expr){
        this.varName = varName;
        this.expr = expr;
    }

    @Override
    public ProgramState execute(ProgramState state) throws Exception{
        if(!state.getSymbolTable().containsKey(varName))
            throw new VariableNotDeclaredException(String.format("Variable '%s' was not declared",varName));
        Value varValue = state.getSymbolTable().get(varName);
        if(!(varValue instanceof RefValue))
            throw new TypeMismatchException(String.format("Variable '%s' is not RefType",varName));

        Value expressionValue = expr.evaluate(state.getSymbolTable(), state.getHeap());  // this gives the value of the thing to put in the heap

        if(!expressionValue.getType().equals(((RefValue)varValue).getLocationType())) // check if location type of value associated to varName is the same as the expression evaluation type
            throw new TypeMismatchException(String.format("Variable '%s' was given a different location type!",varName));

        int address = state.getHeap().allocate(expressionValue);
        state.getSymbolTable().set(varName, new RefValue(address, expressionValue.getType()));

        return null;
    }

    @Override
    public IDictionary<String, Type> typeCheck(IDictionary<String, Type> typeEnv) throws Exception {
        Type typeVar = typeEnv.get(varName);
        Type typExp = expr.typeCheck(typeEnv);
        if(typeVar.equals(new RefType(typExp)))
            return typeEnv;
        throw new TypeMismatchException("NEW stmt: right hand side and left hand side have different types!");
    }

    @Override
    public String toString() {
        return "new(" + varName + "," + expr.toString() + ")";
    }
}

/*
Example:
Ref int v;
new(v,20);
Ref Ref int a;
new(a,v);
print(v);
print(a);

SymTable = {v->(1, int), a->(2, Ref int)}
Heap = {1->20, 2->(1, int)}
Out = {(1, int), (2, Ref int)}
 */
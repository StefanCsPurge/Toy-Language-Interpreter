package Model.expression;

import Model.ADT.IDictionary;
import Model.ADT.IHeap;
import Model.type.Type;
import Model.value.Value;

public class VariableExpression implements Expression{
    private final String ID;

    public VariableExpression(String ID) { this.ID = ID; }

    @Override
    public Value evaluate(IDictionary<String,Value> symbolTable, IHeap<Value> hp) throws Exception{
        return symbolTable.get(ID);
    }

    @Override
    public Type typeCheck(IDictionary<String,Type> typeEnv) throws Exception{
        return typeEnv.get(ID);
    }

    @Override
    public String toString() { return ID; }
}

/*
Var Evaluation:

Eval(Id) = LookUp(SymTable,Id)

where LookUp(SymTable,Id) returns the value to which is mapped the variable Id. If the
variable Id does not exist in SymTable LookUp returns an exception.

Examples:
LookUp({a->2,b->3},a)=2
LookUp({a->2,b->3},x) raised the exception "variable x is not defined"
getType(Id)=getType(Eval(Id))
 */
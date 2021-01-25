package Model.expression;

import Model.ADT.IHeap;
import Model.type.Type;
import Model.value.Value;
import Model.ADT.IDictionary;

public class ValueExpression implements Expression{
    private final Value value;

    public ValueExpression(Value value) { this.value = value; }

    @Override
    public Value evaluate(IDictionary<String,Value> symbolTable, IHeap<Value> hp){
        return value;
    }

    @Override
    public Type typeCheck(IDictionary<String,Type> typeEnv){
        return value.getType();
    }

    @Override
    public String toString() { return value.toString(); }
}

/*
Value Evaluation:

Eval(Number) = Number
Eval(True) = True
Eval(False) = False
the constant is returned by evaluation

getType(Number)=int
getType(True)=bool
getType(False)=bool
 */
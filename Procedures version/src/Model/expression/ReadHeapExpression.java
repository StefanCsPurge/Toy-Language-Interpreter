package Model.expression;

import Exceptions.TypeMismatchException;
import Exceptions.VariableNotDeclaredException;
import Model.ADT.IDictionary;
import Model.ADT.IHeap;
import Model.type.RefType;
import Model.type.Type;
import Model.value.RefValue;
import Model.value.Value;

public class ReadHeapExpression implements Expression{
    private final Expression expr;

    public ReadHeapExpression(Expression expr) {
        this.expr = expr;
    }

    @Override
    public Value evaluate(IDictionary<String,Value> symbolTable, IHeap<Value> hp) throws Exception {
        Value expressionValue = expr.evaluate(symbolTable, hp);
        if(!(expressionValue.getType() instanceof RefType))
            throw new TypeMismatchException(expressionValue.toString() + " is not of RefType");

        int address = ((RefValue)expressionValue).getAddr();
        if(! hp.isDefined(address))
            throw new VariableNotDeclaredException("There is nothing at the address '"+ address + "' in the heap");

        return hp.get(address);
    }

    @Override
    public Type typeCheck(IDictionary<String,Type> typeEnv) throws Exception{
        Type typ=expr.typeCheck(typeEnv);
        if (! (typ instanceof RefType))
            throw new TypeMismatchException("the rH argument is not a Ref Type!");
        return ((RefType)typ).getInner();
    }

    @Override
    public String toString() { return "rH(" + expr.toString() + ")"; }

}

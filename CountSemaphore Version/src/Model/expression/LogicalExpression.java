package Model.expression;

import Exceptions.TypeMismatchException;
import Model.ADT.IDictionary;
import Model.ADT.IHeap;
import Model.type.BoolType;
import Model.type.Type;
import Model.value.Value;
import Model.value.BoolValue;

public class LogicalExpression implements Expression {
    private final Expression exp1,exp2;
    private final String op;

    public LogicalExpression(String op, Expression exp1, Expression exp2) {
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.op = op;
    }

    @Override
    public Value evaluate(IDictionary<String, Value> symbolTable, IHeap<Value> hp) throws Exception {
        Value v1 = exp1.evaluate(symbolTable, hp);
        if (!v1.getType().equals(new BoolType()))
            throw new Exception("First operand is not a bool!");
        Value v2 = exp2.evaluate(symbolTable, hp);
        if (!v2.getType().equals(new BoolType()))
            throw new Exception("Second operand is not a bool!");

        boolean b1 = ((BoolValue)v1).getValue();
        boolean b2 = ((BoolValue)v2).getValue();
        if (op.equals("and"))
            return new BoolValue(b1 && b2);
        if(op.equals("or"))
            return new BoolValue(b1 || b2);
        throw new Exception("Invalid operand!");
    }

    @Override
    public Type typeCheck(IDictionary<String,Type> typeEnv) throws Exception{
        Type typ1 = exp1.typeCheck(typeEnv);
        Type typ2 = exp2.typeCheck(typeEnv);
        if(! typ1.equals(new BoolType()))
            throw new TypeMismatchException("First operand is not a boolean!");
        if(! typ2.equals(new BoolType()))
            throw new TypeMismatchException("Second operand is not a boolean!");
        return new BoolType();
    }

    @Override
    public String toString() {
        return exp1.toString() + " " + op + " " + exp2.toString();
    }
}

/*
Eval(Exp1 and Exp2)=
nr1= Eval(Exp1)
if getType(nr1)== bool then
    nr2=Eval(Exp2)
    if getType(nr2)== bool then
        (nr1&& nr2)
    else Error: "Operand2 is not a boolean"
else Error: "Operand1 is not a boolean"

getType(Exp1 and Exp2) = getType(Eval(Exp1 and Exp2))

The same for or.
 */
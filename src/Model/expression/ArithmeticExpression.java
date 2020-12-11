package Model.expression;

import Exceptions.DivisionByZeroException;
import Exceptions.TypeMismatchException;
import Model.ADT.IDictionary;
import Model.ADT.IHeap;
import Model.type.IntType;
import Model.type.Type;
import Model.value.IntValue;
import Model.value.Value;

public class ArithmeticExpression implements Expression{
    private final Expression exp1,exp2;
    private final char op;

    public ArithmeticExpression(char op, Expression exp1, Expression exp2) {
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.op = op;
    }

    @Override
    public Value evaluate(IDictionary<String,Value> symbolTable, IHeap<Value> hp) throws Exception{
        Value v1 = exp1.evaluate(symbolTable, hp);
        if(!v1.getType().equals(new IntType()))
            throw new Exception("Error - this value is not an integer!");
        Value v2 = exp2.evaluate(symbolTable, hp);
        if(!v2.getType().equals(new IntType()))
            throw new Exception("Error - this value is not an integer!");

        int number1 = ((IntValue) v1).getValue();
        int number2 = ((IntValue) v2).getValue();

        switch (op){
            case '+':
                return new IntValue(number1+number2);
            case '-':
                return new IntValue(number1-number2);
            case '*':
                return new IntValue(number1*number2);
            case '/':
                if(number2 == 0)
                    throw new DivisionByZeroException();
                return new IntValue(number1/number2);
            default:
                throw new Exception("Invalid operand!");
        }
    }

    @Override
    public Type typeCheck(IDictionary<String,Type> typeEnv) throws Exception{
        Type typ1 = exp1.typeCheck(typeEnv);
        Type typ2 = exp2.typeCheck(typeEnv);
        if(! typ1.equals(new IntType()))
            throw new TypeMismatchException("First operand is not an integer!");
        if(! typ2.equals(new IntType()))
            throw new TypeMismatchException("Second operand is not an integer!");
        return new IntType();
    }

    @Override
    public String toString() {
        return exp1.toString()  + op  + exp2.toString();
    }

}

/*
ArithmeticExp evaluation:

Eval(Exp1 + Exp2)=
nr1= Eval(Exp1)
if getType(nr1) == int then
    nr2=Eval(Exp2)
    if getType(nr2)== int then
        (nr1+ nr2)
    else Error: "Operand2 is not an integer"
else Error: "Operand1 is not an integer"

The same for -,*.

Eval(Exp1 / Exp2)=
nr1= Eval(Exp1)
if getType(nr1) == int then
    nr2=Eval(Exp2)
    if getType(nr2)== int then
        if nr2 !=0 then
                (nr1/nr2)
        else Error:"Division by 0"
    else Error: "Operand2 is not an integer"
else Error: "Operand1 is not an integer"

getType(Exp1+Exp2) = getType(Eval(Exp1+Exp2))
the same for -,*, and /.
 */
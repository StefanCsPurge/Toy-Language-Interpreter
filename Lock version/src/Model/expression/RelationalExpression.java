package Model.expression;

import Exceptions.TypeMismatchException;
import Model.ADT.IDictionary;
import Model.ADT.IHeap;
import Model.type.BoolType;
import Model.type.IntType;
import Model.type.Type;
import Model.value.BoolValue;
import Model.value.IntValue;
import Model.value.Value;

public class RelationalExpression implements Expression{
    private final Expression exp1,exp2;
    private final String op;

    public RelationalExpression(String op, Expression exp1, Expression exp2) {
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

        return switch (op) {
            case "<" -> new BoolValue(number1 < number2);   // this returns the new bool value
            case "<=" -> new BoolValue(number1 <= number2);
            case "==" -> new BoolValue(number1 == number2);
            case "!=" -> new BoolValue(number1 != number2);
            case ">" -> new BoolValue(number1 > number2);
            case ">=" -> new BoolValue(number1 >= number2);
            default -> throw new Exception("Invalid operand!");
        };
    }

    @Override
    public Type typeCheck(IDictionary<String,Type> typeEnv) throws Exception{
        Type typ1 = exp1.typeCheck(typeEnv);
        Type typ2 = exp2.typeCheck(typeEnv);
        if(! typ1.equals(new IntType()))
            throw new TypeMismatchException("First operand is not an integer!");
        if(! typ2.equals(new IntType()))
            throw new TypeMismatchException("Second operand is not an integer!");
        return new BoolType();
    }

    @Override
    public String toString() {
        return exp1.toString()  + " " +  op + " " + exp2.toString();
    }
}

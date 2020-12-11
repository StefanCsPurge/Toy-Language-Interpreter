package Model.expression;

import Model.ADT.IDictionary;
import Model.ADT.IHeap;
import Model.type.Type;
import Model.value.Value;

public interface Expression {
    public Value evaluate(IDictionary<String,Value> symbolTable, IHeap<Value> hp) throws Exception;
    /*
    Expression evaluation rules are presented using recursive rules.
    These rules do not change the program state.
    During the evaluation, we also have to check the operands types for some specific operations.
    */
    Type typeCheck(IDictionary<String,Type> typeEnv) throws Exception;
}

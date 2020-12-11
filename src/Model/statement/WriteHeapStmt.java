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

public class WriteHeapStmt implements Statement{
    private final String varName;
    private final Expression expr;

    public WriteHeapStmt(String varName, Expression expr){
        this.varName = varName;
        this.expr = expr;
    }

    @Override
    public  ProgramState execute(ProgramState state) throws Exception {
        /*
        First we check if var_name is a variable defined in SymTable, if its type is a Ref type
        and if the address from the RefValue associated in SymTable is a key in Heap.
        If not, the execution is stopped with an appropriate error message.
        */
        if(!state.getSymbolTable().containsKey(varName))
            throw new VariableNotDeclaredException(varName + " was not declared !");

        Value symTableValue = state.getSymbolTable().get(varName);

        if(!(symTableValue instanceof RefValue))
            throw new TypeMismatchException(symTableValue.toString() + " is not of RefType!");

        int address = ((RefValue) symTableValue).getAddr();

        if(! state.getHeap().isDefined(address))
            throw new VariableNotDeclaredException("The address '" + address + "' was not found in the heap!");

        /*
        Secondly, the expression is evaluated and the result must have its type equal to the locationType of the var_name type.
        If not, the execution is stopped with an appropriate message.
        */
        Value expressionValue = expr.evaluate(state.getSymbolTable(), state.getHeap());

        if(!expressionValue.getType().equals(((RefValue) symTableValue).getLocationType()))
            throw new TypeMismatchException(varName + " is of type " + ((RefValue) symTableValue).getLocationType().toString() +
                      ", not " + expressionValue.getType().toString());

        /*
        Now access the Heap using the address from var_name and that Heap entry is updated
        to the result of the expression evaluation.
        */
        state.getHeap().put(address, expressionValue);

        return null;
    }

    @Override
    public IDictionary<String, Type> typeCheck(IDictionary<String, Type> typeEnv) throws Exception {
        Type typeVar = typeEnv.get(varName);
        Type typeExp = expr.typeCheck(typeEnv);
        if(typeVar.equals(new RefType(typeExp)))
            return typeEnv;
        throw new TypeMismatchException("WriteHeap: right hand side and left hand side have different types!");
    }

    @Override
    public String toString() {
        return  "wH(" + varName + ", " + expr.toString() + ")";
    }
}

/*
Example:

Ref int v;
new(v,20);
print(rH(v));
wH(v,30);
print(rH(v)+5);

Heap={1->30},
SymTable={v->(1,int)}
Out={20, 35}
 */
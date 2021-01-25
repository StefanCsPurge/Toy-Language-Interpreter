package Model.statement;

import Exceptions.TypeMismatchException;
import Exceptions.VariableNotDeclaredException;
import Model.ADT.IDictionary;
import Model.ProgramState;
import Model.expression.Expression;
import Model.type.*;
import Model.value.Value;

public class AssignStmt implements Statement {
    private final String ID;
    private final Expression expression;

    public AssignStmt(String ID, Expression exp){
        this.expression = exp;
        this.ID = ID;
    }

    @Override
    public ProgramState execute(ProgramState state) throws Exception {
        var symbolTable = state.getSymbolTable();
        if(!symbolTable.containsKey(ID))
            throw new VariableNotDeclaredException(String.format("Variable '%s' is not declared!",ID));

        Value resultValue = expression.evaluate(symbolTable, state.getHeap()); // same as symbolTable.get(ID);
        Type actualType = symbolTable.get(ID).getType();
        if(!resultValue.getType().equals(actualType))
            throw new TypeMismatchException(String.format("Variable '%s' is of type %s, not %s!",ID,actualType,resultValue.getType()));

        symbolTable.set(ID,resultValue);  // assign the new value

        return null;
    }

    @Override
    public IDictionary<String,Type> typeCheck(IDictionary<String,Type> typeEnv) throws Exception{
        Type typeVar = typeEnv.get(ID);
        Type typeExp = expression.typeCheck(typeEnv);
        if(typeVar.equals(typeExp))
            return typeEnv;
        throw new TypeMismatchException("Assignment: right hand side and left side have different types!");
    }

    @Override
    public String toString() {
        return ID + "=" + expression.toString();
    }
}

/*
AssignStmt execution: an assignment statement is on top of the stack
ExeStack1={Id=Exp| Stmt1|....}
SymTable1
Out1
==>
    ExeStack2={Stmt1|...}
    SymTable2=if IsVarDef(SymTable1,Id) then
                    Val1=Eval(Exp)
                    If Type(Val1) == GetType(SymTable1,Id) then
                            Update(SymTable1,Id,Val1)
                    else Error: "Type of expression and type of variable do not match"
                else Error: "Variable Id is not declared"
    Out2 = Out1
 */
package Model.statement;

import Exceptions.BoolExprExpectedException;
import Exceptions.TypeMismatchException;
import Model.ADT.IDictionary;
import Model.ProgramState;
import Model.expression.Expression;
import Model.type.BoolType;
import Model.type.Type;
import Model.value.BoolValue;
import Model.value.Value;

public class WhileStmt implements Statement{

    private final Expression expression;
    private final Statement statement;

    public WhileStmt(Expression expr, Statement stmt){
        this.expression = expr;
        this.statement = stmt;
    }

    @Override
    public ProgramState execute(ProgramState state) throws Exception {
        Value resultedValue = expression.evaluate(state.getSymbolTable(), state.getHeap());
        if(!resultedValue.getType().equals(new BoolType()))
            throw new BoolExprExpectedException();

        if(((BoolValue) resultedValue).getValue()){
            state.getExecutionStack().push(new WhileStmt(expression,statement));
            state.getExecutionStack().push(statement);
        }
        return null;
    }

    @Override
    public IDictionary<String,Type> typeCheck(IDictionary<String,Type> typeEnv) throws Exception{
        Type typExp = expression.typeCheck(typeEnv);
        if(! typExp.equals(new BoolType()))
            throw new TypeMismatchException("The condition of WHILE is not of type bool!");
        return statement.typeCheck(typeEnv);
    }

    @Override
    public String toString(){
        return "( while(" + expression.toString() + ") " + statement.toString() + ')';
    }
}

/*
While statement execution

Stack1={while (exp1) Stmt1 | Stmt2|...}
SymTable1
Out1
HeapTable1
FileTable1
==>
    If exp1 is not evaluated to BoolValue then
        throws new MyException("condition exp is not a boolean")
    If exp1 is evaluated to False then
        Stack2={Stmt2|...}
    Else
        Stack2={Stmt1 | while (exp1) Stmt1 | Stmt2|...}

SymTable2 = SymTable1
Out2 = Out1
HeapTable2 = HeapTable1
FileTable2 = FileTable1

Example:

int v; v=4;
(while (v>0) print(v); v=v-1);
print(v);
 */
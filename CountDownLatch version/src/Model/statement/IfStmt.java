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

public class IfStmt implements Statement{
    private final Expression expression;
    private final Statement thenBlock;
    private final Statement elseBlock;

    public IfStmt(Expression exp, Statement thenBlock, Statement elseBlock){
        this.expression = exp;
        this.thenBlock = thenBlock;
        this.elseBlock = elseBlock;
    }

    @Override
    public ProgramState execute(ProgramState state) throws Exception {
        Value resultedValue = expression.evaluate(state.getSymbolTable(), state.getHeap());
        if(!resultedValue.getType().equals(new BoolType()))
            throw new BoolExprExpectedException();

        var executionStack = state.getExecutionStack();
        BoolValue boolVal = (BoolValue) resultedValue;
        if(boolVal.getValue())
            executionStack.push(thenBlock);
        else
            executionStack.push(elseBlock);
        return null;
    }

    @Override
    public IDictionary<String, Type> typeCheck(IDictionary<String,Type> typeEnv) throws Exception{
        Type typExp = expression.typeCheck(typeEnv);
        if(! typExp.equals(new BoolType()))
            throw new TypeMismatchException("The condition of IF is not of type bool!");
        thenBlock.typeCheck(typeEnv.cloneD());
        elseBlock.typeCheck(typeEnv.cloneD());
        return typeEnv;
    }

    @Override
    public String toString() {
        return "if (" + expression.toString() + ") " + thenBlock.toString() + " else " + elseBlock.toString();
    }
}

/* If statement execution

ExeStack1={If Exp Then Stmt1 Else Stmt2 | Stmt3|....}
SymTable1
Out1
==>
    Cond= Eval(Exp)
    if getType(Cond)!=bool) error "conditional expr is not a boolean"
    else
            if Cond ==True ExeStack2={Stmt1|Stmt3|...}
            else ExeStack2={Stmt2|Stmt3|...}
    SymTable2=SymTable1
    Out2 = Out1
*/

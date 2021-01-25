package Model.statement;

import Model.ADT.IDictionary;
import Model.ADT.Stack;
import Model.ProgramState;
import Model.type.Type;

public class ForkStmt implements Statement{

    private final Statement statement;

    public ForkStmt(Statement statement){
        this.statement = statement;
    }

    @Override
    public ProgramState execute(ProgramState state){
        return new ProgramState(new Stack<>(),                  // new execution stack
                                state.getSymbolTable().cloneD(),
                                state.getOutput(),
                                state.getFileTable(),
                                state.getHeap(),
                                state.getLockTable(),
                                statement);
    }

    @Override
    public IDictionary<String,Type> typeCheck(IDictionary<String,Type> typeEnv) throws Exception{
        statement.typeCheck(typeEnv.cloneD());
        return typeEnv;
    }

    @Override
    public String toString() { return "fork(" + statement.toString() + ")"; }
}


/*
Fork execution:

ExeStack1={fork(Stmt1) | Stmt2|Stmt3|....}
SymTable1,
Heap1,
FileTable1,
Out1,
id1
==>

    ExeStack2={Stmt2 | Stmt3|.....}
    SymTable2=SymTable1
    Heap2 = Heap1
    FileTable2=FileTable1
    Out2 = Out1
    id2=id1
and a new PrgState is created with the following data structures:
        ExeStack3={Stmt1}
        SymTable3=clone(SymTable1)
        Heap3=Heap1,
        FileTable3=FileTable1
        Out3=Out1
        id3 is unique
 */
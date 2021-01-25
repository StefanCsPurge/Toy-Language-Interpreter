package Model.statement;

import Model.ADT.IDictionary;
import Model.ProgramState;
import Model.type.Type;

public class NopStmt implements Statement{

    @Override
    public ProgramState execute(ProgramState state) {
        return null;
    }

    @Override
    public IDictionary<String, Type> typeCheck(IDictionary<String, Type> typeEnv) {
        return typeEnv;
    }

    @Override
    public String toString() {
        return "nop";
    }
}

/*
NOP execution:
ExeStack1={Nop | Stmt1|....}
SymTable1
Out1
==>
    ExeStack2={Stmt1|...}
    SymTable2=SymTable1
    Out2 = Out1
 */
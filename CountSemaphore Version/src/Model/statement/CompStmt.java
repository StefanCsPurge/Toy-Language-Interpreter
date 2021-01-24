package Model.statement;

import Model.ADT.IDictionary;
import Model.ProgramState;
import Model.type.Type;

public class CompStmt implements Statement{
        private final Statement first;
        private final Statement second;

        public CompStmt(Statement first, Statement second){
            this.first = first;
            this.second = second;
        }

        @Override
        public IDictionary<String, Type> typeCheck(IDictionary<String,Type> typeEnv) throws Exception{
            return second.typeCheck(first.typeCheck(typeEnv));
        }

        @Override
        public String toString() {
            return "(" + first.toString() + "; " + second.toString()+ ")";
        }

        @Override
        public ProgramState execute(ProgramState state) {
            state.getExecutionStack().push(second);
            state.getExecutionStack().push(first);
            return null;
        }
}

/*
CompStmt execution: when a compound statement is the top of the ExeStack
ExeStack1={Stmt1;Stmt2 | Stmt3|....}
SymTable1
Out1
==>
    ExeStack2={Stmt1| Stmt2 | Stmt3|.....}
    SymTable2=SymTable1
    Out2 = Out1

As you can see, the top of the ExeStack is changed while SymTable and Out remain
unchanged.
*/
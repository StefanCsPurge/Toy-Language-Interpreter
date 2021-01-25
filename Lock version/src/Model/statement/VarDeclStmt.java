package Model.statement;

import Exceptions.VariableAlreadyDeclaredException;
import Model.ADT.IDictionary;
import Model.ProgramState;
import Model.type.Type;

public class VarDeclStmt implements Statement{
    private final Type type;
    private final String ID; // key in dictionary

    public VarDeclStmt(String ID, Type t){
        this.type = t;
        this.ID = ID;
    }

    @Override
    public ProgramState execute(ProgramState state) throws Exception {
        var symbolTable = state.getSymbolTable();
        if(symbolTable.containsKey(ID))
            throw new VariableAlreadyDeclaredException(String.format("Variable '%s' was already declared",ID));

        symbolTable.set(ID, type.defaultValue());

        return null;
    }

    @Override
    public IDictionary<String,Type> typeCheck(IDictionary<String,Type> typeEnv){
        typeEnv.set(ID,type);
        return typeEnv;
    }

    @Override
    public String toString() {
        return type.toString() + " " + ID;
    }
}

/*
VarDecl execution: a variable declaration statement is on top of the stack
ExeStack1={Type Id | Stmt1|....}
SymTable1
Out1
==>
    ExeStack1={Stmt1|....}
    if IsVarDef(SymTable1,Id) == True) then Error: "variable is already declared"
    else SymTable2 = SymTable1 U {Id-> default value}
    Out1

where default value of the variable is determined based on the variable declared type, as
follows:

defaultValue(int) = 0
defaultValue(bool) = false
 */

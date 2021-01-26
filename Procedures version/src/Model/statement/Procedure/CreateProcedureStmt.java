package Model.statement.Procedure;

import Model.ADT.IDictionary;
import Model.ProgramState;
import Model.statement.Statement;
import Model.type.Type;

import java.util.ArrayList;
import java.util.Arrays;

public class CreateProcedureStmt implements Statement {
    private final String procName;
    private final ArrayList<String> formalParameters;
    private final Statement procBody;

    public CreateProcedureStmt(String procName, Statement procBody, String... parameters) {
        this.procName = procName;
        this.formalParameters = new ArrayList<> (Arrays.asList(parameters));
        this.procBody = procBody;
    }

    @Override
    public ProgramState execute(ProgramState state) throws Exception {
        state.getProcTable().put(procName, formalParameters,procBody);
        return null;
    }

    @Override
    public IDictionary<String, Type> typeCheck(IDictionary<String, Type> typeEnv) throws Exception {
        return typeEnv;
    }

    @Override
    public String toString() {
        return "procedure " + procName + "" + formalParameters + " " + procBody.toString() + "";
    }
}

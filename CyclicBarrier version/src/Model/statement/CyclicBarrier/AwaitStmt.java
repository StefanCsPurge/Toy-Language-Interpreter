package Model.statement.CyclicBarrier;

import Exceptions.TypeMismatchException;
import Exceptions.VariableNotDeclaredException;
import Model.ADT.IDictionary;
import Model.ProgramState;
import Model.statement.Statement;
import Model.type.IntType;
import Model.type.Type;
import Model.value.IntValue;

public class AwaitStmt implements Statement {
    private final String var;

    public AwaitStmt(String var) {
        this.var = var;
    }

    @Override
    public ProgramState execute(ProgramState state) throws Exception {
        if (!state.getSymbolTable().containsKey(var))
            throw new VariableNotDeclaredException("Await variable not declared!");

        int foundIndex = ((IntValue)state.getSymbolTable().get(var)).getValue();
        if (!state.getBarrierTable().isDefined(foundIndex))
            throw new Exception("Found index in not an index in the BarrierTable!");

        var pair = state.getBarrierTable().get(foundIndex);
        var list1 = pair.getValue();
        int NL = list1.size();
        int N1 = pair.getKey();

        if(N1 > NL){
            int stateID = state.getID();
            if(!list1.contains(stateID))
                list1.add(stateID);
            state.getExecutionStack().push(new AwaitStmt(var));
        }
        return null;
    }

    @Override
    public IDictionary<String, Type> typeCheck(IDictionary<String, Type> typeEnv) throws Exception {
        Type type = typeEnv.get(var);
        if (!type.equals(new IntType()))
            throw new TypeMismatchException("Await variable is not of type int!");
        return typeEnv;
    }

    @Override
    public String toString() {
        return "await(" + var + ")";
    }
}

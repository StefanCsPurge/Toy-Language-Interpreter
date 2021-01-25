package Model.statement;

import Exceptions.*;
import Model.ADT.IDictionary;
import Model.ProgramState;
import Model.expression.Expression;
import Model.type.IntType;
import Model.type.StringType;
import Model.type.Type;
import Model.value.IntValue;
import Model.value.StringValue;
import Model.value.Value;

import java.io.BufferedReader;

public class ReadFileStmt implements Statement{
    private final Expression expression;
    private final String var_name;

    public ReadFileStmt(Expression e, String v_name) {
        expression = e;
        var_name = v_name;
    }

    @Override
    public ProgramState execute(ProgramState state) throws Exception{

        if(!state.getSymbolTable().containsKey(var_name))
            throw new VariableNotDeclaredException(String.format("'%s' was not declared!",var_name));

        if(!state.getSymbolTable().get(var_name).getType().equals(new IntType()))
            throw new TypeMismatchException(String.format("'%s' is not of type int!",var_name));

        Value resultedValue = expression.evaluate(state.getSymbolTable(), state.getHeap());

        if (!resultedValue.getType().equals(new StringType()))
            throw new StringExprExpectedException();

        String fileName = ((StringValue)resultedValue).getValue();
        if(!state.getFileTable().containsKey(fileName))
            throw new FileNotInFileTableException(String.format("'%s' does not exist in the file table!",fileName));

        BufferedReader fileDescriptor = state.getFileTable().get(fileName);
        String line = fileDescriptor.readLine();
        IntValue intVal;
        if(line == null)
            intVal = new IntValue(0);
        else
            intVal = new IntValue(Integer.parseInt(line));

        state.getSymbolTable().set(var_name,intVal);

        return null;
    }

    @Override
    public IDictionary<String, Type> typeCheck(IDictionary<String, Type> typeEnv) throws Exception {
        Type typeVar = typeEnv.get(var_name);
        Type typeExp = expression.typeCheck(typeEnv);
        if(!typeVar.equals(new IntType()))
            throw new TypeMismatchException("ReadFile: variable is not of type int!");
        if(typeExp.equals(new StringType()))
            return typeEnv;
        throw new TypeMismatchException("ReadFile: expression is not a string!");
    }

    @Override
    public String toString() {
        return "read("+expression.toString() + "," + var_name + ")";
    }
}

package Model.statement;

import Exceptions.FileAlreadyExistsException;
import Exceptions.StringExprExpectedException;
import Exceptions.TypeMismatchException;
import Model.ADT.IDictionary;
import Model.ProgramState;
import Model.expression.Expression;
import Model.type.StringType;
import Model.type.Type;
import Model.value.StringValue;
import Model.value.Value;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class OpenRFileStmt implements Statement {
    private final Expression expression;

    public OpenRFileStmt(Expression e) { expression = e; }

    @Override
    public ProgramState execute(ProgramState state) throws Exception{
        Value resultedValue = expression.evaluate(state.getSymbolTable(), state.getHeap());
        if (!resultedValue.getType().equals(new StringType()))
            throw new StringExprExpectedException();

        String value = ((StringValue)resultedValue).getValue();
        if(state.getFileTable().containsKey(value))
            throw new FileAlreadyExistsException(String.format("'%s' already exists in the file table!",value));
        try {
            BufferedReader fileDescriptor = new BufferedReader(new FileReader(value));
            state.getFileTable().set(value, fileDescriptor);
        }
        catch (FileNotFoundException e) {
                throw new Exception(e.getMessage());
            }
        return null;
    }

    @Override
    public IDictionary<String, Type> typeCheck(IDictionary<String, Type> typeEnv) throws Exception {
        Type typExp = expression.typeCheck(typeEnv);
        if(typExp.equals(new StringType()))
            return typeEnv;
        throw new TypeMismatchException("OpenFile: expression type is not String!");
    }

    @Override
    public String toString() {
        return "open(" + expression.toString() + ")";
    }
}

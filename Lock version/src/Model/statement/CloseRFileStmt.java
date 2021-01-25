package Model.statement;

import Exceptions.FileNotInFileTableException;
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

public class CloseRFileStmt implements Statement{
    private final Expression expression;

    public CloseRFileStmt(Expression e) { expression = e; }

    @Override
    public ProgramState execute(ProgramState state) throws Exception{
        Value resultedValue = expression.evaluate(state.getSymbolTable(), state.getHeap());

        if (!resultedValue.getType().equals(new StringType()))
            throw new StringExprExpectedException();

        String fileName = ((StringValue)resultedValue).getValue();
        if(!state.getFileTable().containsKey(fileName))
            throw new FileNotInFileTableException(String.format("'%s' does not exist in the file table!",fileName));

        BufferedReader fileDescriptor = state.getFileTable().get(fileName);
        fileDescriptor.close();
        state.getFileTable().remove(fileName);

        return null;
    }

    @Override
    public IDictionary<String, Type> typeCheck(IDictionary<String, Type> typeEnv) throws Exception {
        Type typExp = expression.typeCheck(typeEnv);
        if(typExp.equals(new StringType()))
            return typeEnv;
        throw new TypeMismatchException("CloseRFile: expression type is not string!");
    }

    @Override
    public String toString() {
        return "close(" + expression.toString() + ")";
    }
}

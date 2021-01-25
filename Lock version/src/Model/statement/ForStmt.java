package Model.statement;

import Exceptions.TypeMismatchException;
import Model.ADT.IDictionary;
import Model.ADT.IStack;
import Model.ProgramState;
import Model.expression.Expression;
import Model.expression.RelationalExpression;
import Model.expression.VariableExpression;
import Model.type.IntType;
import Model.type.Type;

public class ForStmt implements Statement{
    private final String v;
    private final Expression exp1;
    private final Expression exp2;
    private final Expression exp3;
    private final Statement stmt;

    public ForStmt(String v, Expression exp1, Expression exp2, Expression exp3, Statement stmt) {
        this.v = v;
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.exp3 = exp3;
        this.stmt = stmt;
    }

    @Override
    public ProgramState execute(ProgramState state) throws Exception {

        Statement equiv = new CompStmt(new VarDeclStmt(v, new IntType()),
                                        new CompStmt(new AssignStmt(v,exp1),
                                                new WhileStmt(new RelationalExpression("<", new VariableExpression(v), exp2),
                                                        new CompStmt(stmt, new AssignStmt(v,exp3)))));
        state.getExecutionStack().push(equiv);
        return null;
    }

    @Override
    public IDictionary<String, Type> typeCheck(IDictionary<String, Type> typeEnv) throws Exception {
        Type type1 = exp1.typeCheck(typeEnv);
        Type type2 = exp2.typeCheck(typeEnv);
        var cloneTypeEnv = typeEnv.cloneD();
        cloneTypeEnv.set(v,new IntType());
        Type type3 = exp3.typeCheck(cloneTypeEnv);
        if(!type1.equals(new IntType()) || !type2.equals(new IntType()) ||  !type3.equals(new IntType()))
            throw new TypeMismatchException("For expression types are not of the same type int!");
        return typeEnv;
    }

    @Override
    public String toString() {
        return "for("+v+"="+exp1.toString()+";"+v+"<"+exp2.toString()+";"+v+"="+exp3.toString()+")"+ stmt.toString();
    }
}

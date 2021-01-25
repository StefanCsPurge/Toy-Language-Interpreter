package Model.statement;

import Exceptions.TypeMismatchException;
import Model.ADT.IDictionary;
import Model.ProgramState;
import Model.expression.Expression;
import Model.type.BoolType;
import Model.type.Type;

public class CondAssignStmt implements Statement{
    private final String v;
    private final Expression exp;
    private final Expression exp1;
    private final Expression exp2;

    public CondAssignStmt(String v, Expression exp, Expression exp1, Expression exp2) {
        this.v = v;
        this.exp = exp;
        this.exp1 = exp1;
        this.exp2 = exp2;
    }

    @Override
    public ProgramState execute(ProgramState state) throws Exception {
        Statement equiv = new IfStmt(exp, new AssignStmt(v, exp1), new AssignStmt(v, exp2));
        state.getExecutionStack().push(equiv);
        return null;
    }

    @Override
    public IDictionary<String, Type> typeCheck(IDictionary<String, Type> typeEnv) throws Exception {
        Type type1 = exp.typeCheck(typeEnv);
        if(!type1.equals(new BoolType()))
            throw new TypeMismatchException("Conditional assignment first expression is not of type bool!");
        Type type2 = exp1.typeCheck(typeEnv);
        Type type3 = exp2.typeCheck(typeEnv);
        Type typeVar = typeEnv.get(v);
        if(!type2.equals(type3) || !type2.equals(typeVar))
            throw new TypeMismatchException("Conditional assignment variable, 2nd expression and 3rd expression are not of the same type!");
        return typeEnv;
    }

    @Override
    public String toString(){
        return v+"="+exp.toString()+"?"+exp1.toString()+":"+exp2.toString();
    }
}

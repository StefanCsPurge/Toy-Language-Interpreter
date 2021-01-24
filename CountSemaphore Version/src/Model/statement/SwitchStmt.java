package Model.statement;

import Exceptions.TypeMismatchException;
import Model.ADT.IDictionary;
import Model.ProgramState;
import Model.expression.Expression;
import Model.expression.RelationalExpression;
import Model.type.Type;

public class SwitchStmt implements Statement{
    private final Expression exp;
    private final Expression exp1;
    private final Expression exp2;
    private final Statement case1;
    private final Statement case2;
    private final Statement default_case;

    public SwitchStmt(Expression exp, Expression exp1, Statement case1, Expression exp2, Statement case2, Statement default_case){
        this.exp = exp;
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.case1 = case1;
        this.case2 = case2;
        this.default_case = default_case;
    }

    @Override
    public ProgramState execute(ProgramState state) throws Exception {
        var executionStack = state.getExecutionStack();
        Statement equivalent = new IfStmt(new RelationalExpression("==", exp,exp1), case1, new IfStmt(new RelationalExpression("==", exp, exp2), case2, default_case));
        executionStack.push(equivalent);
        return null;
    }

    @Override
    public IDictionary<String, Type> typeCheck(IDictionary<String, Type> typeEnv) throws Exception {
        Type type = exp.typeCheck(typeEnv);
        Type type1 = exp1.typeCheck(typeEnv);
        if(! type.equals(type1))
            throw new TypeMismatchException("Expressions are not of the same type!");
        Type type2 = exp2.typeCheck(typeEnv);
        if(! type.equals(type2))
            throw new TypeMismatchException("Expressions are not of the same type!");

        case1.typeCheck(typeEnv.cloneD());
        case2.typeCheck(typeEnv.cloneD());
        default_case.typeCheck(typeEnv.cloneD());
        return typeEnv;
    }

    @Override
    public String toString(){
        return "switch(" + exp.toString() + ") " +
               "(case (" + exp1.toString() + ") : " + case1.toString() + ") " +
               "(case (" + exp2.toString() + ") : " + case2.toString() + ") " +
               "(default : " + default_case.toString() + ") ";
    }
}

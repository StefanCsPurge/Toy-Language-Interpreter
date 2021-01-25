package SyntaxParsing;

import Model.expression.*;
import Model.value.BoolValue;
import Model.value.IntValue;

public class ExpressionParser {

    protected static ReadHeapExpression getReadHeapExpression(String s){
        String param = s.trim().replaceAll("^rH\\(|\\)$", "");
        if(!param.matches("rH\\(\\S+\\)"))
            return new ReadHeapExpression(new VariableExpression(param));
        return new ReadHeapExpression(getReadHeapExpression(param));
    }

    protected static RelationalExpression getRelationalExpression(String s) throws Exception{
        s = s.trim().replaceAll(" +", "");  // remove spaces
        s = s.trim().replaceAll("(<=|==|!=|>=|>|<)"," $1 ");
        String[] parts = s.split(" ");
        if(parts.length != 3)
            throw new Exception("Invalid syntax: " + s);
        Expression e1, e2;
        if(parts[0].matches("\\d+"))
            e1 = new ValueExpression(new IntValue(Integer.parseInt(parts[0])));
        else
            e1 = new VariableExpression(parts[0]);
        if(parts[2].matches("\\d+"))
            e2 = new ValueExpression(new IntValue(Integer.parseInt(parts[2])));
        else
            e2 = new VariableExpression(parts[2]);
        return new RelationalExpression(parts[1],e1,e2);
    }

    protected static ArithmeticExpression getArithmeticExpression(String s) throws Exception{
        s = s.trim().replaceAll("\\+"," + ");
        s = s.trim().replaceAll("-"," - ");
        String[] parts = s.split(" ");
        if(parts.length == 1 && s.matches(".*[/*].*")){       // just multiplication or division, not mixed with addition or subtraction
            s = s.trim().replaceAll("\\*"," * ");
            s = s.trim().replaceAll("/"," / ");
            parts = s.split(" ");
            return getFirstArithmeticExpression(parts,parts.length-2);
        }
        return getSecondArithmeticExpression(parts,parts.length-2);
    }

    protected static LogicalExpression getLogicalExpression(String s) throws Exception{
        s = s.trim().replaceAll("_or_", " or ");
        String[] parts = s.split(" ");
        if(parts.length == 1 && s.matches(".+_and_.+")){
            s = s.trim().replaceAll("_and_", " and ");
            parts = s.split(" ");
            return getAndLogicalExpression(parts,1);
        }
        return getOrLogicalExpression(parts,1);
    }


    // Arithmetic expression helper methods

    /** Deals with chained additions and subtractions */
    protected static ArithmeticExpression getSecondArithmeticExpression(String[] parts, int idx) throws Exception{
        if(idx <= 0 || idx >= parts.length)
            throw new Exception("Arithmetic expression parts index out of bounds!");

        Expression e2;
        if(!parts[idx+1].matches(".*[/*].*")){
            if(parts[idx+1].matches("\\d+"))
                e2 = new ValueExpression(new IntValue(Integer.parseInt(parts[idx+1])));
            else if(parts[idx+1].matches("rH\\(\\S+\\)"))
                e2 = getReadHeapExpression(parts[idx+1]);
            else e2 = new VariableExpression(parts[idx+1]);
        }
        else{
            parts[idx+1] = parts[idx+1].trim().replaceAll("\\*"," * ").replaceAll("/"," / ");
            String[] firstParts = parts[idx+1].split(" ");
            e2 = getFirstArithmeticExpression(firstParts,firstParts.length-2);
        }

        if(idx - 1 == 0){
            Expression e1;
            if(!parts[idx-1].matches(".*[/*].*")){
                if(parts[idx-1].matches("\\d+"))
                    e1 = new ValueExpression(new IntValue(Integer.parseInt(parts[idx-1])));
                else if(parts[idx-1].matches("rH\\(\\S+\\)"))
                    e1 = getReadHeapExpression(parts[idx-1]);
                else e1 = new VariableExpression(parts[idx-1]);
            }
            else{
                parts[idx-1] = parts[idx-1].trim().replaceAll("\\*"," * ").replaceAll("/"," / ");
                String[] firstParts = parts[idx-1].split(" ");
                e1 = getFirstArithmeticExpression(firstParts,firstParts.length-2);
            }
            return new ArithmeticExpression(parts[idx].charAt(0), e1, e2);
        }
        return new ArithmeticExpression(parts[idx].charAt(0), getSecondArithmeticExpression(parts,idx-2), e2);
    }

    /** Deals with chained multiplications and divisions */
    protected static ArithmeticExpression getFirstArithmeticExpression(String[] parts, int idx) throws Exception{
        if(idx <= 0 || idx >= parts.length)
            throw new Exception("Arithmetic expression parts index out of bounds!");

        Expression e2;
        if(parts[idx + 1].matches("\\d+"))
            e2 = new ValueExpression(new IntValue(Integer.parseInt(parts[idx+1])));
        else if(parts[idx+1].matches("rH\\(\\S+\\)"))
            e2 = getReadHeapExpression(parts[idx+1]);
        else
            e2 = new VariableExpression(parts[idx+1]);

        if(idx - 1 == 0){
            Expression e1;
            if(parts[idx - 1].matches("\\d+"))
                e1 = new ValueExpression(new IntValue(Integer.parseInt(parts[idx-1])));
            else if(parts[idx-1].matches("rH\\(\\S+\\)"))
                e1 = getReadHeapExpression(parts[idx-1]);
            else
                e1 = new VariableExpression(parts[idx-1]);
            return new ArithmeticExpression(parts[idx].charAt(0), e1, e2);
        }
        return new ArithmeticExpression(parts[idx].charAt(0),getFirstArithmeticExpression(parts,idx-2), e2);
    }


    // Logical expression helper methods

    protected static LogicalExpression getAndLogicalExpression(String[] parts, int idx) throws Exception {
        if(idx <= 0 || idx >= parts.length-1)
            throw new Exception("Logical expression parts index out of bounds!");

        Expression e1;
        if(parts[idx-1].matches("(true|false)"))
            e1 = new ValueExpression(new BoolValue(Boolean.parseBoolean(parts[idx-1])));
        else
            e1 = new VariableExpression(parts[idx-1]);
        if(idx + 2 == parts.length){
            Expression e2;
            if(parts[idx+1].matches("(true|false)"))
                e2 = new ValueExpression(new BoolValue(Boolean.parseBoolean(parts[idx+1])));
            else
                e2 = new VariableExpression(parts[idx+1]);
            return new LogicalExpression(parts[idx], e1, e2);
        }
        return new LogicalExpression(parts[idx], e1, getAndLogicalExpression(parts,idx+2));
    }

    protected static LogicalExpression getOrLogicalExpression(String[] parts, int idx) throws Exception {
        if(idx <= 0 || idx >= parts.length-1)
            throw new Exception("Logical expression parts index out of bounds!");

        Expression e1;
        if(parts[idx-1].matches(".+_and_.+")){
            parts[idx-1] = parts[idx-1].trim().replaceAll("_and_", " and ");
            String[] andParts = parts[idx-1].split(" ");
            e1 = getAndLogicalExpression(andParts,1);
        }
        else{
            if(parts[idx-1].matches("(true|false)"))
                e1 = new ValueExpression(new BoolValue(Boolean.parseBoolean(parts[idx-1])));
            else e1 = new VariableExpression(parts[idx-1]);
        }

        if(idx + 2 == parts.length){
            Expression e2;
            if(parts[idx+1].matches(".+_and_.+")){
                parts[idx+1] = parts[idx+1].trim().replaceAll("_and_", " and ");
                String[] andParts = parts[idx+1].split(" ");
                e2 = getAndLogicalExpression(andParts,1);
            }
            else {
                if (parts[idx + 1].matches("(true|false)"))
                    e2 = new ValueExpression(new BoolValue(Boolean.parseBoolean(parts[idx + 1])));
                else e2 = new VariableExpression(parts[idx + 1]);
            }
            return new LogicalExpression(parts[idx], e1, e2);
        }
        return new LogicalExpression(parts[idx], e1, getOrLogicalExpression(parts, idx+2));
    }
}

package SyntaxParsing;

import Model.expression.Expression;
import Model.expression.ValueExpression;
import Model.expression.VariableExpression;
import Model.statement.*;
import Model.type.BoolType;
import Model.type.IntType;
import Model.type.RefType;
import Model.type.StringType;
import Model.value.BoolValue;
import Model.value.IntValue;
import Model.value.StringValue;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StatementParser {

    protected static Statement getSingleStatement(String instruction) throws Exception
    {
        if(instruction.matches("Print(.+)"))
            return getPrintStmt(instruction);
        if(instruction.matches("openRFile(.+)"))
            return getOpenRFileStmt(instruction);
        if(instruction.matches("closeRFile(.+)"))
            return getCloseRFileStmt(instruction);
        if(instruction.matches("readFile(.+,.+)"))
            return getReadFileStmt(instruction);
        if(instruction.matches("new(.+,.+)"))
            return getNewStmt(instruction);
        if(instruction.matches("wH(.+,.+)"))
            return getWriteHeapStmt(instruction);
        if(instruction.matches("fork(.+)"))
            return getForkStmt(instruction);
        if (instruction.matches("(Ref )*(int|bool|string) [a-zA-Z]+[0-9]*"))
            return getVarDeclStmt(instruction);
        if (instruction.matches("\\(If .+ Then .+ Else .+\\)"))
            return getIfStmt(instruction);
        if(instruction.matches("\\(\\s*while\\(\\S+\\) .+\\)"))
            return getWhileStmt(instruction);
        if (instruction.matches("[a-zA-Z]+[0-9]*\\s*=\\s*.+"))
            return getAssignStmt(instruction);
        if (instruction.equals("Nop"))
            return new NopStmt();
        throw new Exception("Invalid syntax @ : " + instruction);
    }

    private static PrintStmt getPrintStmt(String s) throws Exception{
        String param = s.trim().replaceAll("^Print\\(|\\)$", "");
        if(param.matches("\\d+"))
            return new PrintStmt(new ValueExpression(new IntValue(Integer.parseInt(param))));
        if(param.matches("true|false"))
            return new PrintStmt(new ValueExpression(new BoolValue(Boolean.parseBoolean(param))));
        if(param.matches(".+(<|<=|==|!=|>|>=).+"))
            return new PrintStmt(ExpressionParser.getRelationalExpression(param));
        if(param.matches("\".+\""))
            return new PrintStmt(new ValueExpression(new StringValue(param.substring(1, param.length() - 1))));
        if(param.matches("rH\\(\\S+\\)"))
            return new PrintStmt(ExpressionParser.getReadHeapExpression(param));
        if(param.matches("\\S+([+*/-]\\S+)+"))
            return new PrintStmt(ExpressionParser.getArithmeticExpression(param));
        return new PrintStmt(new VariableExpression(param));
    }

    private static OpenRFileStmt getOpenRFileStmt(String s){
        String param = s.trim().replaceAll("openRFile\\(|\\)", "");
        return new OpenRFileStmt(new VariableExpression(param));
    }

    private static CloseRFileStmt getCloseRFileStmt(String s){
        String param = s.trim().replaceAll("closeRFile\\(|\\)", "");
        return new CloseRFileStmt(new VariableExpression(param));
    }

    private static ReadFileStmt getReadFileStmt(String s){
        String[] param = s.trim().replaceAll("readFile\\(|\\)", "").split(",");
        return new ReadFileStmt(new VariableExpression(param[0]),param[1]);
    }

    private static NewStmt getNewStmt(String s) {
        String[] param = s.trim().replaceAll("new\\(|\\)", "").split(",");
        if(param[1].matches("\\d+"))
            return new NewStmt(param[0], new ValueExpression(new IntValue(Integer.parseInt(param[1]))));
        if(param[1].matches("true|false"))
            return new NewStmt(param[0], new ValueExpression(new BoolValue(Boolean.parseBoolean(param[1]))));
        if(param[1].matches("\".+\""))
            return new NewStmt(param[0], new ValueExpression(new StringValue(param[1].substring(1, param[1].length() - 1))));
        return new NewStmt(param[0], new VariableExpression(param[1]));
    }

    private static WriteHeapStmt getWriteHeapStmt(String s){
        String[] param = s.trim().replaceAll("wH\\(|\\)", "").split(",");
        if(param[1].matches("\\d+"))
            return new WriteHeapStmt(param[0], new ValueExpression(new IntValue(Integer.parseInt(param[1]))));
        if(param[1].matches("true|false"))
            return new WriteHeapStmt(param[0], new ValueExpression(new BoolValue(Boolean.parseBoolean(param[1]))));
        if(param[1].matches("\".+\""))
            return new WriteHeapStmt(param[0], new ValueExpression(new StringValue(param[1].substring(1, param[1].length() - 1))));
        return new WriteHeapStmt(param[0], new VariableExpression(param[1]));
    }

    private static ForkStmt getForkStmt(String s) throws Exception {
        s = s.trim().replaceAll("^fork\\(|\\)$", "");
        s = s.trim().replaceAll(";", " ");
        s = s.trim().replaceAll("\\s+", " ");
        String[] parts = s.split(" ");
        List<String> instrList = Arrays.stream(parts).collect(Collectors.toList());
        return new ForkStmt(SyntaxParser.getProgramRec(instrList,0));
    }

    private static VarDeclStmt getVarDeclStmt(String s)throws Exception{
        String[] parts = s.split(" ");
        if(parts.length < 2)
            throw new Exception("Invalid syntax: " + s);
        if(parts[0].equals("int"))
            return new VarDeclStmt(parts[1],new IntType());
        if(parts[0].equals("bool"))
            return new VarDeclStmt(parts[1],new BoolType());
        if(parts[0].equals("string"))
            return new VarDeclStmt(parts[1],new StringType());
        if(parts.length>2 && parts[0].equals("Ref") && parts[parts.length-2].matches("int|bool|string"))
            return new VarDeclStmt(parts[parts.length-1], getRefType(parts,1));
        throw new Exception("Invalid syntax: " + s);
    }

    private static IfStmt getIfStmt(String s)throws Exception{
        s = s.substring(1, s.length() - 1); // remove parentheses
        String[] parts = s.split(" ");
        if(parts.length != 6)
            throw new Exception("Invalid syntax: " + s);
        if(parts[1].matches("true|false"))
            return new IfStmt(new ValueExpression(new BoolValue(Boolean.parseBoolean(parts[1]))), getSingleStatement(parts[3]), getSingleStatement(parts[5]));
        if(parts[1].matches(".+(<|<=|==|!=|>|>=).+"))
            return new IfStmt(ExpressionParser.getRelationalExpression(parts[1]),getSingleStatement(parts[3]),getSingleStatement(parts[5]));
        return new IfStmt(new VariableExpression(parts[1]), getSingleStatement(parts[3]), getSingleStatement(parts[5]));
    }

    private static WhileStmt getWhileStmt(String s) throws Exception{
        s = s.substring(1, s.length() - 1); // remove parentheses
        s = s.trim().replaceAll(";", " ");
        s = s.trim().replaceAll("\\s+", " ");
        String[] parts = s.split(" ");
        if(parts.length < 3)
            throw new Exception("Invalid syntax: " + s);
        String param = parts[0].trim().replaceAll("while\\(|\\)", "");

        Expression expr;
        if(param.matches("true|false"))
            expr = new ValueExpression(new BoolValue(Boolean.parseBoolean(param)));
        else if(param.matches(".+(<|<=|==|!=|>|>=).+"))
            expr = ExpressionParser.getRelationalExpression(param);
        else expr = new VariableExpression(param);

        List<String> instrList = Arrays.stream(parts).collect(Collectors.toList());
        instrList.remove(0);

        return new WhileStmt(expr, SyntaxParser.getProgramRec(instrList,0));
    }

    private static AssignStmt getAssignStmt(String s)throws Exception{
        if(s.matches(".+ (or|and) .+"))
            s = s.trim().replaceAll(" (or|and) ", "_$1_");
        s = s.trim().replaceAll(" +", "");
        String[] parts = s.split("=");
        if(parts.length != 2)
            throw new Exception("Invalid syntax: " + s);
        if(parts[1].matches("rH\\(\\s+\\)"))
            return new AssignStmt(parts[0], ExpressionParser.getReadHeapExpression(parts[1]));
        if(parts[1].matches("true|false"))
            return new AssignStmt(parts[0], new ValueExpression(new BoolValue(Boolean.parseBoolean(parts[1]))));
        if(parts[1].matches("[0-9]+"))
            return new AssignStmt(parts[0], new ValueExpression(new IntValue(Integer.parseInt(parts[1]))));
        if(parts[1].matches("\\S+([+*/-]\\S+)+"))
            return new AssignStmt(parts[0], ExpressionParser.getArithmeticExpression(parts[1]));
        if(parts[1].matches("\".+\""))
            return new AssignStmt(parts[0], new ValueExpression(new StringValue(parts[1].substring(1, parts[1].length() - 1))));  // remove the commas
        if(parts[1].matches("\\S+_(or|and)_\\S+"))
            return new AssignStmt(parts[0],ExpressionParser.getLogicalExpression(parts[1]));
        if (parts[1].matches("\\S+\\s*(<|<=|==|!=|>|>=)\\s*\\S+"))
            return new AssignStmt(parts[0],ExpressionParser.getRelationalExpression(parts[1]));
        throw new Exception("Invalid syntax: " + s);
    }

    private static RefType getRefType(String[] parts, int idx){
        if(idx == parts.length - 2){
            if(parts[idx].equals("int"))
                return new RefType(new IntType());
            if(parts[idx].equals("bool"))
                return new RefType(new BoolType());
            if(parts[idx].equals("string"))
                return new RefType(new StringType());
        }
        return new RefType(getRefType(parts, idx+1));
    }
}

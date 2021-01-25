package Main;/*

This is a Toy language interpreter. Proceed with caution !

Author:  S P U R G E

So this code is copyrighted m8.
~ ç'est la vie ~

 */

import Model.expression.*;
import Model.statement.*;
import Model.statement.Lock.LockStmt;
import Model.statement.Lock.NewLockStmt;
import Model.statement.Lock.UnlockStmt;
import Model.type.BoolType;
import Model.type.IntType;
import Model.type.RefType;
import Model.type.StringType;
import Model.value.BoolValue;
import Model.value.IntValue;
import Model.value.StringValue;
import View.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Main {
    public static void main(String[] args){

        Console console = new Console();
        List<Statement> examples = getExamples();
        console.addCommand(new ExitCommand("0","exit"));
        console.addCommand(new RunExampleCommand("1","int v; v=2; Print(v)",examples.get(0)));
        console.addCommand(new RunExampleCommand("2","int a; int b; a=2+3*5; b=a+1; Print(b)",examples.get(1)));
        console.addCommand(new RunExampleCommand("3","bool a; int v; a=true; (If a Then v=2 Else v=3); Print(v)",examples.get(2)));
        console.addCommand(new RunExampleCommand("4","string varf; varf=test.in; openRFile(varf); int varc; readFile(varf,varc); print(varc); readFile(varf,varc); print(varc); closeRFile(varf)",examples.get(3)));
        console.addCommand(new RunExampleCommand("5","Ref int v; new(v,20); Ref Ref int a; new(a,v); new(v,30); print(rH(rH(a))) // ok with safe garbage collector",examples.get(4)));
        console.addCommand(new RunExampleCommand("6","Ref int v; new(v,20); Ref Ref int a; new(a,v); wH(v,30); print(rH(rH(a)))",examples.get(5)));
        console.addCommand(new RunExampleCommand("7","int v; v=4; (while (v>0) print(v); v=v-1); print(v)",examples.get(6)));
        console.addCommand(new RunExampleCommand("8","int v; Ref int a; v=10; new(a,22); fork( wH(a,30); v=32; print(v); print(rH(a)) ); print(v); print(rH(a))",examples.get(7)));
        console.addCommand(new RunExampleCommand("9","*for example*",examples.get(8)));
        console.addCommand(new RunExampleCommand("10","*Lock example*",examples.get(9)));

        console.addCommand(new StepsDisplayCommand("show","ENABLE/DISABLE program state showing for each step - hardcoded examples only"));

        console.addCommand(new UseSyntaxParsingCMD("start","USE SYNTAX PARSING INTERPRETER -> write your program in 'toyEx.txt' !","toyEx.txt"));

        console.run();
    }

    public static List<Statement> getExamples() {
        List<Statement> examples = new ArrayList<>();

        // int v; v=2; Print(v)
        Statement ex1 = new CompStmt(new VarDeclStmt("v", new IntType()),
                new CompStmt(new AssignStmt("v", new ValueExpression(new IntValue(2))),
                        new PrintStmt(new VariableExpression("v"))));

        // int a; int b; a=2+3*5; b=a+1; Print(b)
        Statement ex2 = new CompStmt(new VarDeclStmt("a", new IntType()),
                new CompStmt(new VarDeclStmt("b", new IntType()),
                        new CompStmt(new AssignStmt("a", new ArithmeticExpression('+', new ValueExpression(new IntValue(2)),
                                new ArithmeticExpression('*', new ValueExpression(new IntValue(3)),
                                        new ValueExpression(new IntValue(5))))),
                                new CompStmt(new AssignStmt("b", new ArithmeticExpression('+', new VariableExpression("a"),
                                        new ValueExpression(new IntValue(1)))),
                                        new PrintStmt(new VariableExpression("b"))))));

        // bool a; int v; a=true; (If a Then v=2 Else v=3); Print(v)
        Statement ex3 = new CompStmt(new VarDeclStmt("a", new BoolType()),
                new CompStmt(new VarDeclStmt("v", new IntType()),
                        new CompStmt(new AssignStmt("a", new ValueExpression(new BoolValue(true))),
                                new CompStmt(new IfStmt(new VariableExpression("a"), new AssignStmt("v", new ValueExpression(new IntValue(2))),
                                        new AssignStmt("v", new ValueExpression(new IntValue(3)))),
                                        new PrintStmt(new VariableExpression("v"))))));

        // string varf; varf="test.in"; openRFile(varf); int varc; readFile(varf,varc); print(varc); readFile(varf,varc); print(varc); closeRFile(varf)
        Statement ex4 = new CompStmt(new VarDeclStmt("varf", new StringType()),
                new CompStmt(new AssignStmt("varf", new ValueExpression(new StringValue("test.in"))),
                        new CompStmt(new OpenRFileStmt(new VariableExpression("varf")),
                                new CompStmt(new VarDeclStmt("varc", new IntType()),
                                        new CompStmt(new ReadFileStmt(new VariableExpression("varf"), "varc"),
                                                new CompStmt(new PrintStmt(new VariableExpression("varc")),
                                                        new CompStmt(new ReadFileStmt(new VariableExpression("varf"), "varc"),
                                                                new CompStmt(new PrintStmt(new VariableExpression("varc")),
                                                                        new CloseRFileStmt(new VariableExpression("varf")))
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );

        // Ref int v; new(v,20); Ref Ref int a; new(a,v); new(v,30); print(rH(rH(a)))  -- returns heap error bc of the garbage collector
        Statement ex5 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(new NewStmt("v", new ValueExpression(new IntValue(20))),
                        new CompStmt(new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                new CompStmt(new NewStmt("a", new VariableExpression("v")),
                                        new CompStmt(new NewStmt("v", new ValueExpression(new IntValue(30))),
                                                new PrintStmt(new ReadHeapExpression(new ReadHeapExpression(new VariableExpression("a")))))
                                )
                        )
                )
        );

        // Ref int v; new(v,20); Ref Ref int a; new(a,v); wH(v,30); print(rH(rH(a)))  -- no error bc we write at the same addr in the heap
        Statement ex6 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(new NewStmt("v", new ValueExpression(new IntValue(20))),
                        new CompStmt(new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                new CompStmt(new NewStmt("a", new VariableExpression("v")),
                                        new CompStmt(new WriteHeapStmt("v", new ValueExpression(new IntValue(30))),    // here is the change
                                                new PrintStmt(new ReadHeapExpression(new ReadHeapExpression(new VariableExpression("a")))))
                                )
                        )
                )
        );

        // int v; v=4; (while(v>0) print(v); v=v-1); print(v)
        Statement ex7 = new CompStmt(new VarDeclStmt("v", new IntType()),
                new CompStmt(new AssignStmt("v", new ValueExpression(new IntValue(4))),
                        new CompStmt(new WhileStmt(new RelationalExpression(">", new VariableExpression("v"), new ValueExpression(new IntValue(0))),
                                new CompStmt(new PrintStmt(new VariableExpression("v")), new AssignStmt("v", new ArithmeticExpression('-',
                                        new VariableExpression("v"), new ValueExpression(new IntValue(1)))))),
                                new PrintStmt(new VariableExpression("v"))
                        )
                )
        );

        // int v; Ref int a; v=10; new(a,22); fork( wH(a,30); v=32; print(v); print(rH(a)) ); print(v); print(rH(a))
        Statement ex8 = new CompStmt(new VarDeclStmt("v", new IntType()),
                new CompStmt(new VarDeclStmt("a", new RefType(new IntType())),
                        new CompStmt(new AssignStmt("v", new ValueExpression(new IntValue(10))),
                                new CompStmt(new NewStmt("a", new ValueExpression(new IntValue(22))),
                                        new CompStmt(new ForkStmt(
                                                new CompStmt(new WriteHeapStmt("a", new ValueExpression(new IntValue(30))),
                                                        new CompStmt(new AssignStmt("v", new ValueExpression(new IntValue(32))),
                                                                new CompStmt(new PrintStmt(new VariableExpression("v")), new PrintStmt(new ReadHeapExpression(new VariableExpression("a"))))))
                                        ),
                                                new CompStmt(new PrintStmt(new VariableExpression("v")), new PrintStmt(new ReadHeapExpression(new VariableExpression("a"))))
                                        )
                                )
                        )
                )
        );

        /*
        Ref int a; new(a,20);
        (for(v=0;v<3;v=v+1) fork(print(v);v=v*rh(a)));
        print(rh(a))
        */
        Statement ex9 = assemble(
                new VarDeclStmt("a", new RefType(new IntType())),
                new NewStmt("a", new ValueExpression(new IntValue(20))),
                new ForStmt("v",new ValueExpression(new IntValue(0)),new ValueExpression(new IntValue(3)), new ArithmeticExpression('+',new VariableExpression("v"),new ValueExpression(new IntValue(1))),
                        new ForkStmt(assemble(new PrintStmt(new VariableExpression("v")), new AssignStmt("v", new ArithmeticExpression('*',new VariableExpression("v"), new ReadHeapExpression(new VariableExpression("a"))))))),
                new PrintStmt(new ReadHeapExpression(new VariableExpression("a")))
        );

        /*
        Ref int v1; Ref int v2; int x; int q;
        new(v1,20);new(v2,30);newLock(x);
        fork(
                fork(
                        lock(x);wh(v1,rh(v1)-1);unlock(x)
                );
                lock(x);wh(v1,rh(v1)*10);unlock(x)
        );
        newLock(q);
        fork(
                fork(lock(q);wh(v2,rh(v2)+5);unlock(q));
                lock(q);wh(v2,rh(v2)*10);unlock(q)
         );
        nop;nop;nop;nop;
        lock(x); print(rh(v1)); unlock(x);
        lock(q); print(rh(v2)); unlock(q);
         */

        Statement ex10 = assemble(
                new VarDeclStmt("v1", new RefType(new IntType())),
                new VarDeclStmt("v2", new RefType(new IntType())),
                new VarDeclStmt("x", new IntType()),
                new VarDeclStmt("q", new IntType()),
                new NewStmt("v1",new ValueExpression(new IntValue(20))),
                new NewStmt("v2",new ValueExpression(new IntValue(30))),
                new NewLockStmt("x"),
                new ForkStmt(assemble(new ForkStmt(assemble(
                                        new LockStmt("x"),
                                        new WriteHeapStmt("v1", new ArithmeticExpression('-', new ReadHeapExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(1)))),
                                        new UnlockStmt("x"))),
                        new LockStmt("x"),
                        new WriteHeapStmt("v1", new ArithmeticExpression('*', new ReadHeapExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(10)))),
                        new UnlockStmt("x"))),
                new NewLockStmt("q"),
                new ForkStmt(assemble(new ForkStmt(assemble(
                                            new LockStmt("q"),
                                            new WriteHeapStmt("v2", new ArithmeticExpression('+', new ReadHeapExpression(new VariableExpression("v2")), new ValueExpression(new IntValue(5)))),
                                            new UnlockStmt("q"))),
                        new LockStmt("q"),
                        new WriteHeapStmt("v2", new ArithmeticExpression('*', new ReadHeapExpression(new VariableExpression("v2")), new ValueExpression(new IntValue(10)))),
                        new UnlockStmt("q")
                        )),
                new NopStmt(), new NopStmt(), new NopStmt(), new NopStmt(),
                new LockStmt("x"), new PrintStmt(new ReadHeapExpression(new VariableExpression("v1"))), new UnlockStmt("x"),
                new LockStmt("q"), new PrintStmt(new ReadHeapExpression(new VariableExpression("v2"))), new UnlockStmt("q")
                );

        examples.add(ex1); examples.add(ex2); examples.add(ex3); examples.add(ex4); examples.add(ex5);
        examples.add(ex6); examples.add(ex7); examples.add(ex8); examples.add(ex9); examples.add(ex10);
        return examples;



    }

    public static Statement assemble(Statement... statements) {
        if (statements.length == 2) {
            return new CompStmt(statements[0], statements[1]);
        }
        if (statements.length == 1) {
            return statements[0];
        }
        return new CompStmt(statements[0], assemble(Arrays.copyOfRange(statements, 1, statements.length)));
    }
}

package Model;

import Exceptions.ExecutionStackEmptyException;
import Model.ADT.*;
import Model.statement.Statement;
import Model.value.Value;
import java.io.BufferedReader;

public class ProgramState {
    private IStack<Statement> executionStack;
    private IStack<IDictionary<String, Value>> symbolTableStack;
    private final IList<Value> output;
    private IDictionary<String, BufferedReader> fileTable;  // fileName --> Java file descriptor
    private IHeap<Value> heap;
    private IProcTable procTable;

    private final int ID;
    private static int lastID = 0;

    // Statement originalProgram; //optional field, but good to have

    public ProgramState(IStack<Statement> stack ,IStack<IDictionary<String,Value>> sytS, IList<Value> out,
                        IDictionary<String,BufferedReader> ft, IHeap<Value> h, IProcTable pt, Statement program) {
        executionStack = stack;
        symbolTableStack = sytS;
        output = out;
        fileTable = ft;
        heap = h;
        procTable = pt;
        ID = getNewID();

        if(symbolTableStack.isEmpty())
            symbolTableStack.push(new Dictionary<>());

        executionStack.push(program);
        // originalProgram = program.clone();
    }

    private static synchronized int getNewID() {
        lastID++;
        return lastID;
    }

    public ProgramState oneStep() throws Exception{
        if(executionStack.isEmpty())
            throw new ExecutionStackEmptyException();
        Statement currentStatement = executionStack.pop();
        return currentStatement.execute(this);
    }

    public IStack<Statement> getExecutionStack() {
        return executionStack;
    }
    public IDictionary<String, Value> getSymbolTable() {
        return symbolTableStack.top();
    }
    public IStack<IDictionary<String,Value>> getSymbolTableStack() { return symbolTableStack; }
    public IList<Value> getOutput() { return output; }
    public IDictionary<String, BufferedReader> getFileTable() {
        return fileTable;
    }
    public IHeap<Value> getHeap() { return heap; }
    public IProcTable getProcTable() { return procTable; }
    public int getID() { return ID; }


    public void setExecutionStack(IStack<Statement> newStack) { executionStack = newStack; }
    public void setSymbolTableStack(IStack<IDictionary<String, Value>> newSymbolTableStack) { symbolTableStack = newSymbolTableStack; }
    public void setFileTable(IDictionary<String, BufferedReader> newDict) { fileTable = newDict; }
    public void setHeap(IHeap<Value> newHeap) { heap = newHeap; }
    public void setProcTable(IProcTable newPT) { procTable = newPT; }

    public boolean isNotCompleted(){
        return !this.executionStack.isEmpty();
    }

    @Override
    public String toString() {
        return  "@ Program state ID: " + ID + "\n------------------\n" +
                "@ Execution stack: " + executionStack.toString() + "---------------\n" +
                "@ Symbol table: " + symbolTableStack.top().toString() + "----------------\n" +
                "@ Output stream: " + output.toString() + "-------------\n" +
                "@ File table: " + fileTable.toString() + "-------\n" +
                "@ Heap:" + heap.toString() + "----------------\n" +
                "@ ProcedureTable:" + procTable.toString() + "\n\n";
    }
}

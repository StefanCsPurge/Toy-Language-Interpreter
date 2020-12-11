package Model;

import Exceptions.ExecutionStackEmptyException;
import Model.ADT.*;
import Model.statement.Statement;
import Model.value.Value;
import java.io.BufferedReader;

public class ProgramState {
    private IStack<Statement> executionStack;
    private IDictionary<String, Value> symbolTable;
    private IList<Value> output;
    private IDictionary<String, BufferedReader> fileTable;  // fileName --> Java file descriptor
    private IHeap<Value> heap;
    private final int ID;
    private static int lastID = 0;

    // Statement originalProgram; //optional field, but good to have

    public ProgramState(IStack<Statement> stack ,IDictionary<String,Value> syt,IList<Value> out,
                        IDictionary<String,BufferedReader> ft, IHeap<Value> h, Statement program) {
        executionStack = stack;
        symbolTable = syt;
        output = out;
        fileTable = ft;
        heap = h;
        ID = getNewID();

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
        return symbolTable;
    }
    public IList<Value> getOutput() { return output; }
    public IDictionary<String, BufferedReader> getFileTable() {
        return fileTable;
    }
    public IHeap<Value> getHeap() { return heap; }
    public int getID() { return ID; }


    public void setExecutionStack(IStack<Statement> newStack) { executionStack = newStack; }
    public void setSymbolTable(IDictionary<String, Value> newDict) {
         symbolTable = newDict;
    }
    public void setFileTable(IDictionary<String, BufferedReader> newDict) { fileTable = newDict; }
    public void setOutput(IList<Value> newOut) {
        output = newOut;
    }
    public void setHeap(IHeap<Value> newHeap) { heap = newHeap; }

    public boolean isNotCompleted(){
        return !this.executionStack.isEmpty();
    }

    @Override
    public String toString() {
        return  "@ Program state ID: " + ID + "\n------------------\n" +
                "@ Execution stack: " + executionStack.toString() + "---------------\n" +
                "@ Symbol table: " + symbolTable.toString() + "----------------\n" +
                "@ Output stream: " + output.toString() + "-------------\n" +
                "@ File table: " + fileTable.toString() + "-------\n" +
                "@ Heap:" + heap.toString() + "\n\n";
    }
}

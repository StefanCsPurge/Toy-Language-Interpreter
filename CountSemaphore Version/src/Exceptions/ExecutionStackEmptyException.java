package Exceptions;

public class ExecutionStackEmptyException extends Exception{
    public ExecutionStackEmptyException(String msg) {
        super(msg);
    }
    public ExecutionStackEmptyException() {
        super("Error - the execution stack is empty!");
    }
}

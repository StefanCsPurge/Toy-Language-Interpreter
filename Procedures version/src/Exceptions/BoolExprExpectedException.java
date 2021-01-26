package Exceptions;

public class BoolExprExpectedException extends Exception{
    public BoolExprExpectedException(String msg){
        super(msg);
    }
    public BoolExprExpectedException() {
        super("Error - this is not a boolean expression!");
    }
}

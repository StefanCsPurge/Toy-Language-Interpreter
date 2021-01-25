package Exceptions;

public class StringExprExpectedException extends Exception{
    public StringExprExpectedException(String msg){
        super(msg);
    }
    public StringExprExpectedException() {
        super("Error - this is not a string expression!");
    }
}

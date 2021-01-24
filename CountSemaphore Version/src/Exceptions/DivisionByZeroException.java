package Exceptions;

public class DivisionByZeroException extends Exception{
    public DivisionByZeroException(String msg) {
        super(msg);
    }
    public DivisionByZeroException() {
        super("Error - division by zero!");
    }
}

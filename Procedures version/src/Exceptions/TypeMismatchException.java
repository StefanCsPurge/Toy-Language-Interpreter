package Exceptions;

public class TypeMismatchException extends Exception{
    public TypeMismatchException(String msg){
        super(msg);
    }
    public TypeMismatchException() {
        super("Error - the variable types do not match!");
    }
}

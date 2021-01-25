package Exceptions;

public class VariableAlreadyDeclaredException extends Exception{
    public VariableAlreadyDeclaredException(String msg){
        super(msg);
    }
    public VariableAlreadyDeclaredException() {
        super("Error - variable already declared!");
    }
}

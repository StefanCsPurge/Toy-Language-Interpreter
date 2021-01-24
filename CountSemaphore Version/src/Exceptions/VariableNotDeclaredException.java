package Exceptions;

public class VariableNotDeclaredException extends Exception{
    public VariableNotDeclaredException(String msg){
        super(msg);
    }
    public VariableNotDeclaredException() {
        super("Error - variable not declared!");
    }
}

package Exceptions;

public class FileNotInFileTableException extends Exception{
    public FileNotInFileTableException(String msg){
        super(msg);
    }
    public FileNotInFileTableException() {
        super("Error - this file does not exist in the file table!");
    }
}

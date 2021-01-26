package Exceptions;

public class FileAlreadyExistsException extends Exception{
    public FileAlreadyExistsException(String msg){
        super(msg);
    }
    public FileAlreadyExistsException() {
        super("Error - this file already exists in the file table!");
    }
}
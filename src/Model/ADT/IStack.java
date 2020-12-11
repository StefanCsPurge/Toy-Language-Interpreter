package Model.ADT;

import java.util.ArrayList;

public interface IStack<T> {

    public void push(T element);

    public T pop();

    public int size();

    public boolean isEmpty();

    public ArrayList<T> getStack();

}

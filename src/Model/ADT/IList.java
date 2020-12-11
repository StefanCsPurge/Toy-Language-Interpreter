package Model.ADT;

import java.util.ArrayList;
import java.util.Iterator;

public interface IList<T> {

    public void append(T element);

    public Iterator<T> getIterator();

    public ArrayList<T> getList();

    public String toString();
}

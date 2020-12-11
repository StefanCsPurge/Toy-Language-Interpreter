package Model.ADT;

import java.util.ArrayList;
import java.util.Iterator;

public class List<T> implements IList<T>{

    private final ArrayList<T> elements;

    public List() { elements = new ArrayList<T>(); }

    public T getFromIndex(int index) { return elements.get(index); }

    @Override
    public synchronized void append(T element){ elements.add(element); }

    @Override
    public Iterator<T> getIterator() { return elements.iterator(); }

    @Override
    public ArrayList<T> getList() { return elements; }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder("\n");
        for(T el : elements)
            output.append(el.toString()).append("\n");
        return output.toString();
    }

}

package Model.ADT;

import java.util.ArrayList;

public class Stack<T> implements IStack<T>{

    private final ArrayList<T> elements;

    public Stack() { elements = new ArrayList<>(); }

    @Override
    public void push(T element) { elements.add(element); }

    @Override
    public T pop() {
        int index = elements.size() - 1;
        T last = elements.get(index);
        elements.remove(index);
        return last;
    }

    @Override
    public T top() {
        return elements.get(elements.size()-1);
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public boolean isEmpty() {
        return elements.size() == 0;
    }

    @Override
    public ArrayList<T> getStack(){
        var stack = new ArrayList<T>();
        for(int j = elements.size()-1 ; j >= 0; j--)
            stack.add(elements.get(j));
        return stack;
    }

    @Override
    public IStack<T> cloneS() {
        IStack<T> copy = new Stack<>();
        for(T el : elements)
            copy.push(el);
        return copy;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder("\n");
        for(int j = elements.size()-1 ; j >= 0; j--)
            output.append(elements.get(j).toString()).append("\n");
        return output.toString();
    }
}

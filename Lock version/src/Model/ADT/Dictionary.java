package Model.ADT;

import Exceptions.VariableNotDeclaredException;

import java.util.HashMap;
import java.util.Map;

public class Dictionary<K,V> implements IDictionary<K,V>{
    private final HashMap<K,V> dict;

    public Dictionary() { this.dict = new HashMap<>(); }

    @Override
    public synchronized void set(K key, V value) {
        dict.put(key,value);
    }

    @Override
    public V get(K key) throws Exception{
        if(!dict.containsKey(key))
            throw new VariableNotDeclaredException(String.format("Error - variable '%s' is not defined!",key));
        return dict.get(key);
    }

    @Override
    public boolean containsKey(K key) {
        return dict.containsKey(key);
    }

    @Override
    public synchronized void remove(K key) throws Exception{
        if(!dict.containsKey(key))
            throw new VariableNotDeclaredException(String.format("Error - variable '%s' is not defined!",key));
        dict.remove(key);
    }

    @Override
    public String toString() {
        StringBuilder s= new StringBuilder("\n");
        for (K key : dict.keySet()) {
            s.append(key.toString());
            String valueStr = dict.get(key).toString();
            if(!valueStr.contains("@"))
                s.append(" --> ").append(valueStr);
            s.append("\n");
        }
        //s = new StringBuilder(s.substring(0, s.length() - 1));
        return s.toString();
    }

    @Override
    public Map<K,V> getContent(){
        return this.dict;
    }

    @Override
    public IDictionary<K,V> cloneD(){
        IDictionary<K,V> copy = new Dictionary<>();
        for(K key : dict.keySet())
            copy.set(key, dict.get(key));
        return copy;
    }
}

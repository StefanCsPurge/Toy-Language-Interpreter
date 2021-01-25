package Model.ADT;

import java.util.Map;

public interface IDictionary<K,V> {

    void set(K key, V value);

    V get(K key) throws Exception;

    void remove(K key) throws Exception;

    boolean containsKey(K key);

    Map<K,V> getContent();

    IDictionary<K,V> cloneD();
}

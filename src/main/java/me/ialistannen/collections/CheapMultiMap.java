package me.ialistannen.collections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A cheap MultiMap
 */
public class CheapMultiMap<K, V> extends HashMap<K, List<V>> {

  public void putSingle(K key, V value) {
    addCollectionIfNeeded(key).add(value);
  }

  public void removeSingle(K key, V value) {
    if (!containsKey(key)) {
      return;
    }
    List<V> list = get(key);
    list.remove(value);

    if (list.isEmpty()) {
      remove(key);
    }
  }

  private List<V> addCollectionIfNeeded(K key) {
    if (!containsKey(key)) {
      put(key, new ArrayList<>());
    }
    return get(key);
  }
}

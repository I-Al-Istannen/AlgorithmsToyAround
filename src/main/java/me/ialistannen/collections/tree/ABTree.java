package me.ialistannen.collections.tree;

public class ABTree<K extends Comparable<K>, V> {

  private static class TreeNode<K extends Comparable<K>, V> {

    private K key;
    private V value;
  }
}

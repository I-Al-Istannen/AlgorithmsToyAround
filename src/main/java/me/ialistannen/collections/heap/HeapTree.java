package me.ialistannen.collections.heap;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import me.ialistannen.algorithms.math.parser.token.Token;
import me.ialistannen.algorithms.math.parser.token.TokenType;
import me.ialistannen.treeviewer.model.Tree;

/**
 * A {@link Tree} for heaps.
 */
public class HeapTree implements Tree {

  private HeapTree[] elements;
  private int index;
  private int childCount;
  private Object value;

  private HeapTree(HeapTree[] elements, int index, int childCount, Object value) {
    this.elements = elements;
    this.index = index;
    this.childCount = childCount;
    this.value = value;
  }

  @Override
  public Tree getParent() {
    return index == 0 ? null : elements[index / childCount];
  }

  @Override
  public List<? extends Tree> getChildren() {
    List<HeapTree> children = new ArrayList<>();

    int firstChildIndex = childCount * index + 1;
    for (int i = 0; i < childCount; i++) {
      int childIndex = firstChildIndex + i;
      if (childIndex < elements.length && elements[childIndex] != null) {
        children.add(elements[childIndex]);
      } else {
        break;
      }
    }

    return children;
  }

  @Override
  public Token getToken() {
    return new Token() {
      @Override
      public String getTokenText() {
        return Objects.toString(value);
      }

      @Override
      public TokenType getType() {
        return TokenType.NUMBER;
      }

      @Override
      public double evaluate(Tree node) {
        return 0;
      }
    };
  }

  @Override
  public double evaluate() {
    return 0;
  }

  /**
   * Creates a heap tree from a heap array.
   *
   * @param array the array
   * @param childCount the amount of children per node
   * @param <T> the type of the array
   * @return the created tree
   */
  public static <T> HeapTree fromHeapArray(T[] array, int childCount) {
    HeapTree[] heapTreeElement = new HeapTree[array.length];

    for (int i = 0; i < heapTreeElement.length; i++) {
      if (array[i] != null) {
        heapTreeElement[i] = new HeapTree(heapTreeElement, i, childCount, array[i]);
      }
    }
    return heapTreeElement[0];
  }
}

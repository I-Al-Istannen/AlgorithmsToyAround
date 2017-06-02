package me.ialistannen.treeviewer;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Some static utility functions
 */
public class Util {

  /**
   * Casts a whole Map to whatever you assign it to.
   *
   * <p><strong><em>Always make sure R <u>is</u> a subtype of T</em></strong>
   *
   * @param inputMap The input {@link Map}
   * @param <K> The <em>old</em> type of the Map key
   * @param <L> The <em>new</em> type of the Map key
   * @param <V> The <em>old</em> type of the current Map value
   * @param <W> The <em>new</em> type of the new Map value
   * @return The cast {@link Map} of type {@code <L, W>}
   * @throws ClassCastException if you made a mistake.
   * @author jwachter
   */
  public static <K, L, V, W> Map<L, W> unsafeCastMap(Map<K, V> inputMap) {
    return inputMap.entrySet().stream()
        .collect(Collectors.toMap(
            entry -> unsafeCast(entry.getKey()),
            entry -> unsafeCast(entry.getValue())
        ));
  }

  /**
   * Casts an object to the given type.
   *
   * @param o The object to cast
   * @param <T> The type to cast it to
   * @return The cast object
   * @throws ClassCastException if you made a mistake.
   * @author jwachter
   */
  private static <T> T unsafeCast(Object o) {
    @SuppressWarnings("unchecked")
    T cast = (T) o;
    return cast;
  }
}

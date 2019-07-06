package me.ialistannen.algorithms.layout.forcedbased.util;

/**
 * A function that can produce an exception.
 *
 * @param <T> the type of the input
 * @param <R> the type of the output
 */
public interface ExceptionalFunction<T, R> {

  /**
   * Applies this function.
   *
   * @param input the input value
   * @return the result
   * @throws Exception if something went wrong
   */
  R apply(T input) throws Exception;
}

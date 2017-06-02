package me.ialistannen.algorithms.math.parser.variables;

import java.util.Objects;

/**
 * A simple {@link Variable}
 */
public class BasicVariable implements Variable {

  private String keyword;
  private double value;

  /**
   * @param keyword The keyword
   * @param value The value
   */
  public BasicVariable(String keyword, double value) {
    this.keyword = keyword;
    this.value = value;
  }

  @Override
  public String getKeyword() {
    return keyword;
  }

  @Override
  public double getValue() {
    return value;
  }

  @Override
  public void setValue(double value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BasicVariable that = (BasicVariable) o;
    return Objects.equals(getKeyword(), that.getKeyword());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getKeyword());
  }

  @Override
  public String toString() {
    return "BasicVariable{"
        + "keyword='" + keyword + '\''
        + ", value=" + value
        + '}';
  }
}

package me.ialistannen.algorithms.math.matrix;

public class Matrix {

  private double[][] values;

  /**
   * Creates a zero matrix with the given columns and rows. If one value is zero, the whole matrix
   * will become a 0x0 one.
   *
   * @param columns the amount of columns
   * @param rows the amount of rows
   */
  private Matrix(int columns, int rows) {
    if (columns == 0 || rows == 0) {
      this.values = new double[0][0];
    } else {
      this.values = new double[columns][rows];
    }
  }

  /**
   * Returns the amount of columns.
   *
   * @return the amount of columns
   */
  public int getColumns() {
    return values.length;
  }

  /**
   * Returns the amount of rows.
   *
   * @return the amount of rows
   */
  public int getRows() {
    return getColumns() == 0 ? 0 : values[0].length;
  }

  /**
   * Returns the value in the matrix at the given position.
   *
   * @param column the column
   * @param row the row
   * @return the value in the matrix at this position
   */
  public double get(int column, int row) {
    return values[column][row];
  }

  /**
   * Sets the value in the matrix at the given position.
   *
   * @param column the column
   * @param row the row
   * @param value the value to write
   * @return this matrix
   */
  public Matrix set(int column, int row, double value) {
    values[column][row] = value;

    return this;
  }

  /**
   * Adds the given matrix to this one.
   *
   * @param other the other matrix
   * @return this matrix
   * @throws IllegalArgumentException if the dimensions do not match
   */
  public Matrix add(Matrix other) {
    if (other.getColumns() != this.getColumns() || other.getRows() != this.getRows()) {
      throw new IllegalArgumentException("Dimensions do not match");
    }

    for (int x = 0; x < getColumns(); x++) {
      for (int y = 0; y < getRows(); y++) {
        set(x, y, other.get(x, y) + get(x, y));
      }
    }

    return this;
  }

  /**
   * Multiplies this matrix with the given one.
   *
   * @param other the other matrix
   * @return this matrix
   * @throws IllegalArgumentException if the dimensions do not match
   */
  public Matrix multiply(Matrix other) {
    if (other.getRows() != this.getColumns() || other.getColumns() != this.getRows()) {
      throw new IllegalArgumentException("Dimensions do not match");
    }

    for (int x = 0; x < getColumns(); x++) {
      for (int y = 0; y < getRows(); y++) {
        double accumulated = 0;
        for (int delta = 0; delta < getColumns(); delta++) {
          accumulated += get(delta, y) * other.get(x, delta);
        }
        set(x, y, accumulated);
      }
    }

    return this;
  }

  /**
   * Multiplies this matrix with the given scalar.
   *
   * @param scalar the scalar
   * @return this matrix
   */
  public Matrix multiply(double scalar) {
    for (int x = 0; x < getColumns(); x++) {
      for (int y = 0; y < getRows(); y++) {
        set(x, y, get(x, y) * scalar);
      }
    }

    return this;
  }

  /**
   * Adds the given row (a matrix with a height of one) to this matrix.
   *
   * @param rowToAdd the row to add
   * @param rowIndex the row to add it to. Zero indexed.
   * @return this matrix
   */
  public Matrix addRow(Matrix rowToAdd, int rowIndex) {
    if (rowToAdd.getColumns() != getColumns()) {
      throw new IllegalArgumentException("Dimension mismatch");
    }

    for (int i = 0; i < rowToAdd.getColumns(); i++) {
      set(i, rowIndex, get(i, rowIndex) + rowToAdd.get(i, 0));
    }

    return this;
  }

  /**
   * Adds the given column (a matrix with a width of one) to this matrix.
   *
   * @param columnToAdd the column to add
   * @param columnIndex the column to add it to. Zero indexed.
   * @return this matrix
   */
  public Matrix addColumn(Matrix columnToAdd, int columnIndex) {
    if (columnToAdd.getRows() != getRows()) {
      throw new IllegalArgumentException("Dimension mismatch");
    }

    for (int i = 0; i < columnToAdd.getRows(); i++) {
      set(columnIndex, i, get(columnIndex, i) + columnToAdd.get(0, i));
    }

    return this;
  }

  /**
   * Returns a copy of a part of this matrix.
   *
   * @param topLeftColumn the column of the top left coordinate. Zero indexed, inclusive
   * @param topLeftRow the row of the top left coordinate. Zero indexed, inclusive
   * @param bottomRightColumn the column of the bottom right coordinate. Zero indexed,
   *     inclusive
   * @param bottomRightRow the column of the bottom right coordinate. Zero indexed, inclusive
   * @return the sub matrix
   */
  public Matrix getSubMatrix(int topLeftColumn, int topLeftRow, int bottomRightColumn,
      int bottomRightRow) {
    if (topLeftColumn < 0 || topLeftRow < 0) {
      throw new IllegalArgumentException("Dimension mismatch");
    }
    if (bottomRightColumn >= getColumns() || bottomRightRow >= getRows()) {
      throw new IllegalArgumentException("Dimension mismatch");
    }

    Matrix newMatrix = zero(1 + bottomRightColumn - topLeftColumn, 1 + bottomRightRow - topLeftRow);

    for (int x = 0; x < newMatrix.getColumns(); x++) {
      for (int y = 0; y < newMatrix.getRows(); y++) {
        newMatrix.set(x, y, get(topLeftColumn + x, topLeftRow + y));
      }
    }

    return newMatrix;
  }

  /**
   * Returns a single row of the matrix.
   *
   * @param row the row index. Zero based.
   * @return the row
   */
  public Matrix getRow(int row) {
    return getSubMatrix(0, row, getColumns() - 1, row);
  }

  /**
   * Returns a single column of the matrix.
   *
   * @param column the column index. Zero based.
   * @return the column
   */
  public Matrix getColumn(int column) {
    return getSubMatrix(column, 0, column, getRows() - 1);
  }

  /**
   * Sets the row of the matrix.
   *
   * @param rowIndex the row index
   * @param row the new row
   * @return this matrix
   * @throws IllegalArgumentException if the dimensions do not match
   */
  public Matrix setRow(int rowIndex, Matrix row) {
    if (row.getColumns() != getColumns()) {
      throw new IllegalArgumentException("Dimension mismatch");
    }

    for (int x = 0; x < getColumns(); x++) {
      set(x, rowIndex, row.get(x, 0));
    }

    return this;
  }

  /**
   * Swaps two rows in the matrix.
   *
   * @param first the first row index. Zero indexed
   * @param second the second row index. Zero indexed
   */
  public Matrix swapRow(int first, int second) {
    Matrix firstRow = getRow(first);
    setRow(first, getRow(second));
    setRow(second, firstRow);

    return this;
  }

  /**
   * Applies <a href="https://en.wikipedia.org/wiki/Gaussian_elimination">gaussian elimination</a>
   * to the full matrix, modifying it in the process.
   *
   * <p><br>This is identical to call to {@link #gaussianElimination(int, int)} with {@code 0} and
   * {@link #getColumns()} as arguments.</p>
   *
   * @return this matrix
   * @see #gaussianElimination(int, int)
   */
  public Matrix gaussianElimination() {
    return gaussianElimination(0, getColumns());
  }

  /**
   * Applies <a href="https://en.wikipedia.org/wiki/Gaussian_elimination">gaussian elimination</a>
   * to the matrix, modifying it in the process.
   *
   * @param fromColumn the first column to gauss from. If this is set to {@code 0} and maxColumn
   *     to {@link #getColumns()}, the full matrix will be in gaussian normal form at the end.
   *     Setting it to a smaller value might be nice when dealing with an extended matrix
   * @param maxColumn the maximal column to gauss until. See {@code fromColumn} for more
   *     details.
   * @return this matrix
   */
  public Matrix gaussianElimination(int fromColumn, int maxColumn) {
    for (int currentDiagonal = fromColumn; currentDiagonal < maxColumn; currentDiagonal++) {
      int rowWithOne = findRowWithOne(currentDiagonal);

      if (rowWithOne >= 0 && rowWithOne != currentDiagonal) {
        swapRow(currentDiagonal, rowWithOne);
      } else {
        int newFirstRow = findRowWithNonZeroElement(currentDiagonal);
        // only zeros
        if (newFirstRow < 0) {
          continue;
        }
        swapRow(currentDiagonal, newFirstRow);
        if (get(currentDiagonal, currentDiagonal) != 1) {
          Matrix row = getRow(currentDiagonal).multiply(1 / get(currentDiagonal, currentDiagonal));
          setRow(currentDiagonal, row);
        }
      }
      eliminateOnesInColumn(currentDiagonal, currentDiagonal);
    }
    return this;
  }

  private int findRowWithOne(int column) {
    for (int row = column; row < getRows(); row++) {
      if (get(column, row) == 1) {
        return row;
      }
    }
    return -1;
  }

  private int findRowWithNonZeroElement(int column) {
    for (int row = column; row < getRows(); row++) {
      if (get(column, row) != 0) {
        return row;
      }
    }
    return -1;
  }

  private void eliminateOnesInColumn(int column, int pivotRowIndex) {
    double pivotElement = get(column, pivotRowIndex);
    if (pivotElement != 1) {
      // Normalize pivot row to 1
      setRow(pivotRowIndex, getRow(pivotRowIndex).multiply(1 / pivotElement));
    }

    // Eliminate the other rows in that column
    for (int rowIndex = 0; rowIndex < getRows(); rowIndex++) {
      if (rowIndex == pivotRowIndex) {
        continue;
      }
      double firstElementInRow = get(column, rowIndex);
      // Already zero
      if (firstElementInRow == 0) {
        continue;
      }
      // multiply the pivot row by the inverse of the element we want to eliminate
      Matrix pivotRow = getRow(pivotRowIndex).multiply(-firstElementInRow);
      setRow(rowIndex, getRow(rowIndex).add(pivotRow));
    }
  }

  /**
   * Inverts the matrix but returns a <strong>copy</strong>.
   *
   * @return the inverted matrix
   */
  public Matrix calculateInverse() {
    Matrix extendedMatrix = combinedHorizontally(this, identity(getColumns(), getRows()));
    extendedMatrix.gaussianElimination(0, getColumns());

    return extendedMatrix.getSubMatrix(
        getColumns(), 0,
        extendedMatrix.getColumns() - 1, extendedMatrix.getRows() - 1
    );
  }

  /**
   * Creates a zero matrix with the given columns and rows.
   *
   * @param columns the amount of columns
   * @param rows the amount of rows
   * @return a zero matrix with the given dimensions
   */
  public static Matrix zero(int columns, int rows) {
    return new Matrix(columns, rows);
  }

  /**
   * Creates an identity matrix with the given columns and rows.
   *
   * @param columns the amount of columns
   * @param rows the amount of rows
   * @return an identity matrix with the given dimensions
   */
  public static Matrix identity(int columns, int rows) {
    Matrix zero = zero(columns, rows);
    for (int n = 0; n < Math.min(columns, rows); n++) {
      zero.set(n, n, 1);
    }

    return zero;
  }

  /**
   * Creates a new matrix by appending the second matrix to the first.
   *
   * @param first the first matrix
   * @param second the second matrix
   * @return the extended matrix
   */
  public static Matrix combinedHorizontally(Matrix first, Matrix second) {
    if (first.getRows() != second.getRows()) {
      throw new IllegalArgumentException("Dimension mismatch");
    }

    Matrix result = zero(first.getColumns() + second.getColumns(), first.getRows());

    for (int x = 0; x < result.getColumns(); x++) {
      for (int y = 0; y < result.getRows(); y++) {
        if (x < first.getColumns()) {
          result.set(x, y, first.get(x, y));
        } else {
          result.set(x, y, second.get(x - first.getColumns(), y));
        }
      }
    }

    return result;
  }

  /**
   * Creates an identity matrix with the given columns and rows.
   *
   * @param values the values, encoded as [row][column], i.e. {@code { {column 1}, {column 2}}}
   * @return the created matrix
   */
  public static Matrix fromValues(double[][] values) {
    if (values.length == 0 || values[0].length == 0) {
      return zero(0, 0);
    }

    Matrix matrix = zero(values[0].length, values.length);

    for (int row = 0; row < values.length; row++) {
      for (int col = 0; col < values[row].length; col++) {
        matrix.set(col, row, values[row][col]);
      }
    }

    return matrix;
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();

    for (int y = 0; y < getRows(); y++) {
      for (int x = 0; x < getColumns(); x++) {
        result.append(String.format("%7.2f ", get(x, y)));
      }
      result.append("\n");
    }

    return result.toString();
  }

  public static void main(String[] args) {
//    Matrix matrix = fromValues(new double[][]{
//        {0, -3, -6, 6, 4, -5},
//        {3, -7, 8, -5, 8, 9},
//        {3, -9, 12, -9, 6, 15}
//    });
//    System.out.println(matrix);
//
//    System.out.println(matrix.gaussianElimination());

    System.out.println();
    System.out.println(fromValues(new double[][]{
        {1, 0, 3},
        {0, 4, 0},
        {5, 0, 0}
    }).calculateInverse());
  }
}

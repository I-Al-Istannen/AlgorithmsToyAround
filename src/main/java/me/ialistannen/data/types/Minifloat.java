package me.ialistannen.data.types;

//@formatter:off
/**
 * A 16-bit minifloat.
 *
 * <h1>Bit layout</h1>
 * <ul>
 *   <li>1 bit exponent</li>
 *   <li>5 bit exponent</li>
 *   <li>10 bit mantissa (11 with normalization)</li>
 * </ul>
 */
//@formatter:on
public class Minifloat {

  private static final int MANTISSA_LENGTH = 10;
  private static final int EXPONENT_LENGTH = 5;
  private static final int EXPONENT_MAX = (int) (Math.pow(2, EXPONENT_LENGTH) - 1);
  private static final int EXPONENT_BIAS = (int) (Math.pow(2, EXPONENT_LENGTH - 1) - 1);
  private static final int EXPONENT_MIN = 1 - EXPONENT_BIAS;
  private static final float MIN_NORMAL = (float) Math.pow(2, EXPONENT_MIN);

  private short underlying;

  /**
   * Creates a Mini-Float using the given int as underlying bit pattern. It will be cast to a short,
   * i.e. truncated.
   *
   * @param asInt the bit pattern as an int
   */
  public Minifloat(int asInt) {
    underlying = (short) asInt;
  }

  /**
   * Creates a Mini-Float using the given bit pattern. Spaces are silently ignored and can be used
   * to make the input more readable.
   *
   * @param bitPattern the bit patten as a String
   */
  public Minifloat(String bitPattern) {
    this(Integer.parseInt(bitPattern.replaceAll("\\s", ""), 2));
  }

  /**
   * Creates a Mini-Float using the given native floating point number. Rounding errors are to be
   * expected.
   *
   * @param nativeFloat the native float
   */
  public Minifloat(float nativeFloat) {
    underlying = (short) (nativeFloat < 0 ? 1 : 0);

    // log2 (nativeFloat) is the exponent
    int exponent = (int) (Math.log(nativeFloat) / Math.log(2));
    // As the exponent is stored in excess notation we need to add the bias
    // It is later subtracted when converting back
    int storedExponent = exponent + EXPONENT_BIAS;

    boolean deNormalized = Math.abs(nativeFloat) < MIN_NORMAL;

    if (deNormalized) {
      storedExponent = 0;
      exponent = EXPONENT_MIN;
    }

    // apply exponent by shifting left and OR-ing with the binary representation of the characteristic
    // this works as the characteristic is always positive
    underlying = (short) (underlying << EXPONENT_LENGTH | storedExponent);

    // The mantissa is the input value normalized to a single pre-decimal point digit
    float mantissaFloat = (float) (nativeFloat / Math.pow(2, exponent));

    // normalized numbers have an implicit 1 bit before the decimal point
    if (!deNormalized) {
      mantissaFloat -= 1;
    }

    int mantissaBit = 0;

    // Horner-schema: Multiply by two (base) and use the integer value as the bit
    // Using the mantissa length as we can't achieve any better accuracy than that
    for (int i = 0; i < MANTISSA_LENGTH; i++) {
      mantissaFloat *= 2;
      // Shifting left adds a 0 as the bit
      mantissaBit <<= 1;

      if (mantissaFloat >= 1) {
        // Horner schema: Normalizing number back to < 1 and recording the one bit
        mantissaFloat -= 1;
        // we already shifted so this just adjust the *current* bit position to a one
        mantissaBit |= 1;
      }
    }

    underlying = (short) (underlying << MANTISSA_LENGTH | mantissaBit);
  }

  /**
   * Converts this 16-bit mini-float to a java float value.
   *
   * @return the java float value
   */
  public float toNativeFloat() {
    boolean negative = isBitSet(15);

    int exponent = readExponent();

    int adjustedExponent = exponent - EXPONENT_BIAS;

    float mantissa = readMantissa();

    if (isDeNormalized()) {
      // de-normalized numbers
      adjustedExponent = EXPONENT_MIN;
    } else if (exponent == EXPONENT_MAX) {
      if (mantissa != 0) {
        return Float.NaN;
      }
      return negative ? Float.NEGATIVE_INFINITY : Float.POSITIVE_INFINITY;
    } else {
      // normalized numbers
      mantissa += 1;
    }

    return (float) ((negative ? -1 : 1) * Math.pow(2, adjustedExponent) * mantissa);
  }

  private boolean isBitSet(int indexFromLsb) {
    return ((underlying >> indexFromLsb) & 1) == 1;
  }

  private int readExponent() {
    int start = MANTISSA_LENGTH;
    int end = start + EXPONENT_LENGTH - 1;
    int result = 0;
    for (int i = start; i <= end; i++) {
      result += (isBitSet(i) ? 1 << (i - start) : 0);
    }

    return result;
  }

  private float readMantissa() {
    float result = 0;
    for (int i = 1; i <= MANTISSA_LENGTH; i++) {
      result += isBitSet(MANTISSA_LENGTH - i) ? Math.pow(2, -i) : 0;
    }

    return result;
  }

  /**
   * Returns true if this number is in de-normalized encoding.
   *
   * @return true if this number is in de-normalized encoding
   */
  public boolean isDeNormalized() {
    return readExponent() - EXPONENT_BIAS == 0;
  }

  /**
   * Returns the raw bits.
   *
   * @return the raw bits
   */
  public short getRawBits() {
    return underlying;
  }

  /**
   * Returns the raw bits.
   *
   * @return the raw bits
   */
  public String getRawBitsString() {
    StringBuilder result = new StringBuilder();

    result
        .append(getBitValue(MANTISSA_LENGTH + EXPONENT_LENGTH)) // sign bit
        .append(" ");

    for (int i = EXPONENT_LENGTH - 1; i >= 0; i--) {
      result.append(getBitValue(MANTISSA_LENGTH + i));
    }

    result.append(" ");

    for (int i = MANTISSA_LENGTH - 1; i >= 0; i--) {
      result.append(getBitValue(i));
    }

    return result.toString();
  }

  private int getBitValue(int index) {
    return isBitSet(index) ? 1 : 0;
  }

  @Override
  public String toString() {
    return "Minifloat{" +
        "bit pattern=" + getRawBitsString() +
        ", interpreted=" + toNativeFloat() +
        ", exponentRaw=" + readExponent() +
        ", exponentAdjusted=" + (readExponent() - EXPONENT_BIAS) +
        ", rawMantissa=" + readMantissa() +
        ", adjustedMantissa=" + (isDeNormalized() ? readMantissa() : readMantissa() + 1) +
        ", de-normalized=" + isDeNormalized() +
        '}';
  }

  public static void main(String[] args) {
    System.out.println(new Minifloat(0b0_11110_0000001001).toNativeFloat());
    System.out.println(new Minifloat(0b0_11110_0000001001));
    System.out.println(new Minifloat("1 01111 0000000000").toNativeFloat());
    System.out.println(new Minifloat(33056f).toNativeFloat());
    System.out.println(new Minifloat(0b0_00000_0000000001).toNativeFloat());
    System.out.println(new Minifloat(5.9604645e-8f).toNativeFloat());
  }
}

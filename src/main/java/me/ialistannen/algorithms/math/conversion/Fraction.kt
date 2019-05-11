package me.ialistannen.algorithms.math.conversion

import kotlin.math.abs
import kotlin.math.sign

/**
 * A mathematical fraction that can hold two 64 bit integers.
 */
data class Fraction(val numerator: Long, val denom: Long) : Comparable<Fraction> {

    init {
        if (denom == 0L) throw ArithmeticException("Division by zero in fraction")
    }

    /**
     * Adds the fraction to this and reduces the result.
     */
    operator fun plus(other: Fraction): Fraction {
        return Fraction(
                numerator * other.denom + other.numerator * denom,
                denom * other.denom
        ).reduce()
    }

    /**
     * Negates this fraction.
     */
    operator fun unaryMinus(): Fraction {
        return Fraction(-numerator, denom)
    }

    /**
     * Subtracts the given fraction from this fraction.
     */
    operator fun minus(other: Fraction): Fraction {
        return this + (-other)
    }

    /**
     * Multiplies the fraction wit a
     */
    operator fun times(scalar: Int): Fraction {
        return Fraction(numerator * scalar, denom).reduce()
    }

    /**
     * Multiplies two fractions.
     */
    operator fun times(fraction: Fraction): Fraction {
        return Fraction(numerator * fraction.numerator, denom * fraction.denom).reduce()
    }

    /**
     * Dives the fraction by a scalar.
     */
    operator fun div(scalar: Int): Fraction {
        return Fraction(numerator * scalar, denom).reduce()
    }

    /**
     * Dives the fraction by another fraction.
     */
    operator fun div(fraction: Fraction): Fraction {
        return this * fraction.reciprocal()
    }

    /**
     * Returns the reciprocal fraction.
     *
     * @throws ArithmeticException if you try to invert `0/x`.
     */
    fun reciprocal(): Fraction {
        return Fraction(denom, numerator).reduce()
    }

    /**
     * Reduces this fraction.
     *
     * @return the reduced fraction
     */
    fun reduce(): Fraction {
        val divisor = gcd(numerator, denom)
        return Fraction(abs(numerator / divisor) * sign(), abs(denom / divisor))
    }

    /**
     * Returns the integer value of this fraction.
     */
    fun intValue(): Int {
        return (numerator / denom.toDouble()).toInt()
    }

    private fun gcd(a: Long, b: Long): Long {
        if (b == 0L) return a

        return gcd(b, a % b)
    }

    override operator fun compareTo(other: Fraction): Int {
        if ((this - other).sign() == -1) {
            return -1
        }
        if (this == other) return 0

        return 1
    }

    private fun sign(): Int {
        return (numerator * denom).sign
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other.javaClass != Fraction::class.java)
            return false
        val thisReduced = this.reduce()
        val otherReduced = (other as Fraction).reduce()
        return thisReduced.numerator == otherReduced.numerator && thisReduced.denom == otherReduced.denom
    }

    override fun hashCode(): Int {
        var result = numerator.hashCode()
        result = 31 * result + denom.hashCode()
        return result
    }

    override fun toString(): String {
        return "$numerator/$denom"
    }
}

/**
 * Returns the fraction `this/denominator`.
 *
 * @param denominator the denominator
 * @return the fraction
 */
infix fun Long.over(denominator: Long): Fraction {
    return Fraction(this, denominator)
}

/**
 * Returns the fraction `this/denominator`.
 *
 * @param denominator the denominator
 * @return the fraction
 */
infix fun Int.over(denominator: Int): Fraction {
    return Fraction(this.toLong(), denominator.toLong())
}
package me.ialistannen.algorithms.math.conversion

import java.math.BigInteger

/**
 * A result that collects the steps.
 */
data class StepByStepResult<T>(val result: T, val steps: List<String>) {

    fun map(valMapper: (T) -> T, newSteps: () -> List<String>): StepByStepResult<T> {
        return StepByStepResult(
                valMapper(result), steps.plus(newSteps())
        )
    }

    fun appendSteps(steps: List<String>): StepByStepResult<T> {
        return StepByStepResult(result, this.steps + steps)
    }

    fun prependSteps(steps: List<String>): StepByStepResult<T> {
        return StepByStepResult(result, steps + this.steps)
    }

    fun mapAlignSteps(marker: String): StepByStepResult<T> {
        return StepByStepResult(
                result,
                alignSteps(marker).split("\n")
        )
    }

    /**
     * Aligns the steps on the given marker.
     */
    fun alignSteps(marker: String): String {
        return steps.joinToString("\n")
                .alignOn(marker)
    }

    /**
     * Joins the steps with a newline.
     */
    fun joinSteps(): String {
        return steps.joinToString("\n") + "\n\nYielding result: $result"
    }
}

fun Char.decimalValue(): Int {
    return if (isDigit()) this - '0' else toUpperCase() - 'A' + 10
}

fun Int.toBaseChar(): Char {
    return if (this >= 10) 'A' + (this - 10) else '0' + this
}


fun BigInteger.multiply(i: Int): BigInteger {
    return this.multiply(BigInteger.valueOf(i.toLong()))
}

fun String.groupByFour(): String {
    if (length <= 4) return this
    return "${take(4)}${if (length > 4) " " else ""}${drop(4).groupByFour()}"
}

/**
 * Converts a number to decimal.
 */
fun toDecimal(input: String, inputBase: Int): StepByStepResult<BigInteger> {
    return input.foldIndexed(StepByStepResult(BigInteger.ZERO, emptyList())) { index, acc, c ->
        val base = BigInteger.valueOf(inputBase.toLong()).pow(input.length - index - 1)
        val digitValue: Int = c.decimalValue()

        acc.map({ res ->
            res + base.multiply(digitValue)
        }, { listOf("+ $digitValue * $base to ${acc.result}") })
    }.mapAlignSteps("*")
}

/**
 * Converts a decimal number to an arbitrary base.
 */
fun decimalToBase(input: BigInteger, outputBase: Int): StepByStepResult<String> {
    var result = StepByStepResult("", emptyList())
    var tempNumber = input

    while (tempNumber > BigInteger.ZERO) {
        val modResult = tempNumber.mod(outputBase.toBigInteger()).toInt()
        val before = tempNumber
        tempNumber /= outputBase.toBigInteger()

        val resultDigit = modResult.toBaseChar()

        result = result.map({ res ->
            resultDigit + res
        }, { listOf("$before / $outputBase = $tempNumber R $resultDigit") })
    }

    return result.mapAlignSteps("/").mapAlignSteps("R ")
}

/**
 * Converts a decimal fraction to a given base.
 */
fun decimalFractionToBase(decimal: Fraction, targetBase: Int, maxSteps: Int = 20): StepByStepResult<String> {
    val steps = mutableListOf<String>()
    var currentValue = decimal
    var result = ""

    for (i in 0 until maxSteps) {
        var newValue = currentValue * targetBase
        val baseDigit = newValue.intValue()
        result += baseDigit.toBaseChar()

        steps.add("$currentValue * $targetBase = $newValue (R$baseDigit)")

        if (newValue == 1 over 1) break

        if (newValue.intValue() >= 1) {
            newValue -= (baseDigit over 1)
        }

        currentValue = newValue
    }

    return StepByStepResult(result.dropLastWhile { it == '0' }, steps)
            .mapAlignSteps("*")
            .mapAlignSteps("(R")
}

/**
 * Coverts a fraction in a given base to a decimal one.
 */
fun fractionToDecimal(fractionPart: String, sourceBase: Int): StepByStepResult<Fraction> {
    val steps = mutableListOf<String>()
    var result = Fraction(0, 1)

    for ((index, char) in fractionPart.withIndex()) {
        steps.add("+ ${char.decimalValue()} * ($sourceBase^-${index + 1})")
        result += char.decimalValue() over Math.pow(sourceBase.toDouble(), (index + 1).toDouble()).toInt()
    }

    return StepByStepResult(result, steps).mapAlignSteps("*")
}

fun changeBase(input: String, inputBase: Int, outputBase: Int, maxSteps: Int = 24): StepByStepResult<String> {
    if ("," !in input) {
        return changeBaseInteger(inputBase, input, outputBase)
    }
    val parts = input.split(",")

    val preComma = parts[0]
    val postComma = parts[1]

    if (inputBase == 10) {
        return changeBaseFromDecimalFraction(postComma, outputBase, maxSteps, preComma, input)
    }

    val (decPost, decPostSteps) = fractionToDecimal(postComma, inputBase)
            .prependSteps(listOf("Converting .$postComma fraction from base $inputBase to decimal"))

    val (fracResult, fracResultSteps) = decimalFractionToBase(decPost, outputBase, maxSteps)
            .prependSteps(listOf("Converting $decPost fraction from decimal to base $outputBase"))

    val (preCommaAsDecimalRes, preCommaAsDecimalSteps) = toDecimal(preComma, inputBase)
            .prependSteps(listOf("Converting the integer $preComma from base $inputBase to decimal"))

    return decimalToBase(preCommaAsDecimalRes, outputBase)
            .prependSteps(listOf("Converting the integer $preComma from base decimal to base $outputBase:"))
            .prependSteps(preCommaAsDecimalSteps)
            .map({
                "${it.groupByFour()} , ${fracResult.groupByFour()}"
            }, { decPostSteps + "\n" + fracResultSteps })
            .prependSteps(listOf("Converting $input from base $inputBase to base $outputBase as a fraction\n"))
}

private fun changeBaseInteger(inputBase: Int, input: String, outputBase: Int): StepByStepResult<String> {
    if (inputBase == 10) {
        return decimalToBase(BigInteger(input), outputBase)
                .prependSteps(listOf("Converting $input to base $outputBase:"))
                .prependSteps(listOf("Converting $input from decimal to base $outputBase as a simple number\n"))
    }
    val (decResult, steps) = toDecimal(input, inputBase)
            .prependSteps(listOf("Converting $input from $inputBase to decimal:"))
    return decimalToBase(decResult, outputBase)
            .prependSteps(steps + "\n")
            .prependSteps(listOf("Converting $input from $inputBase to base $outputBase as a simple number\n"))
}

private fun changeBaseFromDecimalFraction(postComma: String, outputBase: Int, maxSteps: Int, preComma: String, input: String): StepByStepResult<String> {
    val trimmedPostComma = postComma.dropLastWhile { it == '0' }
    val fraction = postComma.toInt() over Math.pow(10.toDouble(), trimmedPostComma.length.toDouble()).toInt()
    val (fractionPart, steps) = decimalFractionToBase(fraction, outputBase, maxSteps)
            .prependSteps(listOf("Converting the fraction $fraction to base $outputBase:"))

    return decimalToBase(preComma.toBigInteger(), outputBase)
            .prependSteps(listOf("Converting the integer $preComma to base $outputBase:"))
            .appendSteps(listOf(""))
            .map({
                "${it.groupByFour()} , ${fractionPart.groupByFour()}"
            }, { steps })
            .prependSteps(listOf("Converting $input from decimal to base $outputBase as a fraction\n"))
}

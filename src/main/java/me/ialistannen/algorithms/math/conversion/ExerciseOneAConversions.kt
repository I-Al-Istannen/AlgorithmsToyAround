package me.ialistannen.algorithms.math.conversion

import java.util.concurrent.ThreadLocalRandom
import kotlin.streams.toList

fun String.alignOn(delim: String): String {
    val firstExpectedIndex = lines().map { it.indexOf(delim) }.max()!!
    return lines().joinToString("\n") {
        val actualIndex = it.indexOf(delim)

        if (actualIndex < 0) {
            return@joinToString it
        }

        val difference = firstExpectedIndex - actualIndex
        if (actualIndex < firstExpectedIndex) {
            it.substring(0 until actualIndex) + " ".repeat(difference) + it.substring(actualIndex)
        } else {
            it
        }
    }
}

fun main() {
    println()
    val changeBase = changeBase("10,125", 8, 2)
    println(changeBase.joinSteps())

    println()
    TableConversion().apply {
        addValue("3247,875", 10)
        addValue("10101101,101011", 2)
        addValue("257,774", 8)
        addValue("4D2,8", 16)
        addValue("7BB", 13)
    }.calculateValues()

    println(changeBase("137,000025", 10, 2).joinSteps())

    println(randomNums(1, 20, 10))
    println()
    println(getRandomSequence(1, 20, 10, listOf(2)))
}

fun randomNums(min: Int, max: Int, amount: Int): List<Int> {
    return generateSequence {
        ThreadLocalRandom.current().nextInt(min, max)
    }
            .distinct()
            .take(amount)
            .toList()
}

fun getRandomSequence(min: Int = 1, max: Int, size: Int, blacklist: Collection<Int> = emptySet()): List<Int> {
    return ThreadLocalRandom.current()
            .ints(min, max)
            .filter { it !in blacklist }
            .distinct()
            .limit(size.toLong())
            .toList()
}
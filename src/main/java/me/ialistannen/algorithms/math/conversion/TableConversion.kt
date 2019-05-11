package me.ialistannen.algorithms.math.conversion

typealias Base = Int

class TableConversion {

    private val values: MutableList<Pair<String, Base>> = mutableListOf()

    fun addValue(value: String, base: Base) {
        values.add(Pair(value, base))
    }

    fun calculateValues(maxSteps: Int = 4) {
        val bases = values.map { it.second }

        val results = values.map { pair ->
            "${pair.first}_${pair.second}" to bases.map {
                changeBase(pair.first, pair.second, it, maxSteps)
            }
        }

        val prefixAddition = results.map { it.first }.maxBy { it.length }!!.length


        val maxLength = results.flatMap { conversions ->
            conversions.second.map { it.result }
        }.map { it.length }.max()!! + 5

        println(" ".repeat(prefixAddition + 2) + bases.joinToString("") { it.toString().padEnd(maxLength) })
        println("-".repeat(prefixAddition + 2 + bases.size * maxLength))

        results.map { conversions ->
            conversions.first.padEnd(prefixAddition, ' ') + ": " + conversions.second.map { it.result }
                    .joinToString("") { it.padEnd(maxLength, ' ') }
        }.forEach {
            println(it)
        }
    }
}
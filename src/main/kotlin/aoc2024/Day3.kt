package aoc2024

import common.Challenge

val aoc2024day3 = object : Challenge(year = 2024, day = 3) {
    override fun silverStar(lines: List<String>): Long {
        val regex = """mul\(\d+,\d+\)""".toRegex()
        return regex.findAll(lines.joinToString("")).sumOf { match ->
            match.value.getMultiplied()
        }
    }

    override fun goldStar(lines: List<String>): Long {
        val regex = """don't\(\)|do\(\)|mul\(\d+,\d+\)""".toRegex()
        val allLines = lines.joinToString("")

        var enabled = true
        return regex.findAll(allLines).fold(0L) { acc, matchResult ->
            val match = matchResult.value
            if (match == "don't()") {
                enabled = false
                acc
            } else if (match == "do()") {
                enabled = true
                acc
            } else if (enabled) {
                acc + match.getMultiplied()
            } else {
                acc
            }
        }
    }
}

private fun String.getMultiplied(): Long {
    val (first, second) = substringAfter("(").substringBefore(")").split(",").map { it.toLong() }
    return first * second
}

fun main() = aoc2024day3.run()
package aoc2025

import common.Challenge
import utils.longs
import utils.transpose

private val aoc2025day6 = object : Challenge(year = 2025, day = 6) {
    override fun silverStar(lines: List<String>): Long {
        val numbers = lines.dropLast(1).map { it.longs() }.transpose()

        return lines.calculate(numbers)
    }

    override fun goldStar(lines: List<String>): Long {
        val numbers = mutableListOf<List<Long>>()
        var temp = mutableListOf<Long>()

        lines.dropLast(1)
            .map { it.toCharArray() }
            .transpose()
            .forEach { column ->
                column
                    .joinToString("")
                    .takeIf { it.isNotBlank() }
                    ?.filter { !it.isWhitespace() }
                    ?.toLong()
                    ?.also { temp += it }
                    ?: run {
                        numbers.add(temp)
                        temp = mutableListOf()
                    }
            }

        numbers.add(temp)

        return lines.calculate(numbers)
    }

    private fun List<String>.calculate(numbers: List<List<Long>>): Long {
        val operators = last().split(" ").filter { it.isNotEmpty() }

        return numbers.zip(operators).sumOf { (nums, operator) ->
            val operation: (Long, Long) -> Long = when (operator) {
                "+" -> Long::plus
                "-" -> Long::minus
                "*" -> Long::times
                else -> throw IllegalArgumentException("Unknown operator $operator")
            }
            nums.reduce { acc, num ->
                operation(acc, num)
            }
        }
    }
}

private fun List<CharArray>.transpose(): List<CharArray> {
    return List(this.maxOf { it.size }) { i -> CharArray(this.size) { j -> this.getOrNull(j)?.getOrNull(i) ?: ' ' } }
}

fun main() = aoc2025day6.run()
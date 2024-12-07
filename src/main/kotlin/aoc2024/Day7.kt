package aoc2024

import common.Challenge

val aoc2024day7 = object : Challenge(year = 2024, day = 7) {
    override fun silverStar(lines: List<String>): Long {
        val equations = lines.map { line ->
            line.substringBefore(":").toLong() to line.substringAfter(": ").split(" ").map { num -> num.toLong() }
        }
        return equations.sumOf { equation ->
            if (equation.first in equation.second.solve(0L)) equation.first else 0L
        }
    }

    override fun goldStar(lines: List<String>): Long {
        val equations = lines.map { line ->
            line.substringBefore(":").toLong() to line.substringAfter(": ").split(" ").map { num -> num.toLong() }
        }

        return equations.sumOf { equation ->
            if (equation.first in equation.second.solveWithConcatenation(0L)) equation.first else 0L
        }
    }

}

fun List<Long>.solve(sum: Long): List<Long> {
    if (isEmpty()) return listOf(sum)
    return drop(1).solve(sum + first()) +
           drop(1).solve(sum * first())
}

fun List<Long>.solveWithConcatenation(sum: Long): List<Long> {
    if (isEmpty()) return listOf(sum)
    return drop(1).solveWithConcatenation(sum + first()) +
            drop(1).solveWithConcatenation(sum * first()) +
            drop(1).solveWithConcatenation("$sum${first()}".toLong())
}

fun main() = aoc2024day7.run()
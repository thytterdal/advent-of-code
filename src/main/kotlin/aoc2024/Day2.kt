package aoc2024

import common.Challenge

val aoc2024day2 = object : Challenge(year = 2024, day = 2) {
    override fun silverStar(lines: List<String>): Long {
        val numbers = lines.splitToNumbers()
        return numbers.count { line ->
            line.isSafe()
        }.toLong()
    }

    override fun goldStar(lines: List<String>): Long {
        val numbers = lines.splitToNumbers()
        return numbers.count { line ->
            line.indices
                .map { badIndex -> line.filterIndexed { index, _ -> index != badIndex } }
                .any { it.isSafe() }
        }.toLong()
    }
}

private fun List<String>.splitToNumbers() =
    map { line -> line.split(" ").map { it.toLong() } }

private fun List<Long>.isSafe(): Boolean {
    val stepSize = descendingOrIncreasing()
    windowed(2).forEach { chunk ->
        if (stepSize(chunk) !in 1L..3L) {
            return false
        }
    }
    return true
}

private fun List<Long>.descendingOrIncreasing(): (List<Long>) -> Long {
    if (first() < last()) {
        return fun(chunk: List<Long>): Long {
            return chunk.last() - chunk.first()
        }
    } else {
        return fun(chunk: List<Long>): Long {
            return chunk.first() - chunk.last()
        }
    }
}

fun main() = aoc2024day2.run()



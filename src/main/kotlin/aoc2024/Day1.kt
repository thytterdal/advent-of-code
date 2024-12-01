package aoc2024

import common.Challenge
import kotlin.math.abs

private val aoc2024day1 = object : Challenge(year = 2024, day = 1) {
    override fun silverStar(lines: List<String>): Long {
        val (left, right) = lines.splitInput()

        val accumulatedDiff = left.foldIndexed(0L) { index, acc, l ->
            val diff = abs(l - right[index])
            acc + diff
        }

        return accumulatedDiff
    }

    override fun goldStar(lines: List<String>): Long {
        val (left, right) = lines.splitInput()

        val similarityScore = left.fold(0L) { acc, l ->
            val occurences = right.count { it == l }
            acc + l * occurences
        }

        return similarityScore
    }
}

private fun List<String>.splitInput(): Pair<List<Long>, List<Long>> {
    val split = associate { line ->
        val split = line.split(" ")
        split.first().toLong() to split.last().toLong()
    }
    val left = split.keys.sorted()
    val right = split.values.sorted()
    return left to right
}


fun main() = aoc2024day1.run()

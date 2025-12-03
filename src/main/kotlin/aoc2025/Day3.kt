package aoc2025

import common.Challenge

private val aoc2025day3 = object : Challenge(year = 2025, day = 3) {
    override fun silverStar(lines: List<String>) = lines.solve(1)

    override fun goldStar(lines: List<String>) = lines.solve(11)

    private fun List<String>.solve(n: Int) = sumOf { line ->
        var sub = line
        buildString {
            (n downTo 0).forEach { drop ->
                val max = sub.dropLast(drop).max()
                sub = sub.drop(sub.indexOf(max) + 1)
                append(max)
            }
        }.toLong()
    }
}


fun main() = aoc2025day3.run()
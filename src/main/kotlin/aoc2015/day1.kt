package aoc2015

import common.Challenge

fun main() {
    aoc2015Day1.run()
}

val aoc2015Day1 = object : Challenge(year = 2015, day = 1) {
    override fun silverStar(lines: List<String>): Long {
        return lines.first().fold(0L) { acc, char ->
            acc + when (char) {
                ')' -> -1L
                '(' -> 1L
                else -> 0L
            }
        }
    }

    override fun goldStar(lines: List<String>): Long {
        var level = 0L
        lines.first().forEachIndexed {index, char ->
            level += when (char) {
                ')' -> -1L
                '(' -> 1L
                else -> 0L
            }
            if (level < 0L) return index + 1L
        }

        return -1L
    }
}

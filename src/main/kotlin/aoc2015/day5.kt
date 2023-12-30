package aoc2015

import common.Challenge

fun main() = aoc2015day5.run()

private val aoc2015day5 = object : Challenge(year = 2015, day = 5) {
    private val vowels = "aeiou"
    private val naughty = "ab|cd|pq|xy".toRegex()
    private val repeatingPair = "([a-z][a-z]).*\\1".toRegex()
    private val repeatedLetter = "([a-z])[a-z]\\1".toRegex()

    override fun silverStar(lines: List<String>): Long {
        var nice = 0L

        lines.forEach { line ->
            if (
                !line.contains(naughty) &&
                line.fold(0L) { acc, char -> if (vowels.contains(char)) acc + 1 else acc } > 2 &&
                line.toList().zipWithNext { a, b -> a == b }.any { it }
            ) {
                nice++
            }
        }

        return nice
    }

    override fun goldStar(lines: List<String>): Long {
        var nice = 0L

        lines.forEach { line ->
            if (line.contains(repeatingPair) && line.contains(repeatedLetter)) {
                nice++
            }
        }

        return nice
    }

}

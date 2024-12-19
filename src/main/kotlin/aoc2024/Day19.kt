package aoc2024

import common.Challenge
import utils.splitOnEmptyLine

private val aoc2024day19 = object : Challenge(year = 2024, day = 19) {
    private val cache = hashMapOf<String, Long>()

    override fun silverStar(lines: List<String>): Long {
        val (rawTowels, patterns) = lines.splitOnEmptyLine()
        val towels = rawTowels.first().split(", ")

        return patterns.count { towels.checkPattern(it) }.toLong()
    }

    override fun goldStar(lines: List<String>): Long {
        val (rawTowels, patterns) = lines.splitOnEmptyLine()
        val towels = rawTowels.first().split(", ")

        return patterns.sumOf { towels.countArrangements(it) }
    }


    private fun List<String>.checkPattern(pattern: String, candidate: String = ""): Boolean {
        for (towel in this) {
            val current = candidate + towel
            if (current == pattern) {
                return true
            }
            if (pattern.startsWith(current)) {
                val isRestCorrect = checkPattern(pattern, current)
                if (isRestCorrect) {
                    return true
                }
            }
        }

        return false
    }

    private fun List<String>.countArrangements(pattern: String): Long {
        cache[pattern]?.let { return it }

        if (pattern.isEmpty()) {
            return 1L
        }

        val count = filter { towel -> pattern.startsWith(towel) }
            .sumOf { towel -> countArrangements(pattern.removePrefix(towel)) }

        cache[pattern] = count
        return count
    }
}

fun main() = aoc2024day19.run()
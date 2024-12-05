package aoc2024

import common.Challenge
import utils.splitOnEmptyLine

val aoc2024day5 = object : Challenge(year = 2024, day = 5) {
    override fun silverStar(lines: List<String>): Long {
        val (rules, updates) = lines.splitRulesAndUpdates()

        return updates.filter { update ->
            update.windowed(2).all { (a, b) -> a to b in rules }
        }.sumOf { it[it.size / 2] }
    }

    override fun goldStar(lines: List<String>): Long {
        val (rules, updates) = lines.splitRulesAndUpdates()

        return updates.filter { update ->
            !update.windowed(2).all { (a, b) -> a to b in rules }
        }.map { update ->
            val filteredRules = rules.filter { rule -> rule.first in update && rule.second in update }
            update.sortedWith { p0, p1 ->
                when {
                    p0 == p1 -> 0
                    p0 to p1 in filteredRules -> -1
                    else -> 1
                }
            }
        }.sumOf { it[it.size / 2] }
    }
}

private fun List<String>.splitRulesAndUpdates(): Pair<List<Pair<Long, Long>>, List<List<Long>>> {
    val (rawRules, rawUpdates) = splitOnEmptyLine()
    val rules = rawRules.map { rule ->
        rule.split("|").let { it.first().toLong() to it.last().toLong() }
    }
    val updates = rawUpdates.map { update ->
        update.split(",").map { it.toLong() }
    }
    return rules to updates
}

fun main() = aoc2024day5.run()
package aoc2025

import common.Challenge
import utils.numbers

private val aoc2025day12 = object : Challenge(year = 2025, day = 12) {

    override fun silverStar(lines: List<String>): Long {
        return lines.dropWhile { !it.contains('x') }.map {
            it.substringBefore(":").numbers() to it.substringAfter(":").numbers()
        }.count { (dimensions, presents) ->
            val maxPresentArea = presents.sum() * 9
            val availableArea = dimensions.first() * dimensions.last()
            maxPresentArea <= availableArea
        }.toLong()
    }

    override fun goldStar(lines: List<String>): Long {
        return 0L
    }
}

fun main() = aoc2025day12.run()
package aoc2025

import common.Challenge
import utils.Point
import utils.numbers
import kotlin.math.absoluteValue

private val aoc2025day9 = object : Challenge(year = 2025, day = 9) {

    override fun silverStar(lines: List<String>): Long {
        val coordinates = lines.map { it.numbers() }

        return coordinates.flatMapIndexed { index, (x1, y1) ->
            coordinates.dropLast(index).map { (x2, y2) ->
                Point(x1, y1) to Point(x2, y2)
            }
        }.maxOf { (first, second) ->
            val width = second.y - first.y + 1L
            val height = second.x - first.x + 1L
            (width * height).absoluteValue
        }

    }

    override fun goldStar(lines: List<String>): Long {
        return 0L
    }
}

fun main() = aoc2025day9.run()
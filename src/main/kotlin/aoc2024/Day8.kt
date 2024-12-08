package aoc2024

import common.Challenge
import utils.Point
import utils.allPairs
import utils.toGrid

val aoc2024day8 = object : Challenge(year = 2024, day = 8) {
    override fun silverStar(lines: List<String>): Long {
        val antinodes = mutableSetOf<Point>()
        val (grid, antennaPairs) = lines.getGridAndAntennaPairs()

        antennaPairs.forEach { (firstAntenna, secondAntenna) ->
            val distance = firstAntenna - secondAntenna
            antinodes.addAll(
                listOf(
                    secondAntenna.move(distance),
                    firstAntenna.move(distance.negate())
                ).filter { it in grid }
            )
        }

        return antinodes.count().toLong()
    }

    override fun goldStar(lines: List<String>): Long {
        val antinodes = mutableSetOf<Point>()
        val (grid, antennaPairs) = lines.getGridAndAntennaPairs()

        antennaPairs.forEach { (firstAntenna, secondAntenna) ->
            val distance = firstAntenna - secondAntenna

            generateSequence(firstAntenna) { it.move(distance.negate()) }
                .takeWhile { it in grid }
                .let(antinodes::addAll)

            generateSequence(secondAntenna) { it.move(distance) }
                .takeWhile { it in grid }
                .let(antinodes::addAll)
        }

        return antinodes.count().toLong()
    }
}

private fun List<String>.getGridAndAntennaPairs(): Pair<Map<Point, Char>, List<Pair<Point, Point>>> {
    val grid = toGrid()

    val antennaPairs = grid
        .filterValues { it != '.' }
        .entries
        .groupBy(keySelector = { it.value }, valueTransform = { it.key })
        .values
        .map(List<Point>::allPairs)
        .flatten()

    return grid to antennaPairs
}


fun main() = aoc2024day8.run()
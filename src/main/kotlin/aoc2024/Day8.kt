package aoc2024

import common.Challenge
import utils.Point
import utils.allPairs
import utils.toGrid

val aoc2024day8 = object : Challenge(year = 2024, day = 8) {
    override fun silverStar(lines: List<String>): Long {
        val antinodes = mutableSetOf<Point>()
        val (grid, antennaPairs) = lines.getGridAndAntennaPairs()

        antennaPairs.forEach { (first, second) ->
            val distance = first - second
            antinodes.addAll(
                listOf(
                    second.move(distance),
                    first.move(distance.negate())
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
            val negatedDistance = distance.negate()

            var firstAntinode = firstAntenna
            while (firstAntinode in grid) {
                antinodes.add(firstAntinode)
                firstAntinode = firstAntinode.move(negatedDistance)
            }

            var secondAntinode = secondAntenna
            while (secondAntinode in grid) {
                antinodes.add(secondAntinode)
                secondAntinode = secondAntinode.move(distance)
            }
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
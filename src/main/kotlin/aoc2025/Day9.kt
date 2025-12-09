package aoc2025

import common.Challenge
import utils.Point
import utils.numbers
import kotlin.math.absoluteValue

private val aoc2025day9 = object : Challenge(year = 2025, day = 9) {

    override fun silverStar(lines: List<String>): Long {
        val coordinates = lines.map { it.numbers() }

        return coordinates.flatMapIndexed { index, (x1, y1) ->
            coordinates.drop(index + 1).map { (x2, y2) ->
                (Point(x1, y1) to Point(x2, y2)).normalize()
            }
        }.maxOf { (first, second) ->
            val width = second.y - first.y + 1L
            val height = second.x - first.x + 1L
            width * height
        }

    }

    override fun goldStar(lines: List<String>): Long {
        val coordinates = lines.map { line -> line.numbers().let { Point(x = it.first(), y = it.last()) } }
        val normalizedLines = coordinates
            .plus(coordinates.first())
            .zipWithNext()
            .map { it.normalize() }

        return coordinates.flatMapIndexed { index, (x1, y1) ->
            coordinates.drop(index + 1).map { (x2, y2) ->
                (Point(x1, y1) to Point(x2, y2)).normalize()
            }
        }.filter { (corner1, corner2) ->
            normalizedLines.none { (lineStart, lineEnd) ->
                corner1.x < lineEnd.x && corner2.x > lineStart.x && corner1.y < lineEnd.y && corner2.y > lineStart.y
            }
        }.maxOf { (first, second) ->
            val width = second.y - first.y + 1L
            val height = second.x - first.x + 1L
            (width * height).absoluteValue
        }
    }

    private fun Pair<Point, Point>.normalize(): Pair<Point, Point> {
        val firstPoint = Point(x = minOf(first.x, second.x), y = minOf(first.y, second.y))
        val secondPoint = Point(x = maxOf(first.x, second.x), y = maxOf(first.y, second.y))
        return firstPoint to secondPoint
    }
}

fun main() = aoc2025day9.run()
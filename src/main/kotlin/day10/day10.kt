package day10

import utils.readInput
import utils.Point
import utils.pointInPolygon
import kotlin.math.ceil

fun main() {
    val lines = readInput("day10/input")

    firstStar(lines)
    secondStar(lines)
}

private fun firstStar(lines: List<String>) {
    val loop = findMainLoop(lines)

    val maxDistance = ceil((loop.keys.max() + 1) / 2f)

    println("First  ⭐: $maxDistance")
}

private fun secondStar(lines: List<String>) {
    val map = lines.toMap()
    val loop = findMainLoop(lines).values.toTypedArray()
    var sum = 0L

    map.forEachIndexed { row, line ->
        line.forEachIndexed { column, _ ->
            val point = Point(x = column, y = row)
            if (!loop.contains(point) && loop.pointInPolygon(point)) {
                sum++
            }
        }
    }

    println("Second ⭐: $sum")
}

private fun List<String>.toMap() =
    this.filter { it.isNotBlank() }.map { line -> line.toCharArray().toTypedArray() }.toTypedArray()

private fun findMainLoop(lines: List<String>): Map<Int, Point> {
    var currentPosition = Point(0, 0)
    val map = lines
        .filter { it.isNotBlank() }
        .mapIndexed { index, line ->
            if (line.contains("S")) currentPosition = Point(line.indexOf('S'), index)
            line.toCharArray().toTypedArray()
        }.toTypedArray()

    val loop = mutableMapOf(
        0 to currentPosition
    )

    var nextPosition = map.findStartPosition(currentPosition)
    var nextChar = map[nextPosition.x][nextPosition.y]

    while (nextChar != 'S') {
        val positionDifference =
            Point(
                x = (currentPosition.x + 1) - (nextPosition.x + 1),
                y = (currentPosition.y + 1) - (nextPosition.y + 1)
            )
        currentPosition = nextPosition

        nextPosition = when (nextChar) {
            'J' -> Point(
                x = if (positionDifference.x == 0) currentPosition.x - 1 else currentPosition.x,
                y = if (positionDifference.y == 0) currentPosition.y - 1 else currentPosition.y
            )

            'F' -> Point(
                x = if (positionDifference.x == 0) currentPosition.x + 1 else currentPosition.x,
                y = if (positionDifference.y == 0) currentPosition.y + 1 else currentPosition.y
            )

            '-' -> Point(
                x = currentPosition.x - positionDifference.x,
                y = currentPosition.y
            )

            '|' -> Point(
                x = currentPosition.x,
                y = currentPosition.y - positionDifference.y
            )

            'L' -> Point(
                x = if (positionDifference.x == 0) currentPosition.x + 1 else currentPosition.x,
                y = if (positionDifference.y == 0) currentPosition.y - 1 else currentPosition.y
            )

            '7' -> Point(
                x = if (positionDifference.x == 0) currentPosition.x - 1 else currentPosition.x,
                y = if (positionDifference.y == 0) currentPosition.y + 1 else currentPosition.y
            )

            else -> throw Exception("No path found $nextChar")
        }
        loop[loop.keys.max() + 1] = currentPosition
        nextChar = map[nextPosition.y][nextPosition.x]
    }

    return loop
}

private fun Array<Array<Char>>.findStartPosition(position: Point): Point {
    return when {
        this[position.y][position.x - 1] == 'F' -> Point(x = position.x - 1, y = position.y)
        this[position.y - 1][position.x] == 'F' -> Point(x = position.x - 1, y = position.y)
        this[position.y + 1][position.x] == 'J' -> Point(x = position.x + 1, y = position.y)
        this[position.y][position.x + 1] == 'F' -> Point(x = position.x, y = position.y + 1)
        this[position.y][position.x + 1] == 'J' -> Point(x = position.x, y = position.y + 1)
        this[position.y - 1][position.x] == '7' -> Point(x = position.x - 1, y = position.y)
        else -> throw Exception("No path found")
    }
}
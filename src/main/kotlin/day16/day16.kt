package day16

import utils.*

fun main() {
    val lines = readInput("day16/input")

    firstStar(lines)
    secondStar(lines)
}

private fun firstStar(lines: List<String>) {
    val energized = lines.traceBeam(0, 0, Direction.Right)

    println("First  ⭐: $energized")
}

private fun secondStar(lines: List<String>) {
    val energized = maxOf(
        lines.indices.maxOf { lines.traceBeam(0, it, Direction.Right) },
        lines.indices.maxOf { lines.traceBeam(lines.first().lastIndex, it, Direction.Left) },
        lines.first().indices.maxOf { lines.traceBeam(it, 0, Direction.Down) },
        lines.first().indices.maxOf { lines.traceBeam(it, lines.lastIndex, Direction.Up) }
    )

    println("Second ⭐: $energized")
}

private fun List<String>.traceBeam(x: Int, y: Int, initialDirection: Direction): Int {
    val stack = mutableListOf(Point(x, y) to initialDirection)
    val visited = stack.toMutableSet()
    while (true) {
        val (point, direction) = stack.removeLastOrNull() ?: break
        for (dir in directionMapping[direction to this[point]] ?: listOf(direction)) {
            val nextPoint = point + dir
            if (
                nextPoint.x in this.first().indices && nextPoint.y in this.indices
            ) {
                val next = nextPoint to dir
                if (visited.add(next)) {
                    stack.add(next)
                }
            }
        }
    }
    return visited.mapTo(mutableSetOf()) { it.first }.size
}

private val directionMapping: Map<Pair<Direction, Char>, List<Direction>> = mapOf(
    Direction.Right to '\\' to listOf(Direction.Down),
    Direction.Right to '/' to listOf(Direction.Up),
    Direction.Right to '|' to listOf(Direction.Up, Direction.Down),
    Direction.Left to '/' to listOf(Direction.Down),
    Direction.Left to '\\' to listOf(Direction.Up),
    Direction.Left to '|' to listOf(Direction.Up, Direction.Down),
    Direction.Up to '/' to listOf(Direction.Right),
    Direction.Up to '\\' to listOf(Direction.Left),
    Direction.Up to '-' to listOf(Direction.Left, Direction.Right),
    Direction.Down to '/' to listOf(Direction.Left),
    Direction.Down to '\\' to listOf(Direction.Right),
    Direction.Down to '-' to listOf(Direction.Left, Direction.Right)
)
package aoc2024

import common.Challenge
import utils.Direction
import utils.Point
import utils.toGrid
import utils.turn90degrees

val aoc2024day6 = object : Challenge(year = 2024, day = 6) {
    override fun silverStar(lines: List<String>): Long {
        val grid = lines.toGrid()

        val start = grid.entries.first { it.value == '^' }.key
        val direction = Direction.Up

        val positions = grid.moveGuard(start, direction)

        return positions.size.toLong()
    }

    override fun goldStar(lines: List<String>): Long {
        val grid = lines.toGrid()

        val start = grid.entries.first { it.value == '^' }.key
        val direction = Direction.Up

        val positions = grid.moveGuard(start, direction) - start
        return positions.count { position ->
            grid.toMutableMap().also { it[position] = '#' }.moveGuard(start, direction).isEmpty()
        }.toLong()
    }
}

private fun Map<Point, Char>.moveGuard(
    startPosition: Point,
    startDirection: Direction
): Set<Point> {
    var pos = startPosition
    var direction = startDirection
    val positions = mutableSetOf(pos to direction)

    while (true) {
        val next = pos.move(1, direction = direction)
        if (next !in this) {
            break
        }
        if (this[next] == '#') {
            direction = direction.turn90degrees()
        } else {
            pos = next
            if (pos to direction in positions) return emptySet()
            positions.add(pos to direction)
        }

    }
    return positions.unzip().first.toSet()
}

fun main() = aoc2024day6.run()
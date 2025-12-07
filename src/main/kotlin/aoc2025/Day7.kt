package aoc2025

import common.Challenge
import utils.Direction
import utils.Point
import utils.toGrid

private val aoc2025day7 = object : Challenge(year = 2025, day = 7) {
    private val cache = mutableMapOf<Point, Long>()

    override fun silverStar(lines: List<String>): Long {
        val grid = lines.toGrid()
        var beams = grid.filterValues { it == 'S' }.keys.toSet()
        var splits = 0L

        repeat(lines.size - 1) {
            beams = beams.flatMapTo(mutableSetOf()) { beam ->
                val pos = beam + Direction.Down
                if (grid[pos] == '^') {
                    splits++
                    setOf(pos + Direction.Left, pos + Direction.Right)
                } else {
                    setOf(pos)
                }
            }
        }

        return splits
    }

    override fun goldStar(lines: List<String>): Long {
        val grid = lines.toGrid()
        var start = grid.filterValues { it == 'S' }.keys.first()
        return grid.shootBeam(start)
    }

    private fun Map<Point, Char>.shootBeam(beam: Point): Long {
        cache[beam]?.let { return it }
        val pos = beam + Direction.Down
        return when (this[pos]) {
            '.' -> shootBeam(pos)
            '^' -> shootBeam(pos + Direction.Left) + shootBeam(pos + Direction.Right)
            else -> 1L
        }.also { cache[beam] = it }
    }
}

fun main() = aoc2025day7.run()
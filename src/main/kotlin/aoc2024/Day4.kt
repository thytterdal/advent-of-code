package aoc2024

import common.Challenge
import utils.*

val aoc2024day4 = object : Challenge(year = 2024, day = 4) {
    override fun silverStar(lines: List<String>): Long {
        val grid = lines.toGrid()

        return grid.keys.fold(0L) { acc, point ->
            acc + AllDirections.count { dir ->
                List(4) { idx -> grid[point.move(idx, dir)] }.joinToString("") == "XMAS"
            }
        }
    }

    override fun goldStar(lines: List<String>): Long {
        val grid = lines.toGrid()

        val directions = listOf(
            listOf(Direction.UpLeft, Direction.None, Direction.DownRight),
            listOf(Direction.DownLeft, Direction.None, Direction.UpRight)
        )

        val valid = listOf("MAS", "SAM")

        return grid.keys.count { point ->
            directions.all { dirs ->
                dirs.map { dir -> grid[point.move(1, dir)] }.joinToString("") in valid
            }
        }.toLong()
    }
}

fun main() = aoc2024day4.run()
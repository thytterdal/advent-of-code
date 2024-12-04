package aoc2024

import common.Challenge
import utils.Direction
import utils.Point
import utils.positionIsValid
import utils.transpose

val aoc2024day4 = object : Challenge(year = 2024, day = 4) {
    override fun silverStar(lines: List<String>): Long {
        val grid = lines.map { it.toCharArray() }

        val directions = listOf(
            listOf(Direction.Right),
            listOf(Direction.Right, Direction.Up),
            listOf(Direction.Up),
            listOf(Direction.Up, Direction.Left),
            listOf(Direction.Left),
            listOf(Direction.Left, Direction.Down),
            listOf(Direction.Down),
            listOf(Direction.Down, Direction.Right)
        )

        return grid.foldIndexed(0L) { y, outerAcc, line ->
            outerAcc + line.foldIndexed(0L) { x, acc, point ->
                if(point == 'X') {
                    acc + directions.count { dirs ->
                        val start = Point(x, y)
                        List(4) { idx ->
                            var pos = start
                            dirs.forEach { dir ->
                                pos = pos.move(idx, dir)
                            }
                            if(positionIsValid(pos, grid)) {
                                grid[pos]
                            } else {
                                ""
                            }
                        }.joinToString("") == "XMAS"
                    }

                } else {
                    acc
                }
            }
        }
    }

    override fun goldStar(lines: List<String>): Long {
        val grid = lines.map { it.toCharArray() }

        val directions = listOf(
            listOf(Direction.Left, Direction.Up),
            listOf(Direction.Right, Direction.Down),
            listOf(Direction.Left, Direction.Down),
            listOf(Direction.Right, Direction.Up),
        )

        val valid = listOf(
            "SMMS","MSSM", "SMSM", "MSMS"
        )

        return grid.foldIndexed(0L) { y, outerAcc, line ->
            outerAcc + line.foldIndexed(0L) { x, acc, point ->
                if(point == 'A') {
                    val cross = directions.map { dirs ->
                        var pos = Point(x, y)
                        dirs.forEach { dir ->
                            pos = pos.move(1, dir)
                        }
                        if(positionIsValid(pos, grid)) {
                            grid[pos]
                        } else {
                            ""
                        }
                    }.joinToString("")

                    if(cross in valid) {
                        acc +1
                    } else {
                        acc
                    }

                } else {
                    acc
                }
            }
        }
    }

}

private fun positionIsValid(point: Point, grid: List<CharArray>): Boolean {
    return point.x in grid.first().indices && point.y in grid.indices
}

private operator fun List<CharArray>.get(point: Point): Char {
    return this[point.y][point.x]
}

fun main() = aoc2024day4.run()
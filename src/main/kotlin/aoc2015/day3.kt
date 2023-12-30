package aoc2015

import common.Challenge
import utils.Direction
import utils.Point

fun main() = aoc2015day3.run()


private val aoc2015day3 = object : Challenge(year = 2015, day = 3) {
    override fun silverStar(lines: List<String>): Long {
        var current = Point(x = 0, y = 0)
        val visited = mutableSetOf(current)
        lines.first().forEach { char ->
            when(char) {
                '>' -> current += Direction.Right
                '<' -> current += Direction.Left
                '^' -> current += Direction.Up
                'v' -> current += Direction.Down
            }
            visited += current
        }

        return visited.size.toLong()
    }

    override fun goldStar(lines: List<String>): Long {
        var santa = Point(x = 0, y = 0)
        var robotSanta = Point(x = 0, y = 0)
        val visited = mutableSetOf(santa)
        lines.first().forEachIndexed { index, char ->
            val direction = when(char) {
                '>' ->  Direction.Right
                '<' -> Direction.Left
                '^' -> Direction.Up
                'v' -> Direction.Down
                else -> throw Exception("Unexpected input")
            }
            if (index %2 == 0) {
                santa += direction
                visited += santa
            }else {
                robotSanta += direction
                visited += robotSanta
            }
        }
        return visited.size.toLong()
    }

}
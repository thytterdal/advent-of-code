package aoc2024

import common.Challenge
import utils.Point
import utils.toGrid

private val aoc2024day10 = object : Challenge(year = 2024, day = 10) {
    override fun silverStar(lines: List<String>): Long {
        val grid = lines.toGrid { it.digitToInt() }
        val trailheads = grid.filterValues { it == 0 }.map { it.key }

        val trailScores = trailheads.map { startPoint ->
            (1..9).fold(listOf(startPoint)) { acc, i ->
                    acc.flatMap { pos -> pos.neighbors().filter { grid[it] == i } }.distinct()
                }.count()
        }
        return trailScores.sum().toLong()
    }

    override fun goldStar(lines: List<String>): Long {
        val grid = lines.toGrid { it.digitToInt() }
        val trailheads = grid.filterValues { it == 0 }.map { it.key }

        val trailScrores = trailheads.map { startPoint ->
            (1..9).fold(listOf(startPoint)) { acc, i ->
                    acc.flatMap { pos -> pos.neighbors().filter { grid[it] == i } }
                }.count()
        }
        return trailScrores.sum().toLong()
    }
}


fun main() = aoc2024day10.run()
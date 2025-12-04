package aoc2025

import common.Challenge
import utils.Point
import utils.toGrid

private val aoc2025day4 = object : Challenge(year = 2025, day = 4) {
    override fun silverStar(lines: List<String>): Long {
        val grid = lines.toGrid()

        return grid.findForkliftAccessibleToiletPaperRolls().size.toLong()
    }

    override fun goldStar(lines: List<String>): Long {
        val grid = lines.toGrid().toMutableMap()
        var removed = 0L

        while (true) {
            removed += grid.findForkliftAccessibleToiletPaperRolls()
                .also { if (it.isEmpty()) break }
                .onEach(grid::remove)
                .size
        }

        return removed
    }

    private fun Map<Point, Char>.findForkliftAccessibleToiletPaperRolls() =
        filterValues { it == '@' }.keys.filter { point ->
            point.allNeighbors().map(this::get).count { it == '@' } < 4
        }
}


fun main() = aoc2025day4.run()
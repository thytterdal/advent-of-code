package aoc2024

import common.Challenge
import utils.Direction
import utils.Point
import utils.toGrid

private val aoc2024day12 = object : Challenge(year = 2024, day = 12) {
    override fun silverStar(lines: List<String>): Long {
        val grid = lines.toGrid()

        val groups = grid.entries.groupBy({ it.value }, { it.key }).values.flatMap { it.findRegions() }

        return groups.sumOf {
            it.size * it.findPerimiter()
        }.toLong()
    }

    override fun goldStar(lines: List<String>): Long {
        val grid = lines.toGrid()

        val groups = grid.entries.groupBy({ it.value }, { it.key }).values.flatMap { it.findRegions() }

        return groups.sumOf {
            it.size * it.findSides().size
        }.toLong()
    }
}

private fun Set<Point>.findPerimiter() = sumOf {
    it.neighbors().filter { neighbor -> neighbor !in this }.size
}

private fun Set<Point>.findSides() = buildSet {
    this@findSides.groupBy { it.y }.forEach { (level, points) ->
        points.addSides(level, Direction.Up, this@findSides, { this.x }, ::add)
        points.addSides(level, Direction.Down, this@findSides, { this.x }, ::add)
    }
    this@findSides.groupBy { it.x }.forEach { (level, points) ->
        points.addSides(level, Direction.Right, this@findSides, { this.y }, ::add)
        points.addSides(level, Direction.Left, this@findSides, { this.y }, ::add)
    }
}

private fun List<Point>.addSides(
    level: Int,
    direction: Direction,
    points: Set<Point>,
    selector: Point.() -> Int,
    operation: (Triple<Int, Int, Direction>) -> Unit
) {
    val min = minBy(selector)
    val max = maxBy(selector)
    return (min.selector()..max.selector()).map { coordinate ->
        firstOrNull { it.selector() == coordinate }?.let { if (it.move(1, direction) !in points) 1 else 0 } ?: 0
    }.joinToString("")
        .split("0")
        .filter { it.startsWith("1") }
        .forEachIndexed { index, _ ->
            operation(Triple(level, index, direction))
        }
}

private fun List<Point>.findRegions() = buildSet {
    val unused = this@findRegions.toMutableSet()
    while (unused.isNotEmpty()) {
        val test = mutableSetOf<Point>()
        val region = exploreRegion(unused, unused.first(), test)
        add(region)
        unused.removeAll(region)
    }
}

fun exploreRegion(unused: Set<Point>, startPoint: Point, region: MutableSet<Point>): Set<Point> {
    region.add(startPoint)
    val neighbors = startPoint.neighbors()
    neighbors.forEach { point: Point ->
        if (point in unused && point !in region) {
            region.addAll(exploreRegion(unused, point, region))
        }
    }

    return region
}

fun main() = aoc2024day12.run()

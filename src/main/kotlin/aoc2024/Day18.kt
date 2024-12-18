package aoc2024

import common.Challenge
import utils.Graph
import utils.Point

private val aoc2024day18 = object : Challenge(year = 2024, day = 18) {
    override fun silverStar(lines: List<String>): Long {
        val memorySpace = createMemoryMap(70)
        val bytes = lines.map { line -> line.split(",").let { Point(it.first().toInt(), it.last().toInt()) } }
        val end = Point(70, 70)

        bytes.take(1024).forEach { byte ->
            memorySpace[byte] = '#'
        }

        val bestPath = Graph.dijkstra(
            startPosition = Point(0, 0),
            neighbors = { memorySpace.neighbors(it) },
            endCondition = { it == end }
        )

        return bestPath.last().weight.toLong()
    }

    override fun goldStar(lines: List<String>): Long {
        val memorySpace = createMemoryMap(70)
        val bytes = lines.map { line -> line.split(",").let { Point(it.first().toInt(), it.last().toInt()) } }
        val end = Point(70, 70)

        bytes.take(1024).forEach { byte ->
            memorySpace[byte] = '#'
        }
        var index = 1024

        while (true) {
            memorySpace[bytes[index]] = '#'

            val path = Graph.dijkstra(
                startPosition = Point(0, 0),
                neighbors = { memorySpace.neighbors(it) },
                endCondition = { it == end }
            )
            if (path.isEmpty()) {
                println(bytes[index])
                break
            }
            index++
        }

        return index.toLong()
    }

    fun Map<Point, Char>.neighbors(point: Point) = point
        .neighbors()
        .mapNotNull {
            if (it in this && this[it] != '#') {
                Graph.Edge(it, 1)
            } else {
                null
            }
        }

    fun createMemoryMap(size: Int) = buildMap {
        (0..size).forEach { y ->
            (0..size).forEach { x ->
                put(Point(x, y), '.')
            }
        }
    }.toMutableMap()

}

fun main() = aoc2024day18.run()
package aoc2024

import common.Challenge
import utils.Graph
import utils.Point
import utils.toGrid

private val aoc2024day20 = object : Challenge(year = 2024, day = 20) {
    override fun silverStar(lines: List<String>) = lines.getPath().shorterPathsWithCheat(2)

    override fun goldStar(lines: List<String>) = lines.getPath().shorterPathsWithCheat(20)

    fun List<String>.getPath(): List<Graph.Vertex<Point>> {
        val grid = toGrid()
        val start = grid.filterValues { it == 'S' }.keys.first()
        val end = grid.filterValues { it == 'E' }.keys.first()

        return Graph.dijkstra(
            startPosition = start,
            endCondition = { it == end },
            neighbors = {
                it.neighbors().mapNotNull {
                    if (it in grid && grid[it] != '#') {
                        Graph.Edge(it, 1)
                    } else {
                        null
                    }
                }
            }
        )
    }

    fun List<Graph.Vertex<Point>>.shorterPathsWithCheat(maxCheatDistance: Int) = flatMap { vertex ->
        val cheatStart = vertex.position
        val cheatStartWeight = vertex.weight
        val others = this.filter { it.weight > vertex.weight }
        others.mapNotNull { other ->
            val cheatEnd = other.position
            val distance = cheatStart.distanceTo(cheatEnd)
            if (distance <= maxCheatDistance) {
                other.weight - (cheatStartWeight + distance)
            } else {
                null
            }
        }
    }.count { it >= 100 }.toLong()
}

fun main() = aoc2024day20.run()
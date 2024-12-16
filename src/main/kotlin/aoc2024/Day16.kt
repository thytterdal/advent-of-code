package aoc2024

import common.Challenge
import utils.*
import java.util.*

private val aoc2024day16 = object : Challenge(year = 2024, day = 16) {
    override fun silverStar(lines: List<String>): Long {
        val grid = lines.toGrid()
        val start = grid.filterValues { it == 'S' }.keys.first() to Direction.Right
        val path = findBestPath(start, grid)

        return path.last().weight.toLong()
    }


    override fun goldStar(lines: List<String>): Long {
        val grid = lines.toGrid()
        val start = grid.filterValues { it == 'S' }.keys.first() to Direction.Right
        val end = grid.filterValues { it == 'E' }.keys.first()
        val bestPathWeight = findBestPath(start, grid).last().weight

        val bestPaths = findPaths(
            startPosition = start,
            grid = grid,
            end = end,
            maxWeight = bestPathWeight
        )

        val tiles = buildSet {
            bestPaths.forEach { vertex ->
                addAll(vertex.path().map { it.position.first })
            }
        }

        return tiles.size.toLong()
    }

    private fun findPaths(
        startPosition: Pair<Point, Direction>,
        grid: Map<Point, Char>,
        end: Point,
        maxWeight: Int
    ): List<Graph.Vertex<Pair<Point, Direction>>> {
        val visited = mutableSetOf(startPosition)
        val paths = mutableListOf<Graph.Vertex<Pair<Point, Direction>>>()

        val queue = PriorityQueue<Graph.Vertex<Pair<Point, Direction>>>().apply {
            add(Graph.Vertex(startPosition, 0))
        }

        while (queue.isNotEmpty()) {
            val current = queue.poll()
            visited.add(current.position)

            if (current.position.first == end && current.weight == maxWeight) {
                paths.add(current)
                continue
            }
            if (current.weight > maxWeight) {
                continue
            }

            current.position.neighbors(grid).forEach { neighbor ->
                if (neighbor.vertexPosition !in visited) {
                    queue.add(neighbor.toVertex(current))
                }
            }
        }
        return paths
    }

    private fun findBestPath(
        start: Pair<Point, Direction.Right>,
        grid: Map<Point, Char>
    ) = Graph.dijkstra<Pair<Point, Direction>>(
        startPosition = start,
        neighbors = { it.neighbors(grid) },
        endCondition = { (point, _) -> grid[point] == 'E' }
    )

    private fun Pair<Point, Direction>.neighbors(grid: Map<Point, Char>) = listOf(
        second to 1,
        second.turn90degrees() to 1001,
        second.turn90degrees().inverse() to 1001
    ).mapNotNull { (direction, weight) ->
        val next = first.move(1, direction)
        if (grid[next] != '#') {
            Graph.Edge(vertexPosition = next to direction, weight = weight)
        } else {
            null
        }
    }

}

fun main() = aoc2024day16.run()
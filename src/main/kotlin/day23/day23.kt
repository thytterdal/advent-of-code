package day23

import utils.*
import kotlin.math.max

fun main() {
    val lines = readInput("day23/input")
    val start = Point(
        x = lines.first().indexOfFirst { it == '.' },
        y = 0
    )
    val end = Point(
        x = lines.last().indexOfFirst { it == '.' },
        y = lines.lastIndex
    )

    firstStar(lines, start, end)
    secondStar(lines, start, end)
}

private fun firstStar(lines: List<String>, start: Point, end: Point) =
    println("First  ⭐: ${solve(start, end, lines::findNeighborsWithSlopes)}")

private fun secondStar(lines: List<String>, start: Point, end: Point) =
    println("Second ⭐: ${solve(start, end, lines::findNeighbors)}")

private fun solve(start: Point, end: Point, findNeighbors: (Point) -> List<Point>): Int {
    val vertices = findVerticesWherePathSplits(start, end, findNeighbors)
    val edges = findEdges(vertices, findNeighbors)

    val graph = vertices.associate { point ->
        val pointEdges = edges[point] ?: throw Exception("Coordinate not found")
        vertices.indexOf(point) to pointEdges.map { (p, length) -> vertices.indexOf(p) to length }
    }

    return findMaxSteps(graph, vertices.indexOf(start), vertices.indexOf(end))
}

private fun findVerticesWherePathSplits(
    start: Point,
    end: Point,
    findNeighbors: (Point) -> List<Point>
): Set<Point> {
    val nodes = mutableSetOf(start, end)
    val queue = ArrayDeque<Point>().apply { add(start) }
    val seen = mutableSetOf<Point>()
    while (queue.isNotEmpty()) {
        val current = queue.removeFirst()

        if (current in seen) continue
        seen.add(current)

        val neighbors = findNeighbors(current)
        if (neighbors.size >= 3) {
            nodes.add(current)
        }
        queue.addAll(neighbors)
    }
    return nodes
}

private fun findEdges(
    vertices: Set<Point>,
    findNeighbors: (Point) -> List<Point>
): MutableMap<Point, MutableList<Pair<Point, Int>>> {
    val result = mutableMapOf<Point, MutableList<Pair<Point, Int>>>()
    for (vertex in vertices) {
        val edges = mutableListOf<Pair<Point, Int>>()
        val queue = ArrayDeque<Pair<Point, Int>>().apply { add(vertex to 0) }
        val seen = hashSetOf<Point>()
        while (queue.isNotEmpty()) {
            val (position, length) = queue.removeFirst()
            if (position != vertex && position in vertices) {
                edges.add(position to length)
                continue
            }

            if (position in seen) continue
            seen.add(position)

            val neighbors = findNeighbors(position)
            for (neighbor in neighbors) {
                queue.add(neighbor to length + 1)
            }
        }
        result[vertex] = edges
    }
    return result
}

private fun findMaxSteps(
    graph: Map<Int, List<Pair<Int, Int>>>,
    current: Int,
    target: Int,
    seen: HashSet<Int> = hashSetOf()
): Int {
    if (current == target) {
        return 0
    } else if (current in seen) {
        return -1
    }
    seen.add(current)
    var maxSteps = -1
    for ((neighbour, length) in graph[current]!!) {
        val steps = findMaxSteps(graph, neighbour, target, seen)
        if (steps >= 0) {
            maxSteps = max(maxSteps, steps + length)
        }
    }
    seen.remove(current)
    return maxSteps
}

private fun List<String>.findNeighborsWithSlopes(point: Point): List<Point> {
    return when (this[point]) {
        '<' -> listOf(point + Direction.Left)
        '>' -> listOf(point + Direction.Right)
        'v' -> listOf(point + Direction.Down)
        '^' -> listOf(point + Direction.Up)
        else -> point.neighbors()
    }.filter { this.positionInStringIsValid(it) && this[it] != '#' }
}

private fun List<String>.findNeighbors(point: Point) = point.neighbors()
    .filter { this.positionInStringIsValid(it) && this[it] != '#' }


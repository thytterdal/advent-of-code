package aoc2025

import common.Challenge
import utils.longs
import kotlin.math.pow
import kotlin.math.sqrt

private val aoc2025day8 = object : Challenge(year = 2025, day = 8) {

    val unionFind = UnionFind()

    override fun silverStar(lines: List<String>): Long {
        val (points, connections) = parseInput(lines)

        unionFind.setParentSize(points.size)

        connections.take(1000).forEach { connection ->
            unionFind.union(connection.first.index, connection.second.index)
        }

        return points
            .map { unionFind.count(it.index) }
            .sorted()
            .takeLast(3)
            .reduce(Int::times)
            .toLong()
    }

    override fun goldStar(lines: List<String>): Long {
        val (points, connections) = parseInput(lines)

        connections.drop(1000).forEach { connection ->
            unionFind.union(connection.first.index, connection.second.index)

            if (points.singleOrNull { unionFind.count(it.index) > 0 } != null) {
                return (connection.first.x.toLong() * connection.second.x.toLong())
            }
        }

        return 0L
    }

    private fun parseInput(lines: List<String>): Pair<List<JunctionBox>, List<Connection>> {
        val points = lines.mapIndexed { index, line ->
            line.longs()
                .map(Long::toDouble)
                .let { JunctionBox(x = it.first(), y = it[1], z = it.last(), index = index) }
        }

        val connections = points.flatMapIndexed { outerIndex, point ->
            points.mapIndexedNotNull { innerIndex, p2 ->
                if (innerIndex > outerIndex) {
                    Connection(point, p2, point.distanceTo(p2))
                } else {
                    null
                }
            }
        }.sortedBy { it.distance }
        return Pair(points, connections)
    }
}

private fun JunctionBox.distanceTo(other: JunctionBox): Double {
    return sqrt(
        (x - other.x).pow(2.0) + (y - other.y).pow(2.0) + (z - other.z).pow(2.0)
    )
}

private data class JunctionBox(
    val x: Double,
    val y: Double,
    val z: Double,
    val index: Int
)

private data class Connection(
    val first: JunctionBox,
    val second: JunctionBox,
    val distance: Double
)

class UnionFind() {
    var parent = intArrayOf()

    fun find(i: Int): Int {
        if (parent[i] != i) parent[i] = find(parent[i])
        return parent[i]
    }

    fun union(i: Int, j: Int) {
        val iParent = find(i)
        val jParent = find(j)
        parent[iParent] = jParent
    }

    fun count(x: Int) = parent.count { find(it) == x }

    fun setParentSize(size: Int) {
        parent = IntArray(size) { it }
    }
}

fun main() = aoc2025day8.run()
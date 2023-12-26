package day25

import utils.readInput

fun main() {
    val lines = readInput("day25/input")

    firstStar(lines)
}

private fun firstStar(lines: List<String>) {
    val (vertices, edges) = lines.findConnections()

    val sum = kargerMinCut(vertices, edges).fold(1L) { acc, strings -> acc * strings.size }

    println("First  ⭐: $sum")
}

private fun kargerMinCut(vertices: Set<String>, edges: Set<Pair<String, String>>): List<List<String>> {
    var subsets = listOf<MutableList<String>>()

    while (edges.countCuts(subsets) != 3) {
        subsets = buildList {
            vertices.forEach { vertex ->
                add(mutableListOf(vertex))
            }
        }.toMutableList()
        while (subsets.size > 2) {
            val edge = edges.random()

            val subset1 = subsets.first { it.contains(edge.first) }
            val subset2 = subsets.first { it.contains(edge.second) }

            if (subset1 == subset2) continue

            subsets.remove(subset2)
            subset1.addAll(subset2)
        }
    }

    return subsets
}

private fun Set<Pair<String, String>>.countCuts(subsets: List<List<String>>): Int {
    var cuts = 0
    if(subsets.isNotEmpty()) {
        this.forEach { edge ->
            val subset1 = subsets.first { it.contains(edge.first) }
            val subset2 = subsets.first { it.contains(edge.second) }
            if(subset1 != subset2) cuts++
        }
    }
    return cuts
}


private fun List<String>.findConnections(): Pair<Set<String>, Set<Pair<String, String>>> {
    val vertices = hashSetOf<String>()
    val edges = hashSetOf<Pair<String, String>>()
    this.map { line ->
        val (from, to) = line.split(":").let { it.first() to it.last().trim().split(" ") }
        vertices.add(from)
        vertices.addAll(to)
        to.forEach {
            if (!edges.contains(it to from) && !edges.contains(from to it)) {
                edges.add(from to it)
            }
        }
    }

    return vertices to edges
}
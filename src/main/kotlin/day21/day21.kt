package day21


import utils.*

fun main() {
    val lines = readInput("day21/input")

    firstStar(lines)
    secondStar(lines)
}

private fun firstStar(lines: List<String>) {
    val startY = lines.indexOfFirst { it.contains('S') }
    val startX = lines[startY].indexOfFirst { it == 'S' }
    val start = Point(x = startX, y = startY)

    val steps = mutableMapOf(
        0 to listOf(start)
    )

    var current = listOf(start)
    val visited = current.toMutableSet()

    (1..64).forEach { step ->
        current = current.flatMap { it.neighbors() }.filter { it !in visited && lines[it] != '#' }.distinct()
        steps[step] = current
        visited.addAll(current)
    }

    val sum = (0..64 step 2).flatMap { steps[it] ?: throw Exception("Step is null") }.size

    println("First  ⭐: $sum")
}

private fun secondStar(lines: List<String>) {
    val sum = 0

    println("Second ⭐: $sum")
}
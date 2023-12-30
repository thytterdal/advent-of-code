package aoc2023.day21


import utils.*

fun main() {
    val lines = readInput("aoc2023/day21/input")

    val startY = lines.indexOfFirst { it.contains('S') }
    val startX = lines[startY].indexOfFirst { it == 'S' }
    val start = Point(x = startX, y = startY)
    val height = lines.size
    val width = lines.first().length

    firstStar(lines, start, width, height)
    secondStar(lines, start, width, height)
}

private fun firstStar(lines: List<String>, start: Point, width: Int, height: Int) {
    val sum = lines
        .stepSequence(start, width, height)
        .take(65)
        .last()
        .second

    println("First  ⭐: $sum")
}

private fun secondStar(lines: List<String>, start: Point, width: Int, height: Int) {
    val steps = 26501365
    val results = mutableMapOf<Set<Point>, MutableList<Long>>()

    for ((points, score) in lines.stepSequence(start, width, height)) {
        val normalizedPoints = points.map { it.normalizedPosition(height, width) }.toSet()
        when (results[normalizedPoints]?.size) {
            3 -> break
            else -> results.getOrPut(normalizedPoints, ::mutableListOf).add(score) 
        }
    }

    val coefficients = results.values.filter { it.size == 3 }.map { it.quadraticCoefficients() }
    val cycleSize = coefficients.size
    val cycleStart = results.values.indexOfFirst { it.size == 3 }
    val indexInCycle = (steps - cycleStart) % cycleSize
    val cycle = (steps - cycleStart) / cycleSize + 1

    val sum = coefficients[indexInCycle].let { it.first * cycle * cycle + it.second * cycle + it.third }

    println("Second ⭐: $sum")
}

private fun List<String>.stepSequence(start: Point, width: Int, height: Int) = sequence {
    var previous = emptySet<Point>()
    var current = setOf(start)
    var previousSum = 0L
    var sum = 1L
    while (true) {
        yield(current to sum)
        val next = buildSet {
            current.forEach { point ->
                point.neighbors().forEach { neighbor ->
                    val normalizedPoint = neighbor.normalizedPosition(height, width)
                    if (this@stepSequence[normalizedPoint] != '#' && neighbor !in previous) {
                        add(neighbor)
                    }
                }
            }
        }
        previous = current
        current = next
        previousSum = sum.also { sum = previousSum + current.size }
    }
}


private infix fun Int.floorMod(other: Int) = ((this % other) + other) % other

private fun Point.normalizedPosition(height: Int, width: Int) = Point(
    x = this.x.floorMod(width),
    y = this.y.floorMod(height)
)

// https://math.stackexchange.com/questions/680646/get-polynomial-function-from-3-points
private fun List<Long>.quadraticCoefficients(): Triple<Long, Long, Long> {
    val a = (this[2] - this[1] * 2 + this[0]) / 2
    val b = (this[1] - this[0]) - a * 3
    val c = this[0] - a - b
    return Triple(a, b, c)
}
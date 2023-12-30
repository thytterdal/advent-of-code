package aoc2023.day13

import utils.readInput
import utils.splitOnEmpty
import utils.transpose

typealias pattern = List<List<Char>>

fun main() {
    val lines = readInput("aoc2023/day13/input")

    firstStar(lines)
    secondStar(lines)
}

private fun firstStar(lines: List<String>) {
    val sum = lines.findSum(::directMatch)

    println("First  ⭐: $sum")
}

private fun secondStar(lines: List<String>) {
    val sum = lines.findSum(::withSmudge)

    println("Second ⭐: $sum")
}

private fun List<String>.findSum(foundCondition: (pattern, pattern, Int) -> Boolean): Long {
    val sum = this.splitOnEmpty().sumOf { line ->
        val splitLines = line.map { it.toList() }

        val horizontal = splitLines.transpose().findReflection(foundCondition) * 100
        if (horizontal > 0) return@sumOf horizontal

        val vertical = splitLines.findReflection(foundCondition)
        vertical
    }
    return sum
}

private fun pattern.findReflection(foundCondition: (pattern, pattern, Int) -> Boolean): Long {
    repeat(first().size) { index ->
        val firstPart = this.map { it.subList(0, index + 1).reversed() }
        val secondPart = this.map { it.subList(index + 1, it.size) }
        val length = minOf(firstPart.first().size, secondPart.first().size)

        if (
            length > 0 &&
            foundCondition(firstPart, secondPart, length)
        ) {
            return (index + 1).toLong()
        }
    }

    return 0L
}

private fun directMatch(firstPart: pattern, secondPart: pattern, length: Int): Boolean =
    firstPart.map { it.subList(0, length) } == secondPart.map { it.subList(0, length) }

private fun withSmudge(firstPart: pattern, secondPart: pattern, length: Int): Boolean {
    val difference = (0..<length).sumOf { column ->
        firstPart.indices.count { row -> firstPart[row][column] != secondPart[row][column] }
    }
    return difference == 1
}
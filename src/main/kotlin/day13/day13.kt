package day13

import utils.readInput
import utils.splitOnEmpty
import utils.transpose

fun main() {
    val lines = readInput("day13/input")

    firstStar(lines)
    secondStar(lines)
}

private fun firstStar(lines: List<String>) {
    val sum = lines.splitOnEmpty().sumOf { line ->
        val vertical = line.map { it.toList() }.findReflection()
        if(vertical > 0) return@sumOf vertical

        val horizontal = line.map { it.toList() }.transpose().findReflection() * 100
        horizontal
    }

    println("First  ⭐: $sum")
}

private fun secondStar(lines: List<String>) {

    println("Second ⭐: ")
}

private fun List<List<Char>>.findReflection(): Long {
    repeat(first().size) { index ->
        val firstPart = this.map { it.subList(0, index + 1).reversed() }
        val secondPart = this.map { it.subList(index + 1, it.size) }
        val length = minOf(firstPart.first().size, secondPart.first().size)
        if (
            length > 0 && firstPart.map { it.subList(0, length) } == secondPart.map { it.subList(0, length) }
        ) {
            return (index + 1).toLong()
        }
    }
    return 0L
}
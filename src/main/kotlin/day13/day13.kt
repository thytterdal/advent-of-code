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
        (line.findReflection()) + line.map { it.toList() }.transpose().map { it.joinToString("") }
            .findReflection() * 100
    }

    println("First  ⭐: $sum")
}

private fun secondStar(lines: List<String>) {

    println("Second ⭐: ")
}

private fun List<String>.findReflection(): Long {
    for (string in this) {
        repeat(string.length) {
            val firstPart = string.substring(0, it + 1).reversed()
            val secondPart = string.substring(it + 1, string.length)
            val length = minOf(firstPart.length, secondPart.length)
            if (firstPart.substring(0, length) == secondPart.substring(0, length)) return (it +1).toLong()
        }
    }
    return 0L
}
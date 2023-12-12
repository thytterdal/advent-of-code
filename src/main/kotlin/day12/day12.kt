package day12

import utils.numbers
import utils.readInput

fun main() {
    val lines = readInput("day12/input")

    firstStar(lines)
    secondStar(lines)
}

private fun firstStar(lines: List<String>) {
    val sum = lines.sumOf {
        val (text, numbers) = it.split(" ")

        findPermutations(text.toList(), numbers.numbers())
    }

    println("First  ⭐: $sum")
}

private fun secondStar(lines: List<String>) {
    val sum = lines.sumOf {
        val (text, numbers) = it.split(" ")
        val fullText = ("$text?").repeat(5).dropLast(1).toList()
        val allNumbers = List(5) { numbers.numbers() }.flatten()

        findPermutations(fullText, allNumbers)
    }
    println("Second ⭐: $sum")
}

val lruCache = mutableMapOf<Pair<List<Char>, List<Int>>, Long>()

private fun findPermutations(
    line: List<Char>,
    springFormations: List<Int>
): Long {
    if (line.isEmpty()) {
        return if (springFormations.isEmpty()) 1L else 0L
    }

    return when (line.first()) {
        '.' -> findPermutations(line.drop(1), springFormations)
        '#' -> permutations(line, springFormations).also { lruCache[line to springFormations] = it }
        '?' -> findPermutations(line.drop(1), springFormations) + permutations(
            line,
            springFormations
        ).also { lruCache[line to springFormations] = it }

        else -> throw Exception("Unexpected")
    }
}

private fun permutations(
    line: List<Char>,
    springFormations: List<Int>
): Long {
    lruCache[line to springFormations]?.let { return it }

    if (springFormations.isEmpty()) return 0L

    val springFormation = springFormations.first()
    if (line.size < springFormation) return 0L
    repeat(springFormation) {
        if (line[it] == '.') return 0L
    }
    if (line.size == springFormation) {
        if (springFormations.size == 1) return 1L
        return 0L
    }
    if (line[springFormation] == '#') return 0L
    return findPermutations(line.drop(springFormation + 1), springFormations.drop(1))
}
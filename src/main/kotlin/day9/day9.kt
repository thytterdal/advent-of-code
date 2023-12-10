package day9

import utils.readInput

fun main() {
    val lines = readInput("day9/input")
    val expandedLines = expandLines(lines)

    firstStar(expandedLines)
    secondStar(expandedLines)
}

fun firstStar(lines: List<List<List<Long>>>) {

    val sum = lines.sumOf { line ->
        line.fold(0L) { acc, l ->
            acc + l.last()
        }
    }

    println("First  ⭐: $sum")
}

fun secondStar(lines: List<List<List<Long>>>) {
    val sum = lines.sumOf { list ->
        list.fold(0L) { acc, l ->
            l.first() - acc
        }
    }
    println("Second ⭐: $sum")
}

fun expandLines(lines: List<String>): List<List<List<Long>>> {
    val numbers = lines.map { line -> line.split(" ").map { it.toLong() } }
    return numbers.map { list ->
        val lists = mutableListOf(list)
        var currentList = list
        while (!currentList.all { it == 0L }) {
            currentList = currentList.windowed(2).map { it.last() - it.first() }
            lists.add(currentList)
        }
        lists.reversed()
    }
}
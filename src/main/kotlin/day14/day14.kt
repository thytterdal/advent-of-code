package day14

import utils.readInput
import utils.transpose

fun main() {
    val lines = readInput("day14/input")

    firstStar(lines)
    secondStar(lines)
}

private fun firstStar(lines: List<String>) {
    val sum = 0
    tiltNorth(lines)

    println("First  ⭐: $sum")
}

private fun secondStar(lines: List<String>) {
    val sum = 0

    println("Second ⭐: $sum")
}

private fun tiltNorth(lines: List<String>) {
    val splitLines = lines.map { it.toList() }

    val horizontal = splitLines.applyWeight().transpose().sumOf {
        it.reversed()
            .foldIndexed(0L) { index, acc, char ->
                if (char == 'O') acc + index.toLong() + 1 else acc
            }
    }
    println(horizontal)
}

private fun List<List<Char>>.applyWeight(): List<List<Char>> {
    val rows = this.size
    val columns = this.first().size
    val transformed = List(rows) {
        MutableList(columns) { '.' }
    }

    repeat(columns) { column ->
        var index = 0
        repeat(rows) { row ->
            if (this[row][column] == 'O') {
                transformed[index++][column] = 'O'
            } else if (this[row][column] == '#') {
                transformed[row][column] = '#'
                index = row + 1
            }
        }
    }

    return transformed
}
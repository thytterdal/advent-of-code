package day11

import utils.PointL
import utils.readInput

fun main() {
    val lines = readInput("day11/input")

    firstStar(lines)
    secondStar(lines)
}

private fun firstStar(lines: List<String>) {
    val galaxies = parseGalaxies(lines, 2)
    val pairs = galaxies.findPairs()

    val sum = pairs.sumOf { it.first.distanceTo(it.second) } / 2

    println("First  ⭐: $sum")
}

private fun secondStar(lines: List<String>) {
    val galaxies = parseGalaxies(lines, 1000000)
    val pairs = galaxies.findPairs()

    val sum = pairs.sumOf { it.first.distanceTo(it.second) } / 2

    println("Second ⭐: $sum")
}

private fun parseGalaxies(lines: List<String>, expandBy: Long): List<PointL> {
    val rowsToExpand = lines.indices.filter { y -> '#' !in lines[y] }
    val colsToExpand = lines[0].indices.filter { col -> lines.none { it[col] == '#' } }

    return lines.flatMapIndexed { row, line ->
        line.mapIndexedNotNull { column, char ->
            if (char == '#')
                PointL(
                    x = column.toLong() + colsToExpand.count { it < column } * (expandBy - 1),
                    y = row.toLong() + rowsToExpand.count { it < row } * (expandBy - 1)
                )
            else null
        }
    }
}

fun List<PointL>.findPairs() = this.flatMap { a -> this.map { b -> a to b } }

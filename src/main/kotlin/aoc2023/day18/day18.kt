package aoc2023.day18

import utils.*
import kotlin.math.absoluteValue

fun main() {
    val lines = readInput("aoc2023/day18/input")

    firstStar(lines)
    secondStar(lines)
}

private fun firstStar(lines: List<String>) {
    val sum = lines
        .parseDigPlan()
        .calculateLagoonSize()

    println("First  ⭐: $sum")
}

private fun secondStar(lines: List<String>) {
    val sum = lines
        .parseDigPlanFromHexCode()
        .calculateLagoonSize()

    println("Second ⭐: $sum")
}

private fun List<DigInstruction>.calculateLagoonSize(): Long {
    val coordinates = this.runningFold(PointL(x = 0, y = 0)) { acc, digInstruction ->
        acc.move(digInstruction.distance, digInstruction.direction)
    }

    val trench = this.sumOf { it.distance }

    val area = coordinates
        .zipWithNext()
        .fold(0L) { acc, (firstPoint, secondPoint) ->
            acc + firstPoint.x * secondPoint.y - secondPoint.x * firstPoint.y
        }.absoluteValue / 2

    val sum = area + trench / 2 + 1

    return sum
}

private fun List<String>.parseDigPlan() =
    this.map { line ->
        line.split(" ").let { (direction, distance, _) ->
            DigInstruction(
                direction = when (direction) {
                    "U" -> Direction.Up
                    "R" -> Direction.Right
                    "L" -> Direction.Left
                    "D" -> Direction.Down
                    else -> throw Exception("Unexpected input")
                },
                distance = distance.toLong()
            )
        }
    }

private fun List<String>.parseDigPlanFromHexCode() =
    this.map { line ->
        line.split(" ").let { (_, _, color) ->
            DigInstruction(
                direction = when (color.dropLast(1).last()) {
                    '0' -> Direction.Right
                    '1' -> Direction.Down
                    '2' -> Direction.Left
                    '3' -> Direction.Up
                    else -> throw Exception("Unexpected input")
                },
                distance = color.drop(2).dropLast(2).toLong(16)
            )
        }
    }

private data class DigInstruction(
    val direction: Direction,
    val distance: Long
)
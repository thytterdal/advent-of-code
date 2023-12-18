package day18

import utils.*
import kotlin.math.abs

fun main() {
    val lines = readInput("day18/input")


    firstStar(lines)
    secondStar(lines)
}

private fun firstStar(lines: List<String>) {
    val digPlan = lines.parseDigPlan()

    val start = Point(x = 0, y = 0)

    val tempTunnel = mutableListOf(start)

    digPlan.forEach {digInstruction ->
        repeat(digInstruction.distance) {
            tempTunnel.add(tempTunnel.last() + digInstruction.direction)
        }
    }

    val minX = abs(tempTunnel.minOf { it.x })
    val minY = abs(tempTunnel.minOf { it.y })

    val tunnel = tempTunnel.map { point -> Point(x = point.x + minX, y = point.y + minY) }

    val map = List(tunnel.maxOf { it.y + 1 }) {
        List(tunnel.maxOf { it.x + 1}) {
            '.'
        }
    }

    var sum = digPlan.fold(0L) {acc, digInstruction ->
        acc + digInstruction.distance
    }

    map.forEachIndexed { row, line ->
        line.forEachIndexed { column, _ ->
            val point = Point(x = column, y = row)
            if (!tunnel.contains(point) && tunnel.pointInPolygon(point)) {
                sum++
            }
        }
    }

    println("First  ⭐: $sum")
}

private fun secondStar(lines: List<String>) {
    val sum = 0

    println("Second ⭐: $sum")
}

private fun List<String>.parseDigPlan() =
    this.map { line ->
        line.split(" ").let { (direction, distance, color) ->
            DigInstruction(
                direction = when (direction) {
                    "U" -> Direction.Up
                    "R" -> Direction.Right
                    "L" -> Direction.Left
                    "D" -> Direction.Down
                    else -> throw Exception("Unexpected input")
                },
                distance = distance.toInt(),
                color = color.substringAfter("(").substringBefore(")")
            )
        }
    }


private data class DigInstruction(
    val direction: Direction,
    val distance: Int,
    val color: String
)
package day2

import readInput
import kotlin.math.max

private const val MAX_RED = 12
private const val MAX_GREEN = 13
private const val MAX_BLUE = 14

fun main() {
    val lines = readInput("day2/input")

    first(lines)
    second(lines)
}

fun first(lines: List<String>) {
    val sum = lines.sumOf { line ->
        val (gameName, gameInput) = line.split(":")
        val gameId = gameName.replace("Game ", "").toInt()

        var isValid = true

        gameInput.split(";").forEach { round ->
            round.split(",").forEach {
                val (number, color) = it.trim().split(" ")
                when (color) {
                    "red" -> if (number.toInt() > MAX_RED) isValid = false
                    "green" -> if (number.toInt() > MAX_GREEN) isValid = false
                    "blue" -> if (number.toInt() > MAX_BLUE) isValid = false
                }
            }
        }

        if (isValid) gameId else 0
    }

    println("Part 1: $sum")
}

fun second(lines: List<String>) {
    val sum = lines.sumOf { line ->
        val gameInput = line.split(":")[1]

        var red = 0
        var green = 0
        var blue = 0

        gameInput.split(";").forEach { round ->
            round.split(",").forEach {
                val (number, color) = it.trim().split(" ")
                when (color) {
                    "red" -> red = max(number.toInt(), red)
                    "green" -> green = max(number.toInt(), green)
                    "blue" -> blue = max(number.toInt(), blue)
                }
            }
        }

        red * green * blue
    }

    println("Part 2: $sum")
}
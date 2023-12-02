package day2

import readInput
import kotlin.math.max

fun main() {
    val lines = readInput("day2/input")

    first(lines)
    second(lines)
}

private const val MAX_RED = 12
private const val MAX_GREEN = 13
private const val MAX_BLUE = 14

fun first(lines: List<String>) {
    val sum = lines.sumOf { line ->
        val (gameName, gameInput) = line.split(":")
        val gameId = gameName.replace("Game ", "").toInt()

        var isValid = true

        gameInput.split(";").forEach { round ->
            round.split(",").forEach {
                when {
                    it.endsWith("red") -> {
                        if (it.replace("red", "").trim().toInt() > MAX_RED) isValid = false
                    }

                    it.endsWith("green") -> {
                        if (it.replace("green", "").trim().toInt() > MAX_GREEN) isValid = false
                    }

                    it.endsWith("blue") -> {
                        if (it.replace("blue", "").trim().toInt() > MAX_BLUE) isValid = false
                    }
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
                when {
                    it.endsWith("red") -> {
                        red = max(it.replace("red", "").trim().toInt(), red)
                    }

                    it.endsWith("green") -> {
                        green = max(it.replace("green", "").trim().toInt(), green)
                    }

                    it.endsWith("blue") -> {
                        blue = max(it.replace("blue", "").trim().toInt(), blue)
                    }
                }
            }
        }

        red * green * blue
    }

    println("Part 2: $sum")
}
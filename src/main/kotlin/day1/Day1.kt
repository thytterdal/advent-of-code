package day1

import utils.readInput

fun main() {
    val lines = readInput("day1/input")
    print("First: ")
    first(lines)

    print("Second: ")
    second(lines)
}

val numbersAsText = mapOf(
    "0" to 0,
    "1" to 1,
    "2" to 2,
    "3" to 3,
    "4" to 4,
    "5" to 5,
    "6" to 6,
    "7" to 7,
    "8" to 8,
    "9" to 9,
    "one" to 1,
    "two" to 2,
    "three" to 3,
    "four" to 4,
    "five" to 5,
    "six" to 6,
    "seven" to 7,
    "eight" to 8,
    "nine" to 9
)

fun first(lines: List<String>) {
    val calibrationCode = lines.sumOf { line ->
        "${line.first { it.isDigit() }}${line.last { it.isDigit() }}".toInt()
    }
    println("The calibration code is: $calibrationCode")
}

fun second(lines: List<String>) {
    val calibrationCode = lines.sumOf { line ->
        val firstDigit = line.findAnyOf(numbersAsText.keys)?.second ?: "0"
        val secondDigit = line.findLastAnyOf(numbersAsText.keys)?.second ?: "0"
        "${numbersAsText[firstDigit]}${numbersAsText[secondDigit]}".toInt()
    }
    println("The calibration code is: $calibrationCode")
}
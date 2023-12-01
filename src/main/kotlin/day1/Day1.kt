package day1

import java.io.File
import java.io.InputStream

fun main(args: Array<String>) {
    val path = {}.javaClass.classLoader.getResource("day1.txt")?.path ?: throw Exception("File not found")
    print("First: ")
    first(path)

    print("Second: ")
    second(path)
}

val numbersAsText = arrayOf(
    "one",
    "two",
    "three",
    "four",
    "five",
    "six",
    "seven",
    "eight",
    "nine"
)

fun first(path: String) {
    val inputStream: InputStream = File(path).inputStream()
    var calibrationCode = 0

    inputStream.bufferedReader().forEachLine { line ->
        val lineValue = "${line.first { it.isDigit() }}${line.last { it.isDigit() }}".toInt()
        calibrationCode += lineValue
    }
    println("The calibration code is: $calibrationCode")
}

fun second(path: String) {
    val inputStream: InputStream = File(path).inputStream()
    var calibrationCode = 0

    inputStream.bufferedReader().forEachLine { line ->
        firstNumber(line)

        val lineValue = "${firstNumber(line)}${lastNumber(line)}".toInt()

        calibrationCode += lineValue
    }
    println("The calibration code is: $calibrationCode")
}

private fun firstNumber(line: String): Int {
    val firstDigit = line.first { it.isDigit() }
    val firstDigits = numbersAsText.mapIndexed { index, number ->
        val indexOfNumber = line.indexOf(number)
        if (indexOfNumber >= 0) {
            Pair(indexOfNumber, index + 1)
        } else {
            null
        }
    }.toMutableList()

    firstDigits.add(
        Pair(line.indexOfFirst { it == firstDigit }, firstDigit.digitToInt())
    )
    return firstDigits.filterNotNull().minBy { it.first }.second
}

private fun lastNumber(line: String): Int {
    val lastDigit = line.last { it.isDigit() }
    val lastDigits = numbersAsText.mapIndexed { index, number ->
        val indexOfNumber = line.lastIndexOf(number)
        if (indexOfNumber >= 0) {
            Pair(indexOfNumber, index + 1)
        } else {
            null
        }
    }.toMutableList()

    lastDigits.add(
        Pair(line.indexOfLast { it == lastDigit }, lastDigit.digitToInt())
    )
    return lastDigits.filterNotNull().maxBy { it.first }.second
}
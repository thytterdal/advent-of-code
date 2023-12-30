package aoc2023.day6

import utils.readInput

fun main() {
    val lines = readInput("aoc2023/day6/input")

    firstStar(lines)
    secondStar(lines)
}

fun firstStar(lines: List<String>) {
    val regex = """\d+""".toRegex()
    val first = regex.findAll(lines.first()).map { it.value.toLong() }.toList()
    val second = regex.findAll(lines.drop(1).first()).map { it.value.toLong() }.toList()

    val rounds =  first zip second
    val numberOfOptions = mutableListOf<Long>()


    for((time, distance) in rounds) {
        var options = 0L
        repeat(time.toInt()) {speed ->
            val movementTime = time - speed
            val distanceTraveled = speed * movementTime
            if (distanceTraveled > distance) {
                options++
            }
        }
        numberOfOptions.add(options)
    }
    println(numberOfOptions.reduce(Long::times))
}

fun secondStar(lines: List<String>) {
    val regex = """\d+""".toRegex()
    val time = regex.findAll(lines.first()).joinToString("") { it.value }.toLong()
    val distance = regex.findAll(lines.drop(1).first()).joinToString("") { it.value }.toLong()

    var options = 0L
    repeat(time.toInt()) {speed ->
        val movementTime = time - speed
        val distanceTraveled = speed * movementTime
        if (distanceTraveled > distance) {
            options++
        }
    }

    println(options)
}




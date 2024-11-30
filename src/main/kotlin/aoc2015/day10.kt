package aoc2015

import common.Challenge

private val aoc2015day10 = object : Challenge(year = 2015, day = 10) {
    override fun silverStar(lines: List<String>): Long {
        var input = lines.first()
        repeat(40) {
            input = lookAndSay(input)
        }
        return input.length.toLong()
    }

    override fun goldStar(lines: List<String>): Long {
        var input = lines.first()
        repeat(50) {
            input = lookAndSay(input)
        }
        return input.length.toLong()
    }
}

private fun lookAndSay(numbers: String): String {
    val letters = mutableListOf<Pair<Char, Int>>()
    var letter = numbers.first()
    var count = 0
    numbers.forEach { char ->
        if (char == letter) {
            count++
        } else {
            letters.add(letter to count)
            count = 1
            letter = char
        }
    }
    letters.add(letter to count)
    return letters.joinToString(separator = "") { "${it.second}${it.first}" }
}

fun main() = aoc2015day10.run()
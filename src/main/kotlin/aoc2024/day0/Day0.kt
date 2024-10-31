package aoc2024.day0

import common.Challenge

fun main() = aoc2024day0.run()

private val aoc2024day0 = object : Challenge(year = 2024, day = 0) {
    override fun silverStar(lines: List<String>): Long {
        val validNumbers = lines.map { line ->
            val check = line.last().longValue()
            val sum = line.reversed().drop(1).chunked(2).fold(0L) { acc, s ->
                val first = s.first().longValue()
                val second = if (s.length > 1) s.last().longValue() else 0L
                val multiplied = first * 2
                val multipliedString = multiplied.toString()
                acc + if (multipliedString.length > 1) {
                    multipliedString.first().longValue() + multipliedString.last().longValue()
                } else {
                    multiplied
                } + second
            }

            val mod = sum % 10
            if (10 - mod == check) line.toLong() else 0L
        }

        return validNumbers.sum()
    }

    override fun goldStar(lines: List<String>): Long {
        return -1L
    }
}

private fun Char.longValue() = toString().toLong()
package aoc2024.day0

import common.Challenge

fun main() = aoc2024day0.run()


private val aoc2024day0 = object : Challenge(year = 2024, day = 0) {
    override fun silverStar(lines: List<String>): Long {
        val test = lines.map { line ->
            val check = line.last().toString().toLong()
            val sum = line.reversed().drop(1).chunked(2).fold(0L) { acc, s ->
                val first = s.first().toString().toLong()
                val second = if(s.length > 1) s.last().toString().toLong() else 0L
                val multiplied = first * 2
                val multipliedString = multiplied.toString()
                acc + if (multipliedString.length > 1) {
                    multipliedString.first().toString().toLong() + multipliedString.last().toString().toLong()
                } else {
                    multiplied
                } + second
            }

            val mod = sum % 10
            if(10 - mod == check) line.toLong() else 0L
        }

        return test.sum()
    }

    override fun goldStar(lines: List<String>): Long {
        return -1L
    }
}

package aoc2025

import common.Challenge

private val aoc2025day2 = object : Challenge(year = 2025, day = 2) {
    override fun silverStar(lines: List<String>): Long {
        val ranges = lines.flatMap { line ->
            line.split(",").map { range ->
                range.split("-").let { it.first().toLong()..it.last().toLong() }
            }
        }

        return ranges.fold(emptyList<Long>()) { acc, range ->
            val matches = mutableListOf<Long>()
            for (i in range) {
                val str = i.toString()
                val first = str.take(str.length / 2)
                val secondHalf = str.substring(str.length / 2)
                if (first == secondHalf) {
                    matches.add(i)
                }
            }
            acc + matches
        }.sum()
    }

    override fun goldStar(lines: List<String>): Long {
        val ranges = lines.flatMap { line ->
            line.split(",").map { range ->
                range.split("-").let { it.first().toLong()..it.last().toLong() }
            }
        }

        return ranges.fold(emptySet<Long>()) { acc, range ->
            acc + range.filter { i ->
                val str = i.toString().toCharArray().toList()
                val sizeRange = 1..str.size / 2
                sizeRange.any { length ->
                    str.chunked(length).all { it == str.take(length) }
                }
            }
        }.sum()
    }
}


fun main() = aoc2025day2.run()


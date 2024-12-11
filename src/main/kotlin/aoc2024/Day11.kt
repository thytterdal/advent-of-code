package aoc2024

import common.Challenge

private val cache = hashMapOf<Pair<Long, Int>, Long>()
private val aoc2024day11 = object : Challenge(year = 2024, day = 11) {
    override fun silverStar(lines: List<String>): Long {
        val numbers = lines.first().split(" ").map { it.toLong() }
        return numbers.sumOf { it.blink(25) }
    }

    override fun goldStar(lines: List<String>): Long {
        val numbers = lines.first().split(" ").map { it.toLong() }
        return numbers.sumOf { it.blink(75) }
    }
}

fun Long.blink(times: Int): Long {
    if (times == 0) return 1L
    cache[this to times]?.let { return it }
    return this.applyRules().sumOf { it.blink(times - 1) }.also { cache[this to times] = it }
}

fun Long.applyRules(): List<Long> {
    return if (this == 0L) {
        listOf(1L)
    } else if (this.toString().length % 2 == 0) {
        val string = this.toString()
        val middleIndex = string.length / 2
        listOf(string.take(middleIndex).toLong(), string.substring(middleIndex).toLong())
    } else {
        listOf(this * 2024)
    }
}

fun main() = aoc2024day11.run()
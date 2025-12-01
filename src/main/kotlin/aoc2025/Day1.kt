package aoc2025

import common.Challenge

private val aoc2025day1 = object : Challenge(year = 2025, day = 1) {
    override fun silverStar(lines: List<String>): Long {
        var dial = 50

        return lines.count { line ->
            val direction = if (line.first() == 'R') 1 else -1
            dial += (line.drop(1).toInt() * direction)
            dial %= 100
            if(dial < 0) dial += 100
            dial == 0
        }.toLong()
    }

    override fun goldStar(lines: List<String>): Long {
        var count = 0L
        var dial = 50

        lines.forEach { line ->
            val direction = if (line.first() == 'R') 1 else -1
            val clicks = line.drop(1).toInt()
            repeat(clicks) {
                dial += direction
                dial %= 100
                if(dial < 0) dial += 100
                if (dial == 0) {
                    count++
                }
            }
        }
        return count
    }
}


fun main() = aoc2025day1.run()


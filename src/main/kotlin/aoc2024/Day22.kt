package aoc2024

import common.Challenge

private val aoc2024day22 = object : Challenge(year = 2024, day = 22) {
    override fun silverStar(lines: List<String>): Long {
        return lines.sumOf { line ->
            generatePrices(line.toLong()).take(2001).last()
        }
    }

    override fun goldStar(lines: List<String>): Long {
        val grouped = lines.map { line ->
            generatePrices(line.toLong())
                .take(2001)
                .map { it % 10 }
                .zipWithNext { a, b -> b to b - a }
                .windowed(4) { window ->
                    window.unzip().second to window.unzip().first.last()
                }
                .groupBy({ it.first }, { it.second })
                .mapValues { it.value.first() }
        }

        return grouped.flatMapTo(mutableSetOf()) {
            it.keys
        }.map { sequence ->
            sequence to grouped.sumOf { group -> group[sequence] ?: 0L }
        }.maxOf { it.second }
    }

    fun generatePrices(start: Long) = generateSequence(start) { secret ->
        val first = mixAndPrune(secret * 64, secret)
        val second = mixAndPrune(first.floorDiv(32), first)
        mixAndPrune(second * 2048, second)
    }

    fun mixAndPrune(input: Long, secret: Long): Long {
        return (input xor secret) % 16777216
    }
}


fun main() = aoc2024day22.run()
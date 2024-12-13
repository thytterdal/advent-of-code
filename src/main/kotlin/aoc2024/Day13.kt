package aoc2024

import common.Challenge
import utils.splitOnEmptyLine

private val aoc2024day13 = object : Challenge(year = 2024, day = 13) {
    override fun silverStar(lines: List<String>): Long {
        return lines.parseClawMachines(0L).sumOf {
            val (a, b) = it.movesToPrize()
            a * 3 + b
        }
    }

    override fun goldStar(lines: List<String>): Long {
        return lines.parseClawMachines(10000000000000L).sumOf {
            val (a, b) = it.movesToPrize()
            a * 3 + b
        }
    }
}

private fun List<String>.parseClawMachines(offset: Long): List<ClawMachine> {
    val buttonRegex = Regex("""X\+(\d+), Y\+(\d+)""")
    val priceRegex = Regex("""X=(\d+), Y=(\d+)""")
    return splitOnEmptyLine().map { machineDefinition ->
        val buttonA = buttonRegex.find(machineDefinition[0])!!.groupValues.drop(1).map { it.toLong() }
        val buttonB = buttonRegex.find(machineDefinition[1])!!.groupValues.drop(1).map { it.toLong() }
        val prize = priceRegex.find(machineDefinition[2])!!.groupValues.drop(1).map { it.toLong() }
        ClawMachine(
            ax = buttonA.first(),
            ay = buttonA.last(),
            bx = buttonB.first(),
            by = buttonB.last(),
            pricex = prize.first() + offset,
            prizey = prize.last() + offset
        )
    }
}

private data class ClawMachine(
    val ax: Long,
    val ay: Long,
    val bx: Long,
    val by: Long,
    val pricex: Long,
    val prizey: Long
) {
    fun movesToPrize(): Pair<Long, Long> {
        val b = (ax * prizey - ay * pricex) / (ax * by - bx * ay)
        val a = (pricex - bx * b) / ax
        val result = (ax * a + bx * b) to (ay * a + by * b)
        return if (result.first == pricex && result.second == prizey) {
            a to b
        } else {
            0L to 0L
        }
    }
}

fun main() = aoc2024day13.run()
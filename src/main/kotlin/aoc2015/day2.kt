package aoc2015

import common.Challenge

fun main() = aoc2015day2.run()


private val aoc2015day2 = object : Challenge(year = 2015, day = 2) {
    override fun silverStar(lines: List<String>): Long {
        return lines.parseDimensions().sumOf(Present::calculateRequiredWrappingPaper)
    }

    override fun goldStar(lines: List<String>): Long {
        return lines.parseDimensions().sumOf(Present::calculateRibbonLength)
    }
}

private fun List<String>.parseDimensions() = map { line ->
    line.split("x").map(String::toLong).let {
        Present(length = it[0], width = it[1], height = it[2])
    }
}

private data class Present(
    val length: Long,
    val width: Long,
    val height: Long
) {
    private val side1
        get() = length * width
    private val side2
        get() = width * height
    private val side3
        get() = height * length

    private val volume
        get() = length * height * width

    fun calculateRequiredWrappingPaper(): Long {
        val minSide = minOf(side1, side2, side3)
        return side1 * 2 + side2 * 2 + side3 * 2 + minSide
    }

    fun calculateRibbonLength(): Long {
        val minPerimeter = listOf(length, height, width).sorted().take(2).let { it.first() * 2 + it.last() * 2 }
        return minPerimeter + volume
    }
}
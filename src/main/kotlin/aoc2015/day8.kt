package aoc2015

import common.Challenge

private val aoc2015day8 = object : Challenge(year = 2015, day = 8) {
    override fun silverStar(lines: List<String>): Long {
        val quotesRegex = """\\"""".toRegex()
        val slashRegex = """\\\\""".toRegex()
        val hexRegex = """\\x[0-9a-f]{2}""".toRegex()
        return lines.sumOf { line ->
            line.length - line
                .drop(1)
                .dropLast(1)
                .replace(quotesRegex, "?")
                .replace(slashRegex, "?")
                .replace(hexRegex, "?")
                .length
        }.toLong()
    }

    override fun goldStar(lines: List<String>): Long {
        val quotesRegex = "\"".toRegex()
        val slashRegex = """\\""".toRegex()
        return  lines.sumOf { line ->
            line
                .replace(slashRegex, """\\\\""")
                .replace(quotesRegex, """\\"""")
                .let { """"$it"""" }
                .length - line.length
        }.toLong()
    }
}

fun main() = aoc2015day8.run()
package aoc2025


import common.Challenge

private val aoc2025day11 = object : Challenge(year = 2025, day = 11) {
    val cache = hashMapOf<Triple<String, Boolean, Boolean>, Long>()

    override fun silverStar(lines: List<String>): Long {
        return lines.findConnections().followDataFlow("you")
    }

    override fun goldStar(lines: List<String>): Long {
        return lines.findConnections().followDataFlow("svr")
    }

    fun Map<String, List<String>>.followDataFlow(key: String, fft: Boolean = true, dac: Boolean = true): Long {
        cache[Triple(key, fft, dac)]?.let { return it }
        return this[key]!!.sumOf { target ->
            if(target == "out") {
                if(dac && fft) 1 else 0
            } else {
                this.followDataFlow(key = target, fft = fft || target == "fft", dac = dac || target == "dac")
            }
        }.also { cache[Triple(key, fft, dac)] = it }
    }

    fun List<String>.findConnections() = associate { line ->
        val key = line.substringBefore(":")
        val values = line.substringAfter(": ").split(" ")
        key to values
    }
}

fun main() = aoc2025day11.run()
package aoc2024

import common.Challenge

private val aoc2024day23 = object : Challenge(year = 2024, day = 23) {
    override fun silverStar(lines: List<String>): Long {
        val connectionsTo = lines
            .flatMap { line -> line.split("-").let { listOf(it[0] to it[1], it[1] to it[0]) } }
            .groupBy({ it.first }, { it.second })
            .mapValues { it.value.distinct() }
        val computerSets = mutableSetOf<Set<String>>()

        for ((computer, connections) in connectionsTo) {
            val pairs = connections.flatMap { a -> connections.map { b -> a to b } }
            pairs.forEach { (a, b) ->
                if (a in connectionsTo.getValue(b)) computerSets.add(setOf(a, b, computer))
            }
        }

        return computerSets.count { set -> set.any { it.startsWith("t") } }.toLong()
    }

    override fun goldStar(lines: List<String>): Long {
        val connectionsTo = lines
            .flatMap { line -> line.split("-").let { listOf(it[0] to it[1], it[1] to it[0]) } }
            .groupBy({ it.first }, { it.second })
            .mapValues { it.value.toSet() }
        val computerSets = mutableSetOf<Set<String>>()

        for ((currentComputer, connections) in connectionsTo) {
            var intersect = connections + currentComputer
            for (computer in connections) {
                val computersConnections = connectionsTo.getValue(computer)
                val next = intersect.intersect(computersConnections + computer)
                if (next.size > 2) intersect = next
            }
            computerSets.add(intersect)
        }
        val result = computerSets.maxBy { it.size }.sorted()
        println(result.joinToString(","))

        return 0L
    }
}


fun main() = aoc2024day23.run()
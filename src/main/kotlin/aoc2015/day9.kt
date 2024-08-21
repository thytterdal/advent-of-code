package aoc2015

import common.Challenge
import utils.permutations

private val aoc2015day9 = object : Challenge(year = 2015, day = 9) {
    private val inputRegex = """([a-zA-Z]+) to ([a-zA-Z]+) = (\d+)""".toRegex()

    override fun silverStar(lines: List<String>): Long {

        val distances = distanceBetweenLocations(lines)
        val permutations = distances.flatMap { listOf(it.from, it.to) }.distinct().permutations()

        return permutations.minOf { permutation ->
            permutation.windowed(2).fold(0L) { acc, cities ->
                acc + distances.first {
                    (it.from == cities[0] && it.to == cities[1]) || (it.from == cities[1] && it.to == cities[0])
                }.distance
            }
        }
    }

    override fun goldStar(lines: List<String>): Long {
        val distances = distanceBetweenLocations(lines)
        val permutations = distances.flatMap { listOf(it.from, it.to) }.distinct().permutations()

        return permutations.maxOf { permutation ->
            permutation.windowed(2).fold(0L) { acc, cities ->
                acc + distances.first {
                    (it.from == cities[0] && it.to == cities[1]) || (it.from == cities[1] && it.to == cities[0])
                }.distance
            }
        }
    }

    private fun distanceBetweenLocations(lines: List<String>) = lines.flatMap { line ->
        inputRegex.findAll(line).map {
            DistanceBetweenLocations(
                from = it.groupValues[1],
                to = it.groupValues[2],
                distance = it.groupValues[3].toLong()
            )
        }
    }
}


fun main() = aoc2015day9.run()

private data class DistanceBetweenLocations(val from: String, val to: String, val distance: Long)
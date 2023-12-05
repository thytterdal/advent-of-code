package day5

import binarySearch
import readInput
import kotlin.math.min

fun main() {
    val lines = readInput("day5/input")

    firstStar(lines)
    secondStar(lines)
}

fun firstStar(lines: List<String>) {
    val seeds = findSeeds(lines.first())
    val rangeMappings = findRanges(lines)

    val minLocation = seeds.minOf { rangeMappings.process(it) }

    println(minLocation)
}


fun secondStar(lines: List<String>) {
    val seedRanges = findSeedRanges(lines.first())
    val rangeMappings = findRanges(lines)

    var minLocation =
        seedRanges.minOf { min(rangeMappings.process(it.first), rangeMappings.process(it.last)) }

    var inverted = true
    while (true) {
        minLocation = binarySearch(lowerBound = 0, upperBound = minLocation - 1, inverted = inverted) { value ->
            seedRanges.any { range ->
                val seedValue = rangeMappings.processInverse(value)
                range.contains(seedValue) && rangeMappings.process(seedValue) == value
            }
        } ?: break
        inverted = !inverted
    }

    println(minLocation)
}


fun findSeeds(seedList: String): List<Long> {
    val regex = """\d+""".toRegex()
    return regex.findAll(seedList).map { it.value.toLong() }.toList()
}

fun findSeedRanges(seedList: String): List<LongRange> {
    val seeds = findSeeds(seedList)
    return seeds.chunked(2).map { (start, length) -> LongRange(start, start + length - 1) }
}


fun findRanges(lines: List<String>): List<List<Mapping>> =
    lines.drop(2).joinToString("\n").split("\n\n").map { section ->
        section.lines().drop(1).map {
            val (destination, source, length) = it.split(" ").map { num -> num.toLong() }
            val sourceRange = LongRange(source, source + length)
            val destRange = LongRange(destination, destination + length)
            Mapping(source = sourceRange, dest = destRange)
        }
    }


data class Mapping(
    val source: LongRange,
    val dest: LongRange
) {
    fun mappedValue(value: Long): Long? {
        return if (value in source) dest.first + (value - source.first) else null
    }

    fun mappedValueInverse(value: Long): Long? {
        return if (value in dest) source.first + (value - dest.first) else null
    }
}

fun List<Mapping>.mappedValue(value: Long): Long {
    return this.firstNotNullOfOrNull { it.mappedValue(value) } ?: value
}

fun List<Mapping>.mappedValueInverse(value: Long): Long {
    return this.firstNotNullOfOrNull { it.mappedValueInverse(value) } ?: value
}

fun List<List<Mapping>>.process(value: Long): Long {
    var result = value
    for (mapping in this) {
        result = mapping.mappedValue(result)
    }
    return result
}


fun List<List<Mapping>>.processInverse(value: Long): Long {
    var result = value
    for (mapping in this.reversed()) {
        result = mapping.mappedValueInverse(result)
    }
    return result
}
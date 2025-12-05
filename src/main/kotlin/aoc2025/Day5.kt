package aoc2025

import common.Challenge
import utils.splitOnEmptyLine

private val aoc2025day5 = object : Challenge(year = 2025, day = 5) {
    override fun silverStar(lines: List<String>): Long {
        val (ranges, ingredients) = lines.splitOnEmptyLine().let { (ranges, ingredients) ->
            ranges.map(String::toRange) to ingredients.map { it.toLong() }
        }
        return ingredients.count { ingredient ->
            ranges.any { ingredient in it }
        }.toLong()
    }

    override fun goldStar(lines: List<String>): Long {
        val processed = mutableListOf<LongRange>()

        val ranges = lines.takeWhile { it.isNotEmpty() }
            .map(String::toRange)
            .sortedBy { it.first }

        ranges.forEach { range ->
            var overlaps = processed.filter { range.first in it || range.last in it }

            if(overlaps.isNotEmpty()) {
                while (true) {
                    val n = overlaps.size
                    val overlap = overlaps.first()

                    processed.remove(overlap)
                    processed.add(mergeRanges(range, overlap))

                    overlaps = processed.filter { range.first in it || range.last in it }
                    if(n == overlaps.size) break
                }
            } else {
                processed.add(range)
            }
        }

        return processed.sumOf {
            it.last - it.first + 1
        }
    }

    private fun mergeRanges(first: LongRange, second: LongRange): LongRange {
        val start = minOf(first.first, second.first)
        val end = maxOf(first.last, second.last)
        return start..end
    }

}

private fun String.toRange(): LongRange {
    val (first, second) = split("-").map { it.toLong() }
    return first..second
}


fun main() = aoc2025day5.run()
package aoc2024

import common.Challenge

private val aoc2024day9 = object : Challenge(year = 2024, day = 9) {
    override fun silverStar(lines: List<String>): Long {
        val numbers = lines.first().map { it.toString().toInt() }
        var id = 0L
        val diskMap = buildList {
            numbers.chunked(2) { nums ->
                repeat(nums.first()) {
                    add("$id")
                }
                repeat(nums.last()) {
                    add(".")
                }
                id++
            }
        }

        var index = 0
        val reversedNumbers = diskMap.filter { it != "." }.reversed()

        val fragmented = buildList {
            (0..reversedNumbers.lastIndex).forEach { idx ->
                val char = diskMap[idx]
                add(
                    if (char == ".") {
                        val num = reversedNumbers[index]
                        index++
                        num
                    } else {
                        char
                    }
                )
            }
        }

        return fragmented.foldIndexed(0L) { idx, acc, c ->
            acc + idx * c.toLong()
        }
    }

    override fun goldStar(lines: List<String>): Long {
        val numbers = lines.first().map { it.toString().toInt() }
        var id = 0L
        val diskMap = buildList {
            numbers.chunked(2) { nums ->
                repeat(nums.first()) {
                    add("$id")
                }
                repeat(nums.last()) {
                    add(".")
                }

                id++
            }
        }.toMutableList()


        val grouped = diskMap.filter { !it.startsWith(".") && it.isNotEmpty() }.groupBy { it }
            .toSortedMap(compareByDescending { it.toLong() })

        grouped.forEach { (key, values) ->
            val currentLocation = diskMap.indexOfFirst { it == key }
            val location = diskMap
                .asSequence()
                .withIndex()
                .windowed(values.size)
                .takeWhile { it.first().index < currentLocation }
                .firstOrNull { window -> window.all { it.value == "." } }
                ?.first()
                ?.index
            if (location != null && location < currentLocation) {
                val first = diskMap.indexOfFirst { it == key }
                repeat(values.size) { index ->
                    diskMap[location + index] = diskMap[first + index].also {
                        diskMap[first + index] = diskMap[location + index]
                    }
                }
            }
        }

        return diskMap.foldIndexed(0L) { idx, acc, c ->
            acc + idx * if (c.startsWith(".")) 0L else c.toLong()
        }
    }

}

fun main() = aoc2024day9.run()
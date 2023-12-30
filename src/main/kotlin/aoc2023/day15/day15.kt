package aoc2023.day15

import utils.readInput

fun main() {
    val lines = readInput("aoc2023/day15/input")

    firstStar(lines)
    secondStar(lines)
}

private fun firstStar(lines: List<String>) {
    val steps = lines.first().split(",")
    val sum = steps.sumOf {
        it.fold(0L) { acc, char ->
            ((acc + char.code) * 17) % 256
        }
    }

    println("First  ⭐: $sum")
}

private fun secondStar(lines: List<String>) {
    val map = buildMap<Int, MutableMap<String, Long>> {
        repeat(256) {
            this[it] = mutableMapOf()
        }
    }
    val regex = """\d+""".toRegex()

    val steps = lines.first().split(",")

    steps.forEach { step ->
        val add = step.contains("=")
        val key = if (add) step.substringBefore("=") else step.substringBefore("-")
        val keyValue = key.fold(0) { acc, char ->
            ((acc + char.code) * 17) % 256
        }

        if (add) {
            val number = regex.find(step)?.value?.toLong() ?: throw Exception("Invalid number")
            map[keyValue]?.set(key, number)
        } else {
            map[keyValue]?.remove(key)
        }
    }

    val sum = map.entries.sumOf { (boxKey, box) ->
        box.values.foldIndexed(0L) { index, acc, focalStrength ->
            acc + (boxKey + 1) * (index + 1) * focalStrength
        }
    }

    println("Second ⭐: $sum")
}
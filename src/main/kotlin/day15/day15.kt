package day15

import utils.readInput

fun main() {
    val lines = readInput("day15/input")

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
    val map = buildMap<Int, MutableList<Pair<String, Long>>> {
        repeat(256) {
            this[it] = mutableListOf()
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
            if (map[keyValue]?.any { it.first == key } == true) {
                val index = map[keyValue]!!.indexOfFirst { it.first == key }
                map[keyValue]?.removeAll { it.first == key }
                map[keyValue]?.add(index, Pair(key, number))
            } else {
                map[keyValue]?.add(Pair(key, number))
            }
        } else {
            map[keyValue]?.removeAll { it.first == key }
        }
    }

    val sum = map.entries.sumOf { (boxKey, boxValue) ->
        boxValue.foldIndexed(0L) { index, acc, pair ->
            acc + (boxKey + 1) * (index + 1) * pair.second
        }
    }

    println("Second ⭐: $sum")
}
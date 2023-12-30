package aoc2023.day8

import utils.leastCommonMultiple
import utils.readInput

fun main() {
    val lines = readInput("aoc2023/day8/input")

    firstStar(lines)
    secondStar(lines)
}

fun firstStar(lines: List<String>) {
    val nodeMap = parseInput(lines)
    val directions = lines.first()

    val steps = nodeMap.findNumberOfSteps("AAA", directions) {
        it == "ZZZ"
    }

    println("First  ⭐: $steps")
}

fun secondStar(lines: List<String>) {
    val nodeMap = parseInput(lines)
    val directions = lines.first()

    val totalSteps = nodeMap.keys
        .filter { it.endsWith('A') }
        .fold(1L) { acc, startNode ->
            leastCommonMultiple(
                acc,
                nodeMap.findNumberOfSteps(startNode, directions) {
                    it.endsWith('Z')
                }
            )
        }

    println("Second ⭐: $totalSteps")
}

fun parseInput(lines: List<String>): Map<String, Node> {
    val regex = """[A-Z]{3}""".toRegex()
    val nodeLines = lines.drop(1).filterNot { it.isBlank() }
    return nodeLines.associate { line ->
        val (key, left, right) = regex.findAll(line).map { it.value }.toList()
        key to Node(left = left, right = right)
    }
}

fun Map<String, Node>.findNumberOfSteps(
    startNode: String,
    directions: String,
    endCondition: (String) -> Boolean
): Long {
    var steps = 0L
    var lastNode = startNode

    while (!endCondition(lastNode)) {
        val direction = directions[(steps % directions.length).toInt()]
        val node = this[lastNode]

        lastNode = if (direction == 'R') {
            node?.right ?: throw Exception("Node not found")
        } else {
            node?.left ?: throw Exception("Node not found")
        }

        steps++
    }

    return steps
}

data class Node(
    val left: String,
    val right: String
)
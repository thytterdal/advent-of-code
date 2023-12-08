package day8

import readInput

fun main() {
    val lines = readInput("day8/input")

    firstStar(lines)
    secondStar(lines)
}

fun firstStar(lines: List<String>) {
    val nodeMap = parseInput(lines)
    val directions = lines.first()
    var steps = 0
    var lastNode = "AAA"

    while (lastNode != "ZZZ") {

        val direction = directions[steps % directions.length]
        val node = nodeMap[lastNode]

        lastNode = if (direction == 'R') {
            node?.right ?: throw Exception("Node not found")
        } else {
            node?.left ?: throw Exception("Node not found")
        }

        steps++
    }

    println("First star: $steps")
}

fun secondStar(lines: List<String>) {
    val nodeMap = parseInput(lines)
    val directions = lines.first()
    var steps = 0L
    var nodes = nodeMap.keys.filter { it.endsWith('A') }
    val stepMap = mutableMapOf<String, Long>()

    while (nodes.isNotEmpty()) {
        val direction = directions[(steps % directions.length).toInt()]
        nodes = nodes.mapNotNull { lastNode ->
            val node = nodeMap[lastNode]
            val nextNode = if (direction == 'R') {
                node?.right ?: throw Exception("Node not found")
            } else {
                node?.left ?: throw Exception("Node not found")
            }
            if (nextNode.endsWith('Z')) {
                stepMap[nextNode] = steps + 1
                null
            } else nextNode
        }

        steps++
    }

    val totalSteps = stepMap.values.fold(1L) { acc, stepCount -> leastCommonMultiple(acc, stepCount)}
    println(totalSteps)
}

fun parseInput(lines: List<String>): Map<String, Node> {
    val nodeLines = lines.drop(1).filterNot { it.isBlank() }
    return nodeLines.associate { line ->
        val (key, left, right) = line
            .replace("(", "")
            .replace(")", "")
            .split("=", ",")

        key.trim() to Node(left = left.trim(), right = right.trim())
    }
}

fun leastCommonMultiple(n1: Long, n2: Long): Long {
    var gcd = 1L

    var i = 1L
    while (i <= n1 && i <= n2) {
        if (n1 % i == 0L && n2 % i == 0L)
            gcd = i
        ++i
    }

    return n1 * n2 / gcd
}

data class Node(
    val left: String,
    val right: String
)
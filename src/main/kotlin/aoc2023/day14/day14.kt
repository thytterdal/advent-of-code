package aoc2023.day14

import utils.readInput

typealias platform = List<List<Char>>

fun main() {
    val lines = readInput("aoc2023/day14/input")

    firstStar(lines)
    secondStar(lines)
}

private fun firstStar(lines: List<String>) {
    val sum = tiltNorth(lines)

    println("First  ⭐: $sum")
}

private fun secondStar(lines: List<String>) {
    val numCycles = 1000000000
    val splitLines = lines.map { it.toList() }

    val cycles = splitLines.findRepeatingCycles()

    val cyclesAfterStart = numCycles - cycles.second
    val remainingCycles = cyclesAfterStart % cycles.first.size

    val finalState = cycles.first[remainingCycles]
    val sum = finalState.foldIndexed(0L) { index, acc, chars ->
        acc + (splitLines.size - index) * chars.count { it == 'O' }
    }

    println("Second ⭐: $sum")
}

private fun tiltNorth(lines: List<String>): Long {
    val splitLines = lines.map { it.toList() }

    val sum = splitLines.applyWeight(Direction.NORTH)
        .foldIndexed(0L) { index, acc, chars ->
            acc + (splitLines.size - index) * chars.count { it == 'O' }
        }
    return sum
}

private fun List<List<Char>>.findRepeatingCycles(): Pair<MutableList<platform>, Int> {
    val results = mutableMapOf<platform, platform>()
    val cycle = mutableListOf<platform>()

    var temp = this
    var index = 0
    while (!results.contains(temp)) {
        temp = temp.doCycle().also {
            results[temp] = it
        }
        index++
    }

    while (!cycle.contains(temp)) {
        temp = temp.doCycle().also { cycle.add(temp) }
    }

    return Pair(cycle, index - cycle.size)
}

private fun platform.doCycle(): platform {
    return this
        .applyWeight(Direction.NORTH)
        .applyWeight(Direction.WEST)
        .applyWeight(Direction.SOUTH)
        .applyWeight(Direction.EAST)
}

private fun platform.applyWeight(direction: Direction): platform {

    val rows = this.size
    val columns = this.first().size
    val transformed = List(rows) {
        MutableList(columns) { '.' }
    }

    when (direction) {
        Direction.NORTH -> {
            repeat(columns) { column ->
                var index = 0
                repeat(rows) { row ->
                    if (this[row][column] == 'O') {
                        transformed[index++][column] = 'O'
                    } else if (this[row][column] == '#') {
                        transformed[row][column] = '#'
                        index = row + 1
                    }
                }
            }
        }

        Direction.WEST -> {
            repeat(rows) { row ->
                var index = 0
                repeat(columns) { column ->
                    if (this[row][column] == 'O') {
                        transformed[row][index++] = 'O'
                    } else if (this[row][column] == '#') {
                        transformed[row][column] = '#'
                        index = column + 1
                    }
                }
            }
        }

        Direction.SOUTH -> {
            repeat(columns) { column ->
                var index = rows - 1
                repeat(rows) { row ->
                    if (this[rows - row - 1][column] == 'O') {
                        transformed[index--][column] = 'O'
                    } else if (this[rows - row - 1][column] == '#') {
                        transformed[rows - row - 1][column] = '#'
                        index = rows - row - 2
                    }
                }
            }
        }

        Direction.EAST -> {
            repeat(rows) { row ->
                var index = columns - 1
                repeat(columns) { column ->
                    if (this[row][columns - column - 1] == 'O') {
                        transformed[row][index--] = 'O'
                    } else if (this[row][columns - column - 1] == '#') {
                        transformed[row][columns - column - 1] = '#'
                        index = columns - column - 2
                    }
                }
            }
        }
    }

    return transformed
}

enum class Direction {
    NORTH,
    WEST,
    SOUTH,
    EAST
}
package aoc2024

import common.Challenge
import utils.*

private val aoc2024day15 = object : Challenge(year = 2024, day = 15) {
    override fun silverStar(lines: List<String>): Long {
        val (rawGrid, rawInstructions) = lines.splitOnEmptyLine()
        val grid = rawGrid.toGrid().toMutableMap()
        val instructions = rawInstructions.joinToString("")

        val startPosition = grid.filterValues { it == '@' }.keys.first()

        instructions.fold(startPosition) { acc, instruction ->
            grid.moveRobot(acc, instruction.toDirection())
        }

        return grid.filterValues { it == 'O' }.keys.sumOf { crate ->
            crate.y * 100L + crate.x
        }
    }

    override fun goldStar(lines: List<String>): Long {
        val (rawGrid, rawInstructions) = lines.splitOnEmptyLine()
        val grid = rawGrid.scaleMap().toGrid().toMutableMap()
        val instructions = rawInstructions.joinToString("")

        val startPosition = grid.filterValues { it == '@' }.keys.first()
        instructions.fold(startPosition) { acc, instruction ->
            grid.moveRobotScaled(acc, instruction.toDirection())
        }

        return grid.filterValues { it == '[' }.keys.sumOf { crate ->
            crate.y * 100L + crate.x
        }
    }
}

fun List<String>.scaleMap() = map { line ->
    line.replace(".", "..").replace("@", "@.").replace("#", "##").replace("O", "[]")
}

fun MutableMap<Point, Char>.moveRobotScaled(position: Point, direction: Direction): Point {
    val newPosition = position.move(1, direction)
    val valueAtNewPosition = this[newPosition]
    if (valueAtNewPosition == '.') {
        this[position] = '.'
        this[newPosition] = '@'
        return newPosition
    } else if (valueAtNewPosition == ']' || valueAtNewPosition == '[') {
        when (direction) {
            Direction.Down,
            Direction.Up -> {
                val points = if (valueAtNewPosition == ']') {
                    listOf(listOf(newPosition.move(1, Direction.Left), newPosition))
                } else {
                    listOf(listOf(newPosition, newPosition.move(1, Direction.Right)))
                }
                val otherLevels = this.checkVertical(points, direction)
                if (otherLevels.last().none { this[it] == '#' }) {
                    otherLevels.reversed().windowed(2, partialWindows = true).forEach { levels ->
                        levels.first().forEach { point ->
                            val valueAbove = point.move(1, direction.inverse()).let {
                                if (it in levels.last()) {
                                    this[it] ?: throw IllegalAccessException()
                                } else {
                                    '.'
                                }
                            }
                            this[point] = valueAbove
                        }
                    }
                    this[newPosition] = '@'
                    this[position] = '.'
                    return newPosition
                }
            }

            Direction.Left,
            Direction.Right -> {
                var nextPosition = newPosition.move(1, direction)
                while (this[nextPosition] != '#') {
                    if (this[nextPosition] == '.') {
                        nextPosition.xRangeTo(newPosition, direction.inverse()).forEach { pos ->
                            this[pos] = when (this[pos]) {
                                ']' -> '['
                                '[' -> ']'
                                '.' -> if (direction == Direction.Right) ']' else '['
                                else -> throw java.lang.IllegalArgumentException(this[pos].toString())
                            }
                        }
                        this[newPosition] = '@'
                        this[position] = '.'
                        return newPosition
                    } else {
                        nextPosition = nextPosition.move(1, direction)
                    }
                }
            }

            else -> throw IllegalArgumentException()
        }
    }
    return position
}

private fun Map<Point, Char>.checkVertical(points: List<List<Point>>, direction: Direction): List<List<Point>> {
    val latestList = points.last().filter { this[it] != '.' }
    val expanded = expandInDirection(latestList, direction).toList()
    val allLists = buildList {
        addAll(points)
        add(expanded)
    }
    return if (expanded.all { this[it] == '.' } || expanded.any { this[it] == '#' }) {
        allLists
    } else {
        this.checkVertical(allLists, direction)
    }
}

private fun Map<Point, Char>.expandInDirection(points: List<Point>, direction: Direction) = buildSet {
    points.map { it.move(1, direction) }.forEach {
        when (this@expandInDirection[it]) {
            ']' -> {
                add(it)
                add(it.move(1, Direction.Left))
            }

            '[' -> {
                add(it)
                add(it.move(1, Direction.Right))
            }

            else -> add(it)
        }
    }
}

fun MutableMap<Point, Char>.moveRobot(position: Point, direction: Direction): Point {
    val newPosition = position.move(1, direction)
    val valueAtNewPosition = this[newPosition]
    if (valueAtNewPosition == '.') {
        this[position] = '.'
        this[newPosition] = '@'
        return newPosition
    } else if (valueAtNewPosition == 'O') {
        var nextPosition = newPosition.move(1, direction)
        while (this[nextPosition] != '#') {
            if (this[nextPosition] == '.') {
                this[nextPosition] = 'O'
                this[newPosition] = '@'
                this[position] = '.'
                return newPosition
            } else {
                nextPosition = nextPosition.move(1, direction)
            }
        }
    }
    return position
}


fun main() = aoc2024day15.run()
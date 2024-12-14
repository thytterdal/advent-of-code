package aoc2024

import common.Challenge
import utils.Distance
import utils.Point

private val aoc2024day14 = object : Challenge(year = 2024, day = 14) {
    val bounds = 101 to 103

    override fun silverStar(lines: List<String>): Long {
        val robots = lines.parseRobots()

        val moved = robots.map { robot ->
            (1..100).fold(robot) { acc, _ ->
                acc.move(bounds)
            }
        }
        val halfWidth = (bounds.first / 2)
        val halfHeight = (bounds.second / 2)

        val quadrants = listOf(
            0..<halfWidth to 0..<halfHeight,
            (halfWidth + 1)..bounds.first to (halfHeight + 1)..bounds.second,
            0..<halfWidth to (halfHeight + 1)..bounds.second,
            (halfWidth + 1)..bounds.first to 0..<halfHeight,
        )

        return quadrants.map { quadrant ->
            moved.count { robot -> robot.pos.x in quadrant.first && robot.pos.y in quadrant.second }
        }.reduce(Int::times).toLong()
    }

    override fun goldStar(lines: List<String>): Long {
        var robots = lines.parseRobots()
        var seconds = 0L

        while (robots.groupBy { it.pos }.any { it.value.size > 1 }) {
            robots = robots.map { robot ->
                robot.move(bounds)
            }
            seconds++
        }
        robots.printRobots(bounds)

        return seconds
    }
}

private fun List<Robot>.printRobots(bounds: Pair<Int, Int>) {
    val grouped = groupBy(keySelector = { it.pos }, valueTransform = { it.pos })
    repeat(bounds.first) { y ->
        println(
            (0..<bounds.second).map { x ->
                grouped[Point(x, y)]?.size ?: "."
            }.joinToString("")
        )
    }
}

private fun List<String>.parseRobots() = map { line ->
    val (pos, vel) = line.split(" ").map { it.substringAfter("=").split(",").map(String::toInt) }
    Robot(
        pos = Point(pos[0], pos[1]),
        velocity = Distance(vel[0], vel[1])
    )
}


private data class Robot(
    val pos: Point,
    val velocity: Distance
) {
    fun move(bounds: Pair<Int, Int>) = copy(
        pos = Point(
            x = (pos.x + velocity.x + bounds.first) % bounds.first,
            y = (pos.y + velocity.y + bounds.second) % bounds.second
        )
    )
}

fun main() = aoc2024day14.run()
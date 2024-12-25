package aoc2024

import common.Challenge
import utils.Point
import utils.longs
import kotlin.math.abs
import kotlin.math.min

typealias KeyPad = Map<Char, Point>
typealias CacheKey = Triple<Char, Char, Int>

private val aoc2024day21 = object : Challenge(year = 2024, day = 21) {

    val cache = hashMapOf<CacheKey, Long>()

    override fun silverStar(lines: List<String>): Long {
        val keyPads = (0..<2).fold(listOf(numberPad)) { acc, _ ->
            acc + directionalPad
        }
        return lines
            .map {
                it to shortestPathManyRobots(it, keyPads)
            }
            .sumOf { it.first.longs().first() * it.second }
    }

    override fun goldStar(lines: List<String>): Long {
        val keyPads = (0..<25).fold(listOf(numberPad)) { acc, _ ->
            acc + directionalPad
        }
        return lines
            .map {
                it to shortestPathManyRobots(it, keyPads)
            }
            .sumOf { it.first.longs().first() * it.second }
    }

    fun shortestPathManyRobots(code: String, keyPads: List<KeyPad>): Long {
        if (keyPads.isEmpty()) return code.length.toLong()

        var currentKey = 'A'

        return code.sumOf { nextKey ->
            val sum = shortestPathDirectionalPad(currentKey, nextKey, keyPads)
            currentKey = nextKey
            sum
        }
    }

    fun shortestPathDirectionalPad(current: Char, next: Char, keyPads: List<KeyPad>) =
        cache.getOrPut(CacheKey(current, next, keyPads.size)) {
            val keypad = keyPads.first()
            val currentPos = keypad.getValue(current)
            val nextPos = keypad.getValue(next)

            val difference = currentPos - nextPos

            val vertical = buildString { repeat(if (difference.y < 0) "^" else "v", abs(difference.y)) }
            val horizontal = buildString { repeat(if (difference.x < 0) "<" else ">", abs(difference.x)) }

            var cost = Long.MAX_VALUE

            val space = keypad.getValue(' ')

            if (space != Point(currentPos.x, nextPos.y)) {
                cost = min(cost, shortestPathManyRobots("$vertical${horizontal}A", keyPads.drop(1)))
            }
            if (space != Point(nextPos.x, currentPos.y)) {
                cost = min(cost, shortestPathManyRobots("$horizontal${vertical}A", keyPads.drop(1)))
            }
            cost
        }

    val numberPad = mapOf(
        '7' to Point(x = 0, y = 0),
        '8' to Point(x = 1, y = 0),
        '9' to Point(x = 2, y = 0),
        '4' to Point(x = 0, y = 1),
        '5' to Point(x = 1, y = 1),
        '6' to Point(x = 2, y = 1),
        '1' to Point(x = 0, y = 2),
        '2' to Point(x = 1, y = 2),
        '3' to Point(x = 2, y = 2),
        ' ' to Point(x = 0, y = 3),
        '0' to Point(x = 1, y = 3),
        'A' to Point(x = 2, y = 3)
    )

    val directionalPad = mapOf(
        '^' to Point(x = 1, y = 0),
        'A' to Point(x = 2, y = 0),
        '<' to Point(x = 0, y = 1),
        ' ' to Point(x = 0, y = 0),
        'v' to Point(x = 1, y = 1),
        '>' to Point(x = 2, y = 1),
    )
}

fun main() = aoc2024day21.run()
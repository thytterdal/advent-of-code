package aoc2024

import common.Challenge
import utils.splitOnEmptyLine
import utils.transpose

private val aoc2024day25 = object : Challenge(year = 2024, day = 25) {
    override fun silverStar(lines: List<String>): Long {
        val split = lines.splitOnEmptyLine()
        val locks = split.filter { lock -> lock.first().all { it == '#' } }.map { lock ->
            lock.map { it.toList() }.transpose().map { tumbler -> tumbler.count { it == '#' } - 1 }
        }
        val keys = split.filter { lock -> lock.last().all { it == '#' } }.map { lock ->
            lock.map { it.toList() }.transpose().map { tumbler -> tumbler.count { it == '#' } - 1 }
        }

        val pairs = locks.flatMap { a -> keys.map { b -> a to b } }

        return pairs.count { (lock, key) ->
            lock.zip(key).map { it.first + it.second }.all { it <= 5 }
        }.toLong()
    }

    override fun goldStar(lines: List<String>): Long {
        TODO("Not yet implemented")
    }

}

fun main() = aoc2024day25.run()
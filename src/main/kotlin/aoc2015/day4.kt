package aoc2015

import common.Challenge
import utils.md5

fun main() = aoc2015day4.run()

private val aoc2015day4 = object : Challenge(year = 2015, day = 4) {
    override fun silverStar(lines: List<String>): Long {
        var number = 0L
        val secretKey = lines.first()
        while (!"$secretKey$number".md5().startsWith("00000")){
            number++
        }
        return number
    }

    override fun goldStar(lines: List<String>): Long {
        var number = 0L
        val secretKey = lines.first()
        while (!"$secretKey$number".md5().startsWith("000000")){
            number++
        }
        return number
    }

}
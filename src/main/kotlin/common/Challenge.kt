package common

import kotlin.io.path.Path
import kotlin.io.path.readLines

abstract class Challenge(private val year: Int, private val day: Int) {
    abstract fun silverStar(lines: List<String>): Long
    abstract fun goldStar(lines: List<String>): Long

    fun run() {
        val input = Path("src/main/resources/input/$year/$day.txt").readLines()

        println("Silver  ⭐: ${silverStar(input)}")
        println("Gold    ⭐: ${goldStar(input)}")
    }
}
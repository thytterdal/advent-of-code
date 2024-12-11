package common

import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.time.measureTimedValue

abstract class Challenge(private val year: Int, private val day: Int) {
    abstract fun silverStar(lines: List<String>): Long
    abstract fun goldStar(lines: List<String>): Long

    fun run() {
        val input = Path("src/main/resources/input/$year/$day.txt").readLines()

        try {
            val silver = measureTimedValue {
                silverStar(input)
            }
            println("Silver  ⭐: ${silver.value}".padEnd(40, ' ') + "${silver.duration}")
            val gold = measureTimedValue {
                goldStar(input)
            }
            println("Gold    ⭐: ${gold.value}".padEnd(40, ' ') + "${gold.duration}")
        } catch (e: NotImplementedError) {
            println("Not implemented")
        }
    }
}